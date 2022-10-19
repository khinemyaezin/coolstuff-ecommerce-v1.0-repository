package com.cs.jupiter.controller;

import java.sql.Connection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.LoginData;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.service.AuthenService;
import com.cs.jupiter.utility.AuthGuard;

@RestController
@RequestMapping("/auth")
public class AuthenController extends RequestController {
	@Autowired
	AuthenService authService;
	@Autowired
	DataSource ds;
	@Autowired
	Environment env;

	@PostMapping(value = "/request")
	public ViewResult<LoginData> login(@RequestBody User data, HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<LoginData> result;
		try (Connection con = ds.getConnection()) {
			result = authService.authUser(data, con,req,resp);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewResult<LoginData>(e);
		}
	}
	@GetMapping(value = "/logout")
	public ViewResult<String> logout(HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<String> result;
		try {
			Cookie cookie = new Cookie(AuthGuard.TOKEN_NAME, null);
			cookie.setMaxAge(0);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			resp.addCookie(cookie);
			
			Cookie cookie2 = new Cookie(AuthGuard.TOKEN_HASH, null);
			cookie2.setSecure(false);
			cookie2.setMaxAge(0);
			cookie2.setPath("/");
			resp.addCookie(cookie2);
			
			result = new ViewResult<String>();
			result.success();
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewResult<String>(e);
		}
		return result;
	}
}
