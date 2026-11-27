/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Path_Of_Dir implements IFun {

	private static final String funn= "path_of_dir";
	private static final String pm= "function 'path_of_dir' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {		

		if ( vcar_list.size() !=1 ) 
			throw new LangException(pm+"wants 1 parameter: 1=Dir");
		
		String   a = ((Vcar)vcar_list.get(0)).s;

		if ( a.endsWith("/"))
			a = a.substring(0, a.length()-1);
		else if ( a.endsWith("\\"))
			a = a.substring(0, a.length()-1);
	
		return (new Vcar(a));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}