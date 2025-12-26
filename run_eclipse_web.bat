@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Eclipse Web Server
echo ===============================================
echo.

echo [1/2] Compiling Java classes...
echo.

REM Compile all Java classes
javac -cp src -d bin src\core\*.java src\YotaWebServer.java

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    echo.
    echo [2/2] Starting YOTA ML Web Server...
    echo.
    echo 🌐 The web interface will open at: http://localhost:8080
    echo 🛑 Press Ctrl+C to stop the server
    echo.
    
    REM Start the web server
    java -cp bin YotaWebServer
    
) else (
    echo ❌ Compilation failed!
    echo Please check the error messages above.
    echo.
    echo Common solutions:
    echo - Make sure all core classes are present in src/core/
    echo - Check Java syntax errors
    echo - Verify file paths and class names
    echo.
    echo If you see import errors, make sure these files exist:
    echo - src/core/Attribute.java
    echo - src/core/Instance.java  
    echo - src/core/Dataset.java
    echo - src/core/DataAnalyzer.java
    echo - src/core/KNNClassifier.java
)

echo.
echo ===============================================
echo   Server Stopped
echo ===============================================
pause