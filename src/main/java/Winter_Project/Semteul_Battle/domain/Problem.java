package Winter_Project.Semteul_Battle.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false) // 이게 문제의 고유 번호 ex) 12345번
    private Long id;

    @Column(name = "number", nullable = false) // 문제 번호
    private String number;

    @Column(name = "title", nullable = false) // 문제 제목
    private String title;

    @Column(nullable = false) // 문제 내용
    private String content;

    @Column(nullable = false) // 입력 내용
    private String input;

    @Column(nullable = false) // 출력 설명
    private String output;

    @Lob // 문제 사진
    private byte[] pic;

    @Column(nullable = false) // 제한 시간
    private String timeLimit;

//    private // 코드

    private int score; // 문제 배점

    // 외래키로 사용하는 경우

    @OneToMany(mappedBy = "problem") // - 제출 테이블에서 참조해감
    private List<Submit> submits;

    @OneToMany(mappedBy = "problem") // - 입출력 테이블에서 참조해감
    private List<IO> ios;

    // 대회 기본키
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
}