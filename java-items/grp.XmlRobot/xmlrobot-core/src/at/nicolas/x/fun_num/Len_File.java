/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_num;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Len_File implements IFun{

	
	private final static String funn = "len_file";
	private final static String pm = "len_file: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

				int result = 0;
		if ( vcar_list.size() < 1  || vcar_list.size() > 2 ) 
			throw new LangException(358,"I want one parameters: 1=filename [2=Encoding, Default UTF-8]");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		String fileconent = null;
		if ( vcar_list.size() == 1 )
			fileconent = CommonsVfs.readTextfileFormatLfDefault(a.s,"UTF-8",rh.getSitzung());
		else {
			fileconent = CommonsVfs.readTextfileFormatLfDefault(a.s, ((Vcar)vcar_list.get(1)).s,rh.getSitzung());
		}
		result = fileconent.length();
		
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