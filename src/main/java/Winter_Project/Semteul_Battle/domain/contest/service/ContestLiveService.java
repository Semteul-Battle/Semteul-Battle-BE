package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.*;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.*;
import Winter_Project.Semteul_Battle.domain.contest.repository.*;
import Winter_Project.Semteul_Battle.domain.problem.repository.*;
import Winter_Project.Semteul_Battle.domain.user.repository.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ContestLiveService {
    public Long whoAreU(Long contestId, String loginId);
    public List<ContestInfoDTO> getProblemsByContestId(Long contestId);
    public List<Problem> getProblemsInfo(Long contestId);
    public List<ContestNotice> getContestNoticeByContestId(Long contestId, String tokenFromId);
    public ContestNotice saveContestNotice(ContestNoticeDTO contestNoticeDTO);
    public void deleteContestNotice(Long contestNoticeId);
    public void updateContestantsCheckedStatusByContestId(Long contestId, boolean isChecked);
    public void changeCheckIdByHeIs(Long userId);
    public boolean isCheckedReturn(Long contestId, Long userId);
    public SubmitPageDto<SubmitDTO> getSubmitsWithProblems(Long contestId, Pageable pageable);
    public List<ContestQuestion> getQuestionsByContest(Contest contest);
    public void addQuestion(ContestQuestionDTO contestQuestionDTO);
    public void deleteContestQuestion(Long questionId);
    public ResponseEntity<String> answerQuestion(AnswerDTO answerDTO);
}
