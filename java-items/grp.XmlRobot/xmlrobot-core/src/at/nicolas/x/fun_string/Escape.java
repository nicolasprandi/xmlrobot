/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Escape implements IFun {


	private static final String funn= "escape";
	private static final String pm= "function 'escape' ";
	private static HashMap<String, Integer> cmdm = null;
	private static final int c__html =  0;
	private static final int c__htmlb = 1;
	private static final int c_html3 =  2;
	private static final int c_html4 =  3;
	private static final int c__xml =   4;
	private static final int c_xml10 =  5;
	private static final int c_xml11 =  6;	
	private static final int c_sys =    7;
	private static final int c__csv =   8;
	private static final int c_csv =    9;
	private static final int c_ecma =  10;
	private static final int c_java =  11;
	private static final int c_json =  12;
	private static final int c_dummy = 13;	
	private static final int c_xslp = 14;	
	public static void startupActions(boolean report) {
		cmdm=new HashMap<String, Integer>();
		cmdm.put("_html", new Integer(c__html));
		cmdm.put("_htmlb", new Integer(c__htmlb));
		cmdm.put("html3", new Integer(c_html3));	
		cmdm.put("html4", new Integer(c_html4));
		cmdm.put("_xml", new Integer(c__xml));
		cmdm.put("xml10", new Integer(c_xml10));	
		cmdm.put("xml11", new Integer(c_xml11));
		cmdm.put("sys", new Integer(c_sys));
		cmdm.put("_csv", new Integer(c__csv));	
		cmdm.put("csv", new Integer(c_csv));
		cmdm.put("ecma", new Integer(c_ecma));
		cmdm.put("java", new Integer(c_java));	
		cmdm.put("json", new Integer(c_json));
		cmdm.put("dummy", new Integer(c_dummy));
		cmdm.put("xslp", new Integer(c_xslp));
	}
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		String type;
		String value;
		boolean special;

		if ( vcar_list.size() == 1 ) {
			type="sys";
			value = ((Vcar)vcar_list.get(0)).s;	
			special = false;
		} else if (vcar_list.size() == 2 || vcar_list.size() == 3 ) {
			type=((Vcar)vcar_list.get(0)).s;
			value = ((Vcar)vcar_list.get(1)).s;
			if ( vcar_list.size() == 3  ) {
				special = ((Vcar)vcar_list.get(2)).getBool();
			} else
				special = false;			
		} else 
			throw new LangException(pm+"wants 1/2/3 parameter: [1=type _xml, sys, ..., default: sys ] [2=value to transform] [3=special treatment of results regarding xmlrobot-entities 0=like usual and default; 1=dont escape xmlrobot-entites afterwards]");
		
		String result = null;
		
		Integer i_cmd = cmdm.get(type);
		int cmd = -1;
		if ( i_cmd != null)
			cmd = i_cmd.intValue();		

		switch ( cmd ) {
			case c__html: {
				result = Commons.text_to_html(value);
				break;
			}
			case c__htmlb: {
				result = Commons.stringReplace(Commons.text_to_html(value)," ","&nbsp;");
				break;
			}
			case c_html3: {
				result = StringEscapeUtils.escapeHtml3(value);
				break;
			}
			case c_html4: {
				result = StringEscapeUtils.escapeHtml4(value);
				break;
			}
			case c__xml: {
				result = Commons.escapeXml(value);
				break;
			}			
			case c_xml10: {
				result = StringEscapeUtils.escapeXml10(value);
				break;
			}
			case c_xml11: {
				result = StringEscapeUtils.escapeXml11(value);
				break;
			}					
			case c_sys: {
				
				result = Entities.importEntities(value);
				
				break;
			}
			case c__csv: {
				result = Commons.stringReplace(value,"\"","\"\"");
				break;
			}	
			case c_xslp: {
				result = Commons.stringReplace(value,"'","''");
				break;
			}				
			case c_csv: {
				result = StringEscapeUtils.escapeCsv(value);
				break;
			}	
			case c_ecma: {
				result = StringEscapeUtils.escapeEcmaScript(value);
				break;
			}	
			case c_java: {
				result = StringEscapeUtils.escapeJava(value);
				break;
			}	
			case c_json: {
				result = StringEscapeUtils.escapeJson(value);
				break;
			}	
			case c_dummy: {
				result = value;
				break;
			}			
			default: {
				throw new LangException(pm+"found wrong type: '"+type+"', allowed: _html, _htmlb, html3, html4, _xml, xml10, xml11, sys, _csv, csv, ecma, java, json, dummy, xslp");
			}
			
		}		
		
	
		Vcar r = new Vcar(result);

		if ( special ) {
			r.dontImport();
			// lc.log_ext("did set special", this);

		}
			
		return ( r );
	}
	

	public String getExtensionId() {
		return funn;
	}
	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}