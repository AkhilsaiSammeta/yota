package com.yota.model;

import java.util.Map;

/**
 * ML RESULT MODEL
 * 
 * Purpose: Store machine learning results for web display
 * Think of it like: A report card showing how well the AI performed
 * 
 * Real-life analogy: Like test scores showing accuracy and detailed performance
 */
public class MLResult {
    
    private String algorithmName;
    private int kValue;
    private double accuracy;
    private double precision;
    private double recall;
    private Map<String, Map<String, Integer>> confusionMatrix;
    private int trainingInstances;
    private int testInstances;
    private String[] actualClasses;
    private String[] predictedClasses;
    
    // Default constructor
    public MLResult() {}
    
    // Constructor with basic info
    public MLResult(String algorithmName, int kValue, double accuracy) {
        this.algorithmName = algorithmName;
        this.kValue = kValue;
        this.accuracy = accuracy;
    }
    
    // Getters and Setters
    public String getAlgorithmName() { return algorithmName; }
    public void setAlgorithmName(String algorithmName) { this.algorithmName = algorithmName; }
    
    public int getkValue() { return kValue; }
    public void setkValue(int kValue) { this.kValue = kValue; }
    
    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
    
    public double getPrecision() { return precision; }
    public void setPrecision(double precision) { this.precision = precision; }
    
    public double getRecall() { return recall; }
    public void setRecall(double recall) { this.recall = recall; }
    
    public Map<String, Map<String, Integer>> getConfusionMatrix() { return confusionMatrix; }
    public void setConfusionMatrix(Map<String, Map<String, Integer>> confusionMatrix) { this.confusionMatrix = confusionMatrix; }
    
    public int getTrainingInstances() { return trainingInstances; }
    public void setTrainingInstances(int trainingInstances) { this.trainingInstances = trainingInstances; }
    
    public int getTestInstances() { return testInstances; }
    public void setTestInstances(int testInstances) { this.testInstances = testInstances; }
    
    public String[] getActualClasses() { return actualClasses; }
    public void setActualClasses(String[] actualClasses) { this.actualClasses = actualClasses; }
    
    public String[] getPredictedClasses() { return predictedClasses; }
    public void setPredictedClasses(String[] predictedClasses) { this.predictedClasses = predictedClasses; }
}