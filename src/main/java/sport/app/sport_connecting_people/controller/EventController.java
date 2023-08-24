package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.event.request.EventUpsertDto;
import sport.app.sport_connecting_people.dto.event.response.EventDetailsDto;
import sport.app.sport_connecting_people.dto.event.response.EventDto;
import sport.app.sport_connecting_people.dto.event.response.PaginatedEventDto;
import sport.app.sport_connecting_people.dto.event.response.PaginatedMyEventDto;
import sport.app.sport_connecting_people.service.specification.EventService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/event")
@RestController
public class EventController {

    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventUpsertDto dto) {
        eventService.createEvent(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEvent(eventId), HttpStatus.OK);
    }

    @PutMapping("/update/{eventId}")
    public ResponseEntity<Void> updateEvent(@Valid @RequestBody EventUpsertDto dto, @PathVariable Long eventId) {
        eventService.updateEvent(dto, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/details/{eventId}")
    public ResponseEntity<EventDetailsDto> getEventDetails(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEventDetails(eventId), HttpStatus.OK);
    }

    @PostMapping("/join/{eventId}")
    public ResponseEntity<Void> joinEvent(@PathVariable Long eventId) {
        eventService.joinEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/leave/{eventId}")
    public ResponseEntity<Void> leaveEvent(@PathVariable Long eventId) {
        eventService.leaveEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove/{eventId}/{participantId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        eventService.removeParticipantFromEvent(eventId, participantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search-events")
    public ResponseEntity<PaginatedEventDto> searchEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String day,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(eventService.searchEvents(search, city, sport, day, pageable), HttpStatus.OK);
    }

    @GetMapping("/user-creator")
    public ResponseEntity<PaginatedMyEventDto> searchUserCreator(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "false") boolean includePastEvents) {

        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(eventService.searchEventsCreatedByUser(pageable, includePastEvents), HttpStatus.OK);
    }

    @GetMapping("/user-participant")
    public ResponseEntity<PaginatedMyEventDto> searchUserParticipant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam boolean includePastEvents) {

        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(eventService.searchEventsParticipatedByUser(pageable, includePastEvents), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<EventDto>> getLatestEvents(@RequestParam(required = false) String city) {
        Pageable pageable = PageRequest.of(0, 6);
        return new ResponseEntity<>(eventService.getLatestEvents(city, pageable), HttpStatus.OK);
    }
}
