# YOTA ML Engine - Advanced PowerShell Installer
# Creates a professional Windows installer experience

param(
    [switch]$Uninstall
)

$AppName = "YOTA ML Engine"
$Version = "1.0.0"
$Publisher = "YOTA Labs"
$InstallDir = "$env:ProgramFiles\$AppName"
$StartMenuDir = "$env:ProgramData\Microsoft\Windows\Start Menu\Programs\$AppName"

function Write-ColorText {
    param([string]$Text, [string]$Color = "White")
    Write-Host $Text -ForegroundColor $Color
}

function Create-Shortcuts {
    $WScriptShell = New-Object -ComObject WScript.Shell
    
    # Desktop shortcuts
    $DesktopPath = [Environment]::GetFolderPath("Desktop")
    
    # Console shortcut
    $ConsoleShortcut = $WScriptShell.CreateShortcut("$DesktopPath\YOTA ML Console.lnk")
    $ConsoleShortcut.TargetPath = "java"
    $ConsoleShortcut.Arguments = "-cp `"$InstallDir\YotaML.jar`" Main"
    $ConsoleShortcut.WorkingDirectory = $InstallDir
    $ConsoleShortcut.IconLocation = "java.exe,0"
    $ConsoleShortcut.Description = "YOTA ML Engine - Console Interface"
    $ConsoleShortcut.Save()
    
    # Web interface shortcut
    $WebShortcut = $WScriptShell.CreateShortcut("$DesktopPath\YOTA ML Web.lnk")
    $WebShortcut.TargetPath = "java"
    $WebShortcut.Arguments = "-cp `"$InstallDir\YotaML.jar`" YotaWebServer"
    $WebShortcut.WorkingDirectory = $InstallDir
    $WebShortcut.IconLocation = "java.exe,0"
    $WebShortcut.Description = "YOTA ML Engine - Web Interface"
    $WebShortcut.Save()
    
    # Start Menu shortcuts
    if (!(Test-Path $StartMenuDir)) {
        New-Item -Path $StartMenuDir -ItemType Directory -Force | Out-Null
    }
    
    $ConsoleStartMenu = $WScriptShell.CreateShortcut("$StartMenuDir\YOTA ML Console.lnk")
    $ConsoleStartMenu.TargetPath = "java"
    $ConsoleStartMenu.Arguments = "-cp `"$InstallDir\YotaML.jar`" Main"
    $ConsoleStartMenu.WorkingDirectory = $InstallDir
    $ConsoleStartMenu.IconLocation = "java.exe,0"
    $ConsoleStartMenu.Save()
    
    $WebStartMenu = $WScriptShell.CreateShortcut("$StartMenuDir\YOTA ML Web.lnk")
    $WebStartMenu.TargetPath = "java"
    $WebStartMenu.Arguments = "-cp `"$InstallDir\YotaML.jar`" YotaWebServer"
    $WebStartMenu.WorkingDirectory = $InstallDir
    $WebStartMenu.IconLocation = "java.exe,0"
    $WebStartMenu.Save()
}

function Test-Java {
    try {
        $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
        if ($javaVersion -match '"(\d+)\.') {
            $majorVersion = [int]$matches[1]
            if ($majorVersion -ge 8) {
                return $true
            }
        }
        return $false
    }
    catch {
        return $false
    }
}

