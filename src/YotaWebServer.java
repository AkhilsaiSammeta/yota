import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import core.Dataset;
import core.Instance;
import core.Attribute;
import core.DataAnalyzer;
import core.KNNClassifier;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * YOTA WEB SERVER
 * 
 * Purpose: Lightweight HTTP server for WEKA-like ML interface
 * Uses only built-in Java libraries (no Maven/external dependencies)
 * 
 * Real-life analogy: Like setting up a simple restaurant that serves
 * your ML dishes through a web browser instead of command line
 */
public class YotaWebServer {
    
    private HttpServer server;
    private Dataset currentDataset;
    private DataAnalyzer currentAnalyzer;
    private String analysisResults = "";
    private String mlResults = "";
    
    public static void main(String[] args) {
        try {
            YotaWebServer webServer = new YotaWebServer();
            webServer.startServer();
        } catch (Exception e) {
            System.err.println("Failed to start web server: " + e.getMessage());
            e.printStackTrace();
            
            // Provide helpful guidance
            System.err.println("\n🔧 TROUBLESHOOTING TIPS:");
            System.err.println("════════════════════════════════════════");
            if (e.getMessage().contains("Address already in use")) {
                System.err.println("❌ Port 8080 is already in use by another application");
                System.err.println("\n💡 Solutions:");
                System.err.println("1. Close other applications using port 8080");
                System.err.println("2. Check if YOTA ML is already running");
                System.err.println("3. Restart your computer to free up ports");
                System.err.println("4. Try running this launcher again in a few minutes");
                System.err.println("\n🔍 To find what's using port 8080:");
                System.err.println("   netstat -ano | findstr :8080");
            } else {
                System.err.println("❌ Unexpected error occurred");
                System.err.println("💡 Try restarting the application");
            }
            System.err.println("\nPress Enter to exit...");
            try { System.in.read(); } catch (Exception ignored) {}
        }
    }
    
    public void startServer() throws IOException {
        int[] portsToTry = {8080, 8081, 8082, 8083, 8084, 8085, 9090, 9091, 9092};
        IOException lastException = null;
        
        for (int port : portsToTry) {
            try {
                // Try to create HTTP server on this port
                server = HttpServer.create(new InetSocketAddress(port), 0);
                
                // Set up request handlers (like different pages in a website)
                server.createContext("/", new HomeHandler());
                server.createContext("/preprocess", new PreprocessHandler());
                server.createContext("/classify", new ClassifyHandler());
                server.createContext("/visualize", new VisualizeHandler());
                server.createContext("/upload", new UploadHandler());
                server.createContext("/load-sample", new LoadSampleHandler());
                server.createContext("/run-ml", new RunMLHandler());
                server.createContext("/api/", new ApiHandler());
                server.createContext("/static/", new StaticFileHandler());
                
                // Use thread pool for handling multiple requests
                server.setExecutor(Executors.newFixedThreadPool(10));
                
                // Start the server
                server.start();
                
                // Success! Display startup information
                System.out.println("🚀 YOTA ML Web Server Started Successfully!");
                System.out.println("=======================================");
                System.out.println("📊 WEKA-like ML Interface Available");
                System.out.println("🌐 Open browser: http://localhost:" + port);
                if (port != 8080) {
                    System.out.println("ℹ️  Note: Using port " + port + " (8080 was busy)");
                }
                System.out.println("🛑 Press Ctrl+C to stop server");
                System.out.println("=======================================");
                
                // Open browser automatically if possible
                try {
                    String os = System.getProperty("os.name").toLowerCase();
                    if (os.contains("win")) {
                        // Windows - open browser
                        new ProcessBuilder("cmd", "/c", "start", "http://localhost:" + port).start();
                        System.out.println("🌐 Browser should open automatically...");
                    }
                } catch (Exception e) {
                    System.out.println("ℹ️  Please manually open: http://localhost:" + port);
                }
                
                return; // Successfully started, exit the port-trying loop
                
            } catch (IOException e) {
                lastException = e;
                if (e.getMessage().contains("Address already in use")) {
                    System.out.println("⚠️  Port " + port + " is busy, trying next port...");
                } else {
                    System.out.println("⚠️  Failed to start on port " + port + ": " + e.getMessage());
                }
            }
        }
        
        // If we get here, all ports failed
        System.err.println("❌ Could not start server on any available port!");
        System.err.println("\n🔧 Ports tried: " + java.util.Arrays.toString(portsToTry));
        System.err.println("\n💡 Solutions:");
        System.err.println("1. Close applications that might be using these ports");
        System.err.println("2. Restart your computer to free up ports");
        System.err.println("3. Check if YOTA ML Engine is already running");
        System.err.println("4. Run 'netstat -ano | findstr :8080' to see what's using port 8080");
        
        if (lastException != null) {
            throw lastException;
        } else {
            throw new IOException("No available ports found");
        }
    }
    
