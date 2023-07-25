package sport.app.sport_connecting_people.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sport.app.sport_connecting_people.dto.location.LocationResponse;
import sport.app.sport_connecting_people.service.GeoService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/geo")
public class GeoController {

    private GeoService geoService;

    @GetMapping("/autocomplete")
    public ResponseEntity<Mono<List<LocationResponse>>> autocomplete(@RequestParam String query) {
        return ResponseEntity.ok(geoService.autocomplete(query));
    }

    @GetMapping("/address")
    public ResponseEntity<Mono<LocationResponse>> getLocation(@RequestParam double lat, @RequestParam double lon) {
        return ResponseEntity.ok(geoService.getLocation(lat, lon));
    }
}
