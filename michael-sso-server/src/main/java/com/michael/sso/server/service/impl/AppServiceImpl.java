package com.michael.sso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.michael.sso.server.common.Result;
import com.michael.sso.server.service.AppService;
import org.springframework.stereotype.Service;

import com.michael.sso.server.model.App;

@Service("appService")
public class AppServiceImpl implements AppService {

	private static List<App> appList;

	static {
		appList = new ArrayList<>();
		appList.add(new App("服务端1", "server-CAS", "112233"));
		appList.add(new App("客户端1", "client-CAS-1", "112233"));
	}

	@Override
	public boolean exists(String appId) {
		return appList.stream().anyMatch(app -> app.getAppId().equals(appId));
	}

	@Override
	public Result<Void> validate(String appId, String appSecret) {
		for (App app : appList) {
			if (app.getAppId().equals(appId)) {
				if (app.getAppSecret().equals(appSecret)) {
					return Result.success();
				}
				else {
					return Result.createError("appSecret is wrong");
				}
			}
		}
		return Result.createError("appId is not existed");
	}
}
