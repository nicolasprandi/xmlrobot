/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Every_Second implements IFun{
	
	private final static String funn = "every_second";
	private final static String pm = "every_second: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

			
		if ( vcar_list.size() !=3 ) throw new LangException(308,"I want 2 parameters: 1=number of seconds, 2=HHmmss-now, 3=hhmmss-beginning");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   b= (Vcar)vcar_list.get(1);
		Vcar   c= (Vcar)vcar_list.get(2);
		

		int seconds_since_begin = getSeconds(b.s) - getSeconds(c.s);
		int mod = seconds_since_begin % a.getInt();

		boolean result;
		if ( mod == 0 )
			result=true;
		else
			result=false;

		Vcar r = new Vcar(result);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		// TODO Auto-generated method stub
		return funn;
	}

	private int getSeconds(String hms ) {
		int hh = Commons.string2int(hms.substring(0, 2));
		int mm = Commons.string2int(hms.substring(2, 4));
		int ss = Commons.string2int(hms.substring(4, 6));	
		int ret = (hh*60*60) + (mm*60) + ss;
		
		return ret;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}