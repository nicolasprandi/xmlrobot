/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Bin implements IFun {

	
	private final static String funn = "bin";
	private final static String pm = "bin: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		
		//lc.log_ext("bin hat mehr .... size="+vcar_list.size(),this );
		if ( vcar_list.size() < 1 ) throw new LangException(345,"I want one parameters: 1=number");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		int i = a.getInt();
		String result = Integer.toBinaryString(i);
		if ( vcar_list.size() > 1 ) {
			//lc.log_ext("hat .... mehr ....",this);
			Vcar   b= (Vcar)vcar_list.get(1);
			int len = b.getInt();
			//lc.log_ext("requested len="+len,this);
			if (result.length()>len)
				throw new Exception (pm+"more binarys to show than length, result="+result+", len="+len);
			String dazu = Commons.StringTimes("0", len-result.length());
		
			result = dazu + result;
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