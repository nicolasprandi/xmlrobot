/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Text_To_Html implements IFun{

	private static final String funn= "text_to_html";
	private static final String pm= "text_to_html: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		rh.depressive("function text_to_html do not exist anymore, use function 'escape'");		
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want one parameter: 1=text");
		
		String   in= ((Vcar)vcar_list.get(0)).s;
				
		return (new Vcar(Commons.text_to_html(in)));
	}
	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}