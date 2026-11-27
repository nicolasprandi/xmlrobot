/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugDocumentsDir implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		
		String until_now = CoreBuilder.ws.getUserDocDir();
		if ( until_now == null )
			throw new Exception("no documents dir available");

		return (new Vcar(until_now));
	}




	public void help(ArrayList<Vcar> vcar_list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}