package Winter_Project.Semteul_Battle.domain.menu.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // ??? ???????ㅻ쑄??
    @Column(nullable = true)
private String content;

    // ????????????
    @Column(nullable = true)
private Timestamp time;

    // ???????
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private MenuQuestion menuQuestion;
}
