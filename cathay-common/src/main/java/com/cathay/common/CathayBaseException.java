package com.cathay.common;

public class CathayBaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;
	
	public CathayBaseException() {
		super();
	}

	public CathayBaseException(int code,String message) {
		super(message);
		this.code = code;
	}
	
	public CathayBaseException(int code,String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	

}
