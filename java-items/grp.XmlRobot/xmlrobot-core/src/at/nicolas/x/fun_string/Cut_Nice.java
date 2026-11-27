/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Cut_Nice implements IFun{

	private static final String funn = "cut_nice";
	private static final String pm = "cut_nice: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() !=2  ) throw new LangException(pm+"I want 2 parameters: 1=string, 2=length");

		String rs = Commons.cutNice(((Vcar)vcar_list.get(0)).s, 
				((Vcar)vcar_list.get(1)).getInt());

		return (new Vcar(rs));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}