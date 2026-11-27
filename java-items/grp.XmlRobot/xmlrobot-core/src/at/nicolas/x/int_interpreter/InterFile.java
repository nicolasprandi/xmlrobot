/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;

public class InterFile {
	
	public String id = null;
	public String future_fn = null;
	public boolean locked = false;
	LogConnector lc = new LogConnector("io File");
	LogConnector lcd = new LogConnector("");

	InterFile (Element element) throws Exception {
		id = element.getAttributeValue("id");

				
		setValue ( element );	
	}
	
	
	void setValue ( Element element) throws Exception {

		if ( element.getAttribute("x") != null )
			future_fn = element.getAttributeValue("x");
		else
			future_fn = element.getText();
		
		if ( future_fn == null )
			throw new Exception("Defintion of file received no value");
		
		if ( Commons.hasFlag(element, "cool") );
		// value = Commons.vfs2xrFile(value);
		else 		
			future_fn = Commons.constructNormalizedFile(future_fn);
		
		/*
		while ( value.endsWith("/") || value.endsWith("\\")) {
			value = value.substring(0,value.length()-1);
		}
		*/

		/* performance and not really realable
		if ( !future_fn.startsWith(CoreBuilder.ws.getSystemDirEscaped()) && !future_fn.startsWith(CoreBuilder.ws.getWsDirEscaped()))
			lc.trace("outside Xml.Robot: "+future_fn);
			*/
		/*
		else
			lc.trace("inside workspace: "+value);
		*/
		

		
	}
	
	public String translate ( String in ) throws Exception {

		Commons.did_string_replace = false;
		String ret =Commons.stringReplace(
									in,
									"(file,"+id+")",
									future_fn );

		return ret;
		
	}

	public void debugx(int index, boolean only_unlocked, Level level, String prefix  ) {
		String v = Entities.exportEntities(future_fn);

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
				, level );				
	}	
		

}