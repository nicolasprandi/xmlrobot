/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugStrategy extends Object implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
	
		rh.depressive("function 'strategy' does not exist anymore, use (f,info,");
				
		if ( vcar_list.size() != 1 ) throw new Exception("f strategy I want one parameter: 1=Strategy");
		Vcar x = (Vcar)vcar_list.get(0);
		String res = CoreBuilder.ws.getStrategy().getStrategy(x.s);
		if ( res == null )
			res = "null";
		
		return (new Vcar(res));
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}