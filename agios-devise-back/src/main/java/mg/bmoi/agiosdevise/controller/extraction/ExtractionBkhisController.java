package mg.bmoi.agiosdevise.controller.extraction;


import mg.bmoi.agiosdevise.DTO.ExtractionBkhisDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBkhisService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/extraction")
public class ExtractionBkhisController {

    private final ExtractionBkhisService extractionBkhisService;

    public ExtractionBkhisController(ExtractionBkhisService extractionBkhisService) {
        this.extractionBkhisService = extractionBkhisService;
    }

    @GetMapping("/bkhis")
    public ResponseEntity<List<ExtractionBkhisDto>> getBkhisExtraction(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")String dateref,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") String datefin) {
        List<ExtractionBkhisDto> result = extractionBkhisService.getBkhisExtraction(dateref, datefin);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/bkhis/csv", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Resource> getBkhisAsCsv(
            @RequestParam String dateref,
            @RequestParam String datefin) {

        String dateRefForSql = DateFormatUtil.normalizeToIsoDate(dateref);
        String dateFinForSql = DateFormatUtil.normalizeToIsoDate(datefin);
        List<ExtractionBkhisDto> data = extractionBkhisService.getBkhisExtraction(dateRefForSql, dateFinForSql);
        InputStreamResource resource = extractionBkhisService.generateCsv(data);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bkhis_" + dateref + "_" + datefin + "_" + dateExport + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }

    @GetMapping(value = "/bkhis/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<Resource> getBkhisAsExcel(
            @RequestParam String dateref,
            @RequestParam String datefin) {

        String dateRefForSql = DateFormatUtil.normalizeToIsoDate(dateref);
        String dateFinForSql = DateFormatUtil.normalizeToIsoDate(datefin);
        List<ExtractionBkhisDto> data = extractionBkhisService.getBkhisExtraction(dateRefForSql, dateFinForSql);
        InputStreamResource resource = extractionBkhisService.generateExcel(data);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bkhis_" + dateref + "-" + datefin + "_" + dateExport + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }
}
