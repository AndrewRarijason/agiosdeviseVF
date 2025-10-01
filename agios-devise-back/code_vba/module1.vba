                                 
Option Explicit

Dim MNV1, MNV2, MNV3 As Single

Dim D0, D1, DCO, DVA, TMO, OpnD1, AnteD1 As Date
Dim ID, SP, wbs As String
Dim i, msb, EVT  As Integer
Dim ON_sld, ON_dar, ON_his, RtN, Kor As Boolean

Dim cnLogs As New ADODB.Connection
Dim rsData As New ADODB.Recordset
Dim strsql, strConn As String
Dim icol, irow As Long

Dim x, y, z, hx, sx, dx, ox, ax, px, injx, Pejy, PGN As Long
Dim MAIN, BKS, BKD, BKH, BKC, EXL, PDF, AGIO, DATA, INJC, InjB, Temp, NIF As Worksheet
Dim AGE, DEV, NCP, JUST, NUMINJ, CLC, ADev, NCL As String
Dim MoN, NbX As Single
Dim BKHISC, BKHISD, BKHIST, BKSLDT, BKDART, COMP, BKHISE, BKX, rtC, rtD As Long
Dim tpfd1, tpfd2, tpfd3 As Single

Dim fx, fy, fz, pfx, pfy, modx, gx, gy, gz, cnf As Long
Dim Form As Range
Dim bor_set As String
Dim adrF, nomF As String
Dim fs As Object
Dim nWbk As Workbook
Dim AdA As String

Dim Mde, Mdp, Cp As Integer
Dim Tx, Ty, Tz, slsh As Integer

Dim LogoupN, logodwnN As String





Sub start()
        
        D0 = Sheets("main").Cells(100, 1)
        D1 = Sheets("main").Cells(101, 1)
        SP = Sheets("main").Cells(103, 1)
        ID = Sheets("main").Cells(102, 1)

TMO = "01/" & Mid(D0 - 75, 4, 2) & "/" & Mid(D0 - 75, 7, 4)

Set MAIN = Sheets("MAIN")
Application.ScreenUpdating = True

End Sub

Sub imp_sld()

        D0 = Sheets("main").Cells(100, 1)
        D1 = Sheets("main").Cells(101, 1)
        SP = Sheets("main").Cells(103, 1)
        ID = Sheets("main").Cells(102, 1)

Application.ScreenUpdating = False

Sheets.Add After:=Sheets(Sheets.Count)
Sheets(Sheets.Count).Name = "BKSLD"
Set BKS = Sheets("BKSLD")

msb = MsgBox("En cas d'erreur de connexion ODBC, veuillez appuyer sur 'Fin' et fermer l'outil." & vbNewLine & "Avant d'effectuer une nouvelle tentative, vérifier si le complément Microsoft ActiveX Data Objects Library 2.8 est activé dans les références VBA et si votre identifiant et votre mot de passe sont corrects", vbOKCancel + vbInformation, "ODBC Warning")

If msb <> vbOK Then
    msb = MsgBox("Traitement abandonné", vbOKOnly, "ODBC Warning")
    Application.DisplayAlerts = False
    BKS.Delete
    Application.DisplayAlerts = True
    Exit Sub
    End If

strConn = "DSN=DLTBMOIST;UID=" & ID & ";PWD=" & SP & ";Database=DELTAPRODUCTION"
cnLogs.Open strConn

strsql = "select a.age, a.dev, a.ncp, b.sde from bank.bkcom a full join bank.bksld b on a.age=b.age and a.dev=b.dev and a.ncp=b.ncp where a.dev not like '969' and b.dva = '" & D1 & "' and a.cha = '211000' and a.cpro = '202';"

rsData.Open strsql, cnLogs

    For icol = 0 To rsData.Fields.Count - 1
        BKS.Cells(1, icol + 1).Value = rsData.Fields(icol).Name
    Next

BKS.Range("A1").CopyFromRecordset rsData

rsData.Close
cnLogs.Close

Set cnLogs = Nothing
Set rsData = Nothing

BKS.UsedRange.EntireColumn.AutoFit

If BKS.Cells(1, 1) = "AGE" Then
    Cells(1, 1).EntireRow.Delete
    End If

Sheets("MAIN").Select

Application.ScreenUpdating = True

Sheets("MAIN").Range("B5") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("B5") = CDate(Now)

msb = MsgBox("Importation et vérification de la table BKSLD complètées.", vbInformation, "BKSLD")

End Sub


Sub imp_dar()

Dim cnLogs As New ADODB.Connection
Dim rsData As New ADODB.Recordset
Dim DerOg, pAss As Boolean

        D0 = Sheets("main").Cells(100, 1)
        D1 = Sheets("main").Cells(101, 1)
        SP = Sheets("main").Cells(103, 1)
        ID = Sheets("main").Cells(102, 1)

Application.ScreenUpdating = False

Sheets.Add After:=Sheets(Sheets.Count)
Sheets(Sheets.Count).Name = "BKDAR"
Set BKD = Sheets("BKDAR")
BKD.Range("n:n").NumberFormat = "@"
msb = MsgBox("En cas d'erreur de connexion ODBC, veuillez appuyer sur 'Fin' et fermer l'outil." & vbNewLine & "Avant d'effectuer une nouvelle tentative, vérifier si le complément Microsoft ActiveX Data Objects Library 2.8 est activé dans les références VBA et si votre identifiant et votre mot de passe sont corrects", vbOKCancel + vbInformation, "ODBC Warning")


If msb <> vbOK Then
    msb = MsgBox("Traitement abandonné", vbOKOnly, "ODBC Warning")
    Application.DisplayAlerts = False
    BKD.Delete
    Application.DisplayAlerts = True
    Exit Sub
    End If

strConn = "DSN=DLTBMOIST;UID=" & ID & ";PWD=" & SP & ";Database=DELTAPRODUCTION"
cnLogs.Open strConn

strsql = "select  a.age, a.dev, a.ncp, b.nbc, b.txc, c.nbr, c.taux, a.solde, e.nomrest, e.adr1, e.adr2, e.cpos, f.clc, c.datr, d.tcli, e.vil, a.cli from bank.bkdar a left join bank.bkdarc b on b.age=a.age and b.dev=a.dev and b.ncp=a.ncp and b.annee=a.annee and b.mois=a.mois left join bank.bkdard c on c.age=a.age and c.dev=a.dev and c.ncp=a.ncp and c.annee=a.annee and c.mois=a.mois left join bank.bkcli d on d.cli=a.cli full join infoc.xextcli e on e.cli = a.cli full join bank.bkcom f on f.age=a.age and f.dev=a.dev and f.ncp=a.ncp where a.annee = '" & Mid(D1, 7, 4) & "' and a.mois='" & Mid(D1, 4, 2) & "' and a.dev not like '969' and f.cha='211000' and f.cpro='202' and f.cfe='N' order by a.ncp;"

rsData.Open strsql, cnLogs

    For icol = 0 To rsData.Fields.Count - 1
        BKD.Cells(1, icol + 1).Value = rsData.Fields(icol).Name
    Next

BKD.Range("A1").CopyFromRecordset rsData

rsData.Close
cnLogs.Close

Set cnLogs = Nothing
Set rsData = Nothing

BKD.UsedRange.EntireColumn.AutoFit
BKD.Select
'    Set Form = Range("d:d,e:e,f:f,g:g,h:h,i:i,j:j,k:k,l:l,m:m,n:n,o:o,p:p,q:q,r:r,v:v,w:w,x:x,y:y,z:z,aa:aa,ab:ab")
'    Form.EntireColumn.Hidden = True
    
'Intérêts Créditeurs Injection
msb = MsgBox("Souhaitez-vous injecter le fichier contenant les nouvelles valeurs des taux créditeurs négatifs pour chaque devise du trimestre?", vbYesNo + vbExclamation, "Table des taux créditeurs négatifs")

