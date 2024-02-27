package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.ContestantContest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestantContestRepository extends JpaRepository<ContestantContest,Long> {
    List<ContestantContest> findContestantContestByContest_Id(Long contestId);
    List<ContestantContest> findByContestId(Long contestId);
    List<ContestantContest> findByContestant_Id(Long contestantIds);
}