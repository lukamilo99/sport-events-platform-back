package sport.app.sport_connecting_people.dto.event;

import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;

import java.sql.Date;

@Getter
@Setter
public class EventResponseDto {

    private Long id;
    private String name;
    private Integer capacity;
    private Integer availableSpots;
    private String sport;
    private String location;
    private Date date;
    private UserResponseDto eventCreator;
}
