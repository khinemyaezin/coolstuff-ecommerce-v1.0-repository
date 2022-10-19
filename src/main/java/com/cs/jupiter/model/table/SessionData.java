package com.cs.jupiter.model.table;

import java.util.Date;

public class SessionData {
	private String id;
	private int status;
	private String userId;
	private String bizId;
	private Date startTime;
	private Date endTime;
	public String getId() {
		return id;
	}
	public int getStatus() {
		return status;
	}
	public String getBizId() {
		return bizId;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
