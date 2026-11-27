/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugLib implements IFun {


	

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		if ( vcar_list.size() != 1 ) 
			throw new Exception("lib I want 1 parameter: 1=ant, poi");
		
		String r_lib = ((Vcar)vcar_list.get(0)).s;
		String result_fn = null;
		if ( r_lib.equals("ant"))
			result_fn = CoreBuilder.ws.getAntClassLoaderDesciption();
		else if ( r_lib.equals("poi"))
			result_fn = CoreBuilder.ws.getPoiClassLoaderDesciption();

		if ( result_fn == null)
			throw new Exception("no lib available");
		
		
		return (new Vcar(result_fn));
	}



	public void help(ArrayList<Vcar> vcar_list, ILogConnector lc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}