package at.nicolas.x.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.web.EeSystem;
import at.nicolas.x.web.MasterSitzung;

public class TrapSystemServletContext implements ServletContextListener {
	private static String me="TrapSystemServletContext.";
	private static EeSystem eeSystemSingleton = null;
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		// TODO Auto-generated method stub
		Sp.p(me+"contextDestroyed - start");

		if ( eeSystemSingleton == null ) {
			// no shutdown needed
			System.out.println(me+"Problem: context already destroyed");
		} else {
			// Doing system shutdown
			try {
				System.out.println(me+"Listing registered projects");
				eeSystemSingleton.listRegisteredProjects();
				System.out.println(me+"Starting to shut down all registered projects");
				eeSystemSingleton.shutdownRegisteredProjects();
				System.out.println(me+"Starting to shut down system ...");
				CoreBuilder.shutSystemDown();
				ServletContext ctx = evt.getServletContext();
				ctx.removeAttribute("eeSystem");
				eeSystemSingleton = null;
				System.out.println(me+"Did shut down with success!");
			} catch (Exception e) {
				System.out.println(me+"Having a problem with system-shutdown!");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		Sp.p(me+"contextDestroyed - end");
	}


	@Override
	public void contextInitialized(ServletContextEvent evt) {
		// TODO Auto-generated method stub
		Sp.p("TrapSystemServletContext.contextInitialized");

		if ( evt == null )
			Sp.p(me+"-- der evt ist null");
		ServletContext ctx = evt.getServletContext();
		
		// EeSystem eeSystemMaybe = (EeSystem)evt.getServletContext().getAttribute("eeSystem");
		if ( eeSystemSingleton == null ) {
			
			Sp.p("******** EIN NEuES SYSTEM ******************************************"); 
			Sp.p("** sys: Starting up ******************************");
			Sp.p("** getRealPath="+ctx.getRealPath("/"));
			Sp.p("**************************************************"); 
			
			MasterSitzung masterSitzung = new MasterSitzung();
			
			EeSystem eeSystem;
			
			try {
				/*[echnaton-timer]*/
				
				eeSystem = new EeSystem(
						getSystemTempDir(),evt,masterSitzung);
				Sp.p("** SETTING eeSYSTEM                 dir="+
						eeSystem.tomcatTempDir+", "+eeSystem.takeofffilename);
				ctx.setAttribute("eeSystem", eeSystem);	
				eeSystemSingleton = eeSystem;
			} catch (Exception e) {
				Sp.p("** eeSystem: exception on startup, "+e.getLocalizedMessage());
				e.printStackTrace();
			}
		} else {
			Sp.p("******** EIN BESTEHENDES SYSTEM ******************************************"); 

			// Es gibt schon ein System: eeSystemSingleton
			try {
				/*[echnaton-timer]*/
				Sp.p("**do samme 1************************************************");
				eeSystemSingleton.appendApps(evt);
				
				Sp.p("**do samme 2************************************************");
				ctx.setAttribute("eeSystem", eeSystemSingleton);
			} catch (Exception e) {
				Sp.p("Problem mit dem eeSystemSigelton.appnedApps"+e.getLocalizedMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Sp.p("**************************************************"); 
			//Sp.p("** SYSTEM EXISTS ALREADY *************************");
			//Sp.p("** getRealPath="+ctx.getRealPath("/"));			
			//Sp.p("**************************************************"); 			
		}
		// Sp.p("*********kukuk*****************************************");		
	}

	public static String getSystemTempDir() {
	    String tempDir = System.getProperty("java.io.tmpdir");
	    Sp.p("The systemtempdir is "+tempDir);
	    if ( !tempDir.endsWith("/") && !tempDir.endsWith("\\") )
	    	tempDir = tempDir+"/";
	    tempDir = tempDir+"meXR/";
	    return tempDir;
	}
}
