/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

public class CustomException extends KnownException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error_type = null;

	public static String getClassName() {
		CustomException x = new CustomException(null,null);
		return ( x.getClass().getName() );
	}
	public String getError_type() {
		return error_type;
	}

	public CustomException(String _msg, String _error_type) {
		super(_msg);
		error_type = _error_type;

	}

}