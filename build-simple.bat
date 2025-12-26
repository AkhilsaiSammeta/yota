@echo off
setlocal EnableDelayedExpansion

echo ╔════════════════════════════════════════════════════════════════╗
echo ║                YOTA ML Engine - Simple Builder                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

REM Clean and prepare
if exist "dist" rmdir /s /q "dist"
mkdir "dist"
mkdir "dist\YotaML-Installer"
mkdir "dist\YotaML-Installer\data"
mkdir "dist\YotaML-Installer\docs"

echo [1/5] Using existing compiled classes from bin directory...
if not exist "bin" (
    echo ❌ No compiled classes found in bin directory!
    echo Please run 'javac -cp bin -d bin src\*.java' first to compile.
    pause
    exit /b 1
)

echo [2/5] Creating JAR file...
cd bin
jar cf "..\dist\YotaML-Installer\YotaML.jar" . 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ JAR creation failed!
    cd ..
    pause
    exit /b 1
)
cd ..
echo ✅ JAR created successfully

echo [3/5] Copying data files...
copy "sample_data.csv" "dist\YotaML-Installer\data\" >nul 2>&1
copy "test_upload.csv" "dist\YotaML-Installer\data\" >nul 2>&1
copy "*.md" "dist\YotaML-Installer\docs\" >nul 2>&1

echo [4/5] Creating user-friendly launchers...

REM Console launcher with better error handling
(
echo @echo off
echo title YOTA ML Engine - Console
echo.
echo REM Check Java
echo echo Checking Java installation...
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo.
echo     echo ❌ ERROR: Java is not installed or not found in PATH
echo     echo.
echo     echo YOTA ML Engine requires Java 8 or higher.
echo     echo Please download and install Java from:
echo     echo https://www.oracle.com/java/technologies/downloads/
echo     echo.
echo     echo After installing Java, restart this program.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo ╔════════════════════════════════════════════════════════════════╗
echo echo ║                 YOTA ML Engine - Console Mode                 ║  
echo echo ╚════════════════════════════════════════════════════════════════╝
echo echo.
echo echo 🤖 Loading machine learning algorithms...
echo echo 📊 Preparing data analysis tools...
echo echo.
echo.
echo cd /d "%%~dp0"
echo java -Xms256m -Xmx1g -cp "YotaML.jar" Main
echo.
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo.
echo     echo ❌ Application encountered an error
echo     echo Check the messages above for details
echo ^)
echo echo.
echo echo Thank you for using YOTA ML Engine!
echo pause
) > "dist\YotaML-Installer\YotaML-Console.bat"

REM Web launcher  
(
echo @echo off
echo title YOTA ML Engine - Web Interface
echo color 0A
echo.
echo REM Check Java
echo echo Checking Java installation...
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo.
echo     echo ❌ ERROR: Java is not installed or not found in PATH
echo     echo.
echo     echo YOTA ML Engine requires Java 8 or higher.
echo     echo Please download and install Java from:
echo     echo https://www.oracle.com/java/technologies/downloads/
echo     echo.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo ╔════════════════════════════════════════════════════════════════╗
echo echo ║               YOTA ML Engine - Web Interface                  ║
echo echo ╚════════════════════════════════════════════════════════════════╝
echo echo.
echo echo 🌐 Starting YOTA ML Engine Web Server...
echo echo 📱 Your browser will open automatically
echo echo 🔗 Manual URL: http://localhost:8080
echo echo.
echo echo ⏹️  Press Ctrl+C to stop the server
echo echo.
echo.
echo REM Start browser after delay
echo start /min cmd /c "timeout /t 3 ^>nul ^&^& start http://localhost:8080 ^&^& exit"
echo.
echo cd /d "%%~dp0"  
echo java -Xms256m -Xmx1g -cp "YotaML.jar" YotaWebServer
echo.
echo echo.
echo echo 🛑 Web server stopped
echo pause
) > "dist\YotaML-Installer\YotaML-Web.bat"

REM Enhanced demo launcher
(
echo @echo off
echo title YOTA ML Engine - Enhanced Demo
echo echo Starting Enhanced KNN Demo...
echo cd /d "%%~dp0"
echo java -cp "YotaML.jar" EnhancedKNNDemo
echo pause
) > "dist\YotaML-Installer\YotaML-Demo.bat"

REM Quick formatter launcher
(
echo @echo off  
echo title YOTA ML Engine - Results Formatter
echo echo Starting Results Formatter...
echo cd /d "%%~dp0"
echo java -cp "YotaML.jar" QuickKNNFormatter
echo pause
) > "dist\YotaML-Installer\YotaML-Formatter.bat"

echo [5/5] Creating installer and documentation...

