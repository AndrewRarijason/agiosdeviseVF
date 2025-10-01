package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.DTO.DerogImportExcelDto;
import mg.bmoi.agiosdevise.service.DerogImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class DerogImportExcelController {

    @Autowired
    private DerogImportExcelService derogImportExcelService;

    @PostMapping("/excel")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file){
            List<DerogImportExcelDto> data = derogImportExcelService.importFromExcel(file);
            return ResponseEntity.ok("Import liste dérogations " + data.size() + " lignes traitées.");
    }
}
