/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Time implements IFun{

	private static final String funn ="time";
	private static final String pm ="function 'time' ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {

		if ( vcar_list.size() > 2 ) throw new LangException(pm+"wants 0/1/2 parameters: 1=timeformat eg.yyyyMMdd_HHmmss or ? [, 2='job']");
		
		Date mydate = null;
		String myformat = null;
		
		if ( vcar_list.size()==0) {
			mydate = new Date();
			myformat = "yyyyMMdd_HHmmss";
		} else if ( vcar_list.size()==1) {
			mydate = new Date();
			myformat = ((Vcar)vcar_list.get(0)).s;
			if (myformat.equals("?"))
				myformat = "yyyyMMdd_HHmmss";
		} else {
			
			myformat = ((Vcar)vcar_list.get(0)).s;
			if (myformat.equals("?"))
				myformat = "yyyyMMdd_HHmmss";
			String second = ((Vcar)vcar_list.get(1)).s;
			if ( second.equals("job")) {
				mydate = rh.getJobdate();
			} else {
				throw new LangException(this.funn+" says: The second parameter can only be 'job' but it is '"+second+"'");
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat (myformat);
		String dateString = formatter.format(mydate);

		return (new Vcar(dateString));
	}

	public String getExtensionId() {
		return funn;
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}