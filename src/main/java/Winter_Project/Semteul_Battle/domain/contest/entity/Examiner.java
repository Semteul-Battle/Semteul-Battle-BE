package Winter_Project.Semteul_Battle.domain.contest.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // ??? ?????????
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    // ?????????????
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
}