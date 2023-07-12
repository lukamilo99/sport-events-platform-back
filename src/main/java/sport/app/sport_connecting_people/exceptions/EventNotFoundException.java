package sport.app.sport_connecting_people.exceptions;

public class EventNotFoundException extends RuntimeException {

    private String message;

    public EventNotFoundException(String message) {
        super(message);
    }
}
