package at.nicolas.x.WebVfs;

import java.io.PrintWriter;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;

public class TkTicketTextblock extends TkTicket {

	boolean big = false;
	int rows=5;
	int cols=50;
	public TkTicketTextblock(String tktype, String tip, String label, String regex, String regex_msg, String var_type,
			String var_id, String follow_job, String test_dyn_expression, String msg, boolean has_nono,
			Interpreter interpreter, Element tkSpec, ITiHandler tiHandler) {
		super(tktype, tip, label, regex, regex_msg, var_type, var_id, follow_job, test_dyn_expression, msg, has_nono,
				interpreter, tkSpec, tiHandler);
		if ( Commons.hasFlag(tkSpec, "bigbig") ) {
			rows = 20;
			cols = 70;
		} else if ( Commons.hasFlag(tkSpec, "big") ) {
			rows = 10;
			cols = 40;
		}
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
			) throws Exception {
					
			String value = getVarFromMemoryN();
			
			if ( !editorOnly ) {
				out.println("<div class='tkMember'>");
				
				out.println("<div class='tkLabel'>"+getLabelHtml()+
				"</div>"); // = ("+var_type+","+var_id+")</div>");"+rows+"				
			}

			out.print("<div class='inputWithEraseTextblock'>");
			out.print("<table><tr><td>");	
			
			// kernstuek beginn
			out.print("<textarea form=\"main\" rows=\""+rows+"\" cols=\""+cols+"\"" +
			" name=\""+ticketId+"\"  onKeyPress=\"textareaKeyEvent(event);\" id=\"input-"+ticketId+"\" ");
			if ( autofocus )
				out.print(" autofocus ");
			out.print(">");
			
			if ( !value.equals("") ) {
				//out.print(value);
				out.print(getHtml3ByNatural(value));
				/*
				System.out.println("tk-textblock-edit id="+var_id);
				System.out.println("value        :"+value);				
				System.out.println("html(value)  :"+html(value));
				System.out.println("html3(value) :"+html3(value));
				System.out.println("");
				*/
			}
			out.println("</textarea>"); // end of input
			// kernstueck ende
			
			out.print("</td><td>");		
			out.println("<div class='inputEraseTextblock' >");
			
			out.println("<a class='inputEraseTextblock' href=\"javascript:;\" onclick=\"valErase('"+
			getTicketId()+"');\">x</a>");
			
			
			out.println("</div>"); // ende div class='inputErase'
	out.print("</td></tr></table>");		
		out.print("</div>"); // ende vom  class='inputWithErase'	
		
			if ( !editorOnly ) 			
			out.println("</div>");		
	}	
}
