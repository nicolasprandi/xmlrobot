/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Count_String implements IFun {

	
	private final static String funn = "count_string";
	private final static String pm = "count_string: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

						
		if ( vcar_list.size() != 2  ) 
			throw new LangException(349,pm+"I want 2 parameters: 1=String 2=Text-To-Search");
		
		String fileconent  = ((Vcar)vcar_list.get(0)).s;
		
		String searchString = ((Vcar)vcar_list.get(1)).s;
		int searchStringLen = searchString.length();
		
		int occurrences = 0;
		int index = 0;
		while (index < fileconent.length() && (index = fileconent.indexOf(searchString, index)) >= 0) {
		    occurrences++;
		    index = index + searchStringLen;
		}		
		
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