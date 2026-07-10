package Winter_Project.Semteul_Battle.domain.problem.repository;

import Winter_Project.Semteul_Battle.domain.problem.entity.ProblemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemImageRepository extends JpaRepository<ProblemImage, Long> {
    List<ProblemImage> findByProblemId(Long problemId);
}
