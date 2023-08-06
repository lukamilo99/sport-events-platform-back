package sport.app.sport_connecting_people.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import sport.app.sport_connecting_people.exceptions.authentication.OAuth2AuthenticationProcessingException;
import sport.app.sport_connecting_people.exceptions.event.EventFullException;
import sport.app.sport_connecting_people.exceptions.event.EventNotFoundException;
import sport.app.sport_connecting_people.exceptions.authentication.InvalidTokenException;
import sport.app.sport_connecting_people.exceptions.authentication.TokenExpiredException;
import sport.app.sport_connecting_people.exceptions.user.AccessDeniedException;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.exceptions.user.UserBannedException;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistsException.class, EventFullException.class})
    public ResponseEntity<ExceptionResponse> handleConflicts(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({EventNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({TokenExpiredException.class, InvalidTokenException.class,
            AuthenticationException.class, OAuth2AuthenticationProcessingException.class})
    public ResponseEntity<ExceptionResponse> handleAuthorizationExceptions(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({AccessDeniedException.class, UserBannedException.class})
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
