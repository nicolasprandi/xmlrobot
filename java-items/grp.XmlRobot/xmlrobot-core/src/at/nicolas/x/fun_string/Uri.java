/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.apache.commons.vfs2.FileObject;
import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Uri implements IFun {

	private static final String funn= "uri";
	private static final String pm= "function 'uri' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"wantw one parameter: 1=uri");
		
		String in = ((Vcar)vcar_list.get(1)).s;
		String what = ((Vcar)vcar_list.get(0)).s;
		//FileObject fo = CommonsVfs.getFileObjectByFilename(in);
		String ret ;
		/*
		if ( what.equals("uri")) {
			ret = fo.getPublicURIString();
			ret = fo.getURL()
			ret = fo.toString()
			ret = fo.getChildren()
			ret = fo.exists()
			ret = fo.getChildren()
			ret = fo.getName()
			ret = fo.getType()
			ret = fo.getName().getFriendlyURI()
					
		} */
		return null;
		/*
		String r = fo.getName().get
		if ( r== null)
			r="";
		return (new Vcar(r));
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