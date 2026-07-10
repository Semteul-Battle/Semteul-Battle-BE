package Winter_Project.Semteul_Battle.global.exception;

import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;
import Winter_Project.Semteul_Battle.global.status.ErrorReasonDTO;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public GeneralException(BaseErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return code.getReasonHttpStatus();
    }
}
