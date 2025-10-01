
Sub BkTau()

Dim cnLogs As New ADODB.Connection
Dim rsData As New ADODB.Recordset

        AnteD1 = Sheets("main").Cells(105, 1)
        SP = Sheets("main").Cells(103, 1)
        ID = Sheets("main").Cells(102, 1)

Application.ScreenUpdating = False

strConn = "DSN=DLTBMOIST;UID=" & ID & ";PWD=" & SP & ";Database=DELTAPRODUCTION"
cnLogs.Open strConn

strsql = "select tind from bank.bktau where dev not like '969' and dva='" & AnteD1 & "' order by dev;"

rsData.Open strsql, cnLogs

    For icol = 0 To rsData.Fields.Count - 1
        Sheets("data").Cells(1, icol + 1).Value = rsData.Fields(icol).Name
    Next

Sheets("data").Range("AB1").CopyFromRecordset rsData

rsData.Close
cnLogs.Close

Set cnLogs = Nothing
Set rsData = Nothing


End Sub

Sub cleaner()

Set BKD = Sheets("bkdar")
Set BKH = Sheets("bkhis")
D0 = Sheets("main").Cells(100, 1)
D1 = Sheets("main").Cells(101, 1)
ID = Sheets("main").Cells(102, 1)
TMO = "01/" & Mid(D0 - 75, 4, 2) & "/" & Mid(D0 - 75, 7, 4)

    msb = MsgBox("BKDAR", vbOKCancel, "Cleaner")
    
    If msb = vbOK Then
        
        Range("c1").Select
        x = Range("c1").End(xlDown).Row
        
        Do While x > 2
    
        NCP = BKD.Cells(x, 3)
        DEV = BKD.Cells(x, 2)
        AGE = BKD.Cells(x, 1)
        
            Do While Cells(x - 1, 13) = Cells(x, 13) And Cells(x - 1, 3) = NCP And Cells(x - 1, 2) = DEV And Cells(x - 1, 1) = AGE
                
                BKD.Cells(x, 3).Select
                
                If Cells(x, 14) >= Cells(x - 1, 14) Then
                    Cells(x - 1, 14).EntireRow.Delete
                    Else
                    Cells(x, 14).EntireRow.Delete
                    Exit Do
                End If

            Loop
            
        x = x - 1
        Loop
        
        End If
        
    msb = MsgBox("BKHIS", vbOKCancel, "Cleaner")

    
        y = 1
        Do While BKH.Cells(y, 1) <> ""
            
            DCO = BKH.Cells(y, 4)
            DVA = BKH.Cells(y, 5)
            RtN = True
            
            If DCO < D0 And DVA < D0 Then
                RtN = False
                ElseIf DVA < D0 And DCO > D1 Then
                RtN = False
                ElseIf DVA >= D0 And DVA <= D1 And DCO > D1 Then
                RtN = False
            End If
            
            If RtN = False Then
                BKH.Cells(y, 1).EntireRow.Delete
                Else
                y = y + 1
                End If
        
        Loop
            
End Sub


Sub bmed(Form As Range)

    Form.Select
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
 
End Sub

Sub bCol(Form As Range)

    Form.Select
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlInsideVertical)
        .LineStyle = xlGray25
        .ColorIndex = 0
        .TintAndShade = 0.75
        .Weight = xlThin
    End With
    With Selection.Borders(xlInsideHorizontal)
        .LineStyle = xlNone
    End With
 
End Sub


Sub bmix(Form As Range)

    Form.Select
    Selection.Borders(xlDiagonalDown).LineStyle = xlNone
    Selection.Borders(xlDiagonalUp).LineStyle = xlNone
    With Selection.Borders(xlEdgeLeft)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeTop)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeBottom)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlEdgeRight)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlInsideVertical)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection.Borders(xlInsideHorizontal)
        .LineStyle = xlContinuous
        .ColorIndex = 0
        .TintAndShade = 0
        .Weight = xlThin
    End With
    With Selection
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
        .WrapText = True
    End With
 
End Sub

Sub mercen(Form As Range)

Form.Select
    
    With Selection
        .MergeCells = True
        .HorizontalAlignment = xlCenter
        .VerticalAlignment = xlCenter
    End With

End Sub

