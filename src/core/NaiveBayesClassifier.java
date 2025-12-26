package core;

import java.util.*;

/**
 * NAIVE BAYES CLASSIFIER
 * 
 * Purpose: Implement Naive Bayes algorithm from scratch
 * Think of it like: Making predictions based on probability and "naive" assumptions
 * 
 * Real-life analogy: Like predicting if someone will be hired based on
 * the probability of hiring people with similar characteristics
 * 
 * "Naive" because it assumes features are independent
 * (e.g., assumes age and salary don't affect each other)
 */
public class NaiveBayesClassifier {
    
    private Map<Object, Double> classProbabilities;
    private Map<Object, Map<Integer, GaussianStats>> featureStats;
    private Dataset trainingData;
    
    public NaiveBayesClassifier() {
        this.classProbabilities = new HashMap<>();
        this.featureStats = new HashMap<>();
    }
    
    /**
     * Train the Naive Bayes classifier
     */
    public void train(Dataset dataset) {
        this.trainingData = dataset;
        
        // Count class frequencies
        Map<Object, Integer> classCounts = new HashMap<>();
        int totalInstances = dataset.getNumInstances();
        
        for (int i = 0; i < totalInstances; i++) {
            Instance instance = dataset.getInstance(i);
            Object classValue = instance.getClassValue();
            classCounts.put(classValue, classCounts.getOrDefault(classValue, 0) + 1);
        }
        
        // Calculate class probabilities
        for (Map.Entry<Object, Integer> entry : classCounts.entrySet()) {
            Object classValue = entry.getKey();
            double probability = (double) entry.getValue() / totalInstances;
            classProbabilities.put(classValue, probability);
        }
        
        // Calculate feature statistics for each class
        for (Object classValue : classCounts.keySet()) {
            Map<Integer, GaussianStats> classFeatureStats = new HashMap<>();
            
            // For each numeric feature
            for (int attrIndex = 0; attrIndex < dataset.getNumAttributes() - 1; attrIndex++) {
                Attribute attr = dataset.getAttribute(attrIndex);
                
                if ("numeric".equals(attr.getType())) {
                    List<Double> values = new ArrayList<>();
                    
                    // Collect values for this feature and class
                    for (int i = 0; i < totalInstances; i++) {
                        Instance instance = dataset.getInstance(i);
                        if (instance.getClassValue().equals(classValue)) {
                            Object value = instance.getValue(attrIndex);
                            if (value instanceof Double) {
                                values.add((Double) value);
                            }
                        }
                    }
                    
                    // Calculate mean and standard deviation
                    if (!values.isEmpty()) {
                        GaussianStats stats = calculateGaussianStats(values);
                        classFeatureStats.put(attrIndex, stats);
                    }
                }
            }
            
            featureStats.put(classValue, classFeatureStats);
        }
    }
    
    /**
     * Make prediction for new instance
     */
    public Object predict(Instance instance) {
        if (classProbabilities.isEmpty()) {
            return null;
        }
        
        Object bestClass = null;
        double bestProbability = Double.NEGATIVE_INFINITY;
        
        // For each possible class
        for (Object classValue : classProbabilities.keySet()) {
            double logProbability = Math.log(classProbabilities.get(classValue));
            
            // Multiply by feature probabilities (add in log space)
            Map<Integer, GaussianStats> classStats = featureStats.get(classValue);
            
            for (int attrIndex = 0; attrIndex < trainingData.getNumAttributes() - 1; attrIndex++) {
                Object value = instance.getValue(attrIndex);
                
                if (value instanceof Double && classStats.containsKey(attrIndex)) {
                    double featureValue = (Double) value;
                    GaussianStats stats = classStats.get(attrIndex);
                    double probability = gaussianProbability(featureValue, stats.mean, stats.stdDev);
                    
                    // Add log probability (avoid underflow)
                    if (probability > 0) {
                        logProbability += Math.log(probability);
                    } else {
                        logProbability += Math.log(1e-10); // Small smoothing value
                    }
                }
            }
            
            if (logProbability > bestProbability) {
                bestProbability = logProbability;
                bestClass = classValue;
            }
        }
        
        return bestClass;
    }
    
