' YOTA ML Engine - VBScript Professional Launcher
Dim objShell, objFSO, appDir, jarPath

Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")
appDir = objFSO.GetParentFolderName(WScript.ScriptFullName)
jarPath = appDir & "\app\YotaML.jar"

' Show professional startup dialog
result = MsgBox("🚀 YOTA ML Engine v1.0.0" & vbCrLf & vbCrLf & _
                "Professional Machine Learning Platform" & vbCrLf & vbCrLf & _
                "Click OK to start the web server and open browser..." & vbCrLf & _
                "🌐 Web Interface: http://localhost:8080", _
                vbOKCancel + vbInformation, "YOTA ML Engine")

If result = vbCancel Then WScript.Quit

' Check Java
On Error Resume Next
objShell.Run "java -version", 0, True
If Err.Number <> 0 Then
    MsgBox "Java Runtime Environment required!" & vbCrLf & vbCrLf & _
           "Please install Java 8+ from:" & vbCrLf & _
           "https://www.oracle.com/java/technologies/downloads/", _
           vbCritical, "YOTA ML Engine"
    WScript.Quit 1
End If
On Error GoTo 0

' Check JAR file
If Not objFSO.FileExists(jarPath) Then
    MsgBox "Application files missing!" & vbCrLf & "Please reinstall YOTA ML Engine.", vbCritical
    WScript.Quit 1
End If

' Start server
objShell.CurrentDirectory = appDir  
objShell.Run "java -Xms256m -Xmx1g -cp """ & jarPath & """ YotaWebServer", 1, False

' Wait and open browser
WScript.Sleep 3000
objShell.Run "http://localhost:8080", 1, False

' Success message
MsgBox "✅ YOTA ML Engine started successfully!" & vbCrLf & vbCrLf & _
       "🌐 Web browser should now be open" & vbCrLf & _
       "🔗 URL: http://localhost:8080" & vbCrLf & vbCrLf & _
       "To stop: Close the command window", _
       vbInformation, "YOTA ML Engine"
