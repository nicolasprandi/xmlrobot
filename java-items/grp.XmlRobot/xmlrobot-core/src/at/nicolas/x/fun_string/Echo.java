/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Echo implements IFun{


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		if ( vcar_list.size() < 1 || vcar_list.size() > 2 ) 
			throw new LangException("function 'echo': At least one parameterer with any value to echo");
		String toecho = (((Vcar)vcar_list.get(0)).s);
		
		String toechomsg;
		if ( vcar_list.size() > 1 )
			toechomsg = (((Vcar)vcar_list.get(1)).s) + ": "+"* * * * * * * "+toecho;
		else
			toechomsg = "* * * * * * * "+toecho;
		
		// lc.warn(toechomsg,this);
		Sp.p(toechomsg);


		return (new Vcar(toecho));
	}


	public String getExtensionId() {
		return "echo";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}