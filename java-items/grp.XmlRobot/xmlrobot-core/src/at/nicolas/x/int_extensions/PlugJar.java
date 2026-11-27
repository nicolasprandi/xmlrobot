/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugJar extends Object implements IFun {
	
	public static String getJarFile() {
		PlugJar jar = new PlugJar();
		Class cls = jar.getClass();
		ProtectionDomain p = cls.getProtectionDomain();
		String result = p.getCodeSource().getLocation().getPath();
		try {
			result = URLDecoder.decode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			
			Sp.pe(e);
		}
		return result;
	}
	

	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		this.getClass();
		rh.depressive("function 'jar' does not exist anymore, (f,info,");
		
		
		Class cls = this.getClass();
		if ( vcar_list.size()>0) {
			Vcar   a= (Vcar)vcar_list.get(0);
			cls = Class.forName(a.s);
		}

		 ProtectionDomain p = cls.getProtectionDomain();
		 
		String result = p.getCodeSource().getLocation().getPath();
		try {
			result = URLDecoder.decode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}		
		if ( CoreBuilder.ws.isWin && result.startsWith("/"))
			result = result.substring(1);
		
		return (new Vcar(result));
	}



	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}