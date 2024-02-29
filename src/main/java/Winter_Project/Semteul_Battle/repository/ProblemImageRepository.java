package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.ProblemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemImageRepository extends JpaRepository<ProblemImage, Long> {
    List<ProblemImage> findByProblemId(Long problemId);
}
