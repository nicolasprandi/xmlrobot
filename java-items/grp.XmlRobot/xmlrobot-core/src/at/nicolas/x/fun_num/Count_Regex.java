/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;



public class Count_Regex implements IFun {

	
	private final static String funn = "count_regex";
	private final static String pm = "count_regex: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				
		if ( vcar_list.size() < 2 || vcar_list.size() > 3  ) 
			throw new LangException(348,pm+"I want 2 or 3 parameters: 1=String to examine 2=Regex-To-Search [3=Regex-Flags]");
		
		String fileconent  = ((Vcar)vcar_list.get(0)).s;

		int pattern_flags = 0;
		if ( vcar_list.size() == 3 ) {
			String f = ((Vcar)vcar_list.get(2)).s;
			pattern_flags = Commons.regexFlagsString2int(f);
			
		}
		
		String searchString = ((Vcar)vcar_list.get(1)).s;
		
		int occurrences = Commons.countRegexMatches(fileconent, searchString,pattern_flags);


		Vcar r = new Vcar(occurrences);
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