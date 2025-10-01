package mg.bmoi.agiosdevise.controller.extraction;

import mg.bmoi.agiosdevise.DTO.ExtractionBksldDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBksldService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/extraction")
public class ExtractionBksldController {

    private final ExtractionBksldService extractionBksldService;

    public ExtractionBksldController(ExtractionBksldService extractionBksldService) {
        this.extractionBksldService = extractionBksldService;
    }

    @GetMapping("/bksld")
    public ResponseEntity<List<ExtractionBksldDto>> getSoldesByDate(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") String date) {

        List<ExtractionBksldDto> result = extractionBksldService.getBksldExtraction(date);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/bksld/csv", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<Resource> getSoldesAsCsv(@RequestParam String date) {

        String dateForSql = DateFormatUtil.normalizeToIsoDate(date);
        List<ExtractionBksldDto> data = extractionBksldService.getBksldExtraction(dateForSql);
        InputStreamResource resource = extractionBksldService.generateCsv(data);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bksld_" + date + "_" + dateExport + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }

    @GetMapping(value = "/bksld/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<Resource> getSoldesAsExcel(@RequestParam String date) {

        String dateForSql = DateFormatUtil.normalizeToIsoDate(date);
        List<ExtractionBksldDto> data = extractionBksldService.getBksldExtraction(dateForSql);
        InputStreamResource resource = extractionBksldService.generateExcel(data);

        String dateExport = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "bksld_" + date + "_" + dateExport + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }
}