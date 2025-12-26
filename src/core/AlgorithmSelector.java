package core;

import java.util.*;

/**
 * ALGORITHM SELECTOR
 * 
 * Purpose: Factory class to create different ML algorithms
 * Think of it like: A menu where users can choose different ML algorithms
 * 
 * Real-life analogy: Like a restaurant menu where you can choose
 * different dishes (algorithms) for your meal (data analysis)
 */
public class AlgorithmSelector {
    
    public enum AlgorithmType {
        KNN("K-Nearest Neighbors", "Simple, instance-based learning. Good for non-linear data."),
        DECISION_TREE("Decision Tree", "Rule-based learning with interpretable decisions."),
        NAIVE_BAYES("Naive Bayes", "Probabilistic classifier assuming feature independence."),
        LOGISTIC_REGRESSION("Logistic Regression", "Linear classifier using statistical regression.");
        
        private final String displayName;
        private final String description;
        
        AlgorithmType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Create classifier based on algorithm type
     */
    public static Classifier createClassifier(AlgorithmType algorithmType) {
        return createClassifier(algorithmType, new HashMap<>());
    }
    
    /**
     * Create classifier with parameters
     */
    public static Classifier createClassifier(AlgorithmType algorithmType, Map<String, Object> parameters) {
        switch (algorithmType) {
            case KNN:
                int k = (Integer) parameters.getOrDefault("k", 3);
                return new KNNClassifierWrapper(k);
                
            case DECISION_TREE:
                int maxDepth = (Integer) parameters.getOrDefault("maxDepth", 10);
                int minSamplesLeaf = (Integer) parameters.getOrDefault("minSamplesLeaf", 2);
                return new DecisionTreeClassifierWrapper(maxDepth, minSamplesLeaf);
                
            case NAIVE_BAYES:
                return new NaiveBayesClassifierWrapper();
                
            case LOGISTIC_REGRESSION:
                double learningRate = (Double) parameters.getOrDefault("learningRate", 0.01);
                int maxIterations = (Integer) parameters.getOrDefault("maxIterations", 1000);
                return new LogisticRegressionClassifierWrapper(learningRate, maxIterations);
                
            default:
                throw new IllegalArgumentException("Unknown algorithm type: " + algorithmType);
        }
    }
    
    /**
     * Get all available algorithms
     */
    public static List<AlgorithmType> getAvailableAlgorithms() {
        return Arrays.asList(AlgorithmType.values());
    }
    
    /**
     * Get algorithm information
     */
    public static String getAlgorithmInfo(AlgorithmType algorithmType) {
        StringBuilder info = new StringBuilder();
        info.append("Algorithm: ").append(algorithmType.getDisplayName()).append("\n");
        info.append("Description: ").append(algorithmType.getDescription()).append("\n");
        
        switch (algorithmType) {
            case KNN:
                info.append("Parameters:\n");
                info.append("  - k: Number of neighbors to consider (default: 3)\n");
                info.append("Pros: Simple, no assumptions about data distribution\n");
                info.append("Cons: Can be slow on large datasets, sensitive to irrelevant features\n");
                break;
                
            case DECISION_TREE:
                info.append("Parameters:\n");
                info.append("  - maxDepth: Maximum depth of tree (default: 10)\n");
                info.append("  - minSamplesLeaf: Minimum samples in leaf node (default: 2)\n");
                info.append("Pros: Highly interpretable, handles both numeric and categorical data\n");
                info.append("Cons: Prone to overfitting, can be unstable\n");
                break;
                
            case NAIVE_BAYES:
                info.append("Parameters: None (uses automatic parameter estimation)\n");
                info.append("Pros: Fast training and prediction, works well with small datasets\n");
                info.append("Cons: Assumes feature independence, may not work well with correlated features\n");
                break;
                
            case LOGISTIC_REGRESSION:
                info.append("Parameters:\n");
                info.append("  - learningRate: Step size for gradient descent (default: 0.01)\n");
                info.append("  - maxIterations: Maximum training iterations (default: 1000)\n");
                info.append("Pros: Provides probability estimates, less prone to overfitting\n");
                info.append("Cons: Assumes linear relationship, may need feature engineering\n");
                break;
        }
        
        return info.toString();
    }
    
