package mg.bmoi.agiosdevise.controller.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBksldDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBksldService;
import mg.bmoi.agiosdevise.service.insertion.ImportTmpBksldService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class InsertTmpBksldController {

    private final ImportTmpBksldService importTmpBksld;
    private final ExtractionBksldService extractionBksldService;

    public InsertTmpBksldController(ImportTmpBksldService importTmpBksld, ExtractionBksldService extractionBksldService) {
        this.importTmpBksld = importTmpBksld;
        this.extractionBksldService = extractionBksldService;
    }

    @PostMapping("/bksld")
    public ResponseEntity<String> insertTmpBksld(@RequestParam String date) {
        String dateForSql = DateFormatUtil.normalizeToIsoDate(date);
        List<ExtractionBksldDto> data = extractionBksldService.getBksldExtraction(dateForSql);

        importTmpBksld.insertIntoTmpBksld(data);

        return ResponseEntity.ok("Données insérées dans BKSLD_D avec succès");
    }

}
