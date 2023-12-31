package sport.app.sport_connecting_people.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.service.implementation.facade.UserRelationFacade;
import sport.app.sport_connecting_people.service.specification.FriendshipService;

@AllArgsConstructor
@RequestMapping("/friends")
@RestController
public class FriendshipController {

    private FriendshipService friendshipService;
    private UserRelationFacade userRelationFacade;

    @GetMapping("/user-friends")
    public ResponseEntity<PaginatedUserResponseDto> getUserFriends(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(friendshipService.getUserFriends(name, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{friendshipId}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long friendshipId) {
        userRelationFacade.deleteFriendship(friendshipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
