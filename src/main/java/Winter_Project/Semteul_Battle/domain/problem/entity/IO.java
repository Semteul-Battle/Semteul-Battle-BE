package Winter_Project.Semteul_Battle.domain.problem.entity;


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
public class IO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false) // ?????????獄????癲????????????? ???????ex) 12345??
private Long id;

    @Column(nullable = true) // ???????쇨덫櫻????????댄뱼??
private String input;

    @Column(nullable = true) // ?????????????????댄뱼??
private String output;

    // ???癲?????????????
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

}
