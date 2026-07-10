package Winter_Project.Semteul_Battle.global.status;

public interface BaseErrorCode {
    String getCode();

    String getMessage();

    ErrorReasonDTO getReasonHttpStatus();
}
