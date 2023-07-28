package sport.app.sport_connecting_people.dto.event;

import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.user.UserResponseDto;

import java.util.List;

@Getter
@Setter
public class EventDetailsResponseDto extends EventResponseDto {
    private Integer capacity;
    private Integer availableSpots;
    private UserResponseDto eventCreator;
    private List<UserResponseDto> participants;
}
