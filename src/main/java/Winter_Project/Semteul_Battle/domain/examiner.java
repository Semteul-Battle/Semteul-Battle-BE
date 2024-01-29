package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class examiner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // 유저 기본키
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    // 대회 기본키
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;


}
