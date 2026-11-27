/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Filecontent_Translate implements IFun {

	private static final String funn = "filecontent_translate";
	private static final String pm = "filecontent_translate: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		throw new Exception("filecontent_translate very depressive");
		/*
		String filename = null;
		
		for ( int i = 0; i<vcar_list.size(); i++ ) {
			Vcar x = (Vcar)vcar_list.get(i);
			
			if 		( i == 0 ) filename = x.s;
			else throw new Exception();

		}
		
		if ( !Commons.existsFile(filename)) 
			throw new Exception(pm+"the parameter '"+filename+"' is not a File !!!");

		String result = Commons.readTextfile(filename)	;
		String erg = rh.translateValue(result);
		
		return (new Vcar(erg));
		*/
		
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}