/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Sysprint implements ITask{


		
	public Parameters execute(Element e, IRequestHandler rh ) throws Exception {
		String toprint = Commons.getXValue(e);
		Sp.p(toprint+"                       **** SYSPRINT **** ");
		 		 
		return null;
	}


	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}