/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import at.nicolas.x.int_specs.KnownException;

public class LangException extends KnownException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LangException() {
		// TODO Auto-generated constructor stub
	}
	
	public LangException(String message) {
		super ( message);

	}
	
	public LangException(int code, String message ) {
		super("#"+Integer.toString(code)+" "+message);
	}

	public LangException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public LangException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}



}