/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_call;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;

public class AppJobexecutionIncludeFork {
	
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
	
	IGuiController iGuiController =null;
	ISitzung iSitzung = null;
	Element flexElementEnd = null;
	boolean interpreterShouldGiveBackTk;
	boolean interpreterShouldIgnoreClassloading;
	Surrounding surrounding;
    public AppJobexecutionIncludeFork ( 	
    							String _appId,
								String _instId,
								String _jobId,
								Element _flexElement,
								boolean _dbtrace,
								int _silence,
								ITerminator _terminator,
								int call_depth,
								String _instdir,
								IGuiController iGuiController,
								ISitzung iSitzung,
								Element flexElementEnd,
								boolean interpreterShouldGiveBackTk,
    							boolean interpreterShouldIgnoreClassloading,
    							Surrounding surrounding
								) {
    	this.iGuiController = iGuiController;
    	this.iSitzung = iSitzung;
    	this.flexElementEnd = flexElementEnd;
    	this.interpreterShouldGiveBackTk = interpreterShouldGiveBackTk;
    	this.interpreterShouldIgnoreClassloading = interpreterShouldIgnoreClassloading;
    	this.surrounding = surrounding;
    	appId = _appId;
    	instId = _instId;
    	instdir = _instdir;
    	jobId = _jobId;
    	flexElement = _flexElement;
    	dbtrace = _dbtrace;
    	silence = _silence;
    	terminator = _terminator;
    	this.call_depth = call_depth;
    	lc = new LogConnector ("AppJobexecutionIncludeFork app="+appId+", inst="+instId+", job="+jobId);

    }
    
	public void run () throws JDOMException, IOException, Exception {

		lc.trace("started ...");

		if (dbtrace)
			CoreBuilder.db.trace(false,true);
		/*
		ArrayList<Element> configList = CoreBuilder.db.getExecutionProlog(
				appId, instId,true,false,iSitzung);*/
		
		ArrayList<Element> configList = CoreBuilder.db.getExecutionPrologAnonym(
				 appId,
				 instId,
				 instdir,
				true,
				false,
				iSitzung);
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
				iGuiController,
				iSitzung);
		
		jobexecution.addConfigList(configList);
		jobexecution.setTerminatorBeforeRun(terminator);
		jobexecution.setSurrounder(surrounding);
		if ( flexElementEnd != null ) {
			jobexecution.setFlexEndElement(flexElementEnd);
		}
		jobexecution.setInterpreterShouldGiveBackTk(interpreterShouldGiveBackTk);
		jobexecution.setIgnoreClassloading(interpreterShouldIgnoreClassloading);
		jobexecution.setInterpreterShouldIgnoreTk(true);
		jobexecution.setTerminatorBeforeRun(terminator);

		
	   	Sp.p("---------AppJobexectionIncludeFork");
	   	//System.out.println("Starting to run fork");
	   	
	   	ForkyThread it = new ForkyThread();
	   	String stringFlex ="";
	   	if ( flexElement !=null )
	   		stringFlex= " paras="+Commons.toTextCompact(flexElement);
	   	it.setA(jobexecution,
	   			iSitzung.registerProcess(it, "forked app-call","appid="+appId+", job="+job.getJobid()+stringFlex)
				);
		it.start();
	   	

		System.out.println("Fork started.");

	}
	// Jawohll !!
	class ForkyThread extends StoppableThread {
		Interpreter interpreter;
		int pid;
		void setA( Interpreter _interpreter, int _pid) {
			interpreter = _interpreter;
			pid = _pid;
		}

		public void run () {
			try {
				interpreter.run();
			} finally {
				try {
					interpreter = null;
					iSitzung.finishedProcess(pid);
				} catch (Exception e) {
					System.out.println("Could not kill process after successfull exection, "+e.getMessage());
					e.printStackTrace();
				}
			}
		}

		@Override
		public void stopExecution() {
			interpreter.shouldStopThread();
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getStateStoppable() {
			// TODO Auto-generated method stub
			return interpreter.getState();
		}
	}
	
	

	
}