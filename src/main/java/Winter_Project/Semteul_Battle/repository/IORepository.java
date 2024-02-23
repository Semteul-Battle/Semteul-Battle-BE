package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.IO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IORepository extends JpaRepository<IO, Long> {
    Optional<IO> findById(Long IOId);
}
