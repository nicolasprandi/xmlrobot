/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Remove implements IFun {


	private static final String funn = "remove";
	private static final String pm = "remove: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		boolean result = false;
		
		if ( vcar_list.size() < 4 ) throw new LangException(301,pm+"I want 2 parameters: 1=type 2=all/once 3=string 4=search1 to search");
		
		String function = ((Vcar)vcar_list.get(0)).s;
		String frequence = ((Vcar)vcar_list.get(1)).s;
		boolean all = false;
		if ( frequence.equals("all"))
			all=true;
		
		String str = ((Vcar)vcar_list.get(2)).s;
		
		boolean c = true;
		int cnt = 0;
		Iterator<Vcar> i = vcar_list.iterator();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd_HHmmss");
		while ( i.hasNext() && c ) {
			Vcar x = (Vcar)i.next();
			
			String dateString = formatter.format(new Date());
			Sp.p("in der schleife "+dateString);
			cnt++;
			if ( cnt > 3 ) {
				String para = x.s;
				boolean did = false;
				if ( function.equals("after_last")) {
					int li = str.lastIndexOf(para);
					if ( li > -1 ) {
						str = str.substring(0, li);
						did = true;
					}
					
				} else if ( function.equals("content")) {
					int li = str.indexOf(para);
					if ( li > -1 ) {
						str = str.substring(0, li)+str.substring(li+para.length());
						did = true;
					}
					
				}
				
				if ( did && !all ) {
					c = false;
				}
				
			}
				
			
			
		}
		
		
		Vcar r = new Vcar(str);
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