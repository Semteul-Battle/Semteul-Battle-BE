package Winter_Project.Semteul_Battle.domain.contest.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
private String contestName;

    @Column(name = "Authority", nullable = false)
private Long enterAuthority;
    @Column(nullable = false)
private Timestamp startTime;

    @Column(nullable = false)
private Timestamp endTime;

    @Column(nullable = true)
    private String simpleInfo;
    @Column(nullable = true)
    private String problemInfo;
    @Column(nullable = true)
    private Long contestHost;
    @Column(nullable = true)
private byte[] solution;


    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<Problem> problems;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestantContest> contestantContests;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<ContestNotice> ContestNotices;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<Examiner> examiner;

    @JsonIgnore
    @OneToMany(mappedBy = "contestId")
    private List<ContestQuestion> contestQuestions;

    @JsonIgnore
    @OneToMany(mappedBy = "contest")
    private List<Submit> submits;

    public Contest(Long id) {
        this.id = id;
    }
}
