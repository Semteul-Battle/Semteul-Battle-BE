package Winter_Project.Semteul_Battle.domain.contest.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Timestamp questionTime;
    @Column(nullable = true)
    private String answer;
    @Column(nullable = true)
    private Timestamp answerTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private Users questioner;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "answerer_id", nullable = true)
    private Users answerer;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contestId;

    public Users getAnswerer() {
        return this.answerer;
}

}
