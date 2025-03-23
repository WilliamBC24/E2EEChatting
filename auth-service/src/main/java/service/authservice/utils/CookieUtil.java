package service.authservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private CookieUtil() {
        throw new UnsupportedOperationException("Utility class - should not be instantiated");
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // Only allow over HTTPS
        cookie.setPath("/");
        //should be the same as jwt or refresh expiration
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
