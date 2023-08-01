package sport.app.sport_connecting_people.dto.event.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedEventDto {

    private List<EventDto> events;
    private long totalCount;
}