/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Name_Of_Dir implements IFun {

	private static final String funn = "name_of_dir";
	private static final String pm = "function 'name_of_dir' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {			
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=dir");
		
		String   in = ((Vcar)vcar_list.get(0)).s;
		String ret = Commons.getNameOfDir(in);
		// EgoDirString eds = Commons.introduceDirAsEgoDirString(in);
		// String ret = Commons.constructNameFromEgoDirString(eds);
		// return (new Vcar(Commons.getNameOfDir(a.s)));
		return (new Vcar(ret));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}