package core;

import java.util.*;

/**
 * KNN DIAGNOSTIC TOOL
 * 
 * Purpose: Analyze KNN performance issues and provide detailed debugging information
 * Helps identify why accuracy might be unexpectedly low
 */
public class KNNDiagnostics {
    
    /**
     * Comprehensive diagnostic analysis for KNN classifier
     */
    public static String analyzeLowAccuracy(
        Dataset dataset,
        KNNClassifier classifier,
        Dataset trainingSet,
        Dataset testSet,
        int kValue
    ) {
        StringBuilder diagnosis = new StringBuilder();
        
        diagnosis.append("\n╔═══════════════════════════════════════════════════════════════╗\n");
        diagnosis.append("║               🔍 KNN DIAGNOSTIC ANALYSIS                        ║\n");
        diagnosis.append("╚═══════════════════════════════════════════════════════════════╝\n\n");
        
        // 1. Data Distribution Analysis
        diagnosis.append("1️⃣  DATA DISTRIBUTION ANALYSIS\n");
        diagnosis.append("═".repeat(60)).append("\n");
        analyzeClassDistribution(dataset, diagnosis);
        diagnosis.append("\n");
        
        // 2. Feature Analysis
        diagnosis.append("2️⃣  FEATURE ANALYSIS\n");
        diagnosis.append("═".repeat(60)).append("\n");
        analyzeFeatures(dataset, diagnosis);
        diagnosis.append("\n");
        
        // 3. K-Value Analysis
        diagnosis.append("3️⃣  K-VALUE ANALYSIS\n");
        diagnosis.append("═".repeat(60)).append("\n");
        analyzeKValue(kValue, trainingSet.getNumInstances(), diagnosis);
        diagnosis.append("\n");
        
        // 4. Data Quality Checks
        diagnosis.append("4️⃣  DATA QUALITY CHECKS\n");
        diagnosis.append("═".repeat(60)).append("\n");
        checkDataQuality(dataset, diagnosis);
        diagnosis.append("\n");
        
        // 5. Distance Calculation Check
        diagnosis.append("5️⃣  DISTANCE CALCULATION CHECK\n");
        diagnosis.append("═".repeat(60)).append("\n");
        checkDistanceCalculation(trainingSet, diagnosis);
        diagnosis.append("\n");
        
        // 6. Recommendations
        diagnosis.append("6️⃣  DIAGNOSTIC RECOMMENDATIONS\n");
        diagnosis.append("═".repeat(60)).append("\n");
        generateRecommendations(dataset, kValue, diagnosis);
        
        return diagnosis.toString();
    }
    
    /**
     * Analyze class distribution for imbalance issues
     */
    private static void analyzeClassDistribution(Dataset dataset, StringBuilder diagnosis) {
        Map<Object, Integer> classCount = new HashMap<>();
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Object classValue = dataset.getInstance(i).getClassValue();
            classCount.put(classValue, classCount.getOrDefault(classValue, 0) + 1);
        }
        
        diagnosis.append(String.format("Total Classes: %d\n", classCount.size()));
        diagnosis.append("Class Distribution:\n");
        
        int totalInstances = dataset.getNumInstances();
        Object majorityClass = null;
        int maxCount = 0;
        Object minorityClass = null;
        int minCount = Integer.MAX_VALUE;
        
        for (Map.Entry<Object, Integer> entry : classCount.entrySet()) {
            Object className = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalInstances;
            
            diagnosis.append(String.format("  %-15s: %4d instances (%.1f%%)\n", 
                className, count, percentage));
            
            if (count > maxCount) {
                maxCount = count;
                majorityClass = className;
            }
            if (count < minCount) {
                minCount = count;
                minorityClass = className;
            }
        }
        
