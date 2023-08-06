package sport.app.sport_connecting_people.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sport.app.sport_connecting_people.dto.location.LocationDto;
import sport.app.sport_connecting_people.mapper.LocationMapper;
import sport.app.sport_connecting_people.service.GeoService;

import java.util.List;

@Service
public class GeoServiceImpl implements GeoService {

    @Value("${geoapify.api.key}")
    private String apiKey;
    private WebClient webClient;
    private LocationMapper locationMapper;

    public GeoServiceImpl(WebClient webClient, LocationMapper locationMapper) {
        this.webClient = webClient;
        this.locationMapper = locationMapper;
    }

    @Override
    public Mono<List<LocationDto>> autocompleteLocation(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/geocode/autocomplete")
                        .queryParam("text", query)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> locationMapper.createLocationResponseList(response));
    }

    @Override
    public Mono<LocationDto> getLocation(double latitude, double longitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/geocode/reverse")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> locationMapper.createLocationResponseList(response).get(0));
    }
}
