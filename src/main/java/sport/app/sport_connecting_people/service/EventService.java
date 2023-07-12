package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.dto.event.EventResponseDto;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.EventNotFoundException;
import sport.app.sport_connecting_people.mapper.EventMapper;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.EventRepository;

@AllArgsConstructor
@Service
public class EventService {

    private EventRepository eventRepository;
    private PrincipalService principalService;
    private EventMapper eventMapper;
    private UserMapper userMapper;

    @Transactional
    public void createEvent(EventCreationDto dto) {
        Event event = eventMapper.createEvent(dto);
        User user = principalService.getCurrentUser();
        event.setEventCreator(user);
        eventRepository.save(event);
    }

    @Transactional
    public void joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        User user = principalService.getCurrentUser();
        if(isEventAvailable(event)) {
            event.addParticipant(user);
        }
    }

    @Transactional
    public void leaveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        User user = principalService.getCurrentUser();
        event.removeParticipant(user);
    }

    public Page<EventResponseDto> getEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::mapToEventResponseDto);
    }

    private EventResponseDto mapToEventResponseDto(Event event) {
        EventResponseDto dto = eventMapper.createEventResponseDto(event);
        UserResponseDto creatorDto = userMapper.createUserResponseDto(event.getEventCreator());
        dto.setEventCreator(creatorDto);
        dto.setAvailableSpots(getAvailableSpots(event));
        return dto;
    }

    private boolean isEventAvailable(Event event) {
        return getAvailableSpots(event) > 0;
    }

    private int getAvailableSpots(Event event) {
        return event.getCapacity() - event.getParticipants().size();
    }
}
