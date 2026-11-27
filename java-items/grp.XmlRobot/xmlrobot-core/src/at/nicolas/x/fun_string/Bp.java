/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Bp implements IFun{

	String funn;
	String pm;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "bp";
		this.pm = funn+": ";
		
		Vcar   manip = (Vcar)vcar_list.get(0);
		String i = manip.s;
		
		int len = 15;
		
		if ( i.length() < len ) {
			String j = Commons.StringTimes("0", len-i.length()) ;
			if ( (manip.getNumber()).compareTo(BigDecimal.ZERO) == -1 )
				j = j.concat("-");
			else
				j = j.concat("+");
			
			j = j.concat(manip.s);
			return (new Vcar(j));
		} else
			return manip;
		
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}