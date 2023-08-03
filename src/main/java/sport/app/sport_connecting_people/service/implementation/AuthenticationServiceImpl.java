package sport.app.sport_connecting_people.service.implementation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.util.JwtUtil;
import sport.app.sport_connecting_people.security.model.UserPrincipal;
import sport.app.sport_connecting_people.service.AuthenticationService;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

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
        else {
            throw new UserAlreadyExistsException("User with email " + dto.getEmail() + " already exists.");
        }
    }

    public String login(UserLoginDto dto, HttpServletResponse response) {
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
