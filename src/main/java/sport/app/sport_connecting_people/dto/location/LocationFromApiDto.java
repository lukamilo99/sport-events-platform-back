package sport.app.sport_connecting_people.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationFromApiDto {

    private String formatted;
    private List<Double> coordinates;
}

