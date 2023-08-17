package sport.app.sport_connecting_people.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sport.app.sport_connecting_people.entity.Event;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findById(Long id);

    Page<Event> findAllByOrderByCreationDateDesc(Pageable pageable);

    Page<Event> findByEventCreatorId(Long eventCreatorId, Pageable pageable);

    Page<Event> findByParticipantsId(Long participantId, Pageable pageable);

    Page<Event> findByEventCreatorIdAndDateAfter(Long eventCreatorId, LocalDateTime date, Pageable pageable);

    Page<Event> findByParticipantsIdAndDateAfter(Long participantId, LocalDateTime date, Pageable pageable);
}
