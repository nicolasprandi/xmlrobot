/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Fill implements IFun{

	private static final String funn = "fill";
	private static final String pm = "fill: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 4 ) 
			throw new LangException(pm+"I want one parameter: 1=String 2=left/right 3=len 4=character "+vcar_list.size()+" parameters");
		
		String   string = ((Vcar)vcar_list.get(0)).s;
		String left_right = ((Vcar)vcar_list.get(1)).s;
		String   len = ((Vcar)vcar_list.get(2)).s;
		String   cha = ((Vcar)vcar_list.get(3)).s;
		
		int len2 = Commons.string2int(len);
		
		String result = string;
		if ( result.length() < len2 ) {
			String add = Commons.StringTimes(cha, len2-result.length());
			if ( left_right.equals("left"))
				result = add+result;
			else if ( left_right.equals("right"))
				result = result+add;
			else
				throw new LangException("fill found for left-right* "+left_right);
		}
		
				
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