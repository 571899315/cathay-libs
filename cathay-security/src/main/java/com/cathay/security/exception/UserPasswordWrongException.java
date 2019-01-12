package com.cathay.security.exception;

import com.cathay.common.CathayBaseException;

public class UserPasswordWrongException extends CathayBaseException {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "密码错误";

	public UserPasswordWrongException() {
		super(4001,MSG);
	}
	
	

}
