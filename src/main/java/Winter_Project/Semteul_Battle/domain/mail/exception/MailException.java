package Winter_Project.Semteul_Battle.domain.mail.exception;

import Winter_Project.Semteul_Battle.global.exception.GeneralException;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;

public class MailException extends GeneralException {

    public MailException(BaseErrorCode code) {
        super(code);
    }

    public MailException(BaseErrorCode code, String message) {
        super(code, message);
    }
}
