package com.yota.model;

import java.util.List;
import java.util.Map;

/**
 * ANALYSIS RESULT MODEL
 * 
 * Purpose: Store results from data analysis for web display
 * Think of it like: A report card that holds all the statistics
 * 
 * Real-life analogy: Like a summary report from analyzing survey data
 */
public class AnalysisResult {
    
    private String datasetName;
    private int totalRows;
    private int totalColumns;
    private Map<String, ColumnStats> columnStatistics;
    private Map<String, Map<String, Integer>> frequencyTables;
    private List<String> missingValueColumns;
    
    // Default constructor
    public AnalysisResult() {}
    
    // Constructor with basic info
    public AnalysisResult(String datasetName, int totalRows, int totalColumns) {
        this.datasetName = datasetName;
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
    }
    
    // Getters and Setters
    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
    
    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }
    
    public int getTotalColumns() { return totalColumns; }
    public void setTotalColumns(int totalColumns) { this.totalColumns = totalColumns; }
    
    public Map<String, ColumnStats> getColumnStatistics() { return columnStatistics; }
    public void setColumnStatistics(Map<String, ColumnStats> columnStatistics) { this.columnStatistics = columnStatistics; }
    
    public Map<String, Map<String, Integer>> getFrequencyTables() { return frequencyTables; }
    public void setFrequencyTables(Map<String, Map<String, Integer>> frequencyTables) { this.frequencyTables = frequencyTables; }
    
    public List<String> getMissingValueColumns() { return missingValueColumns; }
    public void setMissingValueColumns(List<String> missingValueColumns) { this.missingValueColumns = missingValueColumns; }
    
    /**
     * COLUMN STATISTICS INNER CLASS
     * 
     * Holds statistics for one column
     */
    public static class ColumnStats {
        private String columnName;
        private String columnType;
        private Double min;
        private Double max;
        private Double average;
        private Integer uniqueValues;
        
        // Constructors
        public ColumnStats() {}
        
        public ColumnStats(String columnName, String columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
        }
        
        // Getters and Setters
        public String getColumnName() { return columnName; }
        public void setColumnName(String columnName) { this.columnName = columnName; }
        
        public String getColumnType() { return columnType; }
        public void setColumnType(String columnType) { this.columnType = columnType; }
        
        public Double getMin() { return min; }
        public void setMin(Double min) { this.min = min; }
        
        public Double getMax() { return max; }
        public void setMax(Double max) { this.max = max; }
        
        public Double getAverage() { return average; }
        public void setAverage(Double average) { this.average = average; }
        
        public Integer getUniqueValues() { return uniqueValues; }
        public void setUniqueValues(Integer uniqueValues) { this.uniqueValues = uniqueValues; }
    }
}