/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Starts_With implements IFun {


	
	private final static String funn = "starts_with";
	private final static String pm = "starts_with: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

				boolean ret = false;	
		if ( vcar_list.size() != 2 ) throw new LangException(331,"I want 2 parameters: 1=text, 2=test to search at beginning");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   b= (Vcar)vcar_list.get(1);
		
		if ( (a.s).startsWith(b.s)) 
			ret = true;
		
		Vcar r = new Vcar(ret);
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