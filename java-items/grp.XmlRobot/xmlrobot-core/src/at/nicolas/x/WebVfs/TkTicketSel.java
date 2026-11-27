package at.nicolas.x.WebVfs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.IInputRequestor;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.term_tk.Tester;

public class TkTicketSel extends TkTicket {
	Element spec;
	Element options = null;
	boolean fresh_xml = false;
	boolean multi = false;

	private HashMap<String, String> map = null;
	
	public TkTicketSel(String tktype, String tip, String label, String regex, String regex_msg, String var_type,
			String var_id, String follow_job, String test_dyn_expression, String msg, boolean has_nono,
			Interpreter interpreter,
			Element spec,
			ITiHandler tiHandler) throws JDOMException, IOException, Exception {
		super(tktype, tip, label, regex, regex_msg, var_type, var_id, follow_job, test_dyn_expression, msg, has_nono,
				interpreter,spec,tiHandler);
		this.spec = spec;
		// TODO Auto-generated constructor stub
		multi = Commons.hasFlag(spec, "multi");
		if ( spec.getAttributeValue("xml") != null ) {
			options = CommonsVfs.getElementByFilename(spec.getAttributeValue("xml"),tiHandler.getSitzung());
			importEntitiesOptions();
			fresh_xml = Commons.hasFlag(spec, "fresh_xml");

		} else {
			options = spec;

		}
		
		buildMap();
	}

	private void buildMap() {
		map = new HashMap<String, String> ();
		List<Element> lo = options.getChildren();
		Iterator<Element> io = lo.iterator();
		
		while ( io.hasNext()) {
			Element ao = (Element)io.next();
			
			boolean do_show = true;
			//if ( ao.getAttribute("test") != null ) {
			//	if ( ao.getAttributeValue("test").equals("0"))
			//		do_show = false;
			//}
			// achtung: wenn aus xml-file wird nichts translated
			if ( Tester.testOrTestdynAttributeIs0(ao,interpreter)) {
				do_show = false;
			}
			
			if ( do_show ) {
				String aoText = (ao.getText());
				String aoLabel = ao.getAttributeValue("label");
				if ( aoLabel == null )
					aoLabel = aoText;			


				//Sp.p("Adding to map "+aoText+" "+aoLabel);
				map.put ( aoText, aoLabel);		

				
			}

		}
		printKeyset();
		
	}	
	private void importEntitiesOptions() {
		Entities.importEntities(options);
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
		

		if ( fresh_xml ) {
			//Sp.p("Sel is doing new buildMap by fresh_xml");
			options = CommonsVfs.getElementByFilename(spec.getAttributeValue("xml"),tiHandler.getSitzung());
			importEntitiesOptions();
			buildMap();
		} else {
			//Sp.p("Sel is NO fresh_xml");
		}
			
			String value = getVarFromMemoryEOnJovial();
			if (!editorOnly) {
				out.print("<div class='tkMember'>");
				
				out.print("<div class='tkLabel'>"+getLabelHtml()+
				"</div>");				
			}

			
			List<Element> lo = options.getChildren();
			Iterator<Element> io = lo.iterator();
			
			boolean firstPic = true;

			if ( !has_nono && !multi) {
				out.print("<input type=\"radio\" name=\""+ticketId
						+"\" value=\""+"sys_empty"+"\"");	
				if ( value.equals("")) {
					out.print(" checked ");
					if ( autofocus )
						out.println(" autofocus ");

				}				
				out.print("><i>"+lang.getIgnore()+"</i><br>");					
			}
			
			while ( io.hasNext()) {
				Element ao = (Element)io.next();
				boolean do_show = true;
				//if ( ao.getAttribute("test") != null ) {
				//	if ( ao.getAttributeValue("test").equals("0"))
				//		do_show = false;
				//}
				if ( Tester.testOrTestdynAttributeIs0(ao,interpreter)) {
					do_show = false;
				}
				
				if ( do_show ) {
					String aoText = ao.getText();
					String aoLabel = ao.getAttributeValue("label");
					if ( aoLabel == null )
						aoLabel = aoText;			
					//Sp.p("displaySel: value="+value+", text="+aoText+", label="+aoLabel);
					if ( !multi) {
						String maybePic = getHtmlImage(ao, "", tiHandler.getSitzung());

						if ( maybePic != null ) {
							if ( firstPic )
							  out.print("<br>");
						}
						
						out.print("<input type=\"radio\" name=\""+ticketId
								+"\" value=\""+htmlByEscaped(aoText)+"\"");
						
						if ( value.equals(aoText)) {
							out.print(" checked ");
							if ( autofocus )
								out.println(" autofocus ");
						}
						
						
						// out.print(">"+html(aoLabel)+"<br>");		
						out.print(">");	// input close
						out.print(htmlByEscaped(aoLabel));	
						
						
						if ( maybePic != null ) {
							out.print("<br>");
							out.print(maybePic);		
							firstPic = false;
							out.print("<hr/>");
						} else
							out.print("<br>");	
					} else {
						
						out.print("<input type=\"checkbox\" name=\""+ticketId
								+"\" value=\""+htmlByEscaped(aoText)+"\"");
						
						if ( Commons.hasFlag(value, aoText)) {
							out.print(" checked ");
							if ( autofocus )
								out.println(" autofocus ");

						}
						
						
						out.print(">"+htmlByEscaped(aoLabel)+"<br>");						
						
					}
					
					// map.put ( aoText, aoLabel);		

					
				}

			}		
			

			/*
			out.println("<input type=\""+"text"+"\""+
			" name=\""+ticketId+"\""+
			" type=\"text\"");
			
			if ( value!=null ) {
				out.println(" value=\""+html(value)+"\"");
			}
			out.println(" />"); // end of input
			*/
			if (!editorOnly) 
			out.print("</div>");		
	}	
	
