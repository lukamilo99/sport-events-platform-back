package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.response.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.request.UserUpdateDto;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.UserService;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateDto dto) {
        userService.update(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        userService.delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(userService.me(userPrincipal), HttpStatus.OK);
    }
}
