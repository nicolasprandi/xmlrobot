/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class Round2 implements IFun{

	private static final String funn = "round2";
	private static final String pm = "round2: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				double result = 0;
		throw new Exception("round2 not implementd");
		/*
		if ( vcar_list.size() == 0 ) throw new Exception("I want 1/2 parameters, 1=number, 2=length");
		
		
		Vcar   a= (Vcar)vcar_list.get(0);
		BigDecimal x = a.getNumber();
		Double X = new Double(x);
		int back = X.intValue();
		
		Vcar vc_return = new Vcar(back); 
		vc_return.calculate();
		
		// the int is now done, now look for second-paraemter
		if ( vcar_list.size() > 1 ) {
			
			Vcar vc_b = (Vcar)vcar_list.get(1);
			int len = vc_b.getInt();
			lc.log_ext("received len="+len, this);
			
			String will_give = Commons.int2String(back);
			lc.log_ext("will_give="+will_give, this);
			int found_point = will_give.indexOf(".");
			if ( will_give.indexOf(".")>-1) {
				// hat einen punkt
			} else {
				// hat keinen punkt
				will_give = will_give.concat(".00");
			}
			
			vc_return = new Vcar(will_give); 
			vc_return.calculate();
			
			
		}
		
	
		
		return (vc_return);
		*/
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}