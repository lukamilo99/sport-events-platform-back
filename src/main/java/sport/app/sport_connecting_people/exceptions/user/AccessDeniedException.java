package sport.app.sport_connecting_people.exceptions.user;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
