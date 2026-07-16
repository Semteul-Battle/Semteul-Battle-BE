package Winter_Project.Semteul_Battle.domain.menu.dto.response;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class MenuCommentResponseDto {
    private Long id;
    private String content;
    private Timestamp time;
    private Long userId;
    private String userName;
    private Long questionId;

    public static MenuCommentResponseDto from(MenuComment comment) {
        return MenuCommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .time(comment.getTime())
                .userId(comment.getUsers().getId())
                .userName(comment.getUsers().getName())
                .questionId(comment.getMenuQuestion().getId())
                .build();
    }
}
