/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Mkdir implements ITask{


		
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		String dir = e.getAttributeValue("dir");
		if ( dir == null ) 
			throw new Exception("task 'mkdir' needs attribute 'dir'");
		if ( CommonsVfs.existsDir(dir,rh.getSitzung()));
			//lc.log_ext("Skipping, Exists already and therefore is not created: "+dir, this);
		else {
			CommonsVfs.mkdirCascade(dir,rh.getSitzung());
			//lc.log_ext("Created: "+dir, this);			
		}
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}