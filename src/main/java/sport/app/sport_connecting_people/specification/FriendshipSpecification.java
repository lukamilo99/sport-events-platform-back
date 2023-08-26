package sport.app.sport_connecting_people.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sport.app.sport_connecting_people.entity.Friendship;
import sport.app.sport_connecting_people.entity.enums.FriendshipStatus;

public class FriendshipSpecification {

    public static Specification<Friendship> hasUserWithId(Long userId) {
        return (root, query, cb) -> {
            Predicate requesterPredicate = cb.equal(root.get("requester").get("id"), userId);
            Predicate responderPredicate = cb.equal(root.get("responder").get("id"), userId);
            return cb.or(requesterPredicate, responderPredicate);
        };
    }

    public static Specification<Friendship> userHasName(String name) {
        return (root, query, cb) -> {
            Predicate requesterPredicate = cb.like(root.get("requester").get("name"), "%" + name + "%");
            Predicate responderPredicate = cb.like(root.get("responder").get("name"), "%" + name + "%");
            return cb.or(requesterPredicate, responderPredicate);
        };
    }

    public static Specification<Friendship> hasStatus(FriendshipStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}

