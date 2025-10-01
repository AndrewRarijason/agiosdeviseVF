package mg.bmoi.agiosdevise.repository;


import mg.bmoi.agiosdevise.entity.TicDevise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicDeviseRepository extends JpaRepository<TicDevise, String> {
    Optional<TicDevise> findByCdAlpha(String cdAlpha);
    Optional<TicDevise> findByCdIso(String cdIso);
}
