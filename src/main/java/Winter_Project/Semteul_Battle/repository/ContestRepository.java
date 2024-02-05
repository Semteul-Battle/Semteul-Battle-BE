package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.Contest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Page<Contest> findByStartTimeBeforeAndEndTimeAfter(Timestamp startTime, Timestamp endTime, Pageable pageable);

    Page<Contest> findByStartTimeAfter(Timestamp startTime, Pageable pageable);

    Page<Contest> findByEndTimeBefore(Timestamp endTime, Pageable pageable);
}