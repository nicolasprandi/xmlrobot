/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Substring_After implements IFun {

	private static final String funn = "substring_after";
	private static final String pm = "function 'substring_after' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		String res;
		if ( vcar_list.size() != 2 ) 
			throw new LangException(pm+"wants two parameters: 1=text, 2=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   b= (Vcar)vcar_list.get(1);
		
		int pos = (a.s).indexOf(b.s);
		if ( pos != -1 ) 
			// found
			res = (a.s).substring(pos+(b.s).length());
		else
			// nicht gefunden
			res = new String (a.s);
					
		return (new Vcar(res));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}