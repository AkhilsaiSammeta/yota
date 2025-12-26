package core;

import java.util.ArrayList;

/**
 * INSTANCE CLASS
 * 
 * Purpose: Represents ONE ROW of data
 * Think of it like: One record/entry in the dataset
 * 
 * Real-life analogy: Like one person's information (age, salary, job status)
 */

public class Instance {
    
    // Store the values in this row
    // Example: [25, 45000, "Yes"] for one person
    // Using ArrayList so we can add values dynamically
    private ArrayList<Object> values;
    
    // Store the class/label for this row (for ML training)
    // Example: "Hired" or "Rejected"
    private Object classValue;
    
    // Constructor: Create an Instance with an empty list of values
    public Instance() {
        this.values = new ArrayList<Object>();
        this.classValue = null;
    }
    
    // Method: Add a value to this row
    // Parameters:
    //   - value: What value to add? (can be number or text)
    public void addValue(Object value) {
        values.add(value);
    }
    
    // Method: Get a specific value from this row
    // Parameters:
    //   - index: Which position? (0 = first value, 1 = second, etc.)
    public Object getValue(int index) {
        if (index >= 0 && index < values.size()) {
            return values.get(index);
        }
        return null;
    }
    
    // Method: Get how many values this row has
    public int getNumValues() {
        return values.size();
    }
    
    // Method: Set the class/label for this instance
    // Example: setClassValue("Hired")
    public void setClassValue(Object classValue) {
        this.classValue = classValue;
    }
    
    // Method: Get the class/label of this instance
    public Object getClassValue() {
        return classValue;
    }
    
    // toString: Print this instance in readable format
    // Example: "Instance{[25, 45000, Yes] -> Hired}"
    public String toString() {
        return "Instance{" + values.toString() + " -> " + classValue + "}";
    }
}