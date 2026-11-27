/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;



import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Count_Flag_In_Flag implements IFun{

	private final static String funn = "count_flag_in_flag";
	private final static String pm = "count_flag_in_flag: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {


		if ( vcar_list.size() != 2 ) 
			throw new LangException(347,"I want 2 parameters, 1=flag , 2=flag");

		String   a= ((Vcar)vcar_list.get(0)).s;
		String   b= ((Vcar)vcar_list.get(1)).s;
		
		int sum = 0;
		
		if ( !a.trim().equals("") ) {
			String[] a_parts = a.split("\\s+");
			for (  String x_part: a_parts ) {
				if ( Commons.hasFlag( b, x_part ) ) {
					sum++;
				} 

			}
		}

		Vcar r = new Vcar(sum);
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