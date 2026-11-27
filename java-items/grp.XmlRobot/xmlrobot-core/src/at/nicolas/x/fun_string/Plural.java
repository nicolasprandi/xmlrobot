/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Plural implements IFun {

	private static final String funn= "plural";
	private static final String pm= "function 'plural' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() < 1 ||  vcar_list.size() > 2 ) 
			throw new LangException(pm+"wants 1/2 parameters: 1=number, [2=plural-s]");
		
		BigDecimal zahl = ((Vcar)vcar_list.get(0)).getNumber();
		int z = ((Vcar)vcar_list.get(0)).getInt();
		
		String plural = "s";
		if ( vcar_list.size() >= 2) 
			plural = ((Vcar)vcar_list.get(1)).s;
		
		String ret = "";
		if (zahl.compareTo(BigDecimal.ONE) == 1 || z==0)
			ret = plural;
					
		return (new Vcar(ret));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}