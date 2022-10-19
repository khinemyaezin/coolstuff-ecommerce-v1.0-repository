package com.cs.jupiter.model.jun;

import com.cs.jupiter.model.table.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginData {
	private String userId;
	private String userAuthMethodValue;
	private String bizId;
	@JsonIgnore
	private String token;
	private UserType userType;
	@JsonIgnore
	private String encPassword;
	
	public String getUserId() {
		return userId;
	}
	public String getUserAuthMethodValue() {
		return userAuthMethodValue;
	}
	public String getBizId() {
		return bizId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserAuthMethodValue(String userAuthMethodValue) {
		this.userAuthMethodValue = userAuthMethodValue;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getEncPassword() {
		return encPassword;
	}
	public void setEncPassword(String encPassword) {
		this.encPassword = encPassword;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	
}
