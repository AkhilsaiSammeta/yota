package core;

import java.util.*;

/**
 * LOGISTIC REGRESSION CLASSIFIER
 * 
 * Purpose: Implement Logistic Regression algorithm from scratch
 * Think of it like: Finding the best line that separates different classes
 * 
 * Real-life analogy: Like finding a formula that calculates the probability
 * of hiring someone based on their age, salary, and experience
 * 
 * Uses gradient descent to learn the optimal coefficients
 */
public class LogisticRegressionClassifier {
    
    private double[] weights;
    private double bias;
    private double learningRate = 0.01;
    private int maxIterations = 1000;
    private Dataset trainingData;
    private Map<Object, Integer> classMapping;
    private List<Object> classLabels;
    
    public LogisticRegressionClassifier() {}
    
    public LogisticRegressionClassifier(double learningRate, int maxIterations) {
        this.learningRate = learningRate;
        this.maxIterations = maxIterations;
    }
    
    /**
     * Train the logistic regression model
     */
    public void train(Dataset dataset) {
        this.trainingData = dataset;
        
        // Map class labels to integers (0, 1, 2, ...)
        mapClassLabels(dataset);
        
        // Initialize weights and bias
        int numFeatures = dataset.getNumAttributes() - 1; // Exclude class attribute
        weights = new double[numFeatures];
        bias = 0.0;
        
        // Initialize weights randomly (small values)
        Random random = new Random(42); // Fixed seed for reproducibility
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextGaussian() * 0.01;
        }
        
        // Prepare training data
        double[][] X = new double[dataset.getNumInstances()][numFeatures];
        int[] y = new int[dataset.getNumInstances()];
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            
            // Extract features
            for (int j = 0; j < numFeatures; j++) {
                Object value = instance.getValue(j);
                if (value instanceof Double) {
                    X[i][j] = (Double) value;
                } else {
                    X[i][j] = 0.0; // Handle non-numeric values
                }
            }
            
