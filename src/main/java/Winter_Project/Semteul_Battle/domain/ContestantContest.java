package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestantContest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // 다대일 관계
    @ManyToOne
    @JoinColumn(name = "contestant_id", nullable = false)
    private Contestant contestant;

    // 다대일 관계
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
}
