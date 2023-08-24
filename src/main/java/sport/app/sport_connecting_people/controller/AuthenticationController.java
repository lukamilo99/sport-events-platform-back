package sport.app.sport_connecting_people.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.service.specification.AuthenticationService;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto dto) {
        authenticationService.register(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto dto) {
        return new ResponseEntity<>(authenticationService.login(dto), HttpStatus.OK);
    }
}
