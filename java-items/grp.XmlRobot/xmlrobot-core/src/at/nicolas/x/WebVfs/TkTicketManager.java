package at.nicolas.x.WebVfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.term_tk.Tester;
import at.nicolas.x.term_tk.Tk_TerminalKeyboardSequ;

public class TkTicketManager {
	
	 HashMap<String, TkTicket> tickets = new HashMap<String, TkTicket>();
	ArrayList<TkTicket> ticketList = new ArrayList<TkTicket> ();

	public boolean flag_sum = false;
	public boolean flag_sum_only = false;
	public boolean do_first_round = true;
	public boolean has_accept = false;
	private Element trans;
	public String top_label = null;
	public String top_tip = null;
	public String top_flag = null;
	//public ArrayList<TkTickets> getListOfTickets() {
	
	public Element tkOriginal;
	public ITiHandler tiHandler;
	public Interpreter interpreter;
	public LogConnector lc;
	private Element myOrig;
	public String tkid;

		
	private Element forPrint = null;
	public Element getTkElementForPrint() throws Exception {
		if ( forPrint == null )
			forPrint = 
				interpreter.translatePrimitive(myOrig, true, -1, false);
		return forPrint;
		
	}
	public TkValidationResult validate ( ) {
		TkValidationResult x = new TkValidationResult();
		
		Iterator<TkTicket> i = ticketList.iterator();
		while ( i.hasNext() ) {
			TkTicket ticket = (TkTicket)i.next();
			if ( !ticket.tktype.equals("msg")) {
				TkValidationError problem = ticket.validate();
				if ( problem != null ) {
					x.errors.add(problem);
				}			
			}
		}		
		return x;
	}
	public void reflectToMemory ( IInputRequestor request )  {
		//Sp.p("TicketManager is starting to reflect to memory");
		Iterator<TkTicket> i = ticketList.iterator();
		while ( i.hasNext() ) {
			TkTicket ticket = (TkTicket)i.next();
			if ( !ticket.tktype.equals("msg")) {
				try {
					String valueFromForm = request.getParameter(ticket.getTicketId());
					//Sp.p("TicketManager found ticket '"+ticket.ticketId+"' with value '"+
					// valueFromForm+"'");
					if ( valueFromForm != null || ticket instanceof TkTicketAccept)
						ticket.reflectToMemory(request);
				} catch (Exception e) {
					Sp.p("Ticket "+ticket.ticketId+" on reflect error:"+e.getMessage());
					Sp.pe(e);
				}				
			}
		}
	}


	public TkTicketManager(Element tkOriginal, 
			Interpreter interpreter,
			LogConnector lc,
			ITiHandler tiHandler) throws Exception {
		// TODO Auto-generated constructor stub
		this.interpreter = interpreter;
		this.tiHandler = tiHandler;
		this.tkOriginal = tkOriginal;
		this.lc = lc;
		
		
		if ( tkOriginal.getChildren().size() == 0 || (
				// tkOriginal.getChildren().size() == 1 &&
				tkOriginal.getAttribute("type") != null &&
				(tkOriginal.getAttributeValue("type").equals("sel") || tkOriginal.getAttributeValue("type").equals("accept"))
				)) {
			myOrig = new Element("tk");
			if ( tkOriginal.getAttribute("top_label") != null )
				myOrig.setAttribute("label", tkOriginal.getAttributeValue("top_label"));
			else {	
				if (tkOriginal.getAttribute("type")!=null &&  tkOriginal.getAttributeValue("type").equals("sel"))
					myOrig.setAttribute("label", tiHandler.getSitzung().lang().getEeTmQuestionNORMAL());
				else if (tkOriginal.getAttribute("label") != null )
					// myOrig.setAttribute("label", tkOriginal.getAttributeValue("label"));
					myOrig.setAttribute("label", tiHandler.getSitzung().lang().getEeTmQuestionNORMAL());
				else
					myOrig.setAttribute("label", tiHandler.getSitzung().lang().getEeTmQuestionNORMAL());
					
			}
			myOrig.addContent(tkOriginal.clone());			
		} else
			myOrig = tkOriginal.clone();
		
		if ( myOrig.getAttribute("label") == null)
			myOrig.setAttribute("label", tiHandler.getSitzung().lang().getEeTmInputDataNORMAL());	
		
	
		if ( myOrig.getAttribute("label")!=null)
			top_label = interpreter.translateString(myOrig.getAttributeValue("label"));
		if ( myOrig.getAttribute("tip")!=null)
			top_tip = interpreter.translateString(myOrig.getAttributeValue("tip"));
		if ( myOrig.getAttribute("flag")!=null) {
			top_flag = interpreter.translateString(myOrig.getAttributeValue("flag"));
			
			/*
			if (myOrig.getAttribute("tk_id") != null && tiHandler.getSurrounding().getTkMatcherSumOnly() != null ) {
				tiHandler.getSurrounding().getTkMatcherSumOnly().report();
			}*/
		
			if ( myOrig.getAttribute("tk_id") != null && tiHandler.getSurrounding().getTkMatcherSumOnly() != null && 
				 tiHandler.getSurrounding().getTkMatcherSumOnly().doesMatch(myOrig.getAttributeValue("tk_id"))) {
				flag_sum = true;
				flag_sum_only = true;
			} else if ( Commons.hasFlag(top_flag, "sum") || Commons.hasFlag(top_flag, "sum_only")) {
				flag_sum = true;
				if (Commons.hasFlag(top_flag, "sum_only"))
					flag_sum_only = true;				
			}		
			
		}
		do_first_round = true;
		if ( flag_sum_only )
			do_first_round = false;
		
		tkid="";
		if ( myOrig.getAttribute("tk_id") != null )
			tkid = myOrig.getAttributeValue("tk_id");

		
	}
	
