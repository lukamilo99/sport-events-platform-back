package sport.app.sport_connecting_people.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import sport.app.sport_connecting_people.controller.config.SecurityPermitAllConfig;
import sport.app.sport_connecting_people.dto.user.request.UserRegistrationDto;
import sport.app.sport_connecting_people.exceptions.user.UserAlreadyExistsException;
import sport.app.sport_connecting_people.security.filter.JwtAuthenticationFilter;
import sport.app.sport_connecting_people.service.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
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
    public void userAlreadyExistsRegisterFail() throws Exception {
        UserRegistrationDto dto = getValidDto();

        Mockito.doThrow(UserAlreadyExistsException.class).when(authenticationService).register(any(UserRegistrationDto.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        Mockito.verify(authenticationService, Mockito.times(1)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void validDtoRegisterSuccess() throws Exception {
        UserRegistrationDto dto = getValidDto();

        Mockito.doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.verify(authenticationService, Mockito.times(1)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void invalidEmailDtoRegisterFail() throws Exception {
        UserRegistrationDto dto = getInvalidEmailDto();

        Mockito.doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(authenticationService, Mockito.times(0)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void invalidPasswordDtoRegisterFail() throws Exception {
        UserRegistrationDto dto = getInvalidPasswordDto();

        Mockito.doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(authenticationService, Mockito.times(0)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void invalidRepeatedPasswordDtoRegisterFail() throws Exception {
        UserRegistrationDto dto = getInvalidRepeatedPasswordDto();

        Mockito.doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(authenticationService, Mockito.times(0)).register(any(UserRegistrationDto.class));
    }

    @Test
    public void emptyFieldDtoRegisterFail() throws Exception {
        UserRegistrationDto dto = getEmptyFieldDto();

        Mockito.doNothing().when(authenticationService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(authenticationService, Mockito.times(0)).register(any(UserRegistrationDto.class));
    }

    private UserRegistrationDto getValidDto() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFirstname("Test");
        dto.setLastname("Test");
        dto.setEmail("test@gmail.com");
        dto.setPassword("Qwerasdf1");
        dto.setRepeatedPassword("Qwerasdf1");
        return dto;
    }

    private UserRegistrationDto getInvalidEmailDto() {
        UserRegistrationDto dto = getValidDto();
        dto.setEmail("testgmail.com");
        return dto;
    }

    private UserRegistrationDto getInvalidPasswordDto() {
        UserRegistrationDto dto = getValidDto();
        dto.setPassword("asdf1");
        dto.setRepeatedPassword("asdf1");
        return dto;
    }

    private UserRegistrationDto getInvalidRepeatedPasswordDto() {
        UserRegistrationDto dto = getValidDto();
        dto.setRepeatedPassword("Qwerasdf2");
        return dto;
    }

    private UserRegistrationDto getEmptyFieldDto() {
        UserRegistrationDto dto = getValidDto();
        dto.setFirstname("");
        return dto;
    }
}
