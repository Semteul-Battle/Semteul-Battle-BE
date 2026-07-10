package Winter_Project.Semteul_Battle.domain.contest.exception;

import Winter_Project.Semteul_Battle.global.exception.GeneralException;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;

public class ContestException extends GeneralException {

    public ContestException(BaseErrorCode code) {
        super(code);
    }

    public ContestException(BaseErrorCode code, String message) {
        super(code, message);
    }
}
