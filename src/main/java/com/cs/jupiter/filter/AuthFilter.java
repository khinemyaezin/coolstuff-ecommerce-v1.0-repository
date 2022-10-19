package com.cs.jupiter.filter;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.utility.AuthGuard;

@Component
public class AuthFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(AuthFilter.class);

	@Autowired
	Environment env;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		try {
			if (req.getMethod().equals("OPTIONS")) {
				chain.doFilter(request, response);
			} else

			if (req.getCookies() != null) {
				RequestCredential reqCred = new RequestCredential();
				String token = Arrays.stream(req.getCookies()).filter(c -> c.getName().equals(AuthGuard.TOKEN_NAME))
						.findFirst().map(Cookie::getValue).orElse(null);
				reqCred.setToken(token);
				reqCred.setSessId(req.getSession().getId());
				if (reqCred.getToken() != null) {
					reqCred.setClaims(AuthGuard.readToken(reqCred.getToken(), resp, env.getProperty("key")));
				}
				if (reqCred.getClaims() == null) {
					sendError(resp);
				} else {
					req.setAttribute("credential", reqCred);
					chain.doFilter(request, response);
				}
			} else {
				sendError(resp);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			sendError(resp);
		}

	}

	public void sendError(HttpServletResponse resp) throws IOException {
		Cookie cookie = new Cookie(AuthGuard.TOKEN_NAME, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
		
		Cookie cookie2 = new Cookie(AuthGuard.TOKEN_HASH, null);
		cookie2.setPath("/");
		cookie2.setHttpOnly(true);
		cookie2.setMaxAge(0);
		resp.addCookie(cookie2);
		resp.setStatus(401);
	}

}
