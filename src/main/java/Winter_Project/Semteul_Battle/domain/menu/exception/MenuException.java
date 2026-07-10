package Winter_Project.Semteul_Battle.domain.menu.exception;

import Winter_Project.Semteul_Battle.global.exception.GeneralException;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;

public class MenuException extends GeneralException {

    public MenuException(BaseErrorCode code) {
        super(code);
    }

    public MenuException(BaseErrorCode code, String message) {
        super(code, message);
    }
}
