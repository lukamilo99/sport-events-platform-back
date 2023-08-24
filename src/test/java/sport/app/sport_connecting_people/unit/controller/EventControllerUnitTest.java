package sport.app.sport_connecting_people.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sport.app.sport_connecting_people.controller.EventController;
import sport.app.sport_connecting_people.unit.config.SecurityPermitAllConfig;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.EventDetailsDto;
import sport.app.sport_connecting_people.dto.location.response.LocationResponseDto;
import sport.app.sport_connecting_people.exceptions.event.EventFullException;
import sport.app.sport_connecting_people.exceptions.event.EventNotFoundException;
import sport.app.sport_connecting_people.security.filter.JwtAuthenticationFilter;
import sport.app.sport_connecting_people.service.specification.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@Import(SecurityPermitAllConfig.class)
public class EventControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    public void createEvent_WithValidDto_ShouldReturnOk() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();

        doNothing().when(eventService).createEvent(dto);

        mockMvc.perform(post("/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventService, Mockito.times(1)).createEvent(eq(dto));
    }

    @ParameterizedTest
    @MethodSource("getInvalidEventUpsertDtos")
    public void createEvent_WithInvalidDto_ShouldReturnBadRequest(EventUpsertDto dto) throws Exception {
        mockMvc.perform(post("/event/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(eventService, Mockito.never()).createEvent(any(EventUpsertDto.class));
    }

    @Test
    public void updateEvent_WithValidDtoAndPresentEventId_ShouldReturnOk() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();
        Long eventId = 1L;

        doNothing().when(eventService).updateEvent(dto, eventId);

        mockMvc.perform(put("/event/update/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventService, Mockito.times(1)).updateEvent(eq(dto), eq(eventId));
    }

    @ParameterizedTest
    @MethodSource("getInvalidEventUpsertDtos")
    public void updateEvent_WithInvalidDtoAndPresentEventId_ShouldReturnBadRequest(EventUpsertDto dto) throws Exception {
        Long eventId = 1L;

        mockMvc.perform(put("/event/update/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(eventService, Mockito.never()).updateEvent(any(EventUpsertDto.class), eq(eventId));
    }

    @Test
    public void updateEvent_ValidDtoAndMissingEventId_ShouldReturnNotFound() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();

        mockMvc.perform(put("/event/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(eventService, Mockito.never()).updateEvent(any(EventUpsertDto.class), any(Long.class));
    }

    @Test
    public void updateEvent_ValidDtoAndPresentEventIdAndMissingEvent_ShouldReturnNotFound() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();
        Long eventId = 1L;

        Mockito.doThrow(new EventNotFoundException("Event not found")).when(eventService).updateEvent(dto, eventId);

        mockMvc.perform(put("/event/update/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(eventService, Mockito.times(1)).updateEvent(eq(dto), eq(eventId));
    }

    @Test
    public void joinEvent_PresentEventId_ShouldReturnOk() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();
        Long eventId = 1L;

        doNothing().when(eventService).joinEvent(eventId);

        mockMvc.perform(post("/event/join/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventService, Mockito.times(1)).joinEvent(eq(eventId));
    }

    @Test
    public void joinEvent_PresentEventIdAndMissingEvent_ShouldReturnNotFound() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();
        Long eventId = 1L;

        Mockito.doThrow(new EventNotFoundException("Event not found")).when(eventService).joinEvent(eventId);

        mockMvc.perform(post("/event/join/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(eventService, Mockito.times(1)).joinEvent(eq(eventId));
    }

    @Test
    public void joinEvent_PresentEventIdAndEventFull_ShouldReturnNotFound() throws Exception {
        EventUpsertDto dto = getValidEventUpsertDto();
        Long eventId = 1L;

        Mockito.doThrow(new EventFullException("Event is full")).when(eventService).joinEvent(eventId);

        mockMvc.perform(post("/event/join/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        verify(eventService, Mockito.times(1)).joinEvent(eq(eventId));
    }

    @Test
    public void getEventDetails_PresentEventId_ShouldReturnEventDetailsDto() throws Exception {
        EventDetailsDto dto = getEventDetailsDto();
        Long eventId = 1L;

        Mockito.when(eventService.getEventDetails(eventId)).thenReturn(getEventDetailsDto());

        mockMvc.perform(get("/event/details/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Event")))
                .andExpect(jsonPath("$.sport", Matchers.is("Sport")));

        verify(eventService, Mockito.times(1)).getEventDetails(eq(eventId));
    }

    private static Stream<EventUpsertDto> getInvalidEventUpsertDtos() {
        EventUpsertDto nullFieldDto = getValidEventUpsertDto();
        nullFieldDto.setLocation(null);

        EventUpsertDto emptyFieldDto = getValidEventUpsertDto();
        emptyFieldDto.setName("");

        return Stream.of(nullFieldDto, emptyFieldDto);
    }

    private static EventUpsertDto getValidEventUpsertDto() {
        EventUpsertDto dto = new EventUpsertDto();
        dto.setName("Event");
        dto.setSport("Sport");
        dto.setCapacity(10);
        dto.setLocation(new LocationResponseDto("City", "Address", List.of(0.5, 0.5)));
        dto.setDate(LocalDateTime.now());
        return dto;
    }

    private EventDetailsDto getEventDetailsDto() {
        EventDetailsDto dto = new EventDetailsDto();
        dto.setName("Event");
        dto.setSport("Sport");
        return dto;
    }
}
