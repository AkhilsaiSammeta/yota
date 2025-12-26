# YOTA - Beginner-Friendly Java ML Engine

A simple, educational machine learning engine built from scratch in **Java 23** for **Eclipse IDE**. This project demonstrates how popular ML tools like **WEKA** work internally, combined with **Power BI-style** data analytics.

## 🎯 Project Goals

- **Learn by Building**: Understand how ML algorithms work under the hood
- **Beginner-Friendly**: Clear, commented code with real-life analogies
- **No Black Boxes**: Everything implemented from scratch using basic Java
- **Power BI + WEKA**: Combines data analytics with machine learning

## 📁 Project Structure

```
YOTA/
├── src/
│   ├── core/                 # Core data structures
│   │   ├── Attribute.java    # Column definitions
│   │   ├── Instance.java     # Single data row
│   │   ├── Dataset.java      # Complete data table
│   │   └── DataAnalyzer.java # Power BI-style statistics
│   ├── io/
│   │   └── CSVLoader.java    # Load CSV files
│   ├── ui/
│   │   └── SummaryPrinter.java # Pretty-print reports
│   ├── algorithms/
│   │   ├── core/
│   │   │   └── DistanceCalculator.java # Distance metrics
│   │   └── classifier/
│   │       └── KNNClassifier.java      # K-Nearest Neighbors
│   ├── evaluation/
│   │   ├── ConfusionMatrix.java # Performance evaluation
│   │   └── Evaluator.java       # Train-test workflows
│   └── Main.java             # Complete pipeline demo
├── sample_data.csv           # Sample dataset
└── README.md                 # This file
```

## 🚀 Features

### 📊 Power BI-Style Data Analytics
- **Dataset Summary**: Row/column counts, data types
- **Descriptive Statistics**: Min, Max, Average for numeric columns
- **Frequency Analysis**: Count occurrences of categorical values
- **Missing Value Detection**: Identify incomplete data
- **Pretty Reports**: Formatted output like business intelligence tools

### 🤖 Machine Learning (WEKA-Like)
- **K-Nearest Neighbors (KNN)**: Complete implementation from scratch
- **Distance Metrics**: Euclidean and Manhattan distance
- **Classification**: Predict categories based on similarity
- **Lazy Learning**: No complex training phase needed

### 📈 Evaluation & Testing
- **Confusion Matrix**: Visual performance assessment
- **Accuracy Metrics**: Precision, Recall, F1-Score
- **Train-Test Split**: Proper ML evaluation workflow
- **Cross-Validation**: Robust performance estimation
- **K-Value Optimization**: Find best parameters automatically

## 🛠️ Technologies Used

- **Java 23**: Latest Java features
- **Eclipse IDE**: Professional development environment
- **Pure Java**: No external libraries or frameworks
- **CSV Files**: Standard data format support

## 📋 Prerequisites

- **Java 23** installed
- **Eclipse IDE** (any recent version)
- Basic understanding of:
  - Java programming
  - Object-oriented concepts
  - CSV file format

## 🏃‍♂️ How to Run

### 1. Setup Project
```bash
# Clone or download the project
# Open Eclipse IDE
# Import project into Eclipse workspace
```

### 2. Compile and Run
```bash
# In Eclipse:
# Right-click on Main.java
# Select "Run As" → "Java Application"

# Or use command line:
    cd YOTA/
javac -d bin src/**/*.java
java -cp bin Main
```