If msb = vbYes Then

    adrF = Application.GetOpenFilename("Fichiers Excel, *.xlsx")
    If Not adrF = False Then
        slsh = InStrRev(adrF, "\")
        nomF = Mid(adrF, slsh + 1, Len(adrF) - slsh)
        Workbooks.Open Filename:=adrF, local:=True
        Workbooks(nomF).Sheets(1).Copy After:=ThisWorkbook.Sheets(ThisWorkbook.Sheets.Count)
        Workbooks(nomF).Close savechanges:=False
        End If
    
    Sheets(Sheets.Count).Name = "TEMP"
    Set Temp = Sheets("temp")
    Tx = 1
    Do While Temp.Cells(Tx, 1) <> ""
        If Len(Temp.Cells(Tx, 1)) = 3 Then
            Ty = 1
            Else
            Ty = 2
            End If
            
            Do While Sheets("data").Cells(Ty, 25) <> "" And Ty < 25
                If Sheets("data").Cells(Ty, 25) = Mid(Temp.Cells(Tx, 1), 1, 3) Then
                    Sheets("data").Cells(Ty, 27) = Temp.Cells(Tx, 2)
                    Exit Do
                    End If
                Ty = Ty + 1
                Loop
            
        Tx = Tx + 1
        Loop
    
    Application.DisplayAlerts = False
    
    Temp.Delete
    
    End If
    


x = 1

If BKD.Cells(1, 1) = "AGE" Then
    Cells(1, 1).EntireRow.Delete
    End If

'modification taux créditeur nul
Do While Not IsEmpty(BKD.Cells(x, 1))

    DEV = BKD.Cells(x, 2)
    Tz = 1
    Do Until Sheets("data").Cells(Tz, 24) = DEV Or Tz = 25
        Tz = Tz + 1
        Loop
    Sheets("bkdar").Cells(x, 5) = Sheets("data").Cells(Tz, 27)
    
    x = x + 1
    Loop
    

    
    
'application dérogations individuelles

msb = MsgBox("Souhaitez-vous injecter le fichier contenant les dérogations sur les comptes autorisés?", vbYesNo + vbExclamation, "Dérogations")

If msb = vbYes Then
    adrF = Application.GetOpenFilename("Fichiers Excel, *.xlsx")
    If Not adrF = False Then
        slsh = InStrRev(adrF, "\")
        nomF = Mid(adrF, slsh + 1, Len(adrF) - slsh)
        Workbooks.Open Filename:=adrF, local:=True
        Workbooks(nomF).Sheets(1).Copy After:=ThisWorkbook.Sheets(ThisWorkbook.Sheets.Count)
        Workbooks(nomF).Close savechanges:=False
        End If
    
    Sheets(Sheets.Count).Name = "TEMP"
    Set Temp = Sheets("temp")
    DerOg = False
        
Tx = 1
If Len(Temp.Cells(Tx, 1)) <> 5 Then
Tx = 2
End If

    Do While Temp.Cells(Tx, 1) <> ""
        
        Ty = 1
        If Len(BKD.Cells(Ty, 1)) <> 5 Then
            Ty = 2
            End If
        
        CLC = UCase(Mid(Temp.Cells(Tx, 10), 1, 1))
        
        If CLC = "O" Then
            
            Do Until BKD.Cells(Ty, 3) = Temp.Cells(Tx, 3) And BKD.Cells(Ty, 2) = Temp.Cells(Tx, 2) And BKD.Cells(Ty, 1) = Temp.Cells(Tx, 1)
                
                If BKD.Cells(Ty, 3) = "" Then
                    DerOg = True
                    Temp.Select
                    Cells(Tx, 1).EntireRow.Font.Color = RGB(200, 2, 2)
                    Exit Do
                    End If
                
            Ty = Ty + 1
            Loop
            
            If BKD.Cells(Ty, 3) <> "" Then
                    Temp.Select
                    Cells(Tx, 1).EntireRow.Font.Color = RGB(20, 200, 150)
                    BKD.Select
                    Cells(Ty, 1).EntireRow.Font.Color = RGB(20, 200, 150)
                    BKD.Cells(Ty, 5) = 0
                End If
                
            End If
    
    Tx = Tx + 1
    Loop



        
    Application.DisplayAlerts = False
    
Temp.Select

    adrF = ThisWorkbook.Path & "\"
    nomF = Mid(CDate(Now), 7, 4) & Mid(CDate(Now), 4, 2) & Mid(CDate(Now), 1, 2) & "." & Mid(CDate(Now), 12, 2) & Mid(CDate(Now), 15, 2) & ".DEROGATIONS SUR LES COMPTES"

    Temp.Copy: Set nWbk = ActiveWorkbook
    nWbk.SaveAs adrF & nomF & ".xlsx", FileFormat:=51
    nWbk.Close True
    
    Temp.Delete
    
    Application.DisplayAlerts = True
    
    If DerOg Then
    msb = MsgBox("Des dérogations n'ont pas pu être appliquées car les comptes ne sont pas présents dans la table BKDAR de l'outil. Vous pouvez consulter le fichier généré par l'outil contenant la liste des dérogations mise à jour avec les dérogations non appliquées en rouge.", vbOKOnly + vbInformation, "Liste des comptes avec dérogation")
    End If
    
    End If



Do While x > 2
    
NCP = Sheets("bkdar").Cells(x, 3)
DEV = Sheets("bkdar").Cells(x, 2)
AGE = Sheets("bkdar").Cells(x, 1)

    Do While Cells(x - 1, 13) = Cells(x, 13) And Cells(x - 1, 3) = NCP And Cells(x - 1, 2) = DEV And Cells(x - 1, 1) = AGE
        
        Sheets("bkdar").Cells(x, 3).Select
        
        If Cells(x, 14) >= Cells(x - 1, 14) Then
            Cells(x - 1, 14).EntireRow.Delete
            Else
            Cells(x, 14).EntireRow.Delete
            Exit Do
            End If
    
    Loop
    
x = x - 1
Loop



Sheets("MAIN").Select

Sheets("MAIN").Range("B10") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("B10") = CDate(Now)

msb = MsgBox("Mise à jour des taux de change", vbOKOnly + vbInformation, "Data")

Call BkTau

msb = MsgBox("Importation et vérification des données d'arrêté complètées.", vbInformation, "BKDAR")
Sheets("data").Cells(1, 1) = "00001"

End Sub

Sub imp_his()

Dim cnLogs As New ADODB.Connection
Dim rsData As New ADODB.Recordset

Application.ScreenUpdating = False

Worksheets.Add After:=Sheets(Sheets.Count)
Worksheets(Sheets.Count).Name = "BKHIS"
Set BKH = Sheets("BKHIS")

BKH.Select
Range("a:a,b:b,c:c,d:d,e:e").NumberFormat = "@"

D0 = Sheets("main").Cells(100, 1)
D1 = Sheets("main").Cells(101, 1)
SP = Sheets("main").Cells(103, 1)
ID = Sheets("main").Cells(102, 1)
TMO = "01/" & Mid(D0 - 75, 4, 2) & "/" & Mid(D0 - 75, 7, 4)
strConn = "DSN=DLTBMOIST;UID=" & ID & ";PWD=" & SP & ";Database=DELTAPRODUCTION"
cnLogs.Open strConn

msb = MsgBox("En cas d'erreur de connexion ODBC, veuillez appuyer sur 'Fin' et fermer l'outil." & vbNewLine & "Avant d'effectuer une nouvelle tentative, vérifier si le complément Microsoft ActiveX Data Objects Library 2.8 est activé dans les références VBA et si votre identifiant et votre mot de passe sont corrects", vbOKCancel + vbInformation, "ODBC Warning")

If msb <> vbOK Then
    msb = MsgBox("Traitement abandonné", vbOKOnly, "ODBC Warning")
    Application.DisplayAlerts = False
    BKH.Delete
    Application.DisplayAlerts = True
    Exit Sub
    End If


    strsql = "select a.age, a.dev, a.ncp, a.dva, a.dva, a.mon, a.sen  from bank.bkhis a full join bank.bkcom b on a.age=b.age and a.dev=b.dev and a.ncp=b.ncp where a.dva between '" & TMO & "' and '" & D1 & "' and b.cha = '211000' and b.cpro = '202' and a.dev not like '969' order by a.dva;"
    rsData.Open strsql, cnLogs
    For icol = 0 To rsData.Fields.Count - 1
        BKH.Cells(1, icol + 1).Value = rsData.Fields(icol).Name
    Next
    BKH.Range("a1").CopyFromRecordset rsData

    rsData.Close
    cnLogs.Close
    

    Set cnLogs = Nothing
    Set rsData = Nothing

BKH.UsedRange.EntireColumn.AutoFit

If BKH.Cells(1, 1) = "AGE" Then
    Cells(1, 1).EntireRow.Delete
    End If



hx = 1
Do While Not IsEmpty(BKH.Cells(hx, 1))
    
    BKH.Cells(hx, 6).Select

    RtN = True
    DCO = BKH.Cells(hx, 4)
    DVA = BKH.Cells(hx, 5)

    If DCO < D0 And DVA < D0 Then
        RtN = False
    ElseIf DCO > D1 Then
        RtN = False
    End If

    If RtN = True Then
    hx = hx + 1
    Else
    BKH.Cells(hx, 1).EntireRow.Delete
    End If
    
Loop

Sheets("MAIN").Select
Sheets("MAIN").Range("B15") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("B15") = CDate(Now)

msb = MsgBox("Importation et vérification de la table BKHIS complètées.", vbInformation, "BKHIS")

End Sub

Sub soldc()


Dim lastrow As Long
Dim Pdone, Pcalc As Single
Dim Cko As Integer

Application.ScreenUpdating = False

Set BKH = Sheets("BKHIS")
Set BKD = Sheets("BKDAR")
Set BKS = Sheets("BKSLD")
D0 = CDate(Sheets("main").Cells(100, 1))
D1 = CDate(Sheets("main").Cells(101, 1))
TMO = CDate("01/" & Mid(CDate(D0) - 75, 4, 2) & "/" & Mid(CDate(D0) - 75, 7, 4))
AnteD1 = CDate(Sheets("main").Cells(105, 1))

BKD.Select
Cells(1, 1).Select

lastrow = 1
Do While BKD.Cells(lastrow + 1, 1) <> ""
lastrow = lastrow + 1
Loop

ufprogress.labelprogress.Width = 0
ufprogress.Show

Sheets("MAIN").Range("c20") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("c20") = CDate(Now)
    
    ox = 0
    dx = 1
    Do While Not IsEmpty(BKD.Cells(dx, 1))
        AGE = BKD.Cells(dx, 1)
        DEV = BKD.Cells(dx, 2)
        NCP = BKD.Cells(dx, 3)
        BKDART = BKD.Cells(dx, 8)
        JUST = ""
        rtC = 0
        
        Pdone = dx / lastrow
        Pcalc = WorksheetFunction.Round(100 * Pdone, 2)
        With ufprogress
            .Caption = "Vérification des soldes... " & Pcalc & "%"
            .Labelcaption.Caption = "Comptes " & dx & " sur " & lastrow & " - KO : " & Cko & " - Anomalie : " & rtC
            .labelprogress.Width = Pdone * 204
            End With
            DoEvents
        
            sx = 1
            Do While Not IsEmpty(BKS.Cells(sx, 1))
                If BKS.Cells(sx, 3) = NCP And BKS.Cells(sx, 2) = DEV And BKS.Cells(sx, 1) = AGE Then
                BKSLDT = BKS.Cells(sx, 4)
                Exit Do
                End If
            sx = sx + 1
            Loop
            
            hx = 1
            
            BKHISC = 0
            BKHISD = 0
            
            Do While Not IsEmpty(BKH.Cells(hx, 1))

            DVA = BKH.Cells(hx, 5)
            DCO = BKH.Cells(hx, 4)

                If BKH.Cells(hx, 3) = NCP And BKH.Cells(hx, 2) = DEV And BKH.Cells(hx, 1) = AGE Then
                    
                    If DVA >= D0 And DVA <= D1 And DCO < D0 And BKH.Cells(hx, 7) = "C" Then
                        BKHISC = BKHISC + BKH.Cells(hx, 6)
                    ElseIf DVA >= D0 And DVA <= D1 And DCO < D0 And BKH.Cells(hx, 7) = "D" Then
                        BKHISD = BKHISD + BKH.Cells(hx, 6)
                        
                    ElseIf DVA >= D0 And DVA <= D1 And DCO >= D0 And DCO <= D1 And BKH.Cells(hx, 7) = "C" Then
                        BKHISC = BKHISC + BKH.Cells(hx, 6)
                    ElseIf DVA >= D0 And DVA <= D1 And DCO >= D0 And DCO <= D1 And BKH.Cells(hx, 7) = "D" Then
                        BKHISD = BKHISD + BKH.Cells(hx, 6)
                        
                    ElseIf DVA < D0 And DCO >= D0 And DCO <= D1 And BKH.Cells(hx, 7) = "C" Then
                        BKHISC = BKHISC + BKH.Cells(hx, 6)
                        BKD.Cells(dx, 19) = "R"
                        JUST = JUST & "R." & hx & "_"
                    ElseIf DVA < D0 And DCO >= D0 And DCO <= D1 And BKH.Cells(hx, 7) = "D" Then
                        BKHISD = BKHISD + BKH.Cells(hx, 6)
                        BKD.Cells(dx, 19) = "R"
                        JUST = JUST & "R." & hx & "_"
                        
                    Else
                        BKH.Cells(hx, 10) = "UNK"
                        rtC = rtC + 1

                        
                    End If
                End If
            
            hx = hx + 1
            Loop

        
        COMP = WorksheetFunction.Round((BKDART + BKHISC - BKHISD - BKSLDT), 2)
        
        
        
        If COMP <> 0 Then
            BKD.Cells(dx, 20) = "SOLDE KO"
            BKD.Cells(dx, 21) = JUST
            BKD.Cells(dx, 22) = BKDART + BKHISC - BKHISD
            BKD.Cells(dx, 23) = BKSLDT
            Cko = Cko + 1
            
            ox = ox + 1
            Else: BKD.Cells(dx, 20) = ""
            End If
        
        
    
    dx = dx + 1
    
    Loop

Unload ufprogress

Sheets("MAIN").Range("c21") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("c21") = CDate(Now)
Sheets("MAIN").Range("c22") = ox & "/" & lastrow & " COMPTES KO DETECTES."

msb = MsgBox("Vérification des données de solde terminée.", vbOKOnly + vbInformation, "BKDAR")

Sheets("MAIN").Select

End Sub

Sub InjectionGen()

Application.DisplayAlerts = False
Application.ScreenUpdating = False


Dim supa As Integer
supa = Sheets.Count

' Suppression BKCOM si existe
Do While supa > 0
If Sheets(supa).Name = "BKCOM" Then
    Sheets(supa).Delete
    Else
    supa = supa - 1
    End If
    Loop

'Impression et modulo lignes
Mde = 40
Mdp = Mde + 11

i = Sheets.Count

' Suppression feuilles inutiles
Do While i > 0
    If Not (Sheets(i).Name = "BKCOM" Or Sheets(i).Name = "MAIN" Or Sheets(i).Name = "ARRT" Or Sheets(i).Name = "BKSLD" Or Sheets(i).Name = "BKHIS" Or Sheets(i).Name = "BKDAR" Or Sheets(i).Name = "BINJC" Or Sheets(i).Name = "DATA") Then
        Sheets(i).Delete
        i = i - 1
        Else: i = i - 1
        End If
    Loop

Dim lastrow As Long
Dim Pdone As Single
Dim rCnt As Long

Set BKH = Sheets("BKHIS")
Set BKD = Sheets("BKDAR")
Set DATA = Sheets("DATA")
' Date du premier jour du trimestre
D0 = Sheets("main").Cells(100, 1)

' Date du dernier jour du trimestre
D1 = Sheets("main").Cells(101, 1)

'Identifiant BMOI
ID = Sheets("main").Cells(102, 1)

'Date du permier jour ouvré du trimestre suivant
OpnD1 = Sheets("MAIN").Cells(104, 1)

'Date du dernier jour ouvré du trimestre précédent
AnteD1 = Sheets("main").Cells(105, 1)


' D0 - 75 jours (Pour 01/01/2025 : 01/10/2024)
TMO = "01/" & Mid(D0 - 75, 4, 2) & "/" & Mid(D0 - 75, 7, 4)
modx = 1

' Compte nb total ligne feuille BKHIS
BKH.Select
rCnt = 1
Do Until BKH.Cells(rCnt, 1) = ""
    rCnt = rCnt + 1
    Loop

' Création feuille INJC
Sheets.Add After:=Sheets(Sheets.Count)
Sheets(Sheets.Count).Name = "INJC"
Set INJC = Sheets("INJC")

' Initialisation feuille INJC
' Poser date debut et fin de l'execution
Sheets("main").Select
Range("g5") = ""
Range("g6") = ""
Sheets("MAIN").Range("g5") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("g5") = CDate(Now)

BKD.Select
' Compter nb total ligne feuille BKDAR
lastrow = 1
Do While BKD.Cells(lastrow + 1, 1) <> ""
lastrow = lastrow + 1
Loop

Cells(1, 29).EntireColumn.ColumnWidth = 25

ufprogress.labelprogress.Width = 0
ufprogress.Show

dx = 1

Do While Not IsEmpty(BKD.Cells(dx, 1))

    AGE = BKD.Cells(dx, 1)
    DEV = BKD.Cells(dx, 2)
    NCP = BKD.Cells(dx, 3)
    CLC = BKD.Cells(dx, 13)
    rtC = 0
    rtD = 0
    Kor = False
    
        'devise
    
    x = 1
    ' Verifier si la devise existe dans la table BKDAR
    Do While DATA.Cells(x, 24) <> DEV
        x = x + 1
        If x > 30 Then
            MsgBox "Devise erronée " & DEV
            End If
        Loop

    ' Récupérer le code devise
    ADev = DATA.Cells(x, 25)

Pdone = dx / lastrow
'labelprogress
        With ufprogress
            .Caption = "Génération du fichier d'injection " & Round(100 * Pdone, 2) & "%"
            .Labelcaption.Caption = "Comptes " & dx & " de " & lastrow
            .labelprogress.Width = Pdone * 204
            End With
            DoEvents
' Fin labelprogress

gy = 1
' Recuperation nom AGE
Do While DATA.Cells(gy, 1) <> ""
    If DATA.Cells(gy, 1) = AGE Then
        AdA = DATA.Cells(gy, 2)
        Exit Do
        End If
    gy = gy + 1
    Loop

' Copie de la feuille ARRT dans une nouvelle feuille nommé NCP.DEV
    Sheets("ARRT").Copy After:=Sheets(Sheets.Count)
    Sheets(Sheets.Count).Name = NCP & "." & DEV
    ' Mettre la feuille créée en variable EXL
    Set EXL = Sheets(Sheets.Count)
    
    EXL.Select
    Range("b:b").NumberFormat = "dd/mm/yyyy"
    
    'VALEURS

    EXL.Cells(8, 7) = BKD.Cells(dx, 5) / 100
    EXL.Cells(9, 7) = BKD.Cells(dx, 7) / 100
    
    'CALCUL LADDER
'------------------------------------------------------------------------------------------------------------------------------------------
' dx s'incrementera de 1 à chaque compte de la table BKDAR
' hx s'incrementera de 1 à chaque ligne de la table BKHIS
' ax s'incrementera de 1 à chaque ligne de la feuille EXL

Dim hMoN, Sld As Single
Dim RtN As Boolean

'RtN : present une ecriture de retour R
RtN = False
Sld = BKD.Cells(dx, 8)

hx = 1
Do While Not IsEmpty(BKH.Cells(hx, 1))

DCO = BKH.Cells(hx, 4)
DVA = BKH.Cells(hx, 5)
' Attribution montant crédit/débit dans hMoN
If BKH.Cells(hx, 7) = "C" Then
    hMoN = BKH.Cells(hx, 6)
    Else: hMoN = -BKH.Cells(hx, 6)
    End If

If BKD.Cells(dx, 19) = "R" Then
    RtN = True
    End If
    
ax = 6
Do While EXL.Cells(ax, 2) <> ""
    ax = ax + 1
    Loop
    
    If BKH.Cells(hx, 3) = NCP And BKH.Cells(hx, 2) = DEV And BKH.Cells(hx, 1) = AGE Then
        
        If ax = 6 And DVA >= D0 Then
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
            EXL.Cells(ax + 1, 1) = hMoN
            EXL.Cells(ax + 1, 3) = EXL.Cells(ax, 3) + hMoN
            EXL.Cells(ax + 1, 2) = DVA
            
'            ElseIf DVA < D0 Then
'                EXL.Cells(ax + 1, 1).Select
'                Selection.EntireRow.Insert
'                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
'                With Selection.Borders(xlEdgeBottom)
'                    .LineStyle = xlContinuous
'                    End With
'            EXL.Cells(ax, 1) = hMoN
'            EXL.Cells(ax, 2) = DVA
            
            ElseIf DVA >= D0 And EXL.Cells(ax - 1, 2) < D0 Then
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
            EXL.Cells(ax + 1, 1) = hMoN
            EXL.Cells(ax + 1, 3) = EXL.Cells(ax, 3) + hMoN
            EXL.Cells(ax + 1, 2) = DVA
            
            ElseIf DVA >= D0 Then
            EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 1) = hMoN
            EXL.Cells(ax, 3) = EXL.Cells(ax - 1, 3) + hMoN
            EXL.Cells(ax, 2) = DVA
            
            End If
            
        End If
    
hx = hx + 1
Loop

ax = 6
Do While EXL.Cells(ax, 2) <> ""
    ax = ax + 1
    Loop

        If ax = 6 Then
        EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
'        ElseIf ax <> 6 And EXL.Cells(ax - 1, 2) < D0 Then
'                Selection.EntireRow.Insert
'                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
'                With Selection.Borders(xlEdgeBottom)
'                    .LineStyle = xlContinuous
'                    End With
'            EXL.Cells(ax, 2) = D0
'            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
        
            
            End If

'------------------------------------------------------------------------------------------------------------------------------------------
ax = 6
    
        'FINITION TABLEAU

        Do While EXL.Cells(ax, 2) <> ""
    
        'SOLDE

        
            'NB JOURS
            If EXL.Cells(ax + 1, 2) <> "" Then
                If Cells(ax, 2) < D0 Then
                    EXL.Cells(ax, 4) = 0
                    Else
                    EXL.Cells(ax, 4) = DateDiff("d", CDate(EXL.Cells(ax, 2)), CDate(EXL.Cells(ax + 1, 2)))
                    End If
                Else
                EXL.Cells(ax, 4) = DateDiff("d", CDate(EXL.Cells(ax, 2)), Sheets("MAIN").Cells(101, 1)) + 1
                End If
            
            'NBC ET NBD
            If EXL.Cells(ax, 2) >= D0 Then
                If EXL.Cells(ax, 3) * EXL.Cells(ax, 4) < 0 Then
                    EXL.Cells(ax, 5) = EXL.Cells(ax, 3) * EXL.Cells(ax, 4)
                    EXL.Cells(ax, 6) = 0
                    ElseIf EXL.Cells(ax, 3) * EXL.Cells(ax, 4) > 0 Then
                    EXL.Cells(ax, 6) = EXL.Cells(ax, 3) * EXL.Cells(ax, 4)
                    EXL.Cells(ax, 5) = 0
                    Else
                    EXL.Cells(ax, 5) = 0
                    EXL.Cells(ax, 6) = 0
                    End If
                End If
            
            'TAUX PLUS FORT DECOUVERT
                
EXL.Cells(2, 3).EntireRow.AutoFit
            
            'MODULO PAGE IMPRESSION
            
            If (ax - 6) Mod (Mde) = 0 Then
                modx = modx + 1
                End If
            EXL.Cells(ax, 7) = modx
    
        ax = ax + 1
        Loop
           
        modx = 1
        
        EXL.Cells(1, 3) = AGE & " - " & NCP & " - " & DEV
        EXL.Cells(2, 3) = BKD.Cells(dx, 9)
        EXL.Cells(3, 3) = "ARRETE DU " & CStr(D0) & " au " & CStr(D1)
        
        'AJOUT DERNIERE LIGNE
        EXL.Range("d:d").NumberFormat = "_(* #,##0_);_(* (#,##0);_(* ""-""_);_(@_)"
        '--------------------------------------------------------------------------------------------------------------------
        'RECAP
        
        px = 6
        
            Do Until EXL.Cells(px, 2) = ""
            px = px + 1
            Loop


            'AJOUT LIGNE RECAP
    
            EXL.Cells(px, 1).Select
            Selection.EntireRow.Insert
            EXL.Cells(px, 2) = CStr(D1)
            EXL.Cells(px, 3) = EXL.Cells(px - 1, 3)
            
            'RECAP NBR JOURS
            EXL.Cells(px, 4) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 4), EXL.Cells(px - 1, 4)))
               
            'RECAP NBR DEB
            EXL.Cells(px, 5) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 5), EXL.Cells(px - 1, 5)))
            
            'RECAP NBR CDT
            EXL.Cells(px, 6) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 6), EXL.Cells(px - 1, 6)))
            
            EXL.Range(EXL.Cells(px, 1), EXL.Cells(px, 6)).Font.Bold = True
            
            Cells(px + 1, 1).EntireRow.Delete
            
            'CALCUL AGIO
            'Créditeur
            EXL.Cells(px + 2, 6) = (EXL.Cells(px, 6) * EXL.Cells(px + 2, 7)) / 360
            'Débiteur
            EXL.Cells(px + 3, 6) = (EXL.Cells(px, 5) * EXL.Cells(px + 3, 7)) / 360
        
            'NET AGIOS
            EXL.Cells(px + 5, 6) = EXL.Cells(px + 2, 6) + EXL.Cells(px + 3, 6)
            
            If DEV <> 392 Then
                MoN = Round(EXL.Cells(px + 5, 6), 2)
                Else
                MoN = Application.WorksheetFunction.Round(EXL.Cells(px + 5, 6), 0)
                MoN = Application.WorksheetFunction.Round(MoN, 2)
                End If
            
                        
            'TAUX PLUS FORT DECOUVERT
            
            
            'ENREGISTREMENT NET AGIO DE L'ECHELLE DANS BKDAR
        BKD.Cells(dx, 30) = EXL.Cells(px, 6)
        BKD.Cells(dx, 31) = BKD.Cells(dx, 4)
        BKD.Cells(dx, 32) = -EXL.Cells(px, 5)
        BKD.Cells(dx, 33) = BKD.Cells(dx, 6)
        NbX = WorksheetFunction.Round((BKD.Cells(dx, 30) - BKD.Cells(dx, 31) + BKD.Cells(dx, 32) - BKD.Cells(dx, 33)), 2)
        If Abs(BKD.Cells(dx, 30) - BKD.Cells(dx, 31) + BKD.Cells(dx, 32) - BKD.Cells(dx, 33)) > (Abs(BKD.Cells(dx, 31)) + Abs(BKD.Cells(dx, 33))) * 0.02 Then
            If NbX < 0 Then
                NbX = -NbX
                End If
            BKD.Cells(dx, 29) = "NB KO : " & NbX
            End If

            'IRCM
            EXL.Cells(px + 4, 6) = 0
            
            'Indication NOK
            
            If BKD.Cells(dx, 20) <> "" Then
                EXL.Cells(px + 6, 3) = "SOLDE NOK"
                EXL.Cells(px + 6, 5) = "CALCUL " & BKD.Cells(dx, 22)
                EXL.Cells(px + 6, 6) = "BKSLD " & BKD.Cells(dx, 23)
                End If
            If BKD.Cells(dx, 29) <> "" Then
                EXL.Cells(px + 8, 3) = "NOMBRES KO"
                EXL.Cells(px + 8, 5) = "CALCUL"
                    EXL.Cells(px + 9, 5) = BKD.Cells(dx, 30)
                    EXL.Cells(px + 10, 5) = BKD.Cells(dx, 32)
                EXL.Cells(px + 8, 6) = "BKDAR"
                    EXL.Cells(px + 9, 6) = BKD.Cells(dx, 31)
                    EXL.Cells(px + 10, 6) = BKD.Cells(dx, 33)
                EXL.Cells(px + 9, 3) = "NB CREDITEURS"
                EXL.Cells(px + 10, 3) = "NB DEBITEURS"
                End If

    
