package com.michael.sso.server.service.impl;

import com.michael.sso.server.common.*;
import com.michael.sso.server.service.AppService;
import com.michael.sso.server.service.Oauth2Service;
import com.michael.sso.server.service.UserService;
import com.michael.sso.server.session.AccessTokenManager;
import com.michael.sso.server.session.CodeManager;
import com.michael.sso.server.session.RefreshTokenManager;
import com.michael.sso.server.session.TicketGrantingTicketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Oauth2ServiceImpl implements Oauth2Service {
    @Autowired
    private AppService appService;
    @Autowired
    private CodeManager codeManager;
    @Autowired
    private TicketGrantingTicketManager ticketGrantingTicketManager;
    @Autowired
    private UserService userService;
    @Autowired
    private AccessTokenManager accessTokenManager;
    @Autowired
    private RefreshTokenManager refreshTokenManager;

    @Override
    public Result<Void> validateApp(String appId, String appSecret) {
        return appService.validate(appId, appSecret);
    }

    @Override
    public Result<AccessTokenContent> validateUserByCode(String grantType, String code,
                                                   String username, String password, String appId) {

        AccessTokenContent authDto = null;
        if (GrantTypeEnum.AUTHORIZATION_CODE.getValue().equals(grantType)) {
            CodeContent codeContent = codeManager.getAndRemove(code);
            if (codeContent == null) {
                return Result.createError("code is wrong or expired");
            }

            AuthenticationUser user = ticketGrantingTicketManager.getAndRefresh(codeContent.getTkn());
            if (user == null) {
                return Result.createError("session expired");
            }
            authDto = new AccessTokenContent(codeContent, user, appId);
        } else if (GrantTypeEnum.PASSWORD.getValue().equals(grantType)) {
            // app通过此方式由客户端代理转发http请求到服务端获取accessToken
            Result<AuthenticationUser> loginResult = userService.login(username, password);
            if (!loginResult.isSuccess()) {
                return Result.createError(loginResult.getMessage());
            }
            AuthenticationUser user = loginResult.getData();
            String tkt = ticketGrantingTicketManager.generateToken(loginResult.getData());
            CodeContent codeContent = new CodeContent(tkt, false, null);
            authDto = new AccessTokenContent(codeContent, user, appId);
        }
        return Result.createSuccess(authDto);
    }

    public RpcAccessToken generateRpcAccessToken(
            AccessTokenContent accessTokenContent, String accessToken) {
        String newAccessToken = accessToken;
        if (newAccessToken == null || !accessTokenManager.refresh(newAccessToken)) {
            newAccessToken = accessTokenManager.generate(accessTokenContent);
        }

        String refreshToken = refreshTokenManager.generateRefreshToken(accessTokenContent, newAccessToken);

        return new RpcAccessToken(newAccessToken, accessTokenManager.getExpiresIn(), refreshToken,
                accessTokenContent.getUser());
    }
}
