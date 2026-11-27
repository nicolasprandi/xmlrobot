/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugRole implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {

		Vcar retCar = null;
		if ( vcar_list.size() == 0 ) {
			// kein parameter
			// rh.getRoleOverall()
			//CoreBuilder.ws.getRole()
			
			retCar = new Vcar(rh.getSitzung().getRoleName());
			
		} else if ( vcar_list.size() == 1 ) {
		
			String r_role = ((Vcar)vcar_list.get(0)).s;
			int r_role_h = Rollen.getRoleHeightDefaultFail(r_role);
			
			// int c_role_h = Rollen.getRoleHeightDefaultUser(CoreBuilder.ws.getRole());
			int c_role_h = rh.getSitzung().getRoleIndex();
			// System.out.println("*** c="+c_role_h+" r="+r_role_h);
			boolean ret = false;
			
			if ( c_role_h >= r_role_h)
				ret = true;
			retCar =(new Vcar(ret));
		} else if ( vcar_list.size() == 2 ) {
		
			String r_role = ((Vcar)vcar_list.get(0)).s;
			int r_role_h = Rollen.getRoleHeightDefaultFail(r_role);
			
			// int c_role_h = Rollen.getRoleHeightDefaultUser(CoreBuilder.ws.getRole());
			String c_role = ((Vcar)vcar_list.get(1)).s;
			int c_role_h = Rollen.getRoleHeightDefaultFail(c_role);
			
			// int c_role_h = rh.getSitzung().getRoleIndex();
			// System.out.println("*** c="+c_role_h+" r="+r_role_h);
			boolean ret = false;
			
			if ( c_role_h >= r_role_h)
				ret = true;
			retCar =(new Vcar(ret));
		}
		retCar.dontImport();
		return retCar;
	
	}



	public void help(ArrayList<Vcar> vcar_list) {
		// TODO Auto-generated method stub
		
	}
	
	public static boolean hasRole ( String requested_role ) {

		int r_role_h = Rollen.getRoleHeightDefaultUser(requested_role);
		
		int c_role_h = Rollen.getRoleHeightDefaultUser(CoreBuilder.ws.getRole());

		
		if ( c_role_h >= r_role_h)
			return true;
		else 
			return false;
			
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}