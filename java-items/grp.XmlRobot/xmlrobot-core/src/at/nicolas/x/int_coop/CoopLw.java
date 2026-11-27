/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_coop;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;


public class CoopLw implements IFun{



	String funn;
	String pm;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "loop_while";
		this.pm = funn+": ";
		
		if ( vcar_list.size() < 2 ) 
			throw new Exception(pm+"I want 2 parameters, 1=loop-id,  2=function (has_next?, #i) ");

		String function = ((Vcar)vcar_list.get(1)).s;
		String id = ((Vcar)vcar_list.get(0)).s;
		
		CoopLoop_While loop_while = (CoopLoop_While)rh.getNo(id, Wellknown.coop_type_loop_while);
		if ( loop_while == null ) {
			throw new Exception(pm+"The loop_while with id '"+id+"' was not found");
		}
		Vcar result = null;
		if ( function.equals("has_next?")) {
			boolean hasNext = loop_while.loopWhile.hasNext();
			if ( rh.shouldBreakLoops())
				hasNext = false;
			result = new Vcar(hasNext);
		} else
			result = loop_while.loopWhile.lwValue(function);
			
		/*
		else if ( function.equals("value")) {
			result = loop_while.loopWhile.lwValue(vcar_list);
		} else 
			throw new Exception(pm+"The loop_while function-request '"+function+"' does not exist");
		*/
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