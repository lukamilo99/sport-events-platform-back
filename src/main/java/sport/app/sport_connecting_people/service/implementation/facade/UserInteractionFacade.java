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
    public void cancelRequest(Long recipientId) {
        Request request = requestService.findFriendshipRequestByRecipientIdAndStatus(recipientId, RequestStatus.PENDING);
        eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.CANCEL_REQUEST));
    }

    @Transactional
    public void acceptRequest(Long requestId, boolean status) {
        Request request = requestService.findById(requestId);

        if (request instanceof FriendshipRequest friendshipRequest) {
            handleRequestAnswer(friendshipRequest, status);
            eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.FRIENDSHIP_REQUEST_ACCEPT));
        } else if (request instanceof EventInvitationRequest eventInvitationRequest) {
            handleRequestAnswer(eventInvitationRequest, status);
            eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.EVENT_INVITATION_REQUEST_ACCEPT));
        }
    }

    @Transactional
    public void declineRequest(Long requestId, boolean status) {
        Request request = requestService.findById(requestId);

        if (request instanceof FriendshipRequest friendshipRequest) {
            handleRequestAnswer(friendshipRequest, status);
            eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.FRIENDSHIP_REQUEST_DECLINE));
        } else if (request instanceof EventInvitationRequest eventInvitationRequest) {
            handleRequestAnswer(eventInvitationRequest, status);
            eventPublisher.publishEvent(new RequestEvent(this, request, RequestEventType.EVENT_INVITATION_REQUEST_DECLINE));
        }
    }

    private void handleRequestAnswer(Request request, boolean status) {
        if(status) {
            request.setStatus(RequestStatus.ACCEPTED);
        }
        else {
            request.setStatus(RequestStatus.DECLINED);
        }
    }
}
