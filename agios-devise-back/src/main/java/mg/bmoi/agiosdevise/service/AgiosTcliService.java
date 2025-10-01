package mg.bmoi.agiosdevise.service;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class AgiosTcliService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getSumAgiosParTcli(Date dateDebut, Date dateFin) {
        String sql = "SELECT TCLI, DEV, DATE_DEBUT_ARRETE, DATE_FIN_ARRETE, CDALPHA, LIBELE, TOTAL_NET_AGIOS " +
                "FROM C##MGSTGADR.V_SUM_AGIOS_PAR_TCLI " +
                "WHERE DATE_DEBUT_ARRETE = :dateDebut " +
                "AND DATE_FIN_ARRETE = :dateFin " +
                "AND DEV IN ( " +
                "    SELECT DEV " +
                "    FROM C##MGSTGADR.V_SUM_AGIOS_PAR_TCLI " +
                "    WHERE DATE_DEBUT_ARRETE = :dateDebut " +
                "    AND DATE_FIN_ARRETE = :dateFin " +
                "    GROUP BY DEV " +
                "    ORDER BY SUM(TOTAL_NET_AGIOS) DESC " +
                "    FETCH FIRST 3 ROWS ONLY " +
                ") " +
                "ORDER BY DEV, TOTAL_NET_AGIOS DESC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateDebut", dateDebut);
        query.setParameter("dateFin", dateFin);

        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> agiosList = new ArrayList<>();

        for (Object[] row : results) {
            int tcli = Integer.parseInt(row[0].toString());
            String dev = row[1] != null ? row[1].toString() : null;
            String dateDebutArrete = row[2] != null ? row[2].toString() : null;
            String dateFinArrete = row[3] != null ? row[3].toString() : null;
            String cdalpha = row[4] != null ? row[4].toString() : null;
            String libele = row[5] != null ? row[5].toString() : null;
            Object totalNetAgios = row[6];

            String typeClient;
            switch (tcli) {
                case 1: typeClient = "Client particulier"; break;
                case 2: typeClient = "Société"; break;
                case 3: typeClient = "Entreprise individuelle"; break;
                default: typeClient = "Inconnu";
            }

            Map<String, Object> map = new HashMap<>();
            map.put("tcli", tcli);
            map.put("dev", dev);
            map.put("cdalpha", cdalpha);
            map.put("libele", libele);
            map.put("type_client", typeClient);
            map.put("dateDebutArrete", dateDebutArrete);
            map.put("dateFinArrete", dateFinArrete);
            map.put("totalNetAgios", totalNetAgios);
            agiosList.add(map);
        }

        return agiosList;
    }
}