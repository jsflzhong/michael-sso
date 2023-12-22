package com.michael.sso.server.service;

import com.michael.sso.server.common.AccessTokenContent;
import com.michael.sso.server.common.Result;
import com.michael.sso.server.common.RpcAccessToken;

public interface Oauth2Service {

    Result<Void> validateApp(String appId, String appSecret);

    Result<AccessTokenContent> validateUserByCode(String grantType, String code,
                                            String username, String password, String appId);

    RpcAccessToken generateRpcAccessToken(
            AccessTokenContent accessTokenContent, String accessToken);
}
