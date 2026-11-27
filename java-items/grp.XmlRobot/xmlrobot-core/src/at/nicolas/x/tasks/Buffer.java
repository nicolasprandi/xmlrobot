/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Buffer implements ITask{

	ILogConnector lc;
	String tname;

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		throw new Exception("Buffer not implemented yet.");
		
	}

	public String getExtensionId() {
		return ( "buffer");
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}