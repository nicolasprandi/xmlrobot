/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.WebVfs;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.httpclient.util.URIUtil;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_tk.FitsRule;
import at.nicolas.x.term_tk.FitsRuleResult;
import at.nicolas.x.term_tk.Tk_TkStatic;


public class TkTicket {
	public boolean isMsg = false;
	public String msg_hedertext = "";
	public int msg_type = 0;
	public final static int MSG_TYPE_INFO = 0;
	public final static int MSG_TYPE_WARNING = 1;
	public final static int MSG_TYPE_ERROR = 2;
	public final static int MSG_TYPE_COPYPASTE = 3;
	public String msg = null;
	public String tktype;
	public String tip;
	public String label;
	public String regex;
	public String regex_msg;
	public String var_type;
	public String var_id;
	public String follow_job;
	public String test_dyn_expression	;
	public boolean has_nono = false;
	public Element tkSpec;
	ITiHandler tiHandler;
	private String htmlFieldType = "text";
	private String cols = null;
	private String rowsMsg = null;
	
	public Lang lang = null;
	
	public String getLabelHtml () {
		String img = getHtmlImage ( tkSpec,"label_",tiHandler.getSitzung());
		if ( img != null )
			return htmlByEscaped(label)+"<br>"+img;
		else {
			return htmlByEscaped(label);
		}
		//return html(label);
	}
	
	
	String ticketId;
	Interpreter interpreter;
	
	public static String getHtmlImage ( Element e, String prefix, ISitzung sitzung ) {
		
		if ( e.getAttribute(prefix+"pic") != null ) {
			String pic = e.getAttributeValue(prefix+"pic");
			String html = "<p>"+prefix+"pic="+pic+"</p>";
			
			String pic_alt =pic;
			if ( e.getAttribute(prefix+"pic_alt") != null ) {
				pic_alt =  e.getAttributeValue(prefix+"pic_alt");
			}
			
			try {
				// String derInhalt = Entities.exportEntities(filenameOfPictureE);
				String derInhalt = Entities.exportEntities(pic);
				CommonsVfs.grantsFileRead(derInhalt, sitzung);
				//System.out.println("--X1--  derInhalt   = "+derInhalt);
				String rel;
				try {
					rel = sitzung.getRelativeLibroPath(derInhalt);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//System.out.println("Reqeusted "+derInhalt);
					e1.printStackTrace();
					throw e1;
				}
				//System.out.println("--X2--  rel         = "+rel);
				String pathEncoded=URIUtil.encodePath(rel);
				//System.out.println("--X3--  pathEncoded = "+pathEncoded);

				String imgsrc = pathEncoded;
				//System.out.println("--X4-- image="+imgsrc);
				String alt =" alt=\""+Commons.text_to_html(pic_alt+"(e:o)LUKKI")+"\" ";
					
				Element eE = e;
				// style='height: auto; width: auto;
				String sizer ="" ;
				if ( ( eE.getAttribute(prefix+"pic_max_y") != null && eE.getAttributeValue(prefix+"pic_max_y").length() > 0 ) || 
					 ( eE.getAttribute(prefix+"pic_max_x") != null && eE.getAttributeValue(prefix+"pic_max_x").length() > 0) ) {
					sizer = " style=\" height: auto; width: auto; ";
					if ( eE.getAttribute(prefix+"pic_max_y") != null && eE.getAttributeValue(prefix+"pic_max_y").length() > 0) {
						sizer = sizer + "max-height: "+eE.getAttributeValue(prefix+"pic_max_y")+"px;";
					}
					if ( eE.getAttribute(prefix+"pic_max_x") != null && eE.getAttributeValue(prefix+"pic_max_x").length() > 0) {
						sizer = sizer + "max-width: "+eE.getAttributeValue(prefix+"pic_max_x")+"px;";
					}				
					sizer = sizer + "\"";
					
				}			
				//System.out.println("--X6-- imgsrc="+imgsrc+" alt="+alt+" sizer="+sizer);	
				return "<img src='"+imgsrc+"' "+alt+sizer+">";				
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				Sp.pe(ex);
				return "<p>problem: "+ex.getMessage()+"</p>";
			}			
			
		} else 
			return null;
	}
	public static String getImgHtml(Element eE, ITiHandler rh, String alternativeTextEscaped,
			String filenameOfPictureE ) {
		String myAltE ;
		if ( alternativeTextEscaped != null )
			myAltE = alternativeTextEscaped;
		else if ( eE.getAttribute("explain") != null )
			myAltE = eE.getAttributeValue("explain");
		else
			myAltE = "Picture-Bild-Image";
		
		String imgsrc;
		
		try {
			String derInhalt = Entities.exportEntities(filenameOfPictureE);
			CommonsVfs.grantsFileRead(derInhalt, rh.getSitzung());
			//System.out.println("--X--  derInhalt   = "+derInhalt);
			String rel = rh.getSitzung().getRelativeLibroPath(derInhalt);
			//System.out.println("--X--  rel         = "+rel);
			String pathEncoded=URIUtil.encodePath(rel);
			//System.out.println("--X--  pathEncoded = "+pathEncoded);

			imgsrc = pathEncoded;
			//System.out.println("image="+imgsrc);
			String alt =" alt=\""+Commons.text_to_html(myAltE+"(e:c)SEPPI")+"\" ";
				
			
			// style='height: auto; width: auto;
			String sizer ="" ;
			if ( ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0 ) || 
				 ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) ) {
				sizer = " style=\" height: auto; width: auto; ";
				if ( eE.getAttribute("max_y") != null && eE.getAttributeValue("max_y").length() > 0) {
					sizer = sizer + "max-height: "+eE.getAttributeValue("max_y")+"px;";
				}
				if ( eE.getAttribute("max_x") != null && eE.getAttributeValue("max_x").length() > 0) {
					sizer = sizer + "max-width: "+eE.getAttributeValue("max_x")+"px;";
				}				
				sizer = sizer + "\"";
				
			}			
				
