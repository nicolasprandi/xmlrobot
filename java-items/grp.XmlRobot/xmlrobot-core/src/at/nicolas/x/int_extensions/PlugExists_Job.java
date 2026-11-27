/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.Vcar;

public class PlugExists_Job implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		boolean ret = false;	
		if ( vcar_list.size() != 2 ) 
			throw new Exception("exists_job: I want 1 parameter: 1=app-id 2=job-id");
		
		Vcar   a= (Vcar)vcar_list.get(0);
		Vcar   b= (Vcar)vcar_list.get(1);
		DbApp requested_app = CoreBuilder.db.getAppById(a.s);
		if ( requested_app == null ) {
			throw new Exception("The App "+a.s+" does not exist");
		}
		DbJob requested_job = requested_app.getJm().selectJob(b.s);
		if ( requested_job != null)
			ret = true;
		else 
			ret = false;
		
		return (new Vcar(ret));
	}




	public void help(ArrayList<Vcar> vcar_list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}