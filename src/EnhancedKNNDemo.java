import core.Dataset;
import core.Instance;
import core.Attribute;
import core.KNNClassifier;
import core.KNNResultsFormatter;
import core.KNNDiagnostics;
import evaluation.ConfusionMatrix;
import evaluation.Evaluator;
import io.CSVLoader;

/**
 * ENHANCED KNN DEMO
 * 
 * Purpose: Demonstrate enhanced KNN results formatting with diagnostics
 * Shows how to format your KNN results for better understanding
 */
public class EnhancedKNNDemo {
    
    public static void main(String[] args) {
        try {
            // Demo with your reported results
            demonstrateEnhancedFormatting();
            
            // Run live demo with sample data
            runLiveDemoWithDiagnostics();
            
        } catch (Exception e) {
            System.err.println("Error in demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrate enhanced formatting with your reported numbers
     */
    public static void demonstrateEnhancedFormatting() {
        System.out.println("═".repeat(80));
        System.out.println("ENHANCED KNN RESULTS FORMATTING DEMO");
        System.out.println("═".repeat(80));
        
        // Your reported results
        int trainingInstances = 1021;
        int testInstances = 439;
        int kValue = 3;
        double accuracy = 0.23;
        int correctPredictions = 1;
        int totalPredictions = 439;
        long trainingTime = 5;
        long predictionTime = 150;
        
        // Create a mock confusion matrix for demonstration
        ConfusionMatrix mockMatrix = new ConfusionMatrix();
        // Add some mock predictions (this is just for formatting demo)
        for (int i = 0; i < correctPredictions; i++) {
            mockMatrix.addPrediction("ClassA", "ClassA"); // Correct prediction
        }
        for (int i = 0; i < totalPredictions - correctPredictions; i++) {
            mockMatrix.addPrediction("ClassA", "ClassB"); // Incorrect predictions
        }
        
        // Format results using enhanced formatter
        String enhancedResults = KNNResultsFormatter.formatKNNResults(
            trainingInstances,
            testInstances,
            kValue,
            accuracy,
            correctPredictions,
            totalPredictions,
            mockMatrix,
            trainingTime,
            predictionTime,
            true // Include debug info
        );
        
        System.out.println(enhancedResults);
        
        // Also show simple format
        System.out.println("\n" + "═".repeat(80));
        System.out.println("SIMPLE FORMAT (for quick checks):");
        System.out.println("═".repeat(80));
        String simpleResults = KNNResultsFormatter.formatSimpleResults(
            correctPredictions, totalPredictions, accuracy);
        System.out.println(simpleResults);
    }
    
    /**
     * Run live demo with actual data and diagnostics
     */
    public static void runLiveDemoWithDiagnostics() {
        System.out.println("\n" + "═".repeat(80));
        System.out.println("LIVE DEMO WITH DIAGNOSTICS");
        System.out.println("═".repeat(80));
        
        try {
            // Load sample data
            Dataset dataset = CSVLoader.loadCSV("sample_data.csv", "Employee Data");
            
            if (dataset == null) {
                System.out.println("⚠️  Could not load sample_data.csv - creating synthetic low-accuracy scenario");
                dataset = createSyntheticProblemDataset();
            }
            
            // Split data
            Dataset[] split = Evaluator.splitDataset(dataset, 0.7);
            Dataset trainSet = split[0];
            Dataset testSet = split[1];
            
            // Create and train KNN
            KNNClassifier knn = new KNNClassifier(3);
            long startTime = System.currentTimeMillis();
            knn.train(trainSet);
            long trainingTime = System.currentTimeMillis() - startTime;
            
            // Test and measure accuracy
            startTime = System.currentTimeMillis();
            ConfusionMatrix confusionMatrix = new ConfusionMatrix();
            int correct = 0;
            
            for (int i = 0; i < testSet.getNumInstances(); i++) {
                Instance testInstance = testSet.getInstance(i);
                Object actualClass = testInstance.getClassValue();
                Object predictedClass = knn.predict(testInstance);
                
                confusionMatrix.addPrediction(actualClass, predictedClass);
                if (actualClass.equals(predictedClass)) {
                    correct++;
                }
            }
            
            long predictionTime = System.currentTimeMillis() - startTime;
            double accuracy = (correct * 100.0) / testSet.getNumInstances();
            
            // Show enhanced results
            String results = KNNResultsFormatter.formatKNNResults(
                trainSet.getNumInstances(),
                testSet.getNumInstances(),
                3,
                accuracy,
                correct,
                testSet.getNumInstances(),
                confusionMatrix,
                trainingTime,
                predictionTime,
                true
            );
            
            System.out.println(results);
            
            // If accuracy is low, run diagnostics
            if (accuracy < 50.0) {
                System.out.println("\n" + "═".repeat(80));
                System.out.println("RUNNING DIAGNOSTICS (Low accuracy detected)");
                System.out.println("═".repeat(80));
                
                String diagnostics = KNNDiagnostics.analyzeLowAccuracy(
                    dataset, knn, trainSet, testSet, 3);
                System.out.println(diagnostics);
            }
            
        } catch (Exception e) {
            System.err.println("Error in live demo: " + e.getMessage());
        }
    }
    
    /**
     * Create a synthetic dataset that will produce low accuracy for demonstration
     */
    private static Dataset createSyntheticProblemDataset() {
        Dataset dataset = new Dataset("Synthetic Problem Data");
        
        // Add attributes
        dataset.addAttribute(new Attribute("feature1", "numeric"));
        dataset.addAttribute(new Attribute("feature2", "numeric"));
        dataset.addAttribute(new Attribute("class", "categorical"));
        
        // Add some problematic instances (random data that doesn't correlate with class)
        java.util.Random random = new java.util.Random(42);
        String[] classes = {"ClassA", "ClassB", "ClassC"};
        
        for (int i = 0; i < 100; i++) {
            Instance instance = new Instance();
            instance.addValue(random.nextDouble() * 1000); // Large range, unscaled
            instance.addValue(random.nextDouble() * 0.001); // Very small range
            instance.setClassValue(classes[random.nextInt(classes.length)]); // Random class
            dataset.addInstance(instance);
        }
        
        return dataset;
    }
}