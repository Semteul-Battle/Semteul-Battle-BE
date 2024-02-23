package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Submit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmitRepository extends JpaRepository<Submit, Long> {
    List<Submit> findByContest_Id(Long contestId);
    Page<Submit> findByContestId(Long contestId, Pageable pageable);
}
