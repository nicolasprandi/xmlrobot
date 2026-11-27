/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Filecontent implements IFun {

	private static final String funn= "file_content";
	private static final String pm= "function 'file_content' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {
		
		if ( vcar_list.size() <1 || vcar_list.size() >3) 
			throw new LangException(pm+" wants 1, 2 or 3 parameters: 1=uri, 2=encoding [UTF-8], 3=linefeed-string [&#xA;] but i received "+vcar_list.size()+" Parameters");
		

		
		String filename = ((Vcar)vcar_list.get(0)).s;
		
		String encoding = "utf-8";
		if ( vcar_list.size() >= 2) 
			encoding = ((Vcar)vcar_list.get(1)).s;
		
		String linefeed = null;
		if ( vcar_list.size() >= 3) 
			linefeed = ((Vcar)vcar_list.get(2)).s;
		else
			linefeed = "\n";
		
		String filecontent = CommonsVfs.readTextfileFormatLfURI(filename, encoding, linefeed,rh.getSitzung());
				
		return (new Vcar(filecontent));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}