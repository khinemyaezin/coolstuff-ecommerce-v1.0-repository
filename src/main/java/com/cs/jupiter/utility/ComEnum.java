package com.cs.jupiter.utility;

import org.springframework.stereotype.Component;

@Component
public class ComEnum {
	public static enum ErrorStatus{
		NA(100,"NA"),
		Success(200,"Success"),
		ClientError(400,"ClientError"),
		ServerError(500,"ServerError"),
		DatabaseError(600,"DatabaseError"),
		Unauthorize(401,"Unauthorize");
		ErrorStatus(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
	public static enum RowStatus{
		Normal(1,"Normal"),
		Deleted(4,"Deleted"),
		Inactive(2,"Inactive");
		
		RowStatus(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;
		

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	
	}
	
	public static enum Status{
		Default(0,"Default"),
		True(1,"True"),
		False(2,"False");
		
		Status(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
	public static enum SigninMethod{
		Email('e',"Email"),
		Phone('p',"True");
		
		SigninMethod(char code, String description) {
			this.code = code;
			this.description = description;
		}
		private final char code;
		private final String description;

		public char getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
	
	public static enum BrandStatus{
		Pending(1,"pending"),
		Completed(2,"completed"),
		Reject(4,"reject"),
		Holding(6,"holding");
		
		BrandStatus(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
	public static enum AuthStatus{
		LoginOrActive(2,"LoginOrActive"),
		Logout(4,"Logout");
		
		AuthStatus(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
	public static enum PriceType{
		ByRatio(1,"By Ratio"),
		BySpecific(2,"By Specific");
		
		PriceType(int code, String description) {
			this.code = code;
			this.description = description;
		}
		private final int code;
		private final String description;

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}
	}
}
