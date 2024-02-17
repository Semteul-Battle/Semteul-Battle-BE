package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.*;
import Winter_Project.Semteul_Battle.dto.Contest.*;
import Winter_Project.Semteul_Battle.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ContestLiveService {

    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;
    private final ProblemRepository problemRepository;
    private final ContestNoticeRepository contestNoticeRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final UserRepository userRepository;
    private final ContestantRepository contestantRepository;
    private final SubmitRepository submitRepository;
    private final ContestQuestionRepository contestQuestionRepository;

    // 토큰에서 추출한 아이디로 일반 유저인지 출제자인지 판단
    public Long whoAreU(Long contestId, String loginId) {

        // 출제자 인지 체크
        boolean isExaminer = examinerRepository.existsByUsers_LoginIdAndContest_Id(loginId, contestId);
        if (isExaminer)
            return Long.valueOf(0); // 출제자일 경우

        System.out.println("통과");

        // 참가자 인지 체크
        // ContestantContest 테이블에서 먼저 클라이언트한테 받아온 contest_id로 리스트 contestant_id 추출
        List<ContestantContest> contestantContests = contestantContestRepository.findContestantContestByContest_Id(contestId);
        if (contestantContests.isEmpty()) {
            return Long.valueOf(2); // 대회에 해당하는 참가자가ㅈ 없는 경우
        }

        // contestantContests 리스트에서 contestant_id만 추출
        List<Long> contestantIds = contestantContests.stream()
                .map(contestantContest -> contestantContest.getContestant().getId())
                .collect(Collectors.toList());

        // 로그인 아이디로 유저 id 추출
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();

            // contestantIds 리스트에 있는 contestant_id을 contestant 테이블에서, 위에서 뽑은 userId로 같은지 체크
            Optional<Contestant> contestantOptional = contestantRepository.findByIdInAndUsers_Id(contestantIds, userId);
            if (contestantOptional.isPresent()) {
                // 해당하는 사용자 ID를 가진 참가자가 존재할 경우
                return Long.valueOf(1); // 참가자일 경우
            } else {
                // 해당하는 사용자 ID를 가진 참가자가 존재하지 않을 경우
                return Long.valueOf(2); // 참가자가 아닐 경우
            }
        } else {
            // 사용자가 존재하지 않을 경우에 대한 처리
            return Long.valueOf(2);
        }
    }

    // 문제 리스트
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

    // 문제 정보 넘기기
    public List<Problem> getProblemsInfo(Long contestId) {
        return problemRepository.findByContest_Id(contestId);
    }

    // 대회 공지사항
    @Transactional(readOnly = false)
    public List<ContestNotice> getContestNoticeByContestId(Long contestId) {

        // 공지사항 불러올때는 다 false
        updateContestantsCheckedStatusByContestId(contestId, false);

        return contestNoticeRepository.findByContest_Id(contestId);
    }

    // 대회 공지사항 - 글쓰기
    @Transactional(readOnly = false)
    public ContestNotice saveContestNotice(ContestNoticeDTO contestNoticeDTO) {
        ContestNotice contestNotice = new ContestNotice();

        contestNotice.setTitle(contestNoticeDTO.getTitle());
        contestNotice.setContent(contestNoticeDTO.getContent());
        contestNotice.setTime(new Timestamp(System.currentTimeMillis()));

        Contest contest = contestRepository.findById(contestNoticeDTO.getContestId()).orElseThrow(
                () -> new IllegalArgumentException("Contest not found with id: " + contestNoticeDTO.getContestId()));
        contestNotice.setContest(contest);

        Users user = userRepository.findById(contestNoticeDTO.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("User not found with id: " + contestNoticeDTO.getUserId()));
        contestNotice.setUsers(user);

        // 새로운 글이 써지면 true
        updateContestantsCheckedStatusByContestId(contestNoticeDTO.getContestId(), true);

        return contestNoticeRepository.save(contestNotice);
    }

    // 대회 공지사항 - 삭제
    @Transactional(readOnly = false)
    public void deleteContestNotice(Long contestNoticeId) {
        // 대회 공지사항을 찾습니다.
        ContestNotice contestNotice = contestNoticeRepository.findById(contestNoticeId)
                .orElseThrow(() -> new IllegalArgumentException("Contest Notice not found with id: " + contestNoticeId));

        // 대회 공지사항이 속한 대회의 ID를 가져옵니다.
        Long contestId = contestNotice.getContest().getId();

        // 대회 공지사항을 삭제합니다.
        contestNoticeRepository.delete(contestNotice);

        // 대회 공지사항을 삭제했으므로 참가자의 isChecked 값을 false로 설정합니다.
        updateContestantsCheckedStatusByContestId(contestId, false);
    }

    // 대회 공지사항 - 체크 변환
    @Transactional(readOnly = false)
    public void updateContestantsCheckedStatusByContestId(Long contestId, boolean isChecked) {

        System.out.println("받은 값" + isChecked);

        // 주어진 contestId에 해당하는 모든 참가자를 찾음
        List<ContestantContest> contestantContests = contestantContestRepository.findByContestId(contestId);

        // 각 ContestantContest에 대한 처리를 수행
        for (ContestantContest contestantContest : contestantContests) {
            // 대회에 참가한 각 Contestant의 id
            Long contestantId = contestantContest.getContestant().getId();

            Contestant contestant = contestantRepository.findById(contestantId)
                    .orElseThrow(() -> new IllegalArgumentException("Contestant not found with id: " + contestantId));

            // isChecked 값을 매개변수로 받은 값으로 설정
            contestant.setChecked(isChecked);
            contestantRepository.save(contestant); // 변경사항을 저장
        }
    }


    // 대회 공지사항 - isChecked 값 반환
    public boolean isCheckedReturn(Long contestId, Long userId) {
        List<ContestantContest> contestantContests = contestantContestRepository.findContestantContestByContest_Id(contestId);
        List<Long> contestantIds = contestantContests.stream()
                .map(contestantContest -> contestantContest.getContestant().getId())
                .collect(Collectors.toList());
        Optional<Contestant> contestantOptional = contestantRepository.findByIdInAndUsers_Id(contestantIds, userId);
        return contestantOptional.map(Contestant::isChecked).orElse(false);
    }

    // 대회 제출현황
    public List<SubmitDTO> getSubmitsWithProblems(Long contestId) {
        List<Submit> submits = submitRepository.findByContest_Id(contestId);
        return submits.stream()
                .map(submit -> {
                    SubmitDTO submitDTO = new SubmitDTO();
                    // Submit 정보 설정
                    submitDTO.setId(submit.getId());
                    submitDTO.setLanguage(submit.getLanguage());
                    submitDTO.setRuntime(submit.getRuntime());
                    submitDTO.setTime(submit.getTime());
                    submitDTO.setResult(submit.getResult());
                    submitDTO.setLoginId(submit.getUsers().getLoginId());
                    // ProblemDTO 설정
                    ProblemDTO problemDTO = new ProblemDTO();
                    problemDTO.setId(submit.getProblem().getId());
                    problemDTO.setNumber(submit.getProblem().getNumber());
                    // 다른 필요한 정보들도 추가할 수 있습니다.

                    submitDTO.setProblem(problemDTO);
                    return submitDTO;
                })
                .collect(Collectors.toList());
    }

    // 대회 질문 게시판
    @Transactional(readOnly = false)
    public List<ContestQuestion> getQuestionsByContest(Contest contest) {
        return contestQuestionRepository.findByContestId(contest);
    }

    // 질문 게시판 글쓰기
    @Transactional(readOnly = false)
    public void addQuestion(ContestQuestionDTO contestQuestionDTO) {
        ContestQuestion contestQuestion = new ContestQuestion();
        contestQuestion.setQuestion(contestQuestionDTO.getQuestion());
        contestQuestion.setContent(contestQuestionDTO.getContent());
        contestQuestion.setQuestionTime(contestQuestionDTO.getQuestionTime());

        // 질문자 ID 설정
        Long questionerId = contestQuestionDTO.getUserId();
        Optional<Users> questionerOptional = userRepository.findById(questionerId);
        questionerOptional.ifPresent(contestQuestion::setQuestioner);

        // 대회 정보 설정
        Long contestId = contestQuestionDTO.getContestId();
        Optional<Contest> contestOptional = contestRepository.findById(contestId);
        contestOptional.ifPresent(contestQuestion::setContestId);

        contestQuestionRepository.save(contestQuestion);
    }


    // 질문 글 삭제
    @Transactional
    public void deleteContestQuestion(Long questionId) {
        contestQuestionRepository.deleteById(questionId);
    }

    @Transactional
    public ResponseEntity<String> answerQuestion(AnswerDTO answerDTO) {
        // 답변에 필요한 정보를 추출합니다.
        Long questionId = answerDTO.getQuestionId();
        String answerContent = answerDTO.getAnswer();
        Timestamp answerTime = answerDTO.getAnswerTime();
        Long answererId = answerDTO.getAnswerer();

        // 질문 조회
        Optional<ContestQuestion> questionOptional = contestQuestionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            ContestQuestion contestQuestion = questionOptional.get();

            // 답변자 정보 설정
            Optional<Users> answererOptional = userRepository.findById(answererId);
            if (answererOptional.isPresent()) {
                Users answerer = answererOptional.get();
                contestQuestion.setAnswerer(answerer);
                contestQuestion.setAnswer(answerContent);
                contestQuestion.setAnswerTime(answerTime);

                contestQuestionRepository.save(contestQuestion);
                return new ResponseEntity<>("Answer added successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Answerer not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
    }




}
