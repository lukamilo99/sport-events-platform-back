package sport.app.sport_connecting_people.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.service.implementation.facade.UserInteractionFacade;
import sport.app.sport_connecting_people.service.specification.FriendshipService;

@AllArgsConstructor
@RequestMapping("/friends")
@RestController
public class FriendshipController {

    private FriendshipService friendshipService;
    private UserInteractionFacade userInteractionFacade;

    @PostMapping("/create/{responderId}")
    public ResponseEntity<Void> createFriendship(@PathVariable Long responderId) {
        userInteractionFacade.sendFriendRequest(responderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user-friends")
    public ResponseEntity<PaginatedUserResponseDto> getUserFriends(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(friendshipService.getUserFriends(name, pageable), HttpStatus.OK);
    }

    @PutMapping("/update/{friendshipId}")
    public ResponseEntity<Void> updateFriendship(@PathVariable Long friendshipId, @RequestParam boolean status) {
        friendshipService.updateFriendshipStatus(friendshipId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{responderId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long responderId) {
        friendshipService.deleteFriendship(responderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
