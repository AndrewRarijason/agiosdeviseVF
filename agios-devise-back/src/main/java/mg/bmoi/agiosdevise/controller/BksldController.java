package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.entity.Bksld;
import mg.bmoi.agiosdevise.repository.BksldRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

@RestController
public class BksldController {

    private final BksldRepository bksldRepository;

    public BksldController(BksldRepository bksldRepository) {
        this.bksldRepository = bksldRepository;
    }

    @GetMapping("/api/bksld")
    public List<Bksld> getBksldByNcpAndDateRange(
            @RequestParam String ncp,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate) {

        return bksldRepository.findByNcpAndDateRange(ncp, startDate, endDate);
    }
}