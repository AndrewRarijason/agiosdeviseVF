package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bkhis;
import mg.bmoi.agiosdevise.entity.identifiants.BkhisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtractionBkhisRepository extends JpaRepository<Bkhis, BkhisId> {
    @Query (
            nativeQuery = true,
            value = "SELECT a.age AS age_bkhis, a.dev, a.ncp, a.dco, a.dva, a.mon, a.sen " +
                    "FROM bank.bkhis a " +
                    "full join bank.bkcom b on a.age=b.age and a.dev=b.dev and a.ncp=b.ncp " +
                    "where a.dva between :dateref " +
                    "and :datefin " +
                    "and b.cha = '211000' " +
                    "and b.cpro = '202' " +
                    "and a.dev not like '969' " +
                    "order by a.dva "
    )
    List<Object[]> findBkhisExtraction(@Param("dateref") String dateRef, @Param("datefin") String dateFin);
}
