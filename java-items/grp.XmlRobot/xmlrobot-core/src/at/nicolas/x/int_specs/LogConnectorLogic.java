/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import at.nicolas.x.pub.ILogConnectorLogic;

public class LogConnectorLogic extends LogConnector implements ILogConnectorLogic {

	private String from;
	private String counterTitle;
	public LogConnectorLogic(String counterTitle) {		
		super(null);
		this.counterTitle = counterTitle;
		from = counterTitle;
	}
	/*
	public LogConnectorLogic(int counterTitle) {		
		super(null);
		this.counterTitle = String.valueOf(counterTitle);
	}	
	*/
	
	public void setLogicHeader( String headerTitle ) {
		from = counterTitle + "."+ headerTitle;
	}
	public void logic(String msg ) {
		debug_logic("| "+from+": "+msg);
	}
	
	public void logicStart(String msg ) {
		debug_logic("+ "+from+": "+msg);
	}	
	
	public void logicEnd(String msg ) {
		debug_logic("- "+from+": "+msg);
	}		
	
	public void logic(String left, String msg ) {
		if ( msg != null)
			debug_logic("| "+""+from+" "+left+": "+msg);
		else
			debug_logic("| "+""+from+" "+left);
	}	
	
	public void logicStart(String left, String msg ) {
		if ( msg != null)
			debug_logic("+ "+""+from+" "+left+": "+msg);
		else
			debug_logic("+ "+""+from+" "+left);
	}	
	
	public void logicEnd(String left, String msg ) {
		if ( msg != null)
			debug_logic("- "+""+from+" "+left+": "+msg);
		else
			debug_logic("- "+""+from+" "+left);
	}		

}