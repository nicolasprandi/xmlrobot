/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jdom2.Element;

import at.nicolas.webwalk.WebDriverTasks;
import at.nicolas.webwalk.WebElementTasks;
import at.nicolas.webwalk.WebTabulator;
import at.nicolas.x.compare.FileDiff;
import at.nicolas.x.compare.PdfToText;
import at.nicolas.x.compare.VisualCompare;
import at.nicolas.x.csv2xml.Csv2Xml;
import at.nicolas.x.generator.Shuffler;
import at.nicolas.x.hibiscus.Integriere;
import at.nicolas.x.int_coop.CoopConnection;
import at.nicolas.x.int_coop.CoopCrypto;
import at.nicolas.x.int_coop.CoopDbc;
import at.nicolas.x.int_coop.CoopDbci;
import at.nicolas.x.int_coop.CoopLoop_Regex;
import at.nicolas.x.int_coop.CoopLoop_While;
import at.nicolas.x.int_coop.CoopLoop_Xml;
import at.nicolas.x.int_coop.CoopPrint;
import at.nicolas.x.int_coop.CoopSpirit;
import at.nicolas.x.int_coop.InterLoopRegex;
import at.nicolas.x.int_coop.InterLoopSelect;
import at.nicolas.x.int_coop.InterLoopWhile;
import at.nicolas.x.int_coop.InterLoopXml;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.DummyRequestHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.LogConnectorLogic;
import at.nicolas.x.panorama.AggregateScan;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.ITaskLogic;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.task_quickscan.QuickScanTask;
import at.nicolas.x.tasks.Buffer;
import at.nicolas.x.tasks.Convert;
import at.nicolas.x.tasks.Copy;
import at.nicolas.x.tasks.Create_File;
import at.nicolas.x.tasks.Delete;
import at.nicolas.x.tasks.Dummy;
import at.nicolas.x.tasks.Excel_Form;
import at.nicolas.x.tasks.Excel_Matrix;
import at.nicolas.x.tasks.Excel_Sheets;
import at.nicolas.x.tasks.Excel_Table;
import at.nicolas.x.tasks.Excel_Update;
import at.nicolas.x.tasks.Exe;
import at.nicolas.x.tasks.Javascript;
import at.nicolas.x.tasks.KeySourceReport;
import at.nicolas.x.tasks.KeyTargetReport;
import at.nicolas.x.tasks.Mkdir;
import at.nicolas.x.tasks.ParaSplit;
import at.nicolas.x.tasks.Parallel;
import at.nicolas.x.tasks.Prozess;
import at.nicolas.x.tasks.Scan;
import at.nicolas.x.tasks.Secret;
import at.nicolas.x.tasks.Split;
import at.nicolas.x.tasks.Stack;
import at.nicolas.x.tasks.Sysprint;
import at.nicolas.x.tasks.Systempost;
import at.nicolas.x.tasks.Ulm;
import at.nicolas.x.tasks.UniqueCoop;
import at.nicolas.x.tasks.Unzip;
import at.nicolas.x.tasks.Wait;
import at.nicolas.x.tasks.Xml_Update;
import at.nicolas.x.tasks.Xquery;
import at.nicolas.x.tasks.Xslt;
import at.nicolas.x.tasks.Zip;

public class PlugTryTask {
	
	Element step = null;
	Interpreter interpreter =null;
	DbJob job = null;
	HashMap<String, PlugTaskdef> taskdefMap = null;
	// private LogConnector lc = new LogConnector("");

