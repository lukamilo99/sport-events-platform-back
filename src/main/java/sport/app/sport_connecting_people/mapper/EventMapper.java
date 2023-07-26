package sport.app.sport_connecting_people.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.dto.event.EventResponseDto;
import sport.app.sport_connecting_people.dto.event.EventUpdateDto;
import sport.app.sport_connecting_people.dto.location.LocationFromApiDto;
import sport.app.sport_connecting_people.entity.Event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
public class EventMapper {

    public Event createEvent(EventCreationDto dto) {
        Event event = new Event();
        Map<String, String> locationInfo = formatLocation(dto.getLocation());
        event.setName(dto.getName());
        event.setCapacity(dto.getCapacity());
        event.setStreetName(locationInfo.get("streetName"));
        event.setCity(locationInfo.get("city"));
        event.setCoordinatesLat(dto.getLocation().getCoordinates().get(0));
        event.setCoordinatesLon(dto.getLocation().getCoordinates().get(1));
        event.setSport(dto.getSport());
        event.setDate(dto.getDate());
        event.setCreationDate(LocalDateTime.now());
        return event;
    }

    public Event updateEvent(Event event, EventUpdateDto dto) {
        Map<String, String> locationInfo = formatLocation(dto.getLocation());
        event.setCapacity(dto.getCapacity());
        event.setName(dto.getName());
        event.setSport(dto.getSport());
        event.setDate(dto.getDate());
        event.setStreetName(locationInfo.get("streetName"));
        event.setCity(locationInfo.get("city"));
        event.setCoordinatesLat(dto.getLocation().getCoordinates().get(0));
        event.setCoordinatesLon(dto.getLocation().getCoordinates().get(1));
        return event;
    }

    public EventResponseDto createEventResponseDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setCapacity(event.getCapacity());
        dto.setStreetName(event.getStreetName());
        dto.setCity(event.getCity());
        dto.setCoordinatesLat(event.getCoordinatesLat());
        dto.setCoordinatesLon(event.getCoordinatesLon());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setSport(event.getSport());
        return dto;
    }

    private Map<String, String> formatLocation(LocationFromApiDto locationDto) {
        Map<String, String> locationInfo = new HashMap<>();
        String[] locationParts = locationDto.getFormatted().split(",");
        if (locationParts.length == 2) {
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
