/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Create_File implements ITask{

	String val;
	String out;
	String home;

	String tname;
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "create_file";

		
		val = Commons.getXValue(e);
		
		
		if ( Commons.hasFlag(e, "dont_unescape_on_write")) {
		
			val = Entities.importEntities(val);
		
		}
		
		out = null;
		if ( e.getAttribute("out")!=null)
			out = e.getAttributeValue("out");
		else if ( e.getAttribute("file")!=null)
			out = e.getAttributeValue("file");		
		
		if ( out==null)
			throw new Exception ("create_file wants attribute 'out' or 'file'");		
		
		String encoding = "UTF-8";
		if ( e.getAttribute("encoding") != null ) {
			encoding = e.getAttributeValue("encoding");
			//lc.log_ext("encoding="+encoding,this);
		}
	
		CommonsVfs.writeToEncodedFile(val,out,encoding,rh.getSitzung());
		 
		return null;
	}
	


	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}