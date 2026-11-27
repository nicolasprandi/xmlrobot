/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Dir_Of_File implements IFun {

	private static final String funn = "dir_of_file";
	private static final String pm = "function 'dir_of_file' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=full filename");
				
		Vcar   a_v = (Vcar)vcar_list.get(0);
		String a_s = a_v.s;
		
		String r= Commons.getDirOfFile(a_s);
				
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