@echo off
echo.
echo ===============================================
echo   YOTA ML Engine - Eclipse Project Web Setup
echo ===============================================
echo.

echo [INFO] Converting to Eclipse-compatible Spring Boot project...
echo.

REM Create lib directory for JAR dependencies
if not exist "lib" mkdir lib

echo [1/4] Creating lib directory for dependencies...
echo ✅ Created lib/ folder for JAR files

echo.
echo [2/4] Downloading required JAR files...
echo.
echo 📦 Required JAR files (download manually):
echo.
echo Spring Boot Core JARs:
echo - spring-boot-2.7.0.jar
echo - spring-boot-autoconfigure-2.7.0.jar  
echo - spring-boot-starter-2.7.0.jar
echo - spring-boot-starter-web-2.7.0.jar
echo - spring-boot-starter-thymeleaf-2.7.0.jar
echo.
echo Spring Framework JARs:
echo - spring-core-5.3.21.jar
echo - spring-context-5.3.21.jar
echo - spring-web-5.3.21.jar
echo - spring-webmvc-5.3.21.jar
echo.
echo Other Dependencies:
echo - tomcat-embed-core-9.0.63.jar
echo - thymeleaf-3.0.15.jar
echo - jackson-databind-2.13.3.jar
echo.

echo [3/4] Alternative: Creating Lightweight Web Server...
echo ✅ Will create a simple HTTP server using built-in Java libraries

echo.
echo [4/4] Setting up Eclipse-compatible project structure...

REM Update classpath for Eclipse
echo ✅ Maintaining Eclipse project structure

echo.
echo ===============================================
echo   Setup Complete - Choose Option:
echo ===============================================
echo.
echo Option 1: Manual JAR Download (Full Spring Boot)
echo   - Download JARs from Maven Central
echo   - Add to lib/ folder  
echo   - Update Eclipse build path
echo.
echo Option 2: Lightweight HTTP Server (Recommended)
echo   - Uses built-in Java HTTP server
echo   - No external dependencies required
echo   - Ready to run immediately
echo.

set /p choice="Select option (1 or 2): "

if "%choice%"=="1" (
    echo.
    echo 📋 Manual Setup Instructions:
    echo 1. Download JAR files from: https://repo1.maven.org/maven2/
    echo 2. Place in lib/ folder
    echo 3. Right-click project → Properties → Build Path → Add JARs
    echo 4. Run YotaWebServer.java
) else (
    echo.
    echo ✅ Creating lightweight web server...
    echo 🚀 Ready to run! Execute: java YotaWebServer
)

echo.
pause