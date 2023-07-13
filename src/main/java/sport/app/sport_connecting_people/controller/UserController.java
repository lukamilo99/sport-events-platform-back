package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.UserUpdateDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.service.UserService;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody UserUpdateDto dto) {
        return new ResponseEntity<>(userService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete() {
        userService.delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}