			return "<img src='"+imgsrc+"' "+alt+sizer+">";				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			return "<p>problem: "+e.getMessage()+"</p>";
		}		
	}	
	//
	public boolean isDynamo() {
		return Commons.hasFlag(tkSpec, "dynamo");
	}

	public void setValueN(String valueNatural) throws Exception {
		
		reflectToMemo(var_type, var_id,valueNatural);
	}
	/*
	public void setX2xxx() {
		System.out.println("--------------------  Ticket "+var_type+" "+var_id+" called with setX2");
		if ( tkSpec.getAttribute("x2")!=null ) {
			String curVal = getVarFromMemoryE();

			if ( curVal.equals("")) {
				try {
					setValueN(Entities.exportEntities(tkSpec.getAttributeValue("x2")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Sp.pe(e);
				}
			}
		}

		
		
	}*/
	public void setInitialValue() throws Exception {
					
		String curVal = getVarFromMemoryE();
		
		if ( curVal.equals("")) {
			String defVal = "";
			
			try {
				defVal = Tk_TkStatic.getDefaultValue(tkSpec, var_type, var_id, tiHandler);
			} catch (Exception e) {
			}	
			//if ( !defVal.equals("")) {
				setValueN(Entities.exportEntities(defVal));
			//}				
		}
	}	
	
	public void reflectToMemo( 
			String varType,
			String varIdNatural,
			String valueNatural ) throws Exception {
		// Sp.p("reflectToMemo"+var_type+","+var_id+"="+valueNatural);
		if ( follow_job == null ) {
			interpreter.setVar(var_type, 
					varIdNatural, 
					Entities.importEntities(valueNatural));
		} else {
			
			interpreter.setVarFollow(var_type, 
					varIdNatural, 
					Entities.importEntities(valueNatural),
					follow_job);			
		}
		
	}
	public void reflectToMemory(IInputRequestor request) throws Exception {
		Sp.p("TkTicket: Reflect to Mem var_id = [ [ "+var_id+" ] ]");

		String parametername = ticketId;
		String valueFromForm = request.getParameter(parametername);
		//Sp.p("valueFromForm1 = "+valueFromForm);
		if (valueFromForm == null)
			valueFromForm ="";
		else
			valueFromForm=valueFromForm.replaceAll("\r\n", "\n");
		Sp.p("valueFromForm2 = "+valueFromForm);
		// Sp.p("TkTicket.reflectToMemory("+ticketId+") found value ["+valueFromForm+"] for para ["+parametername+"]");

		
		reflectToMemo(var_type, var_id,valueFromForm);
		//String check = getVarFromMemoryE();
		//Sp.p("checked       = "+check        );
	}
	
	public String getTicketId() {
		return ticketId;
	}


	
	public String getVarFromMemoryN () {
		String ret = null;
		try {
			ret = interpreter.getVar(var_type, var_id);
			ret = Entities.exportEntities(ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Sp.p("Da sind wir mit nichts in der hand-Natural");
		
			ret= "";
		}
		return ret;
	}
	public String getVarFromMemoryE () {
		String ret = null;
		try {
			ret = interpreter.getVar(var_type, var_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Sp.p("Da sind wir mit nichts in der hand-Escaped");
		
			ret= "";
		}
		return ret;
	}	
	
	public String getVarFromMemoryEOnJovial () {
		String ret = null;
	
			try {
				ret = interpreter.getVariableValue2(var_type, var_id);
				//System.out.println("getVarFromMemoryEOnJovial hat erhalten fuer "+var_type+"/"+var_id+" :"+ret);
				if ( var_type.equals("dir") || var_type.equals("file"))
					ret = tiHandler.getUriPresentation(ret);
				//System.out.println("nach gtiHandler.getUriPresentation fuer "+var_type+"/"+var_id+" :"+ret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("getVarFromMemoryEOnJovia -------------------------l");
				e.printStackTrace();
			}
		
		return ret;
	}		
	public TkValidationError validate() {
		String mycurrentvalue = getVarFromMemoryN (); 
		
		if ( tktype.equals("text") && tiHandler.getSitzung().getMaxTkTextLen() != null ) {
			//System.out.println("handling maxLen for text, "+tiHandler.getSitzung().getMaxTkTextLen());
			if ( mycurrentvalue.length() > tiHandler.getSitzung().getMaxTkTextLen() ) {
				Sp.pRejectedInputText(mycurrentvalue.length(),tiHandler.getSitzung().getMaxTkTextLen());
				return new TkValidationError("more characters than allowed: "+
					tiHandler.getSitzung().getMaxTkTextLen(),label);
			}
		} else if ( tktype.equals("textblock") && tiHandler.getSitzung().getMaxTkTextblockLen() != null ) {
			//System.out.println("handling maxLen for textblock "+tiHandler.getSitzung().getMaxTkTextblockLen());
			if ( mycurrentvalue.length() > tiHandler.getSitzung().getMaxTkTextblockLen() ) {
				Sp.pRejectedInputTextBlock(mycurrentvalue.length(), tiHandler.getSitzung().getMaxTkTextblockLen());
				return new TkValidationError("more characters than allowed: "+
					tiHandler.getSitzung().getMaxTkTextblockLen(),label);
			}
		} 
		TkValidationError tkve = 
		fits (
			mycurrentvalue	,
			label,
			has_nono,
			regex,
			regex_msg,
			this.lang);
		if ( tkve != null ) return tkve;
		
		FitsRuleResult frr = FitsRule.check(
				mycurrentvalue, 
				tkSpec.getAttributeValue("rule"), 
				this, 
				label);
		if ( frr.changed ) {
			try {
				setValueN(frr.newValue);
				return (new TkValidationError(frr.askMessage,label));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
				return null;
			}
			
		} else {
			return null;
		}
	}
	
	static TkValidationError fits(
			String valueN,
			String labelN,
			boolean hasNono,
			String regex,
			String regexMsg,
			Lang lang
			) {
		String ret = null;
		if ( valueN == null ) {							
			ret = lang.getThereMustBeAValueFor()+" "+labelN;
		} else if ( valueN.length()==0 && !hasNono ) {
			ret = null;
		} else if ( valueN.length()==0 && hasNono ) {
			ret = lang.getThereMustBeAValueFor()+" '"+labelN+"'";			
		} else if ( regex == null ) {
			ret = null;
		} else {
			// boolean valid_regex = valueN.matches(regex);
			//System.out.
			//Sp.p("Evaluating for regex : value=["+valueN+"] regex=["+regex+"] evaluates to ...");
			String regexN = Entities.exportEntities(regex);
			boolean valid_regex = Commons.stringMatchesRegex(valueN, regexN);
			Sp.p("Evaluating for regex : value=["+valueN+"] regex=["+regexN+"] evaluates to ..."+
			valid_regex);
			if ( valid_regex )
				ret = null;
			else
				ret =regexMsg; // +" ... but the value of ["+labelN+"] was ["+valueN+"] and its len is ["+valueN.length()+"] and the requested regex is ["+regexN+"]";
		}		
		if ( ret == null )
			return null;
		else
			return new TkValidationError(ret,labelN);
		
	}
	public synchronized String getSumLabelEscaped() {
		if ( tktype.equals("msg")) {
			return msg;
		} else {	
			if ( Commons.hasFlag(tkSpec, "password"))
			return "xxx";
			else
			return getVarFromMemoryEOnJovial();			
		}

	}
	public static String htmlByEscaped ( String escaped ) {
		return Commons.text_to_html(Entities.exportEntities(escaped));
	}	
	public static String html3ByEscaped ( String escaped ) {
		return Commons.text_to_html3(Entities.exportEntities(escaped));
	}	
	
	public static String getHtml3ByNatural ( String natural ) {
		return Commons.text_to_html3(natural);
	}		
	
	public static String htmlLfByEscaped ( String in ) {
		String ret = Commons.stringReplace(htmlByEscaped(in), "\n", "<br>");

		return ret;
	}
	
	public static String htmlLfByNatural( String in ) {
		String ret = Commons.stringReplace(htmlByNatural(in), "\n", "<br>");

		return ret;
	}	
	
	public static String htmlByNatural ( String natural ) {
		return Commons.text_to_html(natural);
	}	
	public static String html3ByNatural ( String natural ) {
		return Commons.text_to_html3(natural);
	}	
	
	// retourniert jetzt den java für das top
	private String shortenTip (String tippex ) {
		if ( tippex == null ) return null;
		else if ( tippex.indexOf("Help:") > 0 )
			return tippex.substring(0, tippex.indexOf("Help:"));
		else
			return tippex;
	}	
	public String printHtmlDivSum (
			PrintWriter out
			) throws Exception {
		String sumLabelEscaped = getSumLabelEscaped();
		// System.out.println("getSumLabelEscaped("+ticketId+"/"+tktype+") = ["+sumLabelEscaped+"]");
		String scriptReturn = null;
		if ( tktype.equals("msg")) {
			out.print("<tr class=\"tkItemMsg\">");
			out.print("<td class=\"tkItemMsg\" colspan='2'>");
			
			if ( msg_hedertext != null && msg_hedertext.length()> 0 )
				out.print(htmlByNatural(msg_hedertext));
			if ( msg_hedertext != null && msg_hedertext.length()> 0 &&
					sumLabelEscaped != null && sumLabelEscaped.length()> 0	)
				out.print("<br>");
			if ( sumLabelEscaped != null && sumLabelEscaped.length()> 0 )
				out.print(htmlLfByEscaped(sumLabelEscaped));
								
			out.print("</td></tr>");
			
			/*
			out.print("<div class=\"ti_div_msg_sum\">"+msg_hedertext +htmlLf(sumLabelEscaped)+"</div>");
			*/
		} else if ( sumLabelEscaped.equals("")) {
			out.print("<tr class='tkItem'>");

			out.print("<td class=\"tkItemLabel\" >"+
					getLabelHtml()
					+"</td>");
			
			out.print("<td class=\"tkItemAdd\" >"+
					"<a href='tkEdit?ticket="+ticketId+"'>"+"[ + ]"
					+"</td>");
			
			out.print("<td class=\"tkItemTipLink\" >");			
			out.println("<div class='tkItemTipToggle'><a class='tkItemTipToggle' href=\"javascript:;\" onclick=\"tipToggle('tip-"+getTicketId()+"');\">&nbsp;&nbsp;?&nbsp;&nbsp;</a></div>");
			scriptReturn ="tipToggle('tip-"+getTicketId()+"');";
			out.print("</td>");					
			
			out.print("</tr>");
			
						
		} else {
			
			out.print("<tr class='tkItem'>");

			out.print("<td class=\"tkItemLabel\" >"+
					getLabelHtml()
					+"</td>");
			
			if ( isDynamo() ) {
				out.print("<td class=\"tkItemValue\" >"
						+htmlByEscaped(sumLabelEscaped)
						+"</td>");				
			} else {
				out.print("<td class=\"tkItemValue\" >"+
						"<a href='tkEdit?ticket="+ticketId+"'>"+htmlByEscaped(sumLabelEscaped)+"</a>"
						+"</td>");				
			}
			

			
			out.print("<td class=\"tkItemTipLink\" >");			
			out.println("<div class='tkItemTipToggle'><a class='tkItemTipToggle' href=\"javascript:;\" onclick=\"tipToggle('tip-"+getTicketId()+"');\">&nbsp;&nbsp;?&nbsp;&nbsp;</a></div>");
			scriptReturn ="tipToggle('tip-"+getTicketId()+"');";
			out.print("</td>");		
			
			out.print("</tr>");
						
		}		
		
		out.print("<tr class='tkItemTipContentTr'><td></td>");

		out.print("<td class=\"tkItemTipContent\"  colspan=\"2\" />");
		
		out.print("<div class='tkItemTip'id='tip-"+getTicketId()+"' style='display:none;'>");
		
		//nic123 tip runterkürzen wenn nicht im langsamen summary
		//shortenTip
		String naturalTip = Entities.exportEntities(tip);
		naturalTip = shortenTip ( naturalTip );
		// out.print(TkTicket.htmlLfByEscaped (tip));
		out.print(TkTicket.htmlLfByNatural(naturalTip));
		out.print("</div></td></tr>");
		return scriptReturn;
	}	
	public void printHtmlDivEditorOnly (
			PrintWriter out,
			boolean autofocus
			) throws Exception {}
	public void printHtmlDivEditorOnly (
			PrintWriter out,
			boolean autofocus,
			String cssPrefix
			) throws Exception {
		// System.out.println(".................. printHtmlDivEditorOnly for "+tktype);
		if ( tktype.equals("msg")) {
			if ( msg_type == MSG_TYPE_COPYPASTE) { 

				out.print("<div class=\""+cssPrefix+"Msg "+cssPrefix+"MsgCopy\">"+html3ByEscaped(msg_hedertext)+"<br>");

				
				out.print("<textarea rows=\""+rowsMsg+"\" cols=\""+cols+"\"" );
				if ( msg_type == MSG_TYPE_COPYPASTE)
				out.print(" onclick=\"this.focus();this.select()\" ");
				out.print(" readonly=\"readonly\" ");				
				out.print(">");				
				
				out.print(html3ByEscaped(msg));

				out.println("</textarea>"); // end of input				
				
				out.print("</div>");
			} else {
				out.print("<div class=\""+cssPrefix+"Msg\">");
			
				
				if ( msg_hedertext != null && msg_hedertext.length()> 0 )
					out.print(htmlByNatural(msg_hedertext));
				if ( msg_hedertext != null && msg_hedertext.length()> 0 &&
						msg != null && msg.length()> 0	)
					out.print("<br>");
				if ( msg != null && msg.length()> 0 )
					out.print(htmlLfByEscaped(msg));
									
				out.print("</div>");
				
			}
		} else if ( tktype.equals("text")) {
			
			String value = getVarFromMemoryN();
			
		
	out.print("<div class='inputWithErase'>");
		out.print("<table><tr><td>");	
	
			out.print("<input size=\""+cols+"\" "+
			" name=\""+ticketId+"\" id=\"input-"+ticketId+"\"");
			
			if ( tkSpec.getAttribute("html_type") != null ) 
				out.print(" type=\""+getHtml3ByNatural(tkSpec.getAttributeValue("html_type"))+"\" " );
			else
				out.print(" type=\""+this.htmlFieldType+"\"" );

			if ( tkSpec.getAttribute("html_inputmode") != null ) 
				out.print(" inputmode=\""+getHtml3ByNatural(tkSpec.getAttributeValue("html_inputmode"))+"\" " );

			if ( tkSpec.getAttribute("html_placeholder") != null ) 
				out.print(" placeholder=\""+getHtml3ByNatural(tkSpec.getAttributeValue("html_placeholder"))+"\" " );
			
			
			if ( !value.equals("") ) {
				out.print(" value=\""+getHtml3ByNatural(value)+"\"");
			}
			if ( autofocus )
				out.print(" autofocus ");
			if ( has_nono )
				out.print(" required ");
			
			if ( regex!= null)
				out.print(" pattern=\""+getHtml3ByNatural(regex)+"\"");
			if ( regex_msg != null )
				out.print(" title=\""+getHtml3ByNatural(regex_msg)+"\"");
				
		out.println(" />");		
	out.print("</td><td>");		
	
			out.println("<div class='inputErase' >");
			
			out.println("<a class='inputErase' href=\"javascript:;\" onclick=\"valErase('"+
			getTicketId()+"');\">x</a>");
			
			
			out.println("</div>"); // ende div class='inputErase'
	out.print("</td></tr></table>");		
		out.print("</div>"); // ende vom  class='inputWithErase'	
			
		} else {
			printHtmlDivEditorOnly (
					 out,
					 autofocus);
		}
		

		

			
		
	}
	public void printHtmlDiv (
			PrintWriter out,
			boolean autofocus
			) throws Exception {
		
		if ( tktype.equals("msg")) {
			if ( msg_type == MSG_TYPE_COPYPASTE) {

				out.print("<div class=\"tkMbrMsg tkMbrMsgCopy\">"+html3ByEscaped(msg_hedertext)+"<br>");

				
				out.print("<textarea rows=\""+rowsMsg+"\" cols=\""+cols+"\"" );
				if ( msg_type == MSG_TYPE_COPYPASTE)
				out.print(" onclick=\"this.focus();this.select()\" ");
				out.print(" readonly=\"readonly\" ");				
				out.print(">");				
				
				out.print(html3ByEscaped(msg));

				out.println("</textarea>"); // end of input				
				
				out.print("</div>");
			} else {
				out.print("<div class=\"tkMbrMsg\">");
			
				
				if ( msg_hedertext != null && msg_hedertext.length()> 0 )
					out.print(htmlByNatural(msg_hedertext));
				if ( msg_hedertext != null && msg_hedertext.length()> 0 &&
						msg != null && msg.length()> 0	)
					out.print("<br>");
				if ( msg != null && msg.length()> 0 )
					out.print(htmlLfByEscaped(msg));
									
				out.print("</div>");
				
			}
		} else {
			
			String value = getVarFromMemoryN();
			//out.print("<p>asking: ("+var_type+","+var_id+")</p>");
			//out.print("<p>asking: "+interpreter.getDescriptionStringSmall()+"</p>");

			// out.print("<p>value: "+value+"</p>");
			//Sp.p("*** value of "+var_type+","+var_id+" is="+value);
			
			out.print("<div class='tkMember'>");
			
			out.println("<div class='tk_label_fastsum'>"+getLabelHtml()+
			"</div>"); // = ("+var_type+","+var_id+")</div>");
		
			
			out.print("<input size=\""+cols+"\" type=\""+this.htmlFieldType+"\""+
			" name=\""+ticketId+"\"");
			
			if ( !value.equals("") ) {
				out.print(" value=\""+htmlByEscaped(value)+"\"");
			}
			if ( autofocus )
				out.print(" autofocus ");
			out.println(" />");	
			
			out.println("<p>apfel</p>");
			out.print("</div>");			
		}		
	}
	public static TkTicket getTkTicketByTkSpec (
			String tktype,
			String tip,
			String label,
			String regex,
			String regex_msg,
			String var_type,
			String var_id,
			String follow_job,
			String test_dyn_expression,
			String msg,
			boolean has_nono,
			Interpreter interpreter,
			Element tkSpec,
			ITiHandler tiHandler
					) throws JDOMException, IOException, Exception {
		//Sp.p("-------------- tktype="+tktype+" ----------------");
		if ( tktype.equals("msg")) 
			return new TkTicket(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);
		else if ( tktype.equals("bool")) 
			return new TkTicketBool(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);
		else if ( tktype.equals("file") || tktype.equals("dir")) {
			//System.out.println("wir wind in einem dir oder file");
			/* if ( tiHandler.getSurrounding().appFileProvisionPath != null ||
				 tiHandler.getSurrounding().uploadPath != null )
				return new TkTicketFileExtended(
					tktype,
					 tip,
					 label,
					 regex,
					 regex_msg,
					 var_type,
					 var_id,
					 follow_job,
					 test_dyn_expression,
					 msg,
					 has_nono,
					 interpreter,
					 tkSpec,
					 tiHandler);	
			else */
				return new TkTicketFileExtra(
				tktype,
				 tip,
				 label,
				 regex,
				 regex_msg,
				 var_type,
				 var_id,
				 follow_job,
				 test_dyn_expression,
				 msg,
				 has_nono,
				 interpreter,
				 tkSpec,
				 tiHandler);		
		} else if ( tktype.equals("sel")) 
			return new TkTicketSel(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);
		else if ( tktype.equals("textblock")) 
			return new TkTicketTextblock(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);
		else if ( tktype.equals("accept")) 
			return new TkTicketAccept(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);
		else
			return new TkTicket(
			tktype,
			 tip,
			 label,
			 regex,
			 regex_msg,
			 var_type,
			 var_id,
			 follow_job,
			 test_dyn_expression,
			 msg,
			 has_nono,
			 interpreter,
			 tkSpec,
			 tiHandler);			
		
		
	}
	
	public TkTicket(
			String tktype,
			String tip,
			String label,
			String regex,
			String regex_msg,
			String var_type,
			String var_id,
			String follow_job,
			String test_dyn_expression,
			String msg,
			boolean has_nono,
			Interpreter interpreter,
			Element tkSpec,
			ITiHandler tiHandler) {
		
		this.tiHandler = tiHandler;
		this.has_nono = has_nono;
		this.interpreter = interpreter;
		this.tktype = tktype;
		this.tip = tip;
		this.label = label;
		this.regex = regex;
		this.regex_msg = regex_msg;
		this.var_type = var_type;
		this.var_id = var_id;
		this.follow_job = follow_job;
		this.test_dyn_expression = test_dyn_expression;
		this.msg = msg;
		this.tkSpec = tkSpec;
		
		this.lang = tiHandler.getSitzung().lang();
		
		if ( tkSpec.getAttribute("cols") != null )
			cols = tkSpec.getAttributeValue("cols");
		else if (tktype.equals("text"))
			cols = tiHandler.getSurrounding().getDefaultColsText();
		else
			cols = tiHandler.getSurrounding().getDefaultColsMsg();
		
		
		if ( Commons.hasFlag(tkSpec, "password"))
			this.htmlFieldType = "password";
		//
		if ( msg != null && tktype.equals("msg") ) {
			isMsg = true;
			if ( tkSpec.getAttribute("msg_type") != null ) {
				if ( tkSpec.getAttributeValue("msg_type").equals("i")) {
					msg_type = MSG_TYPE_INFO;
					// msg_hedertext = "I N F O:";
				} else if ( tkSpec.getAttributeValue("msg_type").equals("w")) {
					msg_type = MSG_TYPE_WARNING;	
					msg_hedertext = this.lang.getLabel_warning();
				} else if ( tkSpec.getAttributeValue("msg_type").equals("e")) {
					msg_type = MSG_TYPE_ERROR;		
					msg_hedertext = this.lang.getLabel_error();
				} else if ( tkSpec.getAttributeValue("msg_type").equals("c") && Tk_TkStatic.getLabel(this.tkSpec)!=null) {
					msg_type = MSG_TYPE_COPYPASTE;		
					msg_hedertext = Tk_TkStatic.getLabel(this.tkSpec);
				}
			}
			if ( tkSpec.getAttribute("rows") != null )
				rowsMsg = tkSpec.getAttributeValue("rows");
			else
				rowsMsg = tiHandler.getSurrounding().getDefaultRowsMsg();
		}
	
		
		
		if ( isMsg) //if ( tktype.equals("msg"))
			this.ticketId = "m";
		else
			this.ticketId=var_type.substring(0, 1)+var_id;
		
	}

}