package com.michael.sso.server.service;


import com.michael.sso.server.common.Result;
import com.michael.sso.server.common.AuthenticationUser;


public interface UserService {

	Result<AuthenticationUser> login(String username, String password);
}
