package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Examiner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminerRepository extends JpaRepository<Examiner, Long> {
    void deleteByContest_Id(Long contestId);
    boolean existsByUsers_LoginIdAndContest_Id(String loginId, Long contestId);
}