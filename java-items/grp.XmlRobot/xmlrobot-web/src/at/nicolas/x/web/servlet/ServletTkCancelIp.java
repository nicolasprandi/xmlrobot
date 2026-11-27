package at.nicolas.x.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.web.EeProject;

public class ServletTkCancelIp extends HttpServlet {

   /**
	 * 
	 */

public void doGet(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {
		// HttpSession hs = request.getSession();
		// ServletContext context = request.getSession().getServletContext();
	   	EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
	   	// String link = request.getParameter("link");
	   	// eeProject.
        // eeProject.doTheNextThing(request, response);
        eeProject.doTkCancelIp(request, response);

        
    }

}
