package sport.app.sport_connecting_people.service.specification;

import reactor.core.publisher.Mono;
import sport.app.sport_connecting_people.dto.location.response.LocationResponseDto;

import java.util.List;

public interface GeoService {

    Mono<List<LocationResponseDto>> autocompleteLocation(String query);

    Mono<LocationResponseDto> getLocation(double latitude, double longitude);
}
