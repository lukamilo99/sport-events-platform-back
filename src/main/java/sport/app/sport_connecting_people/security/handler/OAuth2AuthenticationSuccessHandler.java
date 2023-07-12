package sport.app.sport_connecting_people.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.security.jwt.JwtUtil;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

import java.io.IOException;

@AllArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(user.getId());
        response.addHeader("Authorization", "Bearer " + jwtToken);
        getRedirectStrategy().sendRedirect(request, response, "/login");
    }
}

