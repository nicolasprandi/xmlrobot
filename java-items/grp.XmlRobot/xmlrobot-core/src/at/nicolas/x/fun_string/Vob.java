/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Vob implements IFun {

	private static final String funn ="vob";
	private static final String pm = "function 'vob' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
			
		if ( vcar_list.size() != 2 ) 
			throw new LangException(pm+"wants 2 parameters: 1=variable-type 2=variable-id");
		
		String p1 = ((Vcar)vcar_list.get(0)).s;
		if ( p1.equals("prop") || p1.equals("dir") || p1.equals("file") ) {
			String p2 = ((Vcar)vcar_list.get(1)).s;
			
			Vcar r;
			if ( rh.doesExistVariable(p1, p2)) {
				String value = rh.getVariableValue(p1, p2);
				//value = Entities.exportEntities(value);
				r = new Vcar(value);
				
			} else
				r = new Vcar("");
	
			r.dontImport();		
			return  r;
		} else {
			throw new LangException(pm+" found an uncorrect variable-type '"+p1+"'");
		}
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
}