/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class GtNum implements IFun {

	
	private final static String funn = "gt_num";
	private final static String pm ="function 'gt_num' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new LangException(317,pm+"I want 2 parameters: 1=Number 2=Number");
		
		BigDecimal a = ((Vcar)vcar_list.get(0)).getNumber();
		BigDecimal b = ((Vcar)vcar_list.get(1)).getNumber();
		
		boolean result = false;
		if ( a.compareTo(b) == 1)
			result = true;
		
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