package sport.app.sport_connecting_people.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.Role;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.service.implementation.AuthenticationServiceImpl;
import sport.app.sport_connecting_people.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void register_UserNotAlreadyRegistered_RegisterSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@gmail.com");
        User user = new User();
        user.setPassword("Qwerasdf1");
        Role role = new Role();
        role.setName("USER");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.createUser(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);

        authenticationService.register(dto);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(role, user.getRole());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userMapper, times(1)).createUser(dto);
        verify(passwordEncoder, times(1)).encode(dto.getPassword());
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void register_UserAlreadyRegistered_RegisterFail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@gmail.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(dto));
    }

    @Test
    public void login_UserNotAlreadyRegistered_RegisterSuccess() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@gmail.com");
        User user = new User();
        user.setPassword("Qwerasdf1");
        Role role = new Role();
        role.setName("USER");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.createUser(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);

        authenticationService.register(dto);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(role, user.getRole());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userMapper, times(1)).createUser(dto);
        verify(passwordEncoder, times(1)).encode(dto.getPassword());
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(user);
    }
}
