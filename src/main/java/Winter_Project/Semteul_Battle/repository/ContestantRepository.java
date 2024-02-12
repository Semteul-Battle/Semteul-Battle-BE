package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {
    Optional<Contestant> findByIdInAndUsers_Id(List<Long> contestantIds, Long userId);

//    List<Contestant> findByContestId(Long contestId);


}
