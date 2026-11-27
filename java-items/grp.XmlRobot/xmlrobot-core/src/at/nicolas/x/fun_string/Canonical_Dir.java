/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.EgoDirString;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Canonical_Dir implements IFun {

	private static final String funn= "can_dir";
	private static final String pm= "function 'can_dir' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(377,pm+" wants one parameter: 1=dir");
				
		//String r=Commons.getCanoDirXr(((Vcar)vcar_list.get(0)).s);
		String in = ((Vcar)vcar_list.get(0)).s;
		EgoDirString eds = Commons.introduceDirAsEgoDirString(in);

		return (new Vcar(eds.string));
	}


	public String getExtensionId() {
		return funn;
	}
	

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}