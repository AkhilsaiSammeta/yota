package core;

/**
 * CLASSIFIER INTERFACE
 * 
 * Purpose: Common interface for all ML algorithms
 * Think of it like: A contract that all classifiers must follow
 * 
 * Real-life analogy: Like a standard format for all ML algorithms
 * so they can be used interchangeably
 */
public interface Classifier {
    
    /**
     * Train the classifier with given dataset
     */
    void train(Dataset dataset);
    
    /**
     * Predict class for new instance
     */
    Object predict(Instance instance);
    
    /**
     * Get algorithm name
     */
    String getAlgorithmName();
    
    /**
     * Get model summary/details for display
     */
    String getModelSummary();
}