package sport.app.sport_connecting_people.dto.location.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class LocationResponseDto {

    private String city;
    private String formattedAddress;
    private List<Double> coordinates;
}

