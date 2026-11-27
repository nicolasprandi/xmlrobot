/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class NbrBaseConvert implements IFun {

	
	private final static String funn="nbr_base_convert";
	private final static String pm="Function 'nbr_base_convert' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() < 3 ) 
			throw new LangException(350,pm+" want at least 3 parameters: 1=number, 2=source-base, 3=target-base [, 4=resultLen]");
		

	


		int baseSourceInt =  ((Vcar)vcar_list.get(1)).getInt();
		
		int baseTargetInt = ((Vcar)vcar_list.get(2)).getInt();	
		
		// int wert = 	Integer.valueOf(((Vcar)vcar_list.get(0)).s, baseSourceInt);
		
	
		
		String y= Integer.toString(Integer.parseInt(((Vcar)vcar_list.get(0)).s, baseSourceInt), baseTargetInt);
		
		
		if ( vcar_list.size() == 4 ) {
			// number, divisor, no scale
			y = Commons.leadSymbols(y, ((Vcar)vcar_list.get(3)).getInt(),"0");
			

		} 
		
		Vcar r = new Vcar(y);
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