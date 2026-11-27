/* Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;


public class DbApp implements Comparable<DbApp>{
	
	private String id ;
	private String dir;
	private String idEscaped ;
	private String dirEscaped;	
	private String label;

	private ArrayList<DbPro> instList = null;
	private DbJm jm = null;
	
	private String pro_home_dir ;
	private String pro_home_dirEscaped ;
	private boolean is_hidden;
	
	private boolean isServiceApp;
	
	private String app_role = null;
	private String app_tip = null;
	private String projectDataDir = null;
	private boolean app_has_static_pro = false;
	private String app_sort = null;
	
	private String commitsPath = "";
	private String cachePath = "";
	

	
	//
	// id und dirRel
	//

	public boolean app_has_static_pro() {
		return app_has_static_pro;
	}
	
	public boolean app_has_static_pro_real() throws FileSystemException {
		if ( app_has_static_pro ) {
			
			String pruefe = pro_home_dir +"pro_standard";
			return CommonsVfs.existsDir(pruefe,appSitzung);			
		} else
			return false;
	}	

	public String getProjectDataDir() {
		return projectDataDir;
	}

	public boolean isServiceApp() {
		return isServiceApp;
	}

	public Element getDbDump ( boolean natural) {
		Element x = new Element("app");
		
		if ( natural ) {
			x.setAttribute("id",id );
			x.setAttribute("dir",dir );
			if ( app_tip !=null) 
				x.setAttribute("tip", app_tip);
			x.setAttribute("pro_home_dir",pro_home_dir);
			if ( projectDataDir != null )
				x.setAttribute("pro_data_dir", projectDataDir);	

		} else {
			x.setAttribute("id",idEscaped );
			x.setAttribute("dir",dirEscaped );
			if ( app_tip !=null) 
				x.setAttribute("tip", Entities.importEntities(app_tip));
			x.setAttribute("pro_home_dir",pro_home_dirEscaped);	
			if ( projectDataDir != null )
				x.setAttribute("pro_data_dir", Entities.importEntities(projectDataDir));

		}
		if ( isServiceApp )
			x.setAttribute("sa","1" );
		else
			x.setAttribute("sa","0" );
		
		x.setAttribute("role", app_role);
		
		if ( app_has_static_pro )
			x.setAttribute("sp","1" );
		else
			x.setAttribute("sp","0" );	
		x.setAttribute("sort",app_sort );

		ArrayList<DbPro> instList = getInstList();
		
		Iterator<DbPro> i2 = instList.iterator();
		while ( i2.hasNext()) {
			DbPro inst = (DbPro)i2.next();
			Element y = inst.getDbDump(natural,this);
			x.addContent(y);
		}
		
		// statisches project, ist schon bei der instanzenliste dabe
		/*
		if ( app_has_static_pro ) {
			String looking = pro_home_dir + "pro_standard";
			if ( Commons.existsDir(looking)) {
			
				
				Element stato = new Element("pro");
				stato.setAttribute("id", "Standard-Project");
				if ( natural ) {					
					stato.setAttribute("dir", pro_home_dir);
					stato.setAttribute("data_dir",pro_home_dir );
				} else {
					stato.setAttribute("dir", pro_home_dirEscaped);	
					stato.setAttribute("data_dir",pro_home_dirEscaped );
				}
				x.addContent(stato);
			}
		}
		*/

		return x;
	}		
		
	public String getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}	
	public void setId(String id) {
		this.id = id;
		this.idEscaped = Entities.importEntities(id);
	}
	public String getDir() {
		return dir;
	}
	public String getDirEscaped() {
		return dirEscaped;
	}	
	public void setDir(String _dir) {
		dir = _dir;
		this.dirEscaped = Entities.importEntities(_dir);
	}
	public boolean isHidden() {
		return is_hidden;
	}
	
	public String getCommitsPath() {
		return commitsPath;
	}
	
	public String getCachePath() {
		return cachePath;
	}	

	ISitzung appSitzung;
	public DbApp ( String _id, 
					String __dir, 
					DbJm _jm, 
					String __proHomeDir, 
					boolean _is_hidden, 
					String _label, 
					boolean _is_serviceApp,
					String _app_role,
					String _app_tip,
					String _projectDataDir,
					boolean _app_has_static_pro,
					String _commitsPath,
					String _cachePath,
					ISitzung _appSitzung,
					String _app_sort ) throws Exception {
		this.appSitzung = _appSitzung;
		this.commitsPath = _commitsPath;
		this.cachePath = _cachePath;
		this.app_has_static_pro = _app_has_static_pro;
		this.app_sort = _app_sort;
		/*[echnaton-crypto]*/
		id = _id;
		this.idEscaped = Entities.importEntities(id);
		dir = Commons.constructNormalizedDir(__dir);
		
		Sp.p("###### app ####################################");

		this.dirEscaped = Entities.importEntities(dir);

		instList = new ArrayList<DbPro>();
		jm = _jm;		
		pro_home_dir = Commons.constructNormalizedDir(__proHomeDir);
		pro_home_dirEscaped = Entities.importEntities(pro_home_dir);
		is_hidden = _is_hidden;
		label = _label;
		isServiceApp = _is_serviceApp;
		app_role = _app_role;
		app_tip = _app_tip;
		projectDataDir = _projectDataDir;
		
		
	}
	/*
	public DbApp ( String _id, String _dir, DbJm _jm, String _proHomeDir, boolean _is_hidden, boolean _is_serviceApp,
			String _app_role,
			String _app_tip ) {
		
		id = _id;
		dir = _dir;
		instList = new ArrayList<DbPro>();
		jm = _jm;		
		pro_home_dir = _proHomeDir;
		is_hidden = _is_hidden;
		label = _id;
		isServiceApp = _is_serviceApp;
		
	} */
	
	public String icon_file = null;
	public boolean hasIcon() throws FileSystemException {
		boolean ret = false;
		return ret;
	}
	public String getProjectHomeDir() {
		return pro_home_dir;
	}
	// #arenga#3#
	DbPro standardProject = null;
	public void addInst ( DbPro _inst,boolean isStandardProject ) {
		instList.add(_inst);
		if ( isStandardProject)
			standardProject = _inst;
	}
	
	public ArrayList<DbPro> getInstList() {
		return instList;
	}
	
	public DbJm getJm() {
		return jm;
	}
	
	public Element getExecutionProlog() {
		Element ret = new Element("config");
		/* wegdamit
		Element e99 = new Element("dir");
		e99.setAttribute("id", "pro_home");
		e99.setText(pro_home_dirEscaped);
		ret.addContent(e99);

		Element e98 = new Element("lock");
		e98.setAttribute("id", "pro_home");
		e98.setAttribute("type", "dir");
		ret.addContent(e98);		*/
		
		Element e1x = new Element("dir");
		e1x.setAttribute("id", "pro_home");
		e1x.setAttribute("flag", "lock");
		e1x.setText(dirEscaped);
		ret.addContent(e1x);		
		// (dir,app)
		Element e1 = new Element("dir");
		e1.setAttribute("id", "app");
		e1.setText(dirEscaped);
		e1.setAttribute("flag", "lock");
		ret.addContent(e1);			
		
		// (prop,app)
		Element e2 = new Element("prop");
		e2.setAttribute("id", "app");
		e2.setText(idEscaped);
		e2.setAttribute("flag", "lock");
		ret.addContent(e2);
		
	
		
		// (prop,is_service_app)
		/* wegdamit*/
		Element e2sa = new Element("prop");
		e2sa.setAttribute("id", "is_service_app");
		e2sa.setAttribute("flag", "lock");
		e2sa.setText(Commons.boolToStringXr(isServiceApp()));
		ret.addContent(e2sa);
		
		//Element l2sa = new Element("lock");
		//l2sa.setAttribute("id", "is_service_app");
		//l2sa.setAttribute("type", "prop");
		//ret.addContent(l2sa);				
		
		return ret;
	}
	
	public DbPro getInstById ( String id ) {
		
		DbPro inst = null;
		
		Iterator<DbPro> i2 = instList.iterator();
		while ( i2.hasNext()) {
			DbPro inst2 = (DbPro)i2.next();

			if ( inst2.getId().equals(id)) {
				inst = inst2;
				break;
			}
		}
		
		return inst;
	}
	
	public int getInstIndexById ( String instId ) {
		int result = -1;
		Iterator<DbPro> iterator = instList.iterator();
		while ( iterator.hasNext()) {
			result++;
			DbPro inst = (DbPro)iterator.next();
			if ( instId.equals(inst.getId()))
				break;
		}
		return result;
	}
	

	public int getInstIndexByIdGui ( String instId ) {
		int result = -1;
		Iterator<DbPro> iterator = instList.iterator();
		while ( iterator.hasNext()) {
			
			DbPro inst = (DbPro)iterator.next();
			if ( inst.getId().equals("Standard-Project")) {
				
			} else {
			result++;
			if ( instId.equals(inst.getId()))
				break;
			}
		}
		return result;
	}
	@Override
	public int compareTo(DbApp o) {
		//return this.id.toLowerCase().compareTo(o.id.toLowerCase());
		return this.app_sort.toLowerCase().compareTo(o.app_sort.toLowerCase());
	}		
	
	void sortProList() {
		Collections.sort(instList);

	}	

	
}
