package sport.app.sport_connecting_people.mapper;

import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.EventDetailsDto;
import sport.app.sport_connecting_people.dto.event.response.EventDto;
import sport.app.sport_connecting_people.dto.event.response.MyEventDto;
import sport.app.sport_connecting_people.dto.location.response.LocationResponseDto;
import sport.app.sport_connecting_people.entity.Event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventMapper {

    public Event mapToEvent(EventUpsertDto dto) {
        Event event = new Event();
        Map<String, String> locationInfo = formatLocation(dto.getLocation());
        event.setName(dto.getName());
        event.setCapacity(dto.getCapacity());
        event.setStreetName(locationInfo.get("streetName"));
        event.setCity(locationInfo.get("city"));
        event.setCoordinatesLon(dto.getLocation().getCoordinates().get(0));
        event.setCoordinatesLat(dto.getLocation().getCoordinates().get(1));
        event.setSport(dto.getSport());
        event.setDate(dto.getDate());
        event.setCreationDate(LocalDateTime.now());
        return event;
    }

    public Event updateEventData(Event event, EventUpsertDto dto) {
        Map<String, String> locationInfo = formatLocation(dto.getLocation());
        event.setCapacity(dto.getCapacity());
        event.setName(dto.getName());
        event.setSport(dto.getSport());
        event.setDate(dto.getDate());
        event.setStreetName(locationInfo.get("streetName"));
        event.setCity(locationInfo.get("city"));
        event.setCoordinatesLon(dto.getLocation().getCoordinates().get(0));
        event.setCoordinatesLat(dto.getLocation().getCoordinates().get(1));
        return event;
    }

    public EventDto mapToEventDto(Event event) {
        EventDto dto = new EventDto();
        mapCommonEventAttributes(event, dto);
        return dto;
    }

    public MyEventDto mapToMyEventDto(Event event) {
        MyEventDto dto = new MyEventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setEventPast(event.getDate().isBefore(LocalDateTime.now()));
        return dto;
    }

    public EventDetailsDto mapToEventDetailsDto(Event event) {
        EventDetailsDto dto = new EventDetailsDto();
        mapCommonEventAttributes(event, dto);
        return dto;
    }

    private void mapCommonEventAttributes(Event event, EventDto dto) {
        dto.setId(event.getId());
        dto.setLocation(new LocationResponseDto(
                event.getCity(),
                event.getStreetName() + ", " + event.getCity(),
                List.of(event.getCoordinatesLat(),
                        event.getCoordinatesLon())
        ));
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setSport(event.getSport());
        dto.setCapacity(event.getCapacity());
    }

    private Map<String, String> formatLocation(LocationResponseDto locationResponseDto) {
        Map<String, String> locationInfo = new HashMap<>();
        String[] locationParts = locationResponseDto.getFormattedAddress().split(",");
        if (locationParts.length == 2) {
            locationInfo.put("streetName", "Unknown");
            locationInfo.put("city", locationParts[0].replaceAll("\\d", "").trim());
        }
        else if (locationParts.length == 3) {
            locationInfo.put("streetName", locationParts[0].trim());
            locationInfo.put("city", locationParts[1].replaceAll("\\d", "").trim());
        }
        else {
            locationInfo.put("streetName", "Unknown");
            locationInfo.put("city", "Unknown");
        }
        return locationInfo;
    }
}
