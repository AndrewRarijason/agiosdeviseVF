package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.service.InjectionExcelExportService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/injection")
public class InjectionExportController {

    private final BkdarTempRepository bkdarTempRepository;
    private final InjectionExcelExportService injectionExcelExportService;

    public InjectionExportController(BkdarTempRepository bkdarTempRepository,
                                     InjectionExcelExportService injectionExcelExportService) {
        this.bkdarTempRepository = bkdarTempRepository;
        this.injectionExcelExportService = injectionExcelExportService;
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportInjectionFile(
            @RequestParam String idBmoi,
            @RequestParam String dtDebutArrete,
            @RequestParam String dtFinArrete,
            @RequestParam String dtDernierJourOuvre,
            @RequestParam String dtJourOuvreNextTrim
    ) throws Exception {
        List<BkdarTemp> comptes = bkdarTempRepository.findAll();
        byte[] excelBytes = injectionExcelExportService.generateInjectionExcel(
                comptes, idBmoi, dtDebutArrete, dtFinArrete, dtDernierJourOuvre, dtJourOuvreNextTrim);

        Path rootPath = Paths.get("").toAbsolutePath();
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd.HHmm"));
        String excelFileName = datePart + ".INJECTION.xlsx";
        String csvFileName = datePart + ".INJECTION.ART.csv";
        Path excelFilePath = rootPath.resolve(excelFileName);
        Path csvFilePath = rootPath.resolve(csvFileName);

        // Écriture du fichier Excel
        Files.write(excelFilePath, excelBytes);

        // Génération et écriture du fichier CSV
        injectionExcelExportService.generateInjectionCsv(
                comptes, idBmoi, dtDebutArrete, dtFinArrete, dtDernierJourOuvre, dtJourOuvreNextTrim, csvFilePath);

        // Retourne les deux chemins
        String result = "Excel: " + excelFilePath.toString() + "\nCSV: " + csvFilePath.toString();
        return ResponseEntity.ok(result);
    }
}