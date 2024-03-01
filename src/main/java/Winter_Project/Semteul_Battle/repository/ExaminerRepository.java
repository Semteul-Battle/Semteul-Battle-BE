package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Examiner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExaminerRepository extends JpaRepository<Examiner, Long> {
    void deleteByContest_Id(Long contestId);
    boolean existsByUsers_LoginIdAndContest_Id(String loginId, Long contestId);
    List<Examiner> findByContest_Id(Long contestId);
    @Query("SELECT e FROM Examiner e WHERE e.contest IN :contests")
    List<Examiner> findAllExaminerIdsByContestIn(List<Contest> contests);
}