/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.regex.Matcher;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Replace_Regex_Literal implements IFun{

	private static final String funn = "replace_regex_literal";
	private static final String pm = "replace_regex_literal: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() != 2 ) 
			throw new LangException(pm+"I want one parameter: 1=string to be manipulated, 2=Regexreplace including $1 "+vcar_list.size()+" parameters");
		
		String a = ((Vcar)vcar_list.get(0)).s;
		String b = ((Vcar)vcar_list.get(1)).s;
		String c = a.replaceAll(a, Matcher.quoteReplacement(b));
				
		return (new Vcar(c));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}