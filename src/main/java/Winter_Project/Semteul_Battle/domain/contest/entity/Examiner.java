package Winter_Project.Semteul_Battle.domain.contest.entity;


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
public class Examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @NotNull(message = "출제자는 필수입니다.")
    private Users users;


    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @NotNull(message = "대회 정보는 필수입니다.")
    private Contest contest;
}
