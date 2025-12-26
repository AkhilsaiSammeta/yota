package algorithms.core;

import core.Instance;

/**
 * DISTANCECALCULATOR CLASS
 * 
 * Purpose: Calculate distances between data points (instances)
 * Think of it like: Measuring how similar/different two people are
 * 
 * Real-life analogy: Like measuring the distance between two cities using coordinates
 */

public class DistanceCalculator {
    
    // Method: Calculate Euclidean distance between two instances
    // This is the most common distance measure in machine learning
    // 
    // Formula (in plain English):
    // 1. For each feature (age, salary, etc.)
    // 2. Find the difference between the two values
    // 3. Square that difference
    // 4. Add up all the squared differences
    // 5. Take the square root of the sum
    // 
    // Example:
    // Person A: [25, 45000]  (age=25, salary=45000)
    // Person B: [30, 50000]  (age=30, salary=50000)
    // 
    // Distance = sqrt((25-30)² + (45000-50000)²)
    //          = sqrt(25 + 25000000)
    //          = sqrt(25000025) ≈ 5000.0
    // 
    // Parameters:
    //   - instance1: First data point
    //   - instance2: Second data point
    // Returns: Distance as a double (0 = identical, higher = more different)
    public static double euclideanDistance(Instance instance1, Instance instance2) {
        // Make sure both instances have the same number of features
        int numFeatures = Math.min(instance1.getNumValues(), instance2.getNumValues());
        
        double sumOfSquaredDifferences = 0.0;
        
        // Loop through each feature (excluding the last one which is the class)
        for (int i = 0; i < numFeatures - 1; i++) {
            Object value1 = instance1.getValue(i);
            Object value2 = instance2.getValue(i);
            
            // Only calculate distance for numeric values
            if (value1 instanceof Double && value2 instanceof Double) {
                double num1 = (Double) value1;
                double num2 = (Double) value2;
                
                // Calculate the difference
                double difference = num1 - num2;
                
                // Square the difference and add to sum
                sumOfSquaredDifferences += difference * difference;
            }
            // For non-numeric values, we could implement other measures
            // but for simplicity, we'll skip them for now
        }
        
        // Return the square root of the sum
        return Math.sqrt(sumOfSquaredDifferences);
    }
    
    // Method: Calculate Manhattan distance (alternative to Euclidean)
    // This is like walking in a city with blocks - you can't go diagonally
    // 
    // Formula: Sum of absolute differences
    // Example: |25-30| + |45000-50000| = 5 + 5000 = 5005
    // 
    // Parameters:
    //   - instance1: First data point
    //   - instance2: Second data point
    // Returns: Manhattan distance as a double
    public static double manhattanDistance(Instance instance1, Instance instance2) {
        int numFeatures = Math.min(instance1.getNumValues(), instance2.getNumValues());
        
        double sumOfAbsoluteDifferences = 0.0;
        
        // Loop through each feature (excluding the class)
        for (int i = 0; i < numFeatures - 1; i++) {
            Object value1 = instance1.getValue(i);
            Object value2 = instance2.getValue(i);
            
            // Only calculate distance for numeric values
            if (value1 instanceof Double && value2 instanceof Double) {
                double num1 = (Double) value1;
                double num2 = (Double) value2;
                
                // Calculate absolute difference and add to sum
                sumOfAbsoluteDifferences += Math.abs(num1 - num2);
            }
        }
        
        return sumOfAbsoluteDifferences;
    }
}