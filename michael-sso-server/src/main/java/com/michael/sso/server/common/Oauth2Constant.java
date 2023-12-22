package com.michael.sso.server.common;


public class Oauth2Constant {

	public static final String STATE = "state";

	public static final String GRANT_TYPE = "grantType";

	public static final String APP_ID = "appId";

	public static final String APP_SECRET = "appSecret";

	public static final String REFRESH_TOKEN = "refreshToken";

	public static final String AUTH_CODE = "code";

	public static final String USERNAME = "username";

	public static final String PASSWORD = "password";

	/**
	 * The address for getting token
	 */
	public static final String ACCESS_TOKEN_URL = "/oauth2/access_token";

	/**
	 * The address for refreshing token
	 */
	public static final String REFRESH_TOKEN_URL = "/oauth2/refresh_token";
}
