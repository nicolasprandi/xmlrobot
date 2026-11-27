/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Substring implements IFun {

	private static final String funn= "substring";
	private static final String pm= "function 'substring' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		String res;
		if ( vcar_list.size() != 2 &&  vcar_list.size() != 3) 
			throw new LangException(pm+"wants two or three parameters: 1=text, 2=fromIndex, 3=endIndex");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   from= (Vcar)vcar_list.get(1);
		if ( !from.isNum ) 
			throw new Exception(pm+"says fromIndex is not numeric: "+from.s);		
		
		if ( vcar_list.size() == 3) {
			Vcar   end= (Vcar)vcar_list.get(2);
			int end2 = end.getInt();
			if ( end2 > a.s.length())
				end2 = a.s.length();
			if ( !end.isNum ) 
				throw new Exception(pm+"says endIndex is not numeric: "+end.s);	
			res = (a.s).substring(from.getInt(),end2);
			
		} else {

			res = (a.s).substring(from.getInt());			
		}

				
		return (new Vcar(res));
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}