/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_call;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;

public class AppJobexecutionThread {
	
	// Logging

	private LogConnector lc = null;

	private String appId = null;
	private String instId = null;
	private String instdir = null;
	private String jobId = null;
	private Element flexElement = null;
	private boolean dbtrace = false;
	private int silence = 0;
	private ITerminator terminator = null;
	int call_depth = 0;
	
    public AppJobexecutionThread ( 	String _appId,
								String _instId,
								String _jobId,
								Element _flexElement,
								boolean _dbtrace,
								int _silence,
								ITerminator _terminator,
								int call_depth,
								String _instdir
								) {
    	appId = _appId;
    	instId = _instId;
    	instdir = _instdir;
    	jobId = _jobId;
    	flexElement = _flexElement;
    	dbtrace = _dbtrace;
    	silence = _silence;
    	terminator = _terminator;
    	this.call_depth = call_depth;
    	lc = new LogConnector ("AppJobexecution app="+appId+", inst="+instId+", job="+jobId);

    }
    
	public void run () throws JDOMException, IOException, Exception {

		lc.trace("started ...");

		if (dbtrace)
			CoreBuilder.db.trace(false,true);
		
		ArrayList<Element> configList = CoreBuilder.db.getExecutionProlog(appId, instId,true,false,Terminal.offlineSitzung);
		// Commons.printElementList(configList,"DB VERSION");
		
		DbApp app = CoreBuilder.db.getAppById(appId);
		if ( app == null )
			throw new Exception("The app '"+appId+"' was not found.");
		
		DbJob job = app.getJm().selectJob(jobId);
		if ( job == null ) {
			Exception ex = new Exception("The job '"+jobId+"' could not be selected.");
			// lc.fatalException(ex);
			throw ex;
		}
		Interpreter jobexecution = new Interpreter(
				job,
				appId,
				instId,
				false,
				silence,
				call_depth,
				flexElement,
				instdir,
				new GuiControllerDesktop(),
				Terminal.offlineSitzung);
		
		jobexecution.addConfigList(configList);
		jobexecution.setTerminatorBeforeRun(terminator);
		jobexecution.setSurrounder(Surrounding.getSurroundingDesktop());

		
	   	Sp.p("---------servsice");
		
		// lc.trace("Went into Conf.appManager.startInterpreter...");
		CoreBuilder.appManager.startInterpreter(jobexecution);

	}
	
}