package sport.app.sport_connecting_people.event.listener;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.Request;
import sport.app.sport_connecting_people.event.RequestEvent;
import sport.app.sport_connecting_people.service.specification.EventService;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.NotificationService;
import sport.app.sport_connecting_people.service.specification.RequestService;

@AllArgsConstructor
@Component
public class RequestEventListener implements ApplicationListener<RequestEvent> {

    private NotificationService notificationService;
    private FriendshipService friendshipService;
    private EventService eventService;
    private RequestService requestService;

    @Override
    public void onApplicationEvent(RequestEvent event) {
        Request request = event.getRequest();
        switch (event.getType()) {
            case CANCEL_REQUEST:
                requestService.deleteById(request.getId());
            case FRIENDSHIP_REQUEST, EVENT_INVITATION_REQUEST:
                notificationService.createNotificationForRequest(request);
                break;
            case FRIENDSHIP_REQUEST_ACCEPT:
                notificationService.markAsRead(request.getId());
                friendshipService.createFriendship(request.getSender(), request.getReceiver());
                break;
            case FRIENDSHIP_REQUEST_DECLINE, EVENT_INVITATION_REQUEST_DECLINE:
                notificationService.markAsRead(request.getId());
                requestService.deleteById(request.getId());
                break;
            case EVENT_INVITATION_REQUEST_ACCEPT:
                notificationService.markAsRead(request.getId());
                eventService.joinEvent(((EventInvitationRequest) request).getEventId());
                break;
        }
    }
}


