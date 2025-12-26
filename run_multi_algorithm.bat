@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Multi-Algorithm Compilation
echo ===============================================
echo.

echo [1/3] Compiling core ML algorithms...

REM Compile core classes first
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Attribute.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Instance.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Dataset.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\DataAnalyzer.java"

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error compiling basic core classes
    pause
    exit /b 1
)

REM Compile interface and base classes
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Classifier.java"

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error compiling Classifier interface
    pause
    exit /b 1
)

REM Compile ML algorithms
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\KNNClassifier.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\DecisionTreeClassifier.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\NaiveBayesClassifier.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\LogisticRegressionClassifier.java"

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error compiling ML algorithm classes
    pause
    exit /b 1
)

REM Compile support classes
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\ConfusionMatrix.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\ResultsFormatter.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\AlgorithmSelector.java"

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error compiling support classes
    pause
    exit /b 1
)

echo [2/3] Compiling remaining classes...

REM Compile IO and UI classes
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\io\CSVLoader.java"

javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\ui\SummaryPrinter.java"

REM Compile evaluation classes
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\evaluation\Evaluator.java"

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error compiling IO/UI/evaluation classes
    pause
    exit /b 1
)

echo [3/3] Compiling Main application...

REM Compile Main class
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\Main.java"

if %ERRORLEVEL% EQU 0 (
    echo ✅ All classes compiled successfully!
    echo.
    echo 🚀 Available Algorithms:
    echo   - K-Nearest Neighbors (KNN)
    echo   - Decision Tree
    echo   - Naive Bayes  
    echo   - Logistic Regression
    echo.
    echo 🎯 Running Multi-Algorithm ML Engine...
    echo.
    
    REM Run the enhanced main class
    java -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" Main
    
) else (
    echo ❌ Main class compilation failed!
    echo Please check error messages above.
)

echo.
echo ===============================================
echo   Execution Complete
echo ===============================================
pause