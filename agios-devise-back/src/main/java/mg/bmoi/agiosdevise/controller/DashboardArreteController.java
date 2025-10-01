package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.DashboardArrete;
import mg.bmoi.agiosdevise.service.DashboardArreteService;
import mg.bmoi.agiosdevise.util.DateFormatUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardArreteController {
    private final DashboardArreteService dashboardArreteService;

    public DashboardArreteController(DashboardArreteService service) {
        this.dashboardArreteService = service;
    }

    @GetMapping("/dashboard-arrete")
    public DashboardArrete getDashboard(
            @RequestParam String dateDebutArrete,
            @RequestParam String dateFinArrete
    ) {
        Date debut = DateFormatUtil.parseDate(dateDebutArrete);
        Date fin = DateFormatUtil.parseDate(dateFinArrete);
        return dashboardArreteService.getDashboardByPeriode(debut, fin);
    }

    @GetMapping("/repartition-agios")
    public ResponseEntity<List<Map<String, Object>>> getRepartitionAgios(
            @RequestParam String dateDebutArrete,
            @RequestParam String dateFinArrete
    ) {
        Date debut = DateFormatUtil.parseDate(dateDebutArrete);
        Date fin = DateFormatUtil.parseDate(dateFinArrete);
        List<Map<String, Object>> repartition = dashboardArreteService.getRepartitionAgios(debut, fin);
        return ResponseEntity.ok(repartition);
    }


}