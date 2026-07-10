package Winter_Project.Semteul_Battle.domain.menu.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.CommentCheckDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final MenuCommentRepository menuCommentRepository;


public MenuComment createComment(CommentDto commentDto, Users users, MenuQuestion menuQuestion) {
        MenuComment comment = MenuComment.builder()
                .content(commentDto.getContent())
                .time(commentDto.getTime())
                .users(users)
                .menuQuestion(menuQuestion)
                .build();
        return menuCommentRepository.save(comment);
    }


public List<MenuComment> getCommentsFromQuestion(Long questionId) {

List<MenuComment> comments = menuCommentRepository.findByMenuQuestion_Id(questionId);
        if(comments.isEmpty()) {
            throw new MenuException(ErrorStatus._NOT_FOUND, "댓글을 찾을 수 없습니다.");
        }
        return comments;
    }


public MenuComment updateComment(CommentUpdateDto commentUpdateDto, String loginId) {
        Long CommentId = commentUpdateDto.getCommentId();

        MenuComment comment = menuCommentRepository.findById(CommentId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!comment.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "댓글을 수정하거나 삭제할 권한이 없습니다.");
        }

        comment.setContent(commentUpdateDto.getContent());
        comment.setTime(commentUpdateDto.getTime());

        return menuCommentRepository.save(comment);
    }

public void deleteComment(CommentDeleteDto commentDeleteDto, String loginId) {
        Long commentId = commentDeleteDto.getCommentId();


MenuComment comment = menuCommentRepository.findById(commentId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "댓글을 찾을 수 없습니다."));


if (!comment.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "댓글을 수정하거나 삭제할 권한이 없습니다.");
        }

        menuCommentRepository.delete(comment);
    }
}