    /**
     * Get prediction probabilities for all classes
     */
    public Map<Object, Double> getPredictionProbabilities(Instance instance) {
        Map<Object, Double> probabilities = new HashMap<>();
        
        if (classProbabilities.isEmpty()) {
            return probabilities;
        }
        
        double totalLogProb = Double.NEGATIVE_INFINITY;
        Map<Object, Double> logProbs = new HashMap<>();
        
        // Calculate log probabilities for each class
        for (Object classValue : classProbabilities.keySet()) {
            double logProbability = Math.log(classProbabilities.get(classValue));
            
            Map<Integer, GaussianStats> classStats = featureStats.get(classValue);
            
            for (int attrIndex = 0; attrIndex < trainingData.getNumAttributes() - 1; attrIndex++) {
                Object value = instance.getValue(attrIndex);
                
                if (value instanceof Double && classStats.containsKey(attrIndex)) {
                    double featureValue = (Double) value;
                    GaussianStats stats = classStats.get(attrIndex);
                    double probability = gaussianProbability(featureValue, stats.mean, stats.stdDev);
                    
                    if (probability > 0) {
                        logProbability += Math.log(probability);
                    } else {
                        logProbability += Math.log(1e-10);
                    }
                }
            }
            
            logProbs.put(classValue, logProbability);
            totalLogProb = logSumExp(totalLogProb, logProbability);
        }
        
        // Convert back to probabilities and normalize
        for (Map.Entry<Object, Double> entry : logProbs.entrySet()) {
            double normalizedProb = Math.exp(entry.getValue() - totalLogProb);
            probabilities.put(entry.getKey(), normalizedProb);
        }
        
        return probabilities;
    }
    
    /**
     * Calculate Gaussian probability density
     */
    private double gaussianProbability(double x, double mean, double stdDev) {
        if (stdDev == 0) {
            return x == mean ? 1.0 : 1e-10;
        }
        
        double exponent = -Math.pow(x - mean, 2) / (2 * Math.pow(stdDev, 2));
        double coefficient = 1.0 / (Math.sqrt(2 * Math.PI) * stdDev);
        return coefficient * Math.exp(exponent);
    }
    
    /**
     * Calculate mean and standard deviation for Gaussian distribution
     */
    private GaussianStats calculateGaussianStats(List<Double> values) {
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }
        double mean = sum / values.size();
        
        double variance = 0.0;
        for (Double value : values) {
            variance += Math.pow(value - mean, 2);
        }
        variance /= values.size();
        
        double stdDev = Math.sqrt(variance);
        if (stdDev == 0) {
            stdDev = 1e-10; // Avoid division by zero
        }
        
        return new GaussianStats(mean, stdDev);
    }
    
    /**
     * Log-sum-exp trick to avoid numerical underflow
     */
    private double logSumExp(double a, double b) {
        if (a == Double.NEGATIVE_INFINITY) return b;
        if (b == Double.NEGATIVE_INFINITY) return a;
        
        if (a > b) {
            return a + Math.log(1 + Math.exp(b - a));
        } else {
            return b + Math.log(1 + Math.exp(a - b));
        }
    }
    
    /**
     * Get model summary for display
     */
    public String getModelSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Naive Bayes Model Summary ===\n");
        
        sb.append("Class Probabilities:\n");
        for (Map.Entry<Object, Double> entry : classProbabilities.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ")
              .append(String.format("%.3f", entry.getValue())).append("\n");
        }
        
        sb.append("\nFeature Statistics by Class:\n");
        for (Object classValue : featureStats.keySet()) {
            sb.append("Class: ").append(classValue).append("\n");
            Map<Integer, GaussianStats> classStats = featureStats.get(classValue);
            
            for (Map.Entry<Integer, GaussianStats> entry : classStats.entrySet()) {
                int attrIndex = entry.getKey();
                GaussianStats stats = entry.getValue();
                String attrName = trainingData.getAttribute(attrIndex).getName();
                
                sb.append("  ").append(attrName)
                  .append(": μ=").append(String.format("%.2f", stats.mean))
                  .append(", σ=").append(String.format("%.2f", stats.stdDev)).append("\n");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Helper class to store Gaussian statistics
     */
    private static class GaussianStats {
        double mean;
        double stdDev;
        
        public GaussianStats(double mean, double stdDev) {
            this.mean = mean;
            this.stdDev = stdDev;
        }
    }
}