package at.nicolas.x.web.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.web.EeProject;

public class ServletWishUploadPerformed extends HttpServlet {

   /**
	 * 
	 */

public void xdoGet(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {

	   	EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
	   	String ticketid = request.getParameter("ticket");
	   	eeProject.doWishUploadPerformed(request, response,ticketid);


    }
public void doPost(
		   HttpServletRequest request, 
		   HttpServletResponse response)
 throws IOException, ServletException
 {
	
   	EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
   	Sp.p("WishUploadPerformed"); 
   	
	HttpServletRequest req = request;
    Enumeration<String> parameterNames = req.getParameterNames();
    Map<String,String[]> x = req.getParameterMap();
    Sp.p("WishUploadPerformed, anzahl der parameter=" + x.size());
    String htmlTicketId = null;
    while (parameterNames.hasMoreElements()) {

        String paramName = parameterNames.nextElement();

        Sp.p("Parameter:" + paramName);

        
        String[] paramValues = req.getParameterValues(paramName);

        for (int i = 0; i < paramValues.length; i++) {
            String paramValue = paramValues[i];     
            htmlTicketId = paramValues[i];
            Sp.p("WishUploadPerformed: Parameter[" +i +"] = '"+paramValue+"'");
        }
    }



	

	   	eeProject.doWishUploadPerformed(request, response,htmlTicketId);


 }

}
