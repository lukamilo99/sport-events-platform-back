package sport.app.sport_connecting_people.service.specification;

import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.Request;

public interface RequestService {

    Request findById(Long requestId);

    Request findFriendshipRequestByRecipientIdAndStatus(Long recipientId, RequestStatus status);

    Request createFriendshipRequest(User sender, User responder);

    Request createEventInvitationRequest(User sender, User responder, Long eventId);

    void deleteById(Long requestId);
}
