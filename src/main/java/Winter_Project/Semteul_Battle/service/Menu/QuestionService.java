package Winter_Project.Semteul_Battle.service.Menu;

import Winter_Project.Semteul_Battle.domain.MenuNotice;
import Winter_Project.Semteul_Battle.domain.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeCheckDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeDeleteDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticePageDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeUpdateDto;
import Winter_Project.Semteul_Battle.dto.MenuQuestion.*;
import Winter_Project.Semteul_Battle.repository.MenuQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final MenuQuestionRepository menuQuestionRepository;
    // 질문 글쓰기 메서드
    public MenuQuestion createQuestion(QuestionDto questionDto, Users users) {
        MenuQuestion question = MenuQuestion.builder()
                .title(questionDto.getTitle())
                .content(questionDto.getContent())
                .time(questionDto.getTime())
                .users(users)
                .build();

        return menuQuestionRepository.save(question);
    }
    // 질문 목록 조회 메서드
    public QuestionPageDto getQuestionPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuQuestion> questionPage = menuQuestionRepository.findAll(pageable);

        List<QuestionCheckDto> questionDtoList = questionPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        int totalPages = questionPage.getTotalPages();
        long totalElements = questionPage.getTotalElements();
        int currentPageNumber = questionPage.getNumber();

        boolean hasPreviousPage = currentPageNumber > 0; // 이전 페이지 여부
        boolean hasNextPage = currentPageNumber < totalPages - 1; // 다음 페이지 여부
        Integer prevPageNumber = hasPreviousPage ? currentPageNumber - 1 : null; // 이전 페이지 번호
        Integer nextPageNumber = hasNextPage ? currentPageNumber + 1 : null; // 다음 페이지 번호

        return new QuestionPageDto(
                questionDtoList,
                totalPages,
                totalElements,
                currentPageNumber,
                prevPageNumber,
                nextPageNumber
        );
    }

    // MenuQuestion 엔티티를 QuestionCheckDto로 변환하는 메서드
    private QuestionCheckDto convertToDto(MenuQuestion menuQuestion) {
        String loginId = menuQuestion.getUsers().getLoginId();
        return new QuestionCheckDto(
                loginId,
                menuQuestion.getTitle(),
                menuQuestion.getContent(),
                menuQuestion.getTime()
        );
    }

    // 질문 수정 메서드
    public MenuQuestion updateQuestion(QuestionUpdateDto questionUpdateDto, String loginId) {
        Long questionId = questionUpdateDto.getQuestionId();

        MenuQuestion question = menuQuestionRepository.findById(questionId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 질문글을 찾을 수 없습니다.", 1));

        if (!question.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 질문글을 수정할 수 있는 권한이 없습니다.");
        }

        question.setTitle(questionUpdateDto.getTitle());
        question.setContent(questionUpdateDto.getContent());
        question.setTime(questionUpdateDto.getTime());

        return menuQuestionRepository.save(question);
    }

    // 질문 삭제 메서드
    public void deleteQuestion(QuestionDeleteDto questionDeleteDto, String loginId) {
        Long questionId = questionDeleteDto.getQuestionId();

        MenuQuestion question = menuQuestionRepository.findById(questionId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 질문글을 찾을 수 없습니다.", 1));

        if (!question.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 질문글을 수정할 수 있는 권한이 없습니다.");
        }

        menuQuestionRepository.delete(question);
    }
}