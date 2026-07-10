package Winter_Project.Semteul_Battle.global.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    OK(HttpStatus.OK, "COMMON_200", "??μ떜媛?슙?癰귥쥙???????뽯쨦??"),
    CREATED(HttpStatus.CREATED, "COMMON_201", "???袁⑸즴????癲???????"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "COMMON_204", "?轅붽틓??影?뽧걤???癲???????");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .httpStatus(httpStatus)
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    public static SuccessStatus fromHttpStatus(int status) {
        if (status == HttpStatus.CREATED.value()) {
            return CREATED;
        }
        if (status == HttpStatus.NO_CONTENT.value()) {
            return NO_CONTENT;
        }
        return OK;
    }
}
