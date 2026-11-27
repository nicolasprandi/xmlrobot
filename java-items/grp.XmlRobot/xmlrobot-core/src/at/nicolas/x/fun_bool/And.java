/* Copyright C 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class And implements IFun {


	private final static String funn ="and";
	private final static String pm ="function 'and' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		if ( vcar_list.size() < 2 ) 
			throw new LangException(300,pm+"wants at least two boolean parameter");
		boolean result = true;
		Iterator<Vcar> i = vcar_list.iterator();
		while ( i.hasNext() ) {
			Vcar x = (Vcar)i.next();
			boolean now = x.getBool();
			result = result && now;	
		}

		Vcar r = new Vcar(result);
		r.dontImport();
		return r;

	}



	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}