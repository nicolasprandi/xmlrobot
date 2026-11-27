/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_coop.CoopSpirit;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class KeyTargetReport implements ITask{

	String val;
	String out;
	String home;

	String tname;
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "key_target_report";

		String in_file;
		if ( e.getAttribute("in_file")!=null)
			in_file = e.getAttributeValue("in_file");
		else 
			throw new LangException(tname+" needs @in_file which is the target-file");
		
		String out_spirit;
		if ( e.getAttribute("out_spirit")!=null)
			out_spirit = e.getAttributeValue("out_spirit");
		else 
			throw new LangException(tname+" needs @out_spirit which holds the spirit-id of the results. This spirit will be created.");		

		Element targetContent = CommonsVfs.getElementByFilename(in_file, rh.getSitzung());

		
		KeyTargetRegister keyTargetRegister= new KeyTargetRegister();
		Element result = keyTargetRegister.go(targetContent);
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