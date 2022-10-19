package com.cs.jupiter.model.interfaces;

import java.util.Map;

public class RequestCredential {

	private Map<String, String> claims;
	private boolean needToken;
	private String token;
	private String sessId;

	public RequestCredential() {
		super();
	}

	public RequestCredential(Map<String, String> claims, boolean needToken) {
		super();
		this.claims = claims;
		this.needToken = needToken;
	}

	public Map<String, String> getClaims() {
		return claims;
	}

	public boolean isNeedToken() {
		return needToken;
	}

	public void setClaims(Map<String, String> claims) {
		this.claims = claims;
	}

	public void setNeedToken(boolean needToken) {
		this.needToken = needToken;
	}

	public String getToken() {
		return token;
	}

	public String getSessId() {
		return sessId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setSessId(String sessId) {
		this.sessId = sessId;
	}

	public boolean isValidSessionId() {
		if (this.sessId != null && this.claims != null) {
			boolean rtn = this.getClaims().get("session_id").equals(this.getSessId());
			return rtn;
		} else {
			return true;
		}

	}

}
