/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

import org.jdom2.Element;


public interface ITask {
	
	public void staticTaskdef ( Element eTaskdef, String id, IRequestHandler rh);
	
	// execute will be executed for every task-call
	// Parameters holds a list of values which will result as new variables in memory
	//            if you dont need that return null
	//            a naming-convetion of these parameters decides which type of variable it will be
	//            prop_* will be a property
	//            file_* will be a file
	//            dir_* will be a directory
	public Parameters execute ( Element eExecute, IRequestHandler rh) throws Exception;
	
	//public String getExtensionId();

}