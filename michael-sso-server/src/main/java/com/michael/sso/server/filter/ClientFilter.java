package com.michael.sso.server.filter;


import com.michael.sso.server.common.LocalSessionMappingStorage;
import com.michael.sso.server.common.SessionMappingStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public abstract class ClientFilter extends ParamFilter implements Filter {
	
	private SessionMappingStorage sessionMappingStorage;
    
	public abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException {
	}

	@Override
	public void destroy() {
	}
	
	protected SessionMappingStorage getSessionMappingStorage() {
		if (sessionMappingStorage == null) {
            sessionMappingStorage = new LocalSessionMappingStorage();
        }
		return sessionMappingStorage;
	}

}