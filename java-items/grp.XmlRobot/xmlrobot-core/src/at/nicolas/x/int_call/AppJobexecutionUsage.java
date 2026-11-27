/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_call;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.run.Terminal;

public class AppJobexecutionUsage {
	
	// Logging

	private LogConnector lc = null;

	private String appId = null;
	private String instId = null;
	private String instDir = null;
	private String jobId = null;
	private Element flexElement = null;
	private Element flexElementEnd = null;
	private boolean dbtrace = false;
	private int silence = 0;
	private ITerminator terminator = null;	
	int call_depth = 0;
	

	
    public AppJobexecutionUsage ( 	String _appId,
    							String _instId,
								String _instDir,
								String _jobId,
								Element _flexElement,
								boolean _dbtrace,
								int _silence,
								ITerminator _terminator,
								int call_depth,
								Element _flexElementEnd
								) {
    	
    	appId = _appId;
    	instId = _instId;
    	instDir = _instDir;
    	jobId = _jobId;
    	flexElement = _flexElement;
    	dbtrace = _dbtrace;
    	silence = _silence;
    	terminator = _terminator;
    	this.call_depth = call_depth;
    	flexElementEnd = _flexElementEnd;
    	lc = new LogConnector ("AppJobexecution app="+appId+", instDir="+instDir+", job="+jobId);

    }
	public boolean isInterpreterShouldIgnoreTk() {
		return interpreterShouldIgnoreTk;
	}

	public void setInterpreterShouldIgnoreTk(boolean interpreterShouldIgnoreTk) {
		this.interpreterShouldIgnoreTk = interpreterShouldIgnoreTk;
	}
	private boolean interpreterShouldIgnoreTk = false;    
    
	public boolean isInterpreterShouldGiveBackTk() {
		return interpreterShouldGiveBackTk;
	}

	public void setInterpreterShouldGiveBackTk(boolean interpreterShouldGiveBackTk) {
		this.interpreterShouldGiveBackTk = interpreterShouldGiveBackTk;
	}
	private boolean interpreterShouldGiveBackTk = false;
	
	public boolean isInterpreterShouldIgnoreClassloading() {
		return interpreterShouldIgnoreClassloading;
	}
	public void setInterpreterShouldIgnoreClassloading(boolean interpreterShouldIgnoreClassloading) {
		this.interpreterShouldIgnoreClassloading = interpreterShouldIgnoreClassloading;
	}
	private boolean interpreterShouldIgnoreClassloading = false;



	public void run () throws JDOMException, IOException, Exception {
		
		lc.trace("started ...");

		if (dbtrace)
			CoreBuilder.db.trace(false,true);
		// nic123 24.03.2019
		ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(appId, instId, instDir,true,Terminal.offlineSitzung);
		// Commons.printElementList(configList,"DB VERSION");
		
		DbApp app = CoreBuilder.db.getAppById(appId);
		if ( app == null )
			throw new Exception("The app '"+appId+"' was not found.");
		
		DbJob job = app.getJm().selectJob(jobId);
		if ( job == null )
			throw new Exception("The job '"+jobId+"' could not be selected.");		
		
		Interpreter jobexecution = new Interpreter(
				job,
				appId,
				instId,
				false,
				silence,
				call_depth,
				flexElement,
				instDir,
				new GuiControllerDesktop(),
				Terminal.offlineSitzung);
		
		jobexecution.addConfigList(configListx);
		if ( flexElementEnd != null ) {
			jobexecution.setFlexEndElement(flexElementEnd);
		}
		jobexecution.setInterpreterShouldGiveBackTk(interpreterShouldGiveBackTk);
		jobexecution.setIgnoreClassloading(interpreterShouldIgnoreClassloading);
		jobexecution.setTerminatorBeforeRun(terminator);
		jobexecution.setSurrounder(Surrounding.getSurroundingDesktop());


		
		jobexecution.run();			

			lc.trace("well terminated ...");

		
	}
	
}