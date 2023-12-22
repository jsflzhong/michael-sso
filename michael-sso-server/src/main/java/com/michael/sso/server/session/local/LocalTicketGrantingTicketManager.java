package com.michael.sso.server.session.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.michael.sso.server.common.AuthenticationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.michael.sso.server.common.ExpirationPolicy;
import com.michael.sso.server.session.TicketGrantingTicketManager;


@Component
public class LocalTicketGrantingTicketManager
		implements TicketGrantingTicketManager, ExpirationPolicy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${sso.timeout}")
    private int timeout;

	private Map<String, DummyTgt> tknMap = new ConcurrentHashMap<>();

	@Override
	public void create(String tkn, AuthenticationUser user) {
		tknMap.put(tkn, new DummyTgt(user, System.currentTimeMillis() + getExpiresIn() * 1000));
		logger.info("token successfully, tkn:{}", tkn);
	}

	@Override
	public AuthenticationUser getAndRefresh(String tkn) {
		DummyTgt dummyTgt = tknMap.get(tkn);
		long currentTime = System.currentTimeMillis();
		if (dummyTgt == null || currentTime > dummyTgt.expired) {
			return null;
		}
		dummyTgt.expired = currentTime + getExpiresIn() * 1000;
		return dummyTgt.user;
	}
	
	@Override
	public void set(String tkn, AuthenticationUser user) {
		DummyTgt dummyTgt = tknMap.get(tkn);
		if (dummyTgt == null) {
			return;
		}
		dummyTgt.user = user;
	}

	@Override
	public void remove(String tkn) {
		tknMap.remove(tkn);
		logger.debug("token deleted successfully, tkn:{}", tkn);
	}

	@Scheduled(cron = SCHEDULED_CRON)
	@Override
	public void verifyExpired() {
		tknMap.forEach((tkn, dummyTgt) -> {
			if (System.currentTimeMillis() > dummyTgt.expired) {
				tknMap.remove(tkn);
				logger.debug("token failed, tkn:{}", tkn);
			}
		});
	}

	@Override
	public int getExpiresIn() {
		return timeout;
	}
	
	private class DummyTgt {
		private AuthenticationUser user;
		private long expired;

		public DummyTgt(AuthenticationUser user, long expired) {
			super();
			this.user = user;
			this.expired = expired;
		}
	}
}
