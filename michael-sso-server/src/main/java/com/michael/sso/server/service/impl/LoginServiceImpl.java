package com.michael.sso.server.service.impl;

import com.michael.sso.server.common.AuthenticationUser;
import com.michael.sso.server.common.Oauth2Constant;
import com.michael.sso.server.common.Result;
import com.michael.sso.server.service.AppService;
import com.michael.sso.server.service.LoginService;
import com.michael.sso.server.service.UserService;
import com.michael.sso.server.session.CodeManager;
import com.michael.sso.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AppService appService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CodeManager codeManager;

    @Override
    public boolean isAppExists(String appId) {
        return appService.exists(appId);
    }

    @Override
    public Result<AuthenticationUser> login(String username, String password) {
        return userService.login(username, password);
    }

    @Override
    public String setMappingOfTknAndUser(AuthenticationUser user,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        return sessionManager.setMappingOfTknAndUser(user, request, response);
    }

    @Override
    public String generateAndCacheCode(String redirectUri, String tkn) {
        return codeManager.generate(tkn, true, redirectUri);
    }

    @Override
    public String concatCodeToRedirectUri(String redirectUri, String code)
            throws UnsupportedEncodingException {
        StringBuilder sbf = new StringBuilder(redirectUri);
        if (redirectUri.indexOf("?") > -1) {
            sbf.append("&");
        }
        else {
            sbf.append("?");
        }
        sbf.append(Oauth2Constant.AUTH_CODE).append("=").append(code);
        return URLDecoder.decode(sbf.toString(), "utf-8");
    }
}
