package sport.app.sport_connecting_people.dto.event.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.location.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    //Moglo je da se radi nasedjivanje EventCreateOrUpdateDto , ali ovako je vise citko

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer capacity;
    @NotBlank
    private String sport;
    @NotNull
    private LocationDto location;
    @NotNull
    private LocalDateTime date;
}
