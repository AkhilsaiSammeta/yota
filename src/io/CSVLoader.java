package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import core.Dataset;
import core.Attribute;
import core.Instance;

/**
 * CSVLOADER CLASS
 * 
 * Purpose: Read CSV files and convert them to Dataset objects
 * Think of it like: Importing an Excel file into your program
 * 
 * Real-life analogy: Like opening a CSV file and organizing it into a structured format
 * 
 * CSV Format Assumed:
 * - First row = column headers (Attribute names)
 * - Other rows = data
 * - Last column = class/label
 * - Comma-separated values
 * 
 * Example CSV:
 * Age,Salary,Hired
 * 25,45000,Yes
 * 30,50000,No
 */

public class CSVLoader {
    
    // Method: Load a CSV file and return a Dataset object
    // Parameters:
    //   - filename: Path to the CSV file
    //   - datasetName: What to name this dataset
    // Returns: A Dataset object containing all data from the CSV
    public static Dataset loadCSV(String filename, String datasetName) {
        // Create an empty dataset with the given name
        Dataset dataset = new Dataset(datasetName);
        
        try {
            // Open the CSV file for reading
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            boolean firstLine = true;
            
            // Read the CSV file line by line
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split the line by commas to get individual values
                String[] values = line.split(",");
                
                // If this is the first line, these are column headers (attributes)
                if (firstLine) {
                    firstLine = false;
                    
                    // For each header, create an Attribute
                    for (int i = 0; i < values.length; i++) {
                        String header = values[i].trim();
                        
                        // Last column is categorical (class/label), others are numeric by default
                        String type = (i == values.length - 1) ? "categorical" : "numeric";
                        
                        Attribute attr = new Attribute(header, type);
                        dataset.addAttribute(attr);
                    }
                } else {
                    // This is a data row, create an Instance
                    Instance instance = new Instance();
                    
                    // Add each value to the instance
                    for (int i = 0; i < values.length; i++) {
                        String value = values[i].trim();
                        
                        // Try to convert to number, otherwise keep as text
                        Object objectValue = tryParseNumber(value);
                        
                        // Add the value to this instance
                        instance.addValue(objectValue);
                    }
                    
                    // Set the class value (last column)
                    if (values.length > 0) {
                        Object lastValue = tryParseNumber(values[values.length - 1].trim());
                        instance.setClassValue(lastValue);
                    }
                    
                    // Add this instance to the dataset
                    dataset.addInstance(instance);
                }
            }
            
            // Close the file
            reader.close();
            
        } catch (IOException e) {
            // If file cannot be read, print error message
            System.out.println("ERROR: Cannot read file: " + filename);
            System.out.println("Details: " + e.getMessage());
        }
        
        return dataset;
    }
    
    // Helper Method: Try to convert a string to a number
    // If it looks like a number (123 or 45.67), convert it
    // If it's text, keep it as a string
    // Parameters:
    //   - value: The string to try to convert
    // Returns: Double if it's a number, String otherwise
    private static Object tryParseNumber(String value) {
        try {
            // Try to parse as a double
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // If it fails, it's not a number, return as string
            return value;
        }
    }
}