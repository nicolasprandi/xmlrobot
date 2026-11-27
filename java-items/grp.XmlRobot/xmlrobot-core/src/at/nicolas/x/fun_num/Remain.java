/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Remain implements IFun {

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {


		if ( vcar_list.size() !=2 ) throw new LangException(366,"Function 'remain' wants 2 parameters: 1=dividend, 2=divisor");
		
		BigDecimal a= ((Vcar)vcar_list.get(0)).getNumber();
		BigDecimal b= ((Vcar)vcar_list.get(1)).getNumber();

		BigDecimal result = a.remainder(b);
		
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		return "remain";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}