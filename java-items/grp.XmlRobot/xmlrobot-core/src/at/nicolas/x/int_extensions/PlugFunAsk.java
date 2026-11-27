/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_extensions;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class PlugFunAsk implements IFun {


	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(211,"ask: wants at least 1 parameter: [1] app_dir|pro_dir|exists_job|pro_data_dir");
		
		String  variation= ((Vcar)vcar_list.get(0)).s;
		
		if ( variation.equals("app_dir")) {
			if ( vcar_list.size() != 2 ) 
				throw new LangException(212,"ask: variation 'app_dir' wants a second parameter: [2] app-id");			
			String app_dir = CoreBuilder.db.getFullDirApp(((Vcar)vcar_list.get(1)).s);
			if ( app_dir == null )
				throw new LangException(213,"ask: did not find app '"+((Vcar)vcar_list.get(1)).s+"'");
			ret = new Vcar(app_dir);
		} else if ( variation.equals("app_has_static_pro")) {
			if ( vcar_list.size() != 2 ) 
				throw new LangException(212,"ask: variation 'app_has_static_pro' wants a second parameter: [2] app-id");			
			
			Vcar   app_id= (Vcar)vcar_list.get(1);
			DbApp requested_app = CoreBuilder.db.getAppById(app_id.s);
			boolean antwort = false;
			if (requested_app.app_has_static_pro())
				antwort = true;
			ret = new Vcar(antwort);
		}  else 	if ( variation.equals("pro_dir")) {
			if ( vcar_list.size() != 3 ) 
				throw new LangException(214,"ask variation 'pro_dir' wants a second parameter [2] app-id and third paramter [3] pro-id");			
			String pro_dir = CoreBuilder.db.getFullDirInst(((Vcar)vcar_list.get(1)).s,((Vcar)vcar_list.get(2)).s);
			if ( pro_dir == null )
				throw new Exception("#504 ask did not find app '"+((Vcar)vcar_list.get(1)).s+"' combined with pro '"+((Vcar)vcar_list.get(2)).s+"'");
			ret = new Vcar(pro_dir);
		} else 	if ( variation.equals("pro_data_dir")) {
			if ( vcar_list.size() != 3 ) 
				throw new LangException(999,"ask variation 'pro_data_dir' wants a second parameter [2] app-id and third paramter [3] pro-id");			
			String pro_data_dir = CoreBuilder.db.getFullProDataDir(((Vcar)vcar_list.get(1)).s,((Vcar)vcar_list.get(2)).s);
			if ( pro_data_dir == null )
				throw new Exception("#999 ask did not find app '"+((Vcar)vcar_list.get(1)).s+"' combined with pro '"+((Vcar)vcar_list.get(2)).s+"'");
			ret = new Vcar(pro_data_dir);
		}  else 	if ( variation.equals("exists_job")) {
			if ( vcar_list.size() != 3 ) 
				throw new Exception("#505 ask variation 'exists_job' wants a second parameter [2] app-id and third paramter [3] job-id");			
			Vcar   app_id= (Vcar)vcar_list.get(1);
			Vcar   job_id= (Vcar)vcar_list.get(2);
			DbApp requested_app = CoreBuilder.db.getAppById(app_id.s);
			if ( requested_app == null ) {
				throw new Exception("#506 ask variation 'exists_job', the app '"+(app_id.s)+"' does not exist");
			}
			DbJob requested_job = requested_app.getJm().selectJob(job_id.s);
			if ( requested_job != null)
				ret = new Vcar(true);
			else 
				ret = new Vcar(false);
		} else 	if ( variation.equals("exists_app")) {
			if ( vcar_list.size() != 2 ) 
				throw new Exception("#505b ask variation 'exists_app' wants a second parameter [2] app-id and third paramter");			
			Vcar   app_id= (Vcar)vcar_list.get(1);

			DbApp requested_app = CoreBuilder.db.getAppById(app_id.s);
			if ( requested_app != null)
				ret = new Vcar(true);
			else 
				ret = new Vcar(false);

		}  else if ( variation.equals("pro_home")) {
			Vcar   app_id= (Vcar)vcar_list.get(1);
			
			DbApp requested_app = CoreBuilder.db.getAppById(app_id.s);
			if ( requested_app == null ) {
				throw new Exception("#506 ask variation 'pro_home', the app '"+(app_id.s)+"' does not exist");
			}
			String pro_home_dir = requested_app.getProjectHomeDir();
			
			if ( pro_home_dir == null )
				throw new Exception("#504 ask did not find project-home for '"+((Vcar)vcar_list.get(1)).s+"'");
			ret = new Vcar(pro_home_dir);
		} else {
			throw new Exception("#507 ask has unknown variation '"+variation+"' use: app_dir, pro_dir, exists_job, exists_app");
		}
		
		
					
		return (ret);
	}


	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}

}