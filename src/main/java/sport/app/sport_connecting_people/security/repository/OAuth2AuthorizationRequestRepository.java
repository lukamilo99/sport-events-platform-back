package sport.app.sport_connecting_people.security.repository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import sport.app.sport_connecting_people.security.util.CookieUtil;

import java.util.Optional;

@AllArgsConstructor
@Component
public class OAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private static final String REDIRECT_URI_COOKIE_NAME = "redirect_uri";
    private CookieUtil cookieUtil;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Optional<Cookie> optionalCookie = cookieUtil.obtainCookie(OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME, request.getCookies());
        if (optionalCookie.isPresent()) {
            Cookie cookie = optionalCookie.get();
            return cookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
        }
        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(response, request);
            return;
        }
        String serializedCookie = cookieUtil.serialize(authorizationRequest);
        cookieUtil.appendCookie(OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME, serializedCookie, response);
        String redirectUri = request.getParameter(REDIRECT_URI_COOKIE_NAME);
        if (redirectUri != null) {
            cookieUtil.appendCookie(REDIRECT_URI_COOKIE_NAME, redirectUri, response);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response, HttpServletRequest request) {
        cookieUtil.removeCookie(OAUTH_2_AUTHORIZATION_REQUEST_COOKIE_NAME, request, response);
        cookieUtil.removeCookie(REDIRECT_URI_COOKIE_NAME, request, response);
    }
}
