package sport.app.sport_connecting_people.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
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

    @BeforeEach
    void setup() {
        event = new Event();
        user = new User();
        eventUpsertDto = new EventUpsertDto();
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
        verify(eventRepository, times(0)).save(event);
    }

    @Test
    public void createEvent_userNotFoundException_creationFail() {
        when(eventMapper.mapToEvent(eventUpsertDto)).thenReturn(event);
        when(principalService.getCurrentUser()).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> eventService.createEvent(eventUpsertDto));
        assertNull(event.getEventCreator());
        verify(eventMapper, times(1)).mapToEvent(eventUpsertDto);
        verify(principalService, times(1)).getCurrentUser();
        verify(eventRepository, times(0)).save(event);
    }
}
