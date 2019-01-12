package com.cathay.security.exception;

import com.cathay.common.CathayBaseException;

public class UserNotFoundException extends CathayBaseException {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "用户不存在";

	public UserNotFoundException() {
		super(4001,MSG);
	}
	
	

}
