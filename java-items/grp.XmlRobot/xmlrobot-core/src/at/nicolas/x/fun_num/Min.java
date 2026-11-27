/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Min implements IFun {

	
	private final static String funn = "min";
	private final static String pm = "min: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				
		if ( vcar_list.size() < 2 ) 
			throw new LangException(361,pm+"I want at least 2 parameter: 1=number, 2=number [,n=number]");
		
		BigDecimal result = new BigDecimal(0);
		boolean fresh = true;
		Iterator<Vcar> i = vcar_list.iterator();
		// lc.info(pm+" result="+Commons.double2string(result), this);
		while ( i.hasNext() ) {
			Vcar x = (Vcar)i.next();
			x.mustNumber();
			
			if ( fresh ) {
				result = x.getNumber();
				fresh = false;
			} else {
				// if x < result
				if ( (x.getNumber()).compareTo(result) == -1 )
					result = x.getNum;
			}
			
		}
		// TODO Auto-generated method stub
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