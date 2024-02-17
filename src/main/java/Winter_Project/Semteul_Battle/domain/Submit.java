package Winter_Project.Semteul_Battle.domain;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable= false) // 언어
    private String language;

    @Column(nullable= false) // 실행 시간
    private Long runtime;

    @Column(nullable= false) // 메모리 제한
    private Long memoLimit;

    @Column(nullable= false) // 코드
    private String code;

    @Column(nullable= false) // 제출 시간
    private Timestamp time;

    @Column(nullable= false) // 결과
    private Long result;

    // 외래키로 사용하는 경우

    // 유저 기본키
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    // 문제 기본키
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    // 대회 기본키
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

}