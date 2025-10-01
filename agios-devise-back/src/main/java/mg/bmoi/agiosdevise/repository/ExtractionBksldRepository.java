package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bkcom;
import mg.bmoi.agiosdevise.entity.identifiants.BkcomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtractionBksldRepository extends JpaRepository<Bkcom, BkcomId> {

    @Query(nativeQuery = true, value = "SELECT a.age, a.dev, a.ncp, b.sde FROM bank.bkcom a " +
            "FULL JOIN bank.bksld b ON a.age = b.age " +
            "AND a.dev = b.dev " +
            "AND a.ncp = b.ncp " +
            "AND b.dco = TO_DATE(:date, 'DD-MM-YYYY') " +
            "WHERE a.dev NOT LIKE '969' " +
            "AND a.cha = '211000' " +
            "AND a.cpro = '202' ")
    List<Object[]> findComptesWithSoldes(@Param("date") String date);
}