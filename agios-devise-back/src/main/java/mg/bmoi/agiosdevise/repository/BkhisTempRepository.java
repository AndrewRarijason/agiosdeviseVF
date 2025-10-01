package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bkhis;
import mg.bmoi.agiosdevise.entity.BkhisTemp;
import mg.bmoi.agiosdevise.entity.identifiants.BkhisId;
import mg.bmoi.agiosdevise.entity.identifiants.BkhisTempId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BkhisTempRepository extends JpaRepository<BkhisTemp, BkhisTempId> {

    List<BkhisTemp> findByIdNcpAndIdDvaAfterOrderByIdDvaAsc(String ncp, Date dva);
}
