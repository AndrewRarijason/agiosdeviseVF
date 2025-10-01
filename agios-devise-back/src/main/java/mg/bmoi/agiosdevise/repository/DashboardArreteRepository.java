package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.DashboardArrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DashboardArreteRepository extends JpaRepository<DashboardArrete, Long> {
    @Query("SELECT d FROM DashboardArrete d ORDER BY d.dateHeureFinCalcul DESC")
    List<DashboardArrete> findAllOrderByDateHeureFinCalculDesc();

    @Query("SELECT d FROM DashboardArrete d WHERE d.dateDebutArrete = :dateDebut AND d.dateFinArrete = :dateFin ORDER BY d.dateHeureFinCalcul DESC")
    List<DashboardArrete> findByPeriode(Date dateDebut, Date dateFin);
}