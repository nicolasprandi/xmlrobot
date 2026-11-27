/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Remove_Dtd implements ITask{

	String 	in;
	String	out;

	


	
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {

		
		in = 		Commons.para2string(e.getAttributeValue("in"),null,"in");
		out = 		Commons.para2string(e.getAttributeValue("out"),in,"out");
		
		Document doc =  CommonsVfs.getXmldocumentByFilename(in,rh.getSitzung());
		doc.setDocType(null);
		CommonsVfs.writeToFile(doc, out,rh.getSitzung());
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}