/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Secret implements ITask{


	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		throw new Exception("Secret not implemented yet.");
		
	}

	public String getExtensionId() {
		return ( "secret");
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}