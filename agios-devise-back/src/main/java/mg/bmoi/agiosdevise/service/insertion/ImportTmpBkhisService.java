package mg.bmoi.agiosdevise.service.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBkhisDto;
import mg.bmoi.agiosdevise.base.OracleConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImportTmpBkhisService {

    private final OracleConnectionManager oracleConnectionManager;
    private static final Logger logger = LoggerFactory.getLogger(ImportTmpBkhisService.class);

    public ImportTmpBkhisService(OracleConnectionManager oracleConnectionManager) {
        this.oracleConnectionManager = oracleConnectionManager;
    }

    public void insertIntoTmpBkhis(List<ExtractionBkhisDto> data) {
        String deleteSql = "DELETE FROM C##MGSTGADR.BKHIS_D";
        String sql = "INSERT INTO C##MGSTGADR.BKHIS_D (AGE, DEV,  NCP, DCO, DVA, MON, SEN) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = oracleConnectionManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            c.setAutoCommit(false); // Désactive l’auto-commit

            // Nettoyage de la table
            try (PreparedStatement deletePs = c.prepareStatement(deleteSql)) {
                deletePs.executeUpdate();
                logger.info("Table C##MGSTGADR.BKHIS_D nettoyée");
            }

            for (ExtractionBkhisDto dto : data) {
                ps.setString(1, dto.getAge());
                ps.setString(2, dto.getDev());
                ps.setString(3, dto.getNcp());
                ps.setDate(4, dto.getDco() != null ? new java.sql.Date(dto.getDco().getTime()) : null);
                ps.setDate(5, dto.getDva() != null ? new java.sql.Date(dto.getDva().getTime()) : null);
                ps.setBigDecimal(6, dto.getMon());
                ps.setString(7, dto.getSen() != null ? dto.getSen().toString() : null);
                ps.addBatch();
            }
            int[] results = ps.executeBatch(); // Auto-commit si activé
            c.commit();
            logger.info("Lignes insérées : {}", Arrays.stream(results).sum());


        } catch (SQLException e) {
            logger.error("Erreur SQL lors de l'insertion : {}", e.getMessage(), e);
            throw new RuntimeException("Échec de l'insertion : " + e.getMessage(), e);
        }
    }
}
