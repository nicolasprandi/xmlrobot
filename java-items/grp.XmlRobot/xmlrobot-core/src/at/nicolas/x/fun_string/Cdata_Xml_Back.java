/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Cdata_Xml_Back implements IFun {

	
	private static final String wurst1 ="s-38-83=4z";
	private static final String wurst2 ="r-idr-423";
	private static final String wurst3 ="ts+k-sld3-";
	private static final String wurst4 ="sa#sdfl34-";
	private static final String wurst5 ="sa#su=3ifl3-";
	
	String funn;
	String pm;
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		this.funn = "cdata_xml_back";
		this.pm = funn+": ";
				
		if ( vcar_list.size() != 1 ) 
			throw new LangException(pm+"I want one parameter: 1=text");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		
		String worker = run(a.s);

				
		return (new Vcar(worker));
	}
	
	public static String run ( String in ) {
		
		String worker = new String(in);
		worker = Commons.stringReplace(worker,"&amp;",wurst1);
		worker = Commons.stringReplace(worker,"&lt;",wurst2);
		worker = Commons.stringReplace(worker,"&gt;",wurst3);
		worker = Commons.stringReplace(worker,"&quot;",wurst4);
		worker = Commons.stringReplace(worker,"&apos;",wurst5);
		
		worker = Commons.stringReplace(worker,wurst1,"&");
		worker = Commons.stringReplace(worker,wurst2,"<");
		worker = Commons.stringReplace(worker,wurst3,">");
		worker = Commons.stringReplace(worker,wurst4,"\"");
		worker = Commons.stringReplace(worker,wurst5,"'");
		return worker;
		
	}


	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}