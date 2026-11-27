/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_call;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.WebVfs.XrRqRp;
import at.nicolas.x.WebVfs.XrRqRpEx;
import at.nicolas.x.WebVfs.XrRqRpTk;
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

public class AppSystemcall  {

	// Logging
	private LogConnector lc;

	private String systemcalljob = null;
    private String app = null;
    private String inst = null;
    private String instdir = null;
    private Element flex = null;
    private Element flexElementEnd = null;

    private int silence = 0;
	private ITerminator terminator = null;
	int call_depth = 0;
	
    
	public AppSystemcall ( String _systemcalljob,
			               String _app,
			               String _inst,
			               Element _flex,
			   			   int _silence,
						   ITerminator _terminator,
							int call_depth,
							String instdir) throws Exception {
		
		systemcalljob = _systemcalljob;
		app = _app;
		inst = _inst;
		this.instdir = instdir;
		flex = _flex;
		silence = _silence;
    	terminator = _terminator;
    	this.call_depth = call_depth;
		
		lc = new LogConnector ("AppSystemcall app="+app+", inst="+inst+", sysjob="+systemcalljob);
		
	}
	public AppSystemcall ( String _systemcalljob,
            String _app,
            String _inst,
            Element _flex,
			   int _silence,
			   ITerminator _terminator,
				int call_depth,
				String instdir,
				Element _flexElementEnd) throws Exception {

		systemcalljob = _systemcalljob;
		app = _app;
		inst = _inst;
		this.instdir = instdir;
		flex = _flex;
		silence = _silence;
		terminator = _terminator;
		this.call_depth = call_depth;
		flexElementEnd = _flexElementEnd;
		
		lc = new LogConnector ("AppSystemcall app="+app+", inst="+inst+", sysjob="+systemcalljob);
	
	}	
	

	public void run() throws Exception {

	   	lc.trace("started ...");


		DbJob job = CoreBuilder.db.getScJm().selectJob(systemcalljob);
		if ( job == null )
			throw new Exception("The service-call-job '"+systemcalljob+"' could not be selected.");	
		
		// Conf.db.trace();
		Interpreter interpreter = new Interpreter ( 
				job,
				app, 
				inst,
				true,
				silence,
				call_depth,
				flex,
				instdir,
				new GuiControllerDesktop(),
				Terminal.offlineSitzung);	

		ArrayList<Element> configList=null;
		try {
			configList = CoreBuilder.db.getExecutionProlog(app, inst,true,true,Terminal.offlineSitzung);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//lc.fatalException("terminated bad, Error in building Db",e);
		
			throw e;
			
		}
	   	interpreter.addConfigList(configList);	
	   	interpreter.setTerminatorBeforeRun(terminator);
	   	interpreter.setFlexEndElement(flexElementEnd);
   	
		Sp.p("-------------------------servsice");
		Sp.p("-------------------------servsice");
		Sp.p("--FRO USER INTERFACE     --------");
		Sp.p("-------------------------servsice");		   	

		interpreter.setSurrounder(Surrounding.getSurroundingDesktop());   	
	   	interpreter.run();
		XrRqRp rr = interpreter.getRqRpAfterRunning();
		if ( rr instanceof XrRqRpEx ) {
			throw ((XrRqRpEx) rr).getException();			
		}
		if ( rr instanceof XrRqRpTk ) {
			// throw ((XrRqRpEx) rr).getException();
			// hier den tk durchf�hren
			// terminator.executeTk(etE,tiLogLevel,appid,appdir,instid,instdir,this);
			XrRqRpTk rr2 = (XrRqRpTk)rr;
			
			terminator.executeTk(
					rr2.ticketManager.getTkElementForPrint(), 
					rr2.getLoglevel(),
					rr2.getApp(),
					rr2.getAppdir(),
					rr2.getInst(),
					rr2.getInst_dir(),
					rr2.ticketManager.tiHandler
					);
			
			interpreter.continueTheRunAfterResponse();
			
			
		}		
				
		lc.trace("started well ...");		

	}
		
}