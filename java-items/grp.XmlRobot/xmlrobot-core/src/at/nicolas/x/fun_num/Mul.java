/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Mul implements IFun {

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() < 2 ) 
			throw new LangException(363,"Function 'mul' wants at least 2 parameters: 1=number, 2=number [,n=number]");
		
		BigDecimal result = new BigDecimal(0);
		
		for ( int i = 0; i<vcar_list.size(); i++ ) {
			Vcar x = (Vcar)vcar_list.get(i);
			
			if 		( i == 0 ) result = x.getNumber();
			else if ( i > 0) result = result.multiply(x.getNumber()); 

		}
		
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		return "mul";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}