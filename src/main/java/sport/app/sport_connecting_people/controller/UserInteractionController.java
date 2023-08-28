package sport.app.sport_connecting_people.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.service.implementation.facade.UserInteractionFacade;

@AllArgsConstructor
@RequestMapping("/interaction")
@RestController
public class UserInteractionController {

    private final UserInteractionFacade userInteractionFacade;

    @PostMapping ("/friend-request/{recipientId}")
    public ResponseEntity<Long> sendFriendRequest(@PathVariable Long recipientId) {
        Long requestId = userInteractionFacade.sendFriendRequest(recipientId);
        return ResponseEntity.ok(requestId);
    }

    @PostMapping("/event-invitation/{eventId}/{recipientId}")
    public ResponseEntity<Void> sendEventInvitationRequest(@PathVariable Long eventId, @PathVariable Long recipientId) {
        userInteractionFacade.sendEventInvitationRequest(eventId, recipientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/{requestId}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long requestId) {
        userInteractionFacade.acceptRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/{requestId}/decline")
    public ResponseEntity<Void> declineRequest(@PathVariable Long requestId) {
        userInteractionFacade.declineRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/request/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long requestId) {
        userInteractionFacade.cancelRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
