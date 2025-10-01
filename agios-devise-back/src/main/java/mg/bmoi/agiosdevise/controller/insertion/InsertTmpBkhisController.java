package mg.bmoi.agiosdevise.controller.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBkhisDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBkhisService;
import mg.bmoi.agiosdevise.service.insertion.ImportTmpBkhisService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class InsertTmpBkhisController {

    private final ImportTmpBkhisService importTmpBkhisService;
    private final ExtractionBkhisService extractionBkhisService;

    public InsertTmpBkhisController(ImportTmpBkhisService importTmpBkhisService, ExtractionBkhisService extractionBkhisService) {
        this.importTmpBkhisService = importTmpBkhisService;
        this.extractionBkhisService = extractionBkhisService;
    }

    @PostMapping("/bkhis")
    public ResponseEntity<String> insertTmpBkhis(
            @RequestParam String dateref,
            @RequestParam String datefin) {
        String dateRefForSql = DateFormatUtil.normalizeToIsoDate(dateref);
        String dateFinForSql = DateFormatUtil.normalizeToIsoDate(datefin);
        List<ExtractionBkhisDto> data = extractionBkhisService.getBkhisExtraction(dateRefForSql, dateFinForSql);

        importTmpBkhisService.insertIntoTmpBkhis(data);

        return  ResponseEntity.ok("Données insérées dans C##MGSTGADR.BKHIS avec succès");
    }
}
