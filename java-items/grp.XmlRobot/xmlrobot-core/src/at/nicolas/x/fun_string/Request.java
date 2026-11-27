/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Request implements IFun{

	private static final String funn= "request";
	private static final String pm= "function 'request' ";
	/*
	 * 1: action:     choose
	 * 2: type:       file|dir
	 * 3: requestor:  is-on
	 *                write-on
	 *                get-tip
	 *                get-label
	 *                get-extensions
	 *                  
	 * (non-Javadoc)
	 * @see at.nicolas.x.IFun#execute(java.util.ArrayList, at.nicolas.x.IRequestHandler, at.nicolas.x.ILogConnector)
	 */
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() != 2 ) 
			throw new LangException("I want 3 parameters, 1=type[dir|file] , [keyname]");

		String type = ((Vcar)vcar_list.get(0)).s;
		String keyname   = ((Vcar)vcar_list.get(1)).s;

		String retString = null;
		try {
			retString = rh.request(type, keyname);
			
		} catch (Exception e) {
			retString = "";
		}
		Vcar ret = new Vcar(retString);
		ret.dontImport();
		return ret;

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