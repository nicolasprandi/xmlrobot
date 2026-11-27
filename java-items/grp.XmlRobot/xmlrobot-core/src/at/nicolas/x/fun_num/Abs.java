/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Abs implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() !=1  ) 
			throw new LangException(344,"function 'abs' wants one parameter: 1=number");
		Vcar xv = (Vcar)vcar_list.get(0);
		
		BigDecimal result = (xv.getNumber()).abs();

		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}

	

	public String getExtensionId() {
		return "abs";
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}