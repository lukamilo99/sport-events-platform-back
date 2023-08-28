package sport.app.sport_connecting_people.specification;

import org.springframework.data.jpa.domain.Specification;
import sport.app.sport_connecting_people.entity.Friendship;

public class FriendshipSpecification {

    public static Specification<Friendship> hasRequesterOrResponderId(Long userId) {
        return (root, query, cb) -> userId == null ? cb.conjunction() : cb.or(cb.equal(root.get("requester").get("id"), userId), cb.equal(root.get("responder").get("id"), userId));
    }

    public static Specification<Friendship> hasRequesterOrResponderName(String name) {
        return (root, query, cb) -> {
            if (name == null) return cb.conjunction();

            return cb.or(
                    cb.like(root.get("requester").get("firstname"), "%" + name + "%"),
                    cb.like(root.get("requester").get("lastname"), "%" + name + "%"),
                    cb.like(root.get("responder").get("firstname"), "%" + name + "%"),
                    cb.like(root.get("responder").get("lastname"), "%" + name + "%")
            );
        };
    }
}

