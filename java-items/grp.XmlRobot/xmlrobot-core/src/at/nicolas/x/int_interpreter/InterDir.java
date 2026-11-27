/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;

public class InterDir {
	
	public String id = null;
	public String value = null;
	private boolean locked = false;		
	LogConnector lc = new LogConnector("io  Dir");
	LogConnector lcd = new LogConnector("");
	
	public InterDir (Element element) throws Exception {
		id = element.getAttributeValue("id");

		setValue(element);
	}		
	public boolean getIsLocked() {
		return locked;
	}
	public void setLockOn() {
		locked = true;
	}
	
	void setValue(Element element ) throws Exception {
		
		if ( element.getAttribute("x") != null )
			value = element.getAttributeValue("x");
		else
			value = element.getText();
		
		if ( value == null )
			throw new Exception("Defintion of dir received no value");	
		
		if ( Commons.hasFlag(element, "cool") );
			// value = Commons.vfs2xrFile(value);
		else 
			value = Commons.constructNormalizedDir(value);
		
		/*
		if ( !value.startsWith(CoreBuilder.ws.getSystemDirEscaped()) && !value.startsWith(CoreBuilder.ws.getWsDirEscaped()))
			lc.trace("outside Xml.Robot: "+value);
			*/
	}

	
	public String translate ( String in ) {

		return ( Commons.stringReplace(
									in,
									"(dir,"+id+")",
									value ));						

	}
	
	public void debugx(int index ,boolean only_unlocked, Level level, String prefix  ) {
		String v = Entities.exportEntities(value);

		if ( only_unlocked && locked )
			return;
		
		String is;
		if ( locked ) 
			is = Interpreter.debug_equal_lock;
		else
			is = Interpreter.debug_equal_nolock;		
					
		lcd.log(prefix+Interpreter.declareDebug
				+Commons.leadBlanks(Entities.exportEntities(id), Interpreter.lb2)
				+is
				+v+"."
				, level	);				
	}	

}