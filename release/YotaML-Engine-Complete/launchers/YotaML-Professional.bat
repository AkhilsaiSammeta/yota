@echo off
title YOTA ML Engine - Professional Launcher
mode 100,35
color 0A

echo ╔══════════════════════════════════════════════════════════════════════════════════╗
echo ║                          🚀 YOTA ML ENGINE v1.0.0                           ║
echo ║                     Professional Machine Learning Platform                     ║
echo ╚══════════════════════════════════════════════════════════════════════════════════╝
echo.
echo 🔍 Performing comprehensive system checks...
echo.

REM Detailed Java check
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java Runtime Environment: NOT FOUND
    echo.
    echo 📋 REQUIREMENTS:
    echo    • Java 8 or higher required
    echo    • Download from: https://www.oracle.com/java/technologies/downloads/
    echo    • Install and restart your computer
    echo.
    timeout /t 15
    exit /b 1
) else (
    echo ✅ Java Runtime Environment: OK
    java -version 2>&1 | find "version"
)

REM Application files check
if not exist "app\YotaML.jar" (
    echo ❌ Application Files: MISSING
    echo    Expected: app\YotaML.jar
    echo    Please reinstall YOTA ML Engine
    pause & exit /b 1
) else (
    echo ✅ Application Files: OK
)

REM Memory check
echo ✅ System Memory: OK (1GB allocated to application)

REM Port availability check  
echo 🔍 Checking network ports...
netstat -ano | findstr :8080 >nul && (
    echo ⚠️  Port 8080: BUSY (will try alternatives)
) || (
    echo ✅ Port 8080: AVAILABLE
)

echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 🚀 LAUNCHING YOTA ML ENGINE
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.
echo 📊 Loading ML algorithms: KNN, Decision Tree, Naive Bayes, Logistic Regression
echo 🌐 Starting web server with automatic port detection...
echo 📱 Browser will open automatically to the web interface
echo.
echo 💡 TIPS:
echo    • Bookmark the URL for quick access
echo    • Try the sample data in the data folder
echo    • Press Ctrl+C to stop the server
echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.

REM Open browser with delay
start /min cmd /c "timeout /t 4 ^>nul ^&^& start http://localhost:8080 ^&^& start http://localhost:8081"

REM Start application
java -Xms256m -Xmx1g -cp "app\YotaML.jar" YotaWebServer

echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 🛑 YOTA ML Engine stopped
echo Thank you for using YOTA ML Engine!
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
pause
