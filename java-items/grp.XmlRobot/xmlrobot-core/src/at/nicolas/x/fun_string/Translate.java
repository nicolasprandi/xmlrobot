/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Translate implements IFun {

	private static final String funn="translate";
	private static final String pm="function 'translate' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 3 ) 
			throw new LangException(pm+"I want 3 parameters: 1=the value 2=before action [1:export_before 0:nothing] 3=after action [1:dont_import_after 0:nothing]");
		
		String value = ((Vcar)vcar_list.get(0)).s;
	
		boolean export_before = ((Vcar)vcar_list.get(1)).getBool();
		boolean dont_import_after = ((Vcar)vcar_list.get(2)).getBool();
		
		//lc.trace("received '"+value+"' export_before '"+
		//			export_before+"' dont_import_after '"+dont_import_after+"'",this);
		// before
		if (export_before )
			value = Entities.exportEntities(value);
		
		// translate
		String ret = rh.translateValueSave(value);
		Vcar vcar_ret = new Vcar(ret);
		
		// after
		if (dont_import_after )
			vcar_ret.dontImport();	
		
				
		return (vcar_ret);
	}

	public String getExtensionId() {
		return funn;
	}
	


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}