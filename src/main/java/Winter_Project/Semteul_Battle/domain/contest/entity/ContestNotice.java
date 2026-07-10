package Winter_Project.Semteul_Battle.domain.contest.entity;



import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Timestamp time;

    // ?????????????
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    // ??? ?????????
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;




}
