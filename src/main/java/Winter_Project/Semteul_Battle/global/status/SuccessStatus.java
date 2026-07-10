package Winter_Project.Semteul_Battle.global.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_201", "생성되었습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "COMMON_204", "처리되었습니다.");

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
