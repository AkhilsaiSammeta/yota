package com.yota.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DATAANALYZER CLASS (Spring Boot Version)
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
    public Double getMin(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        Double min = null;
        
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
    public Double getMax(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        Double max = null;
        
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
    public Double getAverage(int attributeIndex) {
        Attribute attr = dataset.getAttribute(attributeIndex);
        if (attr == null || !attr.getType().equals("numeric")) {
            return null;
        }
        
        double sum = 0.0;
        int count = 0;
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value instanceof Double) {
                sum += (Double) value;
                count++;
            }
        }
        
        if (count == 0) {
            return null;
        }
        
        return sum / count;
    }
    
    // Method: Get frequency count for categorical values
    public HashMap<String, Integer> getFrequency(int attributeIndex) {
        HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            Object value = instance.getValue(attributeIndex);
            
            if (value != null) {
                String stringValue = value.toString();
                
                if (frequencies.containsKey(stringValue)) {
                    frequencies.put(stringValue, frequencies.get(stringValue) + 1);
                } else {
                    frequencies.put(stringValue, 1);
                }
            }
        }
        
        return frequencies;
    }
    
    // Method: Get unique value count for a column
    public int getUniqueValueCount(int attributeIndex) {
        HashMap<String, Integer> frequencies = getFrequency(attributeIndex);
        return frequencies.size();
    }
    
    // Method: Check if a column has missing values (null)
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