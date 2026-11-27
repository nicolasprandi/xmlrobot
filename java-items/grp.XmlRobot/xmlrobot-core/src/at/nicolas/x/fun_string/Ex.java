/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Ex implements IFun {

	private static final String funn= "xescape";
	private static final String pm = "function 'xescape' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=text");
		
		// Vcar r = new Vcar(Entities.importEntities(Commons.cdata_xml(((Vcar)vcar_list.get(0)).s)));
		Vcar r = new Vcar(Commons.escapeXml(Entities.importEntities(((Vcar)vcar_list.get(0)).s)));
		// r.dontImport();
		return (r);
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}