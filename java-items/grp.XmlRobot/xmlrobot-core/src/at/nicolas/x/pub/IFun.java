/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import java.util.ArrayList;

import org.jdom2.Element;
/*
 * This is the interface for a custom Function
 */

public interface IFun {
	// statucFundef will be called on the fundef-task
	// e .... The element
	// id ... The id under which the function is registered
	// rh ... the request-handler
	// lc ... LogConnector to use
	public void staticFundef ( Element eFundef, String id, IRequestHandler rh);
	
	// execute will be executed for every function-call, so that is doing the actual work
	// vcar_list ... the parameters as an arralist of Vcar
	// rh, lc ...... as usual
	public Vcar execute ( ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception;
	
	// getBugId should return the id under which the function is know, this will be used
	// by logging on logleve extension
	//public String getExtensionId();

}