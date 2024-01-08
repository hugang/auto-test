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
    set cmdObj = CreateObject("WScript.Shell").Exec(command & " " & parameters)

    ' Wait for the command until finish
    Do While cmdObj.Status = 0
        Application.Wait (Now + TimeValue("0:00:01"))
    Loop

    ' Display the output in a message box
    MsgBox "Test finished!"
End Sub
