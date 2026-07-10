package Winter_Project.Semteul_Battle.domain.menu.service;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.CommentCheckDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuCommentRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface CommentService {
    public MenuComment createComment(CommentDto commentDto, Users users, MenuQuestion menuQuestion);
    public List<MenuComment> getCommentsFromQuestion(Long questionId);
    public MenuComment updateComment(CommentUpdateDto commentUpdateDto, String loginId);
    public void deleteComment(CommentDeleteDto commentDeleteDto, String loginId);
}
