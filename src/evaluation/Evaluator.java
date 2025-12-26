package evaluation;

import java.util.ArrayList;
import core.Dataset;
import core.Instance;
import algorithms.classifier.KNNClassifier;

/**
 * EVALUATOR CLASS
 * 
 * Purpose: Orchestrate the training and testing process
 * Think of it like: A teacher running an exam to see how well students learned
 * 
 * Real-life analogy: Like splitting your study materials into practice problems
 * and test problems, then checking how well you did on the test
 * 
 * Standard ML Evaluation Process:
 * 1. Split data into training set (70-80%) and test set (20-30%)
 * 2. Train classifier on training set
 * 3. Make predictions on test set
 * 4. Compare predictions with actual answers
 * 5. Calculate accuracy and other metrics
 */

public class Evaluator {
    
    // Method: Split dataset into training and test sets
    // Parameters:
    //   - dataset: Complete dataset to split
    //   - trainRatio: What percentage for training (e.g., 0.7 = 70% for training)
    // Returns: Array with [trainingSet, testSet]
    public static Dataset[] splitDataset(Dataset dataset, double trainRatio) {
        int totalInstances = dataset.getNumInstances();
        int trainSize = (int) (totalInstances * trainRatio);
        
        // Create training dataset
        Dataset trainSet = new Dataset(dataset.getName() + " - Training");
        
        // Copy all attributes to both datasets
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            trainSet.addAttribute(dataset.getAttribute(i));
        }
        
        // Create test dataset  
        Dataset testSet = new Dataset(dataset.getName() + " - Test");
        
        // Copy all attributes to test dataset too
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            testSet.addAttribute(dataset.getAttribute(i));
        }
        
        // Split instances: first trainSize go to training, rest go to test
        for (int i = 0; i < totalInstances; i++) {
            Instance instance = dataset.getInstance(i);
            
            if (i < trainSize) {
                trainSet.addInstance(instance);
            } else {
                testSet.addInstance(instance);
            }
        }
        
        System.out.println("Dataset split: " + trainSet.getNumInstances() + 
                          " training, " + testSet.getNumInstances() + " test");
        
        return new Dataset[]{trainSet, testSet};
    }
    
    // Method: Evaluate a KNN classifier using train-test split
    // Parameters:
    //   - dataset: Complete dataset
    //   - k: K value for KNN
    //   - trainRatio: Percentage for training (e.g., 0.7)
    // Returns: ConfusionMatrix with results
    public static ConfusionMatrix evaluateKNN(Dataset dataset, int k, double trainRatio) {
        System.out.println("Starting KNN evaluation with K=" + k);
        
        // Step 1: Split the dataset
        Dataset[] split = splitDataset(dataset, trainRatio);
        Dataset trainSet = split[0];
        Dataset testSet = split[1];
        
        // Step 2: Create and train the classifier
        KNNClassifier classifier = new KNNClassifier(k);
        classifier.train(trainSet);
        
        // Step 3: Create confusion matrix to track results
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        
        // Step 4: Make predictions on test set
        System.out.println("Making predictions on test set...");
        
        for (int i = 0; i < testSet.getNumInstances(); i++) {
            Instance testInstance = testSet.getInstance(i);
            
            // Get actual class
            Object actualClass = testInstance.getClassValue();
            
            // Make prediction
            Object predictedClass = classifier.predict(testInstance);
            
            // Add to confusion matrix
            if (actualClass != null && predictedClass != null) {
                confusionMatrix.addPrediction(actualClass, predictedClass);
            }
        }
        
        return confusionMatrix;
    }
    
    // Method: Quick evaluation with default parameters
    // Uses 70% training, 30% test split
    // Parameters:
    //   - dataset: Dataset to evaluate
    //   - k: K value for KNN
    // Returns: ConfusionMatrix with results
    public static ConfusionMatrix quickEvaluate(Dataset dataset, int k) {
        return evaluateKNN(dataset, k, 0.7);
    }
    
    // Method: Cross-validation (simplified version)
    // Split data into multiple folds and average results
    // Parameters:
    //   - dataset: Dataset to evaluate
    //   - k: K value for KNN  
    //   - folds: Number of folds (e.g., 5 for 5-fold CV)
    // Returns: Average accuracy across all folds
    public static double crossValidate(Dataset dataset, int k, int folds) {
        System.out.println("Starting " + folds + "-fold cross-validation with K=" + k);
        
        int totalInstances = dataset.getNumInstances();
        int foldSize = totalInstances / folds;
        double totalAccuracy = 0.0;
        
        // Run evaluation for each fold
        for (int fold = 0; fold < folds; fold++) {
            System.out.println("Fold " + (fold + 1) + "/" + folds);
            
            // Create training set (all instances except current fold)
            Dataset trainSet = new Dataset(dataset.getName() + " - CV Train");
            Dataset testSet = new Dataset(dataset.getName() + " - CV Test");
            
            // Copy attributes
            for (int i = 0; i < dataset.getNumAttributes(); i++) {
                trainSet.addAttribute(dataset.getAttribute(i));
                testSet.addAttribute(dataset.getAttribute(i));
            }
            
            // Split instances
            int testStart = fold * foldSize;
            int testEnd = (fold == folds - 1) ? totalInstances : testStart + foldSize;
            
            for (int i = 0; i < totalInstances; i++) {
                Instance instance = dataset.getInstance(i);
                
                if (i >= testStart && i < testEnd) {
                    testSet.addInstance(instance);  // This fold is for testing
                } else {
                    trainSet.addInstance(instance); // Other folds for training
                }
            }
            
            // Evaluate this fold
            ConfusionMatrix cm = evaluateKNN(trainSet, testSet, k);
            double accuracy = cm.getAccuracy();
            totalAccuracy += accuracy;
            
            System.out.println("Fold " + (fold + 1) + " accuracy: " + String.format("%.2f%%", accuracy));
        }
        
        double avgAccuracy = totalAccuracy / folds;
        System.out.println("Average CV accuracy: " + String.format("%.2f%%", avgAccuracy));
        
        return avgAccuracy;
    }
    
    // Helper method: Evaluate KNN with pre-split datasets
    private static ConfusionMatrix evaluateKNN(Dataset trainSet, Dataset testSet, int k) {
        KNNClassifier classifier = new KNNClassifier(k);
        classifier.train(trainSet);
        
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        
        for (int i = 0; i < testSet.getNumInstances(); i++) {
            Instance testInstance = testSet.getInstance(i);
            Object actualClass = testInstance.getClassValue();
            Object predictedClass = classifier.predict(testInstance);
            
            if (actualClass != null && predictedClass != null) {
                confusionMatrix.addPrediction(actualClass, predictedClass);
            }
        }
        
        return confusionMatrix;
    }
}