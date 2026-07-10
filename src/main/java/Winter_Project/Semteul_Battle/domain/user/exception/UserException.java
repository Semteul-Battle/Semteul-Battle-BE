package Winter_Project.Semteul_Battle.domain.user.exception;

import Winter_Project.Semteul_Battle.global.exception.GeneralException;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;

public class UserException extends GeneralException {

    public UserException(BaseErrorCode code) {
        super(code);
    }

    public UserException(BaseErrorCode code, String message) {
        super(code, message);
    }
}
