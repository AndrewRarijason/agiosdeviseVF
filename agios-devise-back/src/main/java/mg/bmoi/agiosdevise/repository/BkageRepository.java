package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.Bkage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BkageRepository extends JpaRepository<Bkage, String> {
    @Override
    Optional<Bkage> findById(String s);
}
