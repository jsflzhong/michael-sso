package com.michael.sso.server.common;

import java.io.Serializable;


public class CodeContent implements Serializable {

	private static final long serialVersionUID = -1332598459045608781L;

	private String tkn;
	private boolean sendLogoutRequest;
	private String redirectUri;

	public CodeContent(String tkn, boolean sendLogoutRequest, String redirectUri) {
		this.tkn = tkn;
		this.sendLogoutRequest = sendLogoutRequest;
		this.redirectUri = redirectUri;
	}

	public String getTkn() {
		return tkn;
	}

	public void setTkn(String tkn) {
		this.tkn = tkn;
	}

	public boolean isSendLogoutRequest() {
		return sendLogoutRequest;
	}

	public void setSendLogoutRequest(boolean sendLogoutRequest) {
		this.sendLogoutRequest = sendLogoutRequest;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
}