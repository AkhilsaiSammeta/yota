package core;

import java.util.*;

/**
 * DECISION TREE CLASSIFIER
 * 
 * Purpose: Implement Decision Tree algorithm from scratch
 * Think of it like: A series of yes/no questions to make decisions
 * 
 * Real-life analogy: Like a flowchart for hiring decisions -
 * "If age > 30 AND salary > 50000, then likely to be hired"
 */
public class DecisionTreeClassifier {
    
    private TreeNode root;
    private Dataset trainingData;
    private int maxDepth = 10;
    private int minSamplesLeaf = 2;
    
    // Constructor
    public DecisionTreeClassifier() {
        this.root = null;
    }
    
    public DecisionTreeClassifier(int maxDepth, int minSamplesLeaf) {
        this.maxDepth = maxDepth;
        this.minSamplesLeaf = minSamplesLeaf;
    }
    
    /**
     * Train the decision tree
     */
    public void train(Dataset dataset) {
        this.trainingData = dataset;
        this.root = buildTree(dataset.getInstances(), 0);
    }
    
    /**
     * Make prediction for new instance
     */
    public Object predict(Instance instance) {
        if (root == null) {
            return null;
        }
        return predictRecursive(root, instance);
    }
    
    /**
     * Build decision tree recursively
     */
    private TreeNode buildTree(List<Instance> instances, int depth) {
        // Base cases
        if (instances.isEmpty() || depth >= maxDepth || instances.size() < minSamplesLeaf) {
            return new TreeNode(getMajorityClass(instances));
        }
        
        // Check if all instances have same class
        Object firstClass = instances.get(0).getClassValue();
        boolean allSameClass = true;
        for (Instance inst : instances) {
            if (!inst.getClassValue().equals(firstClass)) {
                allSameClass = false;
                break;
            }
        }
        
        if (allSameClass) {
            return new TreeNode(firstClass);
        }
        
        // Find best split
        Split bestSplit = findBestSplit(instances);
        if (bestSplit == null) {
            return new TreeNode(getMajorityClass(instances));
        }
        
        // Create node and split data
        TreeNode node = new TreeNode();
        node.attributeIndex = bestSplit.attributeIndex;
        node.threshold = bestSplit.threshold;
        
        List<Instance> leftInstances = new ArrayList<>();
        List<Instance> rightInstances = new ArrayList<>();
        
        for (Instance inst : instances) {
            Object value = inst.getValue(bestSplit.attributeIndex);
            if (value instanceof Double && (Double) value <= bestSplit.threshold) {
                leftInstances.add(inst);
            } else {
                rightInstances.add(inst);
            }
        }
        
        // Recursively build subtrees
        node.leftChild = buildTree(leftInstances, depth + 1);
        node.rightChild = buildTree(rightInstances, depth + 1);
        
        return node;
    }
    
    /**
     * Find the best split point
     */
    private Split findBestSplit(List<Instance> instances) {
        Split bestSplit = null;
        double bestGini = Double.MAX_VALUE;
        
        // Try all numeric attributes
        for (int attrIndex = 0; attrIndex < trainingData.getNumAttributes() - 1; attrIndex++) {
            Attribute attr = trainingData.getAttribute(attrIndex);
            
            if (!"numeric".equals(attr.getType())) continue;
            
            // Get unique values for this attribute
            Set<Double> uniqueValues = new HashSet<>();
            for (Instance inst : instances) {
                Object value = inst.getValue(attrIndex);
                if (value instanceof Double) {
                    uniqueValues.add((Double) value);
                }
            }
            
            // Try each unique value as threshold
            for (Double threshold : uniqueValues) {
                List<Instance> left = new ArrayList<>();
                List<Instance> right = new ArrayList<>();
                
                for (Instance inst : instances) {
                    Object value = inst.getValue(attrIndex);
                    if (value instanceof Double && (Double) value <= threshold) {
                        left.add(inst);
                    } else {
                        right.add(inst);
                    }
                }
                
                if (left.isEmpty() || right.isEmpty()) continue;
                
                // Calculate weighted Gini impurity
                double leftGini = calculateGini(left);
                double rightGini = calculateGini(right);
                double weightedGini = (left.size() * leftGini + right.size() * rightGini) / instances.size();
                
                if (weightedGini < bestGini) {
                    bestGini = weightedGini;
                    bestSplit = new Split(attrIndex, threshold);
                }
            }
        }
        
        return bestSplit;
    }
    
    /**
     * Calculate Gini impurity
     */
    private double calculateGini(List<Instance> instances) {
        if (instances.isEmpty()) return 0.0;
        
        Map<Object, Integer> classCounts = new HashMap<>();
        for (Instance inst : instances) {
            Object classValue = inst.getClassValue();
            classCounts.put(classValue, classCounts.getOrDefault(classValue, 0) + 1);
        }
        
        double gini = 1.0;
        int total = instances.size();
        
        for (Integer count : classCounts.values()) {
            double probability = (double) count / total;
            gini -= probability * probability;
        }
        
        return gini;
    }
    
    /**
     * Get majority class from instances
     */
    private Object getMajorityClass(List<Instance> instances) {
        if (instances.isEmpty()) return null;
        
        Map<Object, Integer> classCounts = new HashMap<>();
        for (Instance inst : instances) {
            Object classValue = inst.getClassValue();
            classCounts.put(classValue, classCounts.getOrDefault(classValue, 0) + 1);
        }
        
        Object majorityClass = null;
        int maxCount = 0;
        for (Map.Entry<Object, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityClass = entry.getKey();
            }
        }
        
        return majorityClass;
    }
    
    /**
     * Predict using tree recursively
     */
    private Object predictRecursive(TreeNode node, Instance instance) {
        if (node.isLeaf()) {
            return node.classValue;
        }
        
        Object value = instance.getValue(node.attributeIndex);
        if (value instanceof Double && (Double) value <= node.threshold) {
            return predictRecursive(node.leftChild, instance);
        } else {
            return predictRecursive(node.rightChild, instance);
        }
    }
    
    /**
     * Get tree structure as string for display
     */
    public String getTreeStructure() {
        if (root == null) return "Empty tree";
        return getTreeString(root, "", true);
    }
    
    private String getTreeString(TreeNode node, String prefix, boolean isLast) {
        StringBuilder sb = new StringBuilder();
        
        if (node.isLeaf()) {
            sb.append(prefix).append(isLast ? "└── " : "├── ")
              .append("Class: ").append(node.classValue).append("\n");
        } else {
            String attrName = trainingData.getAttribute(node.attributeIndex).getName();
            sb.append(prefix).append(isLast ? "└── " : "├── ")
              .append(attrName).append(" <= ").append(String.format("%.2f", node.threshold)).append("\n");
            
            String newPrefix = prefix + (isLast ? "    " : "│   ");
            sb.append(getTreeString(node.leftChild, newPrefix, false));
            sb.append(getTreeString(node.rightChild, newPrefix, true));
        }
        
        return sb.toString();
    }
    
    // Helper classes
    private static class TreeNode {
        int attributeIndex = -1;
        double threshold = 0.0;
        Object classValue = null;
        TreeNode leftChild = null;
        TreeNode rightChild = null;
        
        public TreeNode() {}
        
        public TreeNode(Object classValue) {
            this.classValue = classValue;
        }
        
        public boolean isLeaf() {
            return classValue != null;
        }
    }
    
    private static class Split {
        int attributeIndex;
        double threshold;
        
        public Split(int attributeIndex, double threshold) {
            this.attributeIndex = attributeIndex;
            this.threshold = threshold;
        }
    }
}