package Winter_Project.Semteul_Battle.domain;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private Users questioner;

    // 유저(답변자) 아이디
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "answerer_id", nullable = true)
    private Users answerer;

    // 대회 아이디
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contestId;

    public Users getAnswerer() {
        return this.answerer != null ? this.answerer : new Users(); // 또는 null을 대신할 다른 사용자를 리턴
    }

}
