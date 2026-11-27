/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class File_Exists implements IFun {
	
	private final static String funn= "file_exists";
	private final static String pm= "function 'file_exists' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		boolean result = false;
		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(314,pm+"wants 1 paramter: 1=File");

		
		Vcar x = (Vcar)vcar_list.get(0);
		
		result = CommonsVfs.existsFile(x.s,rh.getSitzung());
		/*
		try {
			if ( CommonsVfs.existsFile(x.s,rh.getSitzung()))
				result = true;
		} catch (Exception e) {
			result=false;
		}
		*/
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}

	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}