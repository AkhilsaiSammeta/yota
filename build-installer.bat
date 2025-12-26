@echo off
echo ===============================================
echo YOTA ML Engine - Installer Builder
echo ===============================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 21+ first
    pause
    exit /b 1
)

echo [1/6] Preparing build directories...
if not exist "dist" mkdir dist
if not exist "dist\yota-installer" mkdir dist\yota-installer
if not exist "dist\yota-installer\lib" mkdir dist\yota-installer\lib
if not exist "dist\yota-installer\data" mkdir dist\yota-installer\data
if not exist "dist\yota-installer\docs" mkdir dist\yota-installer\docs

echo [2/6] Compiling all Java classes...
javac -cp bin -d bin src\*.java src\core\*.java src\evaluation\*.java src\ui\*.java src\io\*.java src\algorithms\core\*.java src\algorithms\classifier\*.java 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Some classes may not have compiled. Continuing...
)

echo [3/6] Creating JAR file...
cd bin
jar cfm ..\dist\yota-installer\YotaML.jar ..\MANIFEST.MF . 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Creating JAR without manifest...
    jar cf ..\dist\yota-installer\YotaML.jar .
)
cd ..

echo [4/6] Copying application files...
copy sample_data.csv dist\yota-installer\data\ >nul 2>&1
copy test_upload.csv dist\yota-installer\data\ >nul 2>&1
copy README.md dist\yota-installer\docs\ >nul 2>&1
copy *.md dist\yota-installer\docs\ >nul 2>&1

echo [5/6] Creating launcher scripts...
REM Create Windows launcher
(
echo @echo off
echo title YOTA ML Engine
echo cd /d "%%~dp0"
echo echo Starting YOTA ML Engine...
echo java -cp YotaML.jar Main
echo pause
) > dist\yota-installer\YotaML-Console.bat

REM Create Web launcher
(
echo @echo off
echo title YOTA ML Engine - Web Interface
echo cd /d "%%~dp0"
echo echo Starting YOTA ML Engine Web Interface...
echo echo Open browser to: http://localhost:8080
echo java -cp YotaML.jar YotaWebServer
echo pause
) > dist\yota-installer\YotaML-Web.bat

REM Create installer script
(
echo @echo off
echo echo YOTA ML Engine - Installation
echo echo ==============================
echo echo.
echo set INSTALL_DIR=%%PROGRAMFILES%%\YotaML
echo echo Installing to: %%INSTALL_DIR%%
echo if not exist "%%INSTALL_DIR%%" mkdir "%%INSTALL_DIR%%"
echo xcopy /E /I /Y . "%%INSTALL_DIR%%"
echo echo.
echo echo Creating desktop shortcuts...
echo set DESKTOP=%%USERPROFILE%%\Desktop
echo ^(
echo echo @echo off
echo echo cd /d "%%INSTALL_DIR%%"
echo echo java -cp YotaML.jar Main
echo echo pause
echo ^) ^> "%%DESKTOP%%\YOTA ML Console.bat"
echo ^(
echo echo @echo off
echo echo cd /d "%%INSTALL_DIR%%"
echo echo start http://localhost:8080
echo echo java -cp YotaML.jar YotaWebServer
echo ^) ^> "%%DESKTOP%%\YOTA ML Web.bat"
echo echo.
echo echo Installation complete!
echo echo You can now run YOTA ML from your desktop or Start Menu
echo pause
) > dist\yota-installer\install.bat

echo [6/6] Creating user guide...
(
echo # YOTA ML Engine - User Installation Guide
echo.
echo ## Quick Install
echo 1. Extract the installer to any folder
echo 2. Run `install.bat` as Administrator
echo 3. Use desktop shortcuts to launch the application
echo.
echo ## Manual Usage
echo - Double-click `YotaML-Console.bat` for console interface
echo - Double-click `YotaML-Web.bat` for web interface ^(http://localhost:8080^)
echo.
echo ## System Requirements
echo - Windows 10/11
echo - Java 21+ installed
echo - 512MB RAM minimum
echo - 100MB disk space
echo.
echo ## Features
echo - 4 Machine Learning algorithms
echo - Professional web interface
echo - CSV data loading
echo - Real-time analytics
echo - Export results
) > dist\yota-installer\USER-GUIDE.md

echo.
echo ✅ Build complete!
echo.
echo 📁 Installer files created in: dist\yota-installer\
echo.
echo Next steps:
echo 1. Test the installer by running dist\yota-installer\install.bat
echo 2. Create a ZIP file of the yota-installer folder for distribution
echo 3. Users can extract and run install.bat to install YOTA ML Engine
echo.
pause