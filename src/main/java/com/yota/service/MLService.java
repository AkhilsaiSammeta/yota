package com.yota.service;

import com.yota.core.*;
import com.yota.model.AnalysisResult;
import com.yota.model.MLResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * ML SERVICE
 * 
 * Purpose: Bridge between web interface and ML engine
 * Think of it like: The coordinator that runs all ML operations
 * 
 * Real-life analogy: Like a project manager coordinating between different teams
 */
@Service
public class MLService {
    
    // Store current dataset and results
    private Dataset currentDataset;
    private DataAnalyzer currentAnalyzer;
    private AnalysisResult currentAnalysisResult;
    private MLResult currentMLResult;
    
    /**
     * Process uploaded CSV file
     */
    public AnalysisResult processUploadedFile(MultipartFile file) throws Exception {
        // Create dataset from uploaded file
        Dataset dataset = loadCSVFromMultipartFile(file);
        
        // Analyze the dataset
        return analyzeDataset(dataset);
    }
    
    /**
     * Load sample dataset
     */
    public AnalysisResult loadSampleDataset() {
        Dataset dataset = createSampleDataset();
        return analyzeDataset(dataset);
    }
    
    /**
     * Analyze dataset and create analysis result
     */
    private AnalysisResult analyzeDataset(Dataset dataset) {
        this.currentDataset = dataset;
        this.currentAnalyzer = new DataAnalyzer(dataset);
        
        // Create analysis result
        AnalysisResult result = new AnalysisResult(
            dataset.getName(),
            dataset.getNumInstances(),
            dataset.getNumAttributes()
        );
        
        // Calculate column statistics
        Map<String, AnalysisResult.ColumnStats> columnStats = new HashMap<>();
        Map<String, Map<String, Integer>> frequencyTables = new HashMap<>();
        List<String> missingValueColumns = new ArrayList<>();
        
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            Attribute attr = dataset.getAttribute(i);
            String columnName = attr.getName();
            String columnType = attr.getType();
            
            AnalysisResult.ColumnStats stats = new AnalysisResult.ColumnStats(columnName, columnType);
            
            if (columnType.equals("numeric")) {
                stats.setMin(currentAnalyzer.getMin(i));
                stats.setMax(currentAnalyzer.getMax(i));
                stats.setAverage(currentAnalyzer.getAverage(i));
            }
            
            stats.setUniqueValues(currentAnalyzer.getUniqueValueCount(i));
            columnStats.put(columnName, stats);
            
            // Frequency table for all columns
            HashMap<String, Integer> frequencies = currentAnalyzer.getFrequency(i);
            frequencyTables.put(columnName, frequencies);
            
            // Check for missing values
            if (currentAnalyzer.hasMissingValues(i)) {
                missingValueColumns.add(columnName);
            }
        }
        
        result.setColumnStatistics(columnStats);
        result.setFrequencyTables(frequencyTables);
        result.setMissingValueColumns(missingValueColumns);
        
