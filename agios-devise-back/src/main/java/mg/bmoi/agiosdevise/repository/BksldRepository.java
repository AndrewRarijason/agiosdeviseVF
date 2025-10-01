package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bksld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface BksldRepository extends JpaRepository<Bksld, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM bank.bksld WHERE ncp = :ncp AND dco >= :startDate AND dco <= :endDate")
    List<Bksld> findByNcpAndDateRange(
            @Param("ncp") String ncp,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}