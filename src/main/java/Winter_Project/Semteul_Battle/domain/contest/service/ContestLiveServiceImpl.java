package Winter_Project.Semteul_Battle.domain.contest.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.contest.exception.ContestException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContestLiveServiceImpl implements ContestLiveService {

    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;
    private final ProblemRepository problemRepository;
    private final ContestNoticeRepository contestNoticeRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final UserRepository userRepository;
    private final ContestantRepository contestantRepository;
    private final SubmitRepository submitRepository;
    private final ContestQuestionRepository contestQuestionRepository;


public Long whoAreU(Long contestId, String loginId) {


boolean isExaminer = examinerRepository.existsByUsers_LoginIdAndContest_Id(loginId, contestId);
        if (isExaminer)
            return Long.valueOf(0);
        System.out.println("debug");


List<ContestantContest> contestantContests = contestantContestRepository.findContestantContestByContest_Id(contestId);
        if (contestantContests.isEmpty()) {
            return Long.valueOf(2);
}


List<Long> contestantIds = contestantContests.stream()
                .map(contestantContest -> contestantContest.getContestant().getId())
                .collect(Collectors.toList());


Optional<Users> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();


Optional<Contestant> contestantOptional = contestantRepository.findByIdInAndUsers_Id(contestantIds, userId);
            if (contestantOptional.isPresent()) {

return Long.valueOf(1);
} else {

return Long.valueOf(2);
}
} else {

return Long.valueOf(2);
        }
    }


public List<ContestInfoDTO> getProblemsByContestId(Long contestId) {
        List<Problem> problems = problemRepository.findByContest_Id(contestId);
        return problems.stream()
                .map(problem -> new ContestInfoDTO(
                        problem.getId(),
                        problem.getNumber(),
                        problem.getTitle(),
                        problem.getScore()))
                .collect(Collectors.toList());
    }


public List<Problem> getProblemsInfo(Long contestId) {
        return problemRepository.findByContest_Id(contestId);
    }


@Transactional(readOnly = false)
    public List<ContestNotice> getContestNoticeByContestId(Long contestId, String tokenFromId) {

        Long userId = userRepository.findByLoginId(tokenFromId)
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "User not found with loginId: " + tokenFromId))
                .getId();


        changeCheckIdByHeIs(userId);

        return contestNoticeRepository.findByContest_Id(contestId);
    }


@Transactional(readOnly = false)
    public ContestNotice saveContestNotice(ContestNoticeDTO contestNoticeDTO) {
        ContestNotice contestNotice = new ContestNotice();

        contestNotice.setTitle(contestNoticeDTO.getTitle());
        contestNotice.setContent(contestNoticeDTO.getContent());
        contestNotice.setTime(new Timestamp(System.currentTimeMillis()));

        Contest contest = contestRepository.findById(contestNoticeDTO.getContestId()).orElseThrow(
                () -> new ContestException(ErrorStatus._NOT_FOUND, "Contest not found with id: " + contestNoticeDTO.getContestId()));
        contestNotice.setContest(contest);

        Users user = userRepository.findById(contestNoticeDTO.getUserId()).orElseThrow(
                () -> new ContestException(ErrorStatus._NOT_FOUND, "User not found with id: " + contestNoticeDTO.getUserId()));
        contestNotice.setUsers(user);


        updateContestantsCheckedStatusByContestId(contestNoticeDTO.getContestId(), true);

        return contestNoticeRepository.save(contestNotice);
    }


@Transactional(readOnly = false)
    public void deleteContestNotice(Long contestNoticeId) {

ContestNotice contestNotice = contestNoticeRepository.findById(contestNoticeId)
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "Contest Notice not found with id: " + contestNoticeId));


Long contestId = contestNotice.getContest().getId();


contestNoticeRepository.delete(contestNotice);


        updateContestantsCheckedStatusByContestId(contestId, false);
    }


@Transactional(readOnly = false)
    public void updateContestantsCheckedStatusByContestId(Long contestId, boolean isChecked) {


List<ContestantContest> contestantContests = contestantContestRepository.findByContestId(contestId);


for (ContestantContest contestantContest : contestantContests) {

Long contestantId = contestantContest.getContestant().getId();

            Contestant contestant = contestantRepository.findById(contestantId)
                    .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "Contestant not found with id: " + contestantId));


            contestantRepository.save(contestant);
}
    }
    @Transactional(readOnly = false)
    public void changeCheckIdByHeIs(Long userId) {
        Optional<Contestant> optionalContestant = contestantRepository.findByUsersId(userId);
        if (optionalContestant.isPresent()) {
            Contestant contestant = optionalContestant.get();
            contestant.setChecked(false);
contestantRepository.save(contestant);
} else {

throw new ContestException(ErrorStatus._NOT_FOUND, "Contestant with id " + userId + " not found");
        }
    }


