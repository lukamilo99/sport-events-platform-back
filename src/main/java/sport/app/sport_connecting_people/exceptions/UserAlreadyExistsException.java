package sport.app.sport_connecting_people.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private String message;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
