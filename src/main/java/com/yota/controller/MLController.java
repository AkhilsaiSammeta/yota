package com.ayota.controller;

import com.ayota.service.MLService;
import com.ayota.model.AnalysisResult;
import com.ayota.model.MLResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * MAIN WEB CONTROLLER
 * 
 * Purpose: Handle web requests for WEKA-like ML interface
 * Think of it like: The traffic director for web pages
 * 
 * Real-life analogy: Like a receptionist directing visitors to the right department
 */
@Controller
public class MLController {
    
    @Autowired
    private MLService mlService;
    
    /**
     * HOME PAGE - WEKA-like Main Interface
     * Shows the main dashboard similar to WEKA Explorer
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "AYOTA ML Engine - WEKA-like Interface");
        model.addAttribute("currentTab", "home");
        return "index";
    }
    
    /**
     * PREPROCESS TAB - Data Loading and Analysis
     * Similar to WEKA's Preprocess tab
     */
    @GetMapping("/preprocess")
    public String preprocess(Model model) {
        model.addAttribute("pageTitle", "Data Preprocessing - Load & Analyze");
        model.addAttribute("currentTab", "preprocess");
        return "preprocess";
    }
    
    /**
     * CLASSIFY TAB - Machine Learning
     * Similar to WEKA's Classify tab
     */
    @GetMapping("/classify")
    public String classify(Model model) {
        // Check if we have a dataset loaded
        if (mlService.getCurrentDataset() == null) {
            model.addAttribute("error", "Please load a dataset first in the Preprocess tab");
            return "redirect:/preprocess";
        }
        
        model.addAttribute("pageTitle", "Classification - Machine Learning");
        model.addAttribute("currentTab", "classify");
        model.addAttribute("dataset", mlService.getCurrentDataset());
        return "classify";
    }
    
    /**
     * VISUALIZE TAB - Data Visualization
     * Similar to WEKA's Visualize tab
     */
    @GetMapping("/visualize")
    public String visualize(Model model) {
        if (mlService.getCurrentDataset() == null) {
            model.addAttribute("error", "Please load a dataset first in the Preprocess tab");
            return "redirect:/preprocess";
        }
        
        model.addAttribute("pageTitle", "Data Visualization");
        model.addAttribute("currentTab", "visualize");
        model.addAttribute("dataset", mlService.getCurrentDataset());
        return "visualize";
    }
    
    /**
     * UPLOAD CSV FILE
     * Handle file upload and analysis
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, 
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a file to upload");
                return "redirect:/preprocess";
            }
            
            // Process the uploaded file
            AnalysisResult result = mlService.processUploadedFile(file);
            
            // Store results in session/model
            model.addAttribute("analysisResult", result);
            redirectAttributes.addFlashAttribute("success", 
                "File uploaded successfully! Dataset: " + result.getDatasetName() + 
                " (" + result.getTotalRows() + " rows, " + result.getTotalColumns() + " columns)");
            
            return "redirect:/preprocess?analyzed=true";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Failed to upload file: " + e.getMessage());
            return "redirect:/preprocess";
        }
    }
    
    /**
     * LOAD SAMPLE DATASET
     * Load the built-in sample dataset
     */
    @PostMapping("/load-sample")
    public String loadSampleDataset(RedirectAttributes redirectAttributes) {
        try {
            AnalysisResult result = mlService.loadSampleDataset();
            
            redirectAttributes.addFlashAttribute("success", 
                "Sample dataset loaded! " + result.getDatasetName() + 
                " (" + result.getTotalRows() + " rows, " + result.getTotalColumns() + " columns)");
            
            return "redirect:/preprocess?analyzed=true";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Failed to load sample dataset: " + e.getMessage());
            return "redirect:/preprocess";
        }
    }
    
    /**
     * RUN MACHINE LEARNING
     * Execute KNN classification
     */
    @PostMapping("/run-ml")
    public String runMachineLearning(@RequestParam(defaultValue = "3") int kValue,
                                   @RequestParam(defaultValue = "0.7") double trainRatio,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            if (mlService.getCurrentDataset() == null) {
                redirectAttributes.addFlashAttribute("error", "Please load a dataset first");
                return "redirect:/preprocess";
            }
            
            // Run machine learning
            MLResult result = mlService.runKNNClassification(kValue, trainRatio);
            
            // Store results
            model.addAttribute("mlResult", result);
            redirectAttributes.addFlashAttribute("success", 
                "Machine Learning completed! Accuracy: " + 
                String.format("%.2f%%", result.getAccuracy()));
            
            return "redirect:/classify?completed=true";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Machine Learning failed: " + e.getMessage());
            return "redirect:/classify";
        }
    }
    
    /**
     * API: Get current dataset info as JSON
     */
    @GetMapping("/api/dataset")
    @ResponseBody
    public AnalysisResult getCurrentDatasetInfo() {
        return mlService.getCurrentAnalysisResult();
    }
    
    /**
     * API: Get ML results as JSON
     */
    @GetMapping("/api/ml-results")
    @ResponseBody
    public MLResult getMLResults() {
        return mlService.getCurrentMLResult();
    }
    
    /**
     * API: Get visualization data
     */
    @GetMapping("/api/visualization-data")
    @ResponseBody
    public Object getVisualizationData(@RequestParam String type) {
        return mlService.getVisualizationData(type);
    }
}