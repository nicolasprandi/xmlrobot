package at.nicolas.x.int_specs;

import java.io.IOException;

import org.jdom2.JDOMException;

import at.nicolas.x.int_call.AppJobexecutionUsage;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LocalFilesystem;
import at.nicolas.x.pub.ISitzung;

public class Interface {

	public static void startup(
			String takeoff, 
			InterfaceParas ip,
			ISitzung systemSitzung,
			boolean online) throws Exception {
		// TODO Auto-generated method stub
		CommStartMsg sm = new CommStartMsg(System.out);
		if ( ip == null )
			sm.out.println("JEE/Interface.startup() : received null as InterfaceParas");
		else {
			sm.out.println("JEE/Interface.startup() : ip.overwriteSystemp    ="+ip.overwriteSystemp);
			sm.out.println("                          ip.overwriteClassloader="+ip.overwriteClassloader.toString());
		}
			
		
		String standardHome = LocalFilesystem.getHomeDirXrFormat();
		
		CoreBuilder.allStartupActions(sm,online);
		CoreBuilder.build(takeoff,standardHome,sm, ip,systemSitzung);
	}
	
	public static void shutdown() {
		// TODO Auto-generated method stub
	}	
	
	public static void runJob(String job, String app, String pro, String proDir ) throws JDOMException, IOException, Exception {
		AppJobexecutionUsage x = new AppJobexecutionUsage(
				app,
				pro,
				proDir,
				job,
				null,
				false,
				Interpreter.SILENCE_VERY,
				new CoreDummyTerminator(), 
				0,
				null); 	
		x.setInterpreterShouldGiveBackTk(true); // falsch aber braucht keiner
		x.run();	
	
	}		

}
