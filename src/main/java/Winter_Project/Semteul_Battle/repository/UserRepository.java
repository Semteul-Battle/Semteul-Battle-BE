package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLoginId(String loginId);

    Optional<Users> findByEmail(String email);

    boolean existsByLoginId(String loginId);
}