	static HashMap<String,Class<?>> classMap;
	static boolean salamander_mode_is_production = false;
	public static void startupActions( boolean report, boolean online ) throws Exception {
		classMap = new HashMap<String, Class<?>>();
		Element dummyElement = new Element("dummy");
		IRequestHandler dummyRequestHandler = new DummyRequestHandler();

		// hibiscus-integration
		//classMap.put("integriere",at.nicolas.x.hibiscus.a.class);	
		
		/*
		if ( !salamander_mode_is_production ) {
			
			classMap.put("wdriver",at.nicolas.webwalk.d.class);			
			classMap.put("welement",at.nicolas.webwalk.j.class);
			classMap.put("wtabulator",at.nicolas.webwalk.k.class);	

			(new at.nicolas.webwalk.d()).staticTaskdef(
					dummyElement, "wdriver", dummyRequestHandler);
			
			(new at.nicolas.webwalk.j()).staticTaskdef(
					dummyElement, "welement", dummyRequestHandler);
			
			(new at.nicolas.webwalk.k()).staticTaskdef(
					dummyElement, "wtabulator", dummyRequestHandler);
			
		}
		*/

		classMap.put("csv2xml",Csv2Xml.class);	
		classMap.put("dbc",CoopDbc.class);	
		(new CoopDbc()).staticTaskdef(
				dummyElement, "dbc", dummyRequestHandler);
		
		classMap.put("dbci",CoopDbci.class);	
		(new CoopDbci()).staticTaskdef(
				dummyElement, "dbci", dummyRequestHandler);		
		
		classMap.put("zip",Zip.class);
		classMap.put("quick_scan",QuickScanTask.class);		
		classMap.put("para_split",ParaSplit.class);
		classMap.put("unzip",Unzip.class);
		classMap.put("set_last_mod",Ulm.class);
		classMap.put("spirit",CoopSpirit.class);
		
		(new CoopSpirit()).staticTaskdef(
				dummyElement, "spirit", dummyRequestHandler);
		
		classMap.put("conn",CoopConnection.class);
		classMap.put("crypto",CoopCrypto.class);		
		classMap.put("print",CoopPrint.class);
		classMap.put("scan",Scan.class);
		classMap.put("xslt",Xslt.class);
		classMap.put("excel_table",Excel_Table.class);
		classMap.put("excel_form",Excel_Form.class);
		classMap.put("excel_sheets",Excel_Sheets.class);
		classMap.put("excel_matrix",Excel_Matrix.class);
		classMap.put("xml_update",Xml_Update.class);
		classMap.put("excel_update",Excel_Update.class);
		classMap.put("create_file",Create_File.class);
		classMap.put("javascript",Javascript.class);
		// classMap.put("remove_dtd",Remove_Dtd.class);		
		classMap.put("mkdir",Mkdir.class);
		classMap.put("copy",Copy.class);
		classMap.put("delete",Delete.class);
		classMap.put("split",Split.class);
		classMap.put("unique",UniqueCoop.class);
		classMap.put("sysprint",Sysprint.class);

		classMap.put("exe",Exe.class); /*[echnaton-really-exe]*/
		classMap.put("buffer",Buffer.class);
		classMap.put("stack",Stack.class);
		classMap.put("parallel",Parallel.class);
		classMap.put("secret",Secret.class);
		classMap.put("wait",Wait.class);
		classMap.put("xquery",Xquery.class);
		classMap.put("convert",Convert.class);
		classMap.put("unlock",Dummy.class);
		classMap.put("scope",Dummy.class);
		classMap.put("coop",Dummy.class);
		classMap.put("move",Dummy.class);
		classMap.put("loop_until",Dummy.class);
		classMap.put("begin2",Dummy.class);
		classMap.put("begin3",Dummy.class);
		classMap.put("end2",Dummy.class);
		classMap.put("end3",Dummy.class);
		classMap.put("return",Dummy.class);
		classMap.put("quit",Dummy.class);
		classMap.put("systempost",Systempost.class);
		classMap.put("key_source_report",KeySourceReport.class);
		classMap.put("key_target_report",KeyTargetReport.class);
		classMap.put("process",Prozess.class);
		//CryptoBase cb = new CryptoBase("karla");
		classMap.put("key_source_report",KeySourceReport.class);
		classMap.put("key_target_report",KeyTargetReport.class);
		classMap.put("wdriver",WebDriverTasks.class);
		classMap.put("welement",WebElementTasks.class);
		classMap.put("wtabulator",WebTabulator.class);
		classMap.put("pdf_to_text",PdfToText.class);
		classMap.put("filediff",FileDiff.class);
		classMap.put("visualcompare",VisualCompare.class);
		classMap.put("aggregate_scan",AggregateScan.class);
		classMap.put("shuffler",Shuffler.class);
		classMap.put("integriere",Integriere.class);		
		
		(new CoopLoop_Regex()).staticTaskdef(
				dummyElement, "?1?", dummyRequestHandler);
		(new CoopLoop_While()).staticTaskdef(
				dummyElement, "?2?", dummyRequestHandler);
		(new CoopLoop_Xml()).staticTaskdef(
				dummyElement, "?3?", dummyRequestHandler);
		
		(new CoopPrint()).staticTaskdef(
				dummyElement, "?5?", dummyRequestHandler);	
		
		InterLoopRegex.staticStartup();
		InterLoopSelect.staticStartup();
		InterLoopWhile.staticStartup();
		InterLoopXml.staticStartup();
		
		if ( report ) {
			Sp.p("");
			Sp.p("-) Registered BUILT-IN TASKS:");
			SortedSet<String> keys = new TreeSet<String>(classMap.keySet());
			for (String key : keys) { 
				System.out.print(key);
			}
			Sp.p("");
		}		

	}
	public PlugTryTask ( 
				Element _step, 
				Interpreter _interpreter,
				DbJob _job,
				HashMap<String, PlugTaskdef> _taskdefMap ) throws Exception {
		
		step = _step;
		interpreter = _interpreter;
		job = _job;
		taskdefMap = _taskdefMap;

	}
	
