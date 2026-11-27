/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Length implements IFun{

	
	private final static String funn = "length";
	private final static String pm = "length: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				int result = 0;
		if ( vcar_list.size() != 1 ) throw new LangException(359,"I want one parameters: 1=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		result = a.s.length();
		
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