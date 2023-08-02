package sport.app.sport_connecting_people.specification;

import org.springframework.data.jpa.domain.Specification;
import sport.app.sport_connecting_people.entity.Event;

import java.time.LocalDate;

public class EventSpecification {

    public static Specification<Event> hasCity(String city) {
        return (root, query, cb) -> city == null ? cb.conjunction() : cb.like(root.get("city"), "%" + city + "%");
    }

    public static Specification<Event> hasSport(String sport) {
        return (root, query, cb) -> sport == null ? cb.conjunction() : cb.equal(root.get("sport"), sport);
    }

    public static Specification<Event> hasName(String search) {
        return (root, query, cb) -> search == null ? cb.conjunction() : cb.like(root.get("name"), "%" + search + "%");
    }

    public static Specification<Event> isOnDay(String dayOption) {
        if ("today".equals(dayOption)) {
            return (root, query, cb) -> cb.equal(root.get("date"), LocalDate.now());
        } else if ("next5days".equals(dayOption)) {
            return (root, query, cb) -> cb.between(root.get("date"), LocalDate.now(), LocalDate.now().plusDays(5));
        } else if ("fromDay5to10".equals(dayOption)) {
            return (root, query, cb) -> cb.between(root.get("date"), LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));
        } else if ("afterDay10".equals(dayOption)) {
            return (root, query, cb) -> cb.greaterThan(root.get("date"), LocalDate.now().plusDays(10));
        } else {
            return null;
        }
    }
}
