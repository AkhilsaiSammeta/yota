package com.yota.core;

import java.util.HashMap;

/**
 * CONFUSIONMATRIX CLASS (Spring Boot Version)
 * 
 * Purpose: Evaluate classifier performance using confusion matrix
 */
public class ConfusionMatrix {
    
    private HashMap<String, HashMap<String, Integer>> matrix;
    private HashMap<String, Integer> classLabels;
    private int totalPredictions;
    
    public ConfusionMatrix() {
        this.matrix = new HashMap<String, HashMap<String, Integer>>();
        this.classLabels = new HashMap<String, Integer>();
        this.totalPredictions = 0;
    }
    
    public void addPrediction(Object actualClass, Object predictedClass) {
        String actual = actualClass.toString();
        String predicted = predictedClass.toString();
        
        classLabels.put(actual, 1);
        classLabels.put(predicted, 1);
        
        if (!matrix.containsKey(actual)) {
            matrix.put(actual, new HashMap<String, Integer>());
        }
        
        HashMap<String, Integer> row = matrix.get(actual);
        
        if (row.containsKey(predicted)) {
            row.put(predicted, row.get(predicted) + 1);
        } else {
            row.put(predicted, 1);
        }
        
        totalPredictions++;
    }
    
    public double getAccuracy() {
        if (totalPredictions == 0) {
            return 0.0;
        }
        
        int correctPredictions = 0;
        
        for (String actualClass : matrix.keySet()) {
            HashMap<String, Integer> row = matrix.get(actualClass);
            if (row.containsKey(actualClass)) {
                correctPredictions += row.get(actualClass);
            }
        }
        
        return (double) correctPredictions / totalPredictions * 100.0;
    }
    
    public double getPrecision(String className) {
        int truePositives = 0;
        int falsePositives = 0;
        
        if (matrix.containsKey(className) && matrix.get(className).containsKey(className)) {
            truePositives = matrix.get(className).get(className);
        }
        
        for (String actualClass : matrix.keySet()) {
            if (!actualClass.equals(className)) {
                HashMap<String, Integer> row = matrix.get(actualClass);
                if (row.containsKey(className)) {
                    falsePositives += row.get(className);
                }
            }
        }
        
        int totalPredictedAsThisClass = truePositives + falsePositives;
        if (totalPredictedAsThisClass == 0) {
            return 0.0;
        }
        
        return (double) truePositives / totalPredictedAsThisClass * 100.0;
    }
    
    public double getRecall(String className) {
        if (!matrix.containsKey(className)) {
            return 0.0;
        }
        
        HashMap<String, Integer> row = matrix.get(className);
        int totalActualOfThisClass = 0;
        int truePositives = 0;
        
        for (Integer count : row.values()) {
            totalActualOfThisClass += count;
        }
        
        if (row.containsKey(className)) {
            truePositives = row.get(className);
        }
        
        if (totalActualOfThisClass == 0) {
            return 0.0;
        }
        
        return (double) truePositives / totalActualOfThisClass * 100.0;
    }
    
    public int getTotalPredictions() {
        return totalPredictions;
    }
    
    public HashMap<String, HashMap<String, Integer>> getMatrix() {
        return matrix;
    }
}