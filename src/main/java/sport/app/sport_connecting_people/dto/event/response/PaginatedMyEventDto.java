package sport.app.sport_connecting_people.dto.event.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedMyEventDto {

    private List<MyEventDto> events;
    private long totalCount;
}
