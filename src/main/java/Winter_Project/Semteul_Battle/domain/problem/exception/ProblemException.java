package Winter_Project.Semteul_Battle.domain.problem.exception;

import Winter_Project.Semteul_Battle.global.exception.GeneralException;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;

public class ProblemException extends GeneralException {

    public ProblemException(BaseErrorCode code) {
        super(code);
    }

    public ProblemException(BaseErrorCode code, String message) {
        super(code, message);
    }
}
