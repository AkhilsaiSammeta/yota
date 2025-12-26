@echo off
title YOTA ML Engine - Uninstaller
echo.
echo ⚠️  YOTA ML Engine Removal Tool
echo ═══════════════════════════════════════════════════════════════════
echo.
echo This will remove YOTA ML Engine shortcuts from your system.
echo The application files in this folder will remain unchanged.
echo.
set /p confirm="Continue with removal? (Y/N^): "
if /i "%confirm%" NEQ "Y" (
    echo.
    echo ✅ Removal cancelled. YOTA ML Engine is still available.
    pause
    exit /b 0
)

echo 🗑️  Removing shortcuts...

del "%USERPROFILE%\Desktop\YOTA ML Engine.bat" >nul 2>&1
echo ✅ Desktop shortcut removed

echo ✅ YOTA ML Engine shortcuts removed successfully!
echo.
echo 💡 To completely remove YOTA ML Engine:
echo    1. Delete this entire folder
echo    2. Empty your Recycle Bin
echo.
echo The application has been removed from your system.
pause
