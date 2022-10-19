package com.cs.jupiter.model.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cs.jupiter.error.UnAuthException;
import com.cs.jupiter.utility.ComEnum;

public class ViewResult<T> {
	public List<T> list = new ArrayList<T>();
	public T data;
	public String message;
	public String errorCode;
	public int status;
	public int totalItem = 0;

	public ViewResult() {
		this.message = "";
		this.status = -1;
		this.totalItem = 0;
		this.errorCode = "";
	}

	public ViewResult(T d, List<T> dlist) {
		this.data = d;
		this.list = dlist;
		this.message = "";
		this.status = -1;
		this.totalItem = 0;
		this.errorCode = "";
	}

	public ViewResult(int status, String mes) {
		this.message = mes;
		this.status = status;
	}

	public ViewResult(Exception e) {
		
		this.message = e.getMessage();
		if(e.getClass() == UnAuthException.class){
			this.status = ComEnum.ErrorStatus.Unauthorize.getCode();
		}else{
			this.status = ComEnum.ErrorStatus.DatabaseError.getCode();
		}
	}

	public void success() {
		this.status = ComEnum.ErrorStatus.Success.getCode();
	}

	public void error() {
		this.status = ComEnum.ErrorStatus.DatabaseError.getCode();
	}

	public void error(String error) {
		this.status = ComEnum.ErrorStatus.DatabaseError.getCode();
		this.message = error;
	}

	public boolean isSucces() {
		return this.status == ComEnum.ErrorStatus.Success.getCode();
	}

	public void completeTransaction(Connection conn) throws SQLException {
		if (!conn.getAutoCommit()) {
			if (this.status != ComEnum.ErrorStatus.Success.getCode()) {
				conn.rollback();
			} else {
				conn.commit();
			}
		}

	}
}
