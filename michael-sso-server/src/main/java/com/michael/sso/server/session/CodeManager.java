package com.michael.sso.server.session;

import java.util.UUID;

import com.michael.sso.server.common.CodeContent;
import com.michael.sso.server.common.Expiration;


public interface CodeManager extends Expiration {

	default String generate(String tgt, boolean sendLogoutRequest, String redirectUri) {
		String code = "code-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(code, new CodeContent(tgt, sendLogoutRequest, redirectUri));
		return code;
	}

	void create(String code, CodeContent codeContent) ;

	CodeContent getAndRemove(String code);
	
	//10 mins
	@Override
	default int getExpiresIn() {
		return 600;
	}
}
