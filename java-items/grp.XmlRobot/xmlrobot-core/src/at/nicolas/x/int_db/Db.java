/* Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Attribute;
import org.jdom2.Element;

import at.nicolas.x.int_extensions.PlugRole;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.MountConfig;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_setup.UniqueChecker;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;


/**
 * 5.11.2014 - ausbau der datenbank #NODB#
 *
 */
public class Db  {
	
	private boolean printWarnings = false;

	private LogConnector lc = new LogConnector ("Ws");
	private String databaseDir = null;
	private FileObject   database = null;

	private String servcieAppsDir = null;
	private FileObject   servcieApps = null;

	private String sysDir = null;
	private String dbl = null;
	private CommStartMsg sm = null;
	
	private ArrayList<DbApp> appList = null;
	private static HashMap<String, String> appLabelIdMap = null;
	private DbJm scJm = null;
	
	public ArrayList<DbApp> getAppList() {
		return appList;
	}

	public static String serviceAppId_to_Label(String app_id ) {
		// return ( "> "+app_id+".");
		String visulaApp = app_id;
		if ( CoreBuilder.ws.isRoleAdminUp())
			return ( visulaApp );
		else
			return ( visulaApp);
	}
	public static String standardAppId_to_Label(String app_id ) {
		String visulaApp = app_id;
		if ( CoreBuilder.ws.isRoleAdminUp())
			return ( visulaApp );
		else
			return (visulaApp);
	}	
	
	public static String label_to_AppId(String label ) {
		/*
		if ( label.endsWith("."))
			return label.substring(2,label.length()-1);
		else */
		/* vor map
		if ( CoreBuilder.ws.isRoleAdminUp())
			return label.substring(2);
		else
			return label;
		*/
		return (appLabelIdMap.get(label));
		
	}
	
