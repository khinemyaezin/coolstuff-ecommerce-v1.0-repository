package com.cs.jupiter.model.interfaces;

public class Auth {
	private String id;
	private String currentPassword;
	private String newPassword;
	
	public Auth(){
		
	}
	public Auth(String id, String currentPassword, String newPassword) {
		super();
		this.id = id;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getId() {
		return id;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	
}
