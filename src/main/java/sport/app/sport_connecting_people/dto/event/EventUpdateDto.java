package sport.app.sport_connecting_people.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventUpdateDto extends EventCreationDto{

    @NotNull
    private Long eventId;
}
