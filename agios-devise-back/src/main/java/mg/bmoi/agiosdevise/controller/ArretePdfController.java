package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.BkhisTemp;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.service.ArreteCompteService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import mg.bmoi.agiosdevise.util.FolderUtil;
import mg.bmoi.agiosdevise.util.PdfUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/arrete-pdf")
public class ArretePdfController {

    private final ArreteCompteService arreteCompteService;
    private final BkdarTempRepository bkdarTempRepository;

    public ArretePdfController(ArreteCompteService arreteCompteService, BkdarTempRepository bkdarTempRepository) {
        this.arreteCompteService = arreteCompteService;
        this.bkdarTempRepository = bkdarTempRepository;
    }

    private Date parseDate(String dateStr) {
        String normalized = DateFormatUtil.normalizeToIsoDate(dateStr);
        LocalDate localDate = LocalDate.parse(normalized, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return java.sql.Date.valueOf(localDate);
    }

    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>>getSyntheseAvecSommeMouvements(
            @RequestParam("dateDebutArrete") String dateDebutArrete,
            @RequestParam("dateFinArrete") String dateFinArrete,
            @RequestParam("dateDernierJouvre") String dateDernierJouvre
    ) throws Exception {
        Map<String, Object> response = new HashMap<>();

        Date dateDebutParsed = DateFormatUtil.parseDate(dateDebutArrete);
        Date dateFinParsed = DateFormatUtil.parseDate(dateFinArrete);

        Map<String, Map<String, Object>> data = arreteCompteService.gestionTransactionEtSyntheseParCompte(dateDebutParsed, dateFinParsed);
        Path folderPath = FolderUtil.createAndCleanFolder("ECHELLE ARRETES PDF");
        long nbTotal = data.size();

        List<String> ncpList = new ArrayList<>(data.keySet());
        PdfUtils pdfUtils = new PdfUtils(arreteCompteService, bkdarTempRepository);
        pdfUtils.generateEchellePdfFiles(ncpList, dateDebutParsed, dateFinParsed, folderPath, dateDernierJouvre);

        LocalDateTime end = LocalDateTime.now();
        response.put("nbTotal_compte_traites", nbTotal);
        response.put("path_dossier", folderPath.toAbsolutePath().toString());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/generate-specific")
    public ResponseEntity<Map<String, Object>> getSyntheseAvecSommeMouvementsSpecific(
            @RequestParam("dateDebutArrete") String dateDebutArrete,
            @RequestParam("dateFinArrete") String dateFinArrete,
            @RequestParam("dateDernierJouvre") String dateDernierJouvre,
            @RequestParam(value = "file", required = true) MultipartFile file
    ) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Date dateDebutParsed = DateFormatUtil.parseDate(dateDebutArrete);
        Date dateFinParsed = DateFormatUtil.parseDate(dateFinArrete);

        Path folderPath = FolderUtil.createAndCleanFolder("ECHELLE ARRETES PDF");
        List<String> ncpList = new ArrayList<>();
        Map<String, Map<String, Object>> data = new HashMap<>();

        if (file != null && !file.isEmpty()) {
            try (InputStream is = file.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(is);
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // ignorer l'entête
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        String ncp = cell.getStringCellValue().trim();
                        if (!ncp.isEmpty()) {
                            ncpList.add(ncp);
                            Map<String, Map<String, Object>> result = arreteCompteService.gestionTransactionEtSyntheseParComptePourUnCompte(ncp, dateDebutParsed, dateFinParsed);
                            data.putAll(result);
                        }
                    }
                }
            }
        } else {
            ncpList = new ArrayList<>(data.keySet());
        }

        if (ncpList.isEmpty()) {
            throw new IllegalArgumentException("Aucun NCP trouvé à traiter.");
        }
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Aucune donnée trouvée pour les NCP fournis.");
        }

        PdfUtils pdfUtils = new PdfUtils(arreteCompteService, bkdarTempRepository);
        pdfUtils.generateEchellePdfFiles(ncpList, dateDebutParsed, dateFinParsed, folderPath, dateDernierJouvre);

        response.put("nbTotal_compte_traites", ncpList.size());
        response.put("path_dossier", folderPath.toAbsolutePath().toString());
        return ResponseEntity.ok(response);
    }

}