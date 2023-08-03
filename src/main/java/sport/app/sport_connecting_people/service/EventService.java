package sport.app.sport_connecting_people.service;

import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.*;

import java.util.List;

public interface EventService {

    void createEvent(EventUpsertDto dto);

    void updateEvent(EventUpsertDto dto, Long eventId);

    void deleteEvent(Long eventId);

    void joinEvent(Long eventId);

    void leaveEvent(Long eventId);

    void removeParticipantFromEvent(Long eventId, Long participantId);

    PaginatedEventDto searchEvents(String search, String city, String sport, String day, Pageable pageable);

    PaginatedMyEventDto searchEventsCreatedByUser(Pageable pageable);

    PaginatedMyEventDto searchEventsParticipatedByUser(Pageable pageable);

    List<EventDto> getLatestEvents();

    EventDetailsDto getEventDetails(Long eventId);

    EventDto getEventForUpdate(Long eventId);
}
