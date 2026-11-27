/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class EqNum implements IFun{

	
	private final static String funn="eq_num";
	private final static String pm ="function 'eq_num' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(306,pm+"wants 2 parameters: 1=Number 2=Number");

		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		
		boolean result = false;
		if ( a.getNumber().compareTo(b.getNumber()) == 0 )
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