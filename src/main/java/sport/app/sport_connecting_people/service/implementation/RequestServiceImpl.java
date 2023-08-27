package sport.app.sport_connecting_people.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.EventInvitationRequest;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;
import sport.app.sport_connecting_people.entity.request.Request;
import sport.app.sport_connecting_people.exceptions.request.RequestNotFoundException;
import sport.app.sport_connecting_people.mapper.RequestMapper;
import sport.app.sport_connecting_people.repository.RequestRepository;
import sport.app.sport_connecting_people.service.specification.RequestService;

@AllArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;
    private RequestMapper requestMapper;

    @Override
    public Request findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found with id: " + requestId));
    }

    @Override
    public Request findFriendshipRequestByRecipientIdAndStatus(Long recipientId, RequestStatus status) {
        return requestRepository.findFriendshipRequestByReceiverIdAndStatus(recipientId, status)
                .orElseThrow(() -> new RequestNotFoundException("Request not found with id"));
    }

    @Override
    public Request createEventInvitationRequest(User sender, User responder, Long eventId) {
        EventInvitationRequest request = requestMapper.mapToEventInvitationRequest(sender, responder, eventId);
        return requestRepository.save(request);
    }

    @Override
    public void deleteById(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    public Request createFriendshipRequest(User sender, User responder) {
        FriendshipRequest request = requestMapper.mapToFriendshipRequest(sender, responder);
        return requestRepository.save(request);
    }
}
