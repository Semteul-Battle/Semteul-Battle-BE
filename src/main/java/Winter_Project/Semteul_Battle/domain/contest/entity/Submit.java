package Winter_Project.Semteul_Battle.domain.contest.entity;



import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
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
    @Column(nullable= false)
private String language;

    @Column(nullable= false)
private Long runtime;

    @Column(nullable= false)
private Long memoLimit;

    @Column(nullable= false)
private String code;

    @Column(nullable= false)
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
