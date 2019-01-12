package com.cathay.common2.lock;

import com.cathay.common.CathayBaseException;

public class LockException extends CathayBaseException {
	private static final long serialVersionUID = 1L;

	public LockException(String e) {
		super(9999,e);
	}

	public LockException(Throwable cause) {
		super(9999, cause.getMessage(), cause);
	}
	
	
}
