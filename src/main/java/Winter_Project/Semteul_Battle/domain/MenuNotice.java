package Winter_Project.Semteul_Battle.domain;

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
public class MenuNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    // 공지 제목
    @Column(nullable = true)
    private String title;

    // 공지 내용
    @Column(nullable = true)
    private String content;

    // 작성 시간
    @Column(nullable = true)
    private Timestamp time;

    // 작성자
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;
}