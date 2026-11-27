/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_misc;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Matematico implements IFun {

	private final static String funn="math";
	private final static String pm =" function 'math' ";
	
	private static final int act_abs   = 0;
	private static final int act_beauty   = 1;
	private static KeyWordMapper tasknames;
	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(340,pm+"wants at least 1 parameter: [1] abs|beauty ");
		

		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_abs: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(341,pm+" variation 'abs' wants a second parameter: [2] number");			
			Vcar xv = (Vcar)vcar_list.get(1);
			
			BigDecimal result = (xv.getNumber()).abs();
			ret = new Vcar(result);
			ret.dontImport();
			break;
		}
		case act_beauty: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(342,pm+" variation 'beauty' wants a second parameter: [2] number");			
			BigDecimal result = ((Vcar)vcar_list.get(1)).getNum;
			
			ret = new Vcar(result.toString());
			ret.dontImport();
			break;
		}

		} 
					
		return (ret);
	}

	public String getExtensionId() {
		return "math";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Fun-Math",2);
		tasknames.put("abs", act_abs);
		tasknames.put("beauty", act_beauty);
		tasknames.putFinished();		
	}

}