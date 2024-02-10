package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.ContestantContest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ContestantContestRepository extends JpaRepository<ContestantContest,Long> {
    List<ContestantContest> findContestantContestByContest_Id(Long contestId);
}