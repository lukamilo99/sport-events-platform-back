package sport.app.sport_connecting_people.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sport.app.sport_connecting_people.dto.location.LocationResponse;
import sport.app.sport_connecting_people.mapper.LocationMapper;

import java.util.List;

@Service
public class GeoService {

    @Value("${geoapify.api.key}")
    private String apiKey;
    private WebClient webClient;
    private LocationMapper locationMapper;

    public GeoService(WebClient webClient, LocationMapper locationMapper) {
        this.webClient = webClient;
        this.locationMapper = locationMapper;
    }

    public Mono<List<LocationResponse>> autocomplete(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/geocode/autocomplete")
                        .queryParam("text", query)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> locationMapper.createLocationResponseList(response))
                .doOnNext(response -> System.out.println(response.toString()));
    }

    public Mono<LocationResponse> getLocation(double latitude, double longitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/geocode/reverse")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> locationMapper.createLocationResponseList(response).get(0))
                .doOnNext(response -> System.out.println(response.toString()));
    }
}
