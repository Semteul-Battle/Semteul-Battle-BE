package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Contestant", schema = "Semteul_Battle")
public class Contestant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private boolean isChecked;

    // 유저 기본키
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    // 대회 기본키
    @OneToMany(mappedBy = "contestant")
    private List<ContestantContest> contestantContests;

}
