package at.nicolas.x.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.nicolas.x.WebVfs.ReqRes;
import at.nicolas.x.WebVfs.XrRqRpEx;
import at.nicolas.x.WebVfs.XrRqRpTi;
import at.nicolas.x.WebVfs.XrRqRpTk;

public class ReqResEe extends ReqRes {
	
	/*
	JobRunner jr;

	*/
	//JobRunner jr;
	HttpServletRequest request; 
	HttpServletResponse response;
	// ITerminator terminator;
	
	public EeProject eeProject;
	public ReqResEe(HttpServletRequest request, 
			   HttpServletResponse response,
			   // ITerminator terminator,
			   // JobRunner jr,
			   EeProject eeProject) {
		this.request = request;
		this.response = response;
		// this.terminator = terminator;
		// this.jr = jr;
		this.eeProject = eeProject;
	}
	
	public void changeContext(
			   HttpServletRequest request, 
			   HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}	
	
	public void treatTkResponse(XrRqRpTk answerTk) {
		// wird nach vom jobrunner aufgerufen
		// tkSequence++;
		eeProject.treatTkResponse(answerTk, request, response);

	}
	/*
	void sendMap ( 
			PrintWriter out, 
			TkTicketManager ticketManager) throws Exception {
		// ArrayList<Element> l = e.getChildren();
		out.println("<h1>"+ticketManager.top_label+"</h1>");
		out.println("<form action=\"tkOk\">");
		Iterator<TkTicket> iterator = ticketManager.ticketList.iterator();
		while ( iterator.hasNext() ) {
			
			TkTicket ticket = (TkTicket)iterator.next();
			ticket.printHtmlDiv(out);
		
		}
        out.println("<input type=\"submit\" value=\"submit\">");
        out.println("<a href=\"tk_cancel\">Cancel</a>");
        out.println("</form>");		
	}
	*/
	
	
	public void treatException(XrRqRpEx answerEx) {
		eeProject.treatException(answerEx, request, response);

	}	
	
	public void treatTiResponse(XrRqRpTi answerTi) {
		// wird nach vom jobrunner aufgerufen
		
		
		eeProject.treatTiResponse(answerTi,request,response);
		
	}	
		
	/*
	private synchronized void doContinueAfterTk() {

		jr.continueAfterTk();
		
	}
	*/
	

	/*
	private int tkSequence = 0;
	private void talk(String x ) {
		Sp.p("talk: "+x);
	}
	private void say(String x ) {
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");	
		Sp.p(x);
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");
		Sp.p("***********************************");		
	}	
	*/

}
