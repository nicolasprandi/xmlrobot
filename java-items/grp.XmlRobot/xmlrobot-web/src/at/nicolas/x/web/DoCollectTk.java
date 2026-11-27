package at.nicolas.x.web;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoCollectTk extends HttpServlet {

   /**
	 * 
	 */

public void doGet(
		   HttpServletRequest request, 
		   HttpServletResponse response)
    throws IOException, ServletException
    {
		ServletContext context = request.getSession().getServletContext();
	   	EeProject eeProject = (EeProject)context.getAttribute("eeProject");
	   	//eeProject.continueAfterTk(request, response);
	   	/*
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Do Calculation</title>");
        out.println("</head>");
        
        out.println("<form action=\"performCalculation2\">");			
        out.println(" Please enter a color <br>");
        out.println("<input type=\"text\" name=\"color\" size=\"20px\">");
        out.println("<input type=\"submit\" value=\"submit\">");						
        out.println("</form>");	
        out.println("<body>");
        if ( bowebapp == null )
        	out.println("<h1>Hello Worl2d! IT IS NULL</h1>");
        else
            out.println("<h1>Do Calculation "+bowebapp.getTomcatTempDir()+"</h1>");
        
        HttpSession hs = request.getSession();
        BoProject boproject= (BoProject)hs.getAttribute("boproject");
        try {
        	//boproject.runJob("run");
			//boproject.runJob("ant");
			//boproject.runJob("poi");
        	boproject.runJob("ram1");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        out.println("<p>"+boproject.getXrapp() +", "+boproject.getTomcatTempDir()+"</p>");
        
        try {
			out.println("<p>"+Commons.readTextfileFormatLfURI(boproject.getProjectDir()+"/see.txt", "utf-8", "/n")+"</p>");
		} catch (Exception e) {
			out.println("<p> problem in reading content of: "+boproject.getProjectDir()+"/see.txt"+"</p>");

			e.printStackTrace();
		}
        out.println("</body>");
        out.println("</html>");
        */
        
    }

}
