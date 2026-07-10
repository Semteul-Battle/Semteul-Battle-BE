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

    @Column(name = "name", nullable = false) // ?????????
private String contestName;

    @Column(name = "Authority", nullable = false) // ?????耀붾굝????????????????
private Long enterAuthority; // 1 - ?????堉온??? 2 - ?????獄쏅챶留??
    @Column(nullable = false) // ???轅붽틓???壤굿??걜??
private Timestamp startTime;

    @Column(nullable = false) // ???????욱떌???
private Timestamp endTime;

    @Column(nullable = true)
    private String simpleInfo; // ?????ル뒌??????饔낅떽????????
    @Column(nullable = true)
    private String problemInfo; // ???癲??????????筌뤾쑴留?
    @Column(nullable = true)
    private Long contestHost; // 0 - ????釉랁닑???롪퍓媛???????????????, 1 - ????釉랁닑???롪퍓媛??????????????, 2 - ?????獄쏅챶留?????????????????
    @Column(nullable = true) // ???癲???????????욱뒅??pdf ?????
private byte[] solution;

    // ??饔낅떽?????????????????黎앸럽????룸돥??????汝뷴젆?琉????
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
