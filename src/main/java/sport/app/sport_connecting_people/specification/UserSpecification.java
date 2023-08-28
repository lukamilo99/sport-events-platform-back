package sport.app.sport_connecting_people.specification;


import org.springframework.data.jpa.domain.Specification;
import sport.app.sport_connecting_people.entity.User;

public class UserSpecification {

    public static Specification<User> hasFirstName(String search) {
        return (root, query, cb) -> search == null ? cb.conjunction() : cb.like(root.get("firstname"), "%" + search + "%");
    }

    public static Specification<User> hasLastName(String search) {
        return (root, query, cb) -> search == null ? cb.conjunction() : cb.like(root.get("lastname"), "%" + search + "%");
    }

    public static Specification<User> hasNotId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.notEqual(root.get("id"), id);
    }
}
