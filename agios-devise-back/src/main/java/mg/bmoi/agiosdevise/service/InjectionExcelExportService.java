package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.entity.HistoriqueArrete;
import mg.bmoi.agiosdevise.repository.HistoriqueArreteRepository;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Service
public class InjectionExcelExportService {

    private final JdbcTemplate jdbcTemplate;
    private final HistoriqueArreteRepository historiqueArreteRepository;

    public InjectionExcelExportService(HistoriqueArreteRepository historiqueArreteRepository, JdbcTemplate jdbcTemplate) {
        this.historiqueArreteRepository = historiqueArreteRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Méthode pour récupérer TIND
    public BigDecimal getTindFromRemote(String dev, Date dco) {
        String sql = "SELECT TIND FROM C##MGSTGADR.BKTAU WHERE dev = ? AND dco = ?";
        List<BigDecimal> result = jdbcTemplate.query(
                sql,
                new Object[]{dev, new java.sql.Date(dco.getTime())},
                (rs, rowNum) -> rs.getBigDecimal("TIND")
        );
        return result.isEmpty() ? BigDecimal.ZERO : result.get(0);
    }

    // Charge les données CLC depuis le fichier data-injection.xlsx
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // Conversion en chaîne sans décimales
            double val = cell.getNumericCellValue();
            if (val == (long) val) {
                return String.valueOf((long) val);
            } else {
                return String.valueOf(val);
            }
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        } else {
            return cell.toString();
        }
    }

    private Map<String, Map<String, String>> loadClcData() throws Exception {
        Map<String, Map<String, String>> clcMap = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("templates/data-injection.xlsx");
        try (Workbook workbook = WorkbookFactory.create(resource.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // ignore l'entête
                String age = getCellStringValue(row.getCell(0)); // AGE en colonne 0
                String ncp = getCellStringValue(row.getCell(3)); // NCP en colonne 3
                String clc = getCellStringValue(row.getCell(4)); // CLC en colonne 4
                clcMap.computeIfAbsent(ncp, k -> new HashMap<>()).put(age, clc);
            }
        }
        return clcMap;
    }

    public byte[] generateInjectionExcel(List<BkdarTemp> comptes, String identifiantBmoi,
                                         String dateDebutArrete, String dateFinArrete,
                                         String dateDernierJourOuvre, String dateJourOuvreNextTrim
                                         ) throws Exception {
        // Dictionnaire pour la ligne 5
        Map<String, String> ncpDeviseMap = new HashMap<>();
        ncpDeviseMap.put("978", "83420002021");
        ncpDeviseMap.put("124", "83420002026");
        ncpDeviseMap.put("756", "83420002027");
        ncpDeviseMap.put("826", "83420002028");
        ncpDeviseMap.put("392", "83420002029");
        ncpDeviseMap.put("480", "83420002030");
        ncpDeviseMap.put("578", "83420002031");
        ncpDeviseMap.put("752", "83420002032");
        ncpDeviseMap.put("840", "83420002033");
        ncpDeviseMap.put("710", "83420002034");

        int mvti = 1;
        Map<String, Map<String, String>> clcData = loadClcData();

        try (Workbook workbook = new SXSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Font calibriFont = workbook.createFont();
            calibriFont.setFontName("Calibri");
            CellStyle calibriStyle = workbook.createCellStyle();
            calibriStyle.setFont(calibriFont);

            Sheet sheet = workbook.createSheet("INJC");

            sheet.setColumnWidth(3, 3072);
            int rowIdx = 0;

            for (BkdarTemp compte : comptes) {
                String age = compte.getAge();
                String dev = compte.getId().getDev();
                String ncp = compte.getId().getNcp();
                String tcli = compte.getTcli();

                // Récupère l'objet HistoriqueArrete
                Optional<HistoriqueArrete> historiqueOpt = historiqueArreteRepository.findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(
                        ncp, DateFormatUtil.parseDate(dateDebutArrete), DateFormatUtil.parseDate(dateFinArrete)
                );
                BigDecimal netAgios = historiqueOpt.map(HistoriqueArrete::getNetAgios).orElse(BigDecimal.ZERO);
                BigDecimal intDebiteur = historiqueOpt.map(HistoriqueArrete::getInteretDebiteur).orElse(BigDecimal.ZERO);

                BigDecimal agiosMGAValue = BigDecimal.valueOf(0.00);
                BigDecimal intDebMGAValue = BigDecimal.valueOf(0.00);
                BigDecimal monCol15Ligne6 = BigDecimal.ZERO;
                BigDecimal vingtPourcent = BigDecimal.ZERO;
                BigDecimal tind = BigDecimal.ZERO;
                BigDecimal resultat = BigDecimal.ZERO;

                DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.FRANCE));

                for (int gz = 0; gz <= 12; gz++) {
                    Row row = sheet.createRow(rowIdx++);

                    for (int col = 0; col <= 58; col++) {
                        Cell cell = row.createCell(col);
                        cell.setCellStyle(calibriStyle);
                    }

                    // Colonne 1 : AGE
                    row.getCell(0).setCellValue(age);
                    // Colonne 2 : DEV
                    if ((gz <= 3) || (gz >= 7 && gz <= 10)) {
                        row.getCell(1).setCellValue(dev);
                    } else {
                        row.getCell(1).setCellValue("969");
                    }
                    // Colonne 3 CHA
                    if (gz == 0 || gz == 7) {
                        row.getCell(2).setCellValue("211000");
                    } else if (gz < 3 || (gz == 8 || gz == 9)) {
                        row.getCell(2).setCellValue("999998");
                    } else if (gz == 3 || gz == 10) {
                        row.getCell(2).setCellValue("341000");
                    } else if (gz == 4 || gz == 11) {
                        row.getCell(2).setCellValue("342000");
                    } else if (gz == 5 || gz == 6){
                        row.getCell(2).setCellValue("707300");
                    } else {
                        row.getCell(2).setCellValue("315000");
                    }

                    // Colonne 4 NCP
                    String ncpCol4 = "";
                    if (gz == 0 || gz == 7) {
                        ncpCol4 = ncp;
                    } else if (gz < 3 || (gz == 8 || gz == 9)) {
                        ncpCol4 = "89999980014";
                    } else if (gz == 3 || gz == 10) {
                        ncpCol4 = "83410002031";
                    } else if (gz == 4 || gz == 11) {
                        ncpCol4 = ncpDeviseMap.getOrDefault(dev, "");
                    } else if (gz == 12) {
                        ncpCol4 = "83150005050";
                    } else {
                        switch (tcli != null ? tcli.trim() : "") {
                            case "1":
                                ncpCol4 = (gz == 5) ? "87073000410" : "87073000414";
                                break;
                            case "2":
                                ncpCol4 = (gz == 5) ? "87073000610" : "87073000614";
                                break;
                            case "3":
                                ncpCol4 = (gz == 5) ? "87073000210" : "87073000214";
                                break;
                            default:
                                ncpCol4 = "";
                        }
                    }
                    row.getCell(3).setCellValue(ncpCol4);

                    // Colonne 5 : SUF
                    row.getCell(4).setCellValue("");
                    // Colonne 6 : OPE (900)
                    row.getCell(5).setCellValue("900");
                    // Colonne 7 : MVTI (incrémenté, formaté sur 6 chiffres)
                    row.getCell(6).setCellValue(String.format("%06d", mvti++));
                    // Colonne 8 : RGP (vide)
                    row.getCell(7).setCellValue("");
                    // Colonne 9 : UTI (identifiantBmoi)
                    row.getCell(8).setCellValue(identifiantBmoi);
                    // Colonne 10 : EVE (vide)
                    row.getCell(9).setCellValue("");
                    // Colonne 11 : CLC
                    String clcValue = "";
                    if (gz == 0 || gz == 7) {
                        clcValue = compte.getClc();
                    } else if (gz == 12) {
                        clcValue = "96";
                    } else {
                        clcValue = clcData.getOrDefault(ncpCol4, new HashMap<>()).getOrDefault(age, "");
                    }
                    row.getCell(10).setCellValue(clcValue);

                    // Changement du ncp du colonne 4, ligne 5
                    if (gz == 5) {
                        if ("87073000210".equals(ncpCol4)) {
                            ncpCol4 = "87073001002";
                        } else if ("87073000410".equals(ncpCol4)) {
                            ncpCol4 = "87073001005";
                        } else if ("87073000610".equals(ncpCol4)) {
                            ncpCol4 = "87073001007";
                        }
                    }
                    row.getCell(3).setCellValue(ncpCol4);


                    // Colonne 12 : DCO
                    if (gz == 0 || gz == 1 || gz == 7 || gz == 8) {
                        row.getCell(11).setCellValue(DateFormatUtil.formatDateToSlash(dateJourOuvreNextTrim));
                    } else {
                        row.getCell(11).setCellValue(DateFormatUtil.formatDateToSlash(dateFinArrete));
                    }
                    // Colonne 13 : SER
                    row.getCell(12).setCellValue("0037");
                    // Colonne 14 : DVA
                    if (gz == 0 || gz == 7) {
                        row.getCell(13).setCellValue(DateFormatUtil.formatDateToSlash(dateJourOuvreNextTrim));
                    } else {
                        row.getCell(13).setCellValue(DateFormatUtil.formatDateToSlash(dateFinArrete));
                    }

                    // Colonne 15 : MON
                    if (gz == 4) {
                        tind = getTindFromRemote(dev, DateFormatUtil.parseDate(dateDernierJourOuvre));
                        agiosMGAValue = tind.multiply(netAgios).setScale(2, RoundingMode.HALF_UP).abs();
                        row.getCell(14).setCellValue(df.format(agiosMGAValue));
                    } else if (gz == 5) {
                        intDebMGAValue = getTindFromRemote(dev, DateFormatUtil.parseDate(dateDernierJourOuvre))
                                .multiply(intDebiteur).setScale(2, RoundingMode.HALF_UP).abs();
                        row.getCell(14).setCellValue(df.format(intDebMGAValue));
                    } else if (gz == 6) {
                        monCol15Ligne6 = agiosMGAValue.subtract(intDebMGAValue).abs();
                        row.getCell(14).setCellValue(df.format(monCol15Ligne6));
                        vingtPourcent = monCol15Ligne6.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
                    } else if (gz == 11 || gz == 12) {
                        row.getCell(14).setCellValue(df.format(vingtPourcent));
                    } else if (gz >= 7) {
                        if (tind.compareTo(BigDecimal.ZERO) != 0) {
                            resultat = vingtPourcent.divide(tind, 2, RoundingMode.HALF_UP);
                        }
                        row.getCell(14).setCellValue(df.format(resultat));
                    } else {
                        row.getCell(14).setCellValue(df.format(netAgios.abs()));
                    }

                    // Colonne 16 : SEN
                    if (gz == 0 || gz == 2 || gz == 4|| gz == 7 || gz == 9 || gz == 11) {
                        row.getCell(15).setCellValue("D");
                    } else {
                        row.getCell(15).setCellValue("C");
                    }
                    // Colonne 17 : LIB
                    if (gz == 0) {

                        row.getCell(16).setCellValue("AGIOS DU "+ DateFormatUtil.formatDateToSlash(dateDebutArrete) + " AU " + DateFormatUtil.formatDateToSlash(dateFinArrete));
                    } else {
                        row.getCell(16).setCellValue("ARRETE DE COMPTE AU " + DateFormatUtil.formatDateToSlash(dateFinArrete));
                    }
                    // Colonne 18 : EXO
                    row.getCell(17).setCellValue("O");
                    // Colonne 19 : PIE
                    String[] periodeAnnee = DateFormatUtil.getTrimestreFromDateFin(dateFinArrete);
                    row.getCell(18).setCellValue("INJART" + periodeAnnee[0] + periodeAnnee[1]);
                    // Colonne 20 : RLET
                    row.getCell(19).setCellValue(DateFormatUtil.getRlet(dateFinArrete));
                    // Colonne 21-31 : vides
                    for (int c = 20; c <= 30; c++) {
                        row.getCell(c).setCellValue("");
                    }
                    // Colonne 32 : NCC
                    row.getCell(31).setCellValue(ncp);
                    // Colonne 33 : vide
                    row.getCell(32).setCellValue("");
                    // Colonne 34 : ESI
                    row.getCell(33).setCellValue("N");
                    // Colonne 35-39 : vides
                    for (int c = 34; c <= 38; c++) {
                        row.getCell(c).setCellValue("");
                    }
                    // Colonne 40 : AGEM
                    row.getCell(39).setCellValue(age);
                    // Colonne 41 : vide
                    row.getCell(40).setCellValue("");
                    // Colonne 42 : DEVC
                    row.getCell(41).setCellValue(dev);
                    // Colonne 43 : MCTV
                    if (gz <= 6) {
                        row.getCell(42).setCellValue(df.format(netAgios.abs().doubleValue()));
                    } else {
                        row.getCell(42).setCellValue(df.format(resultat.abs().doubleValue()));
                    }
                    // Colonne 44 : PIEO
                    row.getCell(43).setCellValue("INJART" + periodeAnnee[0] + periodeAnnee[1]);
                    // Colonne 45-58 : vides
                    for (int c = 44; c <= 57; c++) {
                        row.getCell(c).setCellValue("");
                    }
                    // Colonne 59 : ORIGIN
                    if (gz <= 6) {
                        row.getCell(58).setCellValue("A");
                    } else {
                        row.getCell(58).setCellValue("B");
                    }

                }
            }
            workbook.write(out);
            return out.toByteArray();
        }
    }



    public void generateInjectionCsv(List<BkdarTemp> comptes, String identifiantBmoi,
                                     String dateDebutArrete, String dateFinArrete,
                                     String dateDernierJourOuvre, String dateJourOuvreNextTrim,
                                     Path csvFilePath) throws Exception {

        Map<String, String> ncpDeviseMap = new HashMap<>();
        ncpDeviseMap.put("978", "83420002021");
        ncpDeviseMap.put("124", "83420002026");
        ncpDeviseMap.put("756", "83420002027");
        ncpDeviseMap.put("826", "83420002028");
        ncpDeviseMap.put("392", "83420002029");
        ncpDeviseMap.put("480", "83420002030");
        ncpDeviseMap.put("578", "83420002031");
        ncpDeviseMap.put("752", "83420002032");
        ncpDeviseMap.put("840", "83420002033");
        ncpDeviseMap.put("710", "83420002034");

        int mvti = 1;
        Map<String, Map<String, String>> clcData = loadClcData();
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.FRANCE));
        String separator = "|";

        try (BufferedWriter writer = Files.newBufferedWriter(csvFilePath, java.nio.charset.StandardCharsets.UTF_8)) {
            for (BkdarTemp compte : comptes) {
                String age = compte.getAge();
                String dev = compte.getId().getDev();
                String ncp = compte.getId().getNcp();
                String tcli = compte.getTcli();

                Optional<HistoriqueArrete> historiqueOpt = historiqueArreteRepository.findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(
                        ncp, DateFormatUtil.parseDate(dateDebutArrete), DateFormatUtil.parseDate(dateFinArrete)
                );
                BigDecimal netAgios = historiqueOpt.map(HistoriqueArrete::getNetAgios).orElse(BigDecimal.ZERO);
                BigDecimal intDebiteur = historiqueOpt.map(HistoriqueArrete::getInteretDebiteur).orElse(BigDecimal.ZERO);

                BigDecimal agiosMGAValue = BigDecimal.valueOf(0.00);
                BigDecimal intDebMGAValue = BigDecimal.valueOf(0.00);
                BigDecimal monCol15Ligne6 = BigDecimal.ZERO;
                BigDecimal vingtPourcent = BigDecimal.ZERO;
                BigDecimal tind = BigDecimal.ZERO;
                BigDecimal resultat = BigDecimal.ZERO;

                for (int gz = 0; gz <= 12; gz++) {
                    List<String> cells = new ArrayList<>(59);

                    // Colonne 1 : AGE
                    cells.add(age);
                    // Colonne 2 : DEV
                    cells.add((gz <= 3) || (gz >= 7 && gz <= 10) ? dev : "969");
                    // Colonne 3 : CHA
                    if (gz == 0 || gz == 7) cells.add("211000");
                    else if (gz < 3 || (gz == 8 || gz == 9)) cells.add("999998");
                    else if (gz == 3 || gz == 10) cells.add("341000");
                    else if (gz == 4 || gz == 11) cells.add("342000");
                    else if (gz == 5 || gz == 6) cells.add("707300");
                    else cells.add("315000");
                    // Colonne 4 : NCP
                    String ncpCol4 = "";
                    if (gz == 0 || gz == 7) ncpCol4 = ncp;
                    else if (gz < 3 || (gz == 8 || gz == 9)) ncpCol4 = "89999980014";
                    else if (gz == 3 || gz == 10) ncpCol4 = "83410002031";
                    else if (gz == 4 || gz == 11) ncpCol4 = ncpDeviseMap.getOrDefault(dev, "");
                    else if (gz == 12) ncpCol4 = "83150005050";
                    else {
                        switch (tcli != null ? tcli.trim() : "") {
                            case "1": ncpCol4 = (gz == 5) ? "87073000410" : "87073000414"; break;
                            case "2": ncpCol4 = (gz == 5) ? "87073000610" : "87073000614"; break;
                            case "3": ncpCol4 = (gz == 5) ? "87073000210" : "87073000214"; break;
                            default: ncpCol4 = "";
                        }
                    }
                    // Changement du ncp du colonne 4, ligne 5
                    if (gz == 5) {
                        if ("87073000210".equals(ncpCol4)) ncpCol4 = "87073001002";
                        else if ("87073000410".equals(ncpCol4)) ncpCol4 = "87073001005";
                        else if ("87073000610".equals(ncpCol4)) ncpCol4 = "87073001007";
                    }
                    cells.add(ncpCol4);

                    // Colonne 5 : SUF
                    cells.add("");
                    // Colonne 6 : OPE
                    cells.add("900");
                    // Colonne 7 : MVTI
                    cells.add(String.format("%06d", mvti++));
                    // Colonne 8 : RGP
                    cells.add("");
                    // Colonne 9 : UTI
                    cells.add(identifiantBmoi);
                    // Colonne 10 : EVE
                    cells.add("");
                    // Colonne 11 : CLC
                    String clcValue = "";
                    if (gz == 0 || gz == 7) clcValue = compte.getClc();
                    else if (gz == 12) clcValue = "96";
                    else clcValue = clcData.getOrDefault(ncpCol4, new HashMap<>()).getOrDefault(age, "");
                    cells.add(clcValue);

                    // Colonne 12 : DCO
                    cells.add((gz == 0 || gz == 1 || gz == 7 || gz == 8) ?
                            DateFormatUtil.formatDateToSlash(dateJourOuvreNextTrim) :
                            DateFormatUtil.formatDateToSlash(dateFinArrete));
                    // Colonne 13 : SER
                    cells.add("0037");
                    // Colonne 14 : DVA
                    cells.add((gz == 0 || gz == 7) ?
                            DateFormatUtil.formatDateToSlash(dateJourOuvreNextTrim) :
                            DateFormatUtil.formatDateToSlash(dateFinArrete));
                    // Colonne 15 : MON
                    if (gz == 4) {
                        tind = getTindFromRemote(dev, DateFormatUtil.parseDate(dateDernierJourOuvre));
                        agiosMGAValue = tind.multiply(netAgios).setScale(2, RoundingMode.HALF_UP).abs();
                        cells.add(df.format(agiosMGAValue));
                    } else if (gz == 5) {
                        intDebMGAValue = getTindFromRemote(dev, DateFormatUtil.parseDate(dateDernierJourOuvre))
                                .multiply(intDebiteur).setScale(2, RoundingMode.HALF_UP).abs();
                        cells.add(df.format(intDebMGAValue));
                    } else if (gz == 6) {
                        monCol15Ligne6 = agiosMGAValue.subtract(intDebMGAValue).abs();
                        cells.add(df.format(monCol15Ligne6));
                        vingtPourcent = monCol15Ligne6.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
                    } else if (gz == 11 || gz == 12) {
                        cells.add(df.format(vingtPourcent));
                    } else if (gz >= 7) {
                        if (tind.compareTo(BigDecimal.ZERO) != 0) {
                            resultat = vingtPourcent.divide(tind, 2, RoundingMode.HALF_UP);
                        }
                        cells.add(df.format(resultat));
                    } else {
                        cells.add(df.format(netAgios.abs()));
                    }

                    // Colonne 16 : SEN
                    cells.add((gz == 0 || gz == 2 || gz == 4|| gz == 7 || gz == 9 || gz == 11) ? "D" : "C");
                    // Colonne 17 : LIB
                    if (gz == 0)
                        cells.add("AGIOS DU "+ DateFormatUtil.formatDateToSlash(dateDebutArrete) + " AU " + DateFormatUtil.formatDateToSlash(dateFinArrete));
                    else
                        cells.add("ARRETE DE COMPTE AU " + DateFormatUtil.formatDateToSlash(dateFinArrete));
                    // Colonne 18 : EXO
                    cells.add("O");
                    // Colonne 19 : PIE
                    String[] periodeAnnee = DateFormatUtil.getTrimestreFromDateFin(dateFinArrete);
                    cells.add("INJART" + periodeAnnee[0] + periodeAnnee[1]);
                    // Colonne 20 : RLET
                    cells.add(DateFormatUtil.getRlet(dateFinArrete));
                    // Colonne 21-31 : vides
                    for (int c = 20; c <= 30; c++) cells.add("");
                    // Colonne 32 : NCC
                    cells.add(ncp);
                    // Colonne 33 : vide
                    cells.add("");
                    // Colonne 34 : ESI
                    cells.add("N");
                    // Colonne 35-39 : vides
                    for (int c = 34; c <= 38; c++) cells.add("");
                    // Colonne 40 : AGEM
                    cells.add(age);
                    // Colonne 41 : vide
                    cells.add("");
                    // Colonne 42 : DEVC
                    cells.add(dev);
                    // Colonne 43 : MCTV
                    cells.add(gz <= 6 ? df.format(netAgios.abs().doubleValue()) : df.format(resultat.abs().doubleValue()));
                    // Colonne 44 : PIEO
                    cells.add("INJART" + periodeAnnee[0] + periodeAnnee[1]);
                    // Colonne 45-58 : vides
                    for (int c = 44; c <= 57; c++) cells.add("");
                    // Colonne 59 : ORIGIN
                    cells.add(gz <= 6 ? "A" : "B");

                    writer.write(String.join(separator, cells));
                    writer.newLine();
                }
            }
        }
    }
}