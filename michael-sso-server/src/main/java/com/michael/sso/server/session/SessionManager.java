package com.michael.sso.server.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.michael.sso.server.common.AuthenticationUser;
import com.michael.sso.server.constant.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.michael.sso.server.util.CookieUtils;


@Component
public class SessionManager {
	
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private TicketGrantingTicketManager ticketGrantingTicketManager;

	public String setMappingOfTknAndUser(AuthenticationUser user,
										 HttpServletRequest request, HttpServletResponse response) {
		String tkn = getCookieTKN(request);
		if (StringUtils.isEmpty(tkn)) {
			tkn = ticketGrantingTicketManager.generateToken(user);
			CookieUtils.addCookie(AppConstant.TKN, tkn, "/", request, response);
		} else if(ticketGrantingTicketManager.getAndRefresh(tkn) == null){
			ticketGrantingTicketManager.create(tkn, user);
		} else {
			ticketGrantingTicketManager.set(tkn, user);
		}
		return tkn;
	}

	public AuthenticationUser getUser(HttpServletRequest request) {
		String tkn = getCookieTKN(request);
		if (StringUtils.isEmpty(tkn)) {
			return null;
		}
		return ticketGrantingTicketManager.getAndRefresh(tkn);
	}

	public String getAndRefreshTKN(HttpServletRequest request) {
		String tkn = getCookieTKN(request);
		if (StringUtils.isEmpty(tkn) || ticketGrantingTicketManager.getAndRefresh(tkn) == null) {
			return null;
		} else {
			return tkn;
		}
	}
	
	private String getCookieTKN(HttpServletRequest request) {
		return CookieUtils.getCookie(request, AppConstant.TKN);
	}
}