'FICHIER D'INJECTION
INJC.Select

' injx : auto incrementera la ligne d'injection
injx = 1
Do While INJC.Cells(injx, 1) <> ""
    injx = injx + 1
    Loop

gz = 0


Do While gz <= 6

    ' Si montant = 0, sortir de la boucle
    ' MoN : montant agios arrondi. Si Yen, pas de décimale
    If MoN = 0 Then
        Exit Do
        End If
    
    If gz = 0 Then
    Range(Cells(injx, 1), Cells(injx + 6, 58)).NumberFormat = "@"
    Range(Cells(injx, 15), Cells(injx + 6, 15)).NumberFormat = "0.00"
    End If
    
'COL01 : injx age
    INJC.Cells(injx + gz, 1) = AGE
    
'col02 : injx dev
    If gz < 4 Then
        INJC.Cells(injx + gz, 2) = DEV
        Else: INJC.Cells(injx + gz, 2) = "969"
        End If
        
'col03 : injxcha

    If gz = 0 Then
        INJC.Cells(injx + gz, 3) = "211000"
        ElseIf gz < 3 Then
        INJC.Cells(injx + gz, 3) = "999998"
        ElseIf gz = 3 Then
        INJC.Cells(injx + gz, 3) = "341000"
        ElseIf gz = 4 Then
        INJC.Cells(injx + gz, 3) = "342000"
        ElseIf gz = 5 Then
        INJC.Cells(injx + gz, 3) = "707300"
        INJC.Cells(injx + gz + 1, 3) = "707300"
        End If

