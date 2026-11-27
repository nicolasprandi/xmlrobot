/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Round implements IFun{

	private final static String funn="round";
	private final static String pm ="function 'round' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() == 0) 
			throw new LangException(367,pm+" wants at least one parameter: 1=number [2=scale/number of decimals default 0] [3=Rounding Method]");
		
		BigDecimal x = ((Vcar)vcar_list.get(0)).getNum;
		BigDecimal y;
		
		switch ( vcar_list.size() ) {
		case 1: {	
			y= x.setScale(0,BigDecimal.ROUND_HALF_UP);
			break;
		}
		case 2: {	
			y=x.setScale(((Vcar)vcar_list.get(1)).getInt(),BigDecimal.ROUND_HALF_UP);
			break;
		}
		case 3: {	
			y=x.setScale(((Vcar)vcar_list.get(1)).getInt(),
					Commons.RoundMethodCode(((Vcar)vcar_list.get(2)).s));
			break;
		}
		default: {
			throw new LangException(368,pm+" found wrong number of arguments");
		}
		} 
		
	
		Vcar r = new Vcar(y);
		r.dontImport();
		return r;
	}


	public String getExtensionId() {
		return funn;
	}
	


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}