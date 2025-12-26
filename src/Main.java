
import core.Dataset;
import core.Instance;
import core.Attribute;
import core.DataAnalyzer;
import core.Classifier;
import core.AlgorithmSelector;
import core.AlgorithmSelector.AlgorithmType;
import core.ResultsFormatter;
import evaluation.ConfusionMatrix;
import evaluation.Evaluator;
import io.CSVLoader;
import ui.SummaryPrinter;
import java.util.*;

/**
 * MAIN CLASS (ENHANCED WITH MULTIPLE ALGORITHMS)
 * 
 * Purpose: Complete ML pipeline with algorithm selection
 * Think of it like: A comprehensive ML laboratory where you can
 * choose different algorithms and compare their performance
 * 
 * New Features:
 * - Multiple algorithm support (KNN, Decision Tree, Naive Bayes, Logistic Regression)
 * - Algorithm comparison and selection
 * - Professional result formatting
 * - Detailed performance analysis
 */

public class Main {

    public static void main(String[] args) {
        System.out.println("🚀 YOTA ML Engine - Enhanced Multi-Algorithm Version");
        System.out.println("═".repeat(65));
        
        // ===== STEP 1: LOAD DATA =====
        System.out.println("📂 Loading dataset...");
        
        String csvFile = "D:\\PROJECTS\\MARVEL\\java\\work_space\\yota\\sample_data.csv";
        Dataset dataset = CSVLoader.loadCSV(csvFile, "Employee Data");
        
        if (dataset.getNumInstances() == 0) {
            System.out.println("❌ No data loaded. Creating sample dataset...");
            dataset = createSampleDataset();
        }
        
        System.out.println("✅ Dataset loaded: " + dataset.toString());
        System.out.println();
        
        // ===== STEP 2: DATA ANALYSIS =====
        System.out.println("📊 Analyzing data...");
        DataAnalyzer analyzer = new DataAnalyzer(dataset);
        SummaryPrinter printer = new SummaryPrinter(analyzer);
        printer.printCompleteReport();
        
        // ===== STEP 3: ALGORITHM SELECTION MENU =====
        System.out.println("🤖 Available Machine Learning Algorithms:");
        System.out.println("═".repeat(50));
        
        List<AlgorithmType> availableAlgorithms = AlgorithmSelector.getAvailableAlgorithms();
        for (int i = 0; i < availableAlgorithms.size(); i++) {
            AlgorithmType alg = availableAlgorithms.get(i);
            System.out.println((i + 1) + ". " + alg.getDisplayName());
            System.out.println("   " + alg.getDescription());
        }
        
        // For demo, let's run all algorithms and compare
        System.out.println("\n🔬 Running all algorithms for comparison...");
        
        // ===== STEP 4: MULTI-ALGORITHM COMPARISON =====
        List<ResultsFormatter.ExperimentResult> comparisonResults = new ArrayList<>();
        
        for (AlgorithmType algorithmType : availableAlgorithms) {
            System.out.println("\n" + "─".repeat(50));
            System.out.println("Training: " + algorithmType.getDisplayName());
            System.out.println("─".repeat(50));
            
            // Create algorithm-specific parameters
            Map<String, Object> parameters = getDefaultParameters(algorithmType);
            Classifier classifier = AlgorithmSelector.createClassifier(algorithmType, parameters);
            
            // Measure training time
            long startTime = System.currentTimeMillis();
            
            // Split dataset for training and testing
            Dataset[] split = Evaluator.splitDataset(dataset, 0.7);
            Dataset trainSet = split[0];
            Dataset testSet = split[1];
            
            // Train the classifier
            classifier.train(trainSet);
            long trainingTime = System.currentTimeMillis() - startTime;
            
            // Measure prediction time
            startTime = System.currentTimeMillis();
            
            // Create confusion matrix and test
            ConfusionMatrix confusionMatrix = new ConfusionMatrix();
            for (int i = 0; i < testSet.getNumInstances(); i++) {
                Instance testInstance = testSet.getInstance(i);
                Object actualClass = testInstance.getClassValue();
                Object predictedClass = classifier.predict(testInstance);
                
                if (actualClass != null && predictedClass != null) {
                    confusionMatrix.addPrediction(actualClass, predictedClass);
                }
            }
            
            long predictionTime = System.currentTimeMillis() - startTime;
            double accuracy = confusionMatrix.getAccuracy();
            
            // Store results
            ResultsFormatter.ExperimentResult result = new ResultsFormatter.ExperimentResult(
                classifier.getAlgorithmName(), accuracy, trainingTime, predictionTime);
            comparisonResults.add(result);
            
            // Show quick results
            System.out.printf("✅ %s completed: %.2f%% accuracy in %d ms\n", 
                classifier.getAlgorithmName(), accuracy, trainingTime + predictionTime);
        }
        
        // ===== STEP 5: RESULTS COMPARISON =====
        System.out.println("\n" + "═".repeat(65));
        System.out.println("📈 ALGORITHM COMPARISON RESULTS");
        System.out.println("═".repeat(65));
        
        String comparisonReport = ResultsFormatter.formatAlgorithmComparison(comparisonResults);
        System.out.println(comparisonReport);
        
        // ===== STEP 6: DETAILED ANALYSIS OF BEST ALGORITHM =====
        // Find best performing algorithm
        ResultsFormatter.ExperimentResult bestResult = comparisonResults.stream()
            .max(Comparator.comparing(r -> r.accuracy))
            .orElse(comparisonResults.get(0));
        
        System.out.println("🏆 DETAILED ANALYSIS - BEST ALGORITHM");
        System.out.println("═".repeat(65));
        
        // Re-run best algorithm for detailed analysis
        AlgorithmType bestAlgorithmType = findAlgorithmType(bestResult.algorithmName);
        Map<String, Object> bestParameters = getDefaultParameters(bestAlgorithmType);
        Classifier bestClassifier = AlgorithmSelector.createClassifier(bestAlgorithmType, bestParameters);
        
        // Split and train again for detailed analysis
        Dataset[] split = Evaluator.splitDataset(dataset, 0.7);
        Dataset trainSet = split[0];
        Dataset testSet = split[1];
        
        long startTime = System.currentTimeMillis();
        bestClassifier.train(trainSet);
        long trainingTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        ConfusionMatrix detailedConfusionMatrix = new ConfusionMatrix();
        for (int i = 0; i < testSet.getNumInstances(); i++) {
            Instance testInstance = testSet.getInstance(i);
            Object actualClass = testInstance.getClassValue();
            Object predictedClass = bestClassifier.predict(testInstance);
            
            if (actualClass != null && predictedClass != null) {
                detailedConfusionMatrix.addPrediction(actualClass, predictedClass);
            }
        }
        long predictionTime = System.currentTimeMillis() - startTime;
        
        // Generate detailed report
        Map<String, Object> experimentDetails = new HashMap<>();
        experimentDetails.put("parameters", bestParameters);
        
        String detailedReport = ResultsFormatter.formatExperimentResults(
            dataset, bestClassifier, detailedConfusionMatrix, 
            trainingTime, predictionTime, experimentDetails);
        
        System.out.println(detailedReport);
        
        // ===== STEP 7: DEMO PREDICTIONS =====
        System.out.println("🎯 DEMO PREDICTIONS WITH BEST ALGORITHM");
        System.out.println("═".repeat(50));
        demonstratePredictions(bestClassifier, dataset);
        
        System.out.println("🎉 YOTA ML Engine Complete!");
        System.out.println("═".repeat(65));
    }
    
