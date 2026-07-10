package Winter_Project.Semteul_Battle.domain.menu.service;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.NoticeCheckDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.NoticeDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.NoticePageDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.NoticeUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.*;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuQuestionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

public interface QuestionService {
    public MenuQuestion createQuestion(QuestionDto questionDto, Users users);
    public QuestionPageDto getQuestionPage(int page, int size);
    public MenuQuestion updateQuestion(QuestionUpdateDto questionUpdateDto, String loginId);
    public void deleteQuestion(QuestionDeleteDto questionDeleteDto, String loginId);
}
