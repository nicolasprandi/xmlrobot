/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;


public class InterProp {
	
	LogConnector lc = new LogConnector("Prop");
	LogConnector lcd = new LogConnector("");	

	public static final String locked_starter = "     l";
	public static final String locked_nostarter = "      ";
	
	public String idE = null;
	public String valueE = null;
	public boolean locked = false;		
	
	public InterProp (Element elementE) throws Exception {
		idE = elementE.getAttributeValue("id");

		setValue ( elementE);

	}
	

	void setValue ( Element elementE) throws Exception {
		
		
		if ( elementE.getAttribute("x") != null )
			valueE = elementE.getAttributeValue("x");
		else
			valueE = elementE.getText();
		
		if ( valueE == null )
			throw new Exception("Defintion of prop received no value");	

	}
	public String translate ( String in ) {
		
		// lc.core("called prop.translateValue('"+in+"')",LogConnector.CORE_TRANSLATE);
		return ( Commons.stringReplace(
									in,
									"(prop,"+idE+")",
									valueE ));
	}
	
	public void debugx(int index, boolean only_unlocked, Level level, String prefix ) {
		String v = Entities.exportEntities(valueE);

		
		if ( only_unlocked && locked )
			return;
		
		String is;
		if ( locked ) 
			is = Interpreter.debug_equal_lock;
		else
			is = Interpreter.debug_equal_nolock;		
		/*
		if ( index == 0 ) 
			lcd.current(Interpreter.declareDebug
					+ Commons.trailBlanks("(prop,",Interpreter.lb2) 
					+ ")");
					*/		
		lcd.log(prefix+Interpreter.declareDebug
				+Commons.leadBlanks(Entities.exportEntities(idE), Interpreter.lb2)
				+is
				+v+"."
				, level	);				
	}	

}