/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Try implements IFun {

	private static final  String funn="try";
	private static final  String pm="function 'try' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() < 1 || vcar_list.size() > 3) 
			throw new LangException(pm+"wants at least 1 parameter: 1=expression-definition 2: fallback for exception 3: fallback for emtpy string");
		
		String value = ((Vcar)vcar_list.get(0)).s;		
		boolean dont_import = false;
		try {

			value = rh.translateValueSave(value);

			if ( value.equals("") && vcar_list.size() > 2 )
				value = ((Vcar)vcar_list.get(2)).s;
			else
				dont_import = true;
		} catch (Exception e) {
			if ( vcar_list.size() > 1 )
				value = ((Vcar)vcar_list.get(1)).s;
			else
				value = "";
		}
		Vcar retti = new Vcar(value);
		if (dont_import)
			retti.dontImport();
		return (retti);
	}

	public String getExtensionId() {
		return funn;
	}
	

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}