'col04 : injxncp

    If gz = 0 Then
        INJC.Cells(injx + gz, 4) = NCP
        ElseIf gz < 3 Then
        INJC.Cells(injx + gz, 4) = "89999980014"
        ElseIf gz = 3 Then
        INJC.Cells(injx + gz, 4) = "83410002031"
        ElseIf gz = 4 Then
            gy = 2
            Do While DATA.Cells(gy, 30) <> ""
                If DATA.Cells(gy, 30) = DEV Then
                    INJC.Cells(injx + gz, 4) = DATA.Cells(gy, 31)
                    Exit Do
                    End If
                gy = gy + 1
                Loop

        ElseIf gz = 5 Then
            If BKD.Cells(dx, 15) = "1" Then
                INJC.Cells(injx + gz, 4) = "87073000410"
                INJC.Cells(injx + gz + 1, 4) = "87073000414"
                ElseIf BKD.Cells(dx, 15) = "2" Then
                INJC.Cells(injx + gz, 4) = "87073000610"
                INJC.Cells(injx + gz + 1, 4) = "87073000614"
                Else
                INJC.Cells(injx + gz, 4) = "87073000210"
                INJC.Cells(injx + gz + 1, 4) = "87073000214"
                End If
        End If
         
'col05 : injxsuf _ empty
'col06 : injxope _ 900

    INJC.Cells(injx + gz, 6) = "900"

'col07 : mvti

    INJC.Cells(injx + gz, 7).NumberFormat = "@"
    EVT = 6 - Len(CStr(injx + gz))
    If EVT = 5 Then
        INJC.Cells(injx + gz, 7) = "00000" & injx + gz
        ElseIf EVT = 4 Then
        INJC.Cells(injx + gz, 7) = "0000" & injx + gz
        ElseIf EVT = 3 Then
        INJC.Cells(injx + gz, 7) = "000" & injx + gz
        ElseIf EVT = 2 Then
        INJC.Cells(injx + gz, 7) = "00" & injx + gz
        ElseIf EVT = 1 Then
        INJC.Cells(injx + gz, 7) = "0" & injx + gz
        Else: INJC.Cells(injx + gz, 7) = injx + gz
        End If

'col08 : rgp _ empty
'col09 : uti (identifiant bmoi)

INJC.Cells(injx + gz, 9) = ID

'col10 : eve empty
'col11 : clc

    If gz = 0 Then
        INJC.Cells(injx + gz, 11) = BKD.Cells(dx, 13)
        
        ElseIf gz = 1 Then
        gy = 1
            Do Until DATA.Cells(gy, 1) = AGE
                gy = gy + 1
            Loop

            INJC.Cells(injx + gz, 11) = DATA.Cells(gy, 5)
            INJC.Cells(injx + gz + 1, 11) = DATA.Cells(gy, 5)
            INJC.Cells(injx + gz + 2, 11) = DATA.Cells(gy, 8)
            
            'If BKD.Cells(dx, 15) = "1" Then
            '    INJC.Cells(injx + gz + 4, 11) = DATA.Cells(gy, 16)
            'ElseIf BKD.Cells(dx, 15) = "2" Then
                'INJC.Cells(injx + gz + 4, 11) = DATA.Cells(gy, 19)
            'Else: INJC.Cells(injx + gz + 4, 11) = DATA.Cells(gy, 22)
                'End If
        End If
    
    x = 2
    y = 32
    If gz = 4 Then
        Do While DATA.Cells(1, y) <> AGE
            Do While DATA.Cells(x, 30) <> DEV
                x = x + 1
                If x > 19 Then
                    msb = MsgBox("Compte non renseigné pour la devise " & DEV, vbOKOnly + vbCritical, "Données de devise")
                    End If
                Loop
            y = y + 1
            Loop
        INJC.Cells(injx + 4, 11) = DATA.Cells(x, y)
        End If
        
    x = 1
    If gz = 6 Then
        Do While DATA.Cells(x, 1) <> AGE
            x = x + 1
            Loop
            If BKD.Cells(dx, 15) = "1" Then
                INJC.Cells(injx + gz, 11) = DATA.Cells(x, 55)
                ElseIf BKD.Cells(dx, 15) = "2" Then
                INJC.Cells(injx + gz, 11) = DATA.Cells(x, 56)
                Else: INJC.Cells(injx + gz, 11) = DATA.Cells(x, 57)
                End If
        End If
        
