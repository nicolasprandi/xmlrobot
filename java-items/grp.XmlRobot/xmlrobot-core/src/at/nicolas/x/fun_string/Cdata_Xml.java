/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Cdata_Xml implements IFun {


	/*
	private static final String wurst1 ="s-38-834z";
	private static final String wurst2 ="r-idr-423";
	private static final String wurst3 ="tsk-sld3-";
	private static final String wurst4 ="tsasdk-ssdfld3-";	*/
	private static final String funn = "cdata_xml";
	private static final String pm = "cdata_xml: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		rh.depressive("function xml do not exist anymore, use function 'escape'");		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want one parameter: 1=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		
		String worker = Commons.escapeXml(a.s);
				
		return (new Vcar(worker));
	}

	public String getExtensionId() {
		return funn;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}