package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.DTO.CompteSoldeDto;
import mg.bmoi.agiosdevise.repository.CompteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompteService {

    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public List<CompteSoldeDto> getComptesWithSoldes(String date) {
        List<Object[]> results = compteRepository.findComptesWithSoldes(date);

        return results.stream()
                .map(row -> new CompteSoldeDto(
                        (String) row[0],  // age
                        (String) row[1],  // dev
                        (String) row[2],  // ncp
                        (BigDecimal) row[3]  // sde
                ))
                .collect(Collectors.toList());
    }
}