'col12 : dva
    
    If gz < 2 Then
        'INJC.Cells(injx + gz, 12) = CDate(OpnD1) & " 00:00"
        'Else: INJC.Cells(injx + gz, 12) = CDate(D1) & " 00:00"
        INJC.Cells(injx + gz, 12) = Format(CDate(OpnD1), "dd/mm/yyyy")
        Else: INJC.Cells(injx + gz, 12) = Format(CDate(D1), "dd/mm/yyyy")
        End If
        

'col13 : 0037 _ ser

    INJC.Cells(injx + gz, 13) = "0037"

'col14 : dva

    If gz = 0 Then
        'INJC.Cells(injx + gz, 14) = CStr(D1 + 1) & " 00:00"
        'Else: INJC.Cells(injx + gz, 14) = CStr(D1) & " 00:00"
        INJC.Cells(injx + gz, 14) = CStr(D1 + 1)
        Else: INJC.Cells(injx + gz, 14) = CStr(D1)
        End If

'col16 : sen
    
    x = 1
    Do While DATA.Cells(x, 24) <> DEV
        x = x + 1
        Loop
    
    If gz = 0 Or gz = 2 Then
        INJC.Cells(injx + gz, 16) = "D"
        INJC.Cells(injx + gz + 1, 16) = "C"
        ElseIf gz = 4 Then
        INJC.Cells(injx + gz, 16) = "D"
        INJC.Cells(injx + gz + 1, 16) = "C"
        INJC.Cells(injx + gz + 2, 16) = "C"
        End If

'col15 : mon. Formule : net agios débité * taux de change
' gz = 0 : 211000'
' gz = 1 : 999998'
' gz = 2 : 999998'
' gz = 3 : 341000'
' gz = 4 : 342000 '
' gz = 5 : 707300 '
' gz = 6 : 707300 '
    If gz = 0 Or gz = 2 Then
        INJC.Cells(injx + gz, 15) = -MoN ' 211000 , gz=0 ; gz=2
        INJC.Cells(injx + gz + 1, 15) = -MoN ' 999998, gz=1; gz=3
        End If
        ElseIf gz = 4 Then
        ' taux de change * net agios'
        INJC.Cells(injx + gz, 15) = -WorksheetFunction.Round((DATA.Cells(x, 28) * EXL.Cells(px + 5, 6)), 2) 'gz = 4
        ' taux de change * interet debiteur'
        INJC.Cells(injx + gz + 1, 15) = -WorksheetFunction.Round(DATA.Cells(x, 28) * EXL.Cells(px + 3, 6), 2) 'gz = 5
        ' montant gz 6 : diff montant gz4 - gz5
        INJC.Cells(injx + gz + 2, 15) = WorksheetFunction.Round(INJC.Cells(injx + gz, 15) - INJC.Cells(injx + gz + 1, 15), 2) 'gz = 6
        End If
    
 
'col17 : lib

    If gz = 0 Then
        INJC.Cells(injx + gz, 17) = "AGIOS DU " & CStr(D0) & " AU " & CStr(D1)
        Else
        INJC.Cells(injx + gz, 17) = "ARRETE DE COMPTE AU " & CStr(D1)
        End If

'col18 : exo "O"

    INJC.Cells(injx + gz, 18) = "O"

'col19 : pie _ INJ / AR / T3 / 2020

    If Mid(CStr(D1), 4, 2) < 4 And gz = 0 Then
        INJC.Cells(injx + gz, 19) = "INJAR" & "T1" & Mid(CStr(D1), 7, 4)
        ElseIf Mid(CStr(D1), 4, 2) < 7 Then
        INJC.Cells(injx + gz, 19) = "INJAR" & "T2" & Mid(CStr(D1), 7, 4)
        ElseIf Mid(CStr(D1), 4, 2) < 10 Then
        INJC.Cells(injx + gz, 19) = "INJAR" & "T3" & Mid(CStr(D1), 7, 4)
        Else
        INJC.Cells(injx + gz, 19) = "INJAR" & "T4" & Mid(CStr(D1), 7, 4)
        End If
        
    If gz <> 0 Then
        INJC.Cells(injx + gz, 19) = INJC.Cells(injx + gz - 1, 19)
        End If
        
'col20 : rlet _ MM / AAAA

    INJC.Cells(injx + gz, 20) = Mid(CStr(D1), 4, 2) & Mid(CStr(D1), 7, 4)

'col 21...31 empty



'col32 : ncc _ CAV CLIENT

    INJC.Cells(injx + gz, 32) = NCP
    
'col33...39 empty

'ESI'
    INJC.Cells(injx + gz, 34) = "N"

'col40 AGEM AGE CAV CLIENT

    INJC.Cells(injx + gz, 40) = AGE

'col41 empty agde



'col42 devc _ DEV NCC

    INJC.Cells(injx + gz, 42) = DEV

'col43 mctv _ montant agios en devise montant 211000

    INJC.Cells(injx + gz, 43) = -WorksheetFunction.Round(MoN, 2)

'col44 PIEO _ col19

    INJC.Cells(injx + gz, 44) = INJC.Cells(injx + gz, 19)

'col45...58 empty

    gz = gz + 1
    Loop

INJC.UsedRange.EntireColumn.AutoFit

'sauvegarde calcul
Application.DisplayAlerts = False

EXL.Select
    
    Set fs = CreateObject("scripting.filesystemobject")
    adrF = ThisWorkbook.Path & "\" & "ARRETES EXCEL" & "\"
    nomF = Mid((BKD.Cells(dx, 17)), 1, 8) & "." & EXL.Name
    
    If Not fs.folderexists(adrF) Then
        fs.createfolder adrF
        End If
      
    EXL.Copy: Set nWbk = ActiveWorkbook
    nWbk.SaveAs adrF & nomF & ".xlsx", FileFormat:=51
    nWbk.Close True

adrF = ""
nomF = ""

EXL.Delete

dx = dx + 1
Loop
----------------------------------------
 
Unload ufprogress

INJC.Select

    adrF = ThisWorkbook.Path & "\"
    nomF = Mid(CDate(Now), 7, 4) & Mid(CDate(Now), 4, 2) & Mid(CDate(Now), 1, 2) & "." & Mid(CDate(Now), 12, 2) & Mid(CDate(Now), 15, 2) & ".INJECTION"

    INJC.Copy: Set nWbk = ActiveWorkbook
    nWbk.SaveAs adrF & nomF & ".xlsx", FileFormat:=51
    nWbk.Close True

Call injector

Application.DisplayAlerts = False
Sheets("MAIN").Range("g6") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("g6") = CDate(Now)
Application.DisplayAlerts = True
ThisWorkbook.Save

msb = MsgBox("Fin de la génération du fichier d'injection et des fichiers de calcul.", vbInformation, "Outil de traitement d'arrêté")
Sheets("main").Select

End Sub

Sub FilesGenerator()

Application.DisplayAlerts = False
Application.ScreenUpdating = False

'Impression
Mde = 40
Mdp = Mde + 11

i = Sheets.Count
Do While i > 0
    If Not (Sheets(i).Name = "MAIN" Or Sheets(i).Name = "ARRT" Or Sheets(i).Name = "BKNIF" Or Sheets(i).Name = "BKSLD" Or Sheets(i).Name = "BKHIS" Or Sheets(i).Name = "BKDAR" Or Sheets(i).Name = "BINJC" Or Sheets(i).Name = "DATA") Then
        Sheets(i).Delete
        i = i - 1
        Else: i = i - 1
        End If
    Loop

Dim lastrow As Long
Dim Pdone As Single
Dim rCnt As Long

Set BKH = Sheets("BKHIS")
Set BKD = Sheets("BKDAR")
Set DATA = Sheets("DATA")

D0 = Sheets("main").Cells(100, 1)
D1 = Sheets("main").Cells(101, 1)
ID = Sheets("main").Cells(102, 1)
OpnD1 = Sheets("MAIN").Cells(104, 1)
AnteD1 = Sheets("main").Cells(105, 1)
TMO = "01/" & Mid(D0 - 75, 4, 2) & "/" & Mid(D0 - 75, 7, 4)
modx = 1

BKH.Select
rCnt = Range("A" & Rows.Count).End(xlUp).Row

Sheets("main").Select
Range("g13") = ""
Range("g13") = ""
Sheets("MAIN").Range("g13") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("g13") = CDate(Now)

BKD.Select

lastrow = 1
Do While BKD.Cells(lastrow + 1, 1) <> ""
lastrow = lastrow + 1
Loop

Cells(1, 29).EntireColumn.ColumnWidth = 25

ufprogress.labelprogress.Width = 0
ufprogress.Show

dx = 1
Do While Not IsEmpty(BKD.Cells(dx, 1))
    
    AGE = BKD.Cells(dx, 1)
    DEV = BKD.Cells(dx, 2)
    NCP = BKD.Cells(dx, 3)
    CLC = BKD.Cells(dx, 13)
    NCL = BKD.Cells(dx, 17)
    rtC = 0
    rtD = 0
    Kor = False

Pdone = dx / lastrow
        With ufprogress
            .Caption = "Génération des éditions " & Round(100 * Pdone, 2) & "%"
            .Labelcaption.Caption = "Comptes " & dx & " de " & lastrow
            .labelprogress.Width = Pdone * 204
            End With
            DoEvents

gy = 1
Do While DATA.Cells(gy, 1) <> ""
    If DATA.Cells(gy, 1) = AGE Then
        AdA = DATA.Cells(gy, 2)
        Exit Do
        End If
    gy = gy + 1
    Loop

    Sheets("ARRT").Copy After:=Sheets(Sheets.Count)
    Sheets(Sheets.Count).Name = AGE & "." & DEV
    Set EXL = Sheets(Sheets.Count)
    
    EXL.Select
    Range("b:b").NumberFormat = "dd/mm/yyyy"

    EXL.Cells(8, 7) = BKD.Cells(dx, 5) / 100
    EXL.Cells(9, 7) = BKD.Cells(dx, 7) / 100
    
    'CALCUL LADDER
'------------------------------------------------------------------------------------------------------------------------------------------

