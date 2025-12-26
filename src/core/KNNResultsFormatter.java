package core;

import evaluation.ConfusionMatrix;
import java.util.*;

/**
 * ENHANCED KNN RESULTS FORMATTER
 * 
 * Purpose: Professional formatting for KNN classification results with detailed diagnostics
 * Helps identify issues when accuracy is unexpectedly low
 */
public class KNNResultsFormatter {
    
    /**
     * Format detailed KNN classification results with diagnostics
     */
    public static String formatKNNResults(
        int trainingInstances,
        int testInstances, 
        int kValue,
        double accuracy,
        int correctPredictions,
        int totalPredictions,
        ConfusionMatrix confusionMatrix,
        long trainingTime,
        long predictionTime,
        boolean includeDebugInfo
    ) {
        StringBuilder report = new StringBuilder();
        
        // Header with visual appeal
        report.append("\n");
        report.append("╔═══════════════════════════════════════════════════════════════╗\n");
        report.append("║           🤖 K-NEAREST NEIGHBORS CLASSIFICATION RESULTS        ║\n");
        report.append("╚═══════════════════════════════════════════════════════════════╝\n");
        report.append("\n");
        
        // Dataset Overview
        report.append("📊 DATASET OVERVIEW\n");
        report.append("═".repeat(65)).append("\n");
        report.append(String.format("%-25s %,d instances\n", "Training Set Size:", trainingInstances));
        report.append(String.format("%-25s %,d instances\n", "Test Set Size:", testInstances));
        report.append(String.format("%-25s %.1f%% / %.1f%%\n", "Train/Test Split:", 
            (trainingInstances * 100.0) / (trainingInstances + testInstances),
            (testInstances * 100.0) / (trainingInstances + testInstances)));
        report.append(String.format("%-25s %d neighbors\n", "K Value:", kValue));
        report.append("\n");
        
        // Performance Results
        report.append("🎯 CLASSIFICATION PERFORMANCE\n");
        report.append("═".repeat(65)).append("\n");
        
        // Format accuracy with appropriate color coding (conceptually)
        String accuracyStatus = getAccuracyStatus(accuracy);
        report.append(String.format("%-25s %.2f%% %s\n", "Overall Accuracy:", accuracy, accuracyStatus));
        report.append(String.format("%-25s %,d out of %,d\n", "Correct Predictions:", correctPredictions, totalPredictions));
        report.append(String.format("%-25s %,d\n", "Incorrect Predictions:", totalPredictions - correctPredictions));
        report.append("\n");
        
        // Performance Analysis
        if (accuracy < 10.0) {
            report.append("⚠️  PERFORMANCE ALERT\n");
            report.append("═".repeat(65)).append("\n");
            report.append("❌ Very Low Accuracy Detected! Possible Issues:\n");
            report.append("   • Data may need normalization/scaling\n");
            report.append("   • Features might not be predictive\n");
            report.append("   • Class distribution heavily imbalanced\n");
            report.append("   • K value may be too high for dataset size\n");
            report.append("   • Distance calculation issues\n");
            report.append("\n");
        } else if (accuracy < 50.0) {
            report.append("⚠️  PERFORMANCE CONCERN\n");
            report.append("═".repeat(65)).append("\n");
            report.append("🔶 Low accuracy. Consider:\n");
            report.append("   • Feature engineering or selection\n");
            report.append("   • Data preprocessing (scaling, normalization)\n");
            report.append("   • Trying different K values\n");
            report.append("   • Checking for class imbalance\n");
            report.append("\n");
        } else if (accuracy > 95.0) {
            report.append("✅ EXCELLENT PERFORMANCE\n");
            report.append("═".repeat(65)).append("\n");
            report.append("🎉 Outstanding results! Model is ready for deployment.\n");
            report.append("\n");
        }
        
        // Algorithm Details
        report.append("🔧 ALGORITHM CONFIGURATION\n");
        report.append("═".repeat(65)).append("\n");
        report.append(String.format("%-25s K-Nearest Neighbors\n", "Algorithm:"));
        report.append(String.format("%-25s %d\n", "K (Neighbors):", kValue));
        report.append(String.format("%-25s Euclidean Distance\n", "Distance Metric:"));
        report.append(String.format("%-25s Majority Voting\n", "Prediction Method:"));
        report.append(String.format("%-25s Lazy Learning\n", "Learning Type:"));
        report.append("\n");
        
        // Timing Performance
        report.append("⏱️  EXECUTION TIMING\n");
        report.append("═".repeat(65)).append("\n");
        report.append(String.format("%-25s %,d ms (%.3f seconds)\n", "Training Time:", trainingTime, trainingTime / 1000.0));
        report.append(String.format("%-25s %,d ms (%.3f seconds)\n", "Prediction Time:", predictionTime, predictionTime / 1000.0));
        report.append(String.format("%-25s %,d ms (%.3f seconds)\n", "Total Time:", trainingTime + predictionTime, (trainingTime + predictionTime) / 1000.0));
        
        if (testInstances > 0) {
            double avgPredictionTime = (double) predictionTime / testInstances;
            report.append(String.format("%-25s %.3f ms per prediction\n", "Avg Prediction Time:", avgPredictionTime));
        }
        report.append("\n");
        
        // Confusion Matrix (if available)
        if (confusionMatrix != null) {
            report.append("📊 CONFUSION MATRIX\n");
            report.append("═".repeat(65)).append("\n");
            report.append(formatConfusionMatrix(confusionMatrix));
            report.append("\n");
            
            // Per-class metrics
            report.append("📈 PER-CLASS METRICS\n");
            report.append("═".repeat(65)).append("\n");
            report.append(formatPerClassMetrics(confusionMatrix));
            report.append("\n");
        }
        
        // Recommendations based on performance
        report.append("💡 RECOMMENDATIONS\n");
        report.append("═".repeat(65)).append("\n");
        if (accuracy < 10.0) {
            report.append("🚨 Critical Issues - Immediate Actions Needed:\n");
            report.append("   1. Check data preprocessing pipeline\n");
            report.append("   2. Verify feature scaling/normalization\n");
            report.append("   3. Examine class distribution\n");
            report.append("   4. Try K=1 to test basic functionality\n");
            report.append("   5. Debug distance calculations\n");
        } else if (accuracy < 50.0) {
            report.append("🔧 Optimization Suggestions:\n");
            report.append("   1. Experiment with different K values\n");
            report.append("   2. Apply feature scaling/normalization\n");
            report.append("   3. Consider feature selection\n");
            report.append("   4. Check for outliers in data\n");
        } else if (accuracy < 80.0) {
            report.append("📈 Performance Improvement Options:\n");
            report.append("   1. Fine-tune K parameter\n");
            report.append("   2. Try weighted distance voting\n");
            report.append("   3. Consider ensemble methods\n");
        } else {
            report.append("✅ Great performance! Consider:\n");
            report.append("   1. Cross-validation for robust evaluation\n");
            report.append("   2. Testing on additional datasets\n");
            report.append("   3. Model deployment preparation\n");
        }
        report.append("\n");
        
        // Footer
        report.append("═".repeat(65)).append("\n");
        report.append("Report generated by YOTA ML Engine - Enhanced KNN Analyzer\n");
        report.append(String.format("Generated on: %s\n", new Date().toString()));
        report.append("═".repeat(65)).append("\n");
        
        return report.toString();
    }
    
    /**
     * Get accuracy status indicator
     */
    private static String getAccuracyStatus(double accuracy) {
        if (accuracy < 10.0) return "🔴 CRITICAL";
        if (accuracy < 30.0) return "🟡 POOR";
        if (accuracy < 50.0) return "🟠 BELOW AVERAGE";
        if (accuracy < 70.0) return "🟡 MODERATE";
        if (accuracy < 85.0) return "🟢 GOOD";
        if (accuracy < 95.0) return "🟢 EXCELLENT";
        return "🟢 OUTSTANDING";
    }
    
    /**
     * Format confusion matrix for display
     */
    private static String formatConfusionMatrix(ConfusionMatrix matrix) {
        StringBuilder sb = new StringBuilder();
        // This would need to be implemented based on your ConfusionMatrix class structure
        // For now, return a placeholder
        sb.append("  Actual \\ Predicted    Class1    Class2    ...\n");
        sb.append("  ────────────────────────────────────────────\n");
        sb.append("  Class1                   TP        FN       \n");
        sb.append("  Class2                   FP        TN       \n");
        sb.append(String.format("\n  Overall Accuracy: %.2f%%\n", matrix.getAccuracy()));
        return sb.toString();
    }
    
    /**
     * Format per-class precision, recall, F1-score
     */
    private static String formatPerClassMetrics(ConfusionMatrix matrix) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %10s %10s %10s\n", "Class", "Precision", "Recall", "F1-Score"));
        sb.append("─".repeat(55)).append("\n");
        // This would need actual implementation based on your ConfusionMatrix class
        sb.append("(Implementation depends on ConfusionMatrix class methods)\n");
        return sb.toString();
    }
    
    /**
     * Quick format for simple results display
     */
    public static String formatSimpleResults(int correct, int total, double accuracy) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n🎯 QUICK RESULTS SUMMARY\n");
        sb.append("═".repeat(30)).append("\n");
        sb.append(String.format("Accuracy:  %.2f%% %s\n", accuracy, getAccuracyStatus(accuracy)));
        sb.append(String.format("Correct:   %d/%d predictions\n", correct, total));
        sb.append(String.format("Incorrect: %d predictions\n", total - correct));
        
        if (accuracy < 10.0) {
            sb.append("\n❌ ALERT: Very low accuracy - check data/algorithm!\n");
        }
        
        return sb.toString();
    }
}