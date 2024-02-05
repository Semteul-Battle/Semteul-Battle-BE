package Winter_Project.Semteul_Battle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(nullable = true)
    private Long contestHost; // 0 - 미완(어드민), 1 - 미완(출제자), 2 - 완성(일반 사용자)
    @Lob
    @Column(nullable = true, columnDefinition = "BLOB") // 문제 해설 pdf 파일
    private byte[] solution;

    // 외래키로 사용하는 경우
    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<Problem> problems;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestantContest> contestantContests;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<contestNotice> contestNotices;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<Examiner> examiner;

    public Contest(Long id) {
        this.id = id;
    }
}