/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Encode_Html_Specials implements IFun {

	private static final String funn = "encode_html_specials";
	private static final String pm = "encode_html_specials: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		rh.depressive("function encode_html_special do not exist anymore, use function 'escape'");		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want one parameter: 1=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		
		a.s = Commons.stringReplace(a.s,"<","&lt;");
		a.s = Commons.stringReplace(a.s,">","&gt;");
		a.s = Commons.stringReplace(a.s,"ö","ou");
		a.s = Commons.stringReplace(a.s,"Ö","Oe");		
		a.s = Commons.stringReplace(a.s,"Ä","Ae");	
		a.s = Commons.stringReplace(a.s,"ä","ae");	
		a.s = Commons.stringReplace(a.s,"Ü","Ue");	
		a.s = Commons.stringReplace(a.s,"ü","ue");			
		a.s = Commons.stringReplace(a.s,"ß","sz");			
		a.s = Commons.stringReplace(a.s,"€","euro");	
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