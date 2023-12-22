package com.michael.sso.server.filter;

import com.alibaba.fastjson.JSON;

import com.michael.sso.server.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;


public final class LoginFilter extends ClientFilter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
    
	@Override
	public boolean isAccessAllowed(HttpServletRequest request,
								   HttpServletResponse response) throws IOException {
		if (doSessionAccessToken(SessionUtils.getAccessTokenFromToken(request), request)) {
			return true;
		}
		String code = request.getParameter(Oauth2Constant.AUTH_CODE);
		if (code != null) {
			getAndCacheAccessToken(code, request);
			return true;
		} else {
			redirectLogin(request, response);
		}
		return false;
	}

	private boolean doSessionAccessToken(SessionAccessToken sessionAccessToken,
										 HttpServletRequest request) {
		return sessionAccessToken != null && (!sessionAccessToken.isExpired()
				|| refreshToken(sessionAccessToken.getRefreshToken(), request));
	}

	private void getAndCacheAccessToken(String code, HttpServletRequest request) {
		Result<RpcAccessToken> result = Oauth2Utils.getAccessTokenByCode(
				getServerUrl(), getAppId(), getAppSecret(), code);
		if (!result.isSuccess()) {
			logger.error("getAccessToken has error, message:{}", result.getMessage());
			return;
		}
		setAccessTokenInSession(result.getData(), request);
	}

	protected boolean refreshToken(String refreshToken, HttpServletRequest request) {
		Result<RpcAccessToken> result = Oauth2Utils.refreshToken(getServerUrl(), getAppId(), refreshToken);
		if (!result.isSuccess()) {
			logger.error("refreshToken has error, message:{}", result.getMessage());
			return false;
		}
		return setAccessTokenInSession(result.getData(), request);
	}
	
	private boolean setAccessTokenInSession(RpcAccessToken rpcAccessToken, HttpServletRequest request) {
		if (rpcAccessToken == null) {
			return false;
		}
		SessionUtils.setAccessToken(request, rpcAccessToken);
		recordSession(request, rpcAccessToken.getAccessToken());
		return true;
	}
	
    private void recordSession(final HttpServletRequest request, String accessToken) {
        final HttpSession session = request.getSession();
        getSessionMappingStorage().removeBySessionById(session.getId());
        getSessionMappingStorage().addSessionById(accessToken, session);
    }

	private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (isAjaxRequest(request)) {
			responseJson(response, SsoConstant.NO_LOGIN, "Not logged in or expired");
		}
		else {
			String loginUrl = new StringBuilder().append(getServerUrl()).append(SsoConstant.LOGIN_URL).append("?")
					.append(Oauth2Constant.APP_ID).append("=").append(getAppId()).append("&")
					.append(SsoConstant.REDIRECT_URI).append("=")
					.append(URLEncoder.encode(getCurrentUrl(request), "utf-8")).toString();
			response.sendRedirect(loginUrl);
		}
	}

	private String getCurrentUrl(HttpServletRequest request) {
		return new StringBuilder().append(request.getRequestURL())
				.append(request.getQueryString() == null ? "" : "?" + request.getQueryString()).toString();
	}
	
	protected boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }
    
    protected void responseJson(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.create(code, message)));
        writer.flush();
        writer.close();
    }
}