@echo off
cd /d "D:\PROJECTS\MARVEL\java\work_space\yota"
echo.
echo ===============================================
echo   YOTA ML Engine - Web Server
echo ===============================================
echo.

echo Starting YOTA ML Web Server...
echo 🌐 Web interface: http://localhost:8080
echo 🛑 Press Ctrl+C to stop
echo.

java -cp bin YotaWebServer

pause