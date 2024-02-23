package Winter_Project.Semteul_Battle.service.Menu;

import Winter_Project.Semteul_Battle.domain.MenuComment;
import Winter_Project.Semteul_Battle.domain.MenuNotice;
import Winter_Project.Semteul_Battle.domain.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentCheckDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentDeleteDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentUpdateDto;
import Winter_Project.Semteul_Battle.repository.MenuCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MenuCommentRepository menuCommentRepository;

    // 댓글 쓰기 메서드
    public MenuComment createComment(CommentDto commentDto, Users users, MenuQuestion menuQuestion) {
        MenuComment comment = MenuComment.builder()
                .content(commentDto.getContent())
                .time(commentDto.getTime())
                .users(users)
                .menuQuestion(menuQuestion)
                .build();
        return menuCommentRepository.save(comment);
    }

    // 댓글 조회 메서드
    public List<MenuComment> getCommentsFromQuestion(Long questionId) {
        // MenuQuestion의 id와 동일한 댓글들을 조회합니다.
        List<MenuComment> comments = menuCommentRepository.findByMenuQuestion_Id(questionId);
        if(comments.isEmpty()) {
            throw new EmptyResultDataAccessException("해당 질문에 대한 댓글을 찾을 수 없습니다.", 1);
        }
        return comments;
    }

    // 댓글 수정 메서드
    public MenuComment updateComment(CommentUpdateDto commentUpdateDto, String loginId) {
        Long CommentId = commentUpdateDto.getCommentId();

        MenuComment comment = menuCommentRepository.findById(CommentId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 댓글을 찾을 수 없습니다.", 1));

        if (!comment.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글을 수정할 수 있는 권한이 없습니다.");
        }

        comment.setContent(commentUpdateDto.getContent());
        comment.setTime(commentUpdateDto.getTime());

        return menuCommentRepository.save(comment);
    }
    // 댓글 삭제 메서드
    public void deleteComment(CommentDeleteDto commentDeleteDto, String loginId) {
        Long commentId = commentDeleteDto.getCommentId();

        // 공지사항을 데이터베이스에서 가져옵니다.
        MenuComment comment = menuCommentRepository.findById(commentId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 댓글을 찾을 수 없습니다.", 1));

        // 해당 공지사항을 수정할 수 있는 권한이 있는지 확인합니다.
        if (!comment.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글을 수정할 수 있는 권한이 없습니다.");
        }

        menuCommentRepository.delete(comment);
    }
}
