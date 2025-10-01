package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.entity.DashboardArrete;
import mg.bmoi.agiosdevise.repository.DashboardArreteRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class DashboardArreteService {
    private final DashboardArreteRepository dashboardArreteRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DashboardArreteService(DashboardArreteRepository repo) {
        this.dashboardArreteRepository = repo;
    }

    public DashboardArrete getLastDashboard() {
        return dashboardArreteRepository.findAllOrderByDateHeureFinCalculDesc()
                .stream().findFirst().orElse(null);
    }

    public DashboardArrete getDashboardByPeriode(Date dateDebut, Date dateFin) {
        return dashboardArreteRepository.findByPeriode(dateDebut, dateFin)
                .stream().findFirst().orElse(null);
    }

    public List<Map<String, Object>> getRepartitionAgios(Date dateDebut, Date dateFin) {
        String sql = "SELECT DEV, DATE_DEBUT_ARRETE, DATE_FIN_ARRETE, ABS(TOTAL_NET_AGIOS) AS TOTAL_NET_AGIOS, CDALPHA, LIBELE " +
                "FROM C##MGSTGADR.V_HISTO_ARRETE_DEVISE " +
                "WHERE DATE_DEBUT_ARRETE = :dateDebut " +
                "AND DATE_FIN_ARRETE = :dateFin " +
                "ORDER BY ABS(TOTAL_NET_AGIOS) DESC " +
                "FETCH FIRST 3 ROWS ONLY";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);

        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> repartition = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("dev", row[0]);
            map.put("dateDebutArrete", row[1]);
            map.put("dateFinArrete", row[2]);
            map.put("totalNetAgios", row[3]);
            map.put("cdAlpha", row[4]);
            map.put("libele", row[5]);
            repartition.add(map);
        }
        return repartition;
    }
}