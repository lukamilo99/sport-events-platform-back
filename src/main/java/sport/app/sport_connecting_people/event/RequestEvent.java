package sport.app.sport_connecting_people.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import sport.app.sport_connecting_people.entity.request.Request;

@Getter
public class RequestEvent extends ApplicationEvent {

    private final Request request;
    private final RequestEventType type;

    public RequestEvent(Object source, Request request, RequestEventType type) {
        super(source);
        this.request = request;
        this.type = type;
    }
}
