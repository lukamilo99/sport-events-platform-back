package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;

import java.time.LocalDateTime;

@Component
public class RequestMapper {

    public FriendshipRequest mapToFriendshipRequest(User sender, User receiver) {
        FriendshipRequest request = new FriendshipRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        return request;
    }

    public EventInvitationRequest mapToEventInvitationRequest(User sender, User receiver, Long eventId) {
        EventInvitationRequest request = new EventInvitationRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setCreationDate(LocalDateTime.now());
        request.setEventId(eventId);
        return request;
    }
}
