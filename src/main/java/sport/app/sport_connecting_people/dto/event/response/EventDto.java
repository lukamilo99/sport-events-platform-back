package sport.app.sport_connecting_people.dto.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.location.response.LocationResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    //Moglo je da se radi nasedjivanje EventCreateOrUpdateDto , ali ovako je vise citko

    private Long id;
    private String name;
    private Integer capacity;
    private String sport;
    private LocationResponseDto location;
    private LocalDateTime date;
}
