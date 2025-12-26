import core.KNNResultsFormatter;
import evaluation.ConfusionMatrix;

/**
 * QUICK KNN RESULTS REFORMATTER
 * 
 * Purpose: Quick utility to reformat KNN results with your specific numbers
 * Just run this with your data to get beautifully formatted output
 */
public class QuickKNNFormatter {
    
    public static void main(String[] args) {
        // Your specific results that need reformatting
        reformatYourResults();
    }
    
    public static void reformatYourResults() {
        System.out.println("🎯 REFORMATTING YOUR KNN RESULTS\n");
        
        // Your exact numbers
        int trainingInstances = 1021;
        int testInstances = 439;
        int kValue = 3;
        double accuracy = 0.23; // 0.23%
        int correctPredictions = 1;
        int totalPredictions = 439;
        long trainingTime = 5; // Estimated
        long predictionTime = 150; // Estimated
        
        // Create mock confusion matrix for your data
        ConfusionMatrix matrix = new ConfusionMatrix();
        // Simulate your results - 1 correct, 438 incorrect
        matrix.addPrediction("Correct", "Correct"); // 1 correct
        for (int i = 0; i < 438; i++) {
            matrix.addPrediction("Correct", "Incorrect"); // 438 wrong
        }
        
        // Format with enhanced formatter
        String formatted = KNNResultsFormatter.formatKNNResults(
            trainingInstances,
            testInstances, 
            kValue,
            accuracy,
            correctPredictions,
            totalPredictions,
            matrix,
            trainingTime,
            predictionTime,
            true // Include diagnostics
        );
        
        System.out.println(formatted);
        
        // Additional analysis
        System.out.println("\n🔍 ADDITIONAL ANALYSIS FOR YOUR DATA:\n");
        
        System.out.println("📊 WHAT YOUR NUMBERS TELL US:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.printf("• Dataset Size: %,d total instances (quite large!)\n", 
            trainingInstances + testInstances);
        System.out.printf("• Train/Test Ratio: %.1f%% / %.1f%% (good split)\n", 
            (trainingInstances * 100.0) / (trainingInstances + testInstances),
            (testInstances * 100.0) / (trainingInstances + testInstances));
        System.out.printf("• K Value: %d (reasonable for dataset size)\n", kValue);
        System.out.printf("• Success Rate: %.4f%% (only %d out of %d correct)\n", 
            accuracy, correctPredictions, totalPredictions);
        
        System.out.println("\n🚨 CRITICAL ISSUES IDENTIFIED:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("1. EXTREMELY LOW ACCURACY (0.23% is essentially random guessing)");
        System.out.println("2. With 1021 training samples, KNN should perform much better");
        System.out.println("3. K=3 is reasonable, so the issue is likely elsewhere");
        System.out.println("4. This suggests fundamental data or algorithm issues");
        
        System.out.println("\n💡 IMMEDIATE ACTION PLAN:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("STEP 1: Verify Data Quality");
        System.out.println("  • Check if features are numeric (not strings)");
        System.out.println("  • Ensure class labels match exactly between train/test");
        System.out.println("  • Look for missing or null values");
        
        System.out.println("\nSTEP 2: Debug Algorithm");
        System.out.println("  • Try K=1 to test if basic algorithm works");
        System.out.println("  • Print out first few distance calculations manually");
        System.out.println("  • Check if distance calculation returns valid numbers");
        
        System.out.println("\nSTEP 3: Data Preprocessing");
        System.out.println("  • Normalize/scale all numeric features to [0,1] range");
        System.out.println("  • Check feature value ranges (huge differences can break distance calc)");
        System.out.println("  • Remove constant/useless features");
        
        System.out.println("\nSTEP 4: Validation");
        System.out.println("  • Try predicting majority class as baseline");
        System.out.println("  • Test with a smaller, known-good dataset first");
        System.out.println("  • Manually verify a few predictions step by step");
        
        System.out.println("\n🎯 EXPECTED RESULTS:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("• After fixes, you should see accuracy > 50% at minimum");
        System.out.println("• Good KNN performance is typically 70-90%+ on clean data");
        System.out.println("• With 1000+ training samples, results should be quite good");
        
        // Show simple comparison
        System.out.println("\n📈 COMPARISON FORMAT:");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("BEFORE (your current results):");
        System.out.printf("  Accuracy: %.2f%% | Correct: %d/%d | Status: 🔴 CRITICAL\n", 
            accuracy, correctPredictions, totalPredictions);
        
        System.out.println("\nEXPECTED AFTER FIXES:");
        System.out.printf("  Accuracy: %.2f%% | Correct: %d/%d | Status: 🟢 GOOD\n", 
            75.0, (int)(totalPredictions * 0.75), totalPredictions);
    }
}