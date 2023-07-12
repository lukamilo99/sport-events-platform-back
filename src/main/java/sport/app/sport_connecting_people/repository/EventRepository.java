package sport.app.sport_connecting_people.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport.app.sport_connecting_people.entity.Event;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findById(Long id);
    Optional<List<Event>> findByLocation(String location);
    Optional<List<Event>> findByDate(Date date);
    Optional<List<Event>> findBySport(String sport);
}
