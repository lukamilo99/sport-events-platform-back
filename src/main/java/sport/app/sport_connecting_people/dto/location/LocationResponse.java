package sport.app.sport_connecting_people.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@AllArgsConstructor
@ToString
@Getter
@Setter
public class LocationResponse {
    private String formatted;
    private List<Double> coordinates;
}

