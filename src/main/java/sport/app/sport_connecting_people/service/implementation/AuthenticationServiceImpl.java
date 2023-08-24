package sport.app.sport_connecting_people.service.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.authentication.CustomAuthenticationException;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.service.specification.PrincipalService;
import sport.app.sport_connecting_people.util.JwtUtil;
import sport.app.sport_connecting_people.service.specification.AuthenticationService;
import org.springframework.security.core.AuthenticationException;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private PrincipalService principalService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Transactional
    @Override
    public void register(UserRegistrationDto dto) {
        if(!userAlreadyRegistered(dto.getEmail())) {
            User user = userMapper.mapToUser(dto);
            user.setEncodedPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(roleRepository.findByName("USER"));
            userRepository.save(user);
        }
        else {
            throw new UserAlreadyExistsException("User with email " + dto.getEmail() + " already exists.");
        }
    }

    @Override
    public String login(UserLoginDto dto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            principalService.setCurrentUserPrincipal(authentication);
            Long userId = principalService.getCurrentUserId();
            return jwtUtil.generateToken(userId);
        }
        catch (AuthenticationException ex) {
            throw new CustomAuthenticationException("Bad credentials");
        }
    }

    private boolean userAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}
