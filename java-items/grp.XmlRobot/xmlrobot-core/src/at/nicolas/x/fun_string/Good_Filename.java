/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Good_Filename implements IFun{

	String funn = "good_filename";
	String pm = "function 'good_filename' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=string to be manipulated ");
		
		String a = ((Vcar)vcar_list.get(0)).s;
		String c = a.replaceAll("([\\/\\\\\\?\\%\\*\\:\\|\\\"\\<\\>\\ \\'])","_");
				
		return (new Vcar(c));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}