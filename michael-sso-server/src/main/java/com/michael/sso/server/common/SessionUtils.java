package com.michael.sso.server.common;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public class SessionUtils {
    
    public static SessionAccessToken getAccessTokenFromToken(HttpServletRequest request) {
        return (SessionAccessToken) request.getSession().getAttribute(SsoConstant.SESSION_ACCESS_TOKEN);
    }
    
	public static AuthenticationUser getUser(HttpServletRequest request) {
	    return Optional.ofNullable(getAccessTokenFromToken(request)).map(u -> u.getUser()).orElse(null);
	}
	
	public static Integer getUserId(HttpServletRequest request) {
        return Optional.ofNullable(getUser(request)).map(u -> u.getId()).orElse(null);
    }

	public static void setAccessToken(HttpServletRequest request, RpcAccessToken rpcAccessToken) {
		SessionAccessToken sessionAccessToken = null;
		if (rpcAccessToken != null) {
			sessionAccessToken = createSessionAccessToken(rpcAccessToken);
		}
		request.getSession().setAttribute(SsoConstant.SESSION_ACCESS_TOKEN, sessionAccessToken);
	}

	private static SessionAccessToken createSessionAccessToken(RpcAccessToken accessToken) {
		long expirationTime = System.currentTimeMillis() + accessToken.getExpiresIn() * 1000;
		return new SessionAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn(),
				accessToken.getRefreshToken(), accessToken.getUser(), expirationTime);
	}

	public static void invalidate(HttpServletRequest request) {
		setAccessToken(request, null);
		request.getSession().invalidate();
	}
}