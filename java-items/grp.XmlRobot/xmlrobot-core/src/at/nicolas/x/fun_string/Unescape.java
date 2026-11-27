/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Unescape implements IFun {


	private static final String funn= "unescape";
	private static final String pm= "function 'unescape' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() < 2 || vcar_list.size() > 3 ) 
			throw new LangException(pm+"wants two or tree parameter: 1=type [html,xml,sys] 2=value to transform 3=special treatment of results regarding xmlrobot-entities [0=like usual and default; 1=dont escape xmlrobot-entites afterwards]");
		
		String value = ((Vcar)vcar_list.get(1)).s;
	

		String result = null;
		
		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act__html: {	
			throw new LangException(pm+"states that unescape for _html is not available, use xml");
		
		}
		case act_html3: {	
			result = StringEscapeUtils.unescapeHtml3(value);
			break;
		}
		case act_html4: {	
			result = StringEscapeUtils.unescapeHtml4(value);
			break;
		}
		case act__xml: {	
			result = Commons.unescapeXml(value);
			break;
		}
		case act_xml: {	
			result = StringEscapeUtils.unescapeXml(value);
			break;
		}
		case act_sys: {	
			result = Entities.exportEntities(value);
			break;
		}
		case act__csv: {	
			result = Commons.stringReplace(value,"\"\"","\"");
			break;
		}
		case act_xslp: {	
			result = Commons.stringReplace(value,"''","'");
			break;
		}
		case act_csv: {	
			result = StringEscapeUtils.unescapeCsv(value);
			break;
		}
		case act_ecma: {	
			result = StringEscapeUtils.unescapeEcmaScript(value);
			break;
		}
		case act_java: {	
			result = StringEscapeUtils.unescapeJava(value);
			break;
		}
		case act_json: {	
			result = StringEscapeUtils.unescapeJson(value);
			break;
		}
		case act_dummy: {	
			result = value;
			break;
		}

		}  		
		
		Vcar r = new Vcar(result);
		if ( vcar_list.size() == 3 ) {
			boolean special = ((Vcar)vcar_list.get(2)).getBool();
			if ( special ) {
				r.dontImport();
				//lc.log_ext("did set special", this);
			}
			
		}
		return ( r );
	}
	

	public String getExtensionId() {
		return funn;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Function:Unescape",13);
		tasknames.put("_html", act__html);
		tasknames.put("html3", act_html3);
		tasknames.put("html4", act_html4);
		tasknames.put("_xml", act__xml);
		tasknames.put("xml", act_xml);
		tasknames.put("sys", act_sys);
		tasknames.put("_csv", act__csv);
		tasknames.put("xslp", act_xslp);
		tasknames.put("csv", act_csv);
		tasknames.put("ecma", act_ecma);
		tasknames.put("java", act_java);
		tasknames.put("json", act_json);
		tasknames.put("dummy", act_dummy);
		tasknames.putFinished();		
	}
	private static final int act__html   = 0;
	private static final int act_html3   = 1;
	private static final int act_html4   = 2;
	private static final int act__xml   = 3;
	private static final int act_xml   = 4;
	private static final int act_sys   = 5;
	private static final int act__csv   = 6;
	private static final int act_xslp   = 7;
	private static final int act_csv   = 8;
	private static final int act_ecma   = 9;
	private static final int act_java   = 10;
	private static final int act_json   = 11;
	private static final int act_dummy   = 12;

	private static KeyWordMapper tasknames;
}