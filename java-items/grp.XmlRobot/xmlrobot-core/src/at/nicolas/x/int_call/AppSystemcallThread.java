/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_call;

import java.util.ArrayList;

import org.jdom2.Element;

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


public class AppSystemcallThread  {

	// Logging
	private LogConnector lc;

	private String systemcalljob = null;
    private String app = null;
    private String inst = null;
    private String instdir =null;
    private Element flex = null;

    private int silence = 0;
	private ITerminator terminator = null;
	int call_depth = 0;
    
	public AppSystemcallThread ( String _systemcalljob,
			               String _app,
			               String _inst,
			               Element _flex,
			   			   int _silence,
						   ITerminator _terminator,
							int call_depth ,
							String instdir ) throws Exception {
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
	

	public void run() throws Exception {

	   	lc.trace("started ...");

		DbJob job = CoreBuilder.db.getScJm().selectJob(systemcalljob);
		if ( job == null )
			throw new Exception("The systemcall-job '"+systemcalljob+"' could not be selected.");	
		
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
			configList = CoreBuilder.db.getExecutionProlog(app, inst ,true,true,Terminal.offlineSitzung);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// lc.fatalException("terminated bad, Error in building Db",e);
			
			throw e;
			
		}
	   	interpreter.addConfigList(configList);	
	   	interpreter.setTerminatorBeforeRun(terminator);
	   	interpreter.setSurrounder(Surrounding.getSurroundingDesktop());

		Sp.p("--sdfgsfg-----------------------servsice");

		CoreBuilder.appManager.startInterpreter(interpreter);
		
				
		lc.trace("started well ...");		

	}
		
}