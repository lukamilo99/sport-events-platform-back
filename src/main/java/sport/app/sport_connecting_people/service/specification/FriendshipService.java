package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;

import java.util.Optional;

public interface FriendshipService {

    void createFriendship(User requester, User responder);

    void deleteFriendship(Long friendshipId);

    PaginatedUserResponseDto getUserFriends(String name, Pageable pageable);

    Friendship findByRequesterAndResponder(User requester, User responder);

    Friendship findById(Long friendshipId);
}
