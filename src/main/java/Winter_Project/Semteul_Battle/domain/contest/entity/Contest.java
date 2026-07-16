package Winter_Project.Semteul_Battle.domain.contest.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "대회 이름은 필수입니다.")
    private String contestName;

    @Column(name = "Authority", nullable = false)
    @NotNull(message = "대회 입장 권한은 필수입니다.")
    private Long enterAuthority;

    @Column(nullable = false)
    @NotNull(message = "대회 시작 시간은 필수입니다.")
    private Timestamp startTime;

    @Column(nullable = false)
    @NotNull(message = "대회 종료 시간은 필수입니다.")
    private Timestamp endTime;

    @Column(nullable = true)
    private String simpleInfo;
    @Column(nullable = true)
    private String problemInfo;
    @Column(nullable = true)
    private Long contestHost;
    @Column(nullable = true)
    private byte[] solution;


    @OneToMany(mappedBy = "contest")
    private List<Problem> problems;

    @OneToMany(mappedBy = "contest")
    private List<ContestantContest> contestantContests;

    @OneToMany(mappedBy = "contest")
    private List<ContestNotice> ContestNotices;

    @OneToMany(mappedBy = "contest")
    private List<Examiner> examiner;

    @OneToMany(mappedBy = "contestId")
    private List<ContestQuestion> contestQuestions;

    @OneToMany(mappedBy = "contest")
    private List<Submit> submits;

    public Contest(Long id) {
        this.id = id;
    }
}