        // Check for severe class imbalance
        double imbalanceRatio = (double) maxCount / minCount;
        if (imbalanceRatio > 10) {
            diagnosis.append(String.format("\n⚠️  SEVERE CLASS IMBALANCE DETECTED!\n"));
            diagnosis.append(String.format("   Majority class (%s): %d instances\n", majorityClass, maxCount));
            diagnosis.append(String.format("   Minority class (%s): %d instances\n", minorityClass, minCount));
            diagnosis.append(String.format("   Imbalance ratio: %.1f:1\n", imbalanceRatio));
            diagnosis.append("   This can cause poor KNN performance!\n");
        } else if (imbalanceRatio > 3) {
            diagnosis.append(String.format("\n🔶 Moderate class imbalance detected (%.1f:1)\n", imbalanceRatio));
        } else {
            diagnosis.append("\n✅ Class distribution appears balanced\n");
        }
    }
    
    /**
     * Analyze features for potential issues
     */
    private static void analyzeFeatures(Dataset dataset, StringBuilder diagnosis) {
        diagnosis.append(String.format("Total Features: %d\n", dataset.getNumAttributes() - 1));
        
        // Analyze each feature
        for (int i = 0; i < dataset.getNumAttributes() - 1; i++) {
            Attribute attr = dataset.getAttribute(i);
            diagnosis.append(String.format("\nFeature %d: %s (%s)\n", i+1, attr.getName(), attr.getType()));
            
            if ("numeric".equals(attr.getType())) {
                analyzeNumericFeature(dataset, i, diagnosis);
            } else {
                analyzeCategoricalFeature(dataset, i, diagnosis);
            }
        }
    }
    
    /**
     * Analyze numeric features for scaling issues
     */
    private static void analyzeNumericFeature(Dataset dataset, int featureIndex, StringBuilder diagnosis) {
        List<Double> values = new ArrayList<>();
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Object value = dataset.getInstance(i).getValue(featureIndex);
            if (value instanceof Double) {
                values.add((Double) value);
            }
        }
        
        if (values.isEmpty()) {
            diagnosis.append("  ❌ No valid numeric values found!\n");
            return;
        }
        
        Collections.sort(values);
        double min = values.get(0);
        double max = values.get(values.size() - 1);
        double range = max - min;
        
        // Calculate mean
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        double mean = sum / values.size();
        
        diagnosis.append(String.format("  Range: %.2f to %.2f (span: %.2f)\n", min, max, range));
        diagnosis.append(String.format("  Mean: %.2f\n", mean));
        
        // Check for scaling issues
        if (range > 1000) {
            diagnosis.append("  ⚠️  Large value range - consider normalization!\n");
        } else if (range < 0.01) {
            diagnosis.append("  ⚠️  Very small value range - may have limited discriminative power\n");
        } else {
            diagnosis.append("  ✅ Value range appears reasonable\n");
        }
    }
    
    /**
     * Analyze categorical features
     */
    private static void analyzeCategoricalFeature(Dataset dataset, int featureIndex, StringBuilder diagnosis) {
        Set<Object> uniqueValues = new HashSet<>();
        
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Object value = dataset.getInstance(i).getValue(featureIndex);
            if (value != null) {
                uniqueValues.add(value);
            }
        }
        
        diagnosis.append(String.format("  Unique values: %d\n", uniqueValues.size()));
        
        if (uniqueValues.size() == dataset.getNumInstances()) {
            diagnosis.append("  ⚠️  All values are unique - may not be useful for classification!\n");
        } else if (uniqueValues.size() == 1) {
            diagnosis.append("  ❌ Only one unique value - this feature is useless!\n");
        } else {
            diagnosis.append("  ✅ Reasonable number of unique values\n");
        }
    }
    
    /**
     * Analyze K-value appropriateness
     */
    private static void analyzeKValue(int k, int trainingSize, StringBuilder diagnosis) {
        diagnosis.append(String.format("K Value: %d\n", k));
        diagnosis.append(String.format("Training Set Size: %,d\n", trainingSize));
        
        double kRatio = (double) k / trainingSize;
        
        if (k >= trainingSize) {
            diagnosis.append("❌ CRITICAL: K is greater than or equal to training set size!\n");
            diagnosis.append("   This will cause poor performance. Reduce K value.\n");
        } else if (kRatio > 0.1) {
            diagnosis.append("⚠️  K is quite large relative to training set size\n");
            diagnosis.append(String.format("   K represents %.1f%% of training data\n", kRatio * 100));
            diagnosis.append("   Consider using a smaller K value\n");
        } else if (k == 1) {
            diagnosis.append("🔶 K=1 (nearest neighbor only)\n");
            diagnosis.append("   May be sensitive to noise but good for debugging\n");
        } else if (k % 2 == 0) {
            diagnosis.append("🔶 K is even - may result in tie votes\n");
            diagnosis.append("   Consider using an odd number for K\n");
        } else {
            diagnosis.append("✅ K value appears reasonable\n");
        }
        
        // Suggest optimal K range
        int suggestedMinK = 1;
        int suggestedMaxK = (int) Math.sqrt(trainingSize);
        if (suggestedMaxK % 2 == 0) suggestedMaxK++; // Make it odd
        
        diagnosis.append(String.format("💡 Suggested K range: %d to %d (odd numbers)\n", 
            suggestedMinK, Math.min(suggestedMaxK, 15)));
    }
    
    /**
     * Check overall data quality
     */
    private static void checkDataQuality(Dataset dataset, StringBuilder diagnosis) {
        int totalInstances = dataset.getNumInstances();
        int missingValues = 0;
        int duplicateInstances = 0;
        
        // Check for missing values and duplicates (simplified check)
        Set<String> instanceStrings = new HashSet<>();
        
        for (int i = 0; i < totalInstances; i++) {
            Instance instance = dataset.getInstance(i);
            StringBuilder instanceStr = new StringBuilder();
            
            for (int j = 0; j < instance.getNumValues(); j++) {
                Object value = instance.getValue(j);
                if (value == null) {
                    missingValues++;
                }
                instanceStr.append(value).append(",");
            }
            
            String instStr = instanceStr.toString();
            if (instanceStrings.contains(instStr)) {
                duplicateInstances++;
            } else {
                instanceStrings.add(instStr);
            }
        }
        
        diagnosis.append(String.format("Total Instances: %,d\n", totalInstances));
        diagnosis.append(String.format("Missing Values: %,d\n", missingValues));
        diagnosis.append(String.format("Potential Duplicates: %,d\n", duplicateInstances));
        
        if (missingValues > 0) {
            diagnosis.append("⚠️  Missing values detected - may affect distance calculations\n");
        }
        
        if (duplicateInstances > totalInstances * 0.1) {
            diagnosis.append("⚠️  High number of duplicate instances detected\n");
        }
        
        if (totalInstances < 100) {
            diagnosis.append("🔶 Small dataset - results may not be reliable\n");
            diagnosis.append("   Consider gathering more data if possible\n");
        }
    }
    
    /**
     * Check distance calculation validity
     */
    private static void checkDistanceCalculation(Dataset trainingSet, StringBuilder diagnosis) {
        if (trainingSet.getNumInstances() < 2) {
            diagnosis.append("❌ Insufficient training data for distance analysis\n");
            return;
        }
        
        // Calculate sample distances
        Instance instance1 = trainingSet.getInstance(0);
        Instance instance2 = trainingSet.getInstance(1);
        
        // Simplified distance calculation check
        boolean hasNumericFeatures = false;
        double totalDistance = 0.0;
        
        for (int i = 0; i < Math.min(instance1.getNumValues(), instance2.getNumValues()) - 1; i++) {
            Object val1 = instance1.getValue(i);
            Object val2 = instance2.getValue(i);
            
            if (val1 instanceof Double && val2 instanceof Double) {
                hasNumericFeatures = true;
                double diff = (Double) val1 - (Double) val2;
                totalDistance += diff * diff;
            }
        }
        
        if (!hasNumericFeatures) {
            diagnosis.append("❌ No numeric features found for distance calculation!\n");
            diagnosis.append("   KNN requires numeric features for Euclidean distance\n");
        } else {
            double euclideanDistance = Math.sqrt(totalDistance);
            diagnosis.append(String.format("Sample distance calculation: %.4f\n", euclideanDistance));
            
            if (euclideanDistance == 0.0) {
                diagnosis.append("⚠️  Zero distance detected - check for identical instances\n");
            } else if (Double.isNaN(euclideanDistance) || Double.isInfinite(euclideanDistance)) {
                diagnosis.append("❌ Invalid distance calculation - check for data issues\n");
            } else {
                diagnosis.append("✅ Distance calculation appears to be working\n");
            }
        }
    }
    
    /**
     * Generate specific recommendations based on analysis
     */
    private static void generateRecommendations(Dataset dataset, int kValue, StringBuilder diagnosis) {
        diagnosis.append("Based on the diagnostic analysis, here are specific recommendations:\n\n");
        
        diagnosis.append("🔧 IMMEDIATE ACTIONS:\n");
        diagnosis.append("1. Try K=1 first to test if basic algorithm works\n");
        diagnosis.append("2. Check if features are properly numeric (not strings)\n");
        diagnosis.append("3. Verify that training and test sets are from same distribution\n");
        diagnosis.append("4. Ensure class labels match exactly (case-sensitive)\n\n");
        
        diagnosis.append("📊 DATA PREPROCESSING:\n");
        diagnosis.append("1. Normalize/scale all numeric features to [0,1] range\n");
        diagnosis.append("2. Remove features with only one unique value\n");
        diagnosis.append("3. Handle missing values appropriately\n");
        diagnosis.append("4. Check for and remove duplicate instances\n\n");
        
        diagnosis.append("⚙️ ALGORITHM TUNING:\n");
        diagnosis.append("1. Test different K values: 1, 3, 5, 7, 9\n");
        diagnosis.append("2. Consider weighted voting by distance\n");
        diagnosis.append("3. Try different distance metrics if available\n");
        diagnosis.append("4. Implement cross-validation for better evaluation\n\n");
        
        diagnosis.append("🎯 DEBUGGING STEPS:\n");
        diagnosis.append("1. Print first few predictions with actual distances\n");
        diagnosis.append("2. Check if majority class prediction gives better results\n");
        diagnosis.append("3. Manually verify a few predictions\n");
        diagnosis.append("4. Compare with a simple baseline (random prediction)\n");
    }
}