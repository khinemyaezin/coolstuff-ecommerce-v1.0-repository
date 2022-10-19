package com.cs.jupiter.error;

import org.springframework.stereotype.Component;

@Component
public class StockSetupError {
	public enum ErrorStatus{
		STK_ER_101(101,"");
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
}
