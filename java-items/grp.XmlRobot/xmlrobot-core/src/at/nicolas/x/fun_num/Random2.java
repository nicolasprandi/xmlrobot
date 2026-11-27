/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Random2 implements IFun{


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {


		if ( vcar_list.size() != 2 ) 
			throw new LangException(365,"function 'random' wants two parameters: 1=min integer 2=max integer");
		
		int   min= ((Vcar)vcar_list.get(0)).getInt();
		int   max= ((Vcar)vcar_list.get(1)).getInt();
		
		Random rn = new Random();
		int result = min + rn.nextInt(max-min+1);
		
		
		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		return "random";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}