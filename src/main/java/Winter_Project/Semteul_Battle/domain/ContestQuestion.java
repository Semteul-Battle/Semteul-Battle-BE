package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ContestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String question; // 질문 제목

    @Column(nullable = false)
    private String content; // 질문 내용

    @Column(nullable = false)
    private Timestamp questionTime; // 질문 시간

    @Column(nullable = true)
    private String answer; // 질문 대답

    @Column(nullable = true)
    private Timestamp answerTime; // 질문 대답 시간

    // 유저(질문자) 아이디
    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private Users questioner;

    // 유저(답변자) 아이디
    @ManyToOne
    @JoinColumn(name = "answerer_id")
    private Users answerer;

    // 대회 아이디
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contestId;

}
