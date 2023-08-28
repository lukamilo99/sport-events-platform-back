package sport.app.sport_connecting_people.service.implementation.facade;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.relation.RelationDto;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.enums.RequestStatus;
import sport.app.sport_connecting_people.entity.request.Request;
import sport.app.sport_connecting_people.service.specification.FriendshipService;
import sport.app.sport_connecting_people.service.specification.PrincipalService;
import sport.app.sport_connecting_people.service.specification.RequestService;
import sport.app.sport_connecting_people.service.specification.UserService;

@AllArgsConstructor
@Service
public class UserRelationFacade {

    private final UserService userService;
    private final PrincipalService principalService;
    private final RequestService requestService;
    private final FriendshipService friendshipService;

    public PaginatedUserResponseDto searchUsers(String name, Pageable pageable) {
        PaginatedUserResponseDto response = userService.searchUsers(name, pageable);
        Long currentUserId = principalService.getCurrentUserId();

        for(UserResponseDto userDto: response.getUsers()) {
            Request request = requestService.findFriendshipRequestForIds(currentUserId, userDto.getId());
            RelationDto relationDto = handleRequest(request);
            userDto.setRelation(relationDto);
        }
        return response;
    }

    public void deleteFriendship(Long friendshipId) {
        Friendship friendship = friendshipService.findById(friendshipId);
        if (friendship != null) {
            Request request = requestService.findFriendshipRequestForIds(friendship.getRequester().getId(), friendship.getResponder().getId());
            if (request != null) {
                requestService.deleteById(request.getId());
            }
        }
        friendshipService.deleteFriendship(friendshipId);
    }

    private RelationDto handleRequest(Request request) {
        RelationDto relationDto = new RelationDto();
        if (request != null) {
            relationDto.setRequestId(request.getId());
            relationDto.setSenderId(request.getSender().getId());
            relationDto.setRecipientId(request.getReceiver().getId());
            relationDto.setStatus(request.getStatus());

            if(request.getStatus().equals(RequestStatus.ACCEPTED)) {
                Friendship friendship = friendshipService.findByRequesterAndResponder(request.getSender(), request.getReceiver());
                relationDto.setFriendshipId(friendship.getId());
            }
        }
        else {
            relationDto.setStatus(RequestStatus.NO_REQUEST);
        }
        return relationDto;
    }
}
