/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Has_Flag implements IFun {

	private final static String funn = "has_flag";
	private final static String pm = "has_flag: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		
		if ( vcar_list.size() != 2 ) throw new LangException(319,pm+"I want 2 parameters: 1=Text 2=Flagname");
		
		Vcar a = (Vcar)vcar_list.get(0);
		Vcar b = (Vcar)vcar_list.get(1);
		String evaluate = a.s;
		String flagname = b.s;
		
		boolean result = false;
		
		if ( evaluate != null ) {
			if ( 	evaluate.equals(flagname) || 
					evaluate.indexOf(" "+flagname+" ") != -1 || 
							evaluate.startsWith(flagname+" ") || 
							evaluate.endsWith(" "+flagname))
				result = true;	
			else
				result = false;
		} else
			result = false;
		
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