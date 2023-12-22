package com.michael.sso.server.common;


public enum GrantTypeEnum {
	
	/**
	 * Authentication code type
	 */
	AUTHORIZATION_CODE("authorization_code"), 

	/**
	 * Password type
	 */
	PASSWORD("password");

	private String value;

	GrantTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}