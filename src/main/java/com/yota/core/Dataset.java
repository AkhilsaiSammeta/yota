package main.java.com.yota.core;

import java.util.ArrayList;

/**
 * DATASET CLASS (Spring Boot Version)
 * 
 * Purpose: Container for ALL data (like a table)
 * Think of it like: A complete Excel spreadsheet
 * 
 * Real-life analogy: Like a database table with column headers and rows
 */
public class Dataset {
    
    // Store the attributes (column definitions)
    // Example: age (numeric), salary (numeric), hired (categorical)
    private ArrayList<Attribute> attributes;
    
    // Store all instances (rows)
    // Example: Row1, Row2, Row3, ... RowN
    private ArrayList<Instance> instances;
    
    // Store the name of this dataset
    // Example: "Employee Data", "Iris Dataset"
    private String name;
    
    // Constructor: Create an empty dataset with a name
    public Dataset(String name) {
        this.name = name;
        this.attributes = new ArrayList<Attribute>();
        this.instances = new ArrayList<Instance>();
    }
    
    // Method: Add an attribute (column) to the dataset
    // Parameters:
    //   - attribute: The column to add
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }
    
    // Method: Add an instance (row) to the dataset
    // Parameters:
    //   - instance: The row to add
    public void addInstance(Instance instance) {
        instances.add(instance);
    }
    
    // Method: Get a specific attribute by index
    // Parameters:
    //   - index: Which column? (0 = first, 1 = second)
    public Attribute getAttribute(int index) {
        if (index >= 0 && index < attributes.size()) {
            return attributes.get(index);
        }
        return null;
    }
    
    // Method: Get a specific instance by index
    // Parameters:
    //   - index: Which row? (0 = first, 1 = second)
    public Instance getInstance(int index) {
        if (index >= 0 && index < instances.size()) {
            return instances.get(index);
        }
        return null;
    }
    
    // Method: Get the total number of attributes (columns)
    public int getNumAttributes() {
        return attributes.size();
    }
    
    // Method: Get the total number of instances (rows)
    public int getNumInstances() {
        return instances.size();
    }
    
    // Method: Get the name of this dataset
    public String getName() {
        return name;
    }
    
    // Method: Get the class attribute (last attribute by convention)
    // Why last? WEKA convention: last column is always the class/label
    public Attribute getClassAttribute() {
        if (attributes.size() > 0) {
            return attributes.get(attributes.size() - 1);
        }
        return null;
    }
    
    // Method: Get all attributes
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
    
    // Method: Get all instances
    public ArrayList<Instance> getInstances() {
        return instances;
    }
    
    // toString: Print summary of the dataset
    public String toString() {
        return "Dataset{" + name + 
               " | Attributes: " + attributes.size() + 
               " | Instances: " + instances.size() + "}";
    }
}