    // Wrapper classes to implement Classifier interface
    
    private static class KNNClassifierWrapper implements Classifier {
        private KNNClassifier classifier;
        private int k;
        
        public KNNClassifierWrapper(int k) {
            this.k = k;
            this.classifier = new KNNClassifier(k);
        }
        
        @Override
        public void train(Dataset dataset) {
            classifier.train(dataset);
        }
        
        @Override
        public Object predict(Instance instance) {
            return classifier.predict(instance);
        }
        
        @Override
        public String getAlgorithmName() {
            return "K-Nearest Neighbors (k=" + k + ")";
        }
        
        @Override
        public String getModelSummary() {
            return "=== K-Nearest Neighbors Summary ===\n" +
                   "K Value: " + k + "\n" +
                   "Distance Metric: Euclidean\n" +
                   "Voting Strategy: Majority Vote\n" +
                   "Training: Lazy learning (stores all instances)\n";
        }
    }
    
    private static class DecisionTreeClassifierWrapper implements Classifier {
        private DecisionTreeClassifier classifier;
        private int maxDepth;
        private int minSamplesLeaf;
        
        public DecisionTreeClassifierWrapper(int maxDepth, int minSamplesLeaf) {
            this.maxDepth = maxDepth;
            this.minSamplesLeaf = minSamplesLeaf;
            this.classifier = new DecisionTreeClassifier(maxDepth, minSamplesLeaf);
        }
        
        @Override
        public void train(Dataset dataset) {
            classifier.train(dataset);
        }
        
        @Override
        public Object predict(Instance instance) {
            return classifier.predict(instance);
        }
        
        @Override
        public String getAlgorithmName() {
            return "Decision Tree";
        }
        
        @Override
        public String getModelSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Decision Tree Summary ===\n");
            sb.append("Max Depth: ").append(maxDepth).append("\n");
            sb.append("Min Samples per Leaf: ").append(minSamplesLeaf).append("\n");
            sb.append("Split Criterion: Gini Impurity\n\n");
            sb.append("Tree Structure:\n");
            sb.append(classifier.getTreeStructure());
            return sb.toString();
        }
    }
    
    private static class NaiveBayesClassifierWrapper implements Classifier {
        private NaiveBayesClassifier classifier;
        
        public NaiveBayesClassifierWrapper() {
            this.classifier = new NaiveBayesClassifier();
        }
        
        @Override
        public void train(Dataset dataset) {
            classifier.train(dataset);
        }
        
        @Override
        public Object predict(Instance instance) {
            return classifier.predict(instance);
        }
        
        @Override
        public String getAlgorithmName() {
            return "Naive Bayes";
        }
        
        @Override
        public String getModelSummary() {
            return classifier.getModelSummary();
        }
    }
    
    private static class LogisticRegressionClassifierWrapper implements Classifier {
        private LogisticRegressionClassifier classifier;
        private double learningRate;
        private int maxIterations;
        
        public LogisticRegressionClassifierWrapper(double learningRate, int maxIterations) {
            this.learningRate = learningRate;
            this.maxIterations = maxIterations;
            this.classifier = new LogisticRegressionClassifier(learningRate, maxIterations);
        }
        
        @Override
        public void train(Dataset dataset) {
            classifier.train(dataset);
        }
        
        @Override
        public Object predict(Instance instance) {
            return classifier.predict(instance);
        }
        
        @Override
        public String getAlgorithmName() {
            return "Logistic Regression";
        }
        
        @Override
        public String getModelSummary() {
            return classifier.getModelSummary();
        }
    }
}