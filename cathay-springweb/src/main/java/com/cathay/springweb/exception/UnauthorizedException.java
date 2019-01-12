package com.cathay.springweb.exception;

import com.cathay.common.CathayBaseException;

public class UnauthorizedException extends CathayBaseException {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "401 Unauthorized";

	public UnauthorizedException() {
		super(401,MSG);
	}
	
	

}
