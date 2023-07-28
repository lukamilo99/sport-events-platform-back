package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.event.*;
import sport.app.sport_connecting_people.entity.Event;
import sport.app.sport_connecting_people.service.EventService;

import java.util.List;

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

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.delete(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping
    public ResponseEntity<PaginatedEventResponseDto> searchEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String day,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(eventService.searchEvents(search, city, sport, day, pageable), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<EventResponseDto>> getLatestEvents() {
        return new ResponseEntity<>(eventService.getLatestEvents(), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsResponseDto> getEventDetails(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEventDetails(eventId), HttpStatus.OK);
    }
}
