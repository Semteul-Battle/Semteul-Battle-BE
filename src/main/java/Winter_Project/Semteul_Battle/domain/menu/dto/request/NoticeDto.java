package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
    @NotBlank(message = "공지 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "공지 내용을 입력해주세요.")
    private String content;

    private Timestamp time;
    private Users users;
}
