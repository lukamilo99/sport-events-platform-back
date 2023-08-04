package sport.app.sport_connecting_people.dto.location;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class LocationDto {

    private String formattedAddress;
    private List<Double> coordinates;
}

