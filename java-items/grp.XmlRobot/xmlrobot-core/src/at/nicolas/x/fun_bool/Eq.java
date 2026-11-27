/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Eq implements IFun{

	
	private final static String funn="eq";
	private final static String pm ="function 'eq' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		boolean result = false;
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(305,pm+"wants 2 parameters: 1=Expression 2=Expression");

		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		
		if ( !a.isNum || !b.isNum ) {
			if ( (a.s).equals(b.s) )
				result = true;
		} else if ( a.getNum.compareTo(b.getNum) == 0 )
			result = true;		

		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}



	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}