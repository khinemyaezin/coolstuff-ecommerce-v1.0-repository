package com.cs.jupiter.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cs.jupiter.filter.AuthFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;;

@Component
public class AuthGuard {
	private static final Logger logger = LogManager.getLogger(AuthFilter.class);

	public final static String TOKEN_NAME = "CS-TOKEN";
	public final static String TOKEN_HASH = "CS-CLIENT-TOKEN";
	public final static String SESSION_NAME = "JSESSIONID";
	public static String KEY128 = "3867!@#$78!@#$76"; // 128 bit key
	public static String INIT_VECTOR = "RandomInitVector"; // 16 bytes IV
	public static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public static String createToken(String subjectName, 
			Map<String, String> claims, Date issueDate, 
			Long durationInMilisec,String key) {
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(subjectName);
		for (Entry<String, String> v : claims.entrySet()) {
			builder.claim(v.getKey(), v.getValue());
		}
		builder.setIssuedAt(issueDate);
		builder.setExpiration(new Date(System.currentTimeMillis() + durationInMilisec));
		builder.setId(UUID.randomUUID().toString());
		builder.signWith(SignatureAlgorithm.HS256, key);
		return builder.compact();

	}

	public static Map<String, String> readToken(String tokenString, 
			HttpServletResponse resp,String secretKey) {
		Map<String, String> credential = new HashMap<>();
		try {
			logger.info("[jwt-parsing]","start");
			Claims claims = (Claims) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenString).getBody();
			for (Entry<String, Object> entry : claims.entrySet()) {
				if (entry.getValue() instanceof String) {
					credential.put(entry.getKey(), (String) entry.getValue());
				}
			}
			logger.info("[jwt-parsing]","end");
		} catch (Exception e) {
			System.out.println("jwt token fail");
			credential = null;
			e.printStackTrace();
			
		}
		return credential;
	}
	
	

	public static String encrypt(String value) {
		if (value == null || "".equals(value))
			return "";
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(KEY128.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String value) {
		if (value == null || "".equals(value))
			return "";
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(KEY128.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(value));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String bcrypt(String password) {
		if (password == null || password.equals("")) {
			return "";
		}
		return encoder.encode(password);
	}

	public static boolean bcryptCheck(String rawPassword, String encodePassword) {
		if (rawPassword.equals("") && encodePassword.equals("")) {
			return true;
		}
		return encoder.matches(rawPassword, encodePassword);
	}
	
}
