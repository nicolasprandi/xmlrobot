/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.io.File;
// File wird gebraucht als parameter 
// f�r den ProcessBuilder
// da wird das directory gesetzt in dem das ablaufen soll
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Exe implements ITask {
	

	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		

		ArrayList<String> argList = new ArrayList<String>();
		
		List<Element> records = e.getChildren("arg");
		Iterator<Element> irecords = records.iterator();
	
		while ( irecords.hasNext()) {

			Element ex = (Element)irecords.next();
			if ( ex.getText().startsWith("file:///")) {
				argList.add(new String(ex.getText().substring(8)));
			} else
				argList.add(new String(ex.getText()));
		
		}
			
		ProcessBuilder pb = new ProcessBuilder(argList);
		if ( e.getAttribute("dir") != null ) {
			String d = e.getAttributeValue("dir");
			if ( d.startsWith("file:///")) {
				d = d.substring(8);
			}
			String process_dir = Commons.removeFsInCase(d);
			pb.directory(new File (process_dir ));
		}
		
		pb.start();

		return null;
		
	}

	public String getExtensionId() {
		return ( "exe");
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}