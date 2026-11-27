package at.nicolas.x.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.web.EeProject;

public class ServletLogin extends HttpServlet {

   /**
	 * 
	 */

public void doPost(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {
		Sp.p("bin im serlet login");
		// HttpSession hs = request.getSession();
		// ServletContext context = request.getSession().getServletContext();
	   	try {
			EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
			Sp.p("Servlet Login eeProjet="+eeProject);
			// String link = request.getParameter("link");
			// eeProject.
			// eeProject.doTheNextThing(request, response);
			eeProject.doReactLogin(request, response);
		} catch (Exception e) {
			Sp.p("Da hats was bei do get: "+e.getMessage());
			e.printStackTrace();
			
		}

        
    }

}
