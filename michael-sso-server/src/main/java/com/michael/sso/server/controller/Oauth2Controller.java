package com.michael.sso.server.controller;

import com.michael.sso.server.common.*;
import com.michael.sso.server.service.Oauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michael.sso.server.service.AppService;
import com.michael.sso.server.session.RefreshTokenManager;
import com.michael.sso.server.session.TicketGrantingTicketManager;


@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {
	
	@Autowired
	private AppService appService;
	@Autowired
	private RefreshTokenManager refreshTokenManager;
	@Autowired
	private TicketGrantingTicketManager ticketGrantingTicketManager;
	@Autowired
	private Oauth2Service oauth2Service;
	
	/**
	 * Get accessToken
	 */
	@RequestMapping(value = "/access_token", method = RequestMethod.GET)
	public Result getAccessToken(
			@RequestParam(value = Oauth2Constant.GRANT_TYPE, required = true) String grantType,
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			@RequestParam(value = Oauth2Constant.APP_SECRET, required = true) String appSecret,
			@RequestParam(value = Oauth2Constant.AUTH_CODE, required = false) String code,
			@RequestParam(value = Oauth2Constant.USERNAME, required = false) String username,
			@RequestParam(value = Oauth2Constant.PASSWORD, required = false) String password) {
		
		Result<Void> result = validateParam(grantType, code, username, password);
		if (!result.isSuccess()) return result;
		Result<Void> appResult = oauth2Service.validateApp(appId, appSecret);
		if (!appResult.isSuccess()) return appResult;
		Result<AccessTokenContent> accessTokenResult
				= oauth2Service.validateUserByCode(grantType, code, username, password, appId);
		if (!accessTokenResult.isSuccess()) return accessTokenResult;
		return Result.createSuccess(
				oauth2Service.generateRpcAccessToken(accessTokenResult.getData(), null));
	}
	
	/**
	 * Refresh accessToken
	 */
	@RequestMapping(value = "/refresh_token", method = RequestMethod.GET)
	public Result refreshToken(
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			@RequestParam(value = Oauth2Constant.REFRESH_TOKEN, required = true) String refreshToken) {
		if(!appService.exists(appId)) return Result.createError("非法应用");
		RefreshTokenContent refreshTokenContent = refreshTokenManager.validate(refreshToken);
		if (refreshTokenContent == null)
			return Result.createError("refreshToken not right or already expired");
		AccessTokenContent accessTokenContent = refreshTokenContent.getAccessTokenContent();
		if (!appId.equals(accessTokenContent.getAppId()))
			return Result.createError("Not allowed App");
		AuthenticationUser user = ticketGrantingTicketManager
				.getAndRefresh(accessTokenContent.getCodeContent().getTkn());
		if (user == null) return Result.createError("Session expired");
		return Result.createSuccess(
				oauth2Service.generateRpcAccessToken(accessTokenContent, refreshTokenContent.getAccessToken()));
	}

	private Result<Void> validateParam(String grantType, String code, String username, String password) {
		if (GrantTypeEnum.AUTHORIZATION_CODE.getValue().equals(grantType)) {
			if (StringUtils.isEmpty(code))
				return Result.createError("Code can not be empty");
		} else if (GrantTypeEnum.PASSWORD.getValue().equals(grantType)) {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				return Result.createError("username and password can not be empty");
			}
		} else {
			return Result.createError("Not a supported authentication type");
		}
		return Result.success();
	}
}