        this.currentAnalysisResult = result;
        return result;
    }
    
    /**
     * Run KNN classification
     */
    public MLResult runKNNClassification(int kValue, double trainRatio) throws Exception {
        if (currentDataset == null) {
            throw new Exception("No dataset loaded");
        }
        
        // Split dataset
        Dataset[] split = splitDataset(currentDataset, trainRatio);
        Dataset trainSet = split[0];
        Dataset testSet = split[1];
        
        // Create and train KNN classifier
        KNNClassifier classifier = new KNNClassifier(kValue);
        classifier.train(trainSet);
        
        // Create confusion matrix
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        
        // Make predictions
        List<String> actualList = new ArrayList<>();
        List<String> predictedList = new ArrayList<>();
        
        for (int i = 0; i < testSet.getNumInstances(); i++) {
            Instance testInstance = testSet.getInstance(i);
            Object actualClass = testInstance.getClassValue();
            Object predictedClass = classifier.predict(testInstance);
            
            if (actualClass != null && predictedClass != null) {
                String actualStr = actualClass.toString();
                String predictedStr = predictedClass.toString();
                
                confusionMatrix.addPrediction(actualClass, predictedClass);
                actualList.add(actualStr);
                predictedList.add(predictedStr);
            }
        }
        
        // Create ML result
        MLResult result = new MLResult("K-Nearest Neighbors", kValue, confusionMatrix.getAccuracy());
        result.setTrainingInstances(trainSet.getNumInstances());
        result.setTestInstances(testSet.getNumInstances());
        result.setActualClasses(actualList.toArray(new String[0]));
        result.setPredictedClasses(predictedList.toArray(new String[0]));
        
        // Convert confusion matrix to web format
        Map<String, Map<String, Integer>> webConfusionMatrix = convertConfusionMatrix(confusionMatrix);
        result.setConfusionMatrix(webConfusionMatrix);
        
        this.currentMLResult = result;
        return result;
    }
    
    /**
     * Get visualization data
     */
    public Object getVisualizationData(String type) {
        if (currentAnalysisResult == null) {
            return null;
        }
        
        switch (type) {
            case "column-distribution":
                return createColumnDistributionData();
            case "class-distribution":
                return createClassDistributionData();
            case "correlation":
                return createCorrelationData();
            default:
                return null;
        }
    }
    
    // Getters
    public Dataset getCurrentDataset() { return currentDataset; }
    public AnalysisResult getCurrentAnalysisResult() { return currentAnalysisResult; }
    public MLResult getCurrentMLResult() { return currentMLResult; }
    
    // ===== HELPER METHODS =====
    
    /**
     * Load CSV from MultipartFile
     */
    private Dataset loadCSVFromMultipartFile(MultipartFile file) throws Exception {
        Dataset dataset = new Dataset(file.getOriginalFilename());
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        boolean firstLine = true;
        
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            String[] values = line.split(",");
            
            if (firstLine) {
                firstLine = false;
                // Create attributes
                for (int i = 0; i < values.length; i++) {
                    String header = values[i].trim();
                    String type = (i == values.length - 1) ? "categorical" : "numeric";
                    dataset.addAttribute(new Attribute(header, type));
                }
            } else {
                // Create instance
                Instance instance = new Instance();
                for (String value : values) {
                    Object objectValue = tryParseNumber(value.trim());
                    instance.addValue(objectValue);
                }
                
                if (values.length > 0) {
                    Object lastValue = tryParseNumber(values[values.length - 1].trim());
                    instance.setClassValue(lastValue);
                }
                
                dataset.addInstance(instance);
            }
        }
        
        reader.close();
        return dataset;
    }
    
    /**
     * Create sample dataset
     */
    private Dataset createSampleDataset() {
        Dataset dataset = new Dataset("Sample Employee Data");
        
        // Add attributes
        dataset.addAttribute(new Attribute("Age", "numeric"));
        dataset.addAttribute(new Attribute("Salary", "numeric"));
        dataset.addAttribute(new Attribute("Experience", "numeric"));
        dataset.addAttribute(new Attribute("Hired", "categorical"));
        
        // Add sample data
        double[][] sampleData = {
            {25, 45000, 2, 0}, {30, 65000, 5, 1}, {35, 80000, 8, 1}, {22, 40000, 1, 0},
            {28, 55000, 3, 1}, {45, 95000, 15, 1}, {24, 42000, 1, 0}, {33, 70000, 7, 1},
            {26, 48000, 2, 0}, {38, 85000, 10, 1}, {23, 41000, 1, 0}, {31, 62000, 5, 1},
            {27, 50000, 3, 1}, {29, 58000, 4, 1}, {36, 75000, 9, 1}, {21, 38000, 0, 0},
            {32, 67000, 6, 1}, {37, 78000, 9, 1}, {25, 46000, 2, 0}, {34, 72000, 7, 1}
        };
        
        for (double[] row : sampleData) {
            Instance instance = new Instance();
            instance.addValue(row[0]); // Age
            instance.addValue(row[1]); // Salary  
            instance.addValue(row[2]); // Experience
            
            String hiredStatus = (row[3] == 1.0) ? "Hired" : "NotHired";
            instance.addValue(hiredStatus);
            instance.setClassValue(hiredStatus);
            
            dataset.addInstance(instance);
        }
        
        return dataset;
    }
    
    private Object tryParseNumber(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private Dataset[] splitDataset(Dataset dataset, double trainRatio) {
        // Implementation similar to existing Evaluator class
        int totalInstances = dataset.getNumInstances();
        int trainSize = (int) (totalInstances * trainRatio);
        
        Dataset trainSet = new Dataset(dataset.getName() + " - Training");
        Dataset testSet = new Dataset(dataset.getName() + " - Test");
        
        // Copy attributes
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            trainSet.addAttribute(dataset.getAttribute(i));
            testSet.addAttribute(dataset.getAttribute(i));
        }
        
        // Split instances
        for (int i = 0; i < totalInstances; i++) {
            Instance instance = dataset.getInstance(i);
            if (i < trainSize) {
                trainSet.addInstance(instance);
            } else {
                testSet.addInstance(instance);
            }
        }
        
        return new Dataset[]{trainSet, testSet};
    }
    
    private Map<String, Map<String, Integer>> convertConfusionMatrix(ConfusionMatrix cm) {
        // This would need access to the internal matrix structure
        // For now, return a simple structure
        Map<String, Map<String, Integer>> result = new HashMap<>();
        // Implementation would depend on ConfusionMatrix internal structure
        return result;
    }
    
    private Object createColumnDistributionData() {
        // Create data for column distribution charts
        return currentAnalysisResult.getFrequencyTables();
    }
    
    private Object createClassDistributionData() {
        // Create data for class distribution pie chart
        if (currentDataset != null && currentDataset.getClassAttribute() != null) {
            String classAttrName = currentDataset.getClassAttribute().getName();
            return currentAnalysisResult.getFrequencyTables().get(classAttrName);
        }
        return null;
    }
    
    private Object createCorrelationData() {
        // Create correlation matrix data
        Map<String, Object> correlationData = new HashMap<>();
        correlationData.put("message", "Correlation analysis not implemented yet");
        return correlationData;
    }
}