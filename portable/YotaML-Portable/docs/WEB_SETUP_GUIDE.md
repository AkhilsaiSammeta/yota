# 🚀 YOTA ML Engine - Spring Boot Web Interface Setup Guide

## 📋 Prerequisites

### Required Software:
- **Java Development Kit (JDK) 21 or higher**
  - Download: https://openjdk.org/projects/jdk/21/
  - Verify: `java -version` and `javac -version`

- **Apache Maven 3.6+ or higher**
  - Download: https://maven.apache.org/download.cgi
  - Verify: `mvn --version`

- **Modern Web Browser** (Chrome, Firefox, Edge, Safari)

### Optional (for development):
- **Eclipse IDE** with Spring Tools 4 plugin
- **IntelliJ IDEA** with Spring Boot plugin
- **VS Code** with Java and Spring Boot extensions

---

## 🛠️ Installation & Setup

### Method 1: Quick Start (Recommended)

1. **Clone/Download the project**
   ```bash
   # Navigate to project directory
   cd D:\PROJECTS\MARVEL\java\work_space\yota
   ```

2. **Run the web application**
   ```bash
   # Double-click this file:
   run_web_app.bat
   
   # OR run manually:
   mvn spring-boot:run
   ```

3. **Open your browser**
   - Navigate to: http://localhost:8080
   - You should see the YOTA ML Engine interface

### Method 2: IDE Setup (For Development)

1. **Import Maven Project**
   - Eclipse: File → Import → Existing Maven Projects
   - IntelliJ: File → Open → Select pom.xml
   - VS Code: Open folder containing pom.xml

2. **Wait for dependencies**
   - Maven will automatically download required libraries
   - This may take a few minutes on first run

3. **Run the application**
   - Find `YotaMLWebApplication.java`
   - Right-click → Run As → Java Application
   - OR use IDE's Spring Boot run configuration

---

## 🌐 Web Interface Overview

The YOTA ML Engine provides a **WEKA-like** web interface with four main tabs:

### 🏠 **Home Tab**
- Welcome dashboard
- System status overview
- Quick start guide
- Feature checklist

### 📊 **Preprocess Tab**
- **File Upload**: Upload your CSV datasets
- **Sample Data**: Load built-in employee dataset
- **Data Analysis**: Automatic statistical analysis
- **Data Quality**: Missing value detection
- **Visualizations**: Column statistics and charts

### 🤖 **Classify Tab**
- **Algorithm Selection**: K-Nearest Neighbors (KNN)
- **Parameter Tuning**: K-value slider, train/test split
- **Model Training**: One-click training execution
- **Results Display**: Accuracy metrics, confusion matrix
- **Performance Analysis**: Detailed classification reports

### 📈 **Visualize Tab**
- **Chart Types**: Class distribution, histograms, scatter plots
- **Interactive Controls**: Attribute selection, chart customization
- **Export Options**: PNG/PDF chart export
- **Statistics Panel**: Real-time visualization metrics

---

## 📁 Project Structure

```
yota/
├── src/main/java/com/yota/
│   ├── YotaMLWebApplication.java    # Spring Boot main class
│   ├── controller/
│   │   └── MLController.java        # Web request handlers
│   ├── service/
│   │   └── MLService.java          # ML operations coordinator
│   ├── model/
│   │   ├── AnalysisResult.java     # Data analysis results
│   │   └── MLResult.java           # ML results wrapper
│   └── core/                       # Original ML engine classes
│       ├── Attribute.java          # Dataset column definitions
│       ├── Instance.java           # Dataset row instances  
│       ├── Dataset.java            # Complete dataset container
│       ├── DataAnalyzer.java       # Statistical analysis
│       ├── KNNClassifier.java      # K-Nearest Neighbors algorithm
│       └── ConfusionMatrix.java    # Performance evaluation
├── src/main/resources/
│   ├── application.properties      # Spring Boot configuration
│   └── templates/                  # Thymeleaf HTML templates
│       ├── index.html             # Main layout and home page
│       ├── preprocess.html        # Data loading and analysis
│       ├── classify.html          # Machine learning interface
│       └── visualize.html         # Data visualization
├── pom.xml                        # Maven dependencies
├── run_web_app.bat               # Windows startup script
└── README.md                     # This file
```

---

## 🎯 How to Use the Interface

### Step 1: Load Data
1. Go to **Preprocess** tab
2. Either:
   - Upload your CSV file (format: features + class column)
   - OR click "Load Sample Employee Data"
3. Review the automatic analysis results

