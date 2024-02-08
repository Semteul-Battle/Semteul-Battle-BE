package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.ContestantContest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantContestRepository extends JpaRepository<ContestantContest,Long> {
}