Dim hMoN, Sld As Single
Dim RtN As Boolean

RtN = False
Sld = BKD.Cells(dx, 8)


hx = 1
Do While Not IsEmpty(BKH.Cells(hx, 1))

DCO = BKH.Cells(hx, 4)
DVA = BKH.Cells(hx, 5)
If BKH.Cells(hx, 7) = "C" Then
    hMoN = BKH.Cells(hx, 6)
    Else: hMoN = -BKH.Cells(hx, 6)
    End If

If BKD.Cells(dx, 19) = "R" Then
    RtN = True
    End If
    
ax = 6
Do While EXL.Cells(ax, 2) <> ""
    ax = ax + 1
    Loop
    
If BKH.Cells(hx, 3) = NCP And BKH.Cells(hx, 2) = DEV And BKH.Cells(hx, 1) = AGE Then
        
        If ax = 6 And DVA >= D0 Then
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
            EXL.Cells(ax + 1, 1) = hMoN
            EXL.Cells(ax + 1, 3) = EXL.Cells(ax, 3) + hMoN
            EXL.Cells(ax + 1, 2) = DVA
            
'            ElseIf DVA < D0 Then
'                EXL.Cells(ax + 1, 1).Select
'                Selection.EntireRow.Insert
'                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
'                With Selection.Borders(xlEdgeBottom)
'                    .LineStyle = xlContinuous
'                    End With
'            EXL.Cells(ax, 1) = hMoN
'            EXL.Cells(ax, 2) = DVA
            
            ElseIf DVA >= D0 And EXL.Cells(ax - 1, 2) < D0 Then
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
                EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
            EXL.Cells(ax + 1, 1) = hMoN
            EXL.Cells(ax + 1, 3) = EXL.Cells(ax, 3) + hMoN
            EXL.Cells(ax + 1, 2) = DVA
            
            ElseIf DVA >= D0 Then
            EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 1) = hMoN
            EXL.Cells(ax, 3) = EXL.Cells(ax - 1, 3) + hMoN
            EXL.Cells(ax, 2) = DVA
            
            End If
            
        End If
    
hx = hx + 1
Loop

ax = 6
Do While EXL.Cells(ax, 2) <> ""
    ax = ax + 1
    Loop
    
        If ax = 6 Then
        EXL.Cells(ax + 1, 1).Select
                Selection.EntireRow.Insert
                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
                With Selection.Borders(xlEdgeBottom)
                    .LineStyle = xlContinuous
                    End With
            EXL.Cells(ax, 2) = D0
            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
'        ElseIf ax <> 6 And EXL.Cells(ax - 1, 2) < D0 Then
'                Selection.EntireRow.Insert
'                Range(EXL.Cells(ax + 1, 1), EXL.Cells(ax + 1, 6)).Select
'                With Selection.Borders(xlEdgeBottom)
'                    .LineStyle = xlContinuous
'                    End With
'            EXL.Cells(ax, 2) = D0
'            EXL.Cells(ax, 3) = BKD.Cells(dx, 8)
        
            
            End If

'------------------------------------------------------------------------------------------------------------------------------------------
ax = 6
    
        'FINITION TABLEAU

        Do While EXL.Cells(ax, 2) <> ""
    
        'SOLDE

        
            'NB JOURS
            If EXL.Cells(ax + 1, 2) <> "" Then
                If Cells(ax, 2) < D0 Then
                    EXL.Cells(ax, 4) = 0
                    Else
                    EXL.Cells(ax, 4) = DateDiff("d", CDate(EXL.Cells(ax, 2)), CDate(EXL.Cells(ax + 1, 2)))
                    End If
                Else
                EXL.Cells(ax, 4) = DateDiff("d", CDate(EXL.Cells(ax, 2)), Sheets("MAIN").Cells(101, 1)) + 1
                End If
            
            'NBC ET NBD
            If EXL.Cells(ax, 2) >= D0 Then
                If EXL.Cells(ax, 3) * EXL.Cells(ax, 4) < 0 Then
                    EXL.Cells(ax, 5) = EXL.Cells(ax, 3) * EXL.Cells(ax, 4)
                    EXL.Cells(ax, 6) = 0
                    ElseIf EXL.Cells(ax, 3) * EXL.Cells(ax, 4) > 0 Then
                    EXL.Cells(ax, 6) = EXL.Cells(ax, 3) * EXL.Cells(ax, 4)
                    EXL.Cells(ax, 5) = 0
                    Else
                    EXL.Cells(ax, 5) = 0
                    EXL.Cells(ax, 6) = 0
                    End If
                End If
            
            'TAUX PLUS FORT DECOUVERT
                
