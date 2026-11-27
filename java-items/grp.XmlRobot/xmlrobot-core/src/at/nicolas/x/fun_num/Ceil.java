/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Ceil implements IFun{


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		if ( vcar_list.size() !=1  ) 
			throw new LangException(346,"ceil: I want excactly one parameter: 1=number");
		
		BigDecimal x = ((Vcar)vcar_list.get(0)).getNumber();
		BigDecimal result = x.setScale(0, RoundingMode.CEILING);
		
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}

	public String getExtensionId() {
		return "ceil";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}