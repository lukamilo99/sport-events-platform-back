package sport.app.sport_connecting_people.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sport.app.sport_connecting_people.dto.user.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.AuthenticationService;

import java.io.IOException;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationDto dto) {
        authenticationService.register(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto dto, HttpServletResponse response) {
        return new ResponseEntity<>(authenticationService.login(dto, response), HttpStatus.OK);
    }
}
