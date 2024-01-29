package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable= false) // 대회 이름
    private String contestName;

    @Column(name = "Authority", nullable = false) // 대회 참여 권한
    private Long enterAuthority; // 1 - 부원, 2 - 전체

    @Column(nullable= false) // 시작일
    private Timestamp startTime;

    @Column(nullable= false) // 종료일
    private Timestamp endTime;

    @Lob
    @Column(nullable = false) // 문제 해설 pdf 파일
    private byte[] solution;

    // 외래키로 사용하는 경우
    @OneToMany(mappedBy = "contest")
    private List<Problem> problems;

    @OneToMany(mappedBy = "contest")
    private List<ContestantContest> contestantContests;

    @OneToMany(mappedBy = "contest")
    private List<contestNotice> contestNotices;

    @OneToMany(mappedBy = "contest")
    private List<examiner> examiner;



}
