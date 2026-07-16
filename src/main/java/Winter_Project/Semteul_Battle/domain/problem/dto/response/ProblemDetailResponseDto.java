package Winter_Project.Semteul_Battle.domain.problem.dto.response;

import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProblemDetailResponseDto {
    private Long id;
    private String number;
    private String title;
    private String content;
    private String input;
    private String output;
    private String timeLimit;
    private int score;
    private Long contestId;
    private List<IoResponseDto> ios;
    private List<String> imageUrls;

    public static ProblemDetailResponseDto from(Problem problem) {
        return ProblemDetailResponseDto.builder()
                .id(problem.getId())
                .number(problem.getNumber())
                .title(problem.getTitle())
                .content(problem.getContent())
                .input(problem.getInput())
                .output(problem.getOutput())
                .timeLimit(problem.getTimeLimit())
                .score(problem.getScore())
                .contestId(problem.getContest() != null ? problem.getContest().getId() : null)
                .ios(problem.getIos() != null
                        ? problem.getIos().stream().map(IoResponseDto::from).toList()
                        : Collections.emptyList())
                .imageUrls(problem.getImages() != null
                        ? problem.getImages().stream().map(image -> image.getImageUrl()).toList()
                        : Collections.emptyList())
                .build();
    }
}
