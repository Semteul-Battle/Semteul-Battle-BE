package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByContest_Id(Long contestId);
}
