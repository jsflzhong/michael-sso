package com.michael.sso.server.session;

import java.util.UUID;

import com.michael.sso.server.common.AccessTokenContent;
import com.michael.sso.server.common.Expiration;
import com.michael.sso.server.common.RefreshTokenContent;


public interface RefreshTokenManager extends Expiration {

	default String generateRefreshToken(AccessTokenContent accessTokenContent, String accessToken) {
		String resfreshToken = "RT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(resfreshToken, new RefreshTokenContent(accessTokenContent, accessToken));
		return resfreshToken;
	}

	void create(String refreshToken, RefreshTokenContent refreshTokenContent);

	RefreshTokenContent validate(String refreshToken);
}
