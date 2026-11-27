/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Is_Bool implements IFun {
	
	private final static String funn = "is_bool";
	private final static String pm ="function 'is_bool' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		boolean result = false;
		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(3222,pm+"wants 1 parameter: 1=Something");
		
		Vcar a = (Vcar)vcar_list.get(0);

		if ( a.isBool )
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