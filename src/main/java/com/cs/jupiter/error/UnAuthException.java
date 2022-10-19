package com.cs.jupiter.error;

public class UnAuthException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnAuthException(String errorMessage) {
        super(errorMessage);
    }
	public UnAuthException() {
        super("invalid token or unauthorized");
    }
}