### 3. Expected Output
```
🚀 YOTA ML Engine Started
=================================
📂 Loading dataset...
✅ Dataset loaded: Dataset{Employee Data | Attributes: 4 | Instances: 20}

📊 Analyzing data...
===== DATA SUMMARY =====
Dataset: Employee Data
Rows: 20
Columns: 4

===== COLUMN STATS =====
Age (numeric) -> Min: 21.00, Max: 45.00, Avg: 30.25
Salary (numeric) -> Min: 38000.00, Max: 95000.00, Avg: 61400.00
Experience (numeric) -> Min: 0.00, Max: 15.00, Avg: 5.40
Hired (categorical) -> Unique values: 2

🤖 Starting Machine Learning...
Testing different K values:
K=1 → Accuracy: 85.00%
K=3 → Accuracy: 90.00%
K=5 → Accuracy: 85.00%
K=7 → Accuracy: 80.00%
🏆 Best K value: 3 (Accuracy: 90.00%)

📈 Detailed Evaluation with K=3
===== CONFUSION MATRIX =====
           Hired NotHired
    Hired      9        1
 NotHired      0        5
Overall Accuracy: 93.33%

🎯 Demo Predictions:
Junior Candidate (Age: 26, Salary: $47000, Exp: 2 years) → Prediction: NotHired
Mid-level Candidate (Age: 32, Salary: $68000, Exp: 6 years) → Prediction: Hired
Senior Candidate (Age: 40, Salary: $90000, Exp: 12 years) → Prediction: Hired

🎉 YOTA ML Engine Complete!
```

## 📚 Educational Features

### Code Style Rules
- **Simplicity > Performance**: Easy to understand algorithms
- **Readability > Cleverness**: Clear variable names and comments
- **Learning > Shortcuts**: Everything implemented from scratch
- **Real-Life Analogies**: Complex concepts explained simply

### Algorithms Implemented
- **Bubble Sort**: Simple O(n²) sorting for K-nearest neighbors
- **Euclidean Distance**: Standard distance metric in ML
- **Majority Voting**: Democratic decision making for classification
- **Train-Test Split**: Proper ML evaluation methodology

### Data Structures Used
- **ArrayList**: Dynamic arrays for flexible data storage
- **HashMap**: Fast key-value lookup for frequency counting
- **Simple Arrays**: Fixed-size collections for sorting

## 🎓 Learning Path

### Phase 1: Data Handling
1. Understand `Attribute`, `Instance`, `Dataset` classes
2. Learn how CSV files are parsed and structured
3. Explore data types and storage strategies

### Phase 2: Data Analysis
1. Study `DataAnalyzer` for statistical computations
2. Practice with `SummaryPrinter` for report generation
3. Understand descriptive statistics concepts

### Phase 3: Machine Learning
1. Learn distance calculations in `DistanceCalculator`
2. Understand KNN algorithm in `KNNClassifier`
3. Practice prediction and classification concepts

### Phase 4: Evaluation
1. Study confusion matrices and accuracy metrics
2. Learn train-test split methodology
3. Understand cross-validation concepts

## 🔧 Customization

### Add Your Own Data
1. Create a CSV file with format: `feature1,feature2,...,class`
2. Place in project root directory
3. Update filename in `Main.java`

### Implement New Algorithms
1. Create new class in `algorithms/classifier/`
2. Follow the same pattern as `KNNClassifier`
3. Add evaluation in `Main.java`

### Add New Features
1. Extend `DataAnalyzer` for new statistics
2. Update `SummaryPrinter` for new reports
3. Test with your datasets

## 🤝 Contributing

This is an educational project! Feel free to:
- Add new ML algorithms (Decision Trees, Naive Bayes, etc.)
- Improve data visualization
- Add more statistical measures
- Enhance documentation with examples

## 📖 References

- **WEKA**: Waikato Environment for Knowledge Analysis
- **Power BI**: Microsoft Business Intelligence Platform
- **Java Documentation**: Oracle Java SE Documentation
- **ML Basics**: Introduction to Statistical Learning

## 📄 License

This project is for educational purposes. Feel free to use, modify, and learn from it.

## 🏆 Achievements

By completing this project, you will understand:
- ✅ How ML libraries work internally
- ✅ Data processing and analysis workflows
- ✅ Algorithm implementation from scratch
- ✅ Software design patterns in Java
- ✅ Performance evaluation methodologies

---

**Happy Learning! 🎓🚀**

*Built with ❤️ for Java beginners and ML enthusiasts*
