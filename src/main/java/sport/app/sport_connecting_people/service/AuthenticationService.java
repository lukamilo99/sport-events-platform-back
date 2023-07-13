package sport.app.sport_connecting_people.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.security.jwt.JwtUtil;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
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

    public String login(UserLoginDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        if(auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserPrincipal user = (UserPrincipal) auth.getPrincipal();
            return jwtUtil.generateToken(user.getId());
        }
        return null;
    }

    private boolean userAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}