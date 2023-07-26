package sport.app.sport_connecting_people.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.location.LocationFromApiDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventUpdateDto {

    @NotNull
    private Long eventId;
    @NotBlank
    private String name;
    @NotNull
    private Integer capacity;
    @NotBlank
    private String sport;
    @NotBlank
    private LocationFromApiDto location;
    @NotNull
    private LocalDateTime date;
}
