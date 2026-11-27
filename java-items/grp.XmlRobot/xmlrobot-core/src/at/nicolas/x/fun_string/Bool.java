/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Bool implements IFun{

	
	
	private static final String pm="bool: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		String result = "?";
		if ( vcar_list.size() != 1 ) throw new LangException(375,"I want one parameters: 1=boolean");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		if ( a.isBool) {
			if ( a.getBool() == true )
				result ="Yes";
			else
				result = "No";
		} else
			throw new Exception(pm+" This value is not bool: "+((Vcar)vcar_list.get(0)).s);
		
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return "bool";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}