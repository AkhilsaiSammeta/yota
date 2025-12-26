package core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DATAANALYZER CLASS
 * 
 * Purpose: Compute Power BI-style statistics on datasets
 * Think of it like: Excel's Data Analysis ToolPak for descriptive statistics
 * 
 * Real-life analogy: Like analyzing survey data to find patterns and summaries
 */

public class DataAnalyzer {
    
    private Dataset dataset;
    
    // Constructor: Initialize with a dataset to analyze
    public DataAnalyzer(Dataset dataset) {
        this.dataset = dataset;
    }
    
    // Method: Get total number of rows in the dataset
    public int getRowCount() {
        return dataset.getNumInstances();
    }
    
    // Method: Get total number of columns in the dataset
    public int getColumnCount() {
        return dataset.getNumAttributes();
    }
    
    // Method: Get minimum value for a numeric column
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: Minimum value or null if column is not numeric
    public Double getMin(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        Double min = null;
        
        // Loop through all instances to find minimum
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value instanceof Double) {
                Double doubleValue = (Double) value;
                if (min == null || doubleValue < min) {
                    min = doubleValue;
                }
            }
        }
        
        return min;
    }
    
    // Method: Get maximum value for a numeric column
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: Maximum value or null if column is not numeric
    public Double getMax(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        Double max = null;
        
        // Loop through all instances to find maximum
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value instanceof Double) {
                Double doubleValue = (Double) value;
                if (max == null || doubleValue > max) {
                    max = doubleValue;
                }
            }
        }
        
        return max;
    }
    
    // Method: Get average value for a numeric column
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: Average value or null if column is not numeric
    public Double getAverage(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        double sum = 0.0;
        int count = 0;
        
        // Loop through all instances to calculate sum and count
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value instanceof Double) {
                sum += (Double) value;
                count++;
            }
        }
        
        // Avoid division by zero
        if (count == 0) {
            return null;
        }
        
        return sum / count;
    }
    
    // Method: Get frequency count for categorical values
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: HashMap with value -> count mapping
    public HashMap<String, Integer> getFrequency(int attributeIndex) {
        HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
        
        // Loop through all instances to count occurrences
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value != null) {
                String stringValue = value.toString();
                
                // If we've seen this value before, increment count
                if (frequencies.containsKey(stringValue)) {
                    frequencies.put(stringValue, frequencies.get(stringValue) + 1);
                } else {
                    // First time seeing this value, set count to 1
                    frequencies.put(stringValue, 1);
                }
            }
        }
        
        return frequencies;
    }
    
    // Method: Get unique value count for a column
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: Number of distinct values in this column
    public int getUniqueValueCount(int attributeIndex) {
        HashMap<String, Integer> frequencies = getFrequency(attributeIndex);
        return frequencies.size();
    }
    
    // Method: Check if a column has missing values (null)
    // Parameters:
    //   - attributeIndex: Which column to analyze (0-based)
    // Returns: True if any null values found, false otherwise
    public boolean hasMissingValues(int attributeIndex) {
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value == null) {
                return true;
            }
        }
        return false;
    }
    
    // Method: Get dataset reference for other operations
    public Dataset getDataset() {
        return dataset;
    }
}