/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class EqText implements IFun{

	
	private final static String funn="eq_text";
	private final static String pm = "function 'eq_text' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(307,pm+"I want 2 parameters: 1=Expression 2=Expression");

		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		
		boolean result = false;
		if ( (a.s).equals(b.s) )
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