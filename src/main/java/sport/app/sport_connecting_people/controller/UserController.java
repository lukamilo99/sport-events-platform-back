package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserProfileDto;
import sport.app.sport_connecting_people.dto.user.response.PaginatedUserResponseDto;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.specification.UserService;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateDto dto) {
        userService.updateUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/ban/{userId}")
    public ResponseEntity<Void> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/unban/{userId}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        userService.unbanUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/private/search-users")
    public ResponseEntity<PaginatedUserProfileDto> searchUsersDetails(@RequestParam(required = false) String name,
                                                               @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(userService.searchUsersDetails(name, pageable), HttpStatus.OK);
    }

    @GetMapping("/public/search-users")
    public ResponseEntity<PaginatedUserResponseDto> searchUsers(@RequestParam(required = false) String name,
                                                                @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        return new ResponseEntity<>(userService.searchUsers(name, pageable), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getPrincipal(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.getPrincipal(userPrincipal), HttpStatus.OK);
    }
}
