# YOTA ML Engine - Native EXE Generator
# Creates Windows executable files using Launch4j-like approach

param(
    [string]$OutputDir = "dist\exe"
)

$AppName = "YotaML"
$Version = "1.0.0"

function Create-NativeExecutable {
    Write-Host "🔧 Creating native Windows executables..." -ForegroundColor Cyan
    
    # Create output directory
    if (!(Test-Path $OutputDir)) {
        New-Item -Path $OutputDir -ItemType Directory -Force | Out-Null
    }
    
    # Create Console EXE launcher script
    $ConsoleExeContent = @'
using System;
using System.Diagnostics;
using System.IO;
using System.Windows.Forms;

namespace YotaMLLauncher
{
    class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            try
            {
                string appDir = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
                string jarPath = Path.Combine(appDir, "YotaML.jar");
                
                if (!File.Exists(jarPath))
                {
                    MessageBox.Show("YotaML.jar not found!\nPlease ensure the application is properly installed.", 
                                   "YOTA ML Engine", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }
                
                ProcessStartInfo startInfo = new ProcessStartInfo
                {
                    FileName = "java",
                    Arguments = $"-cp \"{jarPath}\" Main",
                    WorkingDirectory = appDir,
                    UseShellExecute = false,
                    CreateNoWindow = false
                };
                
                Process.Start(startInfo);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Failed to start YOTA ML Engine:\n{ex.Message}\n\nPlease ensure Java is installed.", 
                               "YOTA ML Engine", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
'@
    
    # Create Web EXE launcher script
    $WebExeContent = @'
using System;
using System.Diagnostics;
using System.IO;
using System.Windows.Forms;
using System.Threading;

namespace YotaMLWebLauncher
{
    class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            try
            {
                string appDir = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
                string jarPath = Path.Combine(appDir, "YotaML.jar");
                
                if (!File.Exists(jarPath))
                {
                    MessageBox.Show("YotaML.jar not found!\nPlease ensure the application is properly installed.", 
                                   "YOTA ML Engine", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }
                
                // Show loading message
                var loadingForm = new Form
                {
                    Text = "YOTA ML Engine",
                    Size = new System.Drawing.Size(400, 200),
                    StartPosition = FormStartPosition.CenterScreen,
                    FormBorderStyle = FormBorderStyle.FixedDialog,
                    MaximizeBox = false,
                    MinimizeBox = false
                };
                
                var label = new Label
                {
                    Text = "Starting YOTA ML Engine Web Interface...\nOpen browser to: http://localhost:8080",
                    Dock = DockStyle.Fill,
                    TextAlign = System.Drawing.ContentAlignment.MiddleCenter,
                    Font = new System.Drawing.Font("Segoe UI", 10)
                };
                
                loadingForm.Controls.Add(label);
                loadingForm.Show();
                
                // Start web server
                ProcessStartInfo startInfo = new ProcessStartInfo
                {
                    FileName = "java",
                    Arguments = $"-cp \"{jarPath}\" YotaWebServer",
                    WorkingDirectory = appDir,
                    UseShellExecute = false,
                    CreateNoWindow = true
                };
                
                var process = Process.Start(startInfo);
                
                // Wait a moment then open browser
                Thread.Sleep(2000);
                Process.Start(new ProcessStartInfo("http://localhost:8080") { UseShellExecute = true });
                
                loadingForm.Close();
                
                // Keep process alive
                if (process != null && !process.HasExited)
                {
                    process.WaitForExit();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Failed to start YOTA ML Engine:\n{ex.Message}\n\nPlease ensure Java is installed.", 
                               "YOTA ML Engine", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
'@
    
    # Save C# source files
    $ConsoleExeContent | Out-File -FilePath "$OutputDir\YotaML-Console.cs" -Encoding UTF8
    $WebExeContent | Out-File -FilePath "$OutputDir\YotaML-Web.cs" -Encoding UTF8
    
    # Try to compile with csc (C# compiler)
    try {
        $cscPath = Get-Command csc -ErrorAction Stop
        
        Write-Host "📝 Compiling Console executable..." -ForegroundColor Yellow
        & csc.exe /target:winexe /out:"$OutputDir\YotaML-Console.exe" "$OutputDir\YotaML-Console.cs" /reference:System.Windows.Forms.dll
        
        Write-Host "📝 Compiling Web executable..." -ForegroundColor Yellow  
        & csc.exe /target:winexe /out:"$OutputDir\YotaML-Web.exe" "$OutputDir\YotaML-Web.cs" /reference:System.Windows.Forms.dll
        
        Write-Host "✅ Native executables created successfully!" -ForegroundColor Green
        
        # Clean up source files
        Remove-Item "$OutputDir\YotaML-Console.cs"
        Remove-Item "$OutputDir\YotaML-Web.cs"
        
    } catch {
        Write-Host "⚠️  C# compiler not found. Creating batch file alternatives..." -ForegroundColor Yellow
        
        # Create enhanced batch files as alternatives
        @"
@echo off
title YOTA ML Engine - Console Interface
setlocal

REM Check if JAR exists
if not exist "%~dp0YotaML.jar" (
    echo ERROR: YotaML.jar not found!
    echo Please ensure YOTA ML Engine is properly installed.
    pause
    exit /b 1
)

REM Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 8+ and try again
    echo.
    echo Download Java from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Launch application
echo Starting YOTA ML Engine...
echo.
cd /d "%~dp0"
java -cp YotaML.jar Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to start YOTA ML Engine
    echo Please check the error messages above
)
echo.
pause
"@ | Out-File -FilePath "$OutputDir\YotaML-Console.bat" -Encoding ASCII

        @"
@echo off
title YOTA ML Engine - Web Interface
setlocal

REM Check if JAR exists
if not exist "%~dp0YotaML.jar" (
    echo ERROR: YotaML.jar not found!
    echo Please ensure YOTA ML Engine is properly installed.
    pause
    exit /b 1
)

REM Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 8+ and try again
    echo.
    echo Download Java from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Launch web interface
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                YOTA ML Engine - Web Interface                  ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo Starting web server...
echo Open your browser to: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

REM Open browser after a delay
start /min cmd /c "timeout /t 3 >nul && start http://localhost:8080"

REM Start server
cd /d "%~dp0"
java -cp YotaML.jar YotaWebServer

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to start YOTA ML Engine Web Server
    echo Please check the error messages above
    pause
)
"@ | Out-File -FilePath "$OutputDir\YotaML-Web.bat" -Encoding ASCII

        Write-Host "📝 Enhanced batch launchers created as .bat files" -ForegroundColor Green
    }
}

# Execute the function
Create-NativeExecutable

Write-Host ""
Write-Host "🎉 Executable generation complete!" -ForegroundColor Green
Write-Host "📁 Files created in: $OutputDir" -ForegroundColor White
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "1. Copy YotaML.jar to the $OutputDir directory" -ForegroundColor White
Write-Host "2. Test the executables" -ForegroundColor White
Write-Host "3. Create an installer package" -ForegroundColor White