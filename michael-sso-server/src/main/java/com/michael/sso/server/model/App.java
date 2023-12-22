package com.michael.sso.server.model;

import java.io.Serializable;

public class App implements Serializable {

	private static final long serialVersionUID = 14358427303197385L;

	private String name;
	private String appId;
	private String appSecret;

	public App() {
		super();
	}

	public App(String name, String appId, String appSecret) {
		super();
		this.name = name;
		this.appId = appId;
		this.appSecret = appSecret;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
}
