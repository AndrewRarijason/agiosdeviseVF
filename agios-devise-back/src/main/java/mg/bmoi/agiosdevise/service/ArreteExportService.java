package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.DTO.TransactionDto;
import mg.bmoi.agiosdevise.DTO.SyntheseInteretsDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArreteExportService {

    public byte[] exportArreteComptes(Map<String, Map<String, Object>> arreteComptes) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Police uniforme
        Font aptosFont = workbook.createFont();
        aptosFont.setFontName("Aptos Narrow");
        aptosFont.setFontHeightInPoints((short) 11);

        // Style blanc pour toutes les cellules
        CellStyle whiteCellStyle = workbook.createCellStyle();
        whiteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        whiteCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        whiteCellStyle.setFont(aptosFont);
        whiteCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        // Style police rouge pour mouvements négatifs
        Font redFont = workbook.createFont();
        redFont.setFontName("Aptos Narrow");
        redFont.setFontHeightInPoints((short) 11);
        redFont.setColor(IndexedColors.RED.getIndex());
        CellStyle redFontStyle = workbook.createCellStyle();
        redFontStyle.cloneStyleFrom(whiteCellStyle);
        redFontStyle.setFont(redFont);

        // Style police rouge avec bordure inférieure
        CellStyle redFontBorderStyle = workbook.createCellStyle();
        redFontBorderStyle.cloneStyleFrom(redFontStyle);
        redFontBorderStyle.setBorderTop(BorderStyle.THIN);
        redFontBorderStyle.setBorderBottom(BorderStyle.THIN);

        // Style gras avec bordure supérieure et inférieure
        Font boldFont = workbook.createFont();
        boldFont.setFontName("Aptos Narrow");
        boldFont.setFontHeightInPoints((short) 11);
        boldFont.setBold(true);
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.cloneStyleFrom(whiteCellStyle);
        boldStyle.setFont(boldFont);
        boldStyle.cloneStyleFrom(boldStyle);
        boldStyle.setBorderTop(BorderStyle.THIN);
        boldStyle.setBorderBottom(BorderStyle.THIN);


        // Style en-tête violet, police blanche
        Font headerFont = workbook.createFont();
        headerFont.setFontName("Aptos Narrow");
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.RIGHT);

        // Style synthèse avec bordure horizontale
        CellStyle synthBorderStyle = workbook.createCellStyle();
        synthBorderStyle.cloneStyleFrom(whiteCellStyle);
        synthBorderStyle.setBorderTop(BorderStyle.THIN);

        // Style avec bordure horizontale inférieure pour les transactions
        CellStyle transactionBorderStyle = workbook.createCellStyle();
        transactionBorderStyle.cloneStyleFrom(whiteCellStyle);
        transactionBorderStyle.setBorderBottom(BorderStyle.THIN);
        transactionBorderStyle.setBorderTop(BorderStyle.THIN);

        // Style synthèse avec bordure supérieure épaisse violette
        CellStyle synthThickTopVioletStyle = workbook.createCellStyle();
        synthThickTopVioletStyle.cloneStyleFrom(synthBorderStyle);
        synthThickTopVioletStyle.setBorderTop(BorderStyle.THICK);
        synthThickTopVioletStyle.setTopBorderColor(IndexedColors.VIOLET.getIndex());

        // Style synthèse avec bordure inférieure épaisse violette
        CellStyle synthThickBottomVioletStyle = workbook.createCellStyle();
        synthThickBottomVioletStyle.cloneStyleFrom(boldStyle);
        synthThickBottomVioletStyle.setBorderBottom(BorderStyle.THICK);
        synthThickBottomVioletStyle.setBottomBorderColor(IndexedColors.VIOLET.getIndex());

        // Style pour les informations d'en-tête
        CellStyle headerInfoStyle = workbook.createCellStyle();
        headerInfoStyle.cloneStyleFrom(whiteCellStyle);
        headerInfoStyle.setFont(boldFont);
        headerInfoStyle.setAlignment(HorizontalAlignment.LEFT);
        headerInfoStyle.setAlignment(HorizontalAlignment.CENTER);
        headerInfoStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Création du format texte
        DataFormat format = workbook.createDataFormat();
        CellStyle synthThickTopVioletTextStyle = workbook.createCellStyle();
        synthThickTopVioletTextStyle.cloneStyleFrom(synthThickTopVioletStyle);
        synthThickTopVioletTextStyle.setDataFormat(format.getFormat("@"));

        CellStyle synthThickBottomVioletTextStyle = workbook.createCellStyle();
        synthThickBottomVioletTextStyle.cloneStyleFrom(synthThickBottomVioletStyle);
        synthThickBottomVioletTextStyle.setDataFormat(format.getFormat("@"));

        CellStyle synthBorderTextStyle = workbook.createCellStyle();
        synthBorderTextStyle.cloneStyleFrom(synthBorderStyle);
        synthBorderTextStyle.setDataFormat(format.getFormat("@"));

        String ncp = arreteComptes.keySet().iterator().next();
        List<Map<String, Object>> transactionsMap = (List<Map<String, Object>>) arreteComptes.get(ncp).get("transactions");
        SyntheseInteretsDto synthese = (SyntheseInteretsDto) arreteComptes.get(ncp).get("synthese");

        List<TransactionDto> transactions = transactionsMap.stream()
                .map(map -> new TransactionDto(
                        (String) map.get("ncp"),
                        (map.get("mon") instanceof BigDecimal) ? (BigDecimal) map.get("mon") : (map.get("mon") != null ? new BigDecimal(map.get("mon").toString()) : null),
                        (Date) map.get("dva"),
                        (map.get("sen") instanceof Character) ? (Character) map.get("sen") : (map.get("sen") != null ? map.get("sen").toString().charAt(0) : null),
                        (map.get("soldeDepart") instanceof BigDecimal) ? (BigDecimal) map.get("soldeDepart") : (map.get("soldeDepart") != null ? new BigDecimal(map.get("soldeDepart").toString()) : null),
                        map.get("nbJoursInactif") != null ? ((Number) map.get("nbJoursInactif")).intValue() : 0,
                        map.containsKey("nbDebiteur") ?
                                ((map.get("nbDebiteur") instanceof BigDecimal) ? (BigDecimal) map.get("nbDebiteur") : (map.get("nbDebiteur") != null ? new BigDecimal(map.get("nbDebiteur").toString()) : null)) :
                                ((map.get("totalNbDebiteur") instanceof BigDecimal) ? (BigDecimal) map.get("totalNbDebiteur") : (map.get("totalNbDebiteur") != null ? new BigDecimal(map.get("totalNbDebiteur").toString()) : null)),
                        map.containsKey("nbCrediteur") ?
                                ((map.get("nbCrediteur") instanceof BigDecimal) ? (BigDecimal) map.get("nbCrediteur") : (map.get("nbCrediteur") != null ? new BigDecimal(map.get("nbCrediteur").toString()) : null)) :
                                ((map.get("totalNbCrediteur") instanceof BigDecimal) ? (BigDecimal) map.get("totalNbCrediteur") : (map.get("totalNbCrediteur") != null ? new BigDecimal(map.get("totalNbCrediteur").toString()) : null))
                ))
                .collect(Collectors.toList());

        Sheet sheet = workbook.createSheet(ncp);

        // Initialiser toutes les cellules en blanc (A1 à BA500)
        for (int rowIdx = 0; rowIdx < 5000; rowIdx++) {
            Row row = sheet.createRow(rowIdx);
            for (int colIdx = 0; colIdx < 52; colIdx++) {
                Cell cell = row.createCell(colIdx);
                cell.setCellStyle(whiteCellStyle);
            }
        }

        // Ajouter les 3 lignes d'en-tête
        Row agenceRow = sheet.getRow(0);
        Row nomRow = sheet.getRow(1);
        Row dateRow = sheet.getRow(2);

        // Récupérer les informations du compte
        String age = (String) arreteComptes.get(ncp).get("age");
        String dev = (String) arreteComptes.get(ncp).get("dev");
        String nomrest = (String) arreteComptes.get(ncp).get("nomrest");
        Date dateDebutArrete = (Date) arreteComptes.get(ncp).get("dateDebutArrete");
        Date dateFinArrete = (Date) arreteComptes.get(ncp).get("dateFinArrete");

        // 1ère ligne: Agence, Compte, Devise (fusionnées)
        Cell agenceCell = agenceRow.createCell(2);
        agenceCell.setCellValue((age != null ? age : "") + " - " + ncp + " - " + (dev != null ? dev : ""));
        agenceCell.setCellStyle(headerInfoStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));

        // 2ème ligne: Nom du client
        Cell nomCell = nomRow.createCell(2);
        nomCell.setCellValue(nomrest != null ? nomrest : "");
        nomCell.setCellStyle(headerInfoStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 4));

        // 3ème ligne: Période d'arrêté
        Cell dateArreteCell = dateRow.createCell(2);
        dateArreteCell.setCellValue("ARRETE DU " + sdf.format(dateDebutArrete) + " AU " + sdf.format(dateFinArrete));
        dateArreteCell.setCellStyle(headerInfoStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));

        // En-tête des colonnes (ligne 4)
        Row header = sheet.getRow(4);
        String[] columns = {"Mouvements", "Date", "Solde", "Nb jour", "Nb débiteur", "Nb créditeur"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.getCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Transactions (commencent à la ligne 5)
        int rowIdx = 5;
        for (int i = 0; i < transactions.size(); i++) {
            TransactionDto tx = transactions.get(i);
            Row row = sheet.getRow(rowIdx++);

            // Appliquer la bordure à partir de la 2ème transaction
            CellStyle rowStyle = (i > 0) ? transactionBorderStyle : whiteCellStyle;

            // Mouvement
            Cell mouvCell = row.getCell(0);
            if (tx.getMon() != null) {
                if (tx.getMon().doubleValue() < 0) {
                    mouvCell.setCellValue(tx.getMon().doubleValue());
                    mouvCell.setCellStyle(i > 0 ? redFontBorderStyle : redFontStyle);
                } else {
                    mouvCell.setCellValue(tx.getMon().doubleValue());
                    mouvCell.setCellStyle(i > 0 ? transactionBorderStyle : whiteCellStyle);
                }
            } else {
                mouvCell.setCellValue("");
                mouvCell.setCellStyle(i > 0 ? transactionBorderStyle : whiteCellStyle);
            }

            // Date
            Cell dateCell = row.getCell(1);
            dateCell.setCellValue(tx.getDva() != null ? sdf.format(tx.getDva()) : "");
            dateCell.setCellStyle(rowStyle);

            // Solde
            Cell soldeCell = row.getCell(2);
            if (tx.getSoldeDepart() != null) {
                soldeCell.setCellValue(tx.getSoldeDepart().doubleValue());
            } else {
                soldeCell.setCellValue("");
            }
            soldeCell.setCellStyle(rowStyle);

            // Nb jour
            Cell nbJourCell = row.getCell(3);
            if (tx.getNbJoursInactif() == 0) {
                nbJourCell.setCellValue("-");
            } else {
                nbJourCell.setCellValue(tx.getNbJoursInactif());
            }
            nbJourCell.setCellStyle(rowStyle);

            // Nb débiteur
            Cell debCell = row.getCell(4);
            if (tx.getNbDebiteur() != null) {
                debCell.setCellValue(tx.getNbDebiteur().doubleValue());
            } else {
                debCell.setCellValue("");
            }
            debCell.setCellStyle(rowStyle);

            // Nb créditeur
            Cell credCell = row.getCell(5);
            if (tx.getNbCrediteur() != null) {
                credCell.setCellValue(tx.getNbCrediteur().doubleValue());
            } else {
                credCell.setCellValue("");
            }
            credCell.setCellStyle(rowStyle);

            // Dernière ligne en gras
            if (rowIdx == transactions.size() + 5) {
                for (int j = 0; j < 6; j++) {
                    row.getCell(j).setCellStyle(boldStyle);
                }
            }
        }

        // Synthèse
        // Conversion des valeurs numériques au format texte avec virgule
        String interetCrediteur = "0";
        String interetDebiteur = "0";
        String netAgios = "0";
        String tauxCrediteur = "0%";
        String tauxDebiteur = "0%";
        String ircm = "";
        if (synthese != null) {
            interetCrediteur = synthese.getInteretCrediteur() != null
                    ? synthese.getInteretCrediteur().toPlainString().replace('.', ',') : "0";
            interetDebiteur = synthese.getInteretDebiteur() != null
                    ? synthese.getInteretDebiteur().toPlainString().replace('.', ',') : "0";
            netAgios = synthese.getNetAgios() != null
                    ? synthese.getNetAgios().toPlainString().replace('.', ',') : "0";
            tauxCrediteur = synthese.getTauxCrediteur() != null
                    ? synthese.getTauxCrediteur().toPlainString().replace('.', ',') + "%" : "0%";
            tauxDebiteur = synthese.getTauxDebiteur() != null
                    ? synthese.getTauxDebiteur().toPlainString().replace('.', ',') + "%" : "0%";
            ircm = synthese.getIrcm() != null ? synthese.getIrcm() : "";
        }

        int synthStart = rowIdx + 1;
        String[][] synthRows = {
                {"Int créditeur", interetCrediteur, tauxCrediteur},
                {"Int débiteur", interetDebiteur, tauxDebiteur},
                {"IRCM", ircm, ""},
                {"Net agios", netAgios, ""}
        };

        for (int i = 0; i < synthRows.length; i++) {
            Row synthRow = sheet.getRow(synthStart + i);
            if (synthRow == null) {
                synthRow = sheet.createRow(synthStart + i);
            }
            for (int j = 0; j < 3; j++) {
                Cell cell = synthRow.getCell(4 + j);
                if (cell == null) {
                    cell = synthRow.createCell(4 + j);
                }
                cell.setCellValue(synthRows[i][j]);
                if (i == 0) {
                    cell.setCellStyle(synthThickTopVioletTextStyle);
                } else if (i == 3) {
                    cell.setCellStyle(synthThickBottomVioletTextStyle);
                } else {
                    cell.setCellStyle(synthBorderTextStyle);
                }
            }
        }

        // Solde NOK
        if ("Solde NOK".equals(arreteComptes.get(ncp).get("resultat"))) {
            Row row = sheet.getRow(synthStart + synthRows.length);
            if (row == null) {
                row = sheet.createRow(synthStart + synthRows.length);
                for (int colIdx = 0; colIdx < 52; colIdx++) {
                    row.createCell(colIdx).setCellStyle(whiteCellStyle);
                }
            }
            Cell nokCell2 = row.getCell(2);
            if (nokCell2 == null) nokCell2 = row.createCell(2);
            nokCell2.setCellValue("Solde NOK");
            Cell nokCell4 = row.getCell(4);
            if (nokCell4 == null) nokCell4 = row.createCell(4);
            nokCell4.setCellValue("CALCUL = " + arreteComptes.get(ncp).get("soldeFinalCalcule"));
            Cell nokCell5 = row.getCell(5);
            if (nokCell5 == null) nokCell5 = row.createCell(5);
            nokCell5.setCellValue("BKSLD = " + arreteComptes.get(ncp).get("soldeFinalBase"));
        }

        // Largeur des colonnes
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, Math.max(currentWidth, 3200));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }
}