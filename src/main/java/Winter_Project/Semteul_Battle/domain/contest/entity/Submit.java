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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class Submit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;
    @Column(nullable= false)
    @NotBlank(message = "언어는 필수입니다.")
    private String language;

    @Column(nullable= false)
    @NotNull(message = "실행 시간은 필수입니다.")
    private Long runtime;

    @Column(nullable= false)
    @NotNull(message = "메모리 제한은 필수입니다.")
    private Long memoLimit;

    @Column(nullable= false)
    @NotBlank(message = "코드는 필수입니다.")
    private String code;

    @Column(nullable= false)
    @NotNull(message = "제출 시간은 필수입니다.")
    private Timestamp time;

    @Column(nullable =true)
    private Long result;



    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;


    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;


    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

}
