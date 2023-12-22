package com.michael.sso.server.session;

import java.util.UUID;

import com.michael.sso.server.common.AuthenticationUser;
import com.michael.sso.server.common.Expiration;


public interface TicketGrantingTicketManager extends Expiration {

	default String generateToken(AuthenticationUser user) {
		String tkn = "TKN-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(tkn, user);
		return tkn;
	}

    void create(String tgt, AuthenticationUser user);

    AuthenticationUser getAndRefresh(String tgt);

    void set(String tgt, AuthenticationUser user);

    void remove(String tgt);
}
