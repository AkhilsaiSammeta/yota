@echo off
echo ╔════════════════════════════════════════════════════════════════╗
echo ║            YOTA ML Engine - Complete Build System             ║
echo ║                 Creates Distributable Package                 ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

REM Set variables
set BUILD_DIR=build
set DIST_DIR=dist\YotaML-Installer
set VERSION=1.0.0

echo [1/8] Preparing build environment...
if exist %BUILD_DIR% rmdir /s /q %BUILD_DIR%
if exist %DIST_DIR% rmdir /s /q %DIST_DIR%
mkdir %BUILD_DIR%
mkdir %DIST_DIR%
mkdir %DIST_DIR%\data
mkdir %DIST_DIR%\docs
mkdir %DIST_DIR%\lib

echo [2/8] Compiling Java application...
echo Compiling core classes...
javac -cp bin -d %BUILD_DIR% src\core\*.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\evaluation\*.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\ui\*.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\io\*.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\algorithms\core\*.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\algorithms\classifier\*.java >nul 2>&1

echo Compiling main applications...
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\Main.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\YotaWebServer.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\EnhancedKNNDemo.java >nul 2>&1
javac -cp %BUILD_DIR% -d %BUILD_DIR% src\QuickKNNFormatter.java >nul 2>&1

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed! Please check for errors.
    pause
    exit /b 1
)

echo [3/8] Creating JAR file...
cd %BUILD_DIR%
jar cfm ..\%DIST_DIR%\YotaML.jar ..\MANIFEST.MF . >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Creating JAR without manifest...
    jar cf ..\%DIST_DIR%\YotaML.jar . >nul 2>&1
)
cd ..

echo [4/8] Copying data files...
copy sample_data.csv %DIST_DIR%\data\ >nul 2>&1
copy test_upload.csv %DIST_DIR%\data\ >nul 2>&1
copy *.md %DIST_DIR%\docs\ >nul 2>&1

echo [5/8] Creating executable launchers...
REM Console launcher
(
echo @echo off
echo title YOTA ML Engine - Console
echo.
echo REM Check Java installation
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Java is not installed or not in PATH
echo     echo.
echo     echo Please install Java JDK 8+ from:
echo     echo https://www.oracle.com/java/technologies/downloads/
echo     echo.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo ╔════════════════════════════════════════════════════════════════╗
echo echo ║                 YOTA ML Engine - Console Mode                 ║
echo echo ╚════════════════════════════════════════════════════════════════╝
echo echo.
echo echo Loading machine learning engine...
echo echo.
echo cd /d "%%~dp0"
echo java -Xmx1g -cp YotaML.jar Main
echo echo.
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Application failed to start
echo     echo Please check the error messages above
echo ^)
echo pause
) > %DIST_DIR%\YotaML-Console.bat

REM Web launcher
(
echo @echo off
echo title YOTA ML Engine - Web Interface
echo.
echo REM Check Java installation
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Java is not installed or not in PATH
echo     echo.
echo     echo Please install Java JDK 8+ from:
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
echo echo 🌐 Starting web server...
echo echo 📱 Open browser to: http://localhost:8080
echo echo ⏹️  Press Ctrl+C to stop server
echo echo.
echo.
echo REM Open browser after delay
echo start /min cmd /c "timeout /t 4 ^>nul ^&^& start http://localhost:8080"
echo.
echo cd /d "%%~dp0"
echo java -Xmx1g -cp YotaML.jar YotaWebServer
echo.
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Web server failed to start
echo     echo Please check the error messages above
echo     pause
echo ^)
) > %DIST_DIR%\YotaML-Web.bat

REM Enhanced demo launcher
(
echo @echo off
echo title YOTA ML Engine - Enhanced Demo
echo cd /d "%%~dp0"
echo java -cp YotaML.jar EnhancedKNNDemo
echo pause
) > %DIST_DIR%\YotaML-Demo.bat

REM Results formatter launcher
(
echo @echo off
echo title YOTA ML Engine - Results Formatter
echo cd /d "%%~dp0"
echo java -cp YotaML.jar QuickKNNFormatter
echo pause
) > %DIST_DIR%\YotaML-Formatter.bat

echo [6/8] Creating installation script...
(
echo @echo off
echo title YOTA ML Engine - Installer
echo echo ╔════════════════════════════════════════════════════════════════╗
echo echo ║              YOTA ML Engine - Professional Installer          ║
echo echo ║                         Version %VERSION%                           ║
echo echo ╚════════════════════════════════════════════════════════════════╝
echo echo.
echo.
echo REM Check admin rights
echo net session ^>nul 2^>^&1
echo if %%errorlevel%% NEQ 0 ^(
echo     echo ❌ Administrator privileges required!
echo     echo Right-click and select "Run as administrator"
echo     pause
echo     exit /b 1
echo ^)
echo.
echo REM Check Java
echo echo 🔍 Checking Java installation...
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Java JDK 8+ is required but not found!
echo     echo.
echo     echo Please install Java from:
echo     echo https://www.oracle.com/java/technologies/downloads/
echo     echo.
echo     pause
echo     exit /b 1
echo ^)
echo echo ✅ Java found
echo.
echo REM Set installation paths
echo set INSTALL_DIR=%%PROGRAMFILES%%\YOTA ML Engine
echo set DESKTOP=%%PUBLIC%%\Desktop
echo set STARTMENU=%%PROGRAMDATA%%\Microsoft\Windows\Start Menu\Programs\YOTA ML Engine
echo.
echo echo 📁 Installing to: %%INSTALL_DIR%%
echo echo.
echo REM Create directories
echo if not exist "%%INSTALL_DIR%%" mkdir "%%INSTALL_DIR%%"
echo if not exist "%%STARTMENU%%" mkdir "%%STARTMENU%%"
echo.
echo echo 📦 Copying files...
echo xcopy /E /I /Y . "%%INSTALL_DIR%%" ^>nul
echo.
echo echo 🔗 Creating shortcuts...
echo REM Desktop shortcuts
echo ^(
echo echo @echo off
echo echo cd /d "%%INSTALL_DIR%%"
echo echo YotaML-Console.bat
echo ^) ^> "%%DESKTOP%%\YOTA ML Console.bat"
echo.
echo ^(
echo echo @echo off
echo echo cd /d "%%INSTALL_DIR%%"
echo echo YotaML-Web.bat
echo ^) ^> "%%DESKTOP%%\YOTA ML Web.bat"
echo.
echo REM Start Menu shortcuts
echo copy "%%DESKTOP%%\YOTA ML Console.bat" "%%STARTMENU%%\" ^>nul
echo copy "%%DESKTOP%%\YOTA ML Web.bat" "%%STARTMENU%%\" ^>nul
echo.
echo REM Add to Windows Programs
echo echo 📝 Registering application...
echo reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\YOTA ML Engine" /v "DisplayName" /d "YOTA ML Engine" /f ^>nul
echo reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\YOTA ML Engine" /v "DisplayVersion" /d "%VERSION%" /f ^>nul
echo reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\YOTA ML Engine" /v "Publisher" /d "YOTA Labs" /f ^>nul
echo reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\YOTA ML Engine" /v "InstallLocation" /d "%%INSTALL_DIR%%" /f ^>nul
echo.
echo echo ✅ Installation completed successfully!
echo echo.
echo echo 🚀 How to use:
echo echo   • Double-click "YOTA ML Console" on desktop for ML algorithms
echo echo   • Double-click "YOTA ML Web" on desktop for web interface
echo echo   • Or find YOTA ML Engine in Start Menu
echo echo.
echo set /p choice="Launch YOTA ML Engine now? (Y/N): "
echo if /i "%%choice%%" EQU "Y" ^(
echo     echo 🚀 Starting YOTA ML Engine...
echo     start "" "%%INSTALL_DIR%%\YotaML-Console.bat"
echo ^)
echo echo.
echo pause
) > %DIST_DIR%\install.bat

echo [7/8] Creating user documentation...
(
echo # YOTA ML Engine - User Guide
echo.
echo ## Quick Start
echo 1. Run `install.bat` as Administrator to install
echo 2. Use desktop shortcuts to launch the application
echo.
echo ## Features
echo - **Console Mode**: Traditional command-line ML interface
echo - **Web Interface**: Modern browser-based interface ^(http://localhost:8080^)
echo - **Multiple Algorithms**: KNN, Decision Tree, Naive Bayes, Logistic Regression
echo - **Data Analysis**: Power BI-style statistics and visualization
echo - **CSV Support**: Load your own datasets
echo.
echo ## System Requirements
echo - Windows 10/11
echo - Java JDK 8+ installed
echo - 512MB RAM minimum
echo - 100MB disk space
echo.
echo ## Launchers
echo - `YotaML-Console.bat` - Start console interface
echo - `YotaML-Web.bat` - Start web interface
echo - `YotaML-Demo.bat` - Run enhanced demo
echo - `YotaML-Formatter.bat` - Format results
echo.
echo ## Troubleshooting
echo If you get "Java not found" errors:
echo 1. Install Java from https://www.oracle.com/java/technologies/downloads/
echo 2. Make sure Java is in your PATH environment variable
echo 3. Restart command prompt and try again
echo.
echo ## Support
echo For issues or questions, check the documentation in the `docs` folder.
) > %DIST_DIR%\README.txt

echo [8/8] Creating distribution package...
echo Creating ZIP file for distribution...
powershell -Command "Compress-Archive -Path '%DIST_DIR%\*' -DestinationPath 'YotaML-Engine-v%VERSION%-Installer.zip' -Force" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️  ZIP creation failed. Manual packaging required.
) else (
    echo ✅ Distribution package created: YotaML-Engine-v%VERSION%-Installer.zip
)

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                    🎉 BUILD COMPLETE! 🎉                      ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo ✅ YOTA ML Engine distributable package created!
echo.
echo 📁 Files created:
echo   • %DIST_DIR%\ - Full installer directory
echo   • YotaML-Engine-v%VERSION%-Installer.zip - Distribution package
echo.
echo 🚀 Distribution instructions:
echo   1. Share the ZIP file with users
echo   2. Users extract and run install.bat as Administrator
echo   3. Application will be installed with desktop shortcuts
echo.
echo 🧪 Testing instructions:
echo   1. Test install.bat in the %DIST_DIR% directory
echo   2. Verify all launchers work correctly
echo   3. Test both console and web interfaces
echo.
set /p choice="Test the installer now? (Y/N): "
if /i "%choice%" EQU "Y" (
    echo Opening installer directory...
    start "" "%DIST_DIR%"
)
echo.
pause