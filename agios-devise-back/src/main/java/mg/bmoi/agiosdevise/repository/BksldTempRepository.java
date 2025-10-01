package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.BksldTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BksldTempRepository extends JpaRepository<BksldTemp, String> {
    Optional<BksldTemp> findByNcp(String ncp);
}
