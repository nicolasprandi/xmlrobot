/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */
package at.nicolas.x.fun_bool;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;


public class Right implements IFun  {

	
	private final static String funn = "right";
	private final static String pm = "right: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		if (true ) {
			String requestedRight = ((Vcar)vcar_list.get(0)).s;
			System.out.println(">> REQUESTED RIGHT <<  >> << "+requestedRight);
		Vcar rx = new Vcar(true);
		rx.dontImport();
		return rx;
		} else {
		boolean ret = false;	
		
		if ( vcar_list.size() != 1 ) throw new LangException(303,"I want 1 parameters: 1=requested right");
		String requestedRight = ((Vcar)vcar_list.get(0)).s;
		if ( rh.getSitzung().isOnline() ) {
			// online
			ret = rh.getSitzung().hasRight(requestedRight);
			// System.out.println("requested right ("+requestedRight+") is "+ret);
			Vcar r = new Vcar(ret);
			r.dontImport();
			return r;
		} else {
			// offline
			String currentApp = rh.getAppId();
			// das sollte genau gleich in reco eingebaut werden koennen damit
			// die rights durch die prodconf gemacht werden koennen
			/*[replace-right]*/
			
			/*[replace-right-off-if]*/   if ( currentApp.equals("Hibiscus")) { ret = Commons.hasFlag("intmydb agent mvexp mymenu spi spr ipr ipi cust ref pdi db repo int install load stat details clear convert mv import explain sqli xls xlsx dev source index fork",requestedRight); } 
			/*[replace-right-off-if]*/	 else if ( currentApp.equals("Arc-Tomcat")) { ret = Commons.hasFlag("testArcTomcat",requestedRight); }
			/*[replace-right-off-else]*/ else
			 							   ret = false;
			
			/*[replace-right-off-print]*/ System.out.println("Requested right ["+((Vcar)vcar_list.get(0)).s+"] for currentApp ["+currentApp+"] is "+ret);
			Vcar r = new Vcar(ret);
			r.dontImport();
			return r;
		}
		
		}

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