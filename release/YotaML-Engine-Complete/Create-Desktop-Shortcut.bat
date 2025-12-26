@echo off
echo 🔗 Creating desktop shortcut...
set "TARGET=%CD%\YotaML.bat"
set "SHORTCUT=%USERPROFILE%\Desktop\YOTA ML Engine.lnk"
echo Set oWS = WScript.CreateObject("WScript.Shell") > temp.vbs
echo sLinkFile = "%SHORTCUT%" >> temp.vbs
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> temp.vbs
echo oLink.TargetPath = "%TARGET%" >> temp.vbs
echo oLink.WorkingDirectory = "%CD%" >> temp.vbs
echo oLink.Description = "YOTA ML Engine - Machine Learning Platform" >> temp.vbs
echo oLink.Save >> temp.vbs
cscript /nologo temp.vbs
del temp.vbs
echo ✅ Desktop shortcut created!
pause
