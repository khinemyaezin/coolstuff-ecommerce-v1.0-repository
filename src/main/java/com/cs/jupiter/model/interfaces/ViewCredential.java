package com.cs.jupiter.model.interfaces;

import java.util.Date;

import com.cs.jupiter.model.interfaces.Auth;

public class ViewCredential {
	private int currentRow = -1;
	private int maxRowsPerPage = -1;
	private String orderby = "";
	private String sorting = "";
	
	private String id;
	private String code;
	private String name;
	private int status;
	private Date cdate;
	private Date mdate;
	private int bizStatus;
	private String fromDate;
	private String toDate;
	private boolean b1;
	private boolean acceptNull;
	private Auth authencation;

	public ViewCredential(){
		this.id = "-1";
		this.status = -1;
		this.b1 = false;
		this.acceptNull = false;
	}
	
	public int getCurrentRow() {
		return currentRow;
	}
	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}
	public int getMaxRowsPerPage() {
		return maxRowsPerPage;
	}
	public void setMaxRowsPerPage(int maxRowsPerPage) {
		this.maxRowsPerPage = maxRowsPerPage;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getSorting() {
		return sorting;
	}

	public void setSorting(String sorting) {
		this.sorting = sorting;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCdate() {
		return cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public Date getMdate() {
		return mdate;
	}

	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}

	public boolean isB1() {
		return b1;
	}

	public void setB1(boolean b1) {
		this.b1 = b1;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Auth getAuthencation() {
		return authencation;
	}

	public void setAuthencation(Auth authencation) {
		this.authencation = authencation;
	}

	public int getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(int bizStatus) {
		this.bizStatus = bizStatus;
	}

	public boolean isAcceptNull() {
		return acceptNull;
	}

	public void setAcceptNull(boolean acceptNull) {
		this.acceptNull = acceptNull;
	}
	
	
}
