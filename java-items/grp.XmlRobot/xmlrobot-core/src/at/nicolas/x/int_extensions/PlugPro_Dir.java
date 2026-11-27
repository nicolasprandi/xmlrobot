/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugPro_Dir implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
	
		if ( vcar_list.size() != 2 ) 
			throw new Exception("f pro_dir I want two parameters: 1=app, 2=pro");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   b= (Vcar)vcar_list.get(1);
		
		String retval = CoreBuilder.db.getFullDirInst(a.s, b.s);
		if ( retval == null )
			throw new Exception("pro_dir didn't find the app=["+a.s+"] pro=["+b.s+"] combination");
					
		return (new Vcar(retval));
	}



	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}