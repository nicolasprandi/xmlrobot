package at.nicolas.x.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.web.EeProject;

public class ServletProjectDbCheckin extends HttpServlet {

   /**
	 * 
	 */

public void doGet(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {
	   	EeProject eeProject = null;
	  
			eeProject = (EeProject)request.getSession().getAttribute("eeProject");
			eeProject.doProjectDbCheckin(request, response);
    
    }

}
