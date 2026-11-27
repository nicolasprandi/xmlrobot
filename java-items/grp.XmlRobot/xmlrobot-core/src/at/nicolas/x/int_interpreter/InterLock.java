/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.LangException;

public class InterLock {

	LogConnector lc = new LogConnector("Lock");
	LogConnector lcd = new LogConnector("");
	
	public String id = null;
	public String type = null;
	
	public InterLock (Element element) throws Exception {
		id = element.getAttributeValue("id");
		type = element.getAttributeValue("type");
		if ( id== null || type == null )
			throw new LangException(202,"task 'lock' must specifiy @id and @type");
		if ( type.equals("dir") || type.equals("file") || type.equals("prop") )
			;
		else
			throw new LangException(203,"task 'lock' must be of type dir|file|prop");
		
	}

	public void debugx(int index, Level level) {
		if ( index == 0)
			lcd.log(Interpreter.declareDebug
					+Commons.trailBlanks("lock",Interpreter.lb+Interpreter.lb2)
					+"= "
					+"("+type+","+id+")"
					, level);
		else
			lcd.log(Interpreter.declareDebug
					+Commons.trailBlanks("",Interpreter.lb+Interpreter.lb2)
					+"= "
					+"("+type+","+id+")"
					, level);				
	}	

}