/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.LogLogger;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class PlugInterpreter implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		
		
		if ( vcar_list.size() < 1 ) 
			throw new LangException("interpreter wants at least one parameter: [1] variation: level|jovial|strategy|math_scale|math_round");

		String r;
		
		Vcar   p= (Vcar)vcar_list.get(0);

		if ( p.s.equals("level")) {
			r = LogLogger.level2String(CoreBuilder.rslogger.getLevel());			
		} else if ( p.s.equals("jovial")) {
			r = Commons.boolToStringXr(rh.isJovial());
		} else if ( p.s.equals("math_scale")) {
			r = Integer.toString(rh.getMathScale());
		} else if ( p.s.equals("math_round")) {
			r =Commons.RoundMethodText(rh.getMathRoundMethod());
		} else if ( p.s.equals("has_priv")) {
			r =Commons.boolToStringXr(rh.hasPriv(((Vcar)vcar_list.get(1)).s));
		} else if ( p.s.equals("get_priv_para")) {
			r = rh.getPrivPara(((Vcar)vcar_list.get(1)).s);
		} else if ( p.s.equals("strategy")) {
			if ( vcar_list.size() != 2 ) 
				throw new LangException("interpreter strategy needs a second parameter with the strategy-name");
			Vcar s = (Vcar)vcar_list.get(1);
			r = CoreBuilder.ws.getStrategy().getStrategy(s.s);
			if ( r == null )
				r = "";
		} else if ( p.s.equals("lang")) {
			r = rh.getSitzung().lang().getLangCode();
		} else if ( p.s.equals("turbo")) {
			r = Commons.boolToString(rh.getTurbo());
		} else if ( p.s.equals("lang_desc_international")) {
			r =rh.getSitzung().lang().getLangDescInternational();
		} else
			throw new LangException("interpreter found unknown variation '"+p.s+"'");			

				
		return (new Vcar(r));
	}

	private String getDefault() {
		return Wellknown.copyright;
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}