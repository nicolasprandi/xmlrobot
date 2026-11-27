/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Decode_Html_Specials implements IFun{

	private static final String funn = "decode_html_specials";
	private static final String pm = "decode_html_specials: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		rh.depressive("function decode-html-special do not exist anymore, use function 'unescape'");		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want one parameter: 1=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		
		a.s = Commons.stringReplace(a.s,"&lt;","<");
		a.s = Commons.stringReplace(a.s,"&gt;",">");
		a.calculate();
				
		return (a);
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}