	private boolean online;
	//private ISitzung systemSitzung;
	public void build( String _dbl, String _databaseDir, String _sysDir, String _serviceAppsDir, CommStartMsg _sm
			, boolean online,ISitzung systemSitzung) throws Exception  {
		this.online = online;
		//this.systemSitzung = systemSitzung;
		/*[echnaton-customer]*/	
		dbl = _dbl;
		databaseDir = _databaseDir;
		database = CommonsVfs.resolveFolderRead(databaseDir,systemSitzung);

		servcieAppsDir = _serviceAppsDir;
		servcieApps = CommonsVfs.resolveFolderRead(servcieAppsDir,systemSitzung);

		sm = _sm;
		
		sysDir = _sysDir;
		/*[echnaton-timer]*/	
		if ( dbl.equals("OH") || dbl.equals("HH") ) {
			
			try {
				/* #NODB#
				 * vormals:
				if ( dbl.equals("OH") )
					buildOH(sm);
				else
					buildHH(sm,true);		
					
				   jetzt:
				   buildOH(sm);
				   
				 */

					buildOH(sm,online,systemSitzung);

				sortAppList();
			} catch (Exception e) {
				sm.out.println("Seems to be a serious problem in database-building, layout='"+dbl+"'");
				sm.out.println("----- dir,database = "+databaseDir);
				sm.out.println("----- dir,system   = "+sysDir);
				Sp.pe(e);
				throw e;
			}
		} else {
			throw new Exception("Unknown Databaselayout '"+dbl+"'");
		}
		
		sm.out.println("Database Setup was successfull ");
		
		try {
			String fileIsEncrypted=
					Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.DIR_SERVICES))
					+ "topsecret.txt";
			boolean thisIsEncrypted=false;
			if ( CommonsVfs.existsFile(fileIsEncrypted,systemSitzung)) {
				thisIsEncrypted=true;
			}
			scJm = new DbJm ( Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.DIR_SERVICES)),
							CoreBuilder.ws.getRole(),CoreBuilder.ws.getLang(), CoreBuilder.ws.getOs(), CoreBuilder.ws.getComputer(),DbJob.V_LEVEL_INST,
							true,sm,false,thisIsEncrypted,systemSitzung);
		} catch (Exception e) {
			sm.out.println("Problem in Setting Up System");
			throw e;

		}
		// sm.out.println("");
	}
	public void appendAppsOnline(String wsDir,
			ISitzung systemSitzung ) throws FileSystemException, Exception {
		buildApps(CommonsVfs.resolveFolderRead(wsDir,systemSitzung), 
				"wsApps", sm, false,uc,false,systemSitzung);
	}
	private UniqueChecker uc= null;
	private void buildOH( CommStartMsg sm, boolean online,
			ISitzung systemSitzung ) throws  Exception   {
		appList = new ArrayList<DbApp>();
		appLabelIdMap = new HashMap<String, String>();
		uc = new UniqueChecker(sm,null);
		buildApps(servcieApps, "serviceApps", sm, true,uc,online,systemSitzung);
		buildApps(database, "wsApps", sm, false,uc,online,systemSitzung);
		 		
	}
	private void buildApps( 
			FileObject rootFile, 
			String type, 
			CommStartMsg sm, 
			boolean isServiceApp,
			UniqueChecker uc, 
			boolean online,
			ISitzung systemSitzung) throws  Exception   {
		
			String rootUri = rootFile.getName().getURI();

			if ( !rootFile.exists()) {
				// throw new Exception("database-direcory does not exist: "+rootFile.getCanonicalPath());
				CommonsVfs.mkdirCascade(rootFile,systemSitzung);
				sm.out.println("Created "+type+"-Dir: "+rootUri);
			}
			

			FileObject[] files = CommonsVfs.getNinos(rootFile,systemSitzung);		

			if ( files==null)
				throw new Exception("no applications in "+type+" "+rootUri);
			
			// Iteration on Applications
			for (int i=0; i<files.length; i++) {
				FileObject  einFile =  files[i];
				String einFileBasename = einFile.getName().getBaseName();
				String einFileUri = einFile.getName().getURI();
				
				
				if ( einFile.isFolder() && einFileBasename.startsWith("app.")) {
					String einFilePath = Commons.constructNormalizedDir(einFileUri);
					// einlesen von app-info.xml
					boolean is_hidden = false;
					boolean app_is_encrypted = false;
					boolean app_has_static_pro = false;
					String app_role = Rollen.getJobsDefaultRole();
					String appCommitsPath="";
					String appCachePath="";
					String app_tip = null;
					String appName = einFileBasename.substring(4);
					String appSort = einFileBasename.substring(4);
					if ( uc.isNew(appName, 
							"app '"+appName+"' located in "+einFileUri, 
							"Application '"+appName+"' could not be added because there is already one with that name, this one locates in "+
							einFileUri)) {
						

					String aifn = einFilePath+"app-info.xml";
					FileObject appinfo = CommonsVfs.grantsFileRead(aifn,systemSitzung);
					Element ape = null;

					
					String projectDataDir = null;
					if ( appinfo.exists()) {
						// apinfo existiert
						ape = CommonsVfs.getElementByFilename(aifn,systemSitzung);
						if ( !online ) {
							Attribute role = ape.getAttribute("role");
							if ( role != null ) {
								String role_value = role.getValue();
								if ( role_value.length()>0) {
									app_role = role_value;
									// there is a role_value
									
									if ( ! PlugRole.hasRole(role_value)) {
										is_hidden = true;
										sm.out.println("    Going to hide app: "+appName);
									}
								}
							}
							Attribute apro_data_dir = ape.getAttribute("pro_data_dir");
							if ( apro_data_dir != null ) {
								String pro_data_dir_value = apro_data_dir.getValue();
								if ( pro_data_dir_value.length()>0) {
									pro_data_dir_value = Commons.stringReplace(pro_data_dir_value, "/", FileName.SEPARATOR);
									pro_data_dir_value = Commons.stringReplace(pro_data_dir_value, "\\", FileName.SEPARATOR);
									if ( pro_data_dir_value.startsWith(FileName.SEPARATOR)){
										pro_data_dir_value = pro_data_dir_value.substring(1);
									}
									if ( !pro_data_dir_value.endsWith(FileName.SEPARATOR)){
										pro_data_dir_value = pro_data_dir_value + FileName.SEPARATOR;
									}
									projectDataDir = pro_data_dir_value;
								}							
							}	
							
							if ( Commons.hasFlag(ape, "has_static_project")) {
								app_has_static_pro = true;							
							}
							if ( ape.getAttribute("sort") != null ) {
								appSort = ape.getAttributeValue("sort");
							}	
						}
						
						Attribute atip = ape.getAttribute("tip");
						if ( atip != null ) {
							String tip_value = atip.getValue();
							if ( tip_value.length()>0)
								app_tip = tip_value;
						}
						
						
						
						
						if ( Commons.hasFlag(ape, "encrypted")) {
							app_is_encrypted = true;							
						}
						

						if ( ape.getAttribute("commits") != null ) {
							appCommitsPath = ape.getAttributeValue("commits");
						}
						if ( ape.getAttribute("cache") != null ) {
							appCachePath = ape.getAttributeValue("cache");
						}						
												
					
					}
										
					String appDir = einFilePath;
					String projectHomeDir = appDir;
					
					if ( ape != null && !this.online) {
						Element link = ape.getChild("link");
						if ( link != null ) {
							// there is a link defined
							sm.out.println("received link for app: "+appName);
							
							String tupe = link.getAttributeValue("type");
							String x = link.getAttributeValue("x");
							sm.out.println("                 for type="+tupe+" x="+x);
							
							String nextAbsDir = null;
							String nextRelDir = null;
							if ( tupe.equals("abs")) nextAbsDir = x;
							else if ( tupe.equals("rel_app")) nextRelDir = appDir;
							else if ( tupe.equals("rel_workspace")) nextRelDir = CoreBuilder.ws.getWsDir();
							else if ( tupe.equals("rel_system")) nextRelDir = CoreBuilder.sys;
							else if ( tupe.equals("rel_services")) nextRelDir = CoreBuilder.db.scJm.getDir();
							else if ( tupe.equals("rel_takeoff")) nextRelDir = Commons.getDirOfFile(CoreBuilder.ws.getWsFilename());
							else if ( tupe.equals("rel_service_apps")) nextRelDir = Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.DIR_SERVICE_APPS));
							else {
								throw new Exception( "the type of the app-info.xml file is unknown: ["+tupe+"]\n apphome ["+appDir+"app-info.xml"+"]");
							}
							String result = null;
							if ( nextAbsDir != null ) result = nextAbsDir;
							else if ( nextRelDir != null ) {
								result = nextRelDir+x;
							} else result = appDir;
							
							sm.out.println(" **************  will be ="+result);
							appDir = result;
							if ( !CommonsVfs.existsDir(appDir,systemSitzung)) {
								sm.out.println(" *** W A R N I N G **** APP DIR not Found: "+appDir);
							}
						}							
					}
										
					DbJm jm = new DbJm ( 	appDir,
							CoreBuilder.ws.getRole(),
							CoreBuilder.ws.getLang(), 
							CoreBuilder.ws.getOs(),
							CoreBuilder.ws.getComputer(),
							DbJob.V_LEVEL_INST,
							isServiceApp,sm,
							app_has_static_pro,
							app_is_encrypted,
							systemSitzung);
					String label; // = appName;
					if ( isServiceApp ) label = serviceAppId_to_Label(appName);
					else label = standardAppId_to_Label(appName);
					DbApp app = new DbApp(
							appName , 
							appDir ,
							jm, 
							projectHomeDir, 
							is_hidden,
							label,
							isServiceApp,
							app_role, 
							app_tip,
							projectDataDir,
							app_has_static_pro,
							appCommitsPath,
							appCachePath,systemSitzung,appSort); 
					 appList.add(app);
					 appLabelIdMap.put(label, appName);
					 boolean found_static_pro = false;
					// Iteration on Installations

					FileObject[] files2 = CommonsVfs.getNinos(einFile,systemSitzung);
					
					
					for (int j=0; j<files2.length;j++) {
						FileObject zweiFile = files2[j];
						String zweiFileBasename = zweiFile.getName().getBaseName();
						String zweiFileUri = zweiFile.getName().getURI();
						
						
						if ( zweiFile.isFolder() ) {
							if ( zweiFile.isFolder() && zweiFileBasename.startsWith("pro..")) {
								throw reservedProjectPrefix (einFile, files2[j], "pro.." );
							} else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("pros.")) {
								throw reservedProjectPrefix (einFile, files2[j], "pros." );
							} else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("pro.+")) {
								throw reservedProjectPrefix (einFile, files2[j], "pro.+" );
							} else if ( zweiFile.isFolder() && zweiFileBasename.equals("pro.Standard-Project")) {
								throw reservedProjectPrefix (einFile, files2[j], "pro.Standard-Project" );
							}  else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("list.")) {
								throw reservedProjectPrefix (einFile, files2[j], "list." );
							} else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("group.")) {
								throw reservedProjectPrefix (einFile, files2[j], "group." );
							} else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("grp.")) {
								throw reservedProjectPrefix (einFile, files2[j], "grp." );
							}  else if ( zweiFile.isFolder() && zweiFileBasename.startsWith(".")) {
								throw reservedProjectPrefix (einFile, files2[j], "." );
							}  else if ( zweiFile.isFolder() && zweiFileBasename.endsWith(".")) {
								throw reservedProjectPostfix (einFile, files2[j], "." );
							}  else if ( zweiFile.isFolder() && zweiFileBasename.endsWith("+")) {
								throw reservedProjectPostfix (einFile, files2[j], "+" );
							}   else if ( zweiFile.isFolder() && zweiFileBasename.startsWith("pro.")) {
								DbPro inst = new DbPro(zweiFileBasename.substring(4), Commons.constructNormalizedDir(zweiFileUri),lc,online,systemSitzung);
								app.addInst(inst,false);
							}   else if ( zweiFile.isFolder() && files2[j].equals("pro_standard")) {
								DbPro inst = new DbPro("Standard-Project", Commons.constructNormalizedDir(zweiFileUri),lc,online,systemSitzung);
								app.addInst(inst,true); 
								found_static_pro = true;
								Sp.p("INIT STANDARD-PROJECT");
							} 							
						}

						

					} // Schleifenende je projekt
					if ( app.app_has_static_pro() && !found_static_pro && !online) {
						// Creating a static pro
						String neuDir = app.getProjectHomeDir()+"pro_standard"+FileName.SEPARATOR;
						if ( printWarnings )
							this.sm.out.println("Creating Standard-Project for "+app.getId()+" at: "+neuDir);
						CommonsVfs.mkdirCascade(neuDir,systemSitzung);
						DbPro inst = new DbPro("Standard-Project", neuDir,lc,online,systemSitzung);
						app.addInst(inst,true); 
					}
					}			
				}
			}
		
	}	
	private Exception reservedProjectPrefix (FileObject parent, FileObject fn, String prefix ) {
		Exception x = new Exception("The directory-prefix '"+prefix+"' is reserved for future implementation of project-lists. "+
	"\n  That regards subdirectory: "+fn.getName().getURI()+"\n  in the directory: "+parent.getName().getURI()+"\n\n  Recommendation: Rename that to something else !");
		return x;
	}
	
	private Exception reservedProjectPostfix (FileObject parent, FileObject fn, String postfix ) {
		Exception x = new Exception("The directory-postfix '"+postfix+"' is reserved for future implementation of project-lists. "+
	"\n  That regards subdirectory: "+fn.getName().getURI()+"\n  in the directory: "+
				parent.getName().getURI()+
				"\n\n  Recommendation: Rename that to something else !");
		return x;
	}	
	void sortAppList() {
		Collections.sort(appList);
		Iterator<DbApp> iterator = appList.iterator();
		while ( iterator.hasNext() ) {
			DbApp currApp = (DbApp)iterator.next();
			currApp.sortProList();
		}
	}
	
	/* #NODB#
	 * 
	Commentarcodes, weil sonst kein commentar moeglich
	begin comment -> *1
	end comment -> *2
	doppelslash -> *3

	private void buildHH( ComuStartMsg sm, boolean isServiceApp ) throws Exception {

		String hiddenAppsDir = getHiddenAppsDir(sysDir);
		if ( !Commons.existsDir(hiddenAppsDir))
			throw new Exception ("The hidden-apps-dir does not exist, create it! "+hiddenAppsDir);
		File hiddenApps = new File(hiddenAppsDir) ;
		*1tod-if-tod*2 appList = new ArrayList<DbApp>();
		
		File einFile = null;
		String[] files = hiddenApps.list();	
		
		*3 Iteration on Applications
		for (int i=0; i<files.length; i++) {
			einFile =  new File(hiddenApps.getAbsolutePath(),files[i]);
			if ( einFile.isDirectory() && files[i].startsWith("app.")) {

				DbJm jm = new DbJm ( 	Commons.correctPathGood(einFile.getAbsolutePath()),
									CoreConf.ws.getRole(),CoreConf.ws.getLang(), CoreConf.ws.getOs(),CoreConf.ws.getComputer(),
									DbJob.V_LEVEL_INST,
									isServiceApp);				
				DbApp app = new DbApp(files[i].substring(4), Commons.correctPathGood(einFile.getAbsolutePath()),jm,Commons.correctPathGood(einFile.getAbsolutePath()),false,false);
				appList.add(app);
				
				*3 Iteration on Installations in the dataDir
				File appFile = new File(databaseDir+files[i]+FileName.SEPARATOR);
				if ( appFile.exists() && appFile.isDirectory() ) {
					File instFile = null;
					String[] files2 = appFile.list();
					for (int j=0; j<files2.length;j++) {
						instFile = new File(appFile.getAbsolutePath(),files2[j]);
						if ( instFile.isDirectory() && files2[j].startsWith("pro.")) {
							DbInst inst = new DbInst(files2[j].substring(4), Commons.correctPathGood(instFile.getAbsolutePath()));
							app.addInst(inst);
						}
					}										
				}
				
			}
		}			

	}	
	 */
	/* #NODB#
	private static String getHiddenAppsDir(String _sysDir ) {
		String x = _sysDir + "hiddenApps" + FileName.SEPARATOR;
		return x;
	}
	*/
	
	public DbApp getAppById ( String id ) {
		DbApp app = null;
		Iterator<DbApp> i1 = appList.iterator();

		while ( i1.hasNext() ) {
			DbApp app2 = (DbApp)i1.next();		
			if ( app2.getId().equals(id)) {
				app = app2;
				break;
			}
		}	
		return app;
	}	
	
	public int getAppIndexById ( String appId ) {
		int result = -1;
		Iterator<DbApp> iterator = appList.iterator();
		while ( iterator.hasNext()) {
			
			DbApp app = (DbApp)iterator.next();
			if (!app.isHidden())
				result++;
			if ( appId.equals(app.getId()))
				break;
		}
		return result;
	}	

	public ArrayList<Element> getExecutionPrologAnonym(
			String appId,
			String instId,
			String instDir,
			boolean newLogAppender,
			boolean service,
			ISitzung _anonymSitzung) throws Exception {
		
	ArrayList<Element> ret = new ArrayList<Element>();
	
	// wegdamit 
	ret.add(CoreBuilder.mc.getConfigSmall());
	
	DbApp app = this.getAppById(appId);
	if ( app != null ) 
	ret.add(app.getExecutionProlog());
	
	ret.add ( DbPro.getExecutionPrologAnonym(
			instId, 
			instDir, newLogAppender, service,online,_anonymSitzung));
	
	return ret;
}	
	public ArrayList<Element> getExecutionProlog(
							String appId,
							String instId,
							boolean newLogAppender,
							boolean service,
							ISitzung _sitzung) throws Exception {
		ArrayList<Element> ret = new ArrayList<Element>();

		// rausdamit 
		ret.add(CoreBuilder.mc.getConfigSmall());
		
		DbApp app = this.getAppById(appId);
		if ( app != null ) {
			ret.add(app.getExecutionProlog());
			DbPro inst = null; // #arenga#3#
			if ( instId==null && app.app_has_static_pro() ) {
				inst = app.standardProject;
			} else 
				inst = app.getInstById(instId);

			if ( inst != null )
				ret.add(inst.getExecutionProlog(newLogAppender,service));
			else
				ret.add(DbPro.getExecutionPrologNoInst(newLogAppender,service,true,_sitzung));
		} else if ( service ) {
			// service call without any app or pro selected
			ret.add(DbPro.getExecutionPrologNoInst(newLogAppender,service,false,_sitzung));
		} else {
			throw new Exception ("An impossible case, service with no service flag [app:] "+
		appId+" [pro:] "+instId+" [is service:] "+service);
		}
		// zwecks test
		
		if ( lc.debugIsOn() ) {
			String tester=null;
			if ( service) {
				// tester="(f,not,(f,or,(f,eq,go-jack-do-file,(prop,job)),(f,eq,go-jack-do-dir,(prop,job)),(f,eq,open_web,(prop,job)),(f,eq,sel_open_file,(prop,job)),(f,eq,sel_open_file_typical,(prop,job)),(f,eq,open_file,(prop,job)),(f,eq,open_dir,(prop,job))))";
			}
			Element ti = new Element("ti");
			ti.setAttribute("type", "dir");
			ti.setAttribute("label", "temp ");
			ti.setAttribute("x", "(dir,temp)");
			ti.setAttribute("flag", "wrap");
			if ( tester!=null)
				ti.setAttribute("test", tester);
			
			Element br = new Element("ti_break");
			if ( tester!=null)
				br.setAttribute("test", tester);
			
			Element hr = new Element("ti_hr");
			if ( tester!=null)
				hr.setAttribute("test", tester);
			
			Element tic = new Element("config");
			tic.addContent(br);

			tic.addContent(ti);
			tic.addContent(br.clone());
			tic.addContent(hr);
			ret.add(tic);
		}
		
		return ret;
	}
	
	
	
	public ArrayList<Element> getExecutionPrologUsage(
			String appId,
			String instId,
			String instDir,
			boolean newLogAppender,
			ISitzung usageSitzung) throws Exception {
		
		ArrayList<Element> ret = new ArrayList<Element>();
		
		//wegdamit 
		ret.add(CoreBuilder.mc.getConfigSmall());
		
		DbApp app = this.getAppById(appId);
		if ( app != null )  
			ret.add(app.getExecutionProlog());
		else
			throw new Exception("The app '"+appId+"' was not found!");
		
		ret.add(DbPro.getExecutionPrologUsage(newLogAppender,instDir,instId,online,usageSitzung));			
		
		return ret;
	}	
	
	public ArrayList<Element> getExecutionPrologExtended() throws Exception {
		
		ArrayList<Element> ret = new ArrayList<Element>();
		
		ret.add(CoreBuilder.mc.getConfig());
					
		
		return ret;
	}		

	private void tr (String x) {
		if ( tmpDoSm && sm!=null ) 
			sm.out.println(x);
		
		if ( tmpDoLog && lc!=null)
			lc.info(x);
	}
	private boolean tmpDoSm;
	private boolean tmpDoLog;
	
	public void trace (boolean doSm, boolean doLog) {
		tmpDoSm = doSm;
		tmpDoLog = doLog;

		tr("'"+databaseDir+"' is dataDir");
		
		Iterator<DbApp> i1 = appList.iterator();

		while ( i1.hasNext() ) {
			DbApp app = (DbApp)i1.next();		
			tr("|");				
			tr("+ '"+app.getId()+"' -> '"+app.getDir()+"'");
			//lc.trace("|");
			tr("|   |projects of "+app.getId()+"|");
			// Loop Instances
			ArrayList<DbPro> instList = app.getInstList();
			
			Iterator<DbPro> i2 = instList.iterator();
			while ( i2.hasNext()) {
				DbPro inst = (DbPro)i2.next();
				tr("|-- '"+inst.getId()+"' -> '"+inst.getDir()+"'");
			}
			// lc.trace("|");
			tr("|   |jobs of "+app.getId()+"|");
			
			// Loop Jobs
			ArrayList<DbJob> jobList = app.getJm().getJobList();
			
			Iterator<DbJob> i3 = jobList.iterator();
			while ( i3.hasNext()) {
				DbJob job = (DbJob)i3.next();
				tr("|-- '"+job.getJobid()+"'");
			}	
		}
		
		tr("| ");
		tr("+ Systemcalls:");
		
		ArrayList<DbJob> scList = scJm.getJobList();
		
		Iterator<DbJob> i4 = scList.iterator();
		while ( i4.hasNext()) {
			DbJob job = (DbJob)i4.next();
			tr("|-- '"+job.getJobid()+"'");
		}	
		

		//lc.trace("==== Trancing finished ...");		
	}
	
	public Element getAskDb (String with_appNatural, boolean natural ) {
		
		Element x = new Element("apps");		
		
		Iterator<DbApp> i1 = appList.iterator();
		
		while ( i1.hasNext() ) {
			
			DbApp app = (DbApp)i1.next();	
			if ( with_appNatural == null || (with_appNatural.equals(app.getId()))) {
				Element appE = app.getDbDump(natural);
				 x.addContent(appE);
				
				Element appJ = app.getJm().getDbDump("jobs",true,natural);
				appE.addContent(appJ);				
			}

		}
		
		return x;		
	}
	
	public Element getAskSystemjobs(boolean natural) {
		Element x = scJm.getDbDump("services",true,natural);
		
		return x;
	}
		
	public String getFullDirApp ( String appid ) {
		DbApp app = this.getAppById(appid);
		if ( app == null )
			return null;
		else 
			return app.getDir();
	}

	public String getFullDirInst ( String appid, String instid ) {
		
		DbApp app = this.getAppById(appid);
		if ( app == null )
			return null;
		DbPro inst = app.getInstById(instid);
		if ( inst == null )
			return null;
		return inst.getDir();
	}
	
	public String getFullProDataDir ( String appid, String instid ) {
		
		DbApp app = this.getAppById(appid);
		if ( app == null )
			return null;
		DbPro inst = app.getInstById(instid);
		if ( inst == null )
			return null;
		return inst.getProjectDataDir_Natural(app);
		
	}	
	public String getFullDirInstEscaped ( String appid, String instid ) {
		String reg = getFullDirInst(appid, instid );
		if (reg !=null)
			return Entities.importEntities(reg);
		else
			return null;
	}
	
	public String getName() {
		return (database.getName().getBaseName());
	}

	public DbJm getScJm() {
		return scJm;
	}
		

}
