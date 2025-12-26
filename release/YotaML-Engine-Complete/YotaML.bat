@echo off
title YOTA ML Engine

REM Quick system check
java -version >nul 2>&1 || (
    echo ❌ Java not found! Install Java 8+ from: https://www.oracle.com/java/
    timeout /t 10
    exit /b 1
)

if not exist "app\YotaML.jar" (
    echo ❌ Application missing! Please reinstall YOTA ML Engine.
    pause & exit /b 1
)

echo 🚀 Starting YOTA ML Engine...
echo 🌐 Opening web browser automatically...

start /min cmd /c "timeout /t 3 ^>nul ^&^& start http://localhost:8080"
java -Xms256m -Xmx1g -cp "app\YotaML.jar" YotaWebServer
pause
