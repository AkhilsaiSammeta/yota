@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Spring Boot Web Runner
echo ===============================================
echo.

REM Set Maven home if needed
REM set MAVEN_HOME=C:\Program Files\Apache\maven
REM set PATH=%PATH%;%MAVEN_HOME%\bin

echo [1/3] Checking Maven installation...
mvn --version
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven not found! Please install Maven first.
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo.
echo [2/3] Building Spring Boot application...
echo.
mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo ✅ Build successful!
    echo.
    echo [3/3] Starting YOTA ML Web Server...
    echo.
    echo 🌐 The web interface will open at: http://localhost:8080
    echo 🛑 Press Ctrl+C to stop the server
    echo.
    
    REM Start Spring Boot application
    mvn spring-boot:run
    
) else (
    echo ❌ Build failed!
    echo Please check the error messages above.
    echo.
    echo Common solutions:
    echo - Make sure Java 21+ is installed
    echo - Check internet connection for dependency download
    echo - Verify pom.xml configuration
)

echo.
echo ===============================================
echo   Server Stopped
echo ===============================================
pause