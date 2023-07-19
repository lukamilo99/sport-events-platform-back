package sport.app.sport_connecting_people.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.UserProfileDto;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.util.CookieUtil;
import sport.app.sport_connecting_people.security.util.JwtUtil;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    @Transactional
    public void register(UserRegistrationDto dto) {
        if(!userAlreadyRegistered(dto.getEmail())) {
            User user = userMapper.createUser(dto);
            user.setRole(roleRepository.findByName("USER"));
            userRepository.save(user);
        }
    }

    public void login(UserLoginDto dto, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        if(auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserPrincipal user = (UserPrincipal) auth.getPrincipal();
            String jwt = jwtUtil.generateToken(user.getId());
            response.addCookie(cookieUtil.getHttpOnlyCookie(jwt));
        }
    }

    public void logout(HttpServletResponse response) {
        response.addCookie(cookieUtil.invalidateHttpOnlyCookie());
    }

    public UserProfileDto me(UserPrincipal userPrincipal) {
        return userMapper.createUserProfile(userPrincipal);
    }

    private boolean userAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}
