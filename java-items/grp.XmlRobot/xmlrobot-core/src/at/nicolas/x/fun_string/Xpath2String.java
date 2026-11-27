/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Xpath2String implements IFun {

	private static final String funn="xpath2string";
	private static final String pm="xpath2string: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want exacltly one parameter");
		
		String in= ((Vcar)vcar_list.get(0)).s;
		
		String result = null;
		
		if ( in.indexOf("\"") == -1 ) {
			// there is no quote
			result = "\""+in+"\"";
		} else {
		    StringBuffer out=new StringBuffer("");
		    out.append("concat(\"");
		    char[] chars=in.toCharArray();
		    for(int i=0;i<chars.length;i++) {
		    	
		    	switch(chars[i]) {
		        // case 38:out.append("&amp;"); break; 
		        case 34:out.append("\",'\"',\""); break; // quote "
		        default:
		            out.append(chars[i]);
		            break;
		    	}
		    	
		    }
		    out.append("\")");
		    result = out.toString();			
		}
		



		return (new Vcar(result));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	



}