	List<Element> setupChilds ;
	Iterator<Element> setupIterator ;
	int setupPos;
	boolean is_real_firsttimer = true;
	
	public void resetNewIteratorForSequence() {
		 tickets = new HashMap<String, TkTicket>();
		ticketList = new ArrayList<TkTicket> ();
		is_real_firsttimer = false;
	}
	public void startIterator() {
		setupChilds = myOrig.getChildren();
		setupIterator = setupChilds.iterator();	
		setupPos = -1;
		
	}
	
	public TkTicket getLastTicket() {
		//Sp.p("************* bim im getLast");
		//Sp.p("************* bim im getLast "+ticketList.size());
		if ( ticketList.size() > 0)
			return ticketList.get(ticketList.size()-1);
		else
			return null;
	}
	
	public TkTicket continueIterator() throws Exception {
		boolean stopnow = false;
		TkTicket stoppedTicket = null;
		setupPos++;
		while ( !stopnow && setupIterator.hasNext()) {
			
			Element thisIteratedElement = (Element)setupIterator.next();
			Element thisIteratedElementTranslated = interpreter.translatePrimitive(thisIteratedElement, false, -1, false);
			//System.out.println(" - e - "+ thisIteratedElement.toString());
			if ( Tester.testOrTestdynAttributeIs0(thisIteratedElementTranslated,interpreter)) {
				// das wird �berlesen
				//System.out.println("       test = 0");
			} else {
				//System.out.println("       test = 1");
				// dieses Element wird mal fix angehaengt
				TkTicket ticket = getTicketByElement(thisIteratedElementTranslated);
				if ( !ticket.isMsg ) 
					ticket.setInitialValue(); // das holt den wert aus dem speicher her

				ticketList.add(ticket);
				tickets.put(ticket.getTicketId(), ticket);
				
				boolean test_first = true;
				if ( ticket.tktype.equals("accept")){
					has_accept = true;
				} else if ( ticket.isMsg ) {
					if ( !is_real_firsttimer )
						test_first = false;
					else if ( Tester.testFirstOrTestfirstDynAttributeIs0(thisIteratedElementTranslated,interpreter))
						//if ( !Tk_TerminalKeyboardSequ.showMsg(setupChilds, setupPos, is_real_firsttimer,tiHandler))
						test_first = false;
				} else if (!is_real_firsttimer ) 
					test_first = true; // von einer sequence nehmen wir alles, dh eineme sequence request
				else if ( Tester.testFirstOrTestfirstDynAttributeIs0(thisIteratedElementTranslated,interpreter))
					test_first = false;
				else
					test_first = true;
				//System.out.println("       testfirt = "+test_first);
				if ( test_first ) {
					// dieses Element muss eingegeben werden
					stopnow = true;
					stoppedTicket = ticket;
				}
				
			}
						
		}
		if ( !stopnow) {
			setupChilds =null;
			setupIterator =null;			
		}
		//Sp.p("TicketManager.iterator finished with stoppedTicket="+stoppedTicket);
		return ( stoppedTicket );
			

		
	}
	
