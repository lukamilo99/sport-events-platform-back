package sport.app.sport_connecting_people.dto.event;

import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseDto {

    private Long id;
    private String name;
    private Integer capacity;
    private Integer availableSpots;
    private String sport;
    private String streetName;
    private String city;
    private Double coordinatesLat;
    private Double coordinatesLon;
    private LocalDateTime date;
    private UserResponseDto eventCreator;
}
