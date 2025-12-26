@echo off
title YOTA ML Engine - Quick Launcher
cd /d "%~dp0"

REM Quick Java check
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java not found! Please install Java 8+ first.
    echo 🌐 Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Quick JAR check
if not exist "lib\YotaML.jar" (
    echo ❌ Application files missing!
    pause
    exit /b 1
)

echo 🚀 Starting YOTA ML Engine...
echo 🌐 Web browser will open automatically...
echo.

REM Open browser after delay
start /min cmd /c "timeout /t 4 >nul && start http://localhost:8080"

REM Start the application
java -Xms256m -Xmx1g -cp "lib\YotaML.jar" YotaWebServer

pause
