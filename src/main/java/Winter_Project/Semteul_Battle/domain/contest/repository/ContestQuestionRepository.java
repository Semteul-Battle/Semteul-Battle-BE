package Winter_Project.Semteul_Battle.domain.contest.repository;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestQuestionRepository extends JpaRepository<ContestQuestion, Long> {
    List<ContestQuestion> findByContestId(Contest contest);

}