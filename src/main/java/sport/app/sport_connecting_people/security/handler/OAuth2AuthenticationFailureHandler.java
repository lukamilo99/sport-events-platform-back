package sport.app.sport_connecting_people.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.security.repository.OAuth2AuthorizationRequestRepository;

import java.io.IOException;

@AllArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private OAuth2AuthorizationRequestRepository requestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        requestRepository.removeAuthorizationRequestCookies(response, request);
        response.sendRedirect("http://localhost:8080/login");
    }
}
