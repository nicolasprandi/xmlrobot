/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class IndexOf implements IFun {


	
	private final static String funn = "index_of";
	private final static String pm = "index_of: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				int result = -1;
		
		if ( vcar_list.size() < 2 || vcar_list.size() > 3) throw new LangException(356,pm+"I want 2 or 3 parameters: 1=Text 2=Text to search [3=from index]");
		
		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		
		if ( vcar_list.size() == 2 ) {
			result = (a.s).indexOf(b.s);
		} else {
			Vcar c = (Vcar)vcar_list.get(2);
			result = (a.s).indexOf(b.s,c.getInt());
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