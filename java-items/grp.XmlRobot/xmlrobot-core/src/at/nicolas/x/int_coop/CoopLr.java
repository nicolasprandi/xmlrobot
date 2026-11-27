/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class CoopLr implements IFun{

	String funn;
	String pm;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "loop_regex";
		this.pm = funn+": ";
		/*[echnaton-crypto]*/
		if ( vcar_list.size() < 1 ) 
			throw new LangException(216,pm+"I want at least 1 parameter: loop-id ");

		
		String id = ((Vcar)vcar_list.get(0)).s;
		
		CoopLoop_Regex loop_regex = (CoopLoop_Regex)rh.getNo(id, Wellknown.coop_type_loop_regex);
		if ( loop_regex == null ) {
			throw new LangException(217,pm+"The loop_regex with id '"+id+"' was not found");
		}
		Vcar result = null;
		if ( vcar_list.size() == 1 )
			result = loop_regex.loopRegex.lrValue(vcar_list);
		else if ( vcar_list.size() == 2 ) {
			String function = ((Vcar)vcar_list.get(1)).s;
			if ( function.equals("has_next?")) {
				boolean hasNext = loop_regex.loopRegex.hasNext();
				if ( rh.shouldBreakLoops())
					hasNext = false;
				result = new Vcar(hasNext);
			} else 
				result = loop_regex.loopRegex.lrValue(vcar_list);			
		} else {
			throw new LangException(218,pm+"too much parameters");
		}

		
		return result;		

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