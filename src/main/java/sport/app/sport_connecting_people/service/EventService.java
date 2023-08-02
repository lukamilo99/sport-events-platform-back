package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.*;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.user.AccessDeniedException;
import sport.app.sport_connecting_people.exceptions.event.EventNotFoundException;
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
    public void create(EventUpsertDto dto) {
        Event event = eventMapper.mapToEvent(dto);
        User user = principalService.getCurrentUser();
        event.setEventCreator(user);
        eventRepository.save(event);
    }

    @Transactional
    public void update(EventUpsertDto dto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        eventRepository.save(eventMapper.updateEventData(event, dto));
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

    public PaginatedEventDto searchEvents(String search, String city, String sport, String day, Pageable pageable) {
        Specification<Event> spec = Specification
                .where(EventSpecification.hasCity(city))
                .and(EventSpecification.hasSport(sport))
                .and(EventSpecification.hasName(search))
                .and(EventSpecification.isOnDay(day));

        Page<Event> eventPage = eventRepository.findAll(spec, pageable);
        List<EventDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.mapToEventDto(event))
                .toList();

        PaginatedEventDto response = new PaginatedEventDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }

    public PaginatedMyEventDto searchEventsCreatedByUser(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Event> eventPage = eventRepository.findByEventCreatorId(userId, pageable)
                .orElseThrow(() -> new EventNotFoundException("There are no events for user with id: " + userId));
        List<MyEventDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.mapToMyEventDto(event))
                .toList();

        PaginatedMyEventDto response = new PaginatedMyEventDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }

    public PaginatedMyEventDto searchEventsParticipatedByUser(Pageable pageable) {
        Long userId = principalService.getCurrentUserId();
        Page<Event> eventPage = eventRepository.findByParticipantsId(userId, pageable)
                .orElseThrow(() -> new EventNotFoundException("There are no events for user with id: " + userId));
        List<MyEventDto> eventDtos = eventPage.getContent().stream()
                .map(event -> eventMapper.mapToMyEventDto(event))
                .toList();

        PaginatedMyEventDto response = new PaginatedMyEventDto();
        response.setEvents(eventDtos);
        response.setTotalCount(eventPage.getTotalElements());

        return response;
    }


    public List<EventDto> getLatestEvents() {
        Pageable topFive = PageRequest.of(0, 6);
        Page<Event> eventPage = eventRepository.findAllByOrderByCreationDateDesc(topFive)
                .orElseThrow(() -> new EventNotFoundException("Latest events not found"));
        return eventPage.getContent().stream()
                .map(event -> eventMapper.mapToEventDto(event))
                .toList();
    }

    public EventDetailsDto getEventDetails(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        return mapToEventResponseDetailsDto(event);
    }

    public EventDto getEventForUpdate(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        EventDto dto = eventMapper.mapToEventDto(event);
        dto.setId(null);
        return dto;
    }

    private EventDetailsDto mapToEventResponseDetailsDto(Event event) {
        EventDetailsDto dto = eventMapper.mapToEventDetailsDto(event);
        UserResponseDto creatorDto = userMapper.mapToUserResponseDto(event.getEventCreator());
        List<UserResponseDto> participants = event.getParticipants()
                .stream()
                .map(user -> userMapper.mapToUserResponseDto(user))
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
}
