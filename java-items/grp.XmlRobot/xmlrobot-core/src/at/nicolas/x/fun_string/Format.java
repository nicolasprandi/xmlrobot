/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Format implements IFun{

	private static final String funn= "format";
	private static final String pm= "function 'format' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() < 1 || vcar_list.size() > 3 ) 
			throw new LangException("I want 1/2/3 parameters, 1=number [, 2=format, default(?)=,##0.00] [, 3=Locale]");

		Vcar   a= (Vcar)vcar_list.get(0);
		BigDecimal x = a.getNumber();
		
		String format = "?";
		if (vcar_list.size() > 1 ) {			
			format = ((Vcar)vcar_list.get(1)).s;
		} 
		if ( format.equals("?"))
			format = ",##0.00";
		
		String locale = null;
		if (vcar_list.size() > 2 ) {			
			locale = ((Vcar)vcar_list.get(2)).s;
		} 
		
		DecimalFormat df = null;
		if ( locale == null ) {
			df=  new DecimalFormat  ( format );			 			
		} else {
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols( new Locale(locale));
			df = new DecimalFormat(format, otherSymbols);
		}
		
		String result =  df.format(x)  ;
		return new Vcar(result);

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