function Install-Application {
    Write-ColorText "╔═══════════════════════════════════════════════════════════╗" "Cyan"
    Write-ColorText "║               YOTA ML Engine Installer                     ║" "Cyan"  
    Write-ColorText "║               Version $Version                                   ║" "Cyan"
    Write-ColorText "╚═══════════════════════════════════════════════════════════╝" "Cyan"
    Write-Host ""
    
    # Check if running as administrator
    $currentUser = [Security.Principal.WindowsIdentity]::GetCurrent()
    $principal = New-Object Security.Principal.WindowsPrincipal($currentUser)
    if (-not $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
        Write-ColorText "❌ This installer requires administrator privileges." "Red"
        Write-ColorText "Please run PowerShell as Administrator and try again." "Yellow"
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    # Check Java installation
    Write-ColorText "🔍 Checking Java installation..." "Yellow"
    if (-not (Test-Java)) {
        Write-ColorText "❌ Java 8+ is required but not found!" "Red"
        Write-ColorText "Please install Java from: https://www.oracle.com/java/technologies/downloads/" "Yellow"
        Write-ColorText "Or OpenJDK from: https://openjdk.org/" "Yellow"
        Read-Host "Press Enter to exit"
        exit 1
    }
    Write-ColorText "✅ Java installation found." "Green"
    
    # Create installation directory
    Write-ColorText "📁 Creating installation directory..." "Yellow"
    if (Test-Path $InstallDir) {
        Write-ColorText "⚠️  Installation directory exists. Removing old files..." "Yellow"
        Remove-Item -Path $InstallDir -Recurse -Force
    }
    New-Item -Path $InstallDir -ItemType Directory -Force | Out-Null
    
    # Copy application files
    Write-ColorText "📦 Copying application files..." "Yellow"
    if (Test-Path "YotaML.jar") {
        Copy-Item "YotaML.jar" $InstallDir
    } else {
        Write-ColorText "❌ YotaML.jar not found! Please build the application first." "Red"
        exit 1
    }
    
    if (Test-Path "data") {
        Copy-Item -Path "data" -Destination $InstallDir -Recurse
    }
    
    if (Test-Path "docs") {
        Copy-Item -Path "docs" -Destination $InstallDir -Recurse  
    }
    
    # Create shortcuts
    Write-ColorText "🔗 Creating shortcuts..." "Yellow"
    Create-Shortcuts
    
    # Add to Windows Programs list
    Write-ColorText "📝 Registering with Windows..." "Yellow"
    $UninstallPath = "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$AppName"
    if (Test-Path $UninstallPath) {
        Remove-Item $UninstallPath -Recurse
    }
    New-Item -Path $UninstallPath -Force | Out-Null
    Set-ItemProperty -Path $UninstallPath -Name "DisplayName" -Value $AppName
    Set-ItemProperty -Path $UninstallPath -Name "DisplayVersion" -Value $Version
    Set-ItemProperty -Path $UninstallPath -Name "Publisher" -Value $Publisher
    Set-ItemProperty -Path $UninstallPath -Name "InstallLocation" -Value $InstallDir
    Set-ItemProperty -Path $UninstallPath -Name "UninstallString" -Value "powershell.exe -ExecutionPolicy Bypass -File `"$InstallDir\uninstall.ps1`""
    Set-ItemProperty -Path $UninstallPath -Name "DisplayIcon" -Value "java.exe,0"
    Set-ItemProperty -Path $UninstallPath -Name "NoModify" -Value 1
    Set-ItemProperty -Path $UninstallPath -Name "NoRepair" -Value 1
    
    # Create uninstaller
    $UninstallScript = @"
# YOTA ML Engine Uninstaller
Write-Host "Uninstalling YOTA ML Engine..." -ForegroundColor Yellow

# Remove shortcuts
Remove-Item "$([Environment]::GetFolderPath("Desktop"))\YOTA ML Console.lnk" -ErrorAction SilentlyContinue
Remove-Item "$([Environment]::GetFolderPath("Desktop"))\YOTA ML Web.lnk" -ErrorAction SilentlyContinue
Remove-Item "$StartMenuDir" -Recurse -ErrorAction SilentlyContinue

# Remove from registry
Remove-Item "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$AppName" -Recurse -ErrorAction SilentlyContinue

# Remove installation directory
Remove-Item "$InstallDir" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "YOTA ML Engine has been uninstalled successfully." -ForegroundColor Green
Read-Host "Press Enter to exit"
"@
    $UninstallScript | Out-File -FilePath "$InstallDir\uninstall.ps1" -Encoding UTF8
    
    # Create launcher batch files for compatibility
    @"
@echo off
title YOTA ML Engine - Console
cd /d "%~dp0"
java -cp YotaML.jar Main
pause
"@ | Out-File -FilePath "$InstallDir\Console.bat" -Encoding ASCII
    
    @"
@echo off
title YOTA ML Engine - Web Interface  
cd /d "%~dp0"
echo Starting YOTA ML Engine Web Interface...
echo Open browser to: http://localhost:8080
echo.
start http://localhost:8080
java -cp YotaML.jar YotaWebServer
"@ | Out-File -FilePath "$InstallDir\Web.bat" -Encoding ASCII
    
    Write-Host ""
    Write-ColorText "🎉 Installation Complete!" "Green"
    Write-ColorText "═══════════════════════════════════════════════════════════" "Green"
    Write-ColorText "YOTA ML Engine has been installed successfully!" "Green"
    Write-Host ""
    Write-ColorText "📁 Installed to: $InstallDir" "White"
    Write-ColorText "🖥️  Desktop shortcuts created" "White"
    Write-ColorText "📋 Start Menu entries created" "White"
    Write-Host ""
    Write-ColorText "🚀 How to run:" "Cyan"
    Write-ColorText "   • Double-click 'YOTA ML Console' on desktop for console interface" "White"
    Write-ColorText "   • Double-click 'YOTA ML Web' on desktop for web interface" "White"
    Write-ColorText "   • Or find YOTA ML Engine in Start Menu" "White"
    Write-Host ""
    
    $choice = Read-Host "Would you like to launch YOTA ML Engine now? (Y/N)"
    if ($choice -eq 'Y' -or $choice -eq 'y') {
        Write-ColorText "🚀 Launching YOTA ML Engine..." "Green"
        Start-Process -FilePath "java" -ArgumentList "-cp `"$InstallDir\YotaML.jar`" Main" -WorkingDirectory $InstallDir
    }
}

function Uninstall-Application {
    Write-ColorText "🗑️  Uninstalling YOTA ML Engine..." "Yellow"
    
    # Remove shortcuts
    Remove-Item "$([Environment]::GetFolderPath("Desktop"))\YOTA ML Console.lnk" -ErrorAction SilentlyContinue
    Remove-Item "$([Environment]::GetFolderPath("Desktop"))\YOTA ML Web.lnk" -ErrorAction SilentlyContinue
    Remove-Item $StartMenuDir -Recurse -ErrorAction SilentlyContinue
    
    # Remove from registry
    Remove-Item "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$AppName" -Recurse -ErrorAction SilentlyContinue
    
    # Remove installation directory
    Remove-Item $InstallDir -Recurse -Force -ErrorAction SilentlyContinue
    
    Write-ColorText "✅ YOTA ML Engine has been uninstalled successfully." "Green"
    Read-Host "Press Enter to exit"
}

# Main execution
if ($Uninstall) {
    Uninstall-Application
} else {
    Install-Application
}