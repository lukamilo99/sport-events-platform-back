package sport.app.sport_connecting_people.exceptions.jwt;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
