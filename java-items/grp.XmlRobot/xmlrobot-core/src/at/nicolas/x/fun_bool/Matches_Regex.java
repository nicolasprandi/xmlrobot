/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Matches_Regex implements IFun {
	
	
	private final static String funn = "matches_regex";
	private final static String pm = "matches_regex: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

				boolean result = false;
		
		if ( vcar_list.size() < 2 || vcar_list.size() > 3 ) 
			throw new LangException(327,pm+"I want 2 or 3 parameters: 1=String 2=Regular Expression [3=Regex-Flags]");
		
		Vcar string = (Vcar)vcar_list.get(0);
		Vcar regex = (Vcar)vcar_list.get(1);

		int pattern_flags = 0;
		if ( vcar_list.size() >= 3 ) {
			String f = ((Vcar)vcar_list.get(2)).s;
			pattern_flags = Commons.regexFlagsString2int(f);
			
		}		
		if ( Commons.stringMatchesRegex(string.s,regex.s,pattern_flags) )
			result = true;
				
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}