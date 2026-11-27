/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Loc implements IFun {

	private static final String funn = "loc";
	private static final String pm = "loc: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {		

		if ( vcar_list.size() > 2 ) 
			throw new LangException(pm+"I want one or two parameters app,inst");
		
		String app = null;
		String inst = null;
		
		if ( vcar_list.size() == 0 ) {
		} else if ( vcar_list.size() == 1 ) {
			// app only
			Vcar   a= (Vcar)vcar_list.get(0);
			app = a.s;
		} else {
			Vcar   a= (Vcar)vcar_list.get(0);
			Vcar   b= (Vcar)vcar_list.get(1);
			app = a.s;
			inst = b.s;
		}

		String res = Commons.getDbLoc(app, inst)	;				
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