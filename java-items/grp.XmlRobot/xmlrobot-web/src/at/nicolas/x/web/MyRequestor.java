package at.nicolas.x.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import at.nicolas.x.int_specs.IInputRequestor;

public class MyRequestor extends HttpServletRequestWrapper 
implements IInputRequestor{

	public MyRequestor(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

}
