@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Batch Runner
echo ===============================================
echo.

REM Compile all Java files
echo [1/2] Compiling Java files...
echo.

REM Compile core package
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Attribute.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Instance.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\Dataset.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\core\DataAnalyzer.java"

REM Compile io package
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\io\CSVLoader.java"

REM Compile ui package
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\ui\SummaryPrinter.java"

REM Compile algorithms package
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\algorithms\core\DistanceCalculator.java"
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\algorithms\classifier\KNNClassifier.java"

REM Compile evaluation package
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\evaluation\ConfusionMatrix.java" "D:\PROJECTS\MARVEL\java\work_space\yota\src\evaluation\Evaluator.java"

REM Compile Main class
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\Main.java"

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    echo.
    echo [2/2] Running YOTA ML Engine...
    echo.
    java -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" Main
) else (
    echo ❌ Compilation failed!
    echo Please check the error messages above.
)

echo.
echo ===============================================
echo   Execution Complete
echo ===============================================
pause