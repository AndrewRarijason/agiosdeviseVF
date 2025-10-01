package mg.bmoi.agiosdevise.controller.extraction;

import mg.bmoi.agiosdevise.DTO.ExtractionBkdarDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBkdarService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/extraction")
public class ExtractionBkdarController {

    private final ExtractionBkdarService extractionBkdarService;

    public ExtractionBkdarController(ExtractionBkdarService extractionBkdarService) {
        this.extractionBkdarService = extractionBkdarService;
    }

    @GetMapping("/bkdar")
    public ResponseEntity<List<ExtractionBkdarDto>> getBkdarExtraction(
            @RequestParam String annee,
            @RequestParam String mois) {
        List<ExtractionBkdarDto> result = extractionBkdarService.getBkdarExtraction(annee, mois);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/bkdar/csv", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Resource>getBkdarAsCsv(
            @RequestParam String annee,
            @RequestParam String mois) {
        List<ExtractionBkdarDto> date = extractionBkdarService.getBkdarExtraction(annee, mois);
        InputStreamResource resource = extractionBkdarService.generateCsv(date);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bkdar_" + annee + "_" + mois + "_" + dateExport + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }

    @GetMapping(value = "/bkdar/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<Resource> getBkdarAsExcel(
            @RequestParam String annee,
            @RequestParam String mois) {
        List<ExtractionBkdarDto> data = extractionBkdarService.getBkdarExtraction(annee, mois);
        InputStreamResource resource = extractionBkdarService.generateExcel(data);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bkdar_" + annee + "_" + mois + "_" + dateExport + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }
}