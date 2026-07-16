package Winter_Project.Semteul_Battle.domain.problem.dto.response;

import Winter_Project.Semteul_Battle.domain.problem.entity.IO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IoResponseDto {
    private Long id;
    private String input;
    private String output;

    public static IoResponseDto from(IO io) {
        return IoResponseDto.builder()
                .id(io.getId())
                .input(io.getInput())
                .output(io.getOutput())
                .build();
    }
}
