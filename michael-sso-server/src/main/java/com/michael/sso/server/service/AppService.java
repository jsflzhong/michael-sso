package com.michael.sso.server.service;


import com.michael.sso.server.common.Result;


public interface AppService {

	boolean exists(String appId);
	
	Result<Void> validate(String appId, String appSecret);
}
