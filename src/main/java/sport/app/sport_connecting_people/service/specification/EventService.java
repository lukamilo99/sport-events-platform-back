package sport.app.sport_connecting_people.service.specification;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.*;
import sport.app.sport_connecting_people.entity.Event;

import java.util.List;

public interface EventService {

    Event findEventById(Long eventId);

    void createEvent(EventUpsertDto dto);

    void updateEvent(EventUpsertDto dto, Long eventId);

    void deleteEvent(Long eventId);

    void joinEvent(Long eventId);

    void leaveEvent(Long eventId);

    void removeParticipantFromEvent(Long eventId, Long participantId);

    PaginatedEventDto searchEvents(String search, String city, String sport, String day, Pageable pageable);

    PaginatedMyEventDto searchEventsCreatedByUser(Pageable pageable, boolean includePastEvents);

    PaginatedMyEventDto searchEventsParticipatedByUser(Pageable pageable, boolean includePastEvents);

    List<EventDto> getLatestEvents(String city, Pageable pageable);

    EventDetailsDto getEventDetails(Long eventId);

    EventDto getEvent(Long eventId);
}
