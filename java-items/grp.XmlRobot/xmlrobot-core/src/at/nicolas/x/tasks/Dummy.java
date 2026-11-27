/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Dummy implements ITask{


	String tname;

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		Parameters p = new Parameters();
		p.setParameter("(", "(dir,services)");
		p.setParameter("prop_(", "(dir,services)");
		p.setParameter("dir_(", "(dir,services)");
		p.setParameter("file_(", "(dir,services)");
		p.setParameter("prop_(", "(dir,services)");
		p.setParameter("dir_(", "(dir,services)");
		p.setParameter("file_(", "(dir,services)");	
		p.setParameter("prop_(hallo", "(dir,system)");
		p.setParameter("dir_(hallo", "(dir,system)");
		p.setParameter("file_(hallo", "(dir,system)");	
		p.setParameter("prop_", "(file,takeoff)");
		p.setParameter("dir_", "(file,takeoff)");
		p.setParameter("file_", "(file,takeoff)");			
		throw new LangException(500,"this task is not implemented yet.");
		// return p;
	}

	public String getExtensionId() {
		return ( "?");
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}