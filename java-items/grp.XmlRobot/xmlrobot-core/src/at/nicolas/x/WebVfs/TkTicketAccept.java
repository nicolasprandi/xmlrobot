package at.nicolas.x.WebVfs;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.LangException;




public class TkTicketAccept extends TkTicket {
	String ok= null;
	String cancel= null;
	String save_textN = null;
	Question questionsN[];
	
	boolean confirmed = false;
	String labelN;
	
	
	int width = -1;
	int height = -1;
	
	boolean contentIsHtml = false;
	String content;
	
	boolean canSave = false;
	


	public TkTicketAccept(String tktype, String tip, String label, String regex, String regex_msg, String var_type,
			String var_id, String follow_job, String test_dyn_expression, String msg, boolean has_nono,
			Interpreter interpreter, Element tkSpec, ITiHandler tiHandler) throws Exception {
		super(tktype, tip, label, regex, regex_msg, var_type, var_id, follow_job, test_dyn_expression, msg, has_nono,
				interpreter, tkSpec, tiHandler);
		// System.out.println("bin im accept");
		
		labelN = label;
		if ( labelN == null )
			labelN = lang.getEeAcceptDefaultLabel();
		
		if ( tkSpec.getAttribute("label_accept") != null )
			ok= tkSpec.getAttributeValue("label_accept");
		else
			ok = lang.getEeAcceptDefaultOk();

		if ( tkSpec.getAttribute("label_decline") != null )
			cancel= tkSpec.getAttributeValue("label_decline");
		else
			cancel = lang.getEeAcceptDefaultDecline();
		
		String filetype = "text";
		if ( tkSpec.getAttribute("file_type") != null )
			filetype= tkSpec.getAttributeValue("file_type");
		
		String fileN = tkSpec.getAttributeValue("file");
		if ( tkSpec.getAttribute("x")!=null) {
			content = tkSpec.getAttributeValue("x");
			//System.out.println("BB x="+tkSpec.getAttributeValue("x"));
		} else if ( filetype.equals("html") && fileN != null) {
			contentIsHtml = true;
			content = CommonsVfs.readTextfile(fileN,tiHandler.getSitzung());
		} else if ( fileN != null){
			content = String.format(CommonsVfs.readTextfile(fileN,tiHandler.getSitzung()));
		} else {
			throw new LangException("tk-accept needs attribute 'x' or 'file'");
		}		
		
		if ( tkSpec.getAttribute("width")!=null && tkSpec.getAttribute("height")!=null) {
			width = Commons.string2int(tkSpec.getAttributeValue("width"));		
			height = Commons.string2int(tkSpec.getAttributeValue("height"));	
		}	
		canSave = Commons.hasFlag(tkSpec, "save");
	}
	
	public void setInitialValue() throws Exception {
		//Sp.p("- a -");
		if ( tkSpec.getChildren("q") != null ) {
			//Sp.p("- b -");
		int count_q = tkSpec.getChildren("q").size();
		//Sp.p("- c -");
		if ( count_q  > 0 ) {
			//Sp.p("- d -");
			questionsN = new Question[count_q];
			//Sp.p("- e -");
			List<Element> lq = tkSpec.getChildren("q");
			Iterator<Element> iq = lq.iterator();
			int i = 0;
			//Sp.p("- f -");
			while ( iq.hasNext()) {
				Element ceN = (Element)iq.next();
				
				Question q = new Question(ceN.getAttributeValue("label"),ceN.getAttributeValue("id"));
				if (Commons.hasFlag(ceN, "load")) {
					//Sp.p(ceN.getAttributeValue("id")+" is with load");
					int isOn = 0;
					try {
						String getVar = tiHandler.getVar("prop", ceN.getAttributeValue("id"));
				
						try {
							isOn = Commons.string2int(getVar);
						} catch (Exception e1) {
							isOn = 0;
						}

					} catch (Exception e1) {
						isOn = 0;
					}
					if ( isOn==1 ) {
					    q.answer=true;
					}
					
				}
				//Sp.p("- g -");
				questionsN[i]= q;				
				i++;
			}
		}
		} else {
			//Sp.p("- h -");
			// questionsN = new Question[count_q];
		}
		//Sp.p("- i -");
	}		
	
	public void printHtmlDivEditorOnly (
			PrintWriter out,
			boolean autofocus
			) throws Exception {
		printHtmlDivInternal(out,autofocus,true);
	}	
	
	public void printHtmlDiv (
			PrintWriter out,
			boolean autofocus
			) throws Exception {
		printHtmlDivInternal(out,autofocus,false);
	}
	public void printHtmlDivInternal (
			PrintWriter out,
			boolean autofocus,
			boolean editorOnly
			) throws Exception  {

		if (!editorOnly) {
			out.print("<div class='tkMember'>");
						
		}		
			

			//Sp.p("- i1 -");
			if ( contentIsHtml)
				out.println("<p>"+content+"</p>");
			else {
				//out.println("<p>z44"+html(content)+"</p>");
				out.println("<p>"+TkTicket.htmlLfByEscaped(content)+"</p>");
				
				//System.out.println("-- content="+content);
				//System.out.println("--        ="+htmlLf(content));
			}
			
			
			//Sp.p("- i2 -");
			try {
				for (Question q : questionsN ) {
					//Sp.p("- i3 -");
					out.print("<input type=\"checkbox\" name=\""+ticketId
							+"\" value=\""+q.index+"\"");
					//Sp.p("- i4 -");
					if ( q.answer) {
						out.print(" checked ");
					}
					//Sp.p("- i5 -");
					//nic123 hier das image
					out.print(">"+htmlByEscaped(q.questionN)+"<br>");
					//Sp.p("- i6 -");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}			
			if (!editorOnly) 
			out.print("</div>");
	}	
	
	public TkValidationError validate() {
		return null;
	}	
	
	public void reflectToMemory(IInputRequestor request) throws Exception {
		/*
		String parametername = ticketId;
		String valueFromForm = request.getParameter(parametername);
		if (valueFromForm == null)
			valueFromForm =""; */
				
		//Sp.p("TicketAccept.reflectToMemory("+ticketId+", "+
		//var_type+", "+var_id);
		reflectToMemo(var_type, var_id,"1");

		
		

		String[] selectedBoxes = request.getParameterValues(ticketId);
		if ( selectedBoxes==null)
			selectedBoxes = new String[0];
		
		try {
			for (Question q : questionsN ) {
				boolean checked = false;
				for (String s: selectedBoxes)
				{
					if ( s.equals(q.index)) {
						checked = true;
					}
				}
				
				if ( checked ) {
					interpreter.setVar("prop", q.prop_idN,"1");
				} else {
					interpreter.setVar("prop", q.prop_idN,"0");
				}			
			}
		} catch (Exception e) {

		}					
	}	
	
	static int qIndex = 0;
	private class Question {
		boolean answer = false;

		String prop_idN;
		String questionN;
		String index;
		
		String pictureHtml = "";
		
		public Question ( String q1N, String prop_idN ) throws Exception {
			if (q1N==null || prop_idN == null )
				throw new Exception("Tk-Accept-Questions must have attribute 'id' and  'label'");
			this.prop_idN = prop_idN;
			this.questionN = q1N;
			index = Commons.int2String(qIndex);
			qIndex++;
		}
	}	

}
