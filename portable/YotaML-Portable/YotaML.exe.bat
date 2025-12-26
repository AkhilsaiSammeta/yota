@echo off
title YOTA ML Engine - Machine Learning Platform
mode 90,30
color 0A

echo.
echo ╔══════════════════════════════════════════════════════════════════════════════════╗
echo ║                          🚀 YOTA ML ENGINE v1.0.0                           ║
echo ║                     Professional Machine Learning Platform                     ║
echo ╚══════════════════════════════════════════════════════════════════════════════════╝
echo.
echo 🔍 Performing system checks...
echo.

REM Check Java installation
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java Runtime Environment not found
    echo.
    echo 📋 YOTA ML Engine requires Java 8 or higher
    echo 🌐 Download from: https://www.oracle.com/java/technologies/downloads/
    echo.
    echo After installing Java, restart this application.
    pause
    exit /b 1
)
echo ✅ Java Runtime Environment: OK

REM Check application files
if not exist "lib\YotaML.jar" (
    echo ❌ Application files missing
    echo Please ensure YotaML.jar exists in the lib directory
    pause
    exit /b 1
)
echo ✅ Application Files: OK

echo 🚀 Starting YOTA ML Engine Web Server...
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.
echo 📊 Loading Machine Learning Algorithms:
echo    ✓ K-Nearest Neighbors
echo    ✓ Decision Tree
echo    ✓ Naive Bayes  
echo    ✓ Logistic Regression
echo.
echo 🌐 Starting Web Interface...
echo 🔍 Checking available ports (8080, 8081, 8082...)
echo 📱 Browser will open automatically
echo.
echo 💡 TIP: Bookmark http://localhost:8080 for easy access
echo ⏹️  To stop: Close this window or press Ctrl+C
echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.

REM Open browser after delay  
start /min cmd /c "timeout /t 4 ^>nul ^&^& start http://localhost:8080 ^&^& start http://localhost:8081"

REM Start the ML engine
java -Xms256m -Xmx1g -cp "lib\YotaML.jar" YotaWebServer

echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 🛑 YOTA ML Engine Web Server Stopped
echo.
echo Thank you for using YOTA ML Engine!
echo For support, check the docs folder or README.txt
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
pause
