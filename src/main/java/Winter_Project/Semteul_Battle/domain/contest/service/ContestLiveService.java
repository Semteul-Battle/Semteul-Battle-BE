package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.AnswerDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestNoticeDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestQuestionDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestInfoDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.SubmitPageDto;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestNotice;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestQuestion;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContestLiveService {

    Long whoAreU(Long contestId, String loginId);

    List<ContestInfoDTO> getProblemsByContestId(Long contestId);

    List<Problem> getProblemsInfo(Long contestId);

    List<ContestNotice> getContestNoticeByContestId(Long contestId, String tokenFromId);

    ContestNotice saveContestNotice(ContestNoticeDTO contestNoticeDTO);

    void deleteContestNotice(Long contestNoticeId);

    void updateContestantsCheckedStatusByContestId(Long contestId, boolean isChecked);

    void changeCheckIdByHeIs(Long userId);

    boolean isCheckedReturn(Long contestId, Long userId);

    SubmitPageDto<SubmitDTO> getSubmitsWithProblems(Long contestId, Pageable pageable);

    List<ContestQuestion> getQuestionsByContest(Contest contest);

    void addQuestion(ContestQuestionDTO contestQuestionDTO);

    void deleteContestQuestion(Long questionId);

    String answerQuestion(AnswerDTO answerDTO);
}
