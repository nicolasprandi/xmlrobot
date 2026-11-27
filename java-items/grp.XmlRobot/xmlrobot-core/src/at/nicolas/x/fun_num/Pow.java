/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Pow implements IFun {

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() < 2 ) 
			throw new LangException(363,"Function 'pow' wants at least 2 parameters: 1=number, 2=number of multiplications");
		
		double a = (((Vcar)vcar_list.get(0)).getNumber()).doubleValue();
		double b = (((Vcar)vcar_list.get(1)).getNumber()).doubleValue();
		
		double result = Math.pow(a, b);
		

		BigDecimal re = BigDecimal.valueOf(result);
		
		Vcar r = new Vcar(re.toString());
		r.dontImport();
		return r;
	}

	public String getExtensionId() {
		return "pow";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}