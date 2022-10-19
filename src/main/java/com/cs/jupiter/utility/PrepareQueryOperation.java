package com.cs.jupiter.utility;

import org.springframework.stereotype.Component;

@Component
public class PrepareQueryOperation {
	public enum Operator {
		EQUAL(0, "="), LESSTHEN(1, "<"), LESSTHEN_EQUAL(2, "<="), GREATERTHEN(3, ">"), GREATERTHEN_EQUAL(4,
				">="), LIKE_START(5,
						"like"), LIKE_END(6, "like"), LIKE_ALL(7, "like"), NOT_EQUAL(8, "<>"), BETWEEN(9, "and");

		Operator(int code, String description) {
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

	public enum Type {
		ID, VARCHAR, DATE, NUMBER, BOOLEAN, CHAR;
		Type() {
		}
	}

}	
