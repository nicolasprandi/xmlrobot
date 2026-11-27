package at.nicolas.x.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.web.EeProject;

public class ServletTkOkEdit extends HttpServlet {

   /**
	 * 
	 */

public void doPost(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {
		//System.out.println("-- SERVLET -- ServletTkOkEdit.doGet -- " + Commons.getTsMs() +
		//		" request.getCharacterEncoding() = "+request.getCharacterEncoding() ) ;
		request.setCharacterEncoding("UTF-8");
		//System.out.println("new request.getCharacterEncoding() = "+request.getCharacterEncoding() ) ;		
		// HttpSession hs = request.getSession();
		// ServletContext context = request.getSession().getServletContext();
	   	EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
	   	// String link = request.getParameter("link");
	   	// eeProject.
        // eeProject.doTheNextThing(request, response);
        eeProject.doEditOk(request, response);

        
    }

}
