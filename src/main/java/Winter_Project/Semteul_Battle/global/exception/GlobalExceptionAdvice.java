package Winter_Project.Semteul_Battle.global.exception;

import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.BaseErrorCode;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException e, HttpServletRequest request) {
        return handleExceptionInternal(e, e.getCode(), null, new ServletWebRequest(request));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<Object> handleBadRequest(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorStatus._BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            EmptyResultDataAccessException.class
    })
    public ResponseEntity<Object> handleNotFound(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorStatus._NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthentication(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorStatus._UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(Exception e, WebRequest request) {
        return handleExceptionInternal(e, ErrorStatus._FORBIDDEN, e.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpected(Exception e, WebRequest request) {
        log.error("Unhandled exception", e);
        return handleExceptionInternal(e, ErrorStatus._INTERNAL_SERVER_ERROR, e.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return handleExceptionInternal(e, ErrorStatus._BAD_REQUEST, e.getParameterName() + ": 필수 요청 값입니다.", request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.merge(
                        fieldError.getField(),
                        fieldError.getDefaultMessage() == null ? "올바른 값이 아닙니다." : fieldError.getDefaultMessage(),
                        (previous, current) -> previous + ", " + current
                )
        );
        return handleExceptionInternal(e, ErrorStatus._BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return handleExceptionInternal(e, ErrorStatus._BAD_REQUEST, errors, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return handleExceptionInternal(e, ErrorStatus._METHOD_NOT_ALLOWED, e.getMethod(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e,
            BaseErrorCode code,
            Object result,
            WebRequest request
    ) {
        BaseResponse<Object> body = BaseResponse.onFailure(code, result);
        HttpStatus httpStatus = code.getReasonHttpStatus().getHttpStatus();
        return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, httpStatus, request);
    }
}
