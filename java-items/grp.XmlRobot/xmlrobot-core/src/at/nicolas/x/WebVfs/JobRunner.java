package at.nicolas.x.WebVfs;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.PlugTaskdef;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_GuiSizer;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class JobRunner {

	// boolean isWaitingForTkAnswersx = false;
	ReqRes reqRes;
	public Interpreter interpreter ;
	public JobRunner() {

	}
	
	public void set (Interpreter _interpreter,
			   ReqRes _reqRes ) {
		interpreter = _interpreter;
		reqRes = _reqRes;
	}
	
	public void continueAfterTk() {
		/*
		Sp.p("Going to resume after tk from Interpreter: "+interpreter.getDescriptionStringSmall());
		if ( interpreter.subInterprterToFinish != null )
			Sp.p("... which is in subInterpreter: "+interpreter.subInterprterToFinish.getDescriptionStringSmall());
		*/
		XrRqRp x = interpreter.continueTheRunAfterResponse();
		interpretInterpreterResult(
				x,reqRes);		
	}	
	public void continueAfterTkCancel() {
		// Sp.p("Cancel Going to resume after tk "+interpreter.getDescriptionStringSmall());
		XrRqRp x = interpreter.continueTheRunAfterTkCancel();
		interpretInterpreterResult(
				x,reqRes);		
	}	
	private void interpretInterpreterResult(
				XrRqRp rsp,
				ReqRes reqRes) {

		if ( rsp instanceof XrRqRpEx ) {
			reqRes.treatException((XrRqRpEx)rsp);			
		} else if ( rsp instanceof XrRqRpTk ) {
			try {
				reqRes.treatTkResponse((XrRqRpTk)rsp);
			} catch (Exception e) {
				Sp.pe(e);
			}
		} else if ( rsp instanceof XrRqRpTi ) {
			reqRes.treatTiResponse((XrRqRpTi)rsp);	
		} else {
			Sp.p("11: UNBEKANNT");
		}
	}
	
	public ReqRes getReqRes() {
		return reqRes;
	}
	public void runNewJob (Interpreter _interpreter,
						   ReqRes _reqRes ) {
		interpreter = _interpreter;
		reqRes = _reqRes;
		interpreter.run();	
		
		if ( _reqRes != null ) {
			interpretInterpreterResult(
					interpreter.getRqRpAfterRunning(),reqRes);			
		} else {
			// we can try
			try {
				interpretInterpreterResult(
						interpreter.getRqRpAfterRunning(),reqRes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("we had here a problem with putting the result somewhere but its ok ...");
			}			
		}
		

		
	}
	
	public static Interpreter createInterpreter(
			String sjobid ,
			boolean sisServiceCall,
			String sappId,
			String sproId,
			String sproDir,
			Element flex,
			Element flexElementEnd,
			int ssilence,
			ArrayList<Element> xxconfigList,
			int depth,
			boolean interpreterShouldGiveBackTk,
			boolean interpreterShouldIgnoreClassloading,
			ITerminator terminator,
			boolean includeDefaultConfig,
			IGuiController _guiController,
			ISitzung _isitzung,
			boolean interpreterShouldIgnoreTk
			
			) throws Exception {
		DbJob jobx=null;

			if ( sproId == null && sproDir !=null ) {
				throw new Exception("createInterpreter: sproDir="+sproDir+", but no proId!!");
			}
			jobx = null;
			if ( sisServiceCall ) 
				jobx = CoreBuilder.db.getScJm().selectJob(sjobid);
			else
				jobx = CoreBuilder.db.getAppById(sappId).getJm().selectJob(sjobid);
			if ( jobx == null )
				throw new Exception("The job '"+sjobid+"' could not be selected.");


		Interpreter myinterpreter = new Interpreter ( 
				jobx,
				sappId, 
				sproId,
				sisServiceCall,
				ssilence,
				depth,
				flex,
				sproDir,
				_guiController,
				_isitzung);
		
		myinterpreter.setInterpreterShouldGiveBackTk(interpreterShouldGiveBackTk);
		myinterpreter.setIgnoreClassloading(interpreterShouldIgnoreClassloading);
		myinterpreter.setInterpreterShouldIgnoreTk(interpreterShouldIgnoreTk);
		
		if ( flexElementEnd != null ) {
			myinterpreter.setFlexEndElement(flexElementEnd);
		}
			   	
	   	if ( xxconfigList != null && xxconfigList.size() > 0  ) {
	   		myinterpreter.addConfigList(xxconfigList);
	   	} 
	   	
	   	if ( includeDefaultConfig){
	   	
	   		ArrayList<Element> sconfigList=null;
	   		try {
	   			//sconfigList = CoreBuilder.db.getExecutionProlog(sappId, sproId,true,sisServiceCall);
	   			sconfigList = CoreBuilder.db.getExecutionPrologAnonym(sappId, sproId, sproDir, true, sisServiceCall,_isitzung);
	   		} catch (Exception e) {
	   			
	   		}
	   	   	myinterpreter.addConfigList(sconfigList);  		
	   	   
	   	}
	   	myinterpreter.setTerminatorBeforeRun(terminator);
	   	// terminator.write(mycalldepth+"."+"1) Starting from subrunner2: "+myinterpreter.getDescriptionString());
	   	
	   	
		return myinterpreter;

	}		
	
	public static void createCorebuilder(
			String filenameTakeoff,
			String dirnameSystemp,
			boolean classloaderForJEE,
			String antJarFilename,
			String poiJarFilename,
			ISitzung systemSitzung,
			boolean online) throws Exception {
		if ( filenameTakeoff == null || dirnameSystemp==null)
			throw new Exception("createCorebuilder: missing filanemTakeoff or dirnameSystemp");
		CommStartMsg sm = new CommStartMsg();
		sm.out.println("======================= FIRST TRY ================== ");
		CoreBuilder.allStartupActions(sm,online);
		
		ClassLoader cl = null;
		String standardHome = null;
		if ( classloaderForJEE ) {
			cl = filenameTakeoff.getClass().getClassLoader();
		} else {
			cl = LocalFilesystem.getClassLoaderFrom2Filenames(antJarFilename,poiJarFilename);
			standardHome = LocalFilesystem.getHomeDirXrFormat();
		}

		PlugTaskdef taskdef_ant = PlugTaskdef.newInstanceTrustedByClassloader("ant", "at.nicolas.x.tasks.Ant" , cl , null);
		if ( taskdef_ant == null)
			Sp.p("taskdef_ant == null");
		else
			Sp.p("taskdef_ant != null");
		
		//
		InterfaceParas ip =InterfaceParas.get(
				dirnameSystemp, 
				cl);
		if ( ip == null )
			sm.out.println("JEE/Interface.startup() : received null as InterfaceParas");
		else {
			sm.out.println("JEE/Interface.startup() : ip.overwriteSystemp    ="+ip.overwriteSystemp);
			sm.out.println("                          ip.overwriteClassloader="+ip.overwriteClassloader.toString());
		}
			
		 
		
		//CoreBuilder.allStartupActions(sm);
		CoreBuilder.build(filenameTakeoff,standardHome,sm, ip,systemSitzung);		
		
		Ti_TerminalLink.setup(online);
		Tc_AppConsole.guiSizer = new Tc_GuiSizer();
		Tc_AppConsole.guiSizer.setSize(1);
		
	}

}
