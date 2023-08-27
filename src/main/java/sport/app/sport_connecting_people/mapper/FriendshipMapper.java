package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.entity.request.FriendshipRequest;

import java.time.LocalDateTime;

@Component
public class FriendshipMapper {

    public Friendship mapToFriendship(User requester, User responder) {
        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setResponder(responder);
        friendship.setCreationDate(LocalDateTime.now());
        return friendship;
    }
}
