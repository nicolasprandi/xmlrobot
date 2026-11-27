/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ILogConnectorLogic;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class InterLoopRegex implements IInterLoop{



	private String idN = null;
	private String idE = null;
	private String filename = null;
	private String iteratortext = null;
	private String encoding = null;


	public int pos = 0;

	private int limit = -1;
	private Matcher matcher;
	private int regex_flags = 0;
	
	private ILogConnectorLogic lcl=null;
	private boolean flag_dont_escape_on_read;
	
	private static final int act_klammer   = 0;
	private static final int act_i   = 1;
	private static final int act_start   = 2;
	private static final int act_end   = 3;
	private static final int act_groups   = 4;
	private static final int act_before   = 5;
	private static KeyWordMapper tasknames;	
	
	private static final int pos_i   = 0;
	private static final int pos_start   = 1;
	private static final int pos_end   = 2;
	private static final int pos_groups   = 3;
	private static final int pos_before   = 4;

	private static KeyWordMapper posnames;
	
	public String getIdN () {
		return idN;
	}	
	public InterLoopRegex (
					Element elementN, 
					ILogConnectorLogic lcl,
					ISitzung interLoopRegex) throws Exception {

		this.lcl = lcl;
		//lcl.setLogicHeader(idN+".loop_regex");
		
		
		
		idN = elementN.getAttributeValue("id");
		if ( idN == null )
			throw new LangException(225,"must specify attribute 'id' for loop.regex");	
		else if ( idN.startsWith("sys_"))
			throw new LangException(226,"Loops can't have an id starting with 'sys_' but found: "
					+ idN);
		lcl.setLogicHeader(idN);
			
		idE = Entities.importEntities(idN);
		
		// file
		if ( elementN.getAttribute("text") != null)
			throw new Exception("found attribute text for regex-loop");
		
		filename = elementN.getAttributeValue("file");
		if (filename == null )
			throw new LangException(227,"Must specify attribute 'file' for regex-loop");
		
		flag_dont_escape_on_read = Commons.hasFlag(elementN, "dont_escape_on_read");
		
		if ( elementN.getAttribute("encoding") != null)
			encoding = elementN.getAttributeValue("encoding");
		else
			encoding = "UTF-8";
				
		// regular expression
		iteratortext = elementN.getAttributeValue("regex");
		if ( iteratortext == null )
			throw new LangException("#? must specify @regex for loop-Regex");

		if ( elementN.getAttribute("regex_flags") != null ) {
			regex_flags = Commons.regexFlagsString2int(elementN.getAttributeValue("regex_flags"));
		} else
			regex_flags = 0;
		
		
		if ( elementN.getAttribute("limit") != null ) {
			limit = Commons.string2int(elementN.getAttributeValue("limit"));
		} else
			limit = -1;
				
		// 2, xml-file oeffnen
		Pattern pattern = Pattern.compile(iteratortext,regex_flags);
		value = CommonsVfs.readTextfileFormatLfDefault(filename, encoding,interLoopRegex);
		matcher = pattern.matcher(value);
		

		// lc.core(name+" started ...",LogConnector.CORE_STRUCTURE);
		lcl.logicStart("(a)","Started");

	}
	private String value;
	private int end=0;
	private int start = 0;
	private int last_end=0;
	private int last_start = 0;
	private String rest;
	
	public String getRest() {
		String r;
		if (!flag_dont_escape_on_read)
			r = Entities.importEntities(rest);
		else
			r =rest;

		return r;
	}
	public boolean hasNext() {
		pos++;

		boolean goon = matcher.find();

		if ( limit != -1 && pos>limit) { 
			// lc.core(name+" stopped by reaching the limit.",LogConnector.CORE_STRUCTURE);
			lcl.logicEnd("(z)","Finished, by reaching limit of "+limit);
			goon = false;
		}
		
		if ( goon ) {
			last_end = end;
			last_start = start;
			start = matcher.start();
			end = matcher.end();
			lcl.logic("("+Integer.toString(pos)+")",null);
		} else {
			rest = value.substring(end);
			lcl.logicEnd("(z)","Finished.");
		}		

		return (goon);		
	}
	
	public String translate ( String i ) throws Exception {
		String r = null;
		
		int klammerauf = i.indexOf("(loop,"+idE);
		if ( klammerauf == -1 ) return null;

		
		int klammerzu = Commons.getClosingBracket(i, klammerauf+6);
		if ( klammerzu == -1 ) 
			throw new LangException(228,"Regex-Loop didnt find closing bracket in: "+i);
			// return i;
		
		
		boolean has_user_data = false;
		String x = new String ( i.substring(klammerauf+6+idE.length(),klammerzu+1));
		
		int ac = tasknames.convertRes(x);

		switch ( ac ) {
		case act_klammer: {	
			r = matcher.group();
			if ( r==null)
				r="";
			has_user_data = true;
			break;
		}
		case act_i: {	
			r = Commons.int2String(pos);
			break;
		}
		case act_start: {	
			r = Commons.int2String(matcher.start());
			break;
		}
		case act_end: {	
			r = Commons.int2String(matcher.end());
			break;
		}
		case act_groups: {	
			r = Commons.int2String(matcher.groupCount());
			break;
		}
		case act_before: {	
			int from = last_end;
			int to = matcher.start();
			r = value.substring(from, to)	;	
			has_user_data = true;
			break;
		}

		default: { 	
			// group-value
			
				String wunsch_nummer = x.substring(1,x.length()-1);
				int int_nummer;
				try {
					int_nummer = Integer.parseInt(wunsch_nummer);

				} catch (NumberFormatException e ) {
					throw new Exception("#? Problem with group-expression in loop '"+idN
							+"'. This is not numeric: '"+wunsch_nummer
							+"' tip: you can't currently use a loop-expression as a parameter here. "
							+"define a property before and use that!");
				}
				
				has_user_data = true;
				try {
					r = matcher.group(int_nummer);
				
					if ( r== null)
						r ="";
				} catch (Exception e) {
					throw new LangException(229,"The regex group '"+int_nummer+"' seems not to exist");
				}	
		  	break;
		}
		} 		
		
	
			
		
		if ( !flag_dont_escape_on_read && has_user_data)
			r = Entities.importEntities(r);

		
		return r;
		
	}
	
	public Vcar lrValue ( ArrayList<Vcar> vcar_list ) throws Exception {
		// 1= loop-id
		boolean has_user_data = false;
		String r = null;
		if ( vcar_list.size() == 1 ) {
			// kein wert vorhanden
			r = matcher.group();
			has_user_data = true;
		} else if ( vcar_list.size() == 2 ) {
			String x = ((Vcar)vcar_list.get(1)).s;

			int ac = posnames.convertRes(x);

			switch ( ac ) {
			case pos_i: {	
				r = Commons.int2String(pos);
				break;
			}
			case pos_start: {	
				r = Commons.int2String(matcher.start());
				break;
			}
			case pos_end: {	
				r = Commons.int2String(matcher.end());
				break;
			}
			case pos_groups: {	
				r = Commons.int2String(matcher.groupCount());
				break;
			}
			case pos_before: {	
				int from = last_end;
				int to = matcher.start();
				r = value.substring(from, to)	;	
				has_user_data = true;
				break;
			}

			default: { 	
				int int_nummer;
				try {
					int_nummer = Integer.parseInt(x);

				} catch (NumberFormatException e ) {
					throw new Exception("#123 Problem with group-expression in loop '"+idN
							+"'. This is not numeric: '"+x
							+"' tip: you can't currently use a loop-expression as a parameter here. "
							+"define a property before and use that!");
				}				

				try {
					r = matcher.group(int_nummer);
					has_user_data = true;
				} catch (Exception e) {
					throw new Exception("The regex group '"+x+"' seems not to exist");
				}	
			  	break;

			}
			} 
			
				
			
			
		} else  {
			throw new LangException(230,"Too much parameters ");

		}

		//if ( r!=null )
		//	r = Entities.importEntities(r);
		if ( r==null) r="";
		if ( has_user_data && flag_dont_escape_on_read )
			r = Entities.exportEntities(r);
	
		
		return (new Vcar(r));
		
	}
	
	public void debugx(int index, Level level, LogConnector myLc) {

		if ( index == 0 )
			myLc.log(Interpreter.declareDebug
					+Commons.trailBlanks("loop",Interpreter.lb)
					+Commons.leadBlanks(idE, Interpreter.lb2)
					+"= "
					+filename
					+","
					+iteratortext
					, level);
		else
			myLc.log(Interpreter.declareDebug
					+Commons.trailBlanks("",Interpreter.lb)
					+Commons.leadBlanks(idE, Interpreter.lb2)
					+"= "
					+filename
					+","
					+iteratortext
					, level);				
	}
	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}	
	
	public static void staticStartup() {
		tasknames = new KeyWordMapper("InterLoopRegex",6);
		tasknames.put(")", act_klammer);
		tasknames.put(",#i)", act_i);
		tasknames.put(",#start)", act_start);
		tasknames.put(",#end)", act_end);
		tasknames.put(",#groups)", act_groups);
		tasknames.put(",$before)", act_before);
		tasknames.putFinished();		
		
		posnames = new KeyWordMapper("InterLoopRegex#",5);
		posnames.put("#i", pos_i);
		posnames.put("#start", pos_start);
		posnames.put("#end", pos_end);
		posnames.put("#groups", pos_groups);
		posnames.put("$before", pos_before);
		posnames.putFinished();		
	}
	
	
}