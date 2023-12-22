package com.michael.sso.server.common;


public interface ExpirationPolicy {
	
	/**
	 * Every 5 minutes
	 */
	public static final String SCHEDULED_CRON = "0 */5 * * * ?";

    void verifyExpired();
}
