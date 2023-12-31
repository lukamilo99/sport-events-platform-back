package sport.app.sport_connecting_people.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import sport.app.sport_connecting_people.security.repository.OAuth2AuthorizationRequestRepository;
import sport.app.sport_connecting_people.util.CookieUtil;
import sport.app.sport_connecting_people.util.JwtUtil;
import sport.app.sport_connecting_people.security.model.UserPrincipal;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;
    private OAuth2AuthorizationRequestRepository requestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        clearAuthenticationAttributes(request, response);

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(user.getId());
        getRedirectStrategy().sendRedirect(request, response, obtainRedirectUri(request, jwt));
    }

    private String obtainRedirectUri(HttpServletRequest request, String jwt) {
        String redirectUri = "";
        Optional<Cookie> optionalCookie = cookieUtil.obtainCookie("redirect_uri", request.getCookies());
        if (optionalCookie.isPresent()) {
            redirectUri = optionalCookie.get().getValue();
        }
        return UriComponentsBuilder.fromUriString(redirectUri).queryParam("jwt", jwt).build().toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super.clearAuthenticationAttributes(servletRequest);
        requestRepository.removeAuthorizationRequestCookies(servletResponse, servletRequest);
    }
}

