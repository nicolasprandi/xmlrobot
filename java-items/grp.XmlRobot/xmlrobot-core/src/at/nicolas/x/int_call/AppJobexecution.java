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
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.GuiControllerDesktop;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;


public class AppJobexecution {
	
	// Logging

	private LogConnector lc = null;

	private String appId = null;
	private String instId = null;
	private String jobId = null;
	private Element flexElement = null;
	private Element flexElementEnd = null;
	private boolean dbtrace = false;
	private int silence = 0;
	private ITerminator terminator = null;
	int call_depth = 0;
	
    public AppJobexecution ( 	String _appId,
								String _instId,
								String _jobId,
								Element _flexElement,
								boolean _dbtrace,
								int _silence,
								ITerminator _terminator,
								int call_depth,
								Element _flexElementEnd
								) {
    	appId = _appId;
    	if ( appId == null )
    		Sp.p("... app==null");
    	instId = _instId;
    	jobId = _jobId;
    	flexElement = _flexElement;
    	flexElementEnd = _flexElementEnd;
    	dbtrace = _dbtrace;
    	
    	silence = _silence;
    	terminator = _terminator;
    	this.call_depth = call_depth;
    	lc = new LogConnector ("AppJobexecution app="+appId+", inst="+instId+", job="+jobId);
    	

    }


	public synchronized void run () throws JDOMException, IOException, Exception {
		String openMsg = "started AppJobexecution with app '"+appId+"'";
		if ( instId !=null)
			openMsg = openMsg+" and pro '"+instId+"'";
		lc.trace(openMsg);
		Sp.p (openMsg);

		if (dbtrace  )
			CoreBuilder.db.trace(false,true);
		
		ArrayList<Element> configList = CoreBuilder.db.getExecutionProlog(appId, instId, true,false,Terminal.offlineSitzung);
		
		DbApp app = CoreBuilder.db.getAppById(appId);
		if ( app == null )
			throw new Exception("The app '"+appId+"' was not found.");
		
		DbJob job = app.getJm().selectJob(jobId);
		if ( job == null ) {
			
			if ( !jobId.endsWith("?")) {
				Sp.p("The job '"+jobId+"' was not found in app '"+appId+"'");
				LogConnector lc = new LogConnector("call_job");
				lc.fatal("The job '"+jobId+"' was not found in app '"+appId+"'");
				throw new Exception("The job '"+jobId+"' was not found in app '"+appId+"'");

			}
			


		} else {
			Interpreter jobexecution = new Interpreter(
					job,
					appId,
					instId,
					false,
					silence,
					call_depth,
					flexElement,
					null,
					new GuiControllerDesktop(),
					Terminal.offlineSitzung);
			
			jobexecution.addConfigList(configList);
			jobexecution.setFlexEndElement(flexElementEnd);
			jobexecution.setTerminatorBeforeRun(terminator);
			jobexecution.setSurrounder(Surrounding.getSurroundingDesktop());
			
		   	
		   	Sp.p("-------------------------");
		   	Sp.p("----| AppJobExecution |--");
		   	Sp.p("-------------------------");
		   	Sp.pl("-- job="+job.getJobid());
		   	Sp.pl("-- appid="+appId);
		   	Sp.pl("-- isSystemcall="+false);
		   	Sp.pl("-- silence="+silence);	
		   	Sp.pl("-- call_depth="+call_depth);
		   	Sp.pl("-- flexElement="+Commons.toTextCompact(flexElement));		   	
		   	Sp.pl("--instdir="+null);		   	
		   	Sp.pl("--terminator=");					
		   	Sp.pl("--");
		   	
			jobexecution.run();			
		}
	}
	
}