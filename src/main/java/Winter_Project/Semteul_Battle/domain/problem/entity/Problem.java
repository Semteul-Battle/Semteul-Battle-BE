package Winter_Project.Semteul_Battle.domain.problem.entity;



import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(name = "number", nullable = true)
    private String number;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private String input;

    @Column(nullable = true)
    private String output;

    @Column(nullable = true)
    private String timeLimit;

    private int score;

    @OneToMany(mappedBy = "problem")
    private List<Submit> submits;

    @OneToMany(mappedBy = "problem")
    private List<IO> ios;

    @OneToMany(mappedBy = "problem")
    private List<ProblemImage> images;


    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = true)
    private Contest contest;

    public void updateProblem(String title, String content, String input, String output, String timeLimit, int score) {
        this.title = title;
        this.content = content;
        this.input = input;
        this.output = output;
        this.timeLimit = timeLimit;
        this.score = score;
    }
}
