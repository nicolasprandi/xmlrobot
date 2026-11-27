/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class InterLoopXml implements IInterLoop {

ILogConnector lc ;
LogConnector lcd = new LogConnector("");	

private String idE = null;
private String idN = null;
private String filename = null;
private String spirit_usedN = null;
private String iteratortext = null;
private List<Element> list = null;
private Iterator<Element> iterator = null;
private Element aktElement = null;
public int pos = 0;
public int length = -1;
private int limit = -1;
private boolean flag_dont_escape_on_read;

private ILogConnectorLogic lcl=null;
IRequestHandler rh = null;

int missing_strategy = 2;
private static final int MISSING_STRATEGY_EMPTY = 0;
private static final int MISSING_STRATEGY_EXCEPTION = 1;
private static final int MISSING_STRATEGY_JOVIAL = 2;
private static KeyWordMapper missingtypes;

private static final int act_i   = 0;
private static final int act_max   = 1;
private static final int act_name   = 2;
private static KeyWordMapper tasknames;

private static final int tra_klammer   = 0;
private static final int tra_i   = 1;
private static final int tra_max   = 2;
private static final int tra_name   = 3;
private static final int tra_xml   = 4;
private static final int tra_attributes   = 5;

private static KeyWordMapper tranames;

// todo nic321
/*
public boolean hasMissingStrategyEXCEPTION() {
	if ( missing_strategy == MISSING_STRATEGY_EXCEPTION)
		return true;
	else
		return false;
} */

public InterLoopXml (
		Element elementN, 

		ILogConnectorLogic lcl, 
		IRequestHandler rh) throws Exception {
	// element is in natural
	// 1, variablen belegen
	idN = elementN.getAttributeValue("id");
	if ( idN== null )
		throw new Exception("must specify @id for loop-XmlXpath");
	else if ( idN.startsWith("sys_"))
		throw new LangException(234,"Loops can't have an id starting with 'sys_' but found: "
				+ idN);
	idE = Entities.importEntities(idN);
	/*
	 if ( Entities.stringContainsEntityCharacter(id))
		throw new Exception("Xpath-Loop-ids are not allowed to have an entity, found id="+id);
	*/
	filename = elementN.getAttributeValue("xml");

	spirit_usedN = elementN.getAttributeValue("spirit");
	iteratortext = elementN.getAttributeValue("xpath");
	this.rh = rh;
	
	if (elementN.getAttribute("missing_strategy")==null )
		missing_strategy = MISSING_STRATEGY_JOVIAL;
	else
		missing_strategy = missingtypes.convert(elementN.getAttributeValue("missing_strategy"));
	
	
	
	this.lcl = lcl;
	//this.lcl.setLogicHeader(idN+".loop_xml");
	this.lcl.setLogicHeader(idN);
	this.lc = lc;
	
	if ( ( filename == null || filename.length() == 0 ) && ( spirit_usedN == null || spirit_usedN.length() == 0 ) )
		throw new Exception("must specify @xml or @spirit for loop-XmlXpath");
	
	if ( iteratortext == null )
		throw new Exception("must specify @xpath for loop-XmlXpath");
	
	if ( elementN.getAttribute("limit") != null ) {
		limit = Commons.string2int(elementN.getAttributeValue("limit"));
	}
		
	// 2, xml-file oeffnen
	Document document;
	Element root;
	if ( filename !=null && (filename.trim()).length()>0 ) {
		// read data from xml-file
		document = CommonsVfs.getXmldocumentByFilename(filename,rh.getSitzung());
		root = document.getRootElement();	
		
	} else {
		// connect to a sprit
		CoopSpirit spirit = (CoopSpirit)rh.getNo(spirit_usedN,  Wellknown.coop_type_spirit);
		if ( spirit == null ) 
			throw new Exception("The spirit with id '"+spirit_usedN+"' was not found");
		document = spirit.getDoc();
		root = spirit.getRoot();			
	}

	// getting the result-set
	list = Commons.xpathSelectNodes(root,iteratortext);
	
	// sorting if requested
	if ( elementN.getAttribute("sort") != null &&  ((elementN.getAttributeValue("sort")).trim()).length()>0 ) {

		ElementComparator ec = new ElementComparator(
										elementN.getAttributeValue("sort"),
										elementN.getAttributeValue("number"),
										elementN.getAttributeValue("ignore_case"),
										elementN.getAttributeValue("desc"));
		
		
		try {
			List<Element> newList = new ArrayList<Element>(list);
			Collections.sort(newList,ec);
			list = newList;
		} catch (Exception e) {
			throw new Exception("The sort did fail in xpath-loop '"+idE+
					"', [Original Expection-Class:] "+
					e.getClass().getCanonicalName()+
					" [Original Message:]+"+e.getLocalizedMessage()
					);
		}			
	}
	
	flag_dont_escape_on_read = Commons.hasFlag(elementN, "dont_escape_on_read");
	// flag "back"
	if ( Commons.hasFlag(elementN, "back")) {
		ArrayList<Element> newList = new ArrayList<Element>();
		for ( int i = list.size(); i >=1 ; i -- ) {
			Element cur = (Element)list.get(i-1);
		    newList.add(cur);
		}
		list = newList;					
	}

	iterator = list.iterator();
	length = list.size();
	lcl.logicStart("(a)","Started, Number of Elements="+length);
	
	// lc.core(name+" started ...",LogConnector.CORE_STRUCTURE);

}
public Element getCurrentElement() {
	return aktElement;
}
public boolean hasNext() {
	
	// considering the limit-attribute
	if ( limit != -1 && pos==limit) { 
		// lc.core(name+" stopped by reaching the max.",LogConnector.CORE_STRUCTURE);
		lcl.logicEnd("(z)","Finished, by reaching max of "+limit);			
		return false;
	}
	
	boolean goon =iterator.hasNext();

	if (!goon) {
		// lc.core(name+" finished.",LogConnector.CORE_STRUCTURE);
		lcl.logicEnd("(z)","Finished.");
	} else {
		// lc.core(name+pos+") next iteration",LogConnector.CORE_STRUCTURE);
		lcl.logic("("+Integer.toString(pos+1)+")",null);
	} return (goon);		
}
	
// Fetching the next one, called from cooperation
public void next() throws Exception {		
	try {
		aktElement = (Element)iterator.next();
		pos++;
	} catch (java.util.NoSuchElementException e) {
		throw new LangException(235,"You probably did a next on the xpath-loop '"+
				idE +"' but there is nothing more. Check your while-attribute and escape that! "
				+" Original Exception: java.util.NoSuchElementException, Message:"+e.getMessage());
	}
}

// called from Cooperation to get a value, 
public String lxValue ( ArrayList<Vcar> vcar_list ) throws Exception {
	// normaler value, ohne default wert
	// wird von der cooperatation-loop verwendet
	// gives back a natural value!
	// 1= loop-id
	String def = null;
	String r = def;
	boolean has_user_data = false;
	if ( vcar_list.size() == 2 ) {
		String x = ((Vcar)vcar_list.get(1)).s;
		// nur ein wert vorhanden
		if ( x.startsWith("@") ) {
			r = aktElement.getAttributeValue(x.substring(1));	
			if ( r == null ) r=def;
			has_user_data = true;
		} else {
			int ac = tasknames.convertRes(x);

			switch ( ac ) {
			case act_i: {	
				r = Commons.int2String(pos);
				break;
			}
			case act_max: {	
				r = Commons.int2String(length);
				break;
			}
			case act_name: {	
				r = aktElement.getName();	
				has_user_data = true;
				break;
			}

			default: { 
				// das ist wieder ein path
				Element y = Commons.xpathSelectSingleNode(aktElement, x);
				if ( y== null ) {
					// lcl.warn("Could not find node: "+x);
					r = def;
				} else {
					r = y.getText();
				}	
				has_user_data = true;
			  	break;

			}
			} 
		}
					
		
	} else if ( vcar_list.size() == 3 ) {
		has_user_data = true;
		// 2 werte vorhanden erster ist path, zweiter ist attribut
		String x = ((Vcar)vcar_list.get(1)).s;
		String a = ((Vcar)vcar_list.get(2)).s;
		if ( a.startsWith("@"))
			a = a.substring(1);
		else
			throw new Exception("LoopXml: 3rd argument must be an attribute and therefore begin with an @");

		Element y = Commons.xpathSelectSingleNode(aktElement, x);
		if ( y== null ) {
			r = def;
		} else {
			r = y.getAttributeValue(a);
			if ( r == null ) r=def;
		}
	} else if ( vcar_list.size() == 1 ) {
		// kein wert vorhanden ->text
		has_user_data = true;
		r = aktElement.getText();
	} else {
		throw new Exception("request does not fit, lx gets more than 4 parameters");
	}

	if ( r== null)
		return null;
	else if ( flag_dont_escape_on_read ) {
		// since we have to give here back natural but it is a friend we do export
		if ( has_user_data)
			return Entities.exportEntities(r);
		else
			return r;
		
	} else {
		// escaping is done by the function exit, not here
		// we give here back natural
		return r;
	}
}

public String lxExists ( ArrayList<Vcar> vcar_list ) throws Exception {
	// normaler value, ohne default wert
	// wird von der cooperatation-loop verwendet
	// 1= loop-id
	// 2= exists?
	String r = "0";
	if ( vcar_list.size() == 3 ) {
		String x = ((Vcar)vcar_list.get(2)).s;
		// nur ein wert vorhanden
		if ( x.startsWith("@") ) {
			//attribute
			if ( aktElement.getAttribute(x.substring(1)) != null ) 
				r="1";
		} else {
			// xpath only
			Element y = Commons.xpathSelectSingleNode(aktElement, x);
			if ( y!= null ) {
				r = "1";
			}	
		}		
		
	} else if ( vcar_list.size() == 4 ) {
		// 2 werte vorhanden 
		//   1: xpath
		//   2: @attribut
		String x = ((Vcar)vcar_list.get(2)).s;
		String a = ((Vcar)vcar_list.get(3)).s;
		if ( a.startsWith("@"))
			a = a.substring(1);
		else
			throw new Exception("LoopXml: fourth argument must be an attribute and therefore begin with an @");

		Element y = Commons.xpathSelectSingleNode(aktElement, x);
		if ( y!= null ) {
			if ( y.getAttribute(a) != null)
				r = "1";
		}
	} else if ( vcar_list.size() == 2 ) {
		// kein wert vorhanden ->text
		if ( aktElement.getText() != null )
			r="1";
	} else {
		throw new Exception("request does not fit, lx gets more than 4 parameters");
	}

	return r;
	// return (new Vcar(r));	
}

// translate is called by the interpreter for plain xml-loops
public String translate ( String i ) throws Exception {
	
	// wird vom interpreter aufgerufen
	// gives back null if not applicable
	String r = null;
	
	//  lc.info("Start translate for "+i);
	 
	// lc.info("***** loop-translate input:"+i);
	int klammerauf = i.indexOf("(loop,"+idE);
	if ( klammerauf == -1 ) {
		return null;			
	}
	
	int klammerzu = Commons.getClosingBracket(i, klammerauf+6);
	if ( klammerzu == -1 )
		throw new LangException(236,"Xpath-Loop '"+idN+"' didnt find closing bracket in: "+i);
		//return i;
	
	String x = new String ( i.substring(klammerauf+6+idE.length(),klammerzu+1));
	
	// this is a flag for performance, if a value is returned that cant have entites at all
	// then the my_import_entities will be false
	// examle is the number of iterations #i or #max, but also the exists? returns
	boolean has_user_data = false;
	
	int ac = tranames.convertRes(x);

	switch ( ac ) {
	case tra_klammer: {	
		r = aktElement.getText();	
		has_user_data = true;
		break;
	}
	case tra_i: {	
		r = Commons.int2String(pos);
		break;
	}
	case tra_max: {	
		r = Commons.int2String(length);
		break;
	}
	case tra_name: {	
		r = aktElement.getName();
		has_user_data = true;
		break;
	}
	case tra_xml: {	
		r = Commons.toText(aktElement);
		has_user_data = true;
		break;
	}
	case tra_attributes: {	
		r = "";
		List<?> attributes = aktElement.getAttributes();
		Iterator<?> iy = attributes.iterator();
		while ( iy.hasNext()) {
			Attribute ey = (Attribute)iy.next();
			r = r+ey.getName()+" ";
		}		
		has_user_data = true;
		break;
	}

	default: { 	
		if ( x.startsWith(",exists?")) { 
			String x2 = x.substring(8);
			
			x=null;
			
			if ( x2.startsWith(",@") ) {
				// exists: attribute only
				if ( aktElement.getAttribute(x2.substring(2, x2.length()-1)) == null )
					r = "0";
				else
					r = "1";
				
			} else if ( x2.lastIndexOf(",@") > -1 ) {
				// exists: attribute and xpath
				
				int cut = x2.lastIndexOf(",@");
				String xp = x2.substring(1, cut);
				String at = x2.substring(cut+2,x2.length()-1);
				//System.out.println("da sollten wir sein ..............");

				Element y = Commons.xpathSelectSingleNode(aktElement, xp);
				if ( y== null ) {
					r = "0";
				} else {
					if ( y.getAttribute(at) == null )
						r = "0";
					else
						r = "1";
				}	
				
			} else {
				// exists: xpath only
				String xp = x2.substring(1,x2.length()-1);
				Element y = Commons.xpathSelectSingleNode(aktElement, xp);
				if ( y== null ) {
					r = "0";
				} else {
					r = "1";
				}						
			}
			
		} else if ( x.startsWith(",@") ) {
			// value: attribute only
			r = aktElement.getAttributeValue(x.substring(2, x.length()-1));	
			if (r==null) r = getMissingStrategy(i);
			has_user_data = true;
		} else if ( x.lastIndexOf(",@") > -1 ) {
			// value: attribute and xpath
			int cut = x.lastIndexOf(",@");
			String xp = x.substring(1, cut);
			String at = x.substring(cut+2,x.length()-1);

			Element y = Commons.xpathSelectSingleNode(aktElement, xp);
			if ( y== null ) {
				// apply missing strategy if node not found
				r=getMissingStrategy(i+", node not found");
			} else {
				r = y.getAttributeValue(at);
				// apply missing strategy if attribute not found
				if (r==null) r = getMissingStrategy(i+", attribute not found");
			}	
			has_user_data = true;
		} else {
			// value: xpath only
			String xp = x.substring(1,x.length()-1);
			Element y = Commons.xpathSelectSingleNode(aktElement, xp);
			if ( y== null ) {
				r = getMissingStrategy(i+", node not found");
			} else {
				r = y.getText();
			}		
			has_user_data = true;
			
		}
		break;

	}
	}  
	
	
	
	// The retured value must return escaped entities
	// if it is a friend (flag friend) they are already escaped and the import is not needed
	if ( r!=null && r.length()>0 && !flag_dont_escape_on_read && has_user_data)
		r = Entities.importEntities(r);	
	return r;
	
}
	
public String getMissingStrategy(String message ) throws Exception {
	if ( missing_strategy == MISSING_STRATEGY_EMPTY )
		return "";
	else if ( missing_strategy == MISSING_STRATEGY_JOVIAL ) {
		if ( rh.isJovial())
			return "";
		else 
			throw new Exception ( "We are not jovial. Xml-Loop [id="+idE+"] didnt delivered a value: "+ message);
	} else if ( missing_strategy == MISSING_STRATEGY_EXCEPTION ) {
		throw new Exception ( "Xml-Loop [id="+idE+"] didnt delivered a value: "+ message);
	} else {
		throw new Exception ( "Houston, we have a problem. call the boss!");
	}
}
	
public void debugx(int index, Level level) {
	if ( index == 0 )
		lcd.log(Interpreter.declareDebug
				+Commons.trailBlanks("loop",Interpreter.lb)
				+Commons.leadBlanks(idE, Interpreter.lb2)
				+"= "
				+filename
				+","
				+iteratortext
				, level);
	else
		lcd.log(Interpreter.declareDebug
				+Commons.trailBlanks("",Interpreter.lb)
				+Commons.leadBlanks(idE, Interpreter.lb2)
				+"= "
				+filename
				+","
				+iteratortext
				, level);				
}	
	
	private class ElementComparator implements Comparator<Object> {
		String attribute_names[];
		boolean is_number[];
		boolean is_attribute[];
		boolean ignore_case[];
		boolean desc[];

		ElementComparator(String _attribute_name, String _number, String _ignore_case, String _desc) throws Exception {
			
			attribute_names = _attribute_name.split("\\s+");			
			
			is_number = new boolean[attribute_names.length];
			ignore_case = new boolean[attribute_names.length];
			is_attribute = new boolean[attribute_names.length];
			desc = new boolean[attribute_names.length];
			for (int i = 0; i < attribute_names.length; i++) {
				if ( _number != null )
					is_number[i] = Commons.hasFlag(_number, attribute_names[i]);
				else
					is_number[i] = false;
				
				
				if ( _ignore_case != null )
					ignore_case[i] = Commons.hasFlag(_ignore_case, attribute_names[i]);
				else
					ignore_case[i] = false;
				
				if ( _desc != null )
					desc[i] = Commons.hasFlag(_desc, attribute_names[i]);
				else
					desc[i] = false;
				
				if ( attribute_names[i].startsWith("@") ) {
					is_attribute[i] = true;
					attribute_names[i] = attribute_names[i].substring(1);
				} else if ( attribute_names[i].equals("text") )
					is_attribute[i] = false;
				else
					throw new Exception("Unknown sort-element: ["+attribute_names[i]+"], use an @ for attributes or text!");
								
			}
			
		}

		// char char_null = '0';
		/*private String strRepeatNull(int repeat ) {
			StringBuilder buf = new StringBuilder(repeat);
			for ( int i=0;i<repeat;i++) {
				buf.append(char_null);
			}
			return buf.toString();
		} */
		/*
		char char_space = ' ';
	
		private String strRepeatSpace(int repeat ) {
			StringBuilder buf = new StringBuilder(repeat);
			for ( int i=0;i<repeat;i++) {
				buf.append(char_space);
			}
			return buf.toString();
		}		*/
		
		@Override
		public int compare(Object o1, Object o2) {
			
			int comp = 0;
			Element e1 = (Element)o1;
			Element e2 = (Element)o2;			

			for (int i = 0; i < attribute_names.length; i++) {	
				
				String a1;
				String a2;
				if ( is_attribute[i]) {
					a1 = e1.getAttributeValue(attribute_names[i]);
					a2 = e2.getAttributeValue(attribute_names[i]);					
				} else {
					a1 = e1.getText();
					a2 = e2.getText();
				}
				
				
				if ( a1==null) a1=new String("");
				if ( a2==null) a2=new String("");
				
				
				if ( !is_number[i] ) {
										
					if ( !ignore_case[i] && !desc[i])
						comp = a1.compareTo(a2);
					else if ( ignore_case[i] && desc[i])
						comp = a2.compareToIgnoreCase(a1);
					else if ( ignore_case[i] )
						comp = a1.compareToIgnoreCase(a2);
					else
						comp = a2.compareTo(a1);
					
					if ( comp != 0 )
						return comp;		
					
				} else {

					double d1;
					try {
						d1 = Double.parseDouble(a1);
					} catch (NumberFormatException e) {
						d1=0;
					}
					double d2;
					try {
						d2 = Double.parseDouble(a2);
					} catch (NumberFormatException e) {
						d2=0;
					}
					
					if ( !desc[i]) {						
						comp = Double.compare(d1, d2);
					} else
						comp = Double.compare(d2, d1);
					
					
					if ( comp != 0 )
						return comp;
				}
				
			}

			return 0;

			
		}
		
	}	
	/*
	private static int compareDouble ( double a, double b ) {
		if ( a==b) return 0;
		else if ( a > b ) return 1;
		return -1;
	}
	*/

	public static void staticStartup() {
		missingtypes = new KeyWordMapper("InterLoopXml-Missing",3);
		missingtypes.put("empty", MISSING_STRATEGY_EMPTY);
		missingtypes.put("exception", MISSING_STRATEGY_EXCEPTION);
		missingtypes.put("jovial_like_interpreter", MISSING_STRATEGY_JOVIAL);
		missingtypes.putFinished();	
		
		tasknames = new KeyWordMapper("InterLoopXml-LxValue",3);
		tasknames.put("#i", act_i);
		tasknames.put("#max", act_max);
		tasknames.put("$name", act_name);
		tasknames.putFinished();	
		
		tranames = new KeyWordMapper("InterLoopXml-Translate",6);
		tranames.put(")", tra_klammer);
		tranames.put(",#i)", tra_i);
		tranames.put(",#max)", tra_max);
		tranames.put(",$name)", tra_name);
		tranames.put(",xml?)", tra_xml);
		tranames.put(",attributes?)", tra_attributes);
		tranames.putFinished();		
	}	
}