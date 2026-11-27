/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Test implements IFun {

	private static final String funn= "test";
	private static final String pm= "test: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		

		if ( vcar_list.size() !=1 ) 
			throw new LangException(pm+"I want 1 para:  a=(,) b=(e:o)(e:c)(e:s) c=<&>");
		
		String   a = ((Vcar)vcar_list.get(0)).s;

		String res = "";
		if ( a.equals("a"))
			res="(,)";
		else if ( a.equals("b"))
			res="(e:o)(e:s)(e:c)";
		else if ( a.equals("c"))
			res="<&>";		
	
		return (new Vcar(res));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}





}