	public void reflectToMemory(IInputRequestor request) throws Exception {
		if ( !multi) {
			// String parametername = ticketId;
			String valueFromForm = request.getParameter(ticketId);
			if (valueFromForm == null)
				valueFromForm ="";
			if ( valueFromForm.equals("sys_empty"))
				valueFromForm = "";			
			/*		
			Sp.p("TkTicketSel.reflectToMemory("+ticketId+") found value ["+valueFromForm+"] for ticketid ["+ticketId+"]");

			
			Sp.p("Reflecting to memory "+var_type+","+var_id+"="+valueFromForm);
		*/
			reflectToMemo(var_type, var_id,valueFromForm);
		} else {
			String collected = "";
			String[] selectedBoxes = request.getParameterValues(ticketId);

			if ( selectedBoxes != null ) {
				try {
					for (String s: selectedBoxes)
					{
					  //Sp.p("S=|"+s+"|");
					  collected = collected+s+" "; // collected+Entities.importEntities(s)+" ";
					}
				} catch (Exception e) {
					Sp.p("soeinmist: "+e.getMessage());
					Sp.pe(e);
				}				
			}

			
			//Sp.p("Reflecting to memory "+var_type+","+var_id+"="+collected);

			reflectToMemo(var_type, var_id,collected);
		}

	}	
	
	public synchronized String getSumLabelEscaped() {
		//Sp.p("Q0 ");
		printKeyset();
		//Sp.p("Q0b");
		String ret = "";
		try {

			ret = getVarFromMemoryEOnJovial();
			//Sp.p("getSumLabel works on base of value: ["+ret+"] based on ("+var_type+","+var_id+")");
			if ( ret.equals("") ) ;
			else if ( multi ){
				String[] splittet = ret.split("\\s+");
				ret = "";
				for ( String x : splittet ) {
					if ( !ret.equals(""))
						ret = ret+", ";
					
					String foundLabel = getLabelOfCurrrentSelectionSingle (x);
	
						ret = ret+foundLabel;				
				}
			} else {
				ret = getLabelOfCurrrentSelectionSingle(ret);
				
			}
				
		} catch (Exception e) {
			ret = "";
		}
		//Sp.p("Q1 ret="+ret);
		return ret;			

	}	
	private void printKeyset() {
		try {
			if (map==null)
				throw new Exception("map is null");
			Set<String> keyset = map.keySet();
			Iterator<String> iterator = keyset.iterator();
			while ( iterator.hasNext() ) {
				String name  = iterator.next();
				String label = map.get(name);
				//Sp.p("key="+name+" label="+label);
			}
		} catch (Exception e) {
			Sp.p("problem with printKeyset "+e.getLocalizedMessage());
			Sp.pe(e);
		}
	}
	private  String getLabelOfCurrrentSelectionSingle ( 
			String currentValue) {
		Sp.p("getLabelOfCurrrentSelectionSingle: "+currentValue);
		// if ( currentValue == null ) return null;	
		String label = null;
		try {
			label= map.get(currentValue);
			//Sp.p("Q2 label="+label);
			if ( label== null ) label = currentValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			label = currentValue;
			//Sp.p("Q3 label="+label);
			Sp.pe(e);
		}
		return label;
	}
}
