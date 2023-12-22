package com.michael.sso.server.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;


public class CookieUtils {

	private CookieUtils() {
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || StringUtils.isEmpty(name)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

    public static void addCookie(String name, String value, String path, HttpServletRequest request,
        HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (path != null) {
            cookie.setPath(path);
        }
        if ("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

	public static void removeCookie(String name, String path, HttpServletResponse response) {

		Cookie cookie = new Cookie(name, null);

		if (path != null) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(-1000);
		response.addCookie(cookie);
	}
}
