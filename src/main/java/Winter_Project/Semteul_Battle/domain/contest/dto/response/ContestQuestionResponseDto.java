package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.ContestQuestion;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ContestQuestionResponseDto {
    private Long id;
    private String question;
    private String content;
    private Timestamp questionTime;
    private String answer;
    private Timestamp answerTime;
    private Long questionerId;
    private String questionerName;
    private Long answererId;
    private String answererName;
    private Long contestId;

    public static ContestQuestionResponseDto from(ContestQuestion question) {
        Users answerer = question.getAnswerer();
        return ContestQuestionResponseDto.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .content(question.getContent())
                .questionTime(question.getQuestionTime())
                .answer(question.getAnswer())
                .answerTime(question.getAnswerTime())
                .questionerId(question.getQuestioner().getId())
                .questionerName(question.getQuestioner().getName())
                .answererId(answerer != null ? answerer.getId() : null)
                .answererName(answerer != null ? answerer.getName() : null)
                .contestId(question.getContestId().getId())
                .build();
    }
}
