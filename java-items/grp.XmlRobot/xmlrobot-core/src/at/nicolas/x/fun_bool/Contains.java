/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Contains implements IFun {


	private final static String funn = "contains";
	private final static String pm = "contains: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				boolean result = false;
		
		if ( vcar_list.size() != 2 ) throw new LangException(301,pm+"I want 2 parameters: 1=Text 2=Text to search");
		
		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		
		if ( (a.s).indexOf(b.s) != -1 )
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