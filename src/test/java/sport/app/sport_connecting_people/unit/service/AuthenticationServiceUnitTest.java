package sport.app.sport_connecting_people.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.entity.Role;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.exceptions.authentication.CustomAuthenticationException;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.mapper.UserMapper;
import sport.app.sport_connecting_people.repository.RoleRepository;
import sport.app.sport_connecting_people.repository.UserRepository;
import sport.app.sport_connecting_people.service.specification.PrincipalService;
import sport.app.sport_connecting_people.service.implementation.AuthenticationServiceImpl;
import sport.app.sport_connecting_people.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;
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
    PrincipalService principalService;

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
        when(userMapper.mapToUser(dto)).thenReturn(user);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);

        authenticationService.register(dto);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(role, user.getRole());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userMapper, times(1)).mapToUser(dto);
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
        verify(userRepository, times(1)).existsByEmail(any());
        verify(userMapper, times(0)).mapToUser(any());
        verify(passwordEncoder, times(0)).encode(any());
        verify(roleRepository, times(0)).findByName(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void login_validCredentials_LoginSuccess() {
        UserLoginDto dto = new UserLoginDto();
        dto.setEmail("test@gmail.com");
        dto.setPassword("Qwerasdf1");

        Authentication mockAuth = mock(Authentication.class);
        Long userId = 1L;

        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        doNothing().when(principalService).setCurrentUserPrincipal(mockAuth);
        when(principalService.getCurrentUserId()).thenReturn(userId);
        when(jwtUtil.generateToken(userId)).thenReturn("jwt");

        String token = authenticationService.login(dto);

        assertEquals("jwt", token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(principalService, times(1)).setCurrentUserPrincipal(mockAuth);
        verify(principalService, times(1)).getCurrentUserId();
        verify(jwtUtil, times(1)).generateToken(userId);
    }

    @Test
    public void login_invalidCredentials_LoginFail() {
        UserLoginDto dto = new UserLoginDto();
        dto.setEmail("test@gmail.com");
        dto.setPassword("Qwerasdf1");

        when(authenticationManager.authenticate(any())).thenThrow(new CustomAuthenticationException("Bad credentials"));

        assertThrows(CustomAuthenticationException.class, () -> authenticationService.login(dto));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(principalService, times(0)).setCurrentUserPrincipal(any());
        verify(principalService, times(0)).getCurrentUserId();
        verify(jwtUtil, times(0)).generateToken(any());
    }
}
