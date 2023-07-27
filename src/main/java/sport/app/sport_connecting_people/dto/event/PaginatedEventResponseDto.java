package sport.app.sport_connecting_people.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedEventResponseDto {

    private List<EventResponseDto> events;
    private long totalCount;
}