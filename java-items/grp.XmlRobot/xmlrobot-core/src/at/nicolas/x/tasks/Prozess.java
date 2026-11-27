/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Prozess implements ITask{
		
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		String act = e.getAttributeValue("act");
		if ( act == null ) 
			throw new Exception("task 'process' needs attribute 'act'");
		else if ( act.equals("list") || act.equals("list_all")) {
			String file = e.getAttributeValue("file");
			if ( file == null ) 
				throw new Exception("task 'process' needs attribute 'file'");
			boolean all = false;
			if ( act.equals("list_all") )
				all = true;
			Element list = rh.getSitzung().getProcesses(all);
			Document docRet = new Document(list);
			CommonsVfs.writeToFile(docRet, file, rh.getSitzung());
		} else if ( act.equals("kill") ) {
			String pid = e.getAttributeValue("pid");
			if ( pid == null ) 
				throw new Exception("task 'process' needs attribute 'pid'");
			rh.getSitzung().killProcess(Commons.string2int(pid));
		}
		
				
		 		 
		return null;
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}