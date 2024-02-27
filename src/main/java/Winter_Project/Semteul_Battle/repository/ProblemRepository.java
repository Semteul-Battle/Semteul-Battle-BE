package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByContest_Id(Long contestId);
    Optional<Problem> findById(Long problemId);
    void deleteByContest_Id(Long contestId);

}
