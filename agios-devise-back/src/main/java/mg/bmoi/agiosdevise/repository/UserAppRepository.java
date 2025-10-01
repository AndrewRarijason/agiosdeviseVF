package mg.bmoi.agiosdevise.repository;

import mg.bmoi.agiosdevise.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Integer> {
    Optional<UserApp> findByUsername(String username);
}