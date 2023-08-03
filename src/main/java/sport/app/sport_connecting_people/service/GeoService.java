package sport.app.sport_connecting_people.service;

import reactor.core.publisher.Mono;
import sport.app.sport_connecting_people.dto.location.LocationDto;

import java.util.List;

public interface GeoService {

    Mono<List<LocationDto>> autocompleteLocation(String query);

    Mono<LocationDto> getLocation(double latitude, double longitude);
}
