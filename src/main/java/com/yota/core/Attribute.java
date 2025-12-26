package com.yota.core;

/**
 * ATTRIBUTE CLASS (Spring Boot Version)
 * 
 * Purpose: Represents one column/feature in a dataset
 * Think of it like: Column header + metadata about that column
 * 
 * Real-life analogy: Like a column in Excel with a name and type
 */
public class Attribute {
    
    // Store the name of this column
    // Example: "Age", "Salary", "IsEmployee"
    private String name;
    
    // Store the type of this column
    // Can be: "numeric" (numbers) or "categorical" (text categories)
    private String type;
    
    // Constructor: Create an Attribute with a name and type
    // Parameters:
    //   - name: What is this column called?
    //   - type: Is it numeric or categorical?
    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    // Getter: Get the name of this attribute
    public String getName() {
        return name;
    }
    
    // Getter: Get the type of this attribute
    public String getType() {
        return type;
    }
    
    // toString: Convert to readable text for printing
    public String toString() {
        return "Attribute{" + name + " [" + type + "]}";
    }
}