package sport.app.sport_connecting_people.exceptions;

public class AccessDeniedException extends RuntimeException {

    private String message;

    public AccessDeniedException(String message) {
        super(message);
    }
}
