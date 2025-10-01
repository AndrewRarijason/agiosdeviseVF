package mg.bmoi.agiosdevise.controller;

import mg.bmoi.agiosdevise.DTO.CompteSoldeDto;
import mg.bmoi.agiosdevise.service.CompteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping("/soldes")
    public ResponseEntity<List<CompteSoldeDto>> getSoldesByDate(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") String date) {

        List<CompteSoldeDto> result = compteService.getComptesWithSoldes(date);
        return ResponseEntity.ok(result);
    }
}