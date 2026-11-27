/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Div implements IFun {

	
	private final static String funn="div";
	private final static String pm="Function 'div' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() < 2 ) 
			throw new LangException(350,pm+" want at least 2 parameters: 1=dividend, 2=divisor [,3=scale, 4=roundmethod]");
		
		BigDecimal value = ((Vcar)vcar_list.get(0)).getNumber();
		BigDecimal result;
		if ( vcar_list.size() == 2 ) {
			// number, divisor, no scale
			
			try {
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber());
			} catch (ArithmeticException ex) {
				/*
				lc.log_ext("doing division with precision/scale '"
						+rh.getMathScale()+"' and roundmethod '"+
						Commons.RoundMethodText(rh.getMathRoundMethod())+"'", this);
						*/
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber(), 
						rh.getMathScale(), 
						rh.getMathRoundMethod());
			}
		} else if ( vcar_list.size() == 3 ) {
			// number, divisor, scale
			try {
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber());
			} catch (ArithmeticException ex) {
				/*
				lc.log_ext("doing division with precision/scale "+((Vcar)vcar_list.get(2)).s
						+"' and roundmethod '"+
						Commons.RoundMethodText(rh.getMathRoundMethod())+"'", this);
						*/
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber(), 
						((Vcar)vcar_list.get(2)).getInt(), 
						rh.getMathRoundMethod());
			}
		} else if ( vcar_list.size() == 4 ) {
			// number, divisor, scale, round method
			try {
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber());
			} catch (ArithmeticException ex) {
				/*
				lc.log_ext("doing division with precision/scale "+((Vcar)vcar_list.get(2)).s
						+"' and roundmethod '"+
						((Vcar)vcar_list.get(3)).s+"'", this);
						*/
				result = value.divide(((Vcar)vcar_list.get(1)).getNumber(), 
						((Vcar)vcar_list.get(2)).getInt(), 
						Commons.RoundMethodCode(((Vcar)vcar_list.get(3)).s));
			}
		} else
			throw new LangException(351,pm+" does not want more than 4 parameters");
		
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