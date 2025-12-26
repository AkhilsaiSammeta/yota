package evaluation;

import java.util.HashMap;

/**
 * CONFUSIONMATRIX CLASS
 * 
 * Purpose: Evaluate classifier performance using confusion matrix
 * Think of it like: A report card showing where the classifier got confused
 * 
 * Real-life analogy: Like a table showing how many emails were correctly
 * classified as spam vs non-spam, and where mistakes were made
 * 
 * What is a Confusion Matrix?
 * A 2D table showing:
 * - Rows: Actual classes (what really happened)
 * - Columns: Predicted classes (what our classifier guessed)
 * 
 * Example for 2 classes (Hired/NotHired):
 *                   PREDICTED
 *               Hired  NotHired
 * ACTUAL Hired    85      15     (85 correct, 15 wrong)
 *     NotHired    10      90     (90 correct, 10 wrong)
 * 
 * From this we can calculate:
 * - Accuracy: (85+90) / (85+15+10+90) = 87.5%
 * - Precision for Hired: 85 / (85+10) = 89.5%
 * - Recall for Hired: 85 / (85+15) = 85.0%
 */

public class ConfusionMatrix {
    
    // Store the actual vs predicted classes
    private HashMap<String, HashMap<String, Integer>> matrix;
    
    // Store all unique class labels we've seen
    private HashMap<String, Integer> classLabels;
    
    // Total number of predictions made
    private int totalPredictions;
    
    // Constructor: Create an empty confusion matrix
    public ConfusionMatrix() {
        this.matrix = new HashMap<String, HashMap<String, Integer>>();
        this.classLabels = new HashMap<String, Integer>();
        this.totalPredictions = 0;
    }
    
    // Method: Add a prediction result to the matrix
    // Parameters:
    //   - actualClass: What the true class was
    //   - predictedClass: What our classifier predicted
    public void addPrediction(Object actualClass, Object predictedClass) {
        String actual = actualClass.toString();
        String predicted = predictedClass.toString();
        
        // Make sure we know about these class labels
        classLabels.put(actual, 1);
        classLabels.put(predicted, 1);
        
        // Initialize row for actual class if not exists
        if (!matrix.containsKey(actual)) {
            matrix.put(actual, new HashMap<String, Integer>());
        }
        
        // Get the row for this actual class
        HashMap<String, Integer> row = matrix.get(actual);
        
        // Increment the count for this actual->predicted combination
        if (row.containsKey(predicted)) {
            row.put(predicted, row.get(predicted) + 1);
        } else {
            row.put(predicted, 1);
        }
        
        totalPredictions++;
    }
    
    // Method: Calculate overall accuracy
    // Accuracy = (Correct Predictions) / (Total Predictions)
    // Returns: Accuracy as percentage (0-100)
    public double getAccuracy() {
        if (totalPredictions == 0) {
            return 0.0;
        }
        
        int correctPredictions = 0;
        
        // Count diagonal elements (where actual == predicted)
        for (String actualClass : matrix.keySet()) {
            HashMap<String, Integer> row = matrix.get(actualClass);
            if (row.containsKey(actualClass)) {
                correctPredictions += row.get(actualClass);
            }
        }
        
        return (double) correctPredictions / totalPredictions * 100.0;
    }
    
    // Method: Calculate precision for a specific class
    // Precision = True Positives / (True Positives + False Positives)
    // "Of all instances we predicted as this class, how many were actually correct?"
    // Parameters:
    //   - className: Which class to calculate precision for
    // Returns: Precision as percentage (0-100)
    public double getPrecision(String className) {
        int truePositives = 0;
        int falsePositives = 0;
        
        // True positives: actual=className AND predicted=className
        if (matrix.containsKey(className) && matrix.get(className).containsKey(className)) {
            truePositives = matrix.get(className).get(className);
        }
        
        // False positives: actual!=className BUT predicted=className
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
    
    // Method: Calculate recall for a specific class
    // Recall = True Positives / (True Positives + False Negatives)
    // "Of all actual instances of this class, how many did we correctly identify?"
    // Parameters:
    //   - className: Which class to calculate recall for
    // Returns: Recall as percentage (0-100)
    public double getRecall(String className) {
        if (!matrix.containsKey(className)) {
            return 0.0;
        }
        
        HashMap<String, Integer> row = matrix.get(className);
        int totalActualOfThisClass = 0;
        int truePositives = 0;
        
        // Count all actual instances of this class
        for (Integer count : row.values()) {
            totalActualOfThisClass += count;
        }
        
        // True positives: actual=className AND predicted=className
        if (row.containsKey(className)) {
            truePositives = row.get(className);
        }
        
        if (totalActualOfThisClass == 0) {
            return 0.0;
        }
        
        return (double) truePositives / totalActualOfThisClass * 100.0;
    }
    
    // Method: Print the confusion matrix in a readable format
    public void printMatrix() {
        System.out.println("===== CONFUSION MATRIX =====");
        
        if (totalPredictions == 0) {
            System.out.println("No predictions recorded yet.");
            return;
        }
        
        // Get all class labels for formatting
        String[] classes = classLabels.keySet().toArray(new String[0]);
        
        // Print header
        System.out.print("        ");
        for (String predictedClass : classes) {
            System.out.printf("%8s", predictedClass);
        }
        System.out.println();
        
        // Print matrix rows
        for (String actualClass : classes) {
            System.out.printf("%8s", actualClass);
            
            for (String predictedClass : classes) {
                int count = 0;
                if (matrix.containsKey(actualClass) && matrix.get(actualClass).containsKey(predictedClass)) {
                    count = matrix.get(actualClass).get(predictedClass);
                }
                System.out.printf("%8d", count);
            }
            System.out.println();
        }
        
        System.out.printf("Overall Accuracy: %.2f%%\n", getAccuracy());
        System.out.println();
    }
    
    // Method: Get total number of predictions
    public int getTotalPredictions() {
        return totalPredictions;
    }
    
    // Method: Get all class labels
    public String[] getClassLabels() {
        return classLabels.keySet().toArray(new String[0]);
    }
    
    // Method: Get matrix count for specific actual and predicted classes
    public int getCount(String actualClass, String predictedClass) {
        if (matrix.containsKey(actualClass)) {
            HashMap<String, Integer> row = matrix.get(actualClass);
            return row.getOrDefault(predictedClass, 0);
        }
        return 0;
    }
    
    // Method: Get the internal matrix (for advanced usage)
    public HashMap<String, HashMap<String, Integer>> getMatrix() {
        return matrix;
    }
}