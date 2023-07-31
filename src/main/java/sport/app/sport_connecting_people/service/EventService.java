package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.event.*;
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
import sport.app.sport_connecting_people.specification.EventSpecification;

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

    @Transactional
    public Event update(EventUpdateDto dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + dto.getEventId()));
        return eventMapper.updateEvent(event, dto);
    }

    @Transactional
    public void delete(Long eventId) {
        eventRepository.deleteById(eventId);
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

    public PaginatedEventResponseDto searchEvents(String search, String city, String sport, String day, Pageable pageable) {
        Specification<Event> spec = Specification
                .where(EventSpecification.hasCity(city))
                .and(EventSpecification.hasSport(sport))
                .and(EventSpecification.hasName(search))
                .and(EventSpecification.isOnDay(day));

        Page<Event> eventPage = eventRepository.findAll(spec, pageable);
        List<EventResponseDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.createEventResponseDto(event))
                .toList();

        PaginatedEventResponseDto response = new PaginatedEventResponseDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }

    public PaginatedEventResponseDto searchEventsCreatedByUser(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Event> eventPage = eventRepository.findByEventCreatorId(userId, pageable)
                .orElseThrow(() -> new EventNotFoundException("There are no events for user with id: " + userId));
        List<EventResponseDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.createEventResponseDto(event))
                .toList();

        PaginatedEventResponseDto response = new PaginatedEventResponseDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }

    public PaginatedEventResponseDto searchEventsParticipatedByUser(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Event> eventPage = eventRepository.findByParticipantsId(userId, pageable)
                .orElseThrow(() -> new EventNotFoundException("There are no events for user with id: " + userId));
        List<EventResponseDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.createEventResponseDto(event))
                .toList();

        PaginatedEventResponseDto response = new PaginatedEventResponseDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }


    public List<EventResponseDto> getLatestEvents() {
        Pageable topFive = PageRequest.of(0, 6);
        Page<Event> eventPage = eventRepository.findAllByOrderByCreationDateDesc(topFive)
                .orElseThrow(() -> new EventNotFoundException("Latest events not found"));
        return eventPage.getContent().stream()
                .map(event -> eventMapper.createEventResponseDto(event))
                .toList();
    }

    public EventDetailsResponseDto getEventDetails(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        return mapToEventResponseDetailsDto(event);
    }

    private EventDetailsResponseDto mapToEventResponseDetailsDto(Event event) {
        EventDetailsResponseDto dto = eventMapper.createEventDetailsResponseDto(event);
        UserResponseDto creatorDto = userMapper.createUserResponseDto(event.getEventCreator());
        List<UserResponseDto> participants = event.getParticipants()
                .stream()
                .map(user -> userMapper.createUserResponseDto(user))
                .toList();
        dto.setParticipants(participants);
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

    private long getTotalCount(Specification<Event> spec) {
        return eventRepository.count(spec);
    }
}
