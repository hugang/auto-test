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
    Set xmlhttp = CreateObject("MSXML2.ServerXMLHTTP")
    xmlhttp.setTimeouts -1, -1, -1, -1

    Dim url As String
    url = "http://localhost:9191/local"
    Dim body As String
     body = "{""mode"":""xlsx"",""testcases"":""" & testcases & """,""path"":""" & Replace(ThisWorkbook.Path & "\" & ThisWorkbook.Name, "\", "/") & """}"
    ' Open the connection to the URL
    xmlhttp.Open "POST", url, False
    ' Set the request headers
    xmlhttp.setRequestHeader "Content-Type", "application/json"
    ' Send the request with the body
    xmlhttp.send body
    ' Get the response text
    response = xmlhttp.responseText
    ' Output the response (you can handle it as needed)
    MsgBox response
    ' Clean up
    Set xmlhttp = Nothing
End Sub

