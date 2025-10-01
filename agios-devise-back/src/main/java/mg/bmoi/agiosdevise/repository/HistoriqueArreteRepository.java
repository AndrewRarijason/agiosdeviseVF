package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.HistoriqueArrete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoriqueArreteRepository extends JpaRepository<HistoriqueArrete, Long> {
    Optional<HistoriqueArrete> findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(
            String ncp, Date dateDebutArrete, Date dateFinArrete
    );
    List<HistoriqueArrete> findAllByIdDateDebutArreteAndIdDateFinArrete(Date dateDebutArrete, Date dateFinArrete);
}