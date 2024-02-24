package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class IO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false) // 이게 문제의 고유 번호 ex) 12345번
    private Long id;

    @Column(nullable = true) // 입력 예시
    private String input;

    @Column(nullable = true) // 출력 예시
    private String output;

    // 문제 기본키
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

}
