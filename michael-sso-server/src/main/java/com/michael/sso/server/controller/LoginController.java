package com.michael.sso.server.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.michael.sso.server.common.Oauth2Constant;
import com.michael.sso.server.common.Result;
import com.michael.sso.server.common.SsoConstant;
import com.michael.sso.server.common.AuthenticationUser;
import com.michael.sso.server.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.michael.sso.server.constant.AppConstant;
import com.michael.sso.server.session.SessionManager;


@Controller
@RequestMapping("/login")
public class LoginController{

	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private LoginService loginService;

	/**
	 * Route to Login html
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			HttpServletRequest request)
			throws UnsupportedEncodingException {
		String tkn = sessionManager.getAndRefreshTKN(request);
		if (StringUtils.isEmpty(tkn)) {
			return routeLoginPath(redirectUri, appId, request);
		}
		String code = loginService.generateAndCacheCode(redirectUri, tkn);
		return "redirect:" + loginService.concatCodeToRedirectUri(redirectUri, code);
	}
	
	/**
	 * Login Logic
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			@RequestParam String username, 
			@RequestParam String password,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		if(!loginService.isAppExists(appId)) {
			request.setAttribute("errorMessage", "Not supported App");
			return routeLoginPath(redirectUri, appId, request);
		}
		
		Result<AuthenticationUser> result = loginService.login(username, password);
		if (!result.isSuccess()) {
			request.setAttribute("errorMessage", result.getMessage());
			return routeLoginPath(redirectUri, appId, request);
		}

		String tkn = loginService.setMappingOfTknAndUser(result.getData(), request, response);
		String code = loginService.generateAndCacheCode(redirectUri, tkn);
		return "redirect:" + loginService.concatCodeToRedirectUri(redirectUri, code);
	}

	private String routeLoginPath(String redirectUri, String appId, HttpServletRequest request) {
		request.setAttribute(SsoConstant.REDIRECT_URI, redirectUri);
		request.setAttribute(Oauth2Constant.APP_ID, appId);
		return AppConstant.LOGIN_PATH;
	}
}