package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bkdar;
import mg.bmoi.agiosdevise.entity.identifiants.BkdarId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExtractionBkdarRepository extends JpaRepository<Bkdar, BkdarId> {
    @Query(
            nativeQuery = true,
            value = "SELECT a.age, a.dev, a.ncp, b.nbc, b.txc, c.nbr, c.taux, a.solde, " +
                    "e.nomrest, e.adr1, e.adr2, e.cpos, f.clc, c.datr, d.tcli, e.vil, a.cli " +
                    "from bank.bkdar a " +
                    "left join bank.bkdarc b on b.age=a.age and b.dev=a.dev and b.ncp=a.ncp and b.annee=a.annee and b.mois=a.mois " +
                    "left join bank.bkdard c on c.age=a.age and c.dev=a.dev and c.ncp=a.ncp and c.annee=a.annee and c.mois=a.mois " +
                    "left join bank.bkcli d on d.cli=a.cli " +
                    "full join bank.xextcli e on e.cli = a.cli " +
                    "full join bank.bkcom f on f.age=a.age and f.dev=a.dev and f.ncp=a.ncp " +
                    "where a.annee = :annee " +
                    "and a.mois= :mois " +
                    "and a.dev not like '969' " +
                    "and f.cha='211000' " +
                    "and f.cpro='202' " +
                    "and f.cfe='N' " +
                    "order by a.ncp "
    )
    List<Object[]> findBkdarExtraction(@Param("annee") String annee, @Param("mois") String mois);
}