package io.github.dovemy.hami.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具
 *
 * @author xuhaoming
 * @date 2021/9/30 16:23
 */
public class CookieUtil {

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
        addCookie(request, response, cookieName, cookieValue, null, null, null);
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, String domain, String path, Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        if (path != null) {
            cookie.setPath(path);
        } else {
            cookie.setPath("/");
        }
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        addCookie(request, response, cookieName, "", null, null, 0);
    }


    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }


    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
