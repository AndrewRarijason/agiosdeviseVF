package mg.bmoi.agiosdevise.service.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBksldDto;
import mg.bmoi.agiosdevise.base.OracleConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImportTmpBksldService {
    private final OracleConnectionManager oracleConnectionManager;
    private static final Logger logger = LoggerFactory.getLogger(ImportTmpBksldService.class);

    public ImportTmpBksldService(OracleConnectionManager oracleConnectionManager) {
        this.oracleConnectionManager = oracleConnectionManager;
    }


    public void insertIntoTmpBksld(List<ExtractionBksldDto> data) {
        String deleteSql = "DELETE FROM C##MGSTGADR.BKSLD_D";
        String sql = "INSERT INTO C##MGSTGADR.BKSLD_D (AGE, DEV, NCP, SDE) VALUES (?, ?, ?, ?)";
        try (Connection conn = oracleConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Désactive l’auto-commit

            // Nettoyage de la table
            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.executeUpdate();
                logger.info("Table C##MGSTGADR.BKSLD_D nettoyée");
            }

            for (ExtractionBksldDto dto : data) {
                ps.setString(1, dto.getAge());
                ps.setString(2, dto.getDev());
                ps.setString(3, dto.getNcp());
                ps.setBigDecimal(4, dto.getSde());
                ps.addBatch();
            }

            int[] results = ps.executeBatch(); // Auto-commit si activé
            conn.commit();
            logger.info("Lignes insérées : {}", Arrays.stream(results).sum());



        } catch (SQLException e) {
            throw new RuntimeException("Échec de l'insertion", e);
        }
    }
}
