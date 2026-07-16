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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "질문 제목은 필수입니다.")
    private String question;

    @Column(nullable = false)
    @NotBlank(message = "질문 내용은 필수입니다.")
    private String content;

    @Column(nullable = false)
    @NotNull(message = "질문 시간은 필수입니다.")
    private Timestamp questionTime;
    @Column(nullable = true)
    private String answer;
    @Column(nullable = true)
    private Timestamp answerTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questioner_id")
    private Users questioner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answerer_id", nullable = true)
    private Users answerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contestId;

    public void registerQuestion(String question, String content, Timestamp questionTime) {
        this.question = question;
        this.content = content;
        this.questionTime = questionTime;
    }

    public void assignQuestioner(Users questioner) {
        this.questioner = questioner;
    }

    public void assignContest(Contest contest) {
        this.contestId = contest;
    }

    public void answer(Users answerer, String answer, Timestamp answerTime) {
        this.answerer = answerer;
        this.answer = answer;
        this.answerTime = answerTime;
    }

}