EXL.Cells(2, 3).EntireRow.AutoFit
            
            'MODULO PAGE IMPRESSION
            
            If (ax - 6) Mod (Mde) = 0 Then
                modx = modx + 1
                End If
            EXL.Cells(ax, 7) = modx
    
        ax = ax + 1
        Loop
           
        modx = 1
        
        EXL.Cells(1, 3) = AGE & " - " & NCP & " - " & DEV
        EXL.Cells(2, 3) = BKD.Cells(dx, 9)
        EXL.Cells(3, 3) = "ARRETE DU " & CStr(D0) & " au " & CStr(D1)
        
        'AJOUT DERNIERE LIGNE
        EXL.Range("d:d").NumberFormat = "_(* #,##0_);_(* (#,##0);_(* ""-""_);_(@_)"
        '--------------------------------------------------------------------------------------------------------------------
        'RECAP
        
        px = 6
        
            Do Until EXL.Cells(px, 2) = ""
            px = px + 1
            Loop


            'AJOUT LIGNE RECAP
    
            EXL.Cells(px, 1).Select
            Selection.EntireRow.Insert
            EXL.Cells(px, 2) = CStr(D1)
            EXL.Cells(px, 3) = EXL.Cells(px - 1, 3)
            
            'RECAP NBR JOURS
            EXL.Cells(px, 4) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 4), EXL.Cells(px - 1, 4)))
               
            'RECAP NBR DEB
            EXL.Cells(px, 5) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 5), EXL.Cells(px - 1, 5)))
            
            'RECAP NBR CDT
            EXL.Cells(px, 6) = Application.WorksheetFunction.Sum(Range(EXL.Cells(6, 6), EXL.Cells(px - 1, 6)))
            
            EXL.Range(EXL.Cells(px, 1), EXL.Cells(px, 6)).Font.Bold = True
            
            Cells(px + 1, 1).EntireRow.Delete
            
            'CALCUL AGIO
            'Créditeur
            EXL.Cells(px + 2, 6) = (EXL.Cells(px, 6) * EXL.Cells(px + 2, 7)) / 360
            'Débiteur
            EXL.Cells(px + 3, 6) = (EXL.Cells(px, 5) * EXL.Cells(px + 3, 7)) / 360
        
            'NET AGIOS
            EXL.Cells(px + 5, 6) = EXL.Cells(px + 2, 6) + EXL.Cells(px + 3, 6)
            MoN = Round(EXL.Cells(px + 5, 6), 2)
                        
            'TAUX PLUS FORT DECOUVERT
            
            'ENREGISTREMENT NET AGIO DE L'ECHELLE DANS BKDAR
        BKD.Cells(dx, 30) = EXL.Cells(px, 6)
        BKD.Cells(dx, 31) = BKD.Cells(dx, 4)
        BKD.Cells(dx, 32) = -EXL.Cells(px, 5)
        BKD.Cells(dx, 33) = BKD.Cells(dx, 6)
        If BKD.Cells(dx, 30) <> BKD.Cells(dx, 31) Or BKD.Cells(dx, 32) <> BKD.Cells(dx, 33) Then
            NbX = WorksheetFunction.Round((BKD.Cells(dx, 30) - BKD.Cells(dx, 31) + BKD.Cells(dx, 32) - BKD.Cells(dx, 33)), 2)
            If NbX < 0 Then
                NbX = -NbX
                End If
            BKD.Cells(dx, 29) = "NB KO : " & NbX
            End If

            'IRCM
            EXL.Cells(px + 4, 6) = 0
            
            'devise
            
            x = 1
            Do While DATA.Cells(x, 24) <> DEV
                x = x + 1
                If x > 30 Then
                    MsgBox "Devise erronée " & DEV
                    End If
                Loop
            ADev = DATA.Cells(x, 25)

    


                
    'IMPRESSION ECHELLE PDF
    
    Sheets.Add After:=Sheets(Sheets.Count)
    Sheets(Sheets.Count).Name = "PDF." & AGE & "." & NCP
    Set PDF = Sheets(Sheets.Count)
    PDF.Select
    
    Range("b:b,c:c,d:d,e:e,g:g,h:h").Select
    With Selection
        .NumberFormat = "#,##0.00"
        End With
    Range("a:a").Select
    With Selection
        .NumberFormat = "@"
        End With
    Range("a:i").Select
    With Selection.Font
        .Name = "Courier new"
        .Size = 8
        End With
    
    Range("f:f").Select
    With Selection
        .ColumnWidth = 3
        .HorizontalAlignment = xlCenter
    End With
    
    Range("a:a").ColumnWidth = 8.5
    Range("b:b,c:c,d:d,e:e,g:g,h:h,i:i").ColumnWidth = 12.8
    Range("i:i").Select
        With Selection
            .HorizontalAlignment = xlCenter
            .NumberFormat = "0.0000000"
        End With

    Pejy = Application.WorksheetFunction.RoundUp((px / Mde) + 1, 0)
    
    PGN = 1
    pfx = 1
    pfy = 6
    cnf = 1
        
        Do While PGN <= Pejy

            'En-tête
            pfx = (PGN * (Mdp + 1)) - Mdp
            PDF.Cells(pfx, 1) = "BMOI"
                If PGN > 1 Then
                Set Form = Range(Cells(pfx, 1), Cells(pfx, 9))
                PDF.HPageBreaks.Add Before:=Form
                End If
            
            Set Form = Range(PDF.Cells(pfx, 7), PDF.Cells(pfx, 9))
                Call mercen(Form)
                Form.Value = "ARR-" & dx & "-" & ID
                
            Set Form = Range(PDF.Cells(pfx + 1, 3), PDF.Cells(pfx + 1, 7))
                Call mercen(Form)
                PDF.Cells(pfx + 1, 3) = "ECHELLE D'ARRETE - Du " & CStr(D0) & " au " & CStr(D1)
            
            
            PDF.Cells(pfx + 2, 1) = "Agence : " & AGE & " - " & AdA
            PDF.Cells(pfx + 3, 1) = "Compte : " & ADev & "-" & NCP & "-" & BKD.Cells(dx, 13)
            PDF.Cells(pfx + 3, 4) = BKD.Cells(dx, 9)
            PDF.Cells(pfx + 4, 4) = "COMPTE A VUE"
            'Début WJR modif NIF STAT
            Do While DATA.Cells(cnf, 60) <> ""
                If Trim(DATA.Cells(cnf, 60)) = Trim(NCL) Then
                    PDF.Cells(pfx + 5, 4) = "NIF : " & DATA.Cells(cnf, 61) & " Stat : " & DATA.Cells(cnf, 62) & "  Rc : " & DATA.Cells(cnf, 63)
                End If
                cnf = cnf + 1
                Loop
            'Fin WJR modif NIF STAT
            PDF.Cells(pfx + 5, 1) = "Date  : " & CStr(AnteD1)
            PDF.Cells(pfx + 5, 9) = "Page " & PGN & "/" & Pejy
            
            Set Form = Range(Cells(pfx, 1), Cells(pfx + 5, 9))
                Call bmed(Form)
            
            If PGN < Pejy Then
                PDF.Cells(pfx + 6, 1) = "Valeur"
                PDF.Cells(pfx + 6, 1).HorizontalAlignment = xlCenter
                PDF.Cells(pfx + 7, 1) = "JJ/MM/AAAA"
                
                Set Form = Range(Cells(pfx + (Mdp - 1), 1), Cells(pfx + Mdp, 1))
                    Call mercen(Form)
                    Form.Value = "Report"
                Set Form = Range(Cells(pfx + 6, 2), Cells(pfx + 6, 3))
                    Call mercen(Form)
                    Form.Value = "Capitaux"
                    Cells(pfx + 7, 2) = "Débit"
                    Cells(pfx + 7, 3) = "Crédit"
                Set Form = Range(Cells(pfx + 6, 4), Cells(pfx + 6, 5))
                    Call mercen(Form)
                    Form.Value = "Soldes"
                    Cells(pfx + 7, 4) = "Débit"
                    Cells(pfx + 7, 5) = "Crédit"
                Set Form = Range(Cells(pfx + 6, 6), Cells(pfx + 7, 6))
                    Call mercen(Form)
                    Form.Value = "NBJ"
                Set Form = Range(Cells(pfx + 6, 7), Cells(pfx + 6, 8))
                    Call mercen(Form)
                    Form.Value = "Nombres"
                    Cells(pfx + 7, 7) = "Débit"
                    Cells(pfx + 7, 8) = "Crédit"
                Set Form = Range(Cells(pfx + 6, 9), Cells(pfx + 7, 9))
                    Call mercen(Form)
                    Form.Value = "Taux / + Fort découvert"
                    Form.WrapText = True
                Set Form = Range(Cells(pfx + 7, 1), Cells(pfx + 7, 9))
                    Form.HorizontalAlignment = xlCenter
                    Call bmix(Form)
                Set Form = Range(Cells(pfx + 8, 1), Cells(pfx + (Mdp - 2), 1))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + 8, 2), Cells(pfx + (Mdp - 2), 3))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + 8, 4), Cells(pfx + (Mdp - 2), 5))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + 8, 6), Cells(pfx + (Mdp - 2), 6))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + 8, 7), Cells(pfx + (Mdp - 2), 8))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + 8, 9), Cells(pfx + (Mdp - 2), 9))
                    Call bmed(Form)
                Set Form = Range(Cells(pfx + (Mdp - 1), 1), Cells(pfx + Mdp, 9))
                    Call bCol(Form)
                
            
                
                
                End If
                         
                
            'données
            
            Do While EXL.Cells(pfy, 7) - 1 = PGN
            
            If EXL.Cells(pfy + 1, 2) = "" Then
                Exit Do
                End If
            
                    PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 1) = CStr(EXL.Cells(pfy, 2))
                    
                    If EXL.Cells(pfy, 1) < 0 Then
                        PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 2) = -EXL.Cells(pfy, 1)
                        Else: PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 3) = EXL.Cells(pfy, 1)
                        End If
                        
                    If EXL.Cells(pfy, 4) > 0 Then
                        PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 6) = EXL.Cells(pfy, 4)
                        If EXL.Cells(pfy, 3) < 0 Then
                            PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 4) = -EXL.Cells(pfy, 3)
                            Else: PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 5) = EXL.Cells(pfy, 3)
                            End If
                        If EXL.Cells(pfy, 5) <> 0 Then
                            PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 7) = -EXL.Cells(pfy, 5)
                            PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 9) = BKD.Cells(dx, 7)
                            ElseIf EXL.Cells(pfy, 6) <> 0 Then
                            PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 8) = EXL.Cells(pfy, 6)
                            PDF.Cells(pfx + 2 + pfy - (Mde * (PGN - 1)), 9) = BKD.Cells(dx, 5)
                            End If
                        End If
                        
            'reportvalues
            
            If PGN = 1 Then
                Cells(pfx + (Mdp - 1), 2) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 2), Cells(pfx + (Mdp - 2), 2)))
                Cells(pfx + Mdp, 3) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 3), Cells(pfx + (Mdp - 2), 3)))
                Cells(pfx + (Mdp - 1), 4) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 4), Cells(pfx + (Mdp - 2), 4)))
                Cells(pfx + Mdp, 5) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 5), Cells(pfx + (Mdp - 2), 5)))
                Cells(pfx + (Mdp - 1), 6) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 6), Cells(pfx + (Mdp - 2), 6)))
                Cells(pfx + (Mdp - 1), 7) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 7), Cells(pfx + (Mdp - 2), 7)))
                Cells(pfx + Mdp, 8) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 8), Cells(pfx + (Mdp - 2), 8)))
                ElseIf PGN < Pejy Then
                Cells(pfx + (Mdp - 1), 2) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 2), Cells(pfx + (Mdp - 2), 2))) + Cells(pfx - 2, 2)
                Cells(pfx + Mdp, 3) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 3), Cells(pfx + (Mdp - 2), 3))) + Cells(pfx - 1, 3)
                Cells(pfx + (Mdp - 1), 4) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 4), Cells(pfx + (Mdp - 2), 4))) + Cells(pfx - 2, 4)
                Cells(pfx + Mdp, 5) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 5), Cells(pfx + (Mdp - 2), 5))) + Cells(pfx - 1, 5)
                Cells(pfx + (Mdp - 1), 6) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 6), Cells(pfx + (Mdp - 2), 6))) + Cells(pfx - 2, 6)
                Cells(pfx + (Mdp - 1), 7) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 7), Cells(pfx + (Mdp - 2), 7))) + Cells(pfx - 2, 7)
                Cells(pfx + Mdp, 8) = Application.WorksheetFunction.Sum(Range(Cells(pfx + 8, 8), Cells(pfx + (Mdp - 2), 8))) + Cells(pfx - 1, 8)
                End If
                    
            pfy = pfy + 1
            Loop
            
            'dernière page
            If PGN = Pejy Then
                PDF.Cells(pfx + 7, 1) = "RETOUR"
                PDF.Cells(pfx + 7, 7).Select
                    With Selection
                        .NumberFormat = "#,##0.00"
                        .Value = EXL.Cells(px + 6, 5)
                    End With
                    
                PDF.Cells(pfx + 9, 1) = "INTERETS DEBITEURS"
                PDF.Cells(pfx + 9, 4) = PDF.Cells(pfx - 2, 7)
                PDF.Cells(pfx + 10, 1) = "INTERETS CREDITEURS"
                PDF.Cells(pfx + 10, 4) = PDF.Cells(pfx - 1, 8)
                Range(Cells(pfx + 9, 5), Cells(pfx + 12, 5)).Select
                    With Selection
                        .NumberFormat = "0.0000000"
                    End With
                PDF.Cells(pfx + 9, 5) = BKD.Cells(dx, 7)
                PDF.Cells(pfx + 9, 7) = Abs(EXL.Cells(px + 3, 6))
                PDF.Cells(pfx + 10, 5) = BKD.Cells(dx, 5)
                PDF.Cells(pfx + 10, 7) = Abs(EXL.Cells(px + 2, 6))
                
                PDF.Cells(pfx + 12, 1) = "COMMISSION/PLUS FORT DECOUVERT"
                
                If (WorksheetFunction.Min(Range(EXL.Cells(6, 5), EXL.Cells(px - 1, 5)))) <> 0 Then
                    PDF.Cells(pfx + 12, 4) = -WorksheetFunction.Min(Range(EXL.Cells(6, 5), EXL.Cells(px - 1, 5)))
                    End If
                PDF.Cells(pfx + 12, 5) = 0
                PDF.Cells(pfx + 12, 6) = 0
                
                
                PDF.Cells(pfx + 15, 1) = "NET A DEBITER"
                PDF.Cells(pfx + 15, 7) = Abs(EXL.Cells(px + 5, 6))
                        
        Set Form = Range(Cells(pfx + 6, 1), Cells(pfx + 17, 9))
            Call bmed(Form)
            
                        
                End If
            
            
        PGN = PGN + 1
        Loop

'sauvegarde pdf

PDF.Select

    Set fs = CreateObject("scripting.filesystemobject")
    adrF = ThisWorkbook.Path & "\" & "ECHELLE ARRETES PDF" & "\"
    nomF = PDF.Name & "." & Mid((BKD.Cells(dx, 17)), 1, 8)
    If Not fs.folderexists(adrF) Then
        fs.createfolder adrF
        End If
    
    With PDF.PageSetup
        .LeftMargin = Application.InchesToPoints(0.5)
        .RightMargin = Application.InchesToPoints(0.5)
        .TopMargin = Application.InchesToPoints(1.6)
        .BottomMargin = Application.InchesToPoints(0.5)
        .HeaderMargin = Application.InchesToPoints(0.3)
        .FooterMargin = Application.InchesToPoints(0.3)
        .LeftHeaderPicture.Filename = ThisWorkbook.Path & "\logoup.png"
        .LeftHeaderPicture.Width = 241
        .LeftHeaderPicture.Height = 100
        .LeftHeader = "&G"
        .CenterFooterPicture.Filename = ThisWorkbook.Path & "\logodwn.png"
        .CenterFooterPicture.Width = 470
        .CenterFooterPicture.Height = 51
        .CenterFooter = "&G"
        .PrintHeadings = False
        .PrintGridlines = False
        .PrintComments = xlPrintNoComments
        .CenterHorizontally = True
        .CenterVertically = False
        .Orientation = xlPortrait
        .Draft = False
        .PaperSize = xlPaperA4
        .FirstPageNumber = xlAutomatic
        .Order = xlDownThenOver
        .BlackAndWhite = False
        .Zoom = False
        .FitToPagesWide = 1
        .FitToPagesTall = False
    End With
    PDF.ExportAsFixedFormat Type:=xlTypePDF, Filename:=adrF & nomF & ".pdf"
    
