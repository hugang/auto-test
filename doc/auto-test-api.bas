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
    ' use vba to access url and get the response
    Dim http As Object
    Set http = CreateObject("MSXML2.ServerXMLHTTP")
    http.SetTimeouts 100000, 100000, 100000, 100000

    Dim url As String
    url = "http://localhost:9191/local?mode=xlsx&testcases=" & testcases & "&path=" & replace(Replace(ThisWorkbook.Path & "\" & ThisWorkbook.Name, "\", "%5C"),":", "%3A")

    http.Open "GET", url, False
    http.send ""

    Dim response As String
    response = http.responseText
End Sub