### Step 2: Train Model
1. Go to **Classify** tab
2. Adjust parameters:
   - **K Value**: Number of neighbors (1-15)
   - **Train Split**: Training data percentage (50%-90%)
3. Click "Run Classification"
4. Review accuracy and confusion matrix

### Step 3: Visualize Results
1. Go to **Visualize** tab
2. Select chart type:
   - **Class Distribution**: See how classes are distributed
   - **Attribute Histogram**: Analyze individual features
3. Export charts as needed

---

## 📊 Sample Datasets

### Built-in Employee Dataset
- **Size**: 20 records
- **Features**: Age, Salary, Experience
- **Target**: Hired (Yes/No)
- **Use Case**: Employee hiring prediction

### CSV Format Requirements
```csv
Age,Salary,Experience,Hired
25,45000,2,NotHired
30,65000,5,Hired
35,80000,8,Hired
```

**Requirements:**
- First row: Column headers
- Last column: Class/target variable
- Separator: Comma (,)
- No missing values in headers

---

## ⚙️ Configuration

### Server Settings
- **Port**: 8080 (change in `application.properties`)
- **Upload Limit**: 10MB max file size
- **Session**: Stateful (data persists during session)

### Performance Tuning
- **Memory**: Increase JVM heap if handling large datasets
  ```bash
  export MAVEN_OPTS="-Xmx2g -Xms512m"
  mvn spring-boot:run
  ```
- **Timeout**: Increase for very large datasets

---

## 🔧 Troubleshooting

### Common Issues:

**1. "Maven not found"**
```bash
# Install Maven and add to PATH
# Windows: Download from maven.apache.org
# Verify: mvn --version
```

**2. "Port 8080 already in use"**
```bash
# Change port in application.properties:
server.port=8081
```

**3. "Cannot upload file"**
```bash
# Check file format (CSV only)
# Verify file size (<10MB)
# Ensure proper CSV structure
```

**4. "No data loaded"**
```bash
# Load dataset in Preprocess tab first
# Check browser console for errors
# Verify CSV format matches requirements
```

### Development Issues:

**Hot Reload not working:**
- Enable Spring DevTools in IDE
- Check `spring.devtools.restart.enabled=true`

**Template changes not reflected:**
- Set `spring.thymeleaf.cache=false`
- Clear browser cache
- Restart application

---

## 🌟 Features Comparison: Console vs Web

| Feature | Console Version | Web Version |
|---------|----------------|-------------|
| Data Loading | Manual CSV path | File upload + drag/drop |
| Analysis | Text output | Interactive charts |
| ML Training | Fixed parameters | Slider controls |
| Results | Console text | Visual confusion matrix |
| Visualization | None | Multiple chart types |
| User Experience | Technical | Beginner-friendly |
| Accessibility | Command line | Any web browser |

---

## 🚀 Advanced Usage

### Custom Datasets
1. Prepare CSV with your data
2. Ensure last column is categorical (your target)
3. Upload via Preprocess tab
4. Review automatic analysis
5. Proceed to classification

### Integration
- **REST APIs**: Available at `/api/*` endpoints
- **Embedding**: Include as Spring Boot dependency
- **Customization**: Modify templates and controllers

### Performance Optimization
- **Caching**: Results cached during session
- **Pagination**: For large datasets (>1000 rows)
- **Async Processing**: For time-intensive operations

---

## 📚 Educational Value

This web interface enhances the learning experience by:

✅ **Visual Learning**: Charts and graphs make concepts clearer  
✅ **Interactive Exploration**: Real-time parameter adjustment  
✅ **Immediate Feedback**: Instant results and validation  
✅ **Professional Interface**: Industry-standard UI/UX patterns  
✅ **Accessibility**: No technical setup required  

Perfect for:
- **Students**: Learning ML concepts visually
- **Educators**: Classroom demonstrations
- **Researchers**: Quick prototyping and analysis
- **Professionals**: Rapid model validation

---

## 🎉 Success! 

If everything works correctly, you should see:

1. **✅ Web server starts** on http://localhost:8080
2. **✅ Home page loads** with YOTA ML Engine interface
3. **✅ Sample data loads** successfully in Preprocess tab  
4. **✅ Classification runs** with accuracy results
5. **✅ Charts display** in Visualize tab

**Congratulations!** You now have a fully functional WEKA-like ML interface running locally.

---

## 📞 Support

- **Issues**: Check troubleshooting section above
- **Development**: Modify source code in `src/` directory
- **Customization**: Update templates in `src/main/resources/templates/`

Built with ❤️ for Machine Learning Education