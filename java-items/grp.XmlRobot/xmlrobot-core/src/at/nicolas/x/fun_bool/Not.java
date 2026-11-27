/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Not implements IFun{

	
	private final static String funn ="not";
	private final static String pm ="function 'not' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		boolean ret = false;	
		if ( vcar_list.size() != 1 ) 
			throw new LangException(329,pm+"wants 1 boolean parameter");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		if ( a.isBool)
			ret = !a.getBool();
		else 
			throw new Exception(pm+"The parameter '"+a.s+"' is not boolean!");
		
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