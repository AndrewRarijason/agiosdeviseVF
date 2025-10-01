package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.DTO.HistoriqueArreteDto;
import mg.bmoi.agiosdevise.service.ArreteCompteService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/arretes")
public class HistoriqueArretesController {

    private final ArreteCompteService arreteCompteService;

    public HistoriqueArretesController(ArreteCompteService arreteCompteService) {
        this.arreteCompteService = arreteCompteService;
    }

    @GetMapping("/historique")
    public List<HistoriqueArreteDto> getSyntheseComptes(
            @RequestParam String trimestre,
            @RequestParam String annee
    ) {
        String[] periode = DateFormatUtil.getPeriodeTrimestre(trimestre, annee);
        Date dateParsedDebut = DateFormatUtil.parseDate(periode[0]);
        Date dateParsedFin = DateFormatUtil.parseDate(periode[1]);
        return arreteCompteService.getHistoriqueArretesByPeriode(dateParsedDebut, dateParsedFin);
    }

    @GetMapping("/historique/detail")
    public HistoriqueArreteDto getHistoriqueDetail(
            @RequestParam String ncp,
            @RequestParam String trimestre,
            @RequestParam String annee
    ) {
        String[] periode = DateFormatUtil.getPeriodeTrimestre(trimestre, annee);
        Date debut = DateFormatUtil.parseDate(periode[0]);
        Date fin = DateFormatUtil.parseDate(periode[1]);
        return arreteCompteService.getHistoriqueArreteById(ncp, debut, fin);
    }
}
