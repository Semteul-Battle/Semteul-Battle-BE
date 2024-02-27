package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.ProblemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemImageRepository extends JpaRepository<ProblemImage, Long> {
}
