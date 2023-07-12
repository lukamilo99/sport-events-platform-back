package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.entity.Event;

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
}
