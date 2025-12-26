@echo off
title YOTA ML Engine - Enhanced Web Interface
color 0A

REM Check Java
echo Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ ERROR: Java is not installed or not found in PATH
    echo.
    echo YOTA ML Engine requires Java 8 or higher.
    echo Please download and install Java from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    echo After installing Java, restart this program.
    pause
    exit /b 1
)

echo ╔════════════════════════════════════════════════════════════════╗
echo ║               YOTA ML Engine - Web Interface                  ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo 🌐 Starting YOTA ML Engine Web Server...
echo 📱 Checking for available ports...
echo 🔗 Will automatically open your browser
echo.
echo ⏹️  Press Ctrl+C to stop the server
echo.

REM Check if port 8080 is already in use
netstat -ano | findstr :8080 >nul
if %ERRORLEVEL% EQU 0 (
    echo ⚠️  Port 8080 appears to be in use
    echo 🔄 YOTA ML will automatically find an available port
    echo.
)

cd /d "%~dp0"
java -Xms256m -Xmx1g -cp "YotaML.jar" YotaWebServer

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Web server failed to start
    echo.
    echo 🔧 TROUBLESHOOTING:
    echo ════════════════════════════════════════════════════════════════
    echo 1. Check if YOTA ML Engine is already running
    echo 2. Try closing other web applications
    echo 3. Restart your computer to free up network ports
    echo 4. Run as Administrator if you continue having issues
    echo.
    echo 🔍 To see what's using network ports:
    echo    netstat -ano | findstr :8080
    echo.
)

echo.
echo 🛑 Web server stopped
echo Thank you for using YOTA ML Engine!
pause