	public PlugTryTaskResult run(Interpreter interpreter) throws Exception {
		LogConnector elc = new LogConnector("");
		boolean found = false;
		String stepname = step.getName();
		
		ITask task = null;

		try {		
			
			Class<?> regClass = (Class<?>) classMap.get(stepname);
			if ( regClass != null ) {
				// found it!!
				Constructor<?> cons = regClass.getConstructor();
				task = (ITask)cons.newInstance();
			} else if ( stepname.equals("loop_while")) {
				CoopLoop_While lw = new CoopLoop_While();
				if ( step.getAttribute("act")!= null && step.getAttributeValue("act").equals("open")) {
					LogConnectorLogic lcc = new LogConnectorLogic(interpreter.getNextLogicCounter()+".LoopWhileCoop");
					lw.setLogConnectorLogic(lcc);
				}
				lw.setInterpreter(interpreter, interpreter);
				task = (ITask)lw;	
			 } else if ( stepname.equals("loop_xml") ) {
				CoopLoop_Xml lx = new CoopLoop_Xml();
				if ( step.getAttribute("act")!= null && step.getAttributeValue("act").equals("open")) {
					LogConnectorLogic lcc = new LogConnectorLogic(interpreter.getNextLogicCounter()+".LoopWhileCoop");
					lx.setLogConnectorLogic(lcc);					
				}
				task = (ITask)lx;	
			 } else if ( stepname.equals("loop_regex")) {
				CoopLoop_Regex lr = new CoopLoop_Regex();
				if ( step.getAttribute("act")!= null && step.getAttributeValue("act").equals("open")) {
					LogConnectorLogic lcc = new LogConnectorLogic(interpreter.getNextLogicCounter()+".LoopWhileCoop");
					lr.setLogConnectorLogic(lcc);
				} 
				task = (ITask)lr;	
			 } else if ( stepname.equals("ant") && interpreter.hasPrivAnt() && !interpreter.getSitzung().isOnline() /*[echnaton-really-ant]*/ ) {			
					try {
						//System.out.println("Going into Ant !!!");
						task = (ITask)CoreBuilder.taskdef_ant.getObject();
						// elc.warn("--ant-- sucessfull");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Exception in Running Ant: "+e.getLocalizedMessage());
						elc.warn("Does not find ant !!");
						elc.warn(CoreBuilder.taskdef_ant.toString());
						Sp.pe(e);
						throw e;
					}					
			} else if ( stepname.equals("ant") ) {
				throw new Exception("Having ants in the shoes. wow.");
			} else {
				// search custom
				PlugTaskdef oTaskdef = (PlugTaskdef)taskdefMap.get(step.getName());
				if ( oTaskdef != null ) {
					Object ox = oTaskdef.getObject();
					if ( 	oTaskdef.isLoop && 
							step.getAttribute("act")!= null && 
							step.getAttributeValue("act").equals("open") ) {
						ITaskLogic iloop = (ITaskLogic)ox;
						
						LogConnectorLogic lcc = new LogConnectorLogic(interpreter.getNextLogicCounter()+"."+step.getName());
						iloop.setLogConnectorLogic(lcc);
						
					}
						
					task = (ITask)ox;
				} 
			}
						 	
			Parameters retvals = null;
			if ( task != null ) {
				
				retvals = task.execute ( step, interpreter) ;
				found = true;	
			} else
				throw new LangException(210,"Task '"+stepname+"' was not found");
	
			return ( new PlugTryTaskResult(found,retvals) );
			
		} catch (Exception e) {
			throw e;

		} 
	}
	

}