    // ===== REQUEST HANDLERS =====
    
    /**
     * HOME PAGE HANDLER
     * Shows main dashboard like WEKA Explorer
     */
    class HomeHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateHomePage();
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * PREPROCESS PAGE HANDLER  
     * Data loading and analysis like WEKA Preprocess tab
     */
    class PreprocessHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = generatePreprocessPage();
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * CLASSIFY PAGE HANDLER
     * Machine learning interface like WEKA Classify tab
     */
    class ClassifyHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateClassifyPage();
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * VISUALIZE PAGE HANDLER
     * Data visualization like WEKA Visualize tab  
     */
    class VisualizeHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateVisualizePage();
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * UPLOAD HANDLER
     * Handle CSV file uploads
     */
    class UploadHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    // Parse multipart form data
                    String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
                    if (contentType != null && contentType.startsWith("multipart/form-data")) {
                        
                        // Read the request body
                        InputStream inputStream = exchange.getRequestBody();
                        String csvContent = parseMultipartFile(inputStream, contentType);
                        
                        if (csvContent != null && !csvContent.trim().isEmpty()) {
                            // Process the CSV content
                            processCSVContent(csvContent);
                            
                            // Redirect with success message
                            String response = "<script>alert('CSV file uploaded and processed successfully!'); window.location='/preprocess';</script>";
                            sendResponse(exchange, response, "text/html");
                        } else {
                            // No file content found
                            String response = "<script>alert('No valid CSV file found. Please select a CSV file.'); window.location='/preprocess';</script>";
                            sendResponse(exchange, response, "text/html");
                        }
                    } else {
                        // Not a multipart form
                        String response = "<script>alert('Invalid file upload format. Please try again.'); window.location='/preprocess';</script>";
                        sendResponse(exchange, response, "text/html");
                    }
                } catch (Exception e) {
                    // Handle any upload errors gracefully
                    System.err.println("Upload error: " + e.getMessage());
                    e.printStackTrace();
                    
                    String response = "<script>alert('File upload failed: " + e.getMessage().replace("'", "\\'") + "'); window.location='/preprocess';</script>";
                    sendResponse(exchange, response, "text/html");
                }
            } else {
                // Wrong HTTP method
                String response = "<script>alert('Invalid request method. Please use the upload form.'); window.location='/preprocess';</script>";
                sendResponse(exchange, response, "text/html");
            }
        }
    }
    
    /**
     * LOAD SAMPLE DATA HANDLER
     * Load built-in sample dataset
     */
    class LoadSampleHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            loadSampleDataset();
            
            // Redirect to preprocess page with success message
            String response = "<script>alert('Sample dataset loaded successfully!'); window.location='/preprocess';</script>";
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * RUN ML HANDLER
     * Execute machine learning training
     */
    class RunMLHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (currentDataset == null) {
                String response = "<script>alert('Please load a dataset first!'); window.location='/preprocess';</script>";
                sendResponse(exchange, response, "text/html");
                return;
            }
            
            runMachineLearning();
            
            // Redirect to classify page with results
            String response = "<script>alert('Machine Learning completed!'); window.location='/classify';</script>";
            sendResponse(exchange, response, "text/html");
        }
    }
    
    /**
     * API HANDLER
     * Handle AJAX requests for dynamic data
     */
    class ApiHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response = "{}";
            
            if (path.contains("dataset-info")) {
                response = generateDatasetInfo();
            } else if (path.contains("ml-results")) {
                response = generateMLResultsJson();
            }
            
            sendResponse(exchange, response, "application/json");
        }
    }
    
    /**
     * STATIC FILE HANDLER
     * Serve CSS, JS, images (basic implementation)
     */
    class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "/* Static files not implemented yet */";
            sendResponse(exchange, response, "text/plain");
        }
    }
    
    // ===== ML OPERATIONS =====
    
    /**
     * Load sample dataset for demonstration
     */
    private void loadSampleDataset() {
        currentDataset = new Dataset("Sample Employee Data");
        
        // Add attributes
        currentDataset.addAttribute(new Attribute("Age", "numeric"));
        currentDataset.addAttribute(new Attribute("Salary", "numeric"));
        currentDataset.addAttribute(new Attribute("Experience", "numeric"));
        currentDataset.addAttribute(new Attribute("Hired", "categorical"));
        
        // Add sample data
        double[][] sampleData = {
            {25, 45000, 2, 0}, {30, 65000, 5, 1}, {35, 80000, 8, 1}, {22, 40000, 1, 0},
            {28, 55000, 3, 1}, {45, 95000, 15, 1}, {24, 42000, 1, 0}, {33, 70000, 7, 1},
            {26, 48000, 2, 0}, {38, 85000, 10, 1}, {23, 41000, 1, 0}, {31, 62000, 5, 1},
            {27, 50000, 3, 1}, {29, 58000, 4, 1}, {36, 75000, 9, 1}, {21, 38000, 0, 0},
            {32, 67000, 6, 1}, {37, 78000, 9, 1}, {25, 46000, 2, 0}, {34, 72000, 7, 1}
        };
        
        for (double[] row : sampleData) {
            Instance instance = new Instance();
            instance.addValue(row[0]); // Age
            instance.addValue(row[1]); // Salary  
            instance.addValue(row[2]); // Experience
            
            String hiredStatus = (row[3] == 1.0) ? "Hired" : "NotHired";
            instance.addValue(hiredStatus);
            instance.setClassValue(hiredStatus);
            
            currentDataset.addInstance(instance);
        }
        
        // Analyze the dataset
        currentAnalyzer = new DataAnalyzer(currentDataset);
        analysisResults = generateAnalysisText();
    }
    
    /**
     * Run machine learning on current dataset
     */
    private void runMachineLearning() {
        if (currentDataset == null) return;
        
        try {
            // Simple train-test split (70-30)
            int totalInstances = currentDataset.getNumInstances();
            int trainSize = (int) (totalInstances * 0.7);
            
            Dataset trainSet = new Dataset("Training Set");
            Dataset testSet = new Dataset("Test Set");
            
            // Copy attributes
            for (int i = 0; i < currentDataset.getNumAttributes(); i++) {
                trainSet.addAttribute(currentDataset.getAttribute(i));
                testSet.addAttribute(currentDataset.getAttribute(i));
            }
            
            // Split instances
            for (int i = 0; i < totalInstances; i++) {
                Instance instance = currentDataset.getInstance(i);
                if (i < trainSize) {
                    trainSet.addInstance(instance);
                } else {
                    testSet.addInstance(instance);
                }
            }
            
            // Train KNN classifier
            KNNClassifier classifier = new KNNClassifier(3);
            classifier.train(trainSet);
            
            // Test classifier
            int correct = 0;
            int total = 0;
            StringBuilder results = new StringBuilder();
            
            results.append("=== K-Nearest Neighbors Results ===\\n\\n");
            results.append("Training Instances: ").append(trainSet.getNumInstances()).append("\\n");
            results.append("Test Instances: ").append(testSet.getNumInstances()).append("\\n");
            results.append("K Value: 3\\n\\n");
            
            for (int i = 0; i < testSet.getNumInstances(); i++) {
                Instance testInstance = testSet.getInstance(i);
                Object actual = testInstance.getClassValue();
                Object predicted = classifier.predict(testInstance);
                
                if (actual != null && predicted != null && actual.toString().equals(predicted.toString())) {
                    correct++;
                }
                total++;
            }
            
            double accuracy = (double) correct / total * 100.0;
            results.append("Accuracy: ").append(String.format("%.2f", accuracy)).append("%\\n");
            results.append("Correct: ").append(correct).append(" out of ").append(total).append("\\n\\n");
            
            results.append("=== Algorithm Details ===\\n");
            results.append("K-Nearest Neighbors finds the 3 most similar instances\\n");
            results.append("and uses majority voting to make predictions.\\n");
            results.append("Distance metric: Euclidean distance\\n");
            
            mlResults = results.toString();
            
        } catch (Exception e) {
            mlResults = "Error running machine learning: " + e.getMessage();
        }
    }
    
    // ===== HTML PAGE GENERATORS =====
    
    private String generateHomePage() {
        return generateBasePage("🏠 Home", 
            "<div class='container mt-4'>" +
            "<h1 class='mb-4'>🚀 YOTA ML Engine</h1>" +
            "<div class='alert alert-info'>" +
            "<h4>🎯 WEKA-like Machine Learning Interface</h4>" +
            "<p>Welcome to your beginner-friendly ML platform!</p>" +
            "</div>" +
            
            "<div class='row'>" +
            "<div class='col-md-8'>" +
            "<div class='card'>" +
            "<div class='card-header'><strong>Quick Start Guide</strong></div>" +
            "<div class='card-body'>" +
            "<ol>" +
            "<li><strong>Preprocess:</strong> Load your data or use sample dataset</li>" +
            "<li><strong>Classify:</strong> Run K-Nearest Neighbors algorithm</li>" +
            "<li><strong>Visualize:</strong> Explore results and data patterns</li>" +
            "</ol>" +
            "<a href='/load-sample' class='btn btn-primary'>📋 Load Sample Data</a> " +
            "<a href='/preprocess' class='btn btn-outline-secondary'>📊 Go to Preprocess</a>" +
            "</div></div></div>" +
            
            "<div class='col-md-4'>" +
            "<div class='card'>" +
            "<div class='card-header'><strong>System Status</strong></div>" +
            "<div class='card-body'>" +
            "<p><strong>Dataset:</strong> " + (currentDataset != null ? currentDataset.getName() : "None loaded") + "</p>" +
            "<p><strong>Instances:</strong> " + (currentDataset != null ? currentDataset.getNumInstances() : "0") + "</p>" +
            "<p><strong>ML Status:</strong> " + (mlResults.isEmpty() ? "Ready" : "Completed") + "</p>" +
            "</div></div></div>" +
            "</div></div>", 
            "home");
    }
    
    private String generatePreprocessPage() {
        String datasetInfo = "";
        if (currentDataset != null) {
            datasetInfo = 
                "<div class='alert alert-success'>" +
                "<h5>📊 Dataset Loaded: " + currentDataset.getName() + "</h5>" +
                "<p><strong>Instances:</strong> " + currentDataset.getNumInstances() + 
                " | <strong>Attributes:</strong> " + currentDataset.getNumAttributes() + "</p>" +
                "</div>" +
                
                "<div class='card'>" +
                "<div class='card-header'><strong>Statistical Analysis</strong></div>" +
                "<div class='card-body'>" +
                "<pre style='white-space: pre-wrap;'>" + analysisResults + "</pre>" +
                "</div></div>";
        } else {
            datasetInfo = 
                "<div class='alert alert-warning'>" +
                "<p>No dataset loaded. Please load sample data or upload a CSV file.</p>" +
                "</div>";
        }
        
        return generateBasePage("📊 Preprocess", 
            "<div class='container mt-4'>" +
            "<h2>📊 Data Preprocessing</h2>" +
            
            "<div class='row mt-4'>" +
            "<div class='col-md-6'>" +
            "<div class='card'>" +
            "<div class='card-header'><strong>📂 Data Loading</strong></div>" +
            "<div class='card-body'>" +
            "<form action='/upload' method='post' enctype='multipart/form-data'>" +
            "<div class='mb-3'>" +
            "<label class='form-label'>Upload CSV File:</label>" +
            "<input type='file' class='form-control' name='file' accept='.csv' required>" +
            "<div class='form-text'>Supported format: CSV files with comma-separated values</div>" +
            "</div>" +
            "<button type='submit' class='btn btn-primary'>📤 Upload & Analyze</button>" +
            "</form>" +
            
            "<div class='mt-3 alert alert-info'>" +
            "<strong>CSV Format Requirements:</strong>" +
            "<ul class='mb-0 mt-2'>" +
            "<li>First row: Column headers</li>" +
            "<li>Last column: Class/target variable</li>" +
            "<li>Comma-separated values</li>" +
            "<li>No empty rows or columns</li>" +
            "</ul>" +
            "</div>" +
            
            "<hr>" +
            
            "<div class='text-center'>" +
            "<p>Or use sample dataset:</p>" +
            "<a href='/load-sample' class='btn btn-success'>📋 Load Sample Employee Data</a>" +
            "<small class='d-block mt-2 text-muted'>20 records: Age, Salary, Experience → Hired</small>" +
            "</div>" +
            "</div></div></div>" +
            
            "<div class='col-md-6'>" +
            datasetInfo +
            "</div>" +
            "</div></div>", 
            "preprocess");
    }
    
    private String generateClassifyPage() {
        String mlContent = "";
        if (currentDataset == null) {
            mlContent = 
                "<div class='alert alert-warning'>" +
                "<p>⚠️ No dataset loaded. Please go to <a href='/preprocess'>Preprocess</a> tab first.</p>" +
                "</div>";
        } else {
            mlContent = 
                "<div class='row'>" +
                "<div class='col-md-6'>" +
                "<div class='card'>" +
                "<div class='card-header'><strong>🤖 Classifier Configuration</strong></div>" +
                "<div class='card-body'>" +
                "<p><strong>Algorithm:</strong> K-Nearest Neighbors (KNN)</p>" +
                "<p><strong>K Value:</strong> 3 neighbors</p>" +
                "<p><strong>Train/Test Split:</strong> 70% / 30%</p>" +
                "<p><strong>Distance Metric:</strong> Euclidean</p>" +
                "<a href='/run-ml' class='btn btn-success btn-lg'>🚀 Run Classification</a>" +
                "</div></div></div>" +
                
                "<div class='col-md-6'>" +
                "<div class='card'>" +
                "<div class='card-header'><strong>📊 Current Dataset</strong></div>" +
                "<div class='card-body'>" +
                "<p><strong>Name:</strong> " + currentDataset.getName() + "</p>" +
                "<p><strong>Instances:</strong> " + currentDataset.getNumInstances() + "</p>" +
                "<p><strong>Attributes:</strong> " + currentDataset.getNumAttributes() + "</p>" +
                "<p><strong>Class:</strong> " + (currentDataset.getClassAttribute() != null ? currentDataset.getClassAttribute().getName() : "Unknown") + "</p>" +
                "</div></div></div>" +
                "</div>";
            
            if (!mlResults.isEmpty()) {
                mlContent += 
                    "<div class='card mt-4'>" +
                    "<div class='card-header'><strong>🎯 Classification Results</strong></div>" +
                    "<div class='card-body'>" +
                    "<pre style='white-space: pre-wrap;'>" + mlResults + "</pre>" +
                    "</div></div>";
            }
        }
        
        return generateBasePage("🤖 Classify", 
            "<div class='container mt-4'>" +
            "<h2>🤖 Machine Learning Classification</h2>" +
            mlContent +
            "</div>", 
            "classify");
    }
    
    private String generateVisualizePage() {
        String visualizeContent = "";
        if (currentDataset == null) {
            visualizeContent = 
                "<div class='alert alert-warning'>" +
                "<p>⚠️ No dataset loaded. Please load data in the <a href='/preprocess'>Preprocess</a> tab first.</p>" +
                "</div>";
        } else {
            visualizeContent = 
                "<div class='alert alert-info'>" +
                "<p>📊 Data visualization features coming soon!</p>" +
                "<p>Current dataset: <strong>" + currentDataset.getName() + "</strong> with " + 
                currentDataset.getNumInstances() + " instances and " + currentDataset.getNumAttributes() + " attributes.</p>" +
                "</div>" +
                
                "<div class='row'>" +
                "<div class='col-md-6'>" +
                "<div class='card'>" +
                "<div class='card-header'><strong>Available Attributes</strong></div>" +
                "<div class='card-body'>" +
                generateAttributeList() +
                "</div></div></div>" +
                
                "<div class='col-md-6'>" +
                "<div class='card'>" +
                "<div class='card-header'><strong>Visualization Options</strong></div>" +
                "<div class='card-body'>" +
                "<ul>" +
                "<li>📊 Class Distribution Chart</li>" +
                "<li>📈 Attribute Histograms</li>" +
                "<li>🔍 Scatter Plots</li>" +
                "<li>📋 Correlation Matrix</li>" +
                "</ul>" +
                "<p class='text-muted'>Interactive charts will be available in future updates.</p>" +
                "</div></div></div>" +
                "</div>";
        }
        
        return generateBasePage("📈 Visualize", 
            "<div class='container mt-4'>" +
            "<h2>📈 Data Visualization</h2>" +
            visualizeContent +
            "</div>", 
            "visualize");
    }
    
    // ===== HELPER METHODS =====
    
    /**
     * Parse multipart form data to extract CSV file content
     */
    private String parseMultipartFile(InputStream inputStream, String contentType) throws IOException {
        try {
            // Extract boundary from content type
            String boundary = extractBoundary(contentType);
            if (boundary == null) {
                return null;
            }
            
            // Read the entire request body
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            
            String requestBody = buffer.toString("UTF-8");
            
            // Find the file content between boundaries
            String boundaryMarker = "--" + boundary;
            String[] parts = requestBody.split(boundaryMarker);
            
            for (String part : parts) {
                if (part.contains("filename=") && part.contains(".csv")) {
                    // This part contains the CSV file
                    int contentStart = part.indexOf("\r\n\r\n");
                    if (contentStart != -1) {
                        String fileContent = part.substring(contentStart + 4);
                        // Remove any trailing boundary markers
                        int endMarker = fileContent.indexOf("\r\n--");
                        if (endMarker != -1) {
                            fileContent = fileContent.substring(0, endMarker);
                        }
                        return fileContent.trim();
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error parsing multipart file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract boundary from Content-Type header
     */
    private String extractBoundary(String contentType) {
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("boundary=")) {
                return part.substring(9);
            }
        }
        return null;
    }
    
    /**
     * Process CSV content and create dataset
     */
    private void processCSVContent(String csvContent) throws Exception {
        String[] lines = csvContent.split("\\r?\\n");
        if (lines.length < 2) {
            throw new Exception("CSV file must have at least 2 lines (header + data)");
        }
        
        // Create new dataset
        currentDataset = new Dataset("Uploaded CSV Data");
        
        boolean firstLine = true;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            String[] values = line.split(",");
            
            if (firstLine) {
                firstLine = false;
                // Create attributes from header
                for (int i = 0; i < values.length; i++) {
                    String header = values[i].trim().replace("\"", "");
                    // Last column is categorical (class), others are numeric by default
                    String type = (i == values.length - 1) ? "categorical" : "numeric";
                    currentDataset.addAttribute(new Attribute(header, type));
                }
            } else {
                // Create instance from data row
                Instance instance = new Instance();
                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim().replace("\"", "");
                    Object objectValue = tryParseNumber(value);
                    instance.addValue(objectValue);
                }
                
                // Set class value (last column)
                if (values.length > 0) {
                    String lastValue = values[values.length - 1].trim().replace("\"", "");
                    Object classValue = tryParseNumber(lastValue);
                    instance.setClassValue(classValue);
                }
                
                currentDataset.addInstance(instance);
            }
        }
        
        // Analyze the new dataset
        if (currentDataset.getNumInstances() > 0) {
            currentAnalyzer = new DataAnalyzer(currentDataset);
            analysisResults = generateAnalysisText();
        } else {
            throw new Exception("No valid data rows found in CSV file");
        }
    }
    
    /**
     * Try to parse a string as a number, return string if not numeric
     */
    private Object tryParseNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        
        try {
            // Try integer first
            if (!value.contains(".")) {
                return Double.valueOf(Integer.parseInt(value.trim()));
            }
            // Try double
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            // Return as string if not numeric
            return value.trim();
        }
    }
    
    private String generateBasePage(String title, String content, String activeTab) {
        return "<!DOCTYPE html>" +
               "<html><head>" +
               "<title>" + title + " - YOTA ML Engine</title>" +
               "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>" +
               "<style>" +
               ".nav-link.active { background-color: #0d6efd !important; color: white !important; }" +
               ".card { margin-bottom: 1rem; }" +
               "pre { background-color: #f8f9fa; padding: 1rem; border-radius: 0.375rem; }" +
               "</style>" +
               "</head><body>" +
               
               "<nav class='navbar navbar-expand-lg navbar-dark bg-primary'>" +
               "<div class='container'>" +
               "<span class='navbar-brand'>🚀 YOTA ML Engine</span>" +
               "<span class='navbar-text'>WEKA-like Machine Learning Interface</span>" +
               "</div></nav>" +
               
               "<div class='container-fluid mt-3'>" +
               "<ul class='nav nav-tabs'>" +
               "<li class='nav-item'><a class='nav-link" + ("home".equals(activeTab) ? " active" : "") + "' href='/'>🏠 Home</a></li>" +
               "<li class='nav-item'><a class='nav-link" + ("preprocess".equals(activeTab) ? " active" : "") + "' href='/preprocess'>📊 Preprocess</a></li>" +
               "<li class='nav-item'><a class='nav-link" + ("classify".equals(activeTab) ? " active" : "") + "' href='/classify'>🤖 Classify</a></li>" +
               "<li class='nav-item'><a class='nav-link" + ("visualize".equals(activeTab) ? " active" : "") + "' href='/visualize'>📈 Visualize</a></li>" +
               "</ul>" +
               content +
               "</div>" +
               
               "<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js'></script>" +
               "</body></html>";
    }
    
    private String generateAnalysisText() {
        if (currentAnalyzer == null) return "No analysis available";
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Dataset Summary ===\n");
        sb.append("Dataset: ").append(currentDataset.getName()).append("\n");
        sb.append("Instances: ").append(currentAnalyzer.getRowCount()).append("\n");
        sb.append("Attributes: ").append(currentAnalyzer.getColumnCount()).append("\n\n");
        
        sb.append("=== Attribute Statistics ===\n");
        for (int i = 0; i < currentDataset.getNumAttributes(); i++) {
            Attribute attr = currentDataset.getAttribute(i);
            sb.append(attr.getName()).append(" (").append(attr.getType()).append(")");
            
            if ("numeric".equals(attr.getType())) {
                Double min = currentAnalyzer.getMin(i);
                Double max = currentAnalyzer.getMax(i);
                Double avg = currentAnalyzer.getAverage(i);
                
                if (min != null && max != null && avg != null) {
                    sb.append(" -> Min: ").append(String.format("%.2f", min));
                    sb.append(", Max: ").append(String.format("%.2f", max));
                    sb.append(", Avg: ").append(String.format("%.2f", avg));
                }
            } else {
                sb.append(" -> Unique values: ").append(currentAnalyzer.getUniqueValueCount(i));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private String generateAttributeList() {
        if (currentDataset == null) return "<p>No attributes available</p>";
        
        StringBuilder sb = new StringBuilder("<ul>");
        for (int i = 0; i < currentDataset.getNumAttributes(); i++) {
            Attribute attr = currentDataset.getAttribute(i);
            sb.append("<li><strong>").append(attr.getName()).append("</strong> (")
              .append(attr.getType()).append(")</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    
    private String generateDatasetInfo() {
        if (currentDataset == null) {
            return "{\"error\": \"No dataset loaded\"}";
        }
        
        return "{" +
               "\"name\": \"" + currentDataset.getName() + "\"," +
               "\"instances\": " + currentDataset.getNumInstances() + "," +
               "\"attributes\": " + currentDataset.getNumAttributes() +
               "}";
    }
    
    private String generateMLResultsJson() {
        if (mlResults.isEmpty()) {
            return "{\"error\": \"No ML results available\"}";
        }
        
        return "{" +
               "\"algorithm\": \"K-Nearest Neighbors\"," +
               "\"status\": \"completed\"," +
               "\"results\": \"" + mlResults.replace("\\n", "\\\\n").replace("\"", "\\\"") + "\"" +
               "}";
    }
    
    private void sendResponse(HttpExchange exchange, String response, String contentType) throws IOException {
        // Set response headers
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        
        // Send response
        byte[] responseBytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}