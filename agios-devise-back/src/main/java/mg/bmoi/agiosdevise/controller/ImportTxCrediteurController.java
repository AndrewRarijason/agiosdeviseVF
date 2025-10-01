package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.TicDevise;
import mg.bmoi.agiosdevise.repository.TicDeviseRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping ("/api")
public class ImportTxCrediteurController {

    @Autowired
    private TicDeviseRepository ticDeviseRepository;

    @PostMapping ("/import-tx-crediteur")
    @Transactional
    public String importTxCrediteur(@RequestParam("file") MultipartFile file) {
        int lignesMiseAJour = 0;
        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cdAlphaCell = row.getCell(0);
                Cell valeurCell = row.getCell(1);
                if (cdAlphaCell == null || valeurCell == null) continue;

                String cdAlpha = cdAlphaCell.getStringCellValue().trim();
                BigDecimal valeur = null;
                if (valeurCell.getCellType() == CellType.NUMERIC) {
                    valeur = BigDecimal.valueOf(valeurCell.getNumericCellValue());
                } else {
                    String valStr = valeurCell.getStringCellValue().replace(",", ".").trim();
                    valeur = new BigDecimal(valStr);
                }

                Optional<TicDevise> optDevise = ticDeviseRepository.findByCdAlpha(cdAlpha);
                if (optDevise.isPresent()) {
                    TicDevise devise = optDevise.get();
                    devise.setValeur(valeur);
                    ticDeviseRepository.save(devise);
                    lignesMiseAJour++;
                }
            }
            return "Import terminé avec succès. " + lignesMiseAJour + " lignes mises à jour.";
        } catch (Exception e) {
            return "Erreur lors de l'import : " + e.getMessage();
        }
    }

    @GetMapping("/visualiser-tx-crediteur")
    public java.util.List<TicDevise> visualiserTxCrediteur() {
        return ticDeviseRepository.findAll();
    }

}
