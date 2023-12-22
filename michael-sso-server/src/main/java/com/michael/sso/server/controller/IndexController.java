package com.michael.sso.server.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.michael.sso.server.common.SessionUtils;
import com.michael.sso.server.common.AuthenticationUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class IndexController {
    
	@Value("${server.port}")
	private Integer serverPort;
    @Value("${sso.server.url}")
    private String serverUrl;

	/**
	 * Index html
	 * @param request
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
    @GetMapping
	public String execute(Model model, HttpServletRequest request)
			throws UnsupportedEncodingException {
		AuthenticationUser user = SessionUtils.getUser(request);
		model.addAttribute("userName", user.getUsername());
		model.addAttribute("serverPort", serverPort);
		model.addAttribute("sessionId", request.getSession().getId());
		return "index";
	}
}