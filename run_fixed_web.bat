@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Web Server (Fixed Upload)
echo ===============================================
echo.

echo [1/3] Stopping any existing Java processes...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 >nul

echo [2/3] Compiling updated classes...
javac -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin;D:\PROJECTS\MARVEL\java\work_space\yota\src" -d "D:\PROJECTS\MARVEL\java\work_space\yota\bin" "D:\PROJECTS\MARVEL\java\work_space\yota\src\YotaWebServer.java"

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    echo.
    echo [3/3] Starting YOTA ML Web Server...
    echo.
    echo 🔧 Fixed Issues:
    echo   ✅ Proper CSV file upload handling
    echo   ✅ Multipart form parsing  
    echo   ✅ Error handling and validation
    echo   ✅ Connection reset protection
    echo.
    echo 🌐 Open browser: http://localhost:8080
    echo 📂 Test file available: test_upload.csv
    echo 🛑 Press Ctrl+C to stop server
    echo.
    
    REM Start the web server
    java -cp "D:\PROJECTS\MARVEL\java\work_space\yota\bin" YotaWebServer
    
) else (
    echo ❌ Compilation failed!
    echo Please check the error messages above.
)

echo.
echo ===============================================
echo   Server Stopped
echo ===============================================
pause