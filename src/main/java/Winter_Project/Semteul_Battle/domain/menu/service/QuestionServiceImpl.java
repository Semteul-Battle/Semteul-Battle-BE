package Winter_Project.Semteul_Battle.domain.menu.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final MenuQuestionRepository menuQuestionRepository;

public MenuQuestion createQuestion(QuestionDto questionDto, Users users) {
        MenuQuestion question = MenuQuestion.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getContent())
                .time(questionDto.getTime())
                .users(users)
                .build();

        return menuQuestionRepository.save(question);
    }

public QuestionPageDto getQuestionPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuQuestion> questionPage = menuQuestionRepository.findAll(pageable);

        List<QuestionCheckDto> questionDtoList = questionPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        int totalPages = questionPage.getTotalPages();
        long totalElements = questionPage.getTotalElements();
        int currentPageNumber = questionPage.getNumber();

        boolean hasPreviousPage = currentPageNumber > 0;
boolean hasNextPage = currentPageNumber < totalPages - 1;
        Integer prevPageNumber = hasPreviousPage ? currentPageNumber - 1 : null;
        Integer nextPageNumber = hasNextPage ? currentPageNumber + 1 : null;
return new QuestionPageDto(
                questionDtoList,
                totalPages,
                totalElements,
                currentPageNumber,
                prevPageNumber,
                nextPageNumber
        );
    }


private QuestionCheckDto convertToDto(MenuQuestion menuQuestion) {
        String loginId = menuQuestion.getUsers().getLoginId();
        return new QuestionCheckDto(
                loginId,
                menuQuestion.getTitle(),
                menuQuestion.getContent(),
                menuQuestion.getTime()
        );
    }


public MenuQuestion updateQuestion(QuestionUpdateDto questionUpdateDto, String loginId) {
        Long questionId = questionUpdateDto.getQuestionId();

        MenuQuestion question = menuQuestionRepository.findById(questionId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "질문을 찾을 수 없습니다."));

        if (!question.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "질문을 수정하거나 삭제할 권한이 없습니다.");
        }

        question.updateQuestion(questionUpdateDto.getTitle(), questionUpdateDto.getContent(), questionUpdateDto.getTime());

        return menuQuestionRepository.save(question);
    }


public void deleteQuestion(QuestionDeleteDto questionDeleteDto, String loginId) {
        Long questionId = questionDeleteDto.getQuestionId();

        MenuQuestion question = menuQuestionRepository.findById(questionId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "질문을 찾을 수 없습니다."));

        if (!question.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "질문을 수정하거나 삭제할 권한이 없습니다.");
        }

        menuQuestionRepository.delete(question);
    }
}
