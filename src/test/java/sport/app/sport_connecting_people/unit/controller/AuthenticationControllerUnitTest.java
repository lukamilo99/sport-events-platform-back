package sport.app.sport_connecting_people.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sport.app.sport_connecting_people.controller.AuthenticationController;
import sport.app.sport_connecting_people.config.SecurityPermitAllConfig;
import sport.app.sport_connecting_people.dto.user.request.UserLoginDto;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.security.filter.JwtAuthenticationFilter;
import sport.app.sport_connecting_people.service.AuthenticationService;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@Import(SecurityPermitAllConfig.class)
public class AuthenticationControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void register_WithExistingEmail_ShouldThrowException() throws Exception {
        UserRegistrationDto dto = getValidRegisterDto();

        Mockito.doThrow(UserAlreadyExistsException.class).when(authenticationService).register(any(UserRegistrationDto.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        verify(authenticationService, Mockito.times(1)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void register_WithValidDto_ShouldReturnOk() throws Exception {
        UserRegistrationDto dto = getValidRegisterDto();

        doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(authenticationService, Mockito.times(1)).register(eq(dto));
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterDtos")
    public void register_WithInvalidDto_ShouldReturnBadRequest(UserRegistrationDto dto) throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, Mockito.never()).register(any(UserRegistrationDto.class));
    }

    @Test
    public void login_WithValidDto_ShouldReturnOk() throws Exception {
        UserLoginDto dto = getValidLoginDto();

        Mockito.when(authenticationService.login(eq(dto))).thenReturn("jwtTest");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwtTest"));

        verify(authenticationService, Mockito.times(1)).login(eq(dto));
    }

    @Test
    public void login_WithInvalidDto_ShouldReturnBadRequest() throws Exception {
        UserLoginDto dto = getEmptyFieldLoginDto();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(authenticationService, Mockito.never()).login(any(UserLoginDto.class));
    }

    private static Stream<UserRegistrationDto> invalidRegisterDtos() {
        UserRegistrationDto invalidEmailDto = getValidRegisterDto();
        invalidEmailDto.setEmail("testgmail.com");

        UserRegistrationDto invalidPasswordDto = getValidRegisterDto();
        invalidPasswordDto.setPassword("asdf1");

        UserRegistrationDto invalidRepeatedPasswordDto = getValidRegisterDto();
        invalidRepeatedPasswordDto.setRepeatedPassword("Qwerasdf2");

        UserRegistrationDto emptyFieldDto = getValidRegisterDto();
        emptyFieldDto.setFirstname("");

        return Stream.of(invalidEmailDto, invalidPasswordDto, invalidRepeatedPasswordDto, emptyFieldDto);
    }

    private static UserRegistrationDto getValidRegisterDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstname("Test");
        dto.setLastname("Test");
        dto.setEmail("test@gmail.com");
        dto.setPassword("Qwerasdf1");
        dto.setRepeatedPassword("Qwerasdf1");
        return dto;
    }

    private UserLoginDto getValidLoginDto() {
        UserLoginDto dto = new UserLoginDto();
        dto.setEmail("test@gmail.com");
        dto.setPassword("Qwerasdf1");
        return dto;
    }

    private UserLoginDto getEmptyFieldLoginDto() {
        UserLoginDto dto = getValidLoginDto();
        dto.setEmail("");
        return dto;
    }
}
