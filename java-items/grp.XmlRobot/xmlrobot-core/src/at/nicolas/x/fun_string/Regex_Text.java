/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Regex_Text implements IFun{

	private static final String funn = "regex_text";
	private static final String pm = "function 'regex_text' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
			
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=string to be converted");
		
		String a = ((Vcar)vcar_list.get(0)).s;
		// String c = a.replaceAll("([^a-zA-z0-9])","\\\\$1");
		String d = Pattern.quote(a);
		// if ( !c.equals(d))
	
				
		return (new Vcar(d));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}