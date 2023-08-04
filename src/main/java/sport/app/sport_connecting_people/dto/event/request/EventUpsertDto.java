package sport.app.sport_connecting_people.dto.event.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sport.app.sport_connecting_people.dto.location.LocationDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
