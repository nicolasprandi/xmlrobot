/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Concat implements IFun{

	private static final String funn = "concat";
	private static final String pm = "concat: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		String result = new String ();
		Iterator<Vcar> i = vcar_list.iterator();
		while ( i.hasNext() ) {
			Vcar x = (Vcar)i.next();
			result = result.concat(x.s);
		}
		// TODO Auto-generated method stub
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