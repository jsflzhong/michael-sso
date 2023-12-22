package com.michael.sso.server.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class Oauth2Utils {

	private static final Logger logger = LoggerFactory.getLogger(Oauth2Utils.class);

	public static Result<RpcAccessToken> getAccessTokenByPSWD(String serverUrl, String appId,
															  String appSecret, String username,
															  String password) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put(Oauth2Constant.GRANT_TYPE, GrantTypeEnum.PASSWORD.getValue());
		paramMap.put(Oauth2Constant.APP_ID, appId);
		paramMap.put(Oauth2Constant.APP_SECRET, appSecret);
		paramMap.put(Oauth2Constant.USERNAME, username);
		paramMap.put(Oauth2Constant.PASSWORD, password);
		return getHttpAccessToken(serverUrl + Oauth2Constant.ACCESS_TOKEN_URL, paramMap);
	}

	public static Result<RpcAccessToken> getAccessTokenByCode(String serverUrl, String appId,
															  String appSecret, String code) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put(Oauth2Constant.GRANT_TYPE, GrantTypeEnum.AUTHORIZATION_CODE.getValue());
		paramMap.put(Oauth2Constant.APP_ID, appId);
		paramMap.put(Oauth2Constant.APP_SECRET, appSecret);
		paramMap.put(Oauth2Constant.AUTH_CODE, code);
		return getHttpAccessToken(serverUrl + Oauth2Constant.ACCESS_TOKEN_URL, paramMap);
	}

	public static Result<RpcAccessToken> refreshToken(String serverUrl, String appId, String refreshToken) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put(Oauth2Constant.APP_ID, appId);
		paramMap.put(Oauth2Constant.REFRESH_TOKEN, refreshToken);
		return getHttpAccessToken(serverUrl + Oauth2Constant.REFRESH_TOKEN_URL, paramMap);
	}

	private static Result<RpcAccessToken> getHttpAccessToken(String url, Map<String, String> paramMap) {
		String jsonStr = HttpUtils.get(url, paramMap);
		if (jsonStr == null || jsonStr.isEmpty()) {
			logger.error("getHttpAccessToken exception, return null. url:{}", url);
			return null;
		}
		return JSONObject.parseObject(jsonStr, new TypeReference<Result<RpcAccessToken>>(){});
	}
}