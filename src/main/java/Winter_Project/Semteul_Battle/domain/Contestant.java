package Winter_Project.Semteul_Battle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Contestant", schema = "Semteul_Battle")
public class Contestant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // 공지사항 체크
    @Column(nullable = true)
    private boolean isChecked;

    // 유저 기본키
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    // 대회 기본키
    @JsonIgnore
    @OneToMany(mappedBy = "contestant")
    private List<ContestantContest> contestantContests;
}
