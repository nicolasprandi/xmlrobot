/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Dir_Exists implements IFun {

	private final static String funn = "dir_exists";
	private final static String pm = "function 'dir_exists' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		boolean result = false;
		
		if ( vcar_list.size() != 1 ) throw new LangException(302,pm+"wants one parameter: 1=dirname");
		Vcar x = (Vcar)vcar_list.get(0);
		
		
		try {
			if ( CommonsVfs.existsDir(x.s,rh.getSitzung()))
				result = true;
			else
				result = false;
		} catch (Exception e) {
			result = false;
		}
		
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