package sport.app.sport_connecting_people.service.implementation.facade;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;
import sport.app.sport_connecting_people.entity.request.Request;
import sport.app.sport_connecting_people.event.RequestEvent;
import sport.app.sport_connecting_people.event.RequestEventType;
import sport.app.sport_connecting_people.service.specification.*;

@AllArgsConstructor
@Service
public class UserInteractionFacade {

    private RequestService requestService;
    private PrincipalService principalService;
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long sendFriendRequest(Long recipientId) {
        User sender = principalService.getCurrentUser();
        User responder = userService.findUserById(recipientId);

        Request request = requestService.createFriendshipRequest(sender, responder);
        eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.FRIENDSHIP_REQUEST));
        return request.getId();
    }

    @Transactional
    public void sendEventInvitationRequest(Long eventId, Long recipientId) {
        User sender = principalService.getCurrentUser();
        User responder = userService.findUserById(recipientId);

        Request request = requestService.createEventInvitationRequest(sender, responder, eventId);
        eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.EVENT_INVITATION_REQUEST));
    }

    @Transactional
    public void cancelRequest(Long requestId) {
        Request request = requestService.findById(requestId);
        eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.CANCEL_REQUEST));
    }

    @Transactional
    public void acceptRequest(Long requestId) {
        Request request = requestService.findById(requestId);

        if (request instanceof FriendshipRequest friendshipRequest) {
            request.setStatus(RequestStatus.ACCEPTED);
            eventPublisher.publishEvent(new RequestEvent(this, friendshipRequest, RequestEventType.FRIENDSHIP_REQUEST_ACCEPT));
        } else if (request instanceof EventInvitationRequest eventInvitationRequest) {
            request.setStatus(RequestStatus.ACCEPTED);
            eventPublisher.publishEvent(new RequestEvent(this, eventInvitationRequest, RequestEventType.EVENT_INVITATION_REQUEST_ACCEPT));
        }
    }

    @Transactional
    public void declineRequest(Long requestId) {
        Request request = requestService.findById(requestId);

        if (request instanceof FriendshipRequest friendshipRequest) {
            eventPublisher.publishEvent(new RequestEvent(this, friendshipRequest, RequestEventType.FRIENDSHIP_REQUEST_DECLINE));
        } else if (request instanceof EventInvitationRequest eventInvitationRequest) {
            eventPublisher.publishEvent(new RequestEvent(this, eventInvitationRequest, RequestEventType.EVENT_INVITATION_REQUEST_DECLINE));
        }
    }
}
