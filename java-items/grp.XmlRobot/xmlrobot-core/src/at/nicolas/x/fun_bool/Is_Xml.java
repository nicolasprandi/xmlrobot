/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Is_Xml implements IFun {
	
	private final static String funn = "is_xml";
	private final static String pm ="function 'is_xml' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(322,pm+"wants 1 parameter: 1=Something");
		
		String x = ((Vcar)vcar_list.get(0)).s;
		boolean result = CommonsVfs.textIsXml(x,rh.getSitzung(),rh.getTempDir());
		
		
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