	public void setupSUMONLY() throws Exception {
		
		trans = interpreter.translatePrimitive(myOrig, false, -1, false);
		
		List<Element> childs = trans.getChildren();
		Iterator<Element> i = childs.iterator();			
		tickets = new HashMap<String, TkTicket>();
		ticketList = new ArrayList<TkTicket> ();
		while ( i.hasNext() ) {
		
			Element aktTkN = (Element)i.next();
			Sp.p("---- Working on: "+Commons.toText(aktTkN));
			if ( Tester.testOrTestdynAttributeIs0(aktTkN,interpreter)) {
				Sp.p("-A-");
				//lc.info("Input of id '"+aktTkN.getAttributeValue("id")+"' and type '"+aktTkN.getAttributeValue("type")+"' is skipped because test==0=="+aktTkN.getAttributeValue("test"));				
			} else {			
				Sp.p("-B 01 -");
				TkTicket ticket = getTicketByElement(aktTkN);

				ticketList.add(ticket);
				tickets.put(ticket.getTicketId(), ticket);
				
				if ( !ticket.isMsg ) 
					ticket.setInitialValue();
				Sp.p("-B 99 -");				

			}
			Sp.p("---- did work on: "+Commons.toText(aktTkN));
		}
		
	}
	
	private TkTicket getTicketByElement(Element aktTkN ) throws JDOMException, IOException, Exception {
		String tipe = aktTkN.getAttributeValue("type");
		if ( tipe == null ) tipe="text";
		if ( (aktTkN.getAttributeValue("id") == null ||
				  aktTkN.getAttributeValue("id").length() == 0 ) 
				  && !tipe.equals("msg")) 
				throw new LangException ("Tk needs an attribute @id: "+Commons.toText(aktTkN));
		
		String tipN = aktTkN.getAttributeValue("tip");
		if ( tipN!=null && tipN.length()== 0) tipN=null;	
	
		String label = aktTkN.getAttributeValue("label");
		if ( label == null && !tipe.equals("msg"))
			label = aktTkN.getAttributeValue("id");
		
		String regexVorstufe = aktTkN.getAttributeValue("regex");
		String regex = null;
		if ( regexVorstufe != null && regexVorstufe.trim().length() > 0 )
			regex = regexVorstufe;
		
		String regexMsgVorstufe = aktTkN.getAttributeValue("regex_msg");
		String regex_msg = null;
		if ( regexMsgVorstufe != null && regexMsgVorstufe.trim().length() > 0 )
			regex_msg = regexMsgVorstufe;		

		if ( regex != null && regex_msg==null)
			regex_msg = tiHandler.getSitzung().lang().getEeTmEnterRegexKompatibelNORMAL()+regex;

		String varType = null;
		boolean isMsg = false;
		String msg = null;
		if ( tipe.equals("text")  ||
			 tipe.equals("textblock") || 
			 tipe.equals("bool") || 
			 tipe.equals("sel") )
			varType="prop";
		else if (tipe.equals("accept")) {
			varType="prop";
			if ( flag_sum )
				throw new Exception("Accept with tk-flag 'sum' or 'sum_only' not allowed!");					
		} else if ( tipe.equals("file"))
			varType="file";
		else if ( tipe.equals("dir"))
			varType="dir";
		else if ( tipe.equals("msg")) {
			isMsg = true;							
			if ( aktTkN.getAttribute("msg") != null ) {
				msg = aktTkN.getAttributeValue("msg");
			} else if ( aktTkN.getAttribute("x") != null ) 
				msg = aktTkN.getAttributeValue("x");
			else
				msg = aktTkN.getText();
		} else {
			throw new LangException("Unknow type of tk, type="+tipe);
		}				
		String varId = aktTkN.getAttributeValue("id");
		if ( varId == null && !isMsg ) {
			throw new LangException("Needs attribute id the tk");
		}
		String follow_job = null;
		if ( aktTkN.getAttribute("follow_job") != null ) {
			follow_job = aktTkN.getAttributeValue("follow_job");
			// Sp.p("tk-sum-member with label ["+label+"] has follow_jobN ["+this.follow_jobN+"]");
		}
		String test_dyn_expression = null;
		if ( aktTkN.getAttribute("test_dyn") != null )
			test_dyn_expression = aktTkN.getAttributeValue("test_dyn");	
		TkTicket ticket = TkTicket.getTkTicketByTkSpec(
					tipe, tipN, label, regex, regex_msg, 
					varType, varId, follow_job, test_dyn_expression,
					msg,
					Commons.hasFlag(aktTkN, "nono"),
					interpreter,
					aktTkN,tiHandler);	
		
		return ticket;
		
	}	
	public HashMap<String, TkTicket> getTickets() {
		return tickets;
	}
	public ArrayList<TkTicket> getTicketList() {
		return ticketList;
	}

}
