/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Dir_Of_Dir implements IFun {

	private static final String funn= "dir_of_dir";
	private static final String pm= "function 'dir_of_dir' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wantw one parameter: 1=dir");
		
		Vcar   a = (Vcar)vcar_list.get(0);
		String r = Commons.getDirOfDir(a.s);
		if ( r== null)
			r="";
		return (new Vcar(r));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}