adrF = ""
nomF = ""

'génération avis d'agios

cnf = 1

Sheets.Add After:=Sheets(Sheets.Count)
Sheets(Sheets.Count).Name = "AVIS." & AGE & "." & NCP
Set AGIO = Sheets(Sheets.Count)

    AGIO.Select
    Set Form = Range(Cells(1, 1), Cells(40, 7))
        Form.Select
            With Selection.Font
            .Name = "Courier new"
            .Size = 8
            End With
    
    Cells(1, 1).EntireColumn.ColumnWidth = 14.78
    Cells(1, 3).EntireColumn.ColumnWidth = 14
    Cells(1, 4).EntireColumn.ColumnWidth = 16.78
    Cells(1, 7).EntireColumn.ColumnWidth = 1.78
    Cells(1, 8).EntireColumn.ColumnWidth = 1.78
    
    Set Form = Range(Cells(3, 3), Cells(3, 5))
        Call mercen(Form)
        Form.Value = "AVIS D'AGIOS"
        Form.HorizontalAlignment = xlLeft
        Selection.InsertIndent 5
        
    'Cells(5, 2) = NCP & "-" & BKD.Cells(dx, 13)
    
    Set Form = Range(Cells(5, 4), Cells(5, 5))
        Call mercen(Form)
        Form.HorizontalAlignment = xlRight
    Form.NumberFormat = "DD MMM YYYY"
    Form.Value = CDate(D1 + 1)
    
        x = 1
        Do While DATA.Cells(x, 24) <> DEV
            x = x + 1
            Loop

    'Début WJR modif NIF STAT
    Cells(5, 2) = NCP & "-" & BKD.Cells(dx, 13) & " " & DATA.Cells(x, 25)
    Cells(6, 1) = "       Agence    : " & AGE & " - " & AdA
    Do While DATA.Cells(cnf, 60) <> ""
        If Trim(DATA.Cells(cnf, 60)) = Trim(NCL) Then
            Cells(7, 1) = "       NIF       : " & DATA.Cells(cnf, 61)
            Cells(8, 1) = "       Stat      : " & DATA.Cells(cnf, 62)
        End If
        cnf = cnf + 1
        Loop
    'Fin WJR modif NIF STAT
    'Cells(7, 1) = "       Agence    : " & AGE & " - " & AdA
    'Cells(8, 1) = "       Devise    : " & DATA.Cells(x, 25)

        x = 1
        
Set Form = Range(Cells(7, 4), Cells(8, 5))
Form.Select
    With Selection
        .MergeCells = True
        .WrapText = True
        .HorizontalAlignment = xlLeft
        .VerticalAlignment = xlVAlignBottom
        .Value = BKD.Cells(dx, 9)
    End With

Set Form = Range(Cells(9, 4), Cells(11, 5))
Form.Select
    With Selection
        .MergeCells = True
        .WrapText = True
        .HorizontalAlignment = xlLeft
        .VerticalAlignment = xlVAlignTop
        .Value = BKD.Cells(dx, 10) & " " & BKD.Cells(dx, 11) & "-" & BKD.Cells(dx, 16) & " " & BKD.Cells(dx, 12)
    End With
        
    
    Set Form = Range(Cells(9, 1), Cells(9, 3))
       Call mercen(Form)
       Form.Value = "PERIODE D'ARRETE"
       Set Form = Range(Cells(10, 1), Cells(10, 3))
       Call mercen(Form)
       Form.Value = "Du " & CStr(D0)
       Set Form = Range(Cells(11, 1), Cells(11, 3))
       Call mercen(Form)
       Form.Value = "Au " & CStr(D1)
    
    Cells(17, 2) = "Nous vous remettons ci-joint, l'arrêté en intérêts de votre compte:"
    
    Set Form = Range(Cells(19, 4), Cells(19, 6))
        Call mercen(Form)
        Form.Value = "INTERETS DEBITEURS"
        
    Cells(20, 2) = "RETOUR HORS TAXES"
    Cells(20, 4) = "NOMBRES"
        Cells(21, 4) = EXL.Cells(px, 5)
    Cells(20, 5) = "TAUX"
        Cells(21, 5).NumberFormat = "0.0000000"
        Cells(21, 5) = BKD.Cells(dx, 7)
    Cells(20, 6) = "INTERETS"
        Cells(21, 6) = Abs(EXL.Cells(px + 3, 6))
    
    Set Form = Range(Cells(21, 3), Cells(21, 6))
        Form.HorizontalAlignment = xlLeft
        
    Range(Cells(21, 3), Cells(21, 4)).NumberFormat = "#,##0.00"
    Cells(21, 6).NumberFormat = "#,##0.00"
        
    Cells(21, 3) = -EXL.Cells(px + 6, 5)
    
    Cells(22, 2) = "RETOUR TAXES"
    
    Cells(24, 2) = "COMM. + FORT DECOUVERT"
    
    
    Set Form = Range(Cells(24, 4), Cells(24, 6))
        Call mercen(Form)
        Form.Value = "INTERETS CREDITEURS"
    
    Cells(25, 4) = "NOMBRES"
        Cells(26, 4) = EXL.Cells(px, 6)
    Cells(25, 5) = "TAUX"
        Cells(26, 5).NumberFormat = "0.0000000"
        Cells(26, 5) = BKD.Cells(dx, 5)
    Cells(25, 6) = "INTERETS"
        Cells(26, 6) = EXL.Cells(px + 2, 6)
    
    Set Form = Range(Cells(26, 3), Cells(26, 6))
        Form.HorizontalAlignment = xlLeft
        
    Range(Cells(26, 3), Cells(26, 4)).NumberFormat = "#,##0.00"
    Cells(26, 6).NumberFormat = "#,##0.00"

    
    Cells(26, 2) = "COMMISSION DE MOUVEMENT"
    Cells(27, 3) = "0,00"
    Cells(28, 2) = "FRAIS FIXES"
    Cells(29, 3) = "0,00"
    Cells(30, 2) = "TAXES"
    Cells(31, 3) = "0,00"
    
    Cells(32, 4) = "NET AGIOS DEBIT"
    Cells(33, 4).Select
        With Selection
        .HorizontalAlignment = xlCenter
        .NumberFormat = "0.00"
        .Value = Cells(21, 6)
        End With
    Set Form = Range(Cells(32, 5), Cells(32, 6))
        Call mercen(Form)
        Form.Value = "NET AGIOS CREDIT"
    Set Form = Range(Cells(33, 5), Cells(33, 6))
        Call mercen(Form)
        Form.NumberFormat = "0.00"
        Form.Value = Cells(26, 6)

Cells(6, 1).EntireRow.Insert 2
Range(Cells(13, 1), Cells(15, 1)).EntireRow.Delete

AGIO.Select

    Set fs = CreateObject("scripting.filesystemobject")
    
    If BKD.Cells(dx, 15) = "1" Then
        adrF = ThisWorkbook.Path & "\" & "AVIS AGIOS PARTICULIER PDF" & "\"
        Else
        adrF = ThisWorkbook.Path & "\" & "AVIS AGIOS PDF" & "\"
        End If
    nomF = AGIO.Name & "." & Mid((BKD.Cells(dx, 17)), 1, 8)

    If Not fs.folderexists(adrF) Then
        fs.createfolder adrF
        End If
    
    With AGIO.PageSetup
        .LeftMargin = Application.InchesToPoints(0.3)
        .RightMargin = Application.InchesToPoints(0)
        .TopMargin = Application.InchesToPoints(0)
        .BottomMargin = Application.InchesToPoints(0)
        .LeftHeaderPicture.Filename = ThisWorkbook.Path & "\logoup.png"
        .LeftHeaderPicture.Width = 175
        .LeftHeaderPicture.Height = 73
        .LeftHeader = "&G"
        .FooterMargin = 50
        .CenterFooterPicture.Filename = ThisWorkbook.Path & "\logodwn.png"
        .CenterFooterPicture.Width = 381
        .CenterFooterPicture.Height = 44
        .CenterFooterPicture.CropLeft = -20
        .CenterFooter = "&G"
        .Zoom = False
        .FitToPagesWide = 1
        .FitToPagesTall = 1
        .CenterHorizontally = True
    End With
    AGIO.ExportAsFixedFormat Type:=xlTypePDF, Filename:=adrF & nomF & ".pdf"
    

EXL.Delete
PDF.Delete
AGIO.Delete

dx = dx + 1
Loop
 
Unload ufprogress

Sheets("MAIN").Range("g14") = "dd/mm/yyyy HH:MM"
Sheets("MAIN").Range("g14") = CDate(Now)
ThisWorkbook.Save

msb = MsgBox("Fin de la génération des fichiers d'arrêté.", vbInformation, "Outil de traitement d'arrêté")
Sheets("main").Select

End Sub

'Mise à jour Liste NIF STAT
Sub GetStat()

Application.ScreenUpdating = False
On Error GoTo CodeErreur

Dim IndexFichier As Integer
Dim MonFichier As String
Dim ContenuLigne As String
Dim row_number As Integer
Set DATA = Sheets("DATA")

row_number = 1
DATA.Columns(60).ClearContents
DATA.Columns(61).ClearContents
DATA.Columns(62).ClearContents
DATA.Columns(63).ClearContents

MonFichier = ThisWorkbook.Path & "\get_stat.txt"
IndexFichier = FreeFile()
Open MonFichier For Input As #IndexFichier

While Not EOF(IndexFichier)
    Line Input #IndexFichier, ContenuLigne
    DATA.Cells(row_number, 60) = "'" & Trim(Mid(ContenuLigne, 1, 8))
    DATA.Cells(row_number, 61) = "'" & Trim(Mid(ContenuLigne, 11, 10))
    DATA.Cells(row_number, 62) = "'" & Trim(Mid(ContenuLigne, 23, 17))
    DATA.Cells(row_number, 63) = "'" & Trim(Mid(ContenuLigne, 41, 10))
    row_number = row_number + 1
Wend

Close #IndexFichier
Application.ScreenUpdating = True
Exit Sub

CodeErreur:
MsgBox "Une erreur s'est produite"
Application.ScreenUpdating = True

End Sub
