package com.michael.sso.server.common;

import javax.servlet.http.HttpSession;


public interface SessionMappingStorage {

    HttpSession removeSessionByMappingId(String accessToken);

    void removeBySessionById(String sessionId);

    void addSessionById(String accessToken, HttpSession session);
}
