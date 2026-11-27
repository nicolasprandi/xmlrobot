/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_coop.CoopSpirit;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class KeySourceReport implements ITask{

	String val;
	String out;
	String home;

	String tname;
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "key_source_report";

		String in_spirit;
		if ( e.getAttribute("in_spirit")!=null)
			in_spirit = e.getAttributeValue("in_spirit");
		else 
			throw new LangException(tname+" needs @in_spirit which holds the spirit-id of the input-source");
		
		String out_spirit;
		if ( e.getAttribute("out_spirit")!=null)
			out_spirit = e.getAttributeValue("out_spirit");
		else 
			throw new LangException(tname+" needs @out_spirit which holds the spirit-id of the results. This spirit will be created.");		

		CoopSpirit spirit = (CoopSpirit)rh.getNo(in_spirit, Wellknown.coop_type_spirit);
		if ( spirit == null ) {
			throw new LangException(tname+"The spirit with id '"+in_spirit+"' was not found");
		}
		//spirit.getDoc().getRootElement()
		
		KeySourceRegister keySourceRegister= new KeySourceRegister();
		Element result = keySourceRegister.go(spirit.getDoc().getRootElement());
		CoopSpirit coopSpirit = CoopSpirit.createSpirit(
				out_spirit, 
				result, 
				rh);
		return null;
	}
	


	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}


}