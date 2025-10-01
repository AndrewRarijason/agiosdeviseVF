Sub injector()
Dim Aerosmith As Integer
Dim zro As Integer


Application.DisplayAlerts = False

nomF = ThisWorkbook.Path & "\" & Mid(CDate(Now), 7, 4) & Mid(CDate(Now), 4, 2) & Mid(CDate(Now), 1, 2) & "." & Mid(CDate(Now), 12, 2) & Mid(CDate(Now), 15, 2) & ".INJECTION.ART.csv"
Set INJC = Sheets("INJC")
INJC.Copy After:=Sheets(Sheets.Count)
Sheets(Sheets.Count).Name = "INJBKUP"
Set InjB = Sheets("INJBKUP")

Aerosmith = 1

Do While InjB.Cells(Aerosmith, 1) <> ""
    
    If InjB.Cells(Aerosmith, 3) = "211000" And InjB.Cells(Aerosmith + 4, 15) < 2000 Then
        Range(Cells(Aerosmith, 1), Cells(Aerosmith + 6, 1)).EntireRow.Delete
        Else
        Aerosmith = Aerosmith + 1
        End If
    
    Loop

zro = 1

Do While InjB.Cells(zro, 1) <> ""

    If InjB.Cells(zro, 15) <> 0 Then
        zro = zro + 1
        Else
        InjB.Cells(zro, 15).EntireRow.Delete
        End If
       
    Loop

fx = 1

Do Until IsEmpty(InjB.Cells(fx, 1))
JUST = ""

icol = 1
    
    Do Until icol = 59
        JUST = JUST & InjB.Cells(fx, icol) & "|"
        icol = icol + 1
        Loop
    
    InjB.Cells(fx, 1) = JUST
    Range(Cells(fx, 2), Cells(fx, 70)).Clear
    
fx = fx + 1
Loop
InjB.Select
    
    Dim fso As Scripting.FileSystemObject
    Dim tsTxtfile As Scripting.TextStream

Set Form = Range(Cells(1, 1), Cells(fx, 1))
Set fso = New Scripting.FileSystemObject
Set tsTxtfile = fso.CreateTextFile(nomF, True)


    For x = 1 To Form.Rows.Count
        Set JUST = InjB.Cells(x, 1)
        If x < Form.Rows.Count - 1 Then
            tsTxtfile.writeline (JUST)
            Else
            tsTxtfile.Write (JUST)
            End If
        Next x

            tsTxtfile.Close

InjB.Delete
INJC.Delete

Application.DisplayAlerts = True

End Sub




