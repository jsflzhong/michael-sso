package com.michael.sso.server.session;

import java.util.UUID;

import com.michael.sso.server.common.AccessTokenContent;
import com.michael.sso.server.common.Expiration;


public interface AccessTokenManager extends Expiration {

	default String generate(AccessTokenContent accessTokenContent) {
		String accessToken = "AT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(accessToken, accessTokenContent);
		return accessToken;
	}

	void create(String accessToken, AccessTokenContent accessTokenContent);

	boolean refresh(String accessToken);


}