    // Method: Create a sample dataset if CSV file is not found
    private static Dataset createSampleDataset() {
        System.out.println("Creating synthetic employee dataset...");
        
        Dataset dataset = new Dataset("Sample Employee Data");
        
        // Add attributes
        dataset.addAttribute(new Attribute("Age", "numeric"));
        dataset.addAttribute(new Attribute("Salary", "numeric"));
        dataset.addAttribute(new Attribute("Experience", "numeric"));
        dataset.addAttribute(new Attribute("Hired", "categorical"));
        
        // Add sample data
        double[][] sampleData = {
            {25, 45000, 2, 1},   // Young, low salary, little experience → Not hired (0)
            {30, 65000, 5, 1},   // Mid-age, good salary, some experience → Hired (1)
            {35, 80000, 8, 1},   // Older, high salary, lots of experience → Hired (1)
            {22, 40000, 1, 0},   // Very young, low salary → Not hired (0)
            {28, 55000, 3, 1},   // Good profile → Hired (1)
            {45, 95000, 15, 1},  // Senior profile → Hired (1)
            {24, 42000, 1, 0},   // Junior profile → Not hired (0)
            {33, 70000, 7, 1},   // Experienced → Hired (1)
            {26, 48000, 2, 0},   // Average profile → Not hired (0)
            {38, 85000, 10, 1},  // Senior profile → Hired (1)
            {23, 41000, 1, 0},   // Junior → Not hired (0)
            {31, 62000, 5, 1},   // Good profile → Hired (1)
            {27, 50000, 3, 1},   // Decent profile → Hired (1)
            {29, 58000, 4, 1},   // Good profile → Hired (1)
            {36, 75000, 9, 1}    // Senior → Hired (1)
        };
        
        for (double[] row : sampleData) {
            Instance instance = new Instance();
            instance.addValue(row[0]); // Age
            instance.addValue(row[1]); // Salary  
            instance.addValue(row[2]); // Experience
            
            // Convert hired status to string
            String hiredStatus = (row[3] == 1.0) ? "Hired" : "NotHired";
            instance.addValue(hiredStatus);
            instance.setClassValue(hiredStatus);
            
            dataset.addInstance(instance);
        }
        
        System.out.println("✅ Created " + dataset.getNumInstances() + " sample records");
        return dataset;
    }
    
    /**
     * Get default parameters for each algorithm type
     */
    private static Map<String, Object> getDefaultParameters(AlgorithmType algorithmType) {
        Map<String, Object> parameters = new HashMap<>();
        
        switch (algorithmType) {
            case KNN:
                parameters.put("k", 3);
                break;
            case DECISION_TREE:
                parameters.put("maxDepth", 10);
                parameters.put("minSamplesLeaf", 2);
                break;
            case NAIVE_BAYES:
                // Naive Bayes has no hyperparameters
                break;
            case LOGISTIC_REGRESSION:
                parameters.put("learningRate", 0.01);
                parameters.put("maxIterations", 1000);
                break;
        }
        
        return parameters;
    }
    
    /**
     * Find algorithm type by name (for detailed analysis)
     */
    private static AlgorithmType findAlgorithmType(String algorithmName) {
        for (AlgorithmType type : AlgorithmType.values()) {
            if (algorithmName.contains(type.getDisplayName()) || 
                algorithmName.contains(type.name())) {
                return type;
            }
        }
        return AlgorithmType.KNN; // Default fallback
    }

    /**
     * Method: Demonstrate predictions on sample instances
     */
    private static void demonstratePredictions(Classifier classifier, Dataset dataset) {
        System.out.println("Making predictions on new candidates:");
        
        // Create test candidates
        double[][] testCandidates = {
            {26, 47000, 2},  // Young candidate
            {32, 68000, 6},  // Experienced candidate  
            {40, 90000, 12}  // Senior candidate
        };
        
        String[] candidateNames = {"Junior Candidate", "Mid-level Candidate", "Senior Candidate"};
        
        for (int i = 0; i < testCandidates.length; i++) {
            Instance testInstance = new Instance();
            testInstance.addValue(testCandidates[i][0]); // Age
            testInstance.addValue(testCandidates[i][1]); // Salary
            testInstance.addValue(testCandidates[i][2]); // Experience
            testInstance.addValue("Unknown"); // Placeholder for class
            
            Object prediction = classifier.predict(testInstance);
            
            System.out.printf("%s (Age: %.0f, Salary: $%.0f, Exp: %.0f years) → Prediction: %s%n",
                candidateNames[i], 
                testCandidates[i][0],
                testCandidates[i][1], 
                testCandidates[i][2],
                prediction
            );
        }
        System.out.println();
    }
}
