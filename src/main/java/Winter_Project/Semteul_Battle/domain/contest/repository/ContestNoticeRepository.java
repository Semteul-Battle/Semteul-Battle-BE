package Winter_Project.Semteul_Battle.domain.contest.repository;

import Winter_Project.Semteul_Battle.domain.contest.entity.ContestNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestNoticeRepository extends JpaRepository<ContestNotice, Long> {
    List<ContestNotice> findByContest_Id(Long contestId);
}
