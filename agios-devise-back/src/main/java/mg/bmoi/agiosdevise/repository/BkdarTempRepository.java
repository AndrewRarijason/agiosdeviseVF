package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.BkdarTemp;
import mg.bmoi.agiosdevise.entity.identifiants.BkdarTempId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BkdarTempRepository extends JpaRepository<BkdarTemp, BkdarTempId> {
    boolean existsByIdNcpAndIdDev(String ncp, String dev);
    @Query(
            value = "SELECT * FROM C##MGSTGADR.BKDAR_D WHERE NCP = :ncp ORDER BY DATR DESC FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true
    )
    Optional<BkdarTemp> findByIdNcp(@Param("ncp") String ncp);

    @Query("SELECT b.taux FROM BkdarTemp b WHERE b.id.ncp = :ncp AND b.id.dev = :dev")
    List<BigDecimal> findTauxListByNcpAndDev(@Param("ncp") String ncp, @Param("dev") String dev);
}
