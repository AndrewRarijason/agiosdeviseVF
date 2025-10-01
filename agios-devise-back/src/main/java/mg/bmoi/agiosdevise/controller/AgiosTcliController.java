package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.service.AgiosTcliService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AgiosTcliController {

    @Autowired
    private AgiosTcliService agiosTcliService;

    @GetMapping("/agios-tcli")
    public ResponseEntity<List<Map<String, Object>>> getSumAgiosParTcli(
            @RequestParam("dateDebutArrete") String dateDebut,
            @RequestParam("dateFinArrete") String dateFin
    ) {
        Date dateDebutParsed = DateFormatUtil.parseDate(dateDebut);
        Date dateFinParsed = DateFormatUtil.parseDate(dateFin);
        List<Map<String, Object>> result = agiosTcliService.getSumAgiosParTcli(dateDebutParsed, dateFinParsed);
        return ResponseEntity.ok(result);
    }
}