/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.EgoFileString;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Canonical_File implements IFun {

	private static final String funn= "can_file";
	private static final String pm= "function 'can_file' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wants one parameter: 1=file");
				
		// String r = Commons.getCanoFileXr(((Vcar)vcar_list.get(0)).s); 
		
		String in = ((Vcar)vcar_list.get(0)).s;
		EgoFileString eds = Commons.introduceFileAsEgoFileString(in);		

		return (new Vcar(eds.string));
	}


	public String getExtensionId() {
		return funn;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}