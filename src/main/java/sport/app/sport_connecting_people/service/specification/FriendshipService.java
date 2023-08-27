package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;

public interface FriendshipService {

    void createFriendship(User requester, User responder);

    PaginatedUserResponseDto getUserFriends(String name, Pageable pageable);

    void deleteFriendship(Long responderId);
}
