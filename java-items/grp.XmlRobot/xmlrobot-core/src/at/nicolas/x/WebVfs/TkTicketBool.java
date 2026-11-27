package at.nicolas.x.WebVfs;

import java.io.PrintWriter;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Vcar;

public class TkTicketBool extends TkTicket {
	boolean no_is_0;
	
	public TkTicketBool(String tktype, String tip, String label, String regex, String regex_msg, String var_type,
			String var_id, String follow_job, String test_dyn_expression, String msg, boolean has_nono,
			Interpreter interpreter, Element tkSpec,ITiHandler tiHandler) {
		super(tktype, tip, label, regex, regex_msg, var_type, var_id, follow_job, test_dyn_expression, msg, has_nono,
				interpreter,tkSpec,tiHandler);
		no_is_0 = Commons.hasFlag(tkSpec, "no_is_0");
	}
	
	public void printHtmlDivEditorOnly (
			PrintWriter out,
			boolean autofocus
			) throws Exception {
		
		String value = getVarFromMemoryN();

		
		out.print("<input type=\"radio\" name=\""+ticketId
				+"\" value=\"1"+"\"");	
		if ( value.equals("1")) {
			out.print(" checked ");
			if (autofocus) out.print(" autofocus ");
		}				
		out.print(">"+lang.getYes()+"<br>");	
		
		out.print("<input type=\"radio\" name=\""+ticketId
				+"\" value=\"0"+"\"");	
		if ( value.equals("0")) {
			out.print(" checked ");
			if (autofocus) out.print(" autofocus ");
		}			


		out.print(">"+lang.getNo()+"<br>");			
		
		if ( !has_nono && !no_is_0) {
			out.print("<input type=\"radio\" name=\""+ticketId
					+"\" value=\""+"sys_empty"+"\"");	
			if ( value.equals("")) {
				out.print(" checked ");
				if (autofocus) out.print(" autofocus ");
			}				
			out.print(">"+lang.getIgnore()+"<br>");					
		}
		

			
		
	}	
	public void printHtmlDiv (
			PrintWriter out,
			boolean autofocus
			) throws Exception {
					
			String value = getVarFromMemoryN();
						
			out.print("<div class='tkMember'>");
			
			out.print("<div class='tkLabel'>"+getLabelHtml()+"</div>");
			
			out.print("<input type=\"radio\" name=\""+ticketId
					+"\" value=\"1"+"\"");	
			if ( value.equals("1")) {
				out.print(" checked ");
				if (autofocus) out.print(" autofocus ");
			}				
			out.print(">"+lang.getYes()+"<br>");	
			
			out.print("<input type=\"radio\" name=\""+ticketId
					+"\" value=\"0"+"\"");	
			if ( value.equals("0")) {
				out.print(" checked ");
				if (autofocus) out.print(" autofocus ");
			}			

	
			out.print(">"+lang.getNo()+"<br>");			
			
			if ( !has_nono && !no_is_0) {
				out.print("<input type=\"radio\" name=\""+ticketId
						+"\" value=\""+"sys_empty"+"\"");	
				if ( value.equals("")) {
					out.print(" checked ");
					if (autofocus) out.print(" autofocus ");
				}				
				out.print(">"+lang.getIgnore()+"<br>");					
			}
			
			out.print("</div>");		
	}		
	
	public void reflectToMemory (IInputRequestor request) throws Exception {
		
		String parametername = ticketId;
		String valueFromForm = request.getParameter(parametername);
		if (valueFromForm == null)
			valueFromForm ="";
		

		Sp.p("TkTicketBool.reflectToMemory("+ticketId+") found value ["+valueFromForm+"] for para ["+parametername+"]");
				
		if ( valueFromForm.equals("sys_empty"))
			valueFromForm = "";
		
		
		reflectToMemo(var_type, var_id,valueFromForm);

	}	
	
	public synchronized String getSumLabelEscaped() {
		String ret =  getVarFromMemoryEOnJovial();
		try {
			if ( !ret.equals("") ) {
				Vcar vb = new Vcar(ret);
				if ( vb.isBool ) {
					if ( vb.getBool() )
						ret= lang.getYes();
					else
						ret= lang.getNo();
				} 
			}
				
		} catch (Exception e) {
			ret = "";
		}
		return ret;			

	}	

}
