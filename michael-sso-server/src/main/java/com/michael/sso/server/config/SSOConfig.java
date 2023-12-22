package com.michael.sso.server.config;

import com.michael.sso.server.common.SSOContainer;
import com.michael.sso.server.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSOConfig {

    @Value("${sso.server.url}")
    private String serverUrl;
    @Value("${sso.app.id}")
    private String appId;
    @Value("${sso.app.secret}")
    private String appSecret;

	@Bean
	public FilterRegistrationBean<SSOContainer> michaelContainer() {
		SSOContainer ssoContainer = new SSOContainer();
		ssoContainer.setServerUrl(serverUrl);
		ssoContainer.setAppId(appId);
		ssoContainer.setAppSecret(appSecret);
		
        ssoContainer.setExcludeUrls("/login,/oauth2/*,/custom/*,/assets/*");
		ssoContainer.setFilters(new LoginFilter());

		FilterRegistrationBean<SSOContainer> registration = new FilterRegistrationBean<>();
		registration.setFilter(ssoContainer);
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		registration.setName("michaelContainer");
		return registration;
	}
}
