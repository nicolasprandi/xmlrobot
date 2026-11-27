/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Replace_Regex implements IFun{

	private static final String funn= "replace_regex";
	private static final String pm= "function 'replace_regex' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
			
		if ( vcar_list.size() != 3 ) 
			throw new LangException(pm+"wants 3 parameter: 1=string to be manipulated, 2=regex, 3=replacement (with group by eg $1 )");
		
		String a = ((Vcar)vcar_list.get(0)).s;
		String b = ((Vcar)vcar_list.get(1)).s;
		String c = ((Vcar)vcar_list.get(2)).s;
		String d = a.replaceAll(b, c);
				
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