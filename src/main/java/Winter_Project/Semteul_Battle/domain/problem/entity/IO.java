package Winter_Project.Semteul_Battle.domain.problem.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class IO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = true)
    private String input;

    @Column(nullable = true)
    private String output;


    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    @NotNull(message = "입출력 대상 문제는 필수입니다.")
    private Problem problem;

    public void updateIo(String input, String output) {
        this.input = input;
        this.output = output;
    }
}