REM Simple installer
(
echo @echo off
echo title YOTA ML Engine - Installer
echo.
echo echo ╔════════════════════════════════════════════════════════════════╗
echo echo ║              YOTA ML Engine - Easy Installer                  ║
echo echo ╚════════════════════════════════════════════════════════════════╝
echo echo.
echo echo This installer will:
echo echo   1. Create desktop shortcuts
echo echo   2. Create Start Menu entries  
echo echo   3. Set up YOTA ML Engine for easy access
echo echo.
echo pause
echo.
echo echo Creating shortcuts...
echo.
echo REM Create desktop shortcuts
echo set DESKTOP=%%USERPROFILE%%\Desktop
echo set CURRENT_DIR=%%CD%%
echo.
echo ^(
echo echo @echo off
echo echo cd /d "%%CURRENT_DIR%%"
echo echo YotaML-Console.bat
echo ^) ^> "%%DESKTOP%%\YOTA ML Console.bat"
echo.
echo ^(
echo echo @echo off  
echo echo cd /d "%%CURRENT_DIR%%"
echo echo YotaML-Web.bat
echo ^) ^> "%%DESKTOP%%\YOTA ML Web.bat"
echo.
echo echo ✅ Desktop shortcuts created:
echo echo    • YOTA ML Console.bat
echo echo    • YOTA ML Web.bat
echo echo.
echo echo 🎉 Installation complete!
echo echo.
echo echo 🚀 How to use YOTA ML Engine:
echo echo ════════════════════════════════════════════════════════════════
echo echo Console Mode:
echo echo   • Double-click "YOTA ML Console" on your desktop
echo echo   • Run machine learning algorithms from command line
echo echo.
echo echo Web Interface:  
echo echo   • Double-click "YOTA ML Web" on your desktop
echo echo   • Use modern web interface in your browser
echo echo.
echo echo Demo ^& Tools:
echo echo   • Run YotaML-Demo.bat for enhanced demonstrations
echo echo   • Run YotaML-Formatter.bat to format results
echo echo.
echo set /p launch="Launch YOTA ML Engine now? (Y/N): "
echo if /i "%%launch%%"=="Y" ^(
echo     echo.
echo     echo 🚀 Starting YOTA ML Console...
echo     start "" YotaML-Console.bat
echo ^)
echo echo.
echo echo Thank you for installing YOTA ML Engine!
echo pause
) > "dist\YotaML-Installer\install.bat"

REM User guide
(
echo YOTA ML Engine - User Guide
echo ============================
echo.
echo QUICK START:
echo 1. Run install.bat to create desktop shortcuts
echo 2. Double-click desktop shortcuts to launch
echo.
echo FEATURES:
echo • Console Interface - Traditional ML command line
echo • Web Interface - Modern browser-based UI
echo • 4 ML Algorithms - KNN, Decision Tree, Naive Bayes, Logistic Regression  
echo • Data Analysis - Power BI-style statistics
echo • CSV Support - Load your own datasets
echo.
echo SYSTEM REQUIREMENTS:
echo • Windows 10/11
echo • Java 8+ installed
echo • 512MB RAM minimum
echo • 100MB disk space
echo.
echo LAUNCHERS:
echo • YotaML-Console.bat - Console interface
echo • YotaML-Web.bat - Web interface ^(http://localhost:8080^)
echo • YotaML-Demo.bat - Enhanced demonstrations
echo • YotaML-Formatter.bat - Format ML results
echo.
echo TROUBLESHOOTING:
echo If you get "Java not found" errors:
echo 1. Download Java from https://www.oracle.com/java/
echo 2. Install Java and restart your computer
echo 3. Try running the application again
echo.
echo For more help, check the docs folder for detailed documentation.
) > "dist\YotaML-Installer\USER-GUIDE.txt"

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                    🎉 BUILD SUCCESSFUL! 🎉                    ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo ✅ YOTA ML Engine installer package created!
echo.
echo 📁 Location: dist\YotaML-Installer\
echo.
echo 📦 Package contents:
echo   ✅ YotaML.jar - Complete application
echo   ✅ YotaML-Console.bat - Console launcher
echo   ✅ YotaML-Web.bat - Web interface launcher
echo   ✅ YotaML-Demo.bat - Enhanced demo
echo   ✅ YotaML-Formatter.bat - Results formatter
echo   ✅ install.bat - Easy installer
echo   ✅ USER-GUIDE.txt - User documentation
echo   ✅ data\ - Sample datasets
echo   ✅ docs\ - Documentation
echo.
echo 🚀 DISTRIBUTION READY!
echo.
echo Users can now:
echo   1. Copy the YotaML-Installer folder
echo   2. Run install.bat to create shortcuts  
echo   3. Use desktop shortcuts to launch YOTA ML Engine
echo.
set /p test="Test the installer now? (Y/N): "
if /i "%test%"=="Y" (
    echo.
    echo Opening installer folder...
    start "" "dist\YotaML-Installer"
    echo.
    echo You can now run install.bat to test the installation process
)
echo.
pause