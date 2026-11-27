/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class Xml_Update implements ITask{
	private static HashMap<String, Integer> cmdm = null; 
	private static final int c_set_attribute = 		0;
	private static final int c_set_text = 			1;
	private static final int c_remove_attribute = 	2;
	private static final int c_remove = 			3;
	private static final int c_needs_with_starting_here = 	4;
	private static final int c_append = 			4;
	private static final int c_insert_in = 			5;
	private static final int c_insert_in_begin = 	6;
	private static final int c_insert_after = 		7;
	private static final int c_insert_before = 		8;
	private static final int c_replace = 			9;
	
	
	String 	what;
	String	out;

	String	with;
	String	where;
	String 	with_what;
	String 	attribute_name;
	String	attribute_value;
	String	text_value;


	String tname;
	
	Document doc ;
	Element root;
	Element currentElement =null;
	Element e;
	IRequestHandler rh;
	
	private static void createCmdm () {
		cmdm=new HashMap<String, Integer>();
		cmdm.put("set_attribute", new Integer(c_set_attribute));
		cmdm.put("a", new Integer(c_set_attribute));
		cmdm.put("set_text", new Integer(c_set_text));
		cmdm.put("t", new Integer(c_set_text));
		
		cmdm.put("remove_attribute", new Integer(c_remove_attribute));
		cmdm.put("append", new Integer(c_append));
		cmdm.put("insert_in", new Integer(c_insert_in));
		cmdm.put("insert_in_begin", new Integer(c_insert_in_begin));
		cmdm.put("insert_after", new Integer(c_insert_after));
		cmdm.put("insert_before", new Integer(c_insert_before));
		cmdm.put("replace", new Integer(c_replace));
		cmdm.put("remove", new Integer(c_remove));				
	}
	
	private int doMiddle() throws Exception {
		if ( cmdm == null )
			createCmdm();

		String 	howx = 		para2stringFast(e.getAttributeValue("how"),null);
		if ( howx == null )
			throw new LangException(508,"xml_update needs attribute 'how' with append/insert_in/insert_in_begin/insert_before/insert_after/replace/remove/set_attribute/remove_attribute/set_text");
		Integer i_cmd = cmdm.get(howx);
		if ( i_cmd == null )
			throw new LangException(509,"xml_update has wrong attribute 'how', found: "+howx);
		int how_c = i_cmd.intValue();
		
		boolean multi = Commons.hasFlag(e, "multi");
		if ( Commons.hasFlag(e, "many"))
			multi = true;
		boolean any = Commons.hasFlag(e, "any");
		boolean noupdate = Commons.hasFlag(e, "noupdate");
		boolean flag_remove = Commons.hasFlag(e, "remove");	
		if ( Commons.hasFlag(e, "with_friend") )
			throw new Exception("xml-update middle verwendet jemand task_friend");
		boolean traceOn = false; //lc.traceIsOn();
		

		where = 	para2stringFast(e.getAttributeValue("where"),null);	
		with = 		para2stringFast(e.getAttributeValue("with"),null);
		with_what = para2stringFast(e.getAttributeValue("with_what"),"root");
		
		attribute_name= para2stringFast(e.getAttributeValue("attribute"),null);
		attribute_value= para2stringFast(e.getAttributeValue("value"),null);	
		text_value= para2stringFast(e.getAttributeValue("value"),null);
		
		if ( traceOn ) {
			/*
			lc.log_ext("how             = "+howx, this);
			lc.log_ext("where           = "+where, this);
			lc.log_ext("with            = "+with, this);
			lc.log_ext("with_what       = "+with_what, this);
			lc.log_ext("attribute_name  = "+attribute_name, this);
			lc.log_ext("attribute_value = "+attribute_value, this);
			lc.log_ext("text_value      = "+text_value, this);
			lc.log_ext("flag multi/many = "+multi, this);
			lc.log_ext("flag any        = "+any, this);
			lc.log_ext("flag noupdate   = "+noupdate, this);
			lc.log_ext("flag remove     = "+flag_remove, this);
			*/
		}
				
		
		if ( where == null && currentElement!=null ); // directly on the current element, where is optional
		else if ( where == null && how_c != c_append)
			throw new LangException(510,"xml_update: Set the attribute 'where' for every operation different to 'append'; use a dot . for the root element"); // where is always needed except for append and spirit selection
		
		if ( with == null &&
			how_c >= c_needs_with_starting_here )
			throw new LangException(511,"xml_upate: Set the attribute 'with' when how='"+howx+"'");	
		
		int ret = -1;
		if ( how_c == c_append) {
			// the append
			Document docWith = CommonsVfs.getXmldocumentByFilename(with,rh.getSitzung());
			
			if ( with_what.equals("root")) {
				// daten-raus
				root.addContent(docWith.getRootElement().clone());
				// Sp.p("Dort wo wir hinwollen (2)"+Commons.toText(root));
				ret = 1;
			} else if ( with_what.equals("childs")) {
				ArrayList<Element> childsWith = new ArrayList<Element>();
				List<Element> cList = docWith.getRootElement().getChildren();
				Iterator<Element> i = cList.iterator();
				while ( i.hasNext()) {
					childsWith.add( ((Element)i.next()).clone() );
				}
				if ( childsWith.size() == 0 ) {
					//lc.warn("No childs found to insert",this);
				}
				// daten-raus
				root.addContent(childsWith);
				ret = 1;
			} else 
				throw new LangException(512,"xml_update 'with_what' not correct. shoud be 'root' or 'childs'. but it is '"+with_what+"'");
			
		} else {
			// String xpath2x = where;
			// here is everything except append
			List<Element> foundList = null;
			
			if ( currentElement == null ) {
				// does not come from spirit, validation already done above ...
				foundList = Commons.xpathSelectNodes(root, where);
				// Sp.p("A xpath select nodes, node : "+root);				
				// Sp.p("A xpath select nodes, node : "+Commons.toText(root));
				// Sp.p("                      where: "+where);
				// Sp.p("                      found: "+foundList.size());
				
			} else {
				// Sp.p("BBB");
				// has currentElement, comes from spirit, currentElement is the location of an loop_xml_coop
				if ( where == null ) {
					// no additional where
					foundList = new ArrayList<Element>();
					foundList.add(currentElement);
				} else {
					// additional where
					// Sp.p("going into additional where: "+where+" on current element: "+Commons.toText(currentElement));
					foundList = Commons.xpathSelectNodes(currentElement, where);
					// Sp.p("B xpath select nodes, node : "+root);
					// Sp.p("B xpath select nodes, node : "+Commons.toText(root));
					// Sp.p("                      where: "+where);
					// Sp.p("                      found: "+foundList.size());
				}
			}
			
			if ( any );
			else if ( foundList==null || foundList.size()==0  ) {
				if ( what != null)
					throw new Exception("xml_update: node not found, where='"+where+"' xml='"+what+"'");
				else
					throw new Exception("spirit/xml_update: node not found, where='"+where+"'");
			} else if ( foundList.size()>1 && !multi) {
				if (traceOn);// lc.log_ext("A number of nodes ("+foundList.size()+") found, according to not setting the mulit-flag, we take only the first one",this);			
			}
			
			ret = foundList.size();
			Iterator<Element> foundIterator = foundList.iterator();
			int found_index = 1;
			while ( foundIterator.hasNext() ) {
				
				if ( found_index > 1 && (!multi && !any)) {
					ret = 1;
					//if (traceOn) lc.log_ext(foundList.size()+" nodes found, according to not setting the mulit-flag, we take only the first one",this);
					break;
				}
				
				String postext=null;
				if (traceOn) postext="["+found_index+"] ";
				
				Element foundWhere = (Element)foundIterator.next();
				//if (traceOn) lc.log_ext(postext+"selected node :"+Commons.toText(foundWhere),this);
				
				Element parent = null;
				int posInParent = -1;
				
				if ( how_c == c_set_attribute ||
					 how_c == c_set_text ||
					 how_c == c_insert_in ||
					 how_c == c_insert_in_begin
						 ) /* we dont need a parent for these ones */ ;
				else {
					parent = foundWhere.getParentElement();
					if ( parent != null ) {
						posInParent = parent.indexOf(foundWhere);		
						//if (traceOn) lc.log_ext(postext+"position of node in parent :"+posInParent +" with content: "+parent.toString(),this);				
					} else {
						throw new Exception("xml_update did not find the requested-parent node of "+where);
					}					
				}
	
				// with
				boolean mode_root = false;
				Element rootWith = null ;
				ArrayList<Element> childsWith = null;
				if (how_c >= c_needs_with_starting_here ){
					// these need a with-attribute, they get data from a file
					Document docWith = CommonsVfs.getXmldocumentByFilename(with,rh.getSitzung());
					
					if ( with_what.equals("root")) {
						mode_root = true;
						rootWith = docWith.getRootElement().clone();
						
					} else if ( with_what.equals("childs")) {
						mode_root = false;
						childsWith = new ArrayList<Element>();
						List<Element> cList = docWith.getRootElement().getChildren();
						Iterator<Element> i = cList.iterator();
						while ( i.hasNext()) {
							childsWith.add( ((Element)i.next()).clone() );
						}
						if ( childsWith.size() == 0 ) {
							// lc.warn(postext+"No childs found to insert",this);
						}
					} else 
						throw new LangException(513,postext+"'with_what' not correct. shoud be 'root' or 'childs'. but it is '"+with_what+"'");
				}

				switch ( how_c ) {
				
					case c_set_attribute : {
						if ( !noupdate || foundWhere.getAttribute(attribute_name)==null) {
							// attribute does not exist yet OR update should be done
							// flag noupdate is done now
							if ( flag_remove && attribute_value.length()==0) {
								// remove possible since the new value is an empty string
								if ( foundWhere.getAttribute(attribute_name)!=null) {
									foundWhere.removeAttribute(attribute_name);
									// if (traceOn) lc.log_ext("Removed Attribute ["+attribute_name+"] with ["+attribute_value+"] because flag [noupdate] is set and there is already a value.", this);
								}
									// else: attribute does not exist anyway, no remove nessessary
							} else {
								foundWhere.setAttribute(attribute_name, attribute_value);
							}
						} else {
							// no update needed if flag 'noupdate'
							// if (traceOn) lc.log_ext("Didn't update attribute ["+attribute_name+"] with ["+attribute_value+"] because flag [noupdate] is set and there is already a value.", this);
						}
						break;
					}
					
					case c_set_text : {
						foundWhere.setText(text_value);
						break;
					}
					
					case c_insert_in : {
						if ( mode_root )
							foundWhere.addContent( rootWith);
						else
							foundWhere.addContent(childsWith);						
						break;
					}
					
					case c_insert_after : {
						if ( mode_root )
							parent.addContent(posInParent+1, rootWith);
						else
							parent.addContent(posInParent+1, childsWith);						
						break;
					}	
					case c_insert_before : {
						if ( mode_root )
							parent.addContent(posInParent, rootWith);
						else
							parent.addContent(posInParent, childsWith);						
						break;
					}	
					case c_insert_in_begin : {
						if ( mode_root )
							foundWhere.addContent(0, rootWith);
						else
							foundWhere.addContent(0, childsWith);						
						break;
					}	
					case c_replace : {
						if ( mode_root ) {
							parent.addContent(posInParent+1, rootWith);
							parent.removeContent(posInParent);
						} else {
							parent.addContent(posInParent+1, childsWith);
							parent.removeContent(posInParent);
						}						
						break;
					}	
					case c_remove : {
						parent.removeContent(posInParent);
						break;
					}	
					case c_remove_attribute : {
						if ( foundWhere.getAttribute(attribute_name)==null) {
							// if (traceOn) lc.warn(postext+"The attribute '"+attribute_name+"' does not exist",this);
						} else {
							foundWhere.removeAttribute(attribute_name);	
							// Sp.p("removing attriubute "+attribute_name);
						}
						break;
					}	
					
					default: { 
						throw new LangException(514,postext+"'how' not correct. shoud be: append, insert_after, insert_before, insert_in, insert_in_begin, replace, remove, set_attribute, remove_attribute; but it is '"+howx+"'");
						
					}
				}
				
				found_index++;
				
			} // end loop
			
		}		// end: else: append
		return ret;
	}
	
	public int executeSpirit( 
					Element rootElement, 
					Element currentElement,
					Element command, 
					IRequestHandler rh) throws Exception {
		
		e = command;
		this.rh = rh;
		root = rootElement;
		this.currentElement = currentElement;
		return doMiddle();
		
	}
	// boolean friend;
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "xml_update";
		//lc=_lc;
		this.e = e;
		this.rh = rh;
		this.currentElement = null;
		
		what = null;
		if ( e.getAttribute("what") != null )
			what = e.getAttributeValue("what");
		else if ( e.getAttribute("xml") != null )
			what = e.getAttributeValue("xml");
		else if ( e.getAttribute("file") != null )
			what = e.getAttributeValue("file");		

		if ( what == null )
			throw new Exception(getExtensionId()+": Set the attribute 'what'");
		
		out = e.getAttributeValue("out");
		if ( out == null )
			out = what;	
		
		doc = CommonsVfs.getXmldocumentByFilename(what,rh.getSitzung());
		root = doc.getRootElement();		
		
		int count = doMiddle();
			 
		 // Dokument schreiben
		 // Document docO = new Document(root);
		 CommonsVfs.writeToFileFormat(doc, out,Format.getRawFormat(),rh.getSitzung());
		 
		 if ( e.getAttribute("count_prop_id") != null ) {
			 rh.setVar("prop", e.getAttributeValue("count_prop_id"), Integer.toString(count));
			 //Parameters parameters = new Parameters();
			 //parameters.setParameter(e.getAttributeValue("count_prop_id"), Integer.toString(count));
			 return null;
		 } else
			 return null;
	}

	public String getExtensionId() {
		return ( tname);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	
	private static String para2stringFast ( String value, String def ) {
		
		if ( value == null ) {
			return def;
		} else {			
			return value;
		}
		
	}	

}