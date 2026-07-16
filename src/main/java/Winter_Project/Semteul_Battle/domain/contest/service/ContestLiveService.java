package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.AnswerDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestNoticeDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestQuestionDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestInfoDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestNoticeResponseDto;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestQuestionResponseDto;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.SubmitPageDto;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestNotice;
import Winter_Project.Semteul_Battle.domain.problem.dto.response.ProblemDetailResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContestLiveService {

    Long whoAreU(Long contestId, String loginId);

    List<ContestInfoDTO> getProblemsByContestId(Long contestId);

    List<ProblemDetailResponseDto> getProblemsInfo(Long contestId);

    List<ContestNoticeResponseDto> getContestNoticeByContestId(Long contestId, String tokenFromId);

    ContestNoticeResponseDto saveContestNotice(ContestNoticeDTO contestNoticeDTO);

    void deleteContestNotice(Long contestNoticeId);

    void updateContestantsCheckedStatusByContestId(Long contestId, boolean isChecked);

    void changeCheckIdByHeIs(Long userId);

    boolean isCheckedReturn(Long contestId, Long userId);

    SubmitPageDto<SubmitDTO> getSubmitsWithProblems(Long contestId, Pageable pageable);

    List<ContestQuestionResponseDto> getQuestionsByContest(Contest contest);

    void addQuestion(ContestQuestionDTO contestQuestionDTO);

    void deleteContestQuestion(Long questionId);

    String answerQuestion(AnswerDTO answerDTO);
}
