package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.event.EventCreationDto;
import sport.app.sport_connecting_people.dto.event.EventResponseDto;
import sport.app.sport_connecting_people.dto.event.EventUpdateDto;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.service.EventService;

@AllArgsConstructor
@RequestMapping("/event")
@RestController
public class EventController {

    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventCreationDto dto) {
        eventService.create(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody EventUpdateDto dto) {
        return new ResponseEntity<>(eventService.update(dto), HttpStatus.OK);
    }

    @PostMapping("/join/{eventId}")
    public ResponseEntity<Void> joinEvent(@PathVariable Long eventId) {
        eventService.join(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/leave/{eventId}")
    public ResponseEntity<Void> leaveEvent(@PathVariable Long eventId) {
        eventService.leave(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove/{eventId}/{participantId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        eventService.removeParticipant(eventId, participantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<EventResponseDto>> getEvents(Pageable pageable) {
        return new ResponseEntity<>(eventService.getEvents(pageable), HttpStatus.OK);
    }
}
