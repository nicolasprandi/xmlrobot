/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Sequence implements IFun {

	private static final String funn = "sequence";
	private static final String pm = "function 'sequence' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		String p1 = null;
		if ( vcar_list.size() > 0 )
			p1 = ((Vcar)vcar_list.get(0)).s;

		String p2 = null;
		if ( vcar_list.size() > 1 )
			p2 = ((Vcar)vcar_list.get(1)).s;
		
		String res = rh.sequenceFun(p1, p2);
		
		if ( res == null)
			return null;
		else
			return ( new Vcar(res));
				
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}