package ui;

import java.util.HashMap;
import core.DataAnalyzer;
import core.Attribute;
import core.Dataset;

/**
 * SUMMARYPRINTER CLASS
 * 
 * Purpose: Pretty-print data analysis results (Power BI-style)
 * Think of it like: Creating formatted reports from raw statistics
 * 
 * Real-life analogy: Like generating a nice dashboard from Excel data
 */

public class SummaryPrinter {
    
    private DataAnalyzer analyzer;
    
    // Constructor: Initialize with a DataAnalyzer
    public SummaryPrinter(DataAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    // Method: Print complete dataset summary
    public void printDatasetSummary() {
        Dataset dataset = analyzer.getDataset();
        
        System.out.println("===== DATA SUMMARY =====");
        System.out.println("Dataset: " + dataset.getName());
        System.out.println("Rows: " + analyzer.getRowCount());
        System.out.println("Columns: " + analyzer.getColumnCount());
        System.out.println();
    }
    
    // Method: Print statistics for all columns
    public void printColumnStats() {
        Dataset dataset = analyzer.getDataset();
        
        System.out.println("===== COLUMN STATS =====");
        
        // Loop through all attributes (columns)
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            Attribute attr = dataset.getAttribute(i);
            String columnName = attr.getName();
            String columnType = attr.getType();
            
            System.out.print(columnName + " (" + columnType + ") -> ");
            
            if (columnType.equals("numeric")) {
                // Print numeric statistics
                Double min = analyzer.getMin(i);
                Double max = analyzer.getMax(i);
                Double avg = analyzer.getAverage(i);
                
                if (min != null && max != null && avg != null) {
                    System.out.printf("Min: %.2f, Max: %.2f, Avg: %.2f%n", min, max, avg);
                } else {
                    System.out.println("No numeric data found");
                }
            } else {
                // Print categorical statistics
                int uniqueCount = analyzer.getUniqueValueCount(i);
                System.out.println("Unique values: " + uniqueCount);
            }
        }
        System.out.println();
    }
    
    // Method: Print frequency table for categorical columns
    public void printFrequencyTables() {
        Dataset dataset = analyzer.getDataset();
        
        System.out.println("===== FREQUENCY TABLES =====");
        
        // Loop through all attributes (columns)
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            Attribute attr = dataset.getAttribute(i);
            
            if (attr.getType().equals("categorical")) {
                String columnName = attr.getName();
                HashMap<String, Integer> frequencies = analyzer.getFrequency(i);
                
                System.out.println(columnName + " frequencies:");
                
                // Print each value and its count
                for (String value : frequencies.keySet()) {
                    int count = frequencies.get(value);
                    System.out.println("  " + value + ": " + count);
                }
                System.out.println();
            }
        }
    }
    
    // Method: Print complete analysis report
    public void printCompleteReport() {
        printDatasetSummary();
        printColumnStats();
        printFrequencyTables();
    }
    
    // Method: Print missing value analysis
    public void printMissingValueAnalysis() {
        Dataset dataset = analyzer.getDataset();
        
        System.out.println("===== MISSING VALUE ANALYSIS =====");
        
        boolean anyMissing = false;
        
        for (int i = 0; i < dataset.getNumAttributes(); i++) {
            Attribute attr = dataset.getAttribute(i);
            String columnName = attr.getName();
            
            if (analyzer.hasMissingValues(i)) {
                System.out.println(columnName + ": Has missing values");
                anyMissing = true;
            }
        }
        
        if (!anyMissing) {
            System.out.println("No missing values detected in any column");
        }
        System.out.println();
    }
}