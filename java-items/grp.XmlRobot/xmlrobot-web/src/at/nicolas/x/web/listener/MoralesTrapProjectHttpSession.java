package at.nicolas.x.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.web.EeProject;
import at.nicolas.x.web.EeSystem;
import at.nicolas.x.web.OnlineSitzung;
import at.nicolas.x.web.SurroundingHelper;
import at.nicolas.x.web.TrapProjectHttpSession;

public class MoralesTrapProjectHttpSession 
extends TrapProjectHttpSession 
implements HttpSessionListener {
	private static String me="Morales.";

	public void sessionDestroyed( HttpSessionEvent evt ) {
		/*[echnaton-customer]*/
		System.out.println(me+"sessionDestroyed("+evt.getSession().getId()+")");
		Sp.ph(me+"sessionDestroyed - start");
		EeSystem eeSystem = (EeSystem)evt.getSession().getServletContext().getAttribute("eeSystem");
		if ( eeSystem == null )
			Sp.ph(me+".sessionDestroyed() : Wir habn hier keines eeSystem gefunden");
		EeProject eeProject = (EeProject)evt.getSession().getAttribute("eeProject");
		String pid = " session="+evt.getSession().getId();
		if ( eeProject == null ) {
			Sp.pShutDownProblem("sessionDestroyed Wir habn hier keine eeProject gefunden"+pid);
		} else {
			pid = pid + "project="+eeProject.getProjectId();
			
			try {
				Sp.pShutDown("Starting to shutdown "+pid);
				eeProject.shutDown();
				Sp.pShutDown("sessionDestroyed Did shut down eeProject with success."+pid);
			} catch (FileSystemException e1) {
				Sp.pShutDownProblem("Problem in shutting down a eePrject "+pid+" "+e1.getMessage());
			}
			try {
				if ( eeSystem != null )
					eeSystem.unRegisterNewProjects(eeProject);
				evt.getSession().removeAttribute("eeProject");
				
				Sp.pShutDown(me+"Did unregester project-session with success "+pid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pShutDownProblem("Having a problem with unregistering a session. " 
				+ pid 
						+ e.getMessage());
			
			}
			
		}
		Sp.ph(me+"sessionDestroyed - end");
		
	}

	
	public static EeProject buildNewProject(ServletContext servletContext, HttpSession httpSession) {
		
		String tips="Session "+httpSession.getId() +" ";
		String tiph ="M ";
		//Sp.pAccessSession(tiph+tips+"08 Started: ");

		// TODO Auto-generated method stub
		String myapp = servletContext.getInitParameter("app");
		Sp.pAccessSession(tiph+tips+"app= "+myapp);
		// username
		HttpSession session = httpSession;		
		
		Surrounding surrounding = SurroundingHelper.getSurrounding(
				servletContext,"dergast");

		
		
	
		EeSystem eeSystem = (EeSystem)servletContext.getAttribute("eeSystem");
		EeProject eeProject = null;
		
		if ( eeSystem == null )			
			Sp.pSoftwareProblem(tiph+tips+"99 the eeSystem is sorrily null ");
		else {
			boolean accepts = false;
			try {
				 accepts = eeSystem.mayAcceptNewProjects(myapp);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("--- EXCEPTION --- the acess to the app caused problems: "+myapp);
				e1.printStackTrace();
				throw e1;
			}
			
			if ( !accepts) {
				Sp.pAlarmx(tiph+tips+" the eeSystem does not accept new projects of app '"+myapp+"'");
			} else {
				// accepting new projects
				try {
					/*[echnaton-timer]*/
					/*[echnaton-customer]*/					
					String newProId = myapp+"-"+getNextProjectIdNbr();
					tips = tips + "proid="+newProId;
					Sp.pl(tiph+tips+" creating a new project ");
					
					DbApp theapp = null;
					try {
						theapp = eeSystem.getDbApp(myapp);
						Sp.ph(tiph+tips+" did request app with success ");
					} catch (Exception e) {
						Sp.ph(tiph+tips+" did FAIL to request app "+myapp);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					eeProject = new EeProject(
							eeSystem,
							myapp, //  "Test-Sanchez",
							eeSystem.getDbApp(myapp),
							newProId,
							httpSession,
							surrounding,
							httpSession.getId()
							);
					
					
					Sp.pl(tiph+tips+" did create project with sussess ");
					
					/*String tomcatBase = System.getProperty("catalina.base");
					System.out.println("tomcatBase="+tomcatBase);
					String webapp = String.format("%s/webapps/xp-rezeptblock-admin", tomcatBase); */
					String webapp =  servletContext.getRealPath("/");
					//System.out.println("***** webapp    ="+webapp);
					webapp = CommonsVfs.makeyBeautifulUri(webapp);
					//System.out.println("***** webapp2   ="+webapp);myapp
					OnlineSitzung os = new OnlineSitzung(
							surrounding,
							eeProject.getProjectDir(),
							eeProject.getProjectId(),
							eeSystem.getDbApp(myapp).getDir(),
							myapp,
							eeSystem.xmlrobotSystemDir,
							eeSystem.getTomcatTempDirForProject(httpSession.getId()),
							"ram:///ramses/"+httpSession.getId(),
							webapp,
							eeSystem,
							surrounding.getPostDirForSession(httpSession.getId()),
							eeProject,
							surrounding.getLibro2Uri()+"/"+myapp);
					eeProject.setSitzung(os);
					//System.out.println("DIRRRRRR: "+os.getDatabaseLinkUriDir());
					Sp.pl(tiph+tips+" did create sitzung with success ");
					// eeProject.setHeaderAndFooter(header_and_footer);
					httpSession.setAttribute("eeProject", eeProject);	
					Sp.pl(tiph+tips+" did set j2ee object ");
					eeSystem.registerNewProjects(eeProject);
					Sp.ph(tiph+tips+" did register project in system ");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Sp.ph("a problem in setting up "+e.getLocalizedMessage());
					e.printStackTrace();
				}	
				Sp.pl("passed control 5");				
			}
			
		}	
		return eeProject;
		
	}
	@Override
	public void sessionCreated(HttpSessionEvent evt) {
	if ( evt != null ) {

		buildNewProject(evt.getSession().getServletContext(), evt.getSession());
		/*
		Enumeration attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
		String name = (String)attributeNames.nextElement();
		String value = (String)session.getAttribute(name);
		System.out.println(name + "=" + value);
		}
		String ua = null;
		if (session.getAttribute("username") != null )
			ua = (String) session.getAttribute("username");
		try {
			System.out.println("username: "+(String) session.getAttribute("username"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("username: "+ua);
		*/
		
	
	

	} else {
			Sp.ph("dieser evt is icht da, aha");
	}
		
}



}
