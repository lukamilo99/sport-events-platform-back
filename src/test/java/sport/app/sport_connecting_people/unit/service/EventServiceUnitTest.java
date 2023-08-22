package sport.app.sport_connecting_people.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.EventDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.authentication.CustomAuthenticationException;
import sport.app.sport_connecting_people.exceptions.user.UserNotFoundException;
import sport.app.sport_connecting_people.mapper.EventMapper;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.EventRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.service.PrincipalService;
import sport.app.sport_connecting_people.service.implementation.EventServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrincipalService principalService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserMapper userMapper;

    private Event event;
    private User user;
    private EventUpsertDto eventUpsertDto;
    private EventDto eventDto;

    @BeforeEach
    void setup() {
        event = new Event();
        user = new User();
        eventUpsertDto = new EventUpsertDto();
        eventDto = new EventDto();
    }

    @Test
    public void createEvent_noException_creationSuccess() {
        when(eventMapper.mapToEvent(eventUpsertDto)).thenReturn(event);
        when(principalService.getCurrentUser()).thenReturn(user);

        eventService.createEvent(eventUpsertDto);

        assertEquals(event.getEventCreator(), user);
        verify(eventMapper, times(1)).mapToEvent(eventUpsertDto);
        verify(principalService, times(1)).getCurrentUser();
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void createEvent_customAuthenticationException_creationFail() {
        when(eventMapper.mapToEvent(eventUpsertDto)).thenReturn(event);
        when(principalService.getCurrentUser()).thenThrow(new CustomAuthenticationException("User not properly authenticated"));

        assertThrows(CustomAuthenticationException.class, () -> eventService.createEvent(eventUpsertDto));
        assertNull(event.getEventCreator());
        verify(eventMapper, times(1)).mapToEvent(eventUpsertDto);
        verify(principalService, times(1)).getCurrentUser();
        verify(eventRepository, Mockito.never()).save(event);
    }

    @Test
    public void createEvent_userNotFoundException_creationFail() {
        when(eventMapper.mapToEvent(eventUpsertDto)).thenReturn(event);
        when(principalService.getCurrentUser()).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> eventService.createEvent(eventUpsertDto));
        assertNull(event.getEventCreator());
        verify(eventMapper, times(1)).mapToEvent(eventUpsertDto);
        verify(principalService, times(1)).getCurrentUser();
        verify(eventRepository, Mockito.never()).save(event);
    }

    //izmenjena metoda, mora da se menja test

    @Test
    @Disabled
    public void getLatestEvents_eventPageEmpty_fetchingFail() {
        Page<Event> emptyEventPage = new PageImpl<>(Collections.emptyList());
        List<EventDto> emptyList = List.of();
        Pageable pageable = Pageable.ofSize(6);

        when(eventRepository.findAllByCityOrderByCreationDateDesc(anyString(), any())).thenReturn(emptyEventPage);

        List<EventDto> result = eventService.getLatestEvents("City", pageable);

        assertEquals(0, result.size());
        assertEquals(emptyList, result);
        verify(eventRepository, times(1)).findAllByCityOrderByCreationDateDesc(anyString(), any());
        verify(eventMapper, Mockito.never()).mapToEventDto(any());
    }

    //izmenjena metoda, mora da se menja test

    @Test
    @Disabled
    public void getLatestEvents_eventPageNotEmpty_fetchingSuccess() {
        Page<Event> populatedEventPage = new PageImpl<>(List.of(event));
        List<EventDto> populatedList = List.of(eventDto);

        when(eventRepository.findAllByCityOrderByCreationDateDesc(anyString(), any())).thenReturn(populatedEventPage);
        when(eventMapper.mapToEventDto(populatedEventPage.getContent().get(0))).thenReturn(eventDto);

        List<EventDto> response = eventService.getLatestEvents("City", any());

        assertEquals(1, response.size());
        assertEquals(populatedList, response);
        verify(eventRepository, times(1)).findAllByCityOrderByCreationDateDesc(anyString(), any());
        verify(eventMapper, times(1)).mapToEventDto(any());
    }
}
