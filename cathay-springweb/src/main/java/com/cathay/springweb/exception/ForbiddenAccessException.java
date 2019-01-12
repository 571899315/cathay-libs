package com.cathay.springweb.exception;

import com.cathay.common.CathayBaseException;

public class ForbiddenAccessException extends CathayBaseException {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "403 forbidden";

	public ForbiddenAccessException() {
		super(403,MSG);
	}
	
	

}
