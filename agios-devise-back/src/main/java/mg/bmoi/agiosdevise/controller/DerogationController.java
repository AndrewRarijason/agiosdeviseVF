package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.Derogation;
import mg.bmoi.agiosdevise.repository.DerogationRepository;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class DerogationController {
    private final BkdarTempRepository bkdarTempRepository;
    private final DerogationRepository derogationRepository;

    public DerogationController(BkdarTempRepository bkdarTempRepository,
                                DerogationRepository derogationRepository) {
        this.bkdarTempRepository = bkdarTempRepository;
        this.derogationRepository = derogationRepository;
    }

    // Méthode utilitaire pour lire une cellule en String
    private String getCellAsString(Row row, int cellIndex) {
        if (row == null || row.getCell(cellIndex) == null) return "";
        Cell cell = row.getCell(cellIndex);
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    double d = cell.getNumericCellValue();
                    if (d == (long) d) return String.valueOf((long) d);
                    else return String.valueOf(d);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }

    @PostMapping("/import-derrogation")
    public ResponseEntity<Map<String, Object>> importDerogationExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dateDebutNextTrim") String dateDebutNextTrim) {

        Map<String, Object> result = new HashMap<>();
        int loaded = 0;

        derogationRepository.deleteAll();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Styles de police
            Font fontGreen = workbook.createFont();
            fontGreen.setColor(IndexedColors.GREEN.getIndex());
            Font fontRed = workbook.createFont();
            fontRed.setColor(IndexedColors.RED.getIndex());

            CellStyle styleGreen = workbook.createCellStyle();
            styleGreen.setFont(fontGreen);
            CellStyle styleRed = workbook.createCellStyle();
            styleRed.setFont(fontRed);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String age = getCellAsString(row, 0);
                String dev = getCellAsString(row, 1);
                String ncp = getCellAsString(row, 2);
                String cha = getCellAsString(row, 3);
                String clc = getCellAsString(row, 4);
                String cli = getCellAsString(row, 5);
                String inti = getCellAsString(row, 6);
                String typ = getCellAsString(row, 7);
                String ges = getCellAsString(row, 8);
                String derog = getCellAsString(row, 9).toLowerCase();

                boolean inserted = false;
                if ("oui".equals(derog) && bkdarTempRepository.existsByIdNcpAndIdDev(ncp, dev)) {
                    Derogation entity = new Derogation();
                    entity.setAge(age);
                    entity.setDev(dev);
                    entity.setNcp(ncp);
                    entity.setCha(cha);
                    entity.setClc(clc);
                    entity.setCli(cli);
                    entity.setInti(inti);
                    entity.setTyp(typ);
                    entity.setGes(ges);
                    entity.setEnDerogation(1);
                    derogationRepository.save(entity);
                    loaded++;
                    inserted = true;
                }

                // Appliquer la couleur à toute la ligne
                CellStyle style = inserted ? styleGreen : styleRed;
                for (int c = 0; c <= 9; c++) {
                    Cell cell = row.getCell(c);
                    if (cell != null) cell.setCellStyle(style);
                }
            }
            result.put("comptes_derogation_charges", loaded);

            LocalDate localDate;
            if (dateDebutNextTrim.contains("/")) {
                localDate = LocalDate.parse(dateDebutNextTrim, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                localDate = LocalDate.parse(dateDebutNextTrim, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            String fileName = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".DERROGATIONS SUR LES COMPTES.xlsx";
            String filePath = new File(fileName).getAbsolutePath();

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }

            result.put("fichier_derogations_colore", fileName);
            result.put("chemin_fichier", filePath);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @DeleteMapping("/derogations")
    public ResponseEntity<Map<String, String>> deleteAllDerogations() {
        derogationRepository.deleteAll();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Toutes les dérogations ont été supprimées.");
        return ResponseEntity.ok(response);
    }


}