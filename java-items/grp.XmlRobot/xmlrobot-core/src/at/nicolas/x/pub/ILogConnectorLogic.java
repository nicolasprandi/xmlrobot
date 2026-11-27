/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.pub;

public interface ILogConnectorLogic {
	/*
	 * Logic-Messages to the logger start with one of the following signs
	 * + ... which increases a level, methods: logicStart
	 * - ... which decreases a level, methogs: logicEnd
	 * | ... which does a plain message, methods: logic
	 */
	// adds additional header text
	public void setLogicHeader( String headerTitle );
		
	public void logic(String msg );
	public void logic(String left, String msg );
	
	public void logicStart(String msg );
	public void logicStart(String left, String msg );
	
	public void logicEnd(String msg );	
	public void logicEnd(String left, String msg );
}