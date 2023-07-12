package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.dto.event.EventResponseDto;
import sport.app.sport_connecting_people.entity.Event;

@AllArgsConstructor
@Component
public class EventMapper {

    public Event createEvent(EventCreationDto dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setCapacity(dto.getCapacity());
        event.setLocation(dto.getLocation());
        event.setSport(dto.getSport());
        event.setDate(dto.getDate());
        return event;
    }

    public EventResponseDto createEventResponseDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setCapacity(event.getCapacity());
        dto.setLocation(event.getLocation());
        dto.setName(dto.getName());
        dto.setDate(dto.getDate());
        return dto;
    }
}
