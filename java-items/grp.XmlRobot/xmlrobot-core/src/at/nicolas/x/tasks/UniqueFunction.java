/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;


import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;


public class UniqueFunction implements IFun{



	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		if ( vcar_list.size() != 2 ) 
			throw new Exception("function unique wants exaclty two parameter 1:id, 2:a value to be converted!");

		String id = ((Vcar)vcar_list.get(0)).s;
		String value = ((Vcar)vcar_list.get(1)).s;
		
		// Sp.p("/// UniqueFunction /// A /// id="+id+", value="+value);
				
		UniqueCoop uniqueCoop = (UniqueCoop)rh.getNo(id, UniqueCoop.type);
		// Sp.p("/// UniqueFunction /// B ///");
		if ( uniqueCoop == null ) {
			// Sp.p("/// UniqueFunction /// C ///");
			throw new Exception("function unique: the unique with id '"+id+"' does not exist!");
		}
		// Sp.p("/// UniqueFunction /// D ///");
		Vcar result = new Vcar(uniqueCoop.unique.getValue(value));
		Sp.p("/// UniqueFunction /// E ///");
		return result;		

	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}
