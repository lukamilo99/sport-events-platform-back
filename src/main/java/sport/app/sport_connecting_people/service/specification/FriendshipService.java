package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;

import java.util.List;

public interface FriendshipService {

    void createFriendship(Long responderId);

    PaginatedUserResponseDto getUserFriends(Pageable pageable);

    void updateFriendshipStatus(Long responderId, boolean status);

    void deleteFriendship(Long responderId);

    FriendshipStatus getFriendshipStatus(Long currentUserId, Long otherUserId);
}
