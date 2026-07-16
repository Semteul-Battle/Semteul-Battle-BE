package Winter_Project.Semteul_Battle.domain.menu.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;


    @Column(nullable = false)
    @NotBlank(message = "질문 제목은 필수입니다.")
    private String title;


    @Column(nullable = false)
    @NotBlank(message = "질문 내용은 필수입니다.")
    private String content;


    @Column(nullable = false)
    @NotNull(message = "질문 시간은 필수입니다.")
    private Timestamp time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    public void updateQuestion(String title, String content, Timestamp time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }
}
