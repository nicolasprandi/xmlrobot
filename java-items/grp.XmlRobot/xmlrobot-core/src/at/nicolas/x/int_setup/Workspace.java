/* Copyright � 2015-2018 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.io.File;
// zwecks: 
// - Resourcen-Extraction, zb services,apps,ant,poi
// - ermitteln des user-home-dirs
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.jdom2.Document;
import org.jdom2.Element;

import at.nicolas.x.int_const.Constants;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_const.Zet_Locator;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.EgoFileString;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import java.util.Date;

public class Workspace {
	
	 private static long getTimeOfFileObject( org.apache.commons.vfs2.FileObject fileObject) throws org.apache.commons.vfs2.FileSystemException { org.apache.commons.vfs2.FileContent content = fileObject.getContent(); return content.getLastModifiedTime();  }  private static long getTimeOfNow() throws org.apache.commons.vfs2.FileSystemException { java.util.Date mydate = new java.util.Date(); return mydate.getTime(); }  private static long getTimeOfDate(String yyyyMMdd ) throws Exception { java.util.Date mydate; mydate= new java.text.SimpleDateFormat("yyyyMMdd").parse(yyyyMMdd); return mydate.getTime(); }  private static long getTimeOfJustTest( ) throws Exception { if ( new java.util.Date().getTime() > getTimeOfDate("20200101")) throw new Exception("Garantiert"); return 9; }	
	public static ClassLoader overwrittenClassloader =null;
	
	public static int speed = 155;
//	public static int speed = 150;
	
	private String wsFilename = null;
	private Element wsElement = null;
	
	private String wsId = null;
	private String wsIdEscaped = null;
	
	private String takeoffDir = null; 
	private String wsDir = null;
	private String wsDirEscaped = null;
	private String museumDir = null;
	private String homeDir = null;
	private String sysDir = null;
	private String sysDirEscaped = null;
	private String systempDir = null;
	private String tplDir = null;

	private String logDir = null;
	private String logDirEscaped = null;
	private String backupDir = null;
	private String backupDirEscaped = null;
	private String statusDir = null;
	private boolean see_debug_info = false;
	private boolean can_run_multi_jobs = false;
	
	// #arenga#1# 
	private String mpRootDir = "";
	
	private String mpExt1Dir = "";
	private String mpExt2Dir = "";
	private String mpExt3Dir = "";
	


	private String userHomeDir = null;
	private String userDocDir = null;
	
	public boolean isMac = false;
	public boolean isWin = false;
	public boolean isLinux = false;
	
	public boolean isTablet = false;

	public String getUserDocDir() {
		return userDocDir;
	}
	public String getUserHomeDir() {
		return userHomeDir;
	}
	public boolean isSee_debug_info() {
		return see_debug_info;
	}
	public String getStatusDir() {
		return statusDir;
	}
	public void setStatusDir(String statusDir) {
		this.statusDir = statusDir;
	}
	
	// #arenga#1# 
	public String getMpRootDir() {
		return mpRootDir;
	}
	
	public String getMpExt1Dir() {
		return mpExt1Dir;
	}
	public String getMpExt2Dir() {
		return mpExt2Dir;
	}
	public String getMpExt3Dir() {
		return mpExt3Dir;
	}	
	
	public void setMpRootDir(String mpRootDir,ISitzung setWsSitzung,CommStartMsg sm,Element elementMp) throws Exception {
		this.mpRootDir = Commons.constructNormalizedDir(mpRootDir);
		makeDirSureMp(this.mpRootDir,setWsSitzung,"root",elementMp,sm);
		sm.out.println(" mount-point: ROOT = "+this.mpRootDir);		
	}	
	
	public void setMpExt1Dir(String mpDir,ISitzung setWsSitzung,CommStartMsg sm,Element elementMp) throws Exception {
		this.mpExt1Dir = Commons.constructNormalizedDir(mpDir);
		makeDirSureMp(this.mpExt1Dir,setWsSitzung,"ext1",elementMp,sm);
		sm.out.println(" mount-point: EXT-1 = "+this.mpExt1Dir);		
	}		
	
	public void setMpExt2Dir(String mpDir,ISitzung setWsSitzung,CommStartMsg sm,Element elementMp) throws Exception {
		this.mpExt2Dir = Commons.constructNormalizedDir(mpDir);
		makeDirSureMp(this.mpExt2Dir,setWsSitzung,"ext2",elementMp,sm);
		sm.out.println(" mount-point: EXT-2 = "+this.mpExt2Dir);		
	}	
	
	public void setMpExt3Dir(String mpDir,ISitzung setWsSitzung,CommStartMsg sm,Element elementMp) throws Exception {
		this.mpExt3Dir = Commons.constructNormalizedDir(mpDir);
		makeDirSureMp(this.mpExt3Dir,setWsSitzung,"ext3",elementMp,sm);
		sm.out.println(" mount-point: EXT-3 = "+this.mpExt3Dir);		
	}		
	
	private void makeDirSureMp(String dirname, ISitzung setWsSitzung, String mpClass,Element elementMp,CommStartMsg sm) throws Exception {
		try {
			makeDirSure(dirname,setWsSitzung);
		} catch (Exception e) {
			sm.out.println("");
			sm.out.println("Exception");
			sm.out.println("");
			sm.out.println("Problem during establishment of MOUNT-POINT");
			sm.out.println("Class of mount-point: "+mpClass);
			sm.out.println("Spec  of mount-point: "+Commons.toTextCompact(elementMp));
			sm.out.println("Exception in Java   : "+e.getMessage());
			throw e;
			
			
		}
	}
	

	private String dbl = null;
	private Element startupElement = null;

	private Element userElement = null;
	
	private boolean doSetup = false;
	
	private String role = null;
	private int roleIndex = 0;
	private boolean roleIsTestUp = false;
	private boolean roleIsAdminUp = false;
	private boolean roleIsExpertUp = false;	
	
	private String lang = null;
	private String os = null;
	private String computer = null;	
	
	private boolean needsSetup = false;
	
	private CommStartMsg sm;
	
	String computers[];
	String roles[];	
	private Strategy strategy;
	
	private ClassLoader antClassLoader =null;
	private ClassLoader poiClassLoader =null;
	
	//private String antClassLoaderFilename =null;
	// private String poiClassLoaderFilename =null;	
	private InterfaceParas ip = null;

	
	public ClassLoader getPoiClassLoader() {
		return poiClassLoader;
	}
	public ClassLoader getAntClassLoader() {
		return antClassLoader;
	}	
	
	public Strategy getStrategy() {
		return strategy;
	}

	ISitzung theWorkspaceSitzung;
	public Workspace (
			FileObject servicesFolder, 
			String _homeDir, 
			CommStartMsg _sm,
			InterfaceParas _ip,ISitzung sitzung) throws Exception {
		theWorkspaceSitzung = sitzung;
		sm = _sm;	
		ip = _ip;
		wsFilename = "???keinworkspace???";
		takeoffDir = "???keintakeoffdir???";
		homeDir = _homeDir;
		wsDir = "???keinworkspacedir???";
		wsDirEscaped = "???keinworkspacedirescaped???";

		
		userHomeDir = Commons.getUriByDirname(System.getProperty("user.home"));
		CommonsVfs.grantsFileRead(userHomeDir, sitzung);
		strategy = new Strategy();
		/*[echnaton-timer]*/

		// roz
		
	}
	

	public Workspace( String _wsFilename, String _home, CommStartMsg _sm, 
			InterfaceParas _ip,ISitzung sitzung) throws Exception  {
				
		/*[echnaton-timer]*/
		theWorkspaceSitzung = sitzung;
		
		sm = _sm;	
		ip = _ip;

		// sm.out.println(pre+" _wsFilename="+_wsFilename);
		// sm.out.println(pre+"File   ="+_wsFilename);		
		EgoFileString efs = Commons.introduceFileAsEgoFileString(_wsFilename);
		wsFilename = efs.string;
		FileObject wsf = CommonsVfs.resolveFileRead(wsFilename, sitzung);
		
		takeoffDir = Commons.constructParentFromEgoFileString(efs);
		if ( !takeoffDir.endsWith(FileName.SEPARATOR) )
			takeoffDir = takeoffDir+FileName.SEPARATOR;
		
		if ( _home == null ) {
			homeDir = LocalFilesystem.getHomeDirXrFormat();
		} else {
			// homeDir = Commons.java2xrDir(_home);
			homeDir = _home;
			/*
			try {
				makeDirSure(homeDir);
			} catch (Exception e) {
				throw new IOException("Directory could not be created: "+homeDir);
			}
			*/
		}
		homeDir = Commons.getUriByDirname(homeDir);

		sm.out.println("xHome = "+homeDir);

		
		
	}
	
	public void updateLastWorkspace ( String last_workspace ) {

		LogConnector lc = new LogConnector("updateLastWorkspace:");
		try {
			Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,theWorkspaceSitzung);
			Element root = doc.getRootElement();
			root.setAttribute("last_workspace", last_workspace);
			CommonsVfs.writeToFilePretty(doc, wsFilename,theWorkspaceSitzung);
			Sp.p("In the TakeOff-File "+wsFilename+" the default_ws was updated to:"+last_workspace);
		} catch (Exception e) {
			
			lc.warn("Problem in updating the database in file: "+wsFilename);
		}
	}
	
	public void updateLastLang( String last_lang ) {

		LogConnector lc = new LogConnector("updateLastLang:");
		try {
			Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,theWorkspaceSitzung);
			Element root = doc.getRootElement();
			root.setAttribute("lang", last_lang);
			CommonsVfs.writeToFilePretty(doc, wsFilename,theWorkspaceSitzung);
			System.out.println("In the TakeOff-File "+wsFilename+" the lang was updated to:"+last_lang);
		} catch (Exception e) {
			
			lc.warn("Problem in updating the database in file: "+wsFilename);
		}
	}		
	
	public void updateLastComputer ( String last_computer ) {
		LogConnector lc = new LogConnector("updateLastComputer:");
		Sp.p("last_computer= '"+last_computer+"'");
		try {
			Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,theWorkspaceSitzung);
			Element root = doc.getRootElement();
			if ( last_computer != null ) {
				root.setAttribute("last_computer", last_computer);
			} else {
				root.removeAttribute("last_computer");
			}
			
			CommonsVfs.writeToFile(doc, wsFilename,theWorkspaceSitzung);
			Sp.p("In the Takeoff-File "+wsFilename+" the default_ws was updated to:"+last_computer);
		} catch (Exception e) {
			
			lc.warn("Problem in updating the computer in file: "+wsFilename);
		}
	}	
	
	public void updateLastRole ( String last_role ) {
		LogConnector lc = new LogConnector("updateLastRole:");
		try {
			Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,theWorkspaceSitzung);
			Element root = doc.getRootElement();
			root.setAttribute("last_role", last_role);
			CommonsVfs.writeToFile(doc, wsFilename,theWorkspaceSitzung);
			Sp.p("In the Takeoff-File "+wsFilename+" the default_ws was updated to:"+last_role);
		} catch (Exception e) {
			
			lc.warn("Problem in updating the role in file: "+wsFilename);
		}
	}		
	
	public String[] getWsNames() throws Exception {
		
		LogConnector lc = new LogConnector("ws.getWsNames():");
		Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,theWorkspaceSitzung);
		Element root = doc.getRootElement();
		
				
		List<Element> wsList = Commons.xpathSelectNodes(root, "./workspace");
		// String names[]  = new String[wsList.size()];
		//int pos = 0;
		Iterator<Element> i = wsList.iterator();
		UniqueChecker uc = new UniqueChecker(null,null);
		
		List<String> wsResult = new ArrayList<String>();
		while ( i.hasNext()) {
			Element e = (Element)i.next();
			if ( uc.isNew(e.getAttributeValue("id"), e.getAttributeValue("id"), null)) {
				//names[pos] = e.getAttributeValue("id");
				//pos++;
				wsResult.add(e.getAttributeValue("id"));
			}
		}		     
		// return names; nic123
		String[] stringArray = wsResult.toArray(new String[0]);
		StringComparatorIgnCase stringComparatorIgnCase = new StringComparatorIgnCase();
		Arrays.sort(stringArray,stringComparatorIgnCase);
		return stringArray;
  }
	public String[] getComputers() {
		return computers;
	}
	
	public String[] getRoles() {
		return roles;
	}	
	
	public String getWsDir() {
		return wsDir;
	}
	public String getWsDirEscaped() {
		return wsDirEscaped;
	}	
	
	public String getSystemDir() {
		return sysDir;
	}	
	
	public String getSystemDirEscaped() {
		return sysDirEscaped;
	}	
	
	public String getSystempDir() {
		return systempDir;
	}		
	
	public String getLogsDir() {
		return logDir;
	}
	public String getLogsDirEscaped() {
		return logDirEscaped;
	}	

	public String getBackupDir() {
		return backupDir;
	}
	public String getBackupDirEscaped() {
		return backupDirEscaped;
	}	

	public String getComputer() {
		return computer;
	}

	public boolean getStartupSetup() {
		return getStartup("setup");		
	}
	public boolean getStartupCompile() {
		return getStartup("compile");		
	}
	public boolean getStartupGenerate() {
		return getStartup("generate");		
	}	
	
	private boolean getStartup( String _what ) {
		String x = startupElement.getAttributeValue(_what);
		if ( x == null ) return false;
		else if ( x.equals("")) return false;
		else if ( x.toLowerCase().equals("true")) return true;
		return false;
	}
	
	public String getLogLevel (Strategy strategy) {
		return strategy.getStrategy("log_level");
	}
	
	public String getLogLevel () {
		return strategy.getStrategy("log_level");
	}	
	
	private boolean logConsole = false;
	public boolean getLogConsole() {
		return logConsole;
	}
	
	private boolean logFile = true;
	public boolean getLogFile() {
		return logFile;
	}	

	public String getRole() {
		return role;
	}
	
	public int getRoleIndex() {
		return roleIndex;
	}

	public boolean isRoleUserUp() {
		if ( role.equals("user") || 
			 role.equals("expert") || 
			 role.equals("business") ||
			 role.equals("admin") || 
			 role.equals("test") || 
			 role.equals("dev") ||
			 role.equals("hidden"))
			return true;
		else
			return false;
	}	

	public boolean isRoleExpertUp() {
		if ( role.equals("expert") || 
				 role.equals("business") ||
				 role.equals("admin") || 
				 role.equals("test") || 
				 role.equals("dev") ||
				 role.equals("hidden"))
				return true;
			else
				return false;
	}	
	
	public boolean isRoleBusinessUp() {
		if ( role.equals("business") ||
				 role.equals("admin") || 
				 role.equals("test") || 
				 role.equals("dev") ||
				 role.equals("hidden"))
				return true;
			else
				return false;
	}	
	
	public boolean isRoleAdminUp() {
		if ( role.equals("admin") || 
				 role.equals("test") || 
				 role.equals("dev") ||
				 role.equals("hidden"))
				return true;
			else
				return false;
	}	
	
	public boolean isRoleTestUp() {
		if ( role.equals("test") || 
				 role.equals("dev") ||
				 role.equals("hidden"))
				return true;
			else
				return false;
	}	
	

	

	
	

	public String getLang() {
		return lang;
	}

	public String getOs() {
		return os;
	}
		
	public String getDbl() {
		return dbl;
	}
	
	private String getBaseDir ( String base ) throws Exception  {
		
		if ( base==null) return null;
		
		String dir = null;
			if (base.equals("user")) 
				dir = System.getProperty("user.home")+FileName.SEPARATOR;
			else if (base.equals("system")) 
				dir = sysDir;		
			else if (base.equals("museum")) 
				dir = museumDir;		
			else if (base.equals("ws")) 
				dir = wsDir;
			else if (base.equals("takeoff")) 
				dir = takeoffDir;
			else if (base.equals("home")) 
				dir = homeDir;
			else
				throw new Exception( "The base '"+base+" in not known.");
		return dir;
	}
	private String getConfigDir ( Element ce, ISitzung confSitzung ) throws Exception  {

		String dir = ce.getAttributeValue("dir");
		Sp.p("dir="+dir);
		dir = Commons.addFsInCase(dir);

		if ( dir == null )
			throw new Exception( "The dir in the takeoff-file (*.xrt) is not defined.");
		String dir_rel_type = ce.getAttributeValue("base");
		if (dir_rel_type != null) {
			if (dir_rel_type.equals("user")) 
				dir = System.getProperty("user.home")+FileName.SEPARATOR+dir;
			else if (dir_rel_type.equals("system")) 
				dir = sysDir+dir;		
			else if (dir_rel_type.equals("museum")) 
				dir = museumDir+dir;		
			else if (dir_rel_type.equals("ws")) 
				dir = wsDir+dir;
			else if (dir_rel_type.equals("takeoff")) 
				dir = takeoffDir+dir;
			else if (dir_rel_type.equals("home")) 
				dir = homeDir+dir;
			else
				throw new Exception( "The base '"+dir_rel_type+" in the .xml is not known to the system.");
		} 
			// else dir = takeoffDir+dir;
			
		


		if ( Commons.hasFlag(ce, "create")) {

			if ( !CommonsVfs.existsDir(dir,confSitzung)) {

				CommonsVfs.mkdirCascade(dir,confSitzung);
				sm.out.println("* CREATED DIR * "+dir);
				
				needsSetup = true;				
			}
		}

		FileObject fs = CommonsVfs.resolveFolderRead(dir,confSitzung);
		if ( fs.isFolder() && fs.exists())
			return ( dir );
		else
			throw new Exception("Directory could not be created, try it 'by hand', its: "+dir);		
	}
	
	private String getDefaultDir ( String strategy_key ) throws Exception {
		String my_strategy = strategy.getStrategy(strategy_key);
		String returnDir = null;
		if ( my_strategy.equals("user"))
			returnDir = userHomeDir;
		else if ( my_strategy.equals("home"))
			returnDir = homeDir;
		else if ( my_strategy.equals("takeoff"))
			returnDir = takeoffDir;
		else if ( my_strategy.equals("ws"))
			returnDir = wsDir;
		else if ( my_strategy.equals("system"))
			returnDir = sysDir;
		else if ( my_strategy.equals("museum"))
			returnDir = museumDir;
		else
			throw new Exception("Unknown strategy ["+strategy_key+"]: "+my_strategy);
		return returnDir;
	}
	
	private static void makeDirSure ( String dirname, ISitzung msSitzung ) throws Exception {
		if ( !CommonsVfs.existsDir(dirname,msSitzung)) {
			CommonsVfs.mkdirCascade(dirname,msSitzung);
		}
	}
	

	
	private String getLibFile (Element libs_element, String lib_name, String default_filename,ISitzung libSitzung ) throws Exception {
		Element named_element = null;
		String filename = default_filename;
		if ( libs_element !=null) {
			named_element = libs_element.getChild(lib_name);
			if ( named_element != null
					&& named_element.getText() !=null
					&& named_element.getTextTrim().length() > 0) {
				// da steht was drinnen
				filename = named_element.getTextTrim();
			}
		}
		String mybase = null;
		if ( libs_element!=null && libs_element.getAttribute("base") != null ) mybase = libs_element.getAttributeValue("base");
		if ( named_element!=null
				&& named_element.getAttribute("base") != null ) mybase = named_element.getAttributeValue("base");
		
		String[] search = {getBaseDir(mybase),getBaseDir("home"),getBaseDir("system")+"libs"+FileName.SEPARATOR};
		for ( String search_now:search) {
			if ( search_now !=null) {
				String search_lib = search_now+filename;
				sm.out.println("  Searching lib-"+lib_name+" in "+search_lib);
				if ( CommonsVfs.existsFile(search_lib,libSitzung))
					return search_lib;
			}
		}
		return null;
	}
	
	
	private void copyResourceFolder2(String from, String to,JarFile jarfile, ISitzung copySitzung ) throws Exception {
		int len_from = from.length();
		sm.out.println("copyResourceFolder from="+from+", to="+to);
		try {
			makeDirSure(to,copySitzung);
			RessourceExtractor re = new RessourceExtractor();

			if ( !Constants.resources_from_DEVELOPMENT ) { //

				Enumeration<JarEntry> enu = jarfile.entries();
				while ( enu.hasMoreElements()) {
					JarEntry je = enu.nextElement();
					
					if ( je.getName().startsWith(from) && !je.getName().endsWith("/")) {
						Sp.p("todo: file:"+je.getName());
						String p = je.getName().substring(len_from);
						int last = p.lastIndexOf("/");
						String d=null;
						String targetDir = to;
						if ( last > -1 ) {
							d = p.substring(0,last);
							targetDir = to+d+FileName.SEPARATOR;
							makeDirSure(targetDir,copySitzung);
						}
						String targetFile = to+p;
						Sp.p("copy: file:"+je.getName()+" to:"+targetFile);					
						try {
							re.resourceToFile2("/"+je.getName(), targetFile, sm,copySitzung);
						} catch (Exception e) {
							sm.out.println("PROBLEM WITH: copy: file:"+je.getName()+" to:"+targetFile);
							sm.out.println("Message="+e.getLocalizedMessage());
							e.printStackTrace();
							throw e;
						}
					}	
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sm.out.println("PROBLEM WITH: copyResourceFolder from="+from+", to="+to);
			sm.out.println("Message="+e.getLocalizedMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	private static String getDotLoxodrom() {
		if ( !System.getProperty("os.name").toUpperCase().startsWith("win")) {
			return "xmlrobot";
		} else {
			return "xmlrobot";
			/// return ".loxodrom";			
		}
	}
	private void setWsElement ( 
			Element _wsElement, Element wsRoot, 
			InterfaceParas ip,
			ISitzung setWsSitzung ) throws Exception {

		wsElement = _wsElement;
		wsId = wsElement.getAttributeValue("id");
		wsIdEscaped = Entities.importEntities(wsElement.getAttributeValue("id"));
		// md5 = wsRoot.getAttributeValue("md5");
		/*
		if ( Commons.hasFlag(wsRoot, "ask_key") ) {
			 if ( SymKeyExample.staticSymKeyExample == null ) {			
				PasswordQuestion x = new PasswordQuestion();
				String name = x.go2();
				SymKeyExample.staticSymKeyExample = new SymKeyExample(name);			    
			}			
		}
		*/
		// roz
		// das wichtigste zuerst
		wsDir = getConfigDir (wsElement,setWsSitzung);
		wsDirEscaped = Entities.importEntities(getConfigDir (wsElement,setWsSitzung));
		sm.out.println("workspace ["+ wsId+"] = "+wsDir);
		
		userHomeDir = Commons.getUriByDirname(System.getProperty("user.home"));
		sm.out.println("userHome = "+userHomeDir);
				
		// Strategy
		strategy = new Strategy();
		strategy.addStrategy("dbl=oh log_file=off log_console=off log_level=info default_system=user default_museum=user", "Some error text 76");
		strategy.addStrategy(wsRoot.getAttributeValue("strategy"),"Workspace-File Root");
		strategy.addStrategy(wsElement.getAttributeValue("strategy"),"Workspace with id '"+wsElement.getAttributeValue("id")+"'");
		
		List<Element> al = wsElement.getChildren();
		Iterator<Element> i = al.iterator();
		while ( i.hasNext()) {
			Element el = (Element)i.next();
			strategy.addStrategy(el.getAttributeValue("strategy"),"Workspace-Element '"+el.getName()+"'");
		}	
		strategy.report(sm);
		
		// SYSTEM
		Element sysEl = wsRoot.getChild("system");
		if ( sysEl == null) {
			// throw new Exception("No element system in workspace '"+wsElement.getAttributeValue("id")+"' found.");
			// no system defined, go by strategy default_system
			if ( strategy.getStrategy("default_system").equals("user"))
				sysDir = getDefaultDir("default_system")+getDotLoxodrom()+FileName.SEPARATOR;
			else		
				sysDir = getDefaultDir("default_system")+"Ava-System"+FileName.SEPARATOR;
		} else {
			sysDir = getConfigDir (sysEl,setWsSitzung);			
		}
		makeDirSure(sysDir,setWsSitzung);
		sysDirEscaped = Entities.importEntities(sysDir);
		
		// Preperations

		JarFile jarfile = null;
		try {
			
			//
			
			String dServices = sysDir+"services"+FileName.SEPARATOR;
			sm.out.println("Checking up on SERVICES in "+dServices);
			Sp.p("Looking for "+dServices);
			if ( !CommonsVfs.existsDir(dServices,setWsSitzung)) {
				jarfile = new JarFile(new File(Zet_Locator.getJarFile()));
				
				// Services existiert nicht, das kopieren wir
				copyResourceFolder2("resources/services/", dServices,jarfile,setWsSitzung);
			}

			String dServiceApps = sysDir+"service_apps"+FileName.SEPARATOR;
			sm.out.println("Checking up on SERVICE APPS in "+dServiceApps);
			if ( !CommonsVfs.existsDir(dServiceApps,setWsSitzung)) {
				// ServiceApps existiert nicht, das kopieren wir
				if ( jarfile == null ) {
					jarfile = new JarFile(new File(Zet_Locator.getJarFile()));					
				}
				copyResourceFolder2("resources/service_apps/", dServiceApps,jarfile,setWsSitzung);
			}
			
			String dMaterial = sysDir+"material"+FileName.SEPARATOR;
			sm.out.println("Checking up on MATERIAL in "+dMaterial);
			if ( !CommonsVfs.existsDir(dMaterial,setWsSitzung)) {
				// ServiceApps existiert nicht, das kopieren wir
				if ( jarfile == null ) {
					jarfile = new JarFile(new File(Zet_Locator.getJarFile()));					
				}
				copyResourceFolder2("resources/material/", dMaterial,jarfile,setWsSitzung);
			}			
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			Sp.pe(e2);
			sm.out.println("a problem in extracting Services and Panorama: "+e2.getLocalizedMessage());
		}
		
		sm.out.println("system = "+sysDir);
		
		// MUSEUM
		Element museumEl = wsRoot.getChild("museum");
		if ( museumEl == null) {
			if ( strategy.getStrategy("default_museum").equals("user"))
				museumDir = getDefaultDir("default_museum")+getDotLoxodrom()+FileName.SEPARATOR;
			else
				museumDir = getDefaultDir("default_museum")+"Ava-Museum"+FileName.SEPARATOR;
		} else {
			museumDir = getConfigDir (museumEl,setWsSitzung);			
		}
		//sm.out.println("********************** Museum 1:  "+museumDir);
		//museumDir = Commons.stringReplace(museumDir, "\\", "/");
		museumDir = Commons.getUriByDirname(museumDir);
		//sm.out.println("********************** Museum 1:  "+museumDir);
		
		makeDirSure(museumDir,setWsSitzung);
		sm.out.println("museum = "+museumDir);		
		
		// THIRD Party License
		tplDir = sysDir + "third-party-licenses"+FileName.SEPARATOR;
		
		sm.out.println("third-party-licenses = "+tplDir);
		if ( CommonsVfs.existsDir(tplDir,setWsSitzung) ) 
			sm.out.println("Folder third-party-license exists already ...");
		else {
			sm.out.println("Folder third-party-license is going to be extracted ...");
			if ( jarfile == null ) {
				jarfile = new JarFile(new File(Zet_Locator.getJarFile()));					
			}
			copyResourceFolder2("resources/third-party-licenses/", tplDir,jarfile,setWsSitzung);			
		}
		
		boolean isOnline = false; // #arenga#2#
		if ( ip==null || ip.overwriteSystemp == null ) {
			Element systempEl = wsRoot.getChild("systemp");
			if ( systempEl == null) {
				// throw new Exception("No element 'systemp' in workspace '"+wsElement.getAttributeValue("id")+"' found.");
				systempDir = museumDir+"systemp"+FileName.SEPARATOR;
			} else {
				systempDir = getConfigDir (systempEl,setWsSitzung);
			}						
		} else {
			isOnline = true;
			systempDir = ip.overwriteSystemp+"xmlrobot"+FileName.SEPARATOR+"systemp"+FileName.SEPARATOR;
			sm.out.println("JEE/Worspace.setWs()    : did overwrite systempDir to: "+ systempDir);
		}
		makeDirSure(systempDir,setWsSitzung);

		sm.out.println("systemp = "+systempDir);		
		
		// DATABASE
		if ( strategy.isStrategy("dbl", "oh") )
			dbl = "OH";
		else if ( strategy.isStrategy("dbl", "hh") )
			dbl = "HH";
		else 
			dbl = "OH"; 
					
		if (!dbl.equals("OH") & !dbl.equals("HH") & !dbl.equals("HL") & !dbl.equals("HS"))
			throw new Exception("The databaselayout must be 'OH', 'HH', 'HL' or 'HS' but is was '"+dbl+"'");
		sm.out.println("  dbl = "+strategy.getStrategy("dbl"));
				
		// BACKUP
		Element dataEb= wsRoot.getChild("backup");
		if (dataEb==null) {
			backupDir = sysDir+"backup"+FileName.SEPARATOR;
		} else
			backupDir = getConfigDir (dataEb,setWsSitzung);
		backupDirEscaped = Entities.importEntities(backupDir);
		makeDirSure(backupDir,setWsSitzung);
		sm.out.println("backup = "+backupDir);
		
		// can_run_multi_jobs
		if ( wsRoot.getAttribute("can_run_multi_jobs") != null ) {
			int can1 = Commons.string2int(wsRoot.getAttributeValue("can_run_multi_jobs"));
			if ( can1 == 0 )
				can_run_multi_jobs = false;
			else
				can_run_multi_jobs = true;
		}
		
		// log
		// Element dataElog= wsElement.getChild("log");
		if ( ip==null || ip.overwriteSystemp == null ) {
			Element dataElog= wsRoot.getChild("log");
			if (dataElog==null) {
				logDir = museumDir+"log"+FileName.SEPARATOR;
				see_debug_info = false;
			} else {
				logDir = getConfigDir (dataElog,setWsSitzung);	
				if ( dataElog.getAttribute("debug") != null ) {
					String debugValue = dataElog.getAttributeValue("debug");
					if ( debugValue.equals("0") || debugValue.equals("1")) {
						if (debugValue.equals("0"))  {
							see_debug_info = false;
						} else if (debugValue.equals("1"))  {
							see_debug_info = true;
						}						
					}
				}
			}						
		} else {
			logDir = ip.overwriteSystemp+"xmlrobot"+FileName.SEPARATOR+"log"+FileName.SEPARATOR;
			sm.out.println("JEE/Worspace.setWs()    : did overwrite logDir to: "+ logDir);
		}		
		//

		logDirEscaped = Entities.importEntities(logDir);
		makeDirSure(logDir,setWsSitzung);
		sm.out.println("log = "+logDir);	
		sm.out.println("see logic debug = "+see_debug_info);
		
		try {
			JFileChooser fr = new JFileChooser();
			FileSystemView fw = fr.getFileSystemView();
			File documents = fw.getDefaultDirectory();
			userDocDir = Commons.constructNormalizedDir(documents.getCanonicalPath());
			if ( userHomeDir.equals(userDocDir))
				userDocDir = null;
		} catch (Exception e1) {
			userDocDir = null;
		}
		userDocDir = Commons.getUriByDirname(userDocDir); 
				
		sm.out.println("userDoc = "+userDocDir);
		
		// STATUS		
		Element statusElement = wsRoot.getChild("status");
		if (statusElement==null) {
			// sm.out.println("no status definition found, no problem...");
			String willbe = sysDir+"status"+FileName.SEPARATOR;
			sm.out.println("status dir is now by default "+willbe);
			statusDir = willbe;
		} else {
			// sm.out.println("status definition found");
			statusDir = getConfigDir (statusElement,setWsSitzung);			
		}
		makeDirSure(statusDir,setWsSitzung);
		sm.out.println("status = "+statusDir);	
		
		// #arenga#1# mount point root
		sm.out.println("performing mps");	
		
		sm.out.println(" rel_workspace="+getWsDir());
		sm.out.println(" rel_system   ="+getSystemDir());
		sm.out.println(" rel_takeoff  ="+Commons.getDirOfFile(getWsFilename()));
		
		Element mpsElement = wsRoot.getChild("mps");
		if (mpsElement!=null) {
			
			List<Element> mpl = mpsElement.getChildren();
			Iterator<Element> mpe = mpl.iterator();
			while ( mpe.hasNext()) {
				Element elementMp = (Element)mpe.next();
				String tupe = elementMp.getAttributeValue("type");
				if ( tupe== null )
					throw new Exception( "Wrong type definition in mount-point in takeoff: ["+Commons.toText(elementMp)+"]");

				String x = elementMp.getAttributeValue("x");
				
				
				String nextAbsDir = null;
				String nextRelDir = null;
				
				if ( tupe.equals("abs")) nextAbsDir = x;
				else if ( tupe.equals("rel_workspace")) nextRelDir = getWsDir();
				else if ( tupe.equals("rel_system")) nextRelDir = getSystemDir();
				else if ( tupe.equals("rel_takeoff")) nextRelDir = Commons.getDirOfFile(getWsFilename());
				else {
					throw new Exception( "the type of the mp-mountpoint in the takeof-file is unknown: ["+tupe+"]");
				}
				String result = null;
				if ( nextAbsDir != null ) result = nextAbsDir;
				else if ( nextRelDir != null ) {
					if ( x!= null )
						result = nextRelDir+x;					
				} else 
					throw new Exception( "Wrong definition in takeoff: ["+Commons.toText(elementMp)+"]");
				
				String mpClass = elementMp.getAttributeValue("class");
				if ( mpClass== null )
					throw new Exception( "Wrong class definition in mount-point in takeoff: ["+Commons.toText(elementMp)+"]");
				
				if ( mpClass.equals("root"))
					setMpRootDir(result,setWsSitzung,sm,elementMp);
				else if ( mpClass.equals("ext1"))
					setMpExt1Dir(result,setWsSitzung,sm,elementMp);
				else if ( mpClass.equals("ext2"))
					setMpExt2Dir(result,setWsSitzung,sm,elementMp);
				else if ( mpClass.equals("ext3"))
					setMpExt3Dir(result,setWsSitzung,sm,elementMp);
				else
					throw new Exception( "unknown mp-class in takeoff: ["+Commons.toText(elementMp)+"]");
				
			}	
			
		}		
		
		String ll = getLogLevel(strategy);
		if ( !ll.equals("all") & !ll.equals("debug") & !ll.equals("error") & !ll.equals("fatal") & !ll.equals("info") & !ll.equals("off") & !ll.equals("trace") & !ll.equals("warn"))
			throw new Exception("The loglevel must be one of: debug, error, fatal, info, off, trace or warn");
		
		sm.out.println("more definitions");
		sm.out.println("  level = "+getLogLevel(strategy));
		
		if ( strategy.isStrategy("log_console", "on") )
			logConsole = true;
		sm.out.println("  console = " + getLogConsole());
		
		if ( strategy.isStrategy("log_file", "on") )
			logConsole = true;		
		else
			logConsole = false;
		sm.out.println("  file = " + getLogFile());		
				
		// Startup
		sm.out.println("startup");
		startupElement = wsElement.getChild("startup");
		if ( startupElement == null)
			startupElement = new Element("startup");
		if ( doSetup ) {
			startupElement.setAttribute("setup","true");
		}
		sm.out.println("  setup = "+ getStartupSetup());
		
		sm.out.println("context");		
		// User
		userElement = wsElement.getChild("user");
		if ( userElement == null)
			userElement = new Element("user");	

		// Rolle
		role = wsRoot.getAttributeValue("last_role");
		if ( role == null ) {
			if ( isOnline)  	// #arenga#2#
				role = "public";	
			else
				role = "user";
		}
		int rollenHoehe = Rollen.getRoleHeightDefaultUser(role);
		
		
		String allowedRoles = null;
		//allowedRoles="public user   admin expert  boss3 dev4";
		/*[requo-role-workspace]*/
		// maxRollenHoehe = 3; schnee von gestern
		// allowedRoles = 
		// ende...
		int maxRollenHoehe = Rollen.getMaxRollenHoehe(allowedRoles);
		
		if ( rollenHoehe > maxRollenHoehe ) { 
			rollenHoehe = maxRollenHoehe;			
		}
		// JOptionPane.showMessageDialog(null, "a rolle="+role+"="+rollenHoehe);	
		role = Rollen.getRoleByHeight(rollenHoehe);
		// JOptionPane.showMessageDialog(null, "b rolle="+role+"="+rollenHoehe);	
		roleIndex = rollenHoehe;
		roleIsTestUp = Rollen.roleIsTestUp(role);
		roleIsAdminUp = Rollen.roleIsAdminUp(role);
		roleIsExpertUp = Rollen.roleIsExpertUp(role);
						
		roles = Rollen.getRoleArray(maxRollenHoehe, allowedRoles);		

		
		lang = wsRoot.getAttributeValue("lang");
		if ( lang == null )
			lang = "en";  /* REPLACE */
		
		sm.out.println("  user@role = " + role);
		sm.out.println("  user@lang = " + lang);
		
		// Computer
		computer = wsRoot.getAttributeValue("last_computer");
		if ( computer != null && computer.equals("")) computer = null;
	
		if ( computer !=null )
			sm.out.println("  computer = " + computer);	
		
		Element ce = wsRoot.getChild("computers");
		if ( ce == null ) {
			computers = new String[0];
		} else {
			
			List<Element> cle = ce.getChildren("computer");
			computers = new String[cle.size()];
			int pos = 0;
			Iterator<Element> ix = cle.iterator();
			while ( ix.hasNext()) {
				Element e = (Element)ix.next();
				computers[pos] = e.getText();
				pos++;
				if ( computer != null && e.getText().equals(computer)) {
					if ( e.getAttribute("speed") != null ) {
						Workspace.speed = Commons.string2int(e.getAttributeValue("speed"));	
						sm.out.println("SPEEEEED was SEEEET to "+Workspace.speed);
					}
					isTablet=Commons.hasFlag(e, "tablet");
				}
			}	
		}
		
		sm.out.println("  speed = " + Workspace.speed );

		// os		
		String os1 = System.getProperty("os.name");
		if ( os1.startsWith("Windows")) {
			os = "win";
			isWin = true;
		} else if ( os1.startsWith("Mac")) {
			os = "mac";	
			isMac = true;			
		} else if ( os1.startsWith("Unix")) {
			os = "unix";
			isLinux = true;
		} else if ( os1.startsWith("Linux")) {
			os = "linux";
			isLinux = true;
		} else
			os = os1;

		sm.out.println("  os = " + os);
		
		// LIBS
		sm.out.println("libs");	
		Element eLibs = wsRoot.getChild("libs");

		// ANT
		if ( ip != null && ip.overwriteClassloader != null ) {
			//antClassLoaderFilename="?tomcat?";
			
			overwrittenClassloader = ip.overwriteClassloader;
			sm.out.println("Classloader was overwritten with ["+ip.overwriteClassloader.hashCode()+"]");
			antClassLoader = ip.overwriteClassloader;		
			poiClassLoader = ip.overwriteClassloader;	
		} else {
			String antClassLoaderFilename = getLibFile(eLibs, "ant", Wellknown.std_lib_ant,setWsSitzung);
			sm.out.println("ooo antClassLoaderFilename="+antClassLoaderFilename);
			if ( antClassLoaderFilename != null ) {
				antClassLoader = LocalFilesystem.getClassLoaderFromFilename(antClassLoaderFilename);
				sm.out.println("  Loading Ant from me and "+antClassLoaderFilename);			
			} else {
				sm.out.println("  Ant Library not installed (until now, but we try it ...");	
				try {
					String neues_ziel = sysDir+"libs"+FileName.SEPARATOR;
					JarFile meinJarFile  = new JarFile(new File(Zet_Locator.getJarFile()));
					copyResourceFolder2("resources/libs/", neues_ziel,meinJarFile,setWsSitzung);
					antClassLoaderFilename = sysDir+"libs"+FileName.SEPARATOR+Wellknown.std_lib_ant;
					antClassLoader = LocalFilesystem.getClassLoaderFromFilename(antClassLoaderFilename);
					sm.out.println("  Ant Library now installed by extraction of resources/libs in "+Zet_Locator.getJarFile());
				} catch (Exception e) {
					sm.out.println("  ... but it did now work out");
					antClassLoader = null;
				}	
			}
			// POI
			String poiClassLoaderFilename = getLibFile(eLibs, "poi", Wellknown.std_lib_poi,setWsSitzung);
			if ( poiClassLoaderFilename != null ) {
				poiClassLoader = LocalFilesystem.getClassLoaderFromFilename(poiClassLoaderFilename);
				sm.out.println("  Loading Poi from "+poiClassLoaderFilename);			
			} else {
				sm.out.println("  Poi Library not installed");	
			}				
		}

		
		/*

		*/
		
	
		
		// Final
		sm.out.println("Workspace Setup '"+wsElement.getAttributeValue("id")+"' was successfull");
		
	}
		
	public String getHome() {
		
		return ( homeDir);
	}
	
	private static Workspace buildById(String wsFilename ,  String xHomeDir,String wsId, CommStartMsg sm,
			InterfaceParas ip, ISitzung buildByIdSitzung ) throws Exception {
		
		LogConnector lc = new LogConnector("Ws.buildById:");
		Workspace ws = new Workspace(wsFilename, xHomeDir,sm, ip,buildByIdSitzung);
		
		Document doc = CommonsVfs.getXmldocumentByFilename(ws.wsFilename,buildByIdSitzung);
		Element root = doc.getRootElement();
		ws.setWsElement( Commons.xpathSelectSingleNode(root,"workspace[@id="+Commons.xpathstring(wsId)+"]"),root, ip,buildByIdSitzung);
		if ( ws.wsElement == null)
			throw new Exception("Error: The workspace '"+wsId+"' was not found in "+ws.wsFilename);	
		
		return ws;		
	}	
	private static UniqueChecker ucWs = null;
	
	
	public static Workspace build( 
			String _wsFilename, 
			String _homeDir, 
			CommStartMsg sm,
			InterfaceParas ip,
			ISitzung workspaceSitzung
			) throws Exception {
		// sm.out.println("build: DatabaseFile="+_wsFilename);
		LogConnector lc = new LogConnector("ws.build(DbFilename,sm):");

		String wsFilename = null;
		String myHomeDir = _homeDir;
		if ( myHomeDir == null )
			myHomeDir = LocalFilesystem.getHomeDirXrFormat();
		makeDirSure(myHomeDir,workspaceSitzung);
		sm.out.println("Workspace-Builder has home "+myHomeDir);
		ucWs = new UniqueChecker (sm,null);
		

		if ( _wsFilename == null ) {
			if ( CommonsVfs.existsFile(myHomeDir+"default.xrt",workspaceSitzung)) {
				// a default takeoff exists in home
				wsFilename = myHomeDir+"default.xrt";
				sm.out.println("  taking default takeoff from os-dir: "+wsFilename);
			} else if ( CommonsVfs.existsFile(System.getProperty("user.home")+FileName.SEPARATOR+getDotLoxodrom()+FileName.SEPARATOR+"default.xrt",workspaceSitzung)) {
				// a default takeoff exists in the user directory
				// we ask to take it
				int answer3 = JOptionPane.showConfirmDialog(null, "There is a default.xrt in {workstation}"+FileName.SEPARATOR+"{user}"+FileName.SEPARATOR+getDotLoxodrom()+" \nShould I use it?");
				if ( answer3== JOptionPane.OK_OPTION) {
					wsFilename = System.getProperty("user.home")+FileName.SEPARATOR+getDotLoxodrom()+FileName.SEPARATOR+"default.xrt";
					sm.out.println("  taking default takeoff from user-dir: "+wsFilename);
				}
			} 
			if (wsFilename==null) {
				// no default takeoff found, we are going to ask for the takeoff-file

				// testing
				JFrame frame = new JFrame();
				String[] options = new String[2];
				options[0] = new String("Install a new Configuration ...");
				options[1] = new String("Select an existing Configuration ...");
				int result = JOptionPane.showOptionDialog(
						frame.getContentPane(),
						"XmlRobot did not find a configuration file \n(thats a 'takeoff-file')","Install or Select ?", 
						0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
				
				// testingend
				int rueckgabeWert =-1;
				String thefile = null;
				if ( result == -1 ) {
					JOptionPane.showMessageDialog(null, "Have a nice day!");
					System.exit(0);	
				} else if ( result == 1 ) {
				
					JFileChooser chooser= new JFileChooser(myHomeDir);
					chooser.setDialogType(JFileChooser.OPEN_DIALOG);
					// chooser.setToolTipText("Select a Takeoff-File (*.loxo).");
					TakeoffFilter tof = new TakeoffFilter(".xrt");
					chooser.setFileFilter(tof);
					
			        rueckgabeWert = chooser.showDialog(null,"Select Takeoff-File (*.xrt)");
			        if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
			        	thefile = chooser.getSelectedFile().getAbsolutePath();
				} else {
					rueckgabeWert = JFileChooser.CANCEL_OPTION;
				}
		        
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
		        	// takeoff selected
		        	wsFilename = thefile;
		        	sm.out.println("build: choosen file ="+wsFilename);
		        } else {
		        	// not takeoff selected, asking to create one
		        	int answer1;
		        	if ( result == 0 )
		        		answer1 = JOptionPane.OK_OPTION;
		        	else 
		        		answer1 = JOptionPane.showConfirmDialog(null, "Do you want to create a default-takeoff-file ?");
					if ( answer1== JOptionPane.OK_OPTION) {
						// wants to create a takeoff file, but which
						
						CommInstallation coreDialogInstallation = new CommInstallation();
						int response = coreDialogInstallation.askInstallationType2();
						sm.out.println("choice="+response);
						if ( response>= 0 && response<=8) {
							RessourceExtractor re = new RessourceExtractor();
							if ( response == 8 ) {
								// Workstation
								CommonsVfs.mkdirCascade(
										System.getProperty("user.home")+FileName.SEPARATOR+getDotLoxodrom(),
										workspaceSitzung);
								wsFilename = System.getProperty("user.home")+FileName.SEPARATOR+getDotLoxodrom()+FileName.SEPARATOR+"default.xrt";
								re.resourceToFile("default-workstation.xrt", wsFilename, sm,workspaceSitzung);

							}  else if ( response == 4 || response == 5 || response == 6|| response == 7) {
								// Select the output file

								JFileChooser chooserDocu= new JFileChooser(myHomeDir);
								chooserDocu.setDialogType(JFileChooser.SAVE_DIALOG);
								// chooser.setToolTipText("Select a Takeoff-File (*.loxo).");
								TakeoffFilter tof2 = new TakeoffFilter(".xrt");
								chooserDocu.setFileFilter(tof2);
								
						        int rueckgabeWert2 = chooserDocu.showDialog(null,"Create Takeoff-File (*.xrt)");
						        if(rueckgabeWert2 == JFileChooser.APPROVE_OPTION) {
						        	String fn_output = chooserDocu.getSelectedFile().getAbsolutePath();
						        	if ( !fn_output.endsWith(".xrt"))
						        		fn_output = fn_output+".xrt";
						        	sm.out.println("creating takeoff: "+fn_output);
						        	String make_dur_sure = Commons.getDirOfFile(fn_output);
						        	makeDirSure(make_dur_sure,workspaceSitzung);
						        	if ( response == 4 ) {
						        		// Document
										re.resourceToFile("default-standard.xrt", fn_output, sm,workspaceSitzung);
										wsFilename = fn_output;
									} else if ( response == 5 ) {
										// Independent Document
										re.resourceToFile("default-independent.xrt", fn_output, sm,workspaceSitzung);
										wsFilename = fn_output;
									} else if ( response == 6 ) {
										// Computer Document
										re.resourceToFile("default-systemshare.xrt", fn_output, sm,workspaceSitzung);
										wsFilename = fn_output;
									}  else if ( response == 7 ) {
										// Computer Document
										re.resourceToFile("default-local.xrt", fn_output, sm,workspaceSitzung);
										wsFilename = fn_output;
									}
						        } else {
						        	JOptionPane.showMessageDialog(null, "Nothing selected, have a nice day!");
									System.exit(0);
						        }
							} else if ( response == 0 ) {
								wsFilename = myHomeDir+"default.xrt";
								re.resourceToFile("default-standard.xrt", wsFilename, sm,workspaceSitzung);

							} else if ( response == 1 ) {
								wsFilename = myHomeDir+"default.xrt";
								re.resourceToFile("default-independent.xrt", wsFilename, sm,workspaceSitzung);
							} else if ( response == 2 ) {
								wsFilename = myHomeDir+"default.xrt";
								re.resourceToFile("default-systemshare.xrt", wsFilename, sm,workspaceSitzung);
							} else if ( response == 3 ) {
								wsFilename = myHomeDir+"default.xrt";
								re.resourceToFile("default-local.xrt", wsFilename, sm,workspaceSitzung);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Have a nice day!");
							System.exit(0);								
						}							
					} else {
						// does not want
						JOptionPane.showMessageDialog(null, "No Takeoff available, have a nice day!");
						System.exit(0);
					} 		        			        	
		        }
			}
			
		} else {
			if ( CommonsVfs.existsFile(_wsFilename,workspaceSitzung))
				wsFilename = _wsFilename;
			else {
				wsFilename = _homeDir+_wsFilename;
				if ( !CommonsVfs.existsFile(wsFilename,workspaceSitzung)) {
					JOptionPane.showMessageDialog(null, "The file "+_wsFilename+" was not found. \nSearching for it in the home dir ("+wsFilename+") was also not successfull!\nMake sure that is exists.", "Invalid takeoff-file in your parameters", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
				
		}
		
		sm.out.println("Takeoff-File = "+wsFilename);
		Document doc = CommonsVfs.getXmldocumentByFilename(wsFilename,workspaceSitzung);
		Element root = doc.getRootElement();
				
		// Select workspace
		List<Element> wsList = Commons.xpathSelectNodes(root, "./workspace");
		
		sm.out.println("Existing workspaces in '"+wsFilename+"' are:");
		
		// lc.trace("");
		Iterator<Element> i = wsList.iterator();
		while ( i.hasNext()) {
			Element ce = (Element)i.next();
			try {
				if (ucWs.isNew(ce.getAttributeValue("id"), 
							"Workspace '"+ce.getAttributeValue("id")+"' with dir "+ce.getAttributeValue("dir"), 
							"Workspace '"+ce.getAttributeValue("id")+
							"' could not be added because there is already one with that name, this one has dir "+ce.getAttributeValue("dir"))) {
					sm.out.println("  '"+ce.getAttributeValue("id")+"'" );	
				}
			} catch (Exception e) {
				sm.out.println("  '"+ce.getAttributeValue("id")+"'" );
			}
						
		}
		

		if (wsList.size()==0) {
			throw new Exception("There is no workspace available in "+wsFilename);
		}  else if ( root.getAttribute("last_workspace") != null ) {
			sm.out.println("There is a 'last_workspace' with '"+root.getAttributeValue("last_workspace")+"' and it's going to be used.");
			Workspace back;
			try {
				back = buildById(wsFilename, myHomeDir,root.getAttributeValue("last_workspace"),sm, 
						ip,workspaceSitzung);
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				sm.out.println("The last_workspace '"+root.getAttributeValue("last_workspace")+"' was not found.");
				back = buildLastOption( wsFilename, myHomeDir, sm, root, wsList, ip,workspaceSitzung );
			}
			return ( back );
		} else if ( wsList.size()==1) {
			Workspace ws = new Workspace(wsFilename,myHomeDir,sm, ip,workspaceSitzung);
			Element x = (Element)wsList.get(0);
			sm.out.println("There is only 1 Workspace, it will be used and its called '"+x.getAttributeValue("id")+"'");
			ws.setWsElement(x,root, ip,workspaceSitzung);
			return ws;
		} else {
			return buildLastOption( wsFilename, myHomeDir,sm, root, wsList, ip,workspaceSitzung  );			
		}			
	}

	public static Workspace buildLastOption( String wsFilename , String xHomeDir, CommStartMsg sm, 
				Element root, List<Element> wsList,
				InterfaceParas ip, ISitzung buildLastSitzung) throws Exception {
		sm.out.println("A number of workspaces found.The first one is going to be used.");
		Element x = (Element)wsList.get(0);		
		return ( buildById(wsFilename, xHomeDir, x.getAttributeValue("id"),sm, ip,buildLastSitzung));			
	}

	public String getWsFilename() {
		return wsFilename;
	}

	public String getWsId() {
		return wsId;
	}
	public String getWsIdEscaped() {
		return wsIdEscaped;
	}	

	public boolean getNeedsSetup() {
		return needsSetup;
	}
	public String getTplDir() {
		return tplDir;
	}
	public String getMuseumDir() {
		return museumDir;
	}
	/*
	public String getAntClassLoaderFilename() {
		return antClassLoaderFilename;
	} */
	/*
	public String getPoiClassLoaderFilename() {
		return poiClassLoaderFilename;
	}	
	*/
	public boolean isCan_run_multi_jobs() {
		return can_run_multi_jobs;
	}	
	
	public String getAntClassLoaderDesciption() {
		if ( antClassLoader == null )
			return "does not exist";
		else
			return antClassLoader.toString();
	}	
	
	public String getPoiClassLoaderDesciption() {
		if ( poiClassLoader == null )
			return "does not exist";
		else
			return poiClassLoader.toString();
	}	
	
		

}
