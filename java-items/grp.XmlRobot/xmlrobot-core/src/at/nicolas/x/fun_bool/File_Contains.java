/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class File_Contains implements IFun {

	
	private final static String funn= "file_contains";
	private final static String pm= "function 'file_contains' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
						
		if ( vcar_list.size() < 2 || vcar_list.size() > 3 ) 
			throw new LangException(313,pm+"wants 2/3 parameters: [1]=Filename [2]=Text-to-Search [3=Encoding, Default UTF-8]");
		
		// lc.warn("size="+ vcar_list.size());
		Vcar filename = (Vcar)vcar_list.get(0);
		if ( !CommonsVfs.existsFile(filename.s,rh.getSitzung())) 
			throw new Exception(pm+": File does not exist: '"+filename.s+"'");
				
		String format = "UTF-8";
		if ( vcar_list.size() == 3 )
			format = ((Vcar)vcar_list.get(2)).s;
		
		String s1 = CommonsVfs.readTextfileFormatLfDefault(filename.s,format,rh.getSitzung());
		
		boolean result = false;
		Vcar search = (Vcar)vcar_list.get(1);
	
		if ( s1.indexOf(search.s) > -1 ) {
			result = true;
		}
		Vcar r = new Vcar(result);
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