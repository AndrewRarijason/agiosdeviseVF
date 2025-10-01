package mg.bmoi.agiosdevise.controller.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBkdarDto;
import mg.bmoi.agiosdevise.service.extraction.ExtractionBkdarService;
import mg.bmoi.agiosdevise.service.insertion.ImportTmpBkdarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class InsertTmpBkdarController {

    private final ImportTmpBkdarService importTmpBkdar;
    private final ExtractionBkdarService extractionBkdarService;

    public InsertTmpBkdarController(ImportTmpBkdarService importTmpBkdar, ExtractionBkdarService extractionBkdarService) {
        this.importTmpBkdar = importTmpBkdar;
        this.extractionBkdarService = extractionBkdarService;
    }

    @PostMapping("/bkdar")
    public ResponseEntity<String> insertTmpBkdar(
            @RequestParam String annee,
            @RequestParam String mois) {
        List<ExtractionBkdarDto> data = extractionBkdarService.getBkdarExtraction(annee, mois);

        importTmpBkdar.insertIntoTmpBkdar(data);

        return ResponseEntity.ok("Données insérées dans BKDAR_D avec succès");
    }

}
