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
    private String question;

    @Column(nullable = false)
    private Timestamp questionTime;

    @Column(nullable = true)
    private String answer;

    @Column(nullable = true)
    private Timestamp answerTime;

    // 유저(질문자) 아이디
    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private Users questioner;

    // 유저(답변자) 아이디
    @ManyToOne
    @JoinColumn(name = "answerer_id")
    private Users answerer;

    // 대회 아이디


}
