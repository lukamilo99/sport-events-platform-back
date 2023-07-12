package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.service.EventService;

@AllArgsConstructor
@RequestMapping("/event")
@RestController
public class EventController {

    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventCreationDto dto) {
        eventService.createEvent(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
