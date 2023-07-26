package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.dto.event.EventResponseDto;
import sport.app.sport_connecting_people.dto.event.EventUpdateDto;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.AccessDeniedException;
import sport.app.sport_connecting_people.exceptions.EventNotFoundException;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import sport.app.sport_connecting_people.mapper.EventMapper;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.EventRepository;
import sport.app.sport_connecting_people.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private PrincipalService principalService;
    private EventMapper eventMapper;
    private UserMapper userMapper;

    @Transactional
    public void create(EventCreationDto dto) {
        Event event = eventMapper.createEvent(dto);
        User user = principalService.getCurrentUser();
        event.setEventCreator(user);
        eventRepository.save(event);
    }

    public Event update(EventUpdateDto dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + dto.getEventId()));
        return eventMapper.updateEvent(event, dto);
    }

    @Transactional
    public void join(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        User user = principalService.getCurrentUser();
        if(isEventAvailable(event)) {
            event.addParticipant(user);
        }
    }

    @Transactional
    public void leave(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        User user = principalService.getCurrentUser();
        event.removeParticipant(user);
    }

    @Transactional
    public void removeParticipant(Long eventId, Long participantId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        User participant = userRepository.findById(participantId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + participantId));

        Long currentUserId = principalService.getCurrentUserId();
        if (!event.getEventCreator().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the event creator can remove participants");
        }
        event.removeParticipant(participant);
    }

    public Page<EventResponseDto> getEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::mapToEventResponseDto);
    }

    public List<EventResponseDto> getLatestEvents() {
        Pageable topFive = PageRequest.of(0, 6);
        Page<Event> eventPage = eventRepository.findAllByOrderByCreationDateDesc(topFive)
                .orElseThrow(() -> new EventNotFoundException("Latest events not found"));
        return eventPage.getContent().stream()
                .map(this::mapToEventResponseDto)
                .toList();
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