@Transactional(readOnly = false)
    public boolean isCheckedReturn(Long contestId, Long userId) {
        List<ContestantContest> contestantContests = contestantContestRepository.findContestantContestByContest_Id(contestId);
        List<Long> contestantIds = contestantContests.stream()
                .map(contestantContest -> contestantContest.getContestant().getId())
                .collect(Collectors.toList());
        Optional<Contestant> contestantOptional = contestantRepository.findByIdInAndUsers_Id(contestantIds, userId);
        return contestantOptional.map(Contestant::isChecked).orElse(false);
    }


public SubmitPageDto<SubmitDTO> getSubmitsWithProblems(Long contestId, Pageable pageable) {
        Page<Submit> submitsPage = submitRepository.findByContestId(contestId, pageable);

        List<SubmitDTO> submitDTOs = submitsPage.getContent().stream()
                .map(submit -> {
                    SubmitDTO submitDTO = new SubmitDTO();

                    submitDTO.setLanguage(submit.getLanguage());
                    submitDTO.setRuntime(submit.getRuntime());
                    submitDTO.setTime(submit.getTime());
                    submitDTO.setResult(submit.getResult());
                    submitDTO.setUserId(submit.getUsers().getId());
                    //
                    ProblemDTO problemDTO = new ProblemDTO();
                    problemDTO.setId(submit.getProblem().getId());
                    problemDTO.setNumber(submit.getProblem().getNumber());


                    submitDTO.setProblem(problemDTO);
                    return submitDTO;
                })
                .collect(Collectors.toList());


boolean hasPreviousPage = submitsPage.hasPrevious();
        boolean hasNextPage = submitsPage.hasNext();

        return new SubmitPageDto<>(
                submitDTOs,
                submitsPage.getNumber(),
                submitsPage.getSize(),
                submitsPage.getTotalElements(),
                submitsPage.getTotalPages(),
                hasPreviousPage,
                hasNextPage
        );
    }


@Transactional(readOnly = false)
    public List<ContestQuestion> getQuestionsByContest(Contest contest) {
        return contestQuestionRepository.findByContestId(contest);
    }


@Transactional(readOnly = false)
    public void addQuestion(ContestQuestionDTO contestQuestionDTO) {
        ContestQuestion contestQuestion = new ContestQuestion();
        contestQuestion.setQuestion(contestQuestionDTO.getQuestion());
        contestQuestion.setContent(contestQuestionDTO.getContent());
        contestQuestion.setQuestionTime(contestQuestionDTO.getQuestionTime());


Long questionerId = contestQuestionDTO.getUserId();
        Optional<Users> questionerOptional = userRepository.findById(questionerId);
        questionerOptional.ifPresent(contestQuestion::setQuestioner);


Long contestId = contestQuestionDTO.getContestId();
        Optional<Contest> contestOptional = contestRepository.findById(contestId);
        contestOptional.ifPresent(contestQuestion::setContestId);

        contestQuestionRepository.save(contestQuestion);
    }



@Transactional
    public void deleteContestQuestion(Long questionId) {
        contestQuestionRepository.deleteById(questionId);
    }

    @Transactional
    public String answerQuestion(AnswerDTO answerDTO) {
        Long questionId = answerDTO.getQuestionId();
        String answerContent = answerDTO.getAnswer();
        Timestamp answerTime = answerDTO.getAnswerTime();
        Long answererId = answerDTO.getAnswerer();

        ContestQuestion contestQuestion = contestQuestionRepository.findById(questionId)
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "질문을 찾을 수 없습니다."));
        Users answerer = userRepository.findById(answererId)
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "답변자를 찾을 수 없습니다."));

        contestQuestion.setAnswerer(answerer);
        contestQuestion.setAnswer(answerContent);
        contestQuestion.setAnswerTime(answerTime);

        contestQuestionRepository.save(contestQuestion);
        return "답변이 등록되었습니다.";
    }

}
