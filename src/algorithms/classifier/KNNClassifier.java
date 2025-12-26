package algorithms.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import core.Dataset;
import core.Instance;
import algorithms.core.DistanceCalculator;

/**
 * KNNCLASSIFIER CLASS
 * 
 * Purpose: Implement K-Nearest Neighbors machine learning algorithm
 * Think of it like: Asking your K closest neighbors what they think
 * 
 * Real-life analogy: If you want to know if a house is expensive,
 * look at the K most similar houses and see what most of them cost
 * 
 * How KNN Works:
 * 1. TRAINING: Just remember all the training data (lazy learning)
 * 2. PREDICTION: For new data point:
 *    a. Calculate distance to all training points
 *    b. Find the K closest neighbors
 *    c. Let them "vote" - whatever class most neighbors have wins
 * 
 * Example with K=3:
 * New person: age=28, salary=48000, class=?
 * Find 3 most similar people in training data:
 * Neighbor 1: age=25, salary=45000, class=Hired
 * Neighbor 2: age=30, salary=50000, class=Hired  
 * Neighbor 3: age=27, salary=47000, class=NotHired
 * Vote: 2 Hired, 1 NotHired → Prediction = Hired
 */

public class KNNClassifier {
    
    // Store the training dataset
    private Dataset trainingData;
    
    // Store the value of K (number of neighbors to consider)
    private int k;
    
    // Constructor: Create a KNN classifier with specified K value
    // Parameters:
    //   - k: Number of neighbors to consider (usually odd number like 3, 5, 7)
    public KNNClassifier(int k) {
        this.k = k;
        this.trainingData = null;
    }
    
    // Method: Train the classifier (just store the training data)
    // KNN is "lazy learning" - no actual training computation needed
    // Parameters:
    //   - dataset: Training data to remember
    public void train(Dataset dataset) {
        this.trainingData = dataset;
        System.out.println("KNN trained with " + dataset.getNumInstances() + " instances");
    }
    
    // Method: Predict the class of a new instance
    // Parameters:
    //   - testInstance: New data point to classify
    // Returns: Predicted class as Object
    public Object predict(Instance testInstance) {
        if (trainingData == null) {
            System.out.println("ERROR: Classifier not trained yet!");
            return null;
        }
        
        // Step 1: Calculate distances to all training instances
        ArrayList<DistanceNeighbor> distances = new ArrayList<DistanceNeighbor>();
        
        for (int i = 0; i < trainingData.getNumInstances(); i++) {
            Instance trainingInstance = trainingData.getInstance(i);
            
            // Calculate distance using Euclidean distance
            double distance = DistanceCalculator.euclideanDistance(testInstance, trainingInstance);
            
            // Store distance along with the training instance
            DistanceNeighbor neighbor = new DistanceNeighbor(distance, trainingInstance);
            distances.add(neighbor);
        }
        
        // Step 2: Sort distances (find K nearest neighbors)
        // Using simple bubble sort - easy to understand for beginners
        sortDistances(distances);
        
        // Step 3: Get votes from K nearest neighbors
        HashMap<Object, Integer> votes = new HashMap<Object, Integer>();
        
        // Take only the first K neighbors (closest ones)
        int neighborsToConsider = Math.min(k, distances.size());
        
        for (int i = 0; i < neighborsToConsider; i++) {
            DistanceNeighbor neighbor = distances.get(i);
            Object neighborClass = neighbor.instance.getClassValue();
            
            // Count the vote
            if (votes.containsKey(neighborClass)) {
                votes.put(neighborClass, votes.get(neighborClass) + 1);
            } else {
                votes.put(neighborClass, 1);
            }
        }
        
        // Step 4: Return the class with most votes (majority vote)
        return getMajorityVote(votes);
    }
    
    // Helper Method: Sort distances using simple bubble sort
    // Bubble sort is O(n²) but easy to understand for beginners
    // Parameters:
    //   - distances: ArrayList of DistanceNeighbor objects to sort
    private void sortDistances(ArrayList<DistanceNeighbor> distances) {
        int n = distances.size();
        
        // Bubble sort: compare adjacent elements and swap if needed
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                
                // If current distance is greater than next distance, swap them
                if (distances.get(j).distance > distances.get(j + 1).distance) {
                    DistanceNeighbor temp = distances.get(j);
                    distances.set(j, distances.get(j + 1));
                    distances.set(j + 1, temp);
                }
            }
        }
    }
    
    // Helper Method: Find the class with most votes
    // Parameters:
    //   - votes: HashMap with class -> vote count
    // Returns: The class with highest vote count
    private Object getMajorityVote(HashMap<Object, Integer> votes) {
        Object majorityClass = null;
        int maxVotes = 0;
        
        // Loop through all classes and find the one with most votes
        for (Object classValue : votes.keySet()) {
            int voteCount = votes.get(classValue);
            
            if (voteCount > maxVotes) {
                maxVotes = voteCount;
                majorityClass = classValue;
            }
        }
        
        return majorityClass;
    }
    
    // Getter: Get the K value
    public int getK() {
        return k;
    }
    
    // Setter: Change the K value
    public void setK(int k) {
        this.k = k;
    }
    
    // Helper Class: Store distance and corresponding instance together
    // This makes it easier to sort neighbors by distance
    private class DistanceNeighbor {
        double distance;
        Instance instance;
        
        public DistanceNeighbor(double distance, Instance instance) {
            this.distance = distance;
            this.instance = instance;
        }
    }
}