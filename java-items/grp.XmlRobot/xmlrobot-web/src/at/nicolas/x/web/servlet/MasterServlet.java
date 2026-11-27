package at.nicolas.x.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.web.EeProject;
import at.nicolas.x.web.EeSystem;
import at.nicolas.x.web.listener.MoralesTrapProjectHttpSession;

public class MasterServlet extends HttpServlet {

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
		Sp.ph("Stazione A");
	   	EeProject eeProject = (EeProject)request.getSession().getAttribute("eeProject");
	   	Sp.ph("Stazione B");
        if ( eeProject == null ) {
        	// create eeProject
        	Sp.ph("Stazione C . eeProject is null");
    		EeSystem eeSystem = (EeSystem)request.getSession().getServletContext().getAttribute("eeSystem");
    		
    		if ( eeSystem == null ) {
    			Sp.ph("Stazione Z . eeSystem is null");
    			throw new IOException(request.getSession().getId()+" . Didnt find eeSystem in Session");
    		}
    		
    		eeProject = MoralesTrapProjectHttpSession.buildNewProject(
    				request.getServletContext(), 
    				request.getSession());
    		
        	
        } 
        Sp.p("Stazione E");
	   	eeProject.doTheNextThing(request, response);
	   	Sp.p("Stazione F");
        /*
	   	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello Wor2ld!</title>");
        out.println("</head>");
        
        out.println("<form action=\"DoCalculation\">");			
        out.println(" Please bla bla a color <br>");
        out.println("<input type=\"text\" name=\"color\" size=\"20px\">");
        out.println("<input type=\"submit\" value=\"submit\">");						
        out.println("</form>");	
        out.println("<body>");
        */
        /*
        if ( bowebapp == null )
        	out.println("<h1>Hello Worl2d! IT IS NULL</h1>");
        else
            out.println("<h1>Hello Worl2d!"+bowebapp.getTomcatTempDir()+"</h1>");
        */
        //HttpSession hs = request.getSession();
        // BoProject boproject= (BoProject)hs.getAttribute("boproject");
        // out.println("<p>"+boproject.getXrapp() +", "+boproject.getTomcatTempDir()+"</p>");
        //out.println("</body>");
        //out.println("</html>");
        
    }

}
