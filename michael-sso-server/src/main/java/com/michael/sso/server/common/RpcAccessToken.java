package com.michael.sso.server.common;

import java.io.Serializable;



public class RpcAccessToken implements Serializable {

	private static final long serialVersionUID = 4507869346123296527L;

	private String accessToken;

	private int expiresIn;
	/**
	 * The param used to refresh Token when expired
	 */
	private String refreshToken;

	private AuthenticationUser user;

	public RpcAccessToken(String accessToken, int expiresIn, String refreshToken, AuthenticationUser user) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.user = user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public AuthenticationUser getUser() {
		return user;
	}

	public void setUser(AuthenticationUser user) {
		this.user = user;
	}
}