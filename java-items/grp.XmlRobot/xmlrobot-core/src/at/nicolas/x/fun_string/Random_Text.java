/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Random_Text implements IFun{

	private static final String funn = "random_text";
	private static final String pm = "random_text: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() < 1 ) throw new LangException(pm+"I wants at least 1 parameters: 1=length");
		if ( vcar_list.size() > 2 ) throw new LangException(pm+"I wants to the max 2 parameters: 1=length 2=characterpool");

		int len = ((Vcar)vcar_list.get(0)).getInt();
		
		String result = null;
		if ( vcar_list.size() == 1 ) {
			result = Commons.randomAlphaNumeric(len);
		} else if ( vcar_list.size() == 2 ) {
			result = Commons.randomText(len, ((Vcar)vcar_list.get(1)).s);
		} else 
			throw new LangException(pm+"I wants to the max 2 parameters: 1=length 2=characterpool");
				
		return (new Vcar(result));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	



}