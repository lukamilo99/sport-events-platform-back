package sport.app.sport_connecting_people.dto.event.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sport.app.sport_connecting_people.dto.location.LocationDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventUpsertDto {

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
