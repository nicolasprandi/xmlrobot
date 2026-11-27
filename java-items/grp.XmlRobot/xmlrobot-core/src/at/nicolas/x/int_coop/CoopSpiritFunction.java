/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import at.nicolas.x.fun_string.Xpath;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class CoopSpiritFunction implements IFun{

	private static final int act_xpath   = 0;
	private static final int act_xpathx   = 1;
	private static final int act_xml   = 2;
	private static KeyWordMapper tasknames;
	String funn;
	String pm;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "spirit";
		this.pm = funn+": ";
		
		if ( vcar_list.size() < 2 ) 
			throw new Exception(pm+"I want at least 2 parameters, 1=loop-id,  2=something?? ");

		String first = ((Vcar)vcar_list.get(1)).s;
		String id = ((Vcar)vcar_list.get(0)).s;
		
		CoopSpirit spirit = (CoopSpirit)rh.getNo(id, Wellknown.coop_type_spirit);
		if ( spirit == null ) {
			throw new Exception(pm+"The spirit with id '"+id+"' was not found");
		}
		Vcar result = null;
		boolean is_user_data = false;
		if ( spirit.old ) {
			// doing old
			is_user_data = true;
			String result_string = null;
			
			int ac = tasknames.convert(first);

			switch ( ac ) {
			case act_xpath: {	
				if ( vcar_list.size() < 3 ||  vcar_list.size() > 4) 
					throw new Exception(pm+"I want two or three parameters: 2=xpath, 3=@attributename");			
				String para_xpath=((Vcar)vcar_list.get(2)).s;
				
				Element found = Commons.xpathSelectSingleNode(spirit.root, para_xpath);
				
				if ( found == null ) {
					String m = "xpath '"+para_xpath+"' found nothing in spirit x";
					Exception e = new Exception(m);
					// lc.addException(e );
					throw e;
				}
				
				if ( vcar_list.size() == 4 ) {
					result_string = Xpath.getValue(found, ((Vcar)vcar_list.get(3)).s,true);
				} else
					result_string = Xpath.getValue(found, null,true);
				
				result = new Vcar(result_string);
				break;
			}
			case act_xpathx: {	
				// bei xpath m�ssen noch 1 oder zwei weitere parameter vorhanden sein:
				// xpath-ausdruck, @attribute
				if ( vcar_list.size() < 4 ||  vcar_list.size() > 5) 
					throw new Exception(pm+"I want two or three parameters: 3=default value 4=xpath, 5=@attributename");			
				String para_xpath=((Vcar)vcar_list.get(3)).s;
				
				Element found = null;
				
				try {
					found = Commons.xpathSelectSingleNode(spirit.root, para_xpath);
				} catch (Exception e) {
					found = null;
				}
				
				if ( found == null ) {
					result_string=((Vcar)vcar_list.get(2)).s;
				} else	if ( vcar_list.size() == 5 ) {
					try {
						result_string = Xpath.getValue(found, ((Vcar)vcar_list.get(4)).s,false);
					} catch (Exception e) {
						result_string = ((Vcar)vcar_list.get(2)).s;
					}
				} else
					result_string = Xpath.getValue(found, null,false);
				result = new Vcar(result_string);
				break;
			}
			case act_xml: {	
				Element toprint = spirit.root;
				if ( vcar_list.size() == 3 ) {
					toprint = Commons.xpathSelectSingleNode(spirit.root, ((Vcar)vcar_list.get(2)).s);
				}
				String x = Commons.toTextRaw(toprint);
				result = new Vcar(x);
				break;
			}
			
			}
			
		} else {
			// new
			String result_string = null;
			String second = null;
			String third = null;
			if ( vcar_list.size() > 2 ) 
				second = ((Vcar)vcar_list.get(2)).s; 
			if ( vcar_list.size() > 3 ) 
				third = ((Vcar)vcar_list.get(3)).s; 	

			if ( first.startsWith("@")) {
				// requesting attribute on root
				is_user_data = true;
				result_string = Xpath.getValue( spirit.root, null, first );
			} else if ( first.equals("exists?")) {
				// exists
				String test;
				if ( second == null )
					throw new LangException(222,"spirit '"+id+"' with request 'exists?' needs more parameters");
				else if ( second.startsWith("@")) {
					// just an attribute
					test = Xpath.getValue( spirit.root, null, second );					
				} else if ( third == null ) {
					// just an xpath
					test = Xpath.getValue( spirit.root, second, null );
				} else if ( third.startsWith("@")) {
					// xpath and attribute
					test = Xpath.getValue( spirit.root, second, third );
				} else 
					throw new LangException(223,"spirit '"+id+"' with request 'exists?' is confused about your parameters");
			    
				if ( test == null )
			    	result_string = "0";
			    else
			    	result_string = "1";
				
			} else if ( first.equals("xml?")) {
				// xml, xpath in second
				is_user_data = true;
				Element toprint = spirit.root;
				if ( second != null ) {
					toprint = Commons.xpathSelectSingleNode(spirit.root, second);						
				}
				if ( toprint == null )
					result_string = spirit.getMissingStrategy("Request: "+
							Vcar.getParaList(vcar_list,"(f,spirit,",")") );
				else
					result_string = Commons.toTextRaw(toprint);
			} else if ( first.equals("count?")) {
				// xml, xpath in second
				is_user_data = false;
				String toprint = null;
				if ( second != null ) {
					List<Element> val = Commons.xpathSelectNodes ( spirit.root, second );
					int size = val.size();
					
					toprint = ""+size;						
				}
				result_string = toprint;

			} else if ( second == null ) {
				// requesting xpath value, xpath in first
				is_user_data = true;
				result_string = Xpath.getValue( spirit.root, first, null );
			} else if ( second.startsWith("@")) {
				// requesting xpath, attribute, xpath in first, attribute in second
				is_user_data = true;
				result_string = Xpath.getValue( spirit.root, first, second );
			} else
				throw new LangException(224,"spirit '"+id+"' received unknown function combination: "
						+ Vcar.getParaList(vcar_list,"(f,spirit,",")") );
			
			if ( result_string == null )
				result_string = spirit.getMissingStrategy("Request: "+
						Vcar.getParaList(vcar_list,"(f,spirit,",")") );
			
			result = new Vcar(result_string);
		}
	
		if ( !is_user_data || spirit.flag_dont_escape_on_read) {
		
			result.dontImport();	
		}			
		
		return result;		

	}


	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Spirit-Functions",3);
		tasknames.put("xpath", act_xpath);
		tasknames.put("xpathx", act_xpathx);
		tasknames.put("xml", act_xml);
		tasknames.putFinished();		
	}



}