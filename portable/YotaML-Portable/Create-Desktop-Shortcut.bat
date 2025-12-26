@echo off
title YOTA ML Engine - Desktop Shortcut Creator
echo.
echo 🔗 Creating desktop shortcut for YOTA ML Engine...
echo.
set "CURRENT_DIR=%CD%"
set "DESKTOP=%USERPROFILE%\Desktop"

(
echo @echo off
echo title YOTA ML Engine
echo cd /d "%CURRENT_DIR%"
echo YotaML.exe
) > "%DESKTOP%\YOTA ML Engine.bat"

echo ✅ Desktop shortcut created successfully!
echo.
echo You can now double-click "YOTA ML Engine" on your desktop
echo to launch the machine learning platform.
echo.
pause
