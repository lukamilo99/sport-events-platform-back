package sport.app.sport_connecting_people.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

@Service
public class CookieUtil {

    public Cookie getHttpOnlyCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public Cookie invalidateHttpOnlyCookie() {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public void appendCookie(String cookieName, String cookieValue, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(180);
        response.addCookie(cookie);
    }

    public Optional<Cookie> obtainCookie(String cookieName, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public void removeCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        obtainCookie(cookieName, request.getCookies()).ifPresent(cookie -> appendCookie(cookieName, "", response));
    }

    public String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    public <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
