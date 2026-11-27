/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Has implements IFun{
	
	private final static String funn ="has";
	private final static String pm ="function 'has' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		boolean result = false;
		
			
		if ( vcar_list.size() == 1 ) {
			
			String p1 = ((Vcar)vcar_list.get(0)).s;
			if ( p1.length() > 0 )
				result = true;
			else
				result = false;
		
			
		} else if ( vcar_list.size() == 2 ) {
			String p1 = ((Vcar)vcar_list.get(0)).s;
			if ( p1.equals("prop") || p1.equals("dir") || p1.equals("file") ) {
				String p2 = ((Vcar)vcar_list.get(1)).s;
				
				boolean var_exists = rh.doesExistVariable(p1, p2);
				if ( var_exists ) {
					String value = rh.getVariableValue(p1, p2);
					if ( value.length() > 0 )
						result = true;
					else
						result = false;
				} else
					result = false;
			} else {
				throw new LangException(320,pm+" found an uncorrect variable-type '"+p1+"'");
			}
			
					
		} else {
			throw new LangException(321,pm+"wants 1 or 2 parameter: (v1) [1]=some content (v2) [1]=variable-type [2]=variable-id");
		}
		
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