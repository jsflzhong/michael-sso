package com.michael.sso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.michael.sso.server.common.Result;
import com.michael.sso.server.common.AuthenticationUser;
import org.springframework.stereotype.Service;

import com.michael.sso.server.model.User;
import com.michael.sso.server.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static List<User> userList;
	
	static {
		userList = new ArrayList<>();
		userList.add(new User(1, "administrator", "admin", "123456"));
	}
	
	@Override
	public Result<AuthenticationUser> login(String username, String password) {
		for (User user : userList) {
			if (user.getUsername().equals(username)) {
				if(user.getPassword().equals(password)) {
					return Result.createSuccess(new AuthenticationUser(user.getId(), user.getUsername()));
				}
				else {
					return Result.createError("Wrong password");
				}
			}
		}
		return Result.createError("User doesn't existed");
	}
}
