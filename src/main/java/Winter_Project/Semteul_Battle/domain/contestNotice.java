package Winter_Project.Semteul_Battle.domain;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class contestNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Timestamp time;

    // 대회 기본키
    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    // 유저 기본키
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;



}
