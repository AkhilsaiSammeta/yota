package com.ayota.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * KNNCLASSIFIER CLASS (Spring Boot Version)
 * 
 * Purpose: Implement K-Nearest Neighbors machine learning algorithm
 */
public class KNNClassifier {
    
    private Dataset trainingData;
    private int k;
    
    public KNNClassifier(int k) {
        this.k = k;
        this.trainingData = null;
    }
    
    public void train(Dataset dataset) {
        this.trainingData = dataset;
    }
    
    public Object predict(Instance testInstance) {
        if (trainingData == null) {
            return null;
        }
        
        // Calculate distances to all training instances
        ArrayList<DistanceNeighbor> distances = new ArrayList<DistanceNeighbor>();
        
        for (int i = 0; i < trainingData.getNumInstances(); i++) {
            Instance trainingInstance = trainingData.getInstance(i);
            double distance = euclideanDistance(testInstance, trainingInstance);
            DistanceNeighbor neighbor = new DistanceNeighbor(distance, trainingInstance);
            distances.add(neighbor);
        }
        
        // Sort distances
        sortDistances(distances);
        
        // Get votes from K nearest neighbors
        HashMap<Object, Integer> votes = new HashMap<Object, Integer>();
        int neighborsToConsider = Math.min(k, distances.size());
        
        for (int i = 0; i < neighborsToConsider; i++) {
            DistanceNeighbor neighbor = distances.get(i);
            Object neighborClass = neighbor.instance.getClassValue();
            
            if (votes.containsKey(neighborClass)) {
                votes.put(neighborClass, votes.get(neighborClass) + 1);
            } else {
                votes.put(neighborClass, 1);
            }
        }
        
        // Return majority vote
        return getMajorityVote(votes);
    }
    
    private double euclideanDistance(Instance instance1, Instance instance2) {
        int numFeatures = Math.min(instance1.getNumValues(), instance2.getNumValues());
        double sumOfSquaredDifferences = 0.0;
        
        for (int i = 0; i < numFeatures - 1; i++) {
            Object value1 = instance1.getValue(i);
            Object value2 = instance2.getValue(i);
            
            if (value1 instanceof Double && value2 instanceof Double) {
                double num1 = (Double) value1;
                double num2 = (Double) value2;
                double difference = num1 - num2;
                sumOfSquaredDifferences += difference * difference;
            }
        }
        
        return Math.sqrt(sumOfSquaredDifferences);
    }
    
    private void sortDistances(ArrayList<DistanceNeighbor> distances) {
        int n = distances.size();
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (distances.get(j).distance > distances.get(j + 1).distance) {
                    DistanceNeighbor temp = distances.get(j);
                    distances.set(j, distances.get(j + 1));
                    distances.set(j + 1, temp);
                }
            }
        }
    }
    
    private Object getMajorityVote(HashMap<Object, Integer> votes) {
        Object majorityClass = null;
        int maxVotes = 0;
        
        for (Object classValue : votes.keySet()) {
            int voteCount = votes.get(classValue);
            if (voteCount > maxVotes) {
                maxVotes = voteCount;
                majorityClass = classValue;
            }
        }
        
        return majorityClass;
    }
    
    public int getK() {
        return k;
    }
    
    public void setK(int k) {
        this.k = k;
    }
    
    private class DistanceNeighbor {
        double distance;
        Instance instance;
        
        public DistanceNeighbor(double distance, Instance instance) {
            this.distance = distance;
            this.instance = instance;
        }
    }
}