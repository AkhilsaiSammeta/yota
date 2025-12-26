@echo off
cd "D:\PROJECTS\MARVEL\java\work_space\yota"
echo.
echo ===============================================
echo   YOTA ML Engine - Web Server
===============================================
echo.

echo [1/2] Compiling Java classes...
echo.

REM Clean and compile
del /q bin\*.class 2>nul
javac -cp src -d bin src\Main.java src\YotaWebServer.java src\EnhancedKNNDemo.java src\QuickKNNFormatter.java src\core\*.java src\algorithms\classifier\*.java src\evaluation\*.java src\io\*.java src\ui\*.java

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    echo.
    echo [2/2] Starting YOTA ML Web Server...
    echo.
    echo 🌐 The web interface will open at: http://localhost:8080
    echo 🛑 Press Ctrl+C to stop the server
    echo.
    
    java -cp bin YotaWebServer
) else (
    echo ❌ Compilation failed!
    echo Please check the error messages above.
)

echo.
echo ===============================================
echo   Server Stopped
===============================================
pause