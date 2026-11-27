/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Add implements IFun {

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		BigDecimal result = BigDecimal.ZERO;
		Iterator<Vcar> i = vcar_list.iterator();

		while ( i.hasNext() ) {
			Vcar x = (Vcar)i.next();
			result = result.add(x.getNumber());
		}

		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		// TODO Auto-generated method stub
		return "add";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}