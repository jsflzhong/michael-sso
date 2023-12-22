package com.michael.sso.server.session.local;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.michael.sso.server.session.AccessTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.michael.sso.server.common.AccessTokenContent;
import com.michael.sso.server.common.ExpirationPolicy;


@Component
public class LocalAccessTokenManager implements AccessTokenManager, ExpirationPolicy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${sso.timeout}")
    private int timeout;

	private Map<String, DummyAccessToken> accessTokenMap = new ConcurrentHashMap<>();
	private Map<String, Set<String>> tgtMap = new ConcurrentHashMap<>();

	@Override
	public void create(String accessToken, AccessTokenContent accessTokenContent) {
		DummyAccessToken dat = new DummyAccessToken(accessTokenContent, System.currentTimeMillis() + getExpiresIn() * 1000);
		accessTokenMap.put(accessToken, dat);

		tgtMap.computeIfAbsent(accessTokenContent.getCodeContent().getTkn(), a -> new HashSet<>()).add(accessToken);
		logger.info("Token generated successfully, accessToken:{}", accessToken);
	}
	
	@Override
	public boolean refresh(String accessToken) {
		DummyAccessToken dummyAt = accessTokenMap.get(accessToken);
		if (dummyAt == null || System.currentTimeMillis() > dummyAt.expired) {
			return false;
		}
		dummyAt.expired = System.currentTimeMillis() + getExpiresIn() * 1000;
		return true;
	}

	@Scheduled(cron = SCHEDULED_CRON)
	@Override
	public void verifyExpired() {
		accessTokenMap.forEach((accessToken, dummyAt) -> {
			if (System.currentTimeMillis() > dummyAt.expired) {
				accessTokenMap.remove(accessToken);
				logger.debug("Token failed, accessToken:{}", accessToken);
			}
		});
	}

	@Override
	public int getExpiresIn() {
		return timeout / 2;
	}

	private class DummyAccessToken {
		private AccessTokenContent accessTokenContent;
		private long expired;

		public DummyAccessToken(AccessTokenContent accessTokenContent, long expired) {
			super();
			this.accessTokenContent = accessTokenContent;
			this.expired = expired;
		}
	}
}
