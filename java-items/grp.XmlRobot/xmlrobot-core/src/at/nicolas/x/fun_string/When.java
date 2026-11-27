/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class When implements IFun{

	private static final String funn ="when";
	private static final String pm ="function 'when' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		String result = "";
		
		if ( vcar_list.size() != 3 ) 
			throw new LangException(pm+"wants 3 parameters: 1=bool 2=true-Expression 3=false-expression");

		Vcar ask = (Vcar)vcar_list.get(0);
		Vcar on_true = (Vcar)vcar_list.get(1);
		Vcar on_false = (Vcar)vcar_list.get(2);

		
		if ( ask.getBool() ) 
			result = on_true.s;
		else
			result = on_false.s;

		return (new Vcar(result));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}