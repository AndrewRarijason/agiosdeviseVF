package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.DTO.HistoriqueArreteDto;
import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.service.ArreteCompteService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import mg.bmoi.agiosdevise.util.FolderUtil;
import mg.bmoi.agiosdevise.util.PdfUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avis-agios-pdf")
public class AvisAgiosPdfController {

    private final ArreteCompteService arreteCompteService;
    private final BkdarTempRepository bkdarTempRepository;
    private final Logger log = LoggerFactory.getLogger(AvisAgiosPdfController.class);

    public AvisAgiosPdfController(ArreteCompteService arreteCompteService, BkdarTempRepository bkdarTempRepository) {
        this.arreteCompteService = arreteCompteService;
        this.bkdarTempRepository = bkdarTempRepository;
    }


    @PostMapping("/export-from-excel")
    public ResponseEntity<Map<String, Object>> exportAvisAgiosFromExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dateDebut") String dateDebut,
            @RequestParam("dateFin") String dateFin
    ) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Date dateDebutParsed = DateFormatUtil.parseDate(dateDebut);
        Date dateFinParsed = DateFormatUtil.parseDate(dateFin);

        List<String> ncpList = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null || headerRow.getCell(0) == null) {
                throw new IllegalArgumentException("Le fichier Excel est vide ou ne contient pas d'en-tête");
            }
            Cell headerCell = headerRow.getCell(0);
            String headerValue = headerCell.getStringCellValue().trim();
            if (!"NCP".equalsIgnoreCase(headerValue)) {
                throw new IllegalArgumentException("La première colonne doit avoir l'en-tête 'NCP'");
            }
            if (headerRow.getLastCellNum() > 1) {
                throw new IllegalArgumentException("Le fichier ne doit contenir qu'une seule colonne 'NCP'");
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String ncp;
                    if (cell.getCellType() == CellType.STRING) {
                        ncp = cell.getStringCellValue().trim();
                        if (!ncp.matches("\\d+")) {
                            throw new IllegalArgumentException("La ligne " + (i + 1) + " contient une valeur non numérique: '" + ncp + "'");
                        }
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        ncp = String.valueOf((long) cell.getNumericCellValue()).trim();
                    } else {
                        throw new IllegalArgumentException("La ligne " + (i + 1) + " contient un format de cellule non supporté");
                    }
                    if (ncp.isEmpty()) {
                        throw new IllegalArgumentException("La ligne " + (i + 1) + " contient un NCP vide");
                    }
                    ncpList.add(ncp);
                }
            }
        }

        if (ncpList.isEmpty()) {
            throw new IllegalArgumentException("Aucun NCP valide trouvé dans le fichier");
        }

        // Vérification des NCP en base
        List<String> ncpTrouves = new ArrayList<>();
        List<String> ncpNonTrouves = new ArrayList<>();
        Set<String> ncpEnBase = bkdarTempRepository.findAll().stream()
                .map(b -> b.getId().getNcp())
                .collect(Collectors.toSet());
        for (String ncp : ncpList) {
            if (ncpEnBase.contains(ncp)) {
                ncpTrouves.add(ncp);
            } else {
                ncpNonTrouves.add(ncp);
            }
        }

        Path folderPath = FolderUtil.createAndCleanFolder("AVIS AGIOS PDF");
        long nbTotal = ncpTrouves.size();

        PdfUtils pdfUtils = new PdfUtils(arreteCompteService, bkdarTempRepository);
        pdfUtils.generatePdfFiles(ncpTrouves, dateDebutParsed, dateFinParsed, folderPath);

        response.put("nbtotal_comptes_traites", nbTotal);
        response.put("path_dossier", folderPath.toAbsolutePath().toString());
        response.put("ncp_non_trouves", ncpNonTrouves);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/export-all")
    public ResponseEntity<Map<String, Object>> exportAllAvisAgiosPdf(
            @RequestParam("dateDebut") String dateDebut,
            @RequestParam("dateFin") String dateFin
    ) throws Exception {
        Map<String, Object> response = new HashMap<>();

        Date dateDebutParsed = DateFormatUtil.parseDate(dateDebut);
        Date dateFinParsed = DateFormatUtil.parseDate(dateFin);

        // Récupérer les historiques d'arrêté pour la période
        List<HistoriqueArreteDto> historiques = arreteCompteService.getHistoriqueArretesByPeriode(dateDebutParsed, dateFinParsed);

        // Extraire la liste des comptes à traiter
        List<String> ncpList = historiques.stream()
                .map(HistoriqueArreteDto::getNcp)
                .distinct()
                .collect(Collectors.toList());

        Path folderPath = FolderUtil.createAndCleanFolder("AVIS AGIOS PDF");
        long nbTotal = ncpList.size();

        PdfUtils pdfUtils = new PdfUtils(arreteCompteService, bkdarTempRepository);
        pdfUtils.generatePdfFiles(ncpList, dateDebutParsed, dateFinParsed, folderPath);

        response.put("nbtotal_comptes_traites", nbTotal);
        response.put("path_dossier", folderPath.toAbsolutePath().toString());

        return ResponseEntity.ok(response);
    }
}