package com.cs.jupiter.service;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cs.jupiter.dao.AuthDao;
import com.cs.jupiter.model.interfaces.ViewResult;
import com.cs.jupiter.model.jun.LoginData;
import com.cs.jupiter.model.table.User;
import com.cs.jupiter.utility.AuthGuard;

@Service
public class AuthenService {
	@Autowired
	Environment env;
	
	@Autowired
	AuthDao authDao;
	

	public ViewResult<LoginData> authUser(User data, Connection conn,HttpServletRequest req, HttpServletResponse resp) {
		ViewResult<LoginData> result;
		Date today = new Date();
		try {
			String loginTypeData = (data.getEmail() == null || data.getEmail().equals("")) ? data.getPhoneNo()
					: data.getEmail();
			if (loginTypeData == null) {
				throw new Exception("invalid_request");
			}
			result = authDao.auth(loginTypeData, conn);
			if (!result.isSucces()) {
				throw new Exception(result.message);
			}
			if (result.data.getEncPassword() != null && !result.data.getEncPassword().equals("")) {
				if (!AuthGuard.bcryptCheck(data.getPassword(), result.data.getEncPassword())) {
					throw new Exception("email_or_password_is_incorrect");
				}
			}
			result.data.setUserAuthMethodValue(loginTypeData);

			Map<String, String> claims = new HashMap<>();
			claims.put("biz_id", result.data.getBizId());
			claims.put("auth_method", loginTypeData);
			claims.put("access_time", today.toString());
			claims.put("session_id", req.getSession().getId());
			result.data.setToken(AuthGuard.createToken("coolstuff_auth", claims, today,
					Long.parseLong(env.getProperty("jwt.expdur.milisec")),
					env.getProperty("key")));
			
			Cookie cookie = new Cookie(AuthGuard.TOKEN_NAME, result.data.getToken());
			cookie.setSecure(false);
			cookie.setMaxAge(Integer.parseInt(env.getProperty("cookie.expdur.sec")));
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			resp.addCookie(cookie);
			
			Cookie cookie2 = new Cookie(AuthGuard.TOKEN_HASH, Integer.toString(result.data.getToken().hashCode()));
			cookie2.setSecure(false);
			cookie2.setMaxAge(Integer.parseInt(env.getProperty("cookie.expdur.sec")));
			cookie2.setPath("/");
			resp.addCookie(cookie2);
			
			resp.setHeader("Access-Control-Allow-Credentials", "true");
		} catch (Exception e) {
			result = new ViewResult<>(e);
			e.printStackTrace();
		}
		return result;
	}
}
