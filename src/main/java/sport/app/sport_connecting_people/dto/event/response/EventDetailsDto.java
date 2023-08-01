package sport.app.sport_connecting_people.dto.event.response;

import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.user.response.UserResponseDto;

import java.util.List;

@Getter
@Setter
public class EventDetailsDto extends EventDto {

    private Integer availableSpots;
    private UserResponseDto eventCreator;
    private List<UserResponseDto> participants;
}
