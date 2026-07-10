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
public class MenuQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // ?耀붾굝??????????壤????轅붽틓?????룰퀣維??
    @Column(nullable = false)
private String title;

    // ?耀붾굝??????????壤????????ㅻ쑄??
    @Column(nullable = false)
private String content;

    // ?耀붾굝??????????壤?????????????
    @Column(nullable = false)
private Timestamp time;

    // ???????
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;
}