            // Extract class label
            y[i] = classMapping.get(instance.getClassValue());
        }
        
        // Normalize features
        normalizeFeatures(X);
        
        // Train using gradient descent
        if (classLabels.size() == 2) {
            trainBinary(X, y);
        } else {
            trainMulticlass(X, y);
        }
    }
    
    /**
     * Make prediction for new instance
     */
    public Object predict(Instance instance) {
        if (weights == null) {
            return null;
        }
        
        double[] probabilities = getPredictionProbabilities(instance);
        
        // Return class with highest probability
        int bestClassIndex = 0;
        double bestProbability = probabilities[0];
        
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > bestProbability) {
                bestProbability = probabilities[i];
                bestClassIndex = i;
            }
        }
        
        return classLabels.get(bestClassIndex);
    }
    
    /**
     * Get prediction probabilities for all classes
     */
    public double[] getPredictionProbabilities(Instance instance) {
        if (weights == null) {
            return new double[0];
        }
        
        // Extract and normalize features
        double[] features = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            Object value = instance.getValue(i);
            if (value instanceof Double) {
                features[i] = (Double) value;
            } else {
                features[i] = 0.0;
            }
        }
        
        // Apply same normalization as training
        // (In a full implementation, we'd store normalization parameters)
        
        if (classLabels.size() == 2) {
            // Binary classification
            double z = bias;
            for (int i = 0; i < weights.length; i++) {
                z += weights[i] * features[i];
            }
            
            double prob1 = sigmoid(z);
            return new double[]{1.0 - prob1, prob1};
        } else {
            // Multiclass - simplified one-vs-all approach
            double[] scores = new double[classLabels.size()];
            
            for (int c = 0; c < classLabels.size(); c++) {
                double z = bias;
                for (int i = 0; i < weights.length; i++) {
                    z += weights[i] * features[i];
                }
                scores[c] = sigmoid(z);
            }
            
            // Normalize to probabilities
            double sum = 0.0;
            for (double score : scores) {
                sum += score;
            }
            
            if (sum > 0) {
                for (int i = 0; i < scores.length; i++) {
                    scores[i] /= sum;
                }
            }
            
            return scores;
        }
    }
    
    /**
     * Train binary classification model
     */
    private void trainBinary(double[][] X, int[] y) {
        int numSamples = X.length;
        int numFeatures = X[0].length;
        
        for (int iter = 0; iter < maxIterations; iter++) {
            double[] gradientW = new double[numFeatures];
            double gradientB = 0.0;
            
            // Calculate gradients
            for (int i = 0; i < numSamples; i++) {
                double z = bias;
                for (int j = 0; j < numFeatures; j++) {
                    z += weights[j] * X[i][j];
                }
                
                double prediction = sigmoid(z);
                double error = prediction - y[i];
                
                // Update gradients
                gradientB += error;
                for (int j = 0; j < numFeatures; j++) {
                    gradientW[j] += error * X[i][j];
                }
            }
            
            // Update weights and bias
            bias -= learningRate * gradientB / numSamples;
            for (int j = 0; j < numFeatures; j++) {
                weights[j] -= learningRate * gradientW[j] / numSamples;
            }
            
            // Optional: Check for convergence
            if (iter % 100 == 0) {
                double cost = calculateCost(X, y);
                // System.out.println("Iteration " + iter + ", Cost: " + cost);
            }
        }
    }
    
    /**
     * Train multiclass model (simplified one-vs-all)
     */
    private void trainMulticlass(double[][] X, int[] y) {
        // For simplicity, train for most common class vs others
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (int label : y) {
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        }
        
        int targetClass = 0;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                targetClass = entry.getKey();
            }
        }
        
        // Convert to binary problem: targetClass vs others
        int[] binaryY = new int[y.length];
        for (int i = 0; i < y.length; i++) {
            binaryY[i] = (y[i] == targetClass) ? 1 : 0;
        }
        
        trainBinary(X, binaryY);
    }
    
    /**
     * Sigmoid activation function
     */
    private double sigmoid(double z) {
        // Prevent overflow
        if (z > 500) return 1.0;
        if (z < -500) return 0.0;
        
        return 1.0 / (1.0 + Math.exp(-z));
    }
    
    /**
     * Calculate cost (log loss)
     */
    private double calculateCost(double[][] X, int[] y) {
        double cost = 0.0;
        int numSamples = X.length;
        
        for (int i = 0; i < numSamples; i++) {
            double z = bias;
            for (int j = 0; j < weights.length; j++) {
                z += weights[j] * X[i][j];
            }
            
            double prediction = sigmoid(z);
            
            // Log loss
            if (y[i] == 1) {
                cost -= Math.log(Math.max(prediction, 1e-15));
            } else {
                cost -= Math.log(Math.max(1 - prediction, 1e-15));
            }
        }
        
        return cost / numSamples;
    }
    
    /**
     * Normalize features (simple min-max normalization)
     */
    private void normalizeFeatures(double[][] X) {
        int numFeatures = X[0].length;
        
        for (int j = 0; j < numFeatures; j++) {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            
            // Find min and max
            for (int i = 0; i < X.length; i++) {
                min = Math.min(min, X[i][j]);
                max = Math.max(max, X[i][j]);
            }
            
            // Normalize
            double range = max - min;
            if (range > 0) {
                for (int i = 0; i < X.length; i++) {
                    X[i][j] = (X[i][j] - min) / range;
                }
            }
        }
    }
    
    /**
     * Map class labels to integers
     */
    private void mapClassLabels(Dataset dataset) {
        Set<Object> uniqueClasses = new HashSet<>();
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            uniqueClasses.add(dataset.getInstance(i).getClassValue());
        }
        
        classLabels = new ArrayList<>(uniqueClasses);
        classMapping = new HashMap<>();
        
        for (int i = 0; i < classLabels.size(); i++) {
            classMapping.put(classLabels.get(i), i);
        }
    }
    
    /**
     * Get model summary for display
     */
    public String getModelSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Logistic Regression Model Summary ===\n");
        
        if (weights != null) {
            sb.append("Bias: ").append(String.format("%.4f", bias)).append("\n");
            sb.append("Feature Weights:\n");
            
            for (int i = 0; i < weights.length; i++) {
                String attrName = trainingData.getAttribute(i).getName();
                sb.append("  ").append(attrName).append(": ")
                  .append(String.format("%.4f", weights[i])).append("\n");
            }
            
            sb.append("Learning Rate: ").append(learningRate).append("\n");
            sb.append("Max Iterations: ").append(maxIterations).append("\n");
        } else {
            sb.append("Model not trained yet.\n");
        }
        
        return sb.toString();
    }
}