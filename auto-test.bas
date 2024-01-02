Attribute VB_Name = "runAutoTest"
' entry point
Sub run()
    Dim command As String
    Dim parameters As String
    Dim batPath As String
    Dim testcases As String

    testcases = InputBox("Please enter your test cases(eg. test case 1 to 3 and test case 12 [1-3,12]):")
    If testcases = "" Then
        Exit Sub
    End If

    batPath = Environ("AUTO_TEST_HOME") & "\auto-test.bat"

    command = "cmd.exe"
    parameters = "/c " & batPath & " -d " & ThisWorkbook.Path & " -f " & ThisWorkbook.Path & "\" & ThisWorkbook.Name & " -c " & testcases & " -m xlsx"
    CreateObject("WScript.Shell").Exec(command & " " & parameters).StdOut.ReadAll

    ' Wait 2s for the command to finish
    Application.Wait (Now + TimeValue("0:00:02"))

    ' read result from file
    Dim fso As Object
    Dim ts As Object
    Dim str As String
    Dim result As String
    Dim resultPath As String
    resultPath = Environ("AUTO_TEST_HOME") & "\result.txt"
    Set fso = CreateObject("Scripting.FileSystemObject")
    Set ts = fso.OpenTextFile(resultPath, 1)
    str = ts.ReadAll
    ts.Close
    Set ts = Nothing
    Set fso = Nothing
    result = "Test Result:" & vbCrLf & str
    ' Display the output in a message box
    MsgBox result
End Sub
