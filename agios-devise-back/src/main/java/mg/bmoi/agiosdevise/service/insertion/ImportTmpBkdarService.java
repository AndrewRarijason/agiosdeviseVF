package mg.bmoi.agiosdevise.service.insertion;

import mg.bmoi.agiosdevise.DTO.ExtractionBkdarDto;
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
public class ImportTmpBkdarService {
    private final OracleConnectionManager conn;
    private static final Logger logger = LoggerFactory.getLogger(ImportTmpBkdarService.class);

    public ImportTmpBkdarService(OracleConnectionManager conn) {
        this.conn = conn;
    }

    public void insertIntoTmpBkdar(List<ExtractionBkdarDto> data) {
        String deleteSql = "DELETE FROM C##MGSTGADR.BKDAR_D";
        String sql = "INSERT INTO C##MGSTGADR.BKDAR_D (AGE, DEV, NCP, NBC, TXC, NBR, TAUX, SOLDE, NOMREST, ADR1, ADR2, CPOS, CLC, DATR, TCLI, VIL, CLI) VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = conn.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false); // Désactive l'auto-commit

            // Nettoyage de la table
            try (PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
                deletePs.executeUpdate();
                logger.info("Table C##MGSTGADR.BKDAR_D nettoyée");
            }

            for (ExtractionBkdarDto dto : data) {
                ps.setString(1, dto.getAgeBkdar());
                ps.setString(2, dto.getDev());
                ps.setString(3, dto.getNcp());
                ps.setBigDecimal(4, dto.getNbc());
                ps.setBigDecimal(5, dto.getTxc());
                ps.setBigDecimal(6, dto.getNbr());
                ps.setBigDecimal(7, dto.getTaux());
                ps.setBigDecimal(8, dto.getSolde());
                ps.setString(9, dto.getNomrest());
                ps.setString(10, dto.getAdr1());
                ps.setString(11, dto.getAdr2());
                ps.setString(12, dto.getCpos());
                ps.setString(13, dto.getClc());
                ps.setDate(14, dto.getDatr() != null ? new java.sql.Date(dto.getDatr().getTime()) : null);
                ps.setString(15, dto.getTcli() != null ? dto.getTcli().toString() : null);
                ps.setString(16, dto.getVil());
                ps.setString(17, dto.getCli());

                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            connection.commit();
            logger.info("Lignes insérées dans C##MGSTGADR.BKDAR_D : {}", Arrays.stream(results).sum());


        } catch (SQLException e) {
            throw  new RuntimeException("Echec de l'insertion dans C##MGSTGADR.BKDAR_D", e);
        }

    }

}
