package core;

import evaluation.ConfusionMatrix;
import java.util.*;

/**
 * RESULTS FORMATTER
 * 
 * Purpose: Format and display ML results in a professional, readable way
 * Think of it like: A professional report generator for ML experiments
 * 
 * Real-life analogy: Like creating a polished presentation of your
 * research results for a business meeting or academic paper
 */
public class ResultsFormatter {
    
    /**
     * Format complete ML experiment results
     */
    public static String formatExperimentResults(
        Dataset dataset, 
        Classifier classifier, 
        ConfusionMatrix confusionMatrix,
        long trainingTimeMs,
        long predictionTimeMs,
        Map<String, Object> experimentDetails
    ) {
        StringBuilder report = new StringBuilder();
        
        // Header
        report.append("╔══════════════════════════════════════════════════════════════╗\n");
        report.append("║                    AYOTA ML EXPERIMENT RESULTS                   ║\n");
        report.append("╚══════════════════════════════════════════════════════════════╝\n\n");
        
        // Dataset Information
        report.append("📊 DATASET INFORMATION\n");
        report.append("═".repeat(50)).append("\n");
        report.append(String.format("Dataset Name: %s\n", dataset.getName()));
        report.append(String.format("Total Instances: %,d\n", dataset.getNumInstances()));
        report.append(String.format("Total Features: %d\n", dataset.getNumAttributes() - 1));
        report.append(String.format("Target Variable: %s\n", dataset.getClassAttribute().getName()));
        report.append("\n");
        
        // Feature Summary
        report.append("🔍 FEATURE SUMMARY\n");
        report.append("═".repeat(50)).append("\n");
        for (int i = 0; i < dataset.getNumAttributes() - 1; i++) {
            Attribute attr = dataset.getAttribute(i);
            report.append(String.format("  %-20s [%s]\n", attr.getName(), attr.getType()));
        }
        report.append("\n");
        
        // Algorithm Information
        report.append("🤖 ALGORITHM DETAILS\n");
        report.append("═".repeat(50)).append("\n");
        report.append(String.format("Algorithm: %s\n", classifier.getAlgorithmName()));
        
        if (experimentDetails.containsKey("parameters")) {
            report.append("Parameters:\n");
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) experimentDetails.get("parameters");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                report.append(String.format("  %s: %s\n", entry.getKey(), entry.getValue()));
            }
        }
        report.append("\n");
        
        // Performance Metrics
        report.append("📈 PERFORMANCE METRICS\n");
        report.append("═".repeat(50)).append("\n");
        
        double accuracy = confusionMatrix.getAccuracy();
        report.append(String.format("Overall Accuracy: %.2f%%\n", accuracy));
        
        // Get class-specific metrics
        Set<String> classes = getUniqueClasses(dataset);
        if (classes.size() > 1) {
            report.append("\nPer-Class Metrics:\n");
            report.append(String.format("%-15s %10s %10s %10s\n", "Class", "Precision", "Recall", "F1-Score"));
            report.append("-".repeat(50)).append("\n");
            
            for (String className : classes) {
                double precision = confusionMatrix.getPrecision(className);
                double recall = confusionMatrix.getRecall(className);
                double f1Score = calculateF1Score(precision, recall);
                
                report.append(String.format("%-15s %9.2f%% %9.2f%% %9.2f%%\n", 
                    className, precision, recall, f1Score));
            }
        }
        report.append("\n");
        
        // Confusion Matrix
        report.append("🎯 CONFUSION MATRIX\n");
        report.append("═".repeat(50)).append("\n");
        report.append(formatConfusionMatrix(confusionMatrix, classes));
        report.append("\n");
        
        // Timing Information
        report.append("⏱️  PERFORMANCE TIMING\n");
        report.append("═".repeat(50)).append("\n");
        report.append(String.format("Training Time: %,d ms (%.3f seconds)\n", 
            trainingTimeMs, trainingTimeMs / 1000.0));
        report.append(String.format("Prediction Time: %,d ms (%.3f seconds)\n", 
            predictionTimeMs, predictionTimeMs / 1000.0));
        report.append(String.format("Total Experiment Time: %,d ms (%.3f seconds)\n", 
            trainingTimeMs + predictionTimeMs, (trainingTimeMs + predictionTimeMs) / 1000.0));
        report.append("\n");
        
        // Model Summary
        report.append("🔧 MODEL SUMMARY\n");
        report.append("═".repeat(50)).append("\n");
        report.append(classifier.getModelSummary());
        report.append("\n");
        
        // Recommendations
        report.append("💡 RECOMMENDATIONS\n");
        report.append("═".repeat(50)).append("\n");
        report.append(generateRecommendations(accuracy, classes.size(), dataset.getNumInstances()));
        
        // Footer
        report.append("\n");
        report.append("═".repeat(70)).append("\n");
        report.append("Report generated by AYOTA ML Engine\n");
        report.append("Timestamp: ").append(new Date().toString()).append("\n");
        report.append("═".repeat(70)).append("\n");
        
        return report.toString();
    }
    
    /**
     * Format algorithm comparison results
     */
    public static String formatAlgorithmComparison(List<ExperimentResult> results) {
        StringBuilder report = new StringBuilder();
        
        report.append("╔══════════════════════════════════════════════════════════════╗\n");
        report.append("║                   ALGORITHM COMPARISON REPORT                    ║\n");
        report.append("╚══════════════════════════════════════════════════════════════╝\n\n");
        
        // Summary Table
        report.append("📊 PERFORMANCE COMPARISON\n");
        report.append("═".repeat(80)).append("\n");
        report.append(String.format("%-25s %12s %12s %12s %12s\n", 
            "Algorithm", "Accuracy", "Train Time", "Pred Time", "Total Time"));
        report.append("-".repeat(80)).append("\n");
        
        // Sort results by accuracy
        results.sort((a, b) -> Double.compare(b.accuracy, a.accuracy));
        
        for (ExperimentResult result : results) {
            report.append(String.format("%-25s %11.2f%% %9d ms %9d ms %9d ms\n",
                result.algorithmName,
                result.accuracy,
                result.trainingTime,
                result.predictionTime,
                result.trainingTime + result.predictionTime
            ));
        }
        
        report.append("\n");
        
        // Best Algorithm
        if (!results.isEmpty()) {
            ExperimentResult best = results.get(0);
            report.append("🏆 BEST PERFORMING ALGORITHM\n");
            report.append("═".repeat(50)).append("\n");
            report.append(String.format("Algorithm: %s\n", best.algorithmName));
            report.append(String.format("Accuracy: %.2f%%\n", best.accuracy));
            report.append(String.format("Training Time: %,d ms\n", best.trainingTime));
            report.append(String.format("Prediction Time: %,d ms\n", best.predictionTime));
            report.append("\n");
        }
        
        // Detailed Results for Each Algorithm
        report.append("📋 DETAILED RESULTS\n");
        report.append("═".repeat(50)).append("\n");
        
        for (int i = 0; i < results.size(); i++) {
            ExperimentResult result = results.get(i);
            report.append(String.format("\n%d. %s\n", i + 1, result.algorithmName));
            report.append("-".repeat(40)).append("\n");
            report.append(String.format("   Accuracy: %.2f%%\n", result.accuracy));
            report.append(String.format("   Training Time: %,d ms\n", result.trainingTime));
            report.append(String.format("   Prediction Time: %,d ms\n", result.predictionTime));
            
            if (result.additionalMetrics != null) {
                for (Map.Entry<String, Double> entry : result.additionalMetrics.entrySet()) {
                    report.append(String.format("   %s: %.3f\n", entry.getKey(), entry.getValue()));
                }
            }
        }
        
        return report.toString();
    }
    
    /**
     * Format confusion matrix in a readable table
     */
    private static String formatConfusionMatrix(ConfusionMatrix cm, Set<String> classes) {
        StringBuilder matrix = new StringBuilder();
        
        List<String> classList = new ArrayList<>(classes);
        Collections.sort(classList);
        
        // Header
        matrix.append(String.format("%15s", "Actual \\ Pred"));
        for (String predictedClass : classList) {
            matrix.append(String.format("%12s", predictedClass));
        }
        matrix.append("\n");
        matrix.append("-".repeat(15 + classList.size() * 12)).append("\n");
        
        // Matrix rows
        for (String actualClass : classList) {
            matrix.append(String.format("%15s", actualClass));
            for (String predictedClass : classList) {
                int count = cm.getCount(actualClass, predictedClass);
                matrix.append(String.format("%12d", count));
            }
            matrix.append("\n");
        }
        
        matrix.append("\nOverall Accuracy: ").append(String.format("%.2f%%", cm.getAccuracy())).append("\n");
        
        return matrix.toString();
    }
    
    /**
     * Get unique classes from dataset
     */
    private static Set<String> getUniqueClasses(Dataset dataset) {
        Set<String> classes = new HashSet<>();
        for (int i = 0; i < dataset.getNumInstances(); i++) {
            Instance instance = dataset.getInstance(i);
            classes.add(instance.getClassValue().toString());
        }
        return classes;
    }
    
    /**
     * Calculate F1 Score from precision and recall
     */
    private static double calculateF1Score(double precision, double recall) {
        if (precision + recall == 0) return 0.0;
        return 2 * (precision * recall) / (precision + recall);
    }
    
    /**
     * Generate recommendations based on results
     */
    private static String generateRecommendations(double accuracy, int numClasses, int datasetSize) {
        StringBuilder recommendations = new StringBuilder();
        
        if (accuracy >= 90.0) {
            recommendations.append("✅ Excellent performance! Model is ready for deployment.\n");
        } else if (accuracy >= 80.0) {
            recommendations.append("✅ Good performance. Consider fine-tuning parameters.\n");
        } else if (accuracy >= 70.0) {
            recommendations.append("⚠️  Moderate performance. Try different algorithms or feature engineering.\n");
        } else {
            recommendations.append("❌ Low performance. Consider:\n");
            recommendations.append("   - Collecting more data\n");
            recommendations.append("   - Feature engineering\n");
            recommendations.append("   - Different algorithms\n");
            recommendations.append("   - Data preprocessing\n");
        }
        
        if (datasetSize < 100) {
            recommendations.append("💡 Small dataset detected. Consider:\n");
            recommendations.append("   - Cross-validation for robust evaluation\n");
            recommendations.append("   - Simple algorithms (Naive Bayes, KNN)\n");
        }
        
        if (numClasses > 5) {
            recommendations.append("💡 Multi-class problem detected. Consider:\n");
            recommendations.append("   - One-vs-all approaches\n");
            recommendations.append("   - Ensemble methods\n");
        }
        
        return recommendations.toString();
    }
    
    /**
     * Helper class to store experiment results
     */
    public static class ExperimentResult {
        public String algorithmName;
        public double accuracy;
        public long trainingTime;
        public long predictionTime;
        public Map<String, Double> additionalMetrics;
        
        public ExperimentResult(String algorithmName, double accuracy, 
                              long trainingTime, long predictionTime) {
            this.algorithmName = algorithmName;
            this.accuracy = accuracy;
            this.trainingTime = trainingTime;
            this.predictionTime = predictionTime;
            this.additionalMetrics = new HashMap<>();
        }
    }
}