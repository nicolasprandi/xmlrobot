/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.int_specs;

import org.jdom2.Element;

import at.nicolas.x.pub.ISitzung;


public interface ITiHandler {
	public boolean triggerButtonsByButton();	
	public Surrounding getSurrounding();
	public ISitzung getSitzung();
	public void printTrace(String msg );	
	public Element translate ( Element e ) throws Exception ;
 	public String translateString ( String x ) throws Exception;
//	public Element translateElement ( Element x ) throws Exception;
	public void setVar(String varType, String varIdE, String valueE) throws Exception;
	public void setVarFollow(String varType, String varIdE, String valueE, String followJobN) throws Exception;
	public String getVar ( String varType, String varIdE )throws Exception;
//	public String translateExportValue( String workon ) throws Exception;
	public ITerminator getTerminator() throws Exception;
	public String getGlobalAndRemove(String global_id ) throws Exception;
	public String getAppId( ) throws Exception;
	public String getProId( ) throws Exception;
	public String getAppDir( ) throws Exception;
	public String getProDir( ) throws Exception;
	public String getUriPresentation ( String uri );
	// public String getMyFileResultsDir( ) throws Exception;
	// public String getMyFolderResultsDir( ) throws Exception;


		
}