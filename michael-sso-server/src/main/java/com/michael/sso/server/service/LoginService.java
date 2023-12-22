package com.michael.sso.server.service;

import com.michael.sso.server.common.AuthenticationUser;
import com.michael.sso.server.common.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface LoginService {

    boolean isAppExists(String appId);

    Result<AuthenticationUser> login(String username, String password);

    String setMappingOfTknAndUser(AuthenticationUser user,
                                  HttpServletRequest request,
                                  HttpServletResponse response);

    String generateAndCacheCode(String redirectUri, String tkn);

    String concatCodeToRedirectUri(String redirectUri, String code)
            throws UnsupportedEncodingException;

}
