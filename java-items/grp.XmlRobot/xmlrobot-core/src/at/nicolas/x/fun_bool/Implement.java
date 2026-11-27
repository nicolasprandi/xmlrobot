/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Implement implements IFun{


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() != 1 ) 
			throw new LangException("implement wants 1 parameters: 1=echnaton|online");
		
		Vcar   p= (Vcar)vcar_list.get(0);
		String asked = p.s;
		boolean ret = false;
		if ( asked.equals("echnaton")) ret = true;
		else if ( asked.equals("online")) {
			ret = rh.getSitzung().isOnline();
		}
		
		Vcar retCar = new Vcar(ret);
		retCar.dontImport();
		return (retCar);
	}

	public String getExtensionId() {
		return "implement";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}