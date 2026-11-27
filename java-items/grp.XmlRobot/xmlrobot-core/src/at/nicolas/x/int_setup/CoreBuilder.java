/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.cache.SoftRefFilesCache;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.provider.jar.JarFileProvider;
import org.apache.commons.vfs2.provider.local.DefaultLocalFileProvider;
import org.apache.commons.vfs2.provider.zip.ZipFileProvider;

import at.nicolas.x.fun_string.Escape;
import at.nicolas.x.int_db.Db;
import at.nicolas.x.int_interpreter.CoopGlobalManager;
import at.nicolas.x.int_interpreter.InterAppManager;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.PlugTaskdef;
import at.nicolas.x.int_interpreter.PlugTryFun;
import at.nicolas.x.int_interpreter.PlugTryTask;
import at.nicolas.x.int_interpreter.Reserve;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.InterfaceParas;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.tasks.ExcelUtilPoi;
import at.nicolas.x.tasks.FileSelectorAll;
import at.nicolas.x.term_ti.Ti_TerminalLinkRec;


public class CoreBuilder {
	
	//public static CoreBuilder defaultCoreBuilder;
	private static String theTomcatTempDir = null;
	//private static String serviceAppDir=null;

	public static LogLogger rslogger = null;
	public static MountConfig mc = null;
	public static String sys = null;
	public static String data = null;
	public static Db db = null;
	//private static CoreLogConnector lcs = new CoreLogConnector("Config");
	public static Workspace ws = null;
	private static CommWelcome welcome = null;
	
	public static PlugTaskdef taskdef_ant = null;
	
	public static InterAppManager appManager;
	public static DefaultFileSystemManager fsm = null;


public static void closeWindow() {
	welcome.closeWindow();
	
}
public static void allStartupActions( CommStartMsg sm, boolean online ) throws Exception {
	/*[echnaton-timer]*/
	Interpreter.startupActions(true);
	Ti_TerminalLinkRec.startupActions();
	CoopGlobalManager.startupActions();

	sm.out.println("Did Initialize Interpreter");
	PlugTryTask.startupActions(true,online);
	sm.out.println("Did Initialize Tasks");		
	PlugTryFun.startupActions(true);
	sm.out.println("Did Initialize Functions");
	Escape.startupActions(true);
	sm.out.println("Did Initialize Small-Ones");
	Reserve.startupActions(true);
	sm.out.println("Did Initialize ReservedWordsTrap");
	Lang.startupActions(sm);
	sm.out.println("Did Initialize Languages");
	Commons.startupActions();
	sm.out.println("Did Initialize Commons");	
	

}
public static void buildTest() throws FileSystemException {
	fsm = new DefaultFileSystemManager();
	fsm.setCacheStrategy(CacheStrategy.ON_RESOLVE);
	fsm.setFilesCache(new SoftRefFilesCache());
	fsm.addProvider(
			"file",
			new DefaultLocalFileProvider());
	// fsm.addProvider("zip", new org.apache.commons.vfs2.provider.zip.ZipFileProvider());
	fsm.addProvider(
			"zip",
			new ZipFileProvider());	
	fsm.addProvider(
			"jar",
			new ZipFileProvider());		
	
	fsm.addExtensionMap("zip", "zip");
	fsm.addExtensionMap("jar", "jar");
	fsm.addMimeTypeMap("application/zip", "zip");
    
	fsm.addProvider("ram", new org.apache.commons.vfs2.provider.ram.RamFileProvider());

	fsm.init();
	
}
private static void buildCommon1(CommStartMsg sm, InterfaceParas ip) throws FileSystemException {
	
	sm.out.println("ip...o systemp     "+ip.getOverwriteSystemp());
	sm.out.println("ip...o classloader "+ip.overwriteClassloader);
	// neu
	
	// hier rein
	//JOptionPane.showMessageDialog(null, "A basic JOptionPane message dialog");
	 
	// 
	fsm = new DefaultFileSystemManager();
	fsm.setCacheStrategy(CacheStrategy.ON_RESOLVE);
	fsm.setFilesCache(new SoftRefFilesCache());
	fsm.addProvider(
			"file",
			new DefaultLocalFileProvider());
	// fsm.addProvider("zip", new org.apache.commons.vfs2.provider.zip.ZipFileProvider());
	fsm.addProvider(
			"zip",
			new ZipFileProvider());	
	fsm.addProvider(
			"jar",
			new JarFileProvider());		
	
	fsm.addExtensionMap("zip", "zip");
	fsm.addExtensionMap("jar", "jar");
	fsm.addMimeTypeMap("application/zip", "zip");
    
	fsm.addProvider("ram", new org.apache.commons.vfs2.provider.ram.RamFileProvider());

	fsm.init();
	
	
	// fsm = ((DefaultFileSystemManager) VFS.getManager()).setCacheStrategy(CacheStrategy.ON_RESOLVE);
	
	// nic123 sfsm.closeFileSystem(arg0);
	
	LogLogger.createStandardLoggerIfNotExists("all");
	
}
public static void shutSystemDown() {
	// fsm.closeFileSystem(arg0);
	fsm.close();
	
	theTomcatTempDir = null;

	rslogger = null;
	mc = null;
	sys = null;
	data = null;
	db = null;
	ws = null;
	welcome = null;	
	taskdef_ant = null;	
	appManager = null;
	fsm = null;
}
public static void appendAppsOnline( String serviceAppsZippedDir, ISitzung systemSitzung,
		String systemDir ) throws Exception {
	Sp.p ("CoreBuilder.appendsAppOnline("+serviceAppsZippedDir+")");
	
	SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSSS");
	String isystemDir = "sys/add_"+formatter.format(new Date())+Commons.randomAlphaNumeric(5);
	String myWorkspaceDir = systemDir +isystemDir+"/";
	
    // extract serviceapps
    FileObject foScrSa = CommonsVfs.resolveFolderRead(
    						serviceAppsZippedDir,systemSitzung);
    FileObject[] sa = CommonsVfs.getNinos(foScrSa,systemSitzung);
    int addedApps = 0;
    
	for (int i=0; i<sa.length; i++) {
		FileObject  einSaZipped =  sa[i];
		if ( einSaZipped.isFile() ) {
			if ( einSaZipped.getName().getExtension().equals("zip")) {
				/*[echnaton-timer]*/
				String appname=einSaZipped.getName().getBaseName();
				if ( appname.startsWith("APP_")) {
					appname = appname.substring(4);
					if ( appname.indexOf("_") > -1 ) {
						appname=appname.substring(0, appname.indexOf("_"));
					}
				}
				if ( appname.endsWith(".zip"))
					appname=appname.substring(0, appname.length()-4);
				
				if ( db.getAppById(appname) != null ) {
					Sp.p("Die app '"+appname+"' ist schon installiert" );
				} else {
					appname = "app."+appname;
					Sp.p("2 "+einSaZipped.getName().getBaseName() 
									+" --> "+appname);
					
					FileObject fxScr = einSaZipped;
					String ziel = myWorkspaceDir+appname+"/";

						Sp.p("Nachinstallation Extrahiere '"+einSaZipped.getName().getURI()+"' nach "+ziel);
						CommonsVfs.grantsFileRead(fxScr, systemSitzung);
						
						FileObject fxDest = CommonsVfs.resolveAnySilent(ziel); 
								
						long opnum = CommonsVfs.grantsUpdateFolderP1(fxDest,systemSitzung,"CoreBuilder.Nachinstallation");
						FileObject myzipFileSystem = fsm.createFileSystem(fxScr);
						fxDest.copyFrom(myzipFileSystem, new FileSelectorAll());
						CommonsVfs.grantsUpdateFolderP2(fxDest,systemSitzung,opnum);
						Sp.p("... did extract Ws-APP to ["+fxDest.getName().getURI()+"]");
						addedApps++;	
				
				}

			}
		}
	}	
	if ( addedApps > 0 ) {
		Sp.p("Going to append "+addedApps+" apps ...");
		db.appendAppsOnline(myWorkspaceDir,systemSitzung);
		
	}
	
}
public static void buildOnline(
		String servicesZip, // zip-file mit services
		String serviceAppsZippedDir, // directory with zipped apps
		String tomcatTempDir,
		String paraHome, 
		CommStartMsg sm,
		InterfaceParas ip,
		ISitzung isitzung,
		String systemDir
		) throws Exception {
	theTomcatTempDir = tomcatTempDir;
	buildCommon1(sm,ip);
	sm.out.println("Doing Configuration for fileServices  ["+servicesZip+"]");	
	sm.out.println("                        tomcatTempDir ["+tomcatTempDir+"]");
	sm.out.println("                        servicesZip   ["+servicesZip+"]");
	
	
	SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSSS");

	sm.out.println(" -->                    systemDir   ["+systemDir+"]");
	
	String servicesDir = systemDir + "services/";
	sm.out.println("                        services    ["+servicesDir+"]");
	
	String  serviceAppDir = systemDir + "service_apps/";
	sm.out.println("                      service_apps  ["+serviceAppDir+"]");
	
	// extract services
	FileObject foScr = CommonsVfs.resolveFolderRead(servicesZip,isitzung);
	FileObject foDest = CommonsVfs.resolveAnySilent(servicesDir); 
			
	long opnum = 		CommonsVfs.grantsUpdateFolderP1(foDest,isitzung,"CoreBuilder.ServiceExtraction");
	

	
    final FileObject zipFileSystem = fsm.createFileSystem(foScr);
    Exception problem = null;
    try {

   	 	foDest.copyFrom(zipFileSystem, new FileSelectorAll());
   	 	
   	 	sm.out.println("                        ... did extract SERVICES to ["+servicesDir+"]");
    } catch (Exception e1) {
		Sp.p("Problem in doing the copy");
		Sp.pe(e1);
		problem = e1;
	}
    finally
    {
       zipFileSystem.close();
       CommonsVfs.grantsUpdateFolderP2(foDest,isitzung,opnum);
    }	
    
    if ( problem != null ) {
    	throw problem;
    }
    
    // extract serviceapps
    
    FileObject foScrSa = CommonsVfs.resolveFileRead(serviceAppsZippedDir,isitzung);
    FileObject[] sa = CommonsVfs.getNinos(foScrSa,isitzung);
    
	for (int i=0; i<sa.length; i++) {
		FileObject  einSaZipped =  sa[i];
		if ( einSaZipped.isFile() ) {
			if ( einSaZipped.getName().getExtension().equals("zip")) {
				String appname=einSaZipped.getName().getBaseName();
				if ( appname.startsWith("APP_")) {
					appname = appname.substring(4);
					if ( appname.indexOf("_") > -1 ) {
						appname=appname.substring(0, appname.indexOf("_"));
					}
				}
				if ( appname.endsWith(".zip"))
					appname=appname.substring(0, appname.length()-4);
				appname = "app."+appname;
				Sp.p("               "+einSaZipped.getName().getBaseName() 
								+" --> "+appname);
				
				FileObject fxScr = einSaZipped;
				String ziel = serviceAppDir+appname+"/";
				if ( CommonsVfs.existsDir(ziel,isitzung) ) {
					sm.out.println("                        ... did NOT extract SERVICEAPP because folder exists already ["+ziel+"]");
				} else {
					CommonsVfs.grantsFileRead(fxScr, isitzung);
					FileObject fxDest = CommonsVfs.resolveAnySilent(ziel);
							
					long opnum2 = CommonsVfs.grantsUpdateFolderP1(fxDest,isitzung,"CoreBuilder.Unzip ServiceApp");
					FileObject myzipFileSystem = fsm.createFileSystem(fxScr);
					fxDest.copyFrom(myzipFileSystem, new FileSelectorAll());
					CommonsVfs.grantsUpdateFolderP2(fxDest,isitzung, opnum2);
			   	 	sm.out.println("                        ... did extract SERVICEAPP to ["+fxDest.getName().getURI()+"]");
					
				}
			}
		}
	}

	String workspaceDir = systemDir + "workspace/";
	sm.out.println("                      workspaceDir  ["+workspaceDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(workspaceDir),isitzung);
	
	String museumDir = systemDir + "museum/";
	sm.out.println("                      museumDir     ["+museumDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(museumDir),isitzung);
	
	String materialDir = systemDir + "material/";
	sm.out.println("                      materialDir     ["+materialDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(materialDir),isitzung);	

	String thirdDir = systemDir + "third-party-licenses/";
	sm.out.println("                   thirdPartyLicDir   ["+thirdDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(thirdDir),isitzung);	
	
	String backupDir = systemDir + "backup/";
	sm.out.println("                   backupDir   ["+backupDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(backupDir),isitzung);		
	
	String statusDir = systemDir + "status/";
	sm.out.println("                   statusDir   ["+statusDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(statusDir),isitzung);	

	String takeoffDir = systemDir + "takeoff/";
	sm.out.println("                   takeoffDir   ["+takeoffDir+"]");
	CommonsVfs.mkdirCascade(CommonsVfs.resolveFolderSilent(statusDir),isitzung);	
	
	String takeoffFile = takeoffDir + "takeoff.xrt";
	

	StringBuilder sb = new StringBuilder();
	sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	sb.append("<takeoff last_role=\"admin\">");
	sb.append("  <system dir=\""+systemDir+"\"/>");
	sb.append("  <museum dir=\""+museumDir+"\"/>");	
	sb.append("  <workspace id=\"one\" dir=\""+workspaceDir+"\"/>");
	sb.append("</takeoff>");
	
	CommonsVfs.writeToEncodedFile(sb.toString(),takeoffFile,"UTF-8",isitzung);
	
	sm.out.println("       ... created takeoff-file   ["+takeoffFile+"]");


	// creating workspace
	ws = Workspace.build(takeoffFile,paraHome,sm,ip,isitzung);
    
    
	buildCommon2(sm,ip,true,isitzung);
    
		
}
private static void buildCommon2(CommStartMsg sm, InterfaceParas ip, boolean online, ISitzung isitzung) throws Exception  {
	
	data = ws.getWsDir();
	sys = ws.getSystemDir();
	
	// RessourceExtractor re = new RessourceExtractor();
	appManager = new InterAppManager();
		
	Core core = new Core(data,sys, ws.getBackupDir(), ws.getLogsDir(), ws.getStartupSetup()|ws.getNeedsSetup(),sm,
			ws.getSystempDir(),ws.getStatusDir(),ws.getMuseumDir(),ws.getHome(),isitzung);

	core.run();
	CoreBuilder.mc = core.getMountConfig();

	// Logging
	CoreBuilder.rslogger.setLevel(LogLogger.string2Level (ws.getLogLevel() ));
	if (ws.getLogConsole()) LogLogger.appStandardConsoleAppender();
	
	// Db Anlegen
	db = new Db();
	db.build( ws.getDbl(), 
			ws.getWsDir(), 
			ws.getSystemDir(), 
			Entities.exportEntities(CoreBuilder.mc.getValue(MountConfig.DIR_SERVICE_APPS)),
			sm,
			online,
			isitzung);
	// db.trace();
	// test ANT
	// String antJarFilename = CoreBuilder.ws.getAntClassLoaderFilename();
	String antClassName = "at.nicolas.x.tasks.Ant";
	if ( ws.getAntClassLoader() == null) {
		sm.out.println("Ant: No ANT-Classloader");
	} else {
		try {
			sm.out.println("Ant: Going to test ant with loader: "+ws.getAntClassLoaderDesciption());
			Sp.p("\nAnt goes "+ws.getAntClassLoaderDesciption()+" load started ...");
			Sp.ph("Wir schauen us jetzt mal das ant an ...");
			taskdef_ant = PlugTaskdef.newInstanceTrustedByClassloader("ant", antClassName , ws.getAntClassLoader() , null);
			Sp.ph("Ant-Test was successfull");
			sm.out.println("Ant: Test was successfull" );
		} catch (Exception e) {
			sm.out.println("Ant: Test did not work for "+antClassName);
			Sp.ph("Ant-Test did not work for "+antClassName+", but its ok!");
			// out, Class to load= "+antClassName+", jar="+ws.getAntClassLoaderDesciption());
			//sm.out.println("ExceptionMessage="+e.getLocalizedMessage());
			taskdef_ant = null;
		}		
	}

	if ( ws.getPoiClassLoader() == null ) {
		sm.out.println("Poi: No POI (Excel)!");
	} else {
		try {
			Sp.p("Starting Poi-Test ...");
			sm.out.println("Poi: Starting Test ...");
			ExcelUtilPoi.setup(ws.getPoiClassLoader());
			sm.out.println("Poi: Test was successfull");
			Sp.p("Poi-Test was successfull");

		} catch (Exception e) {
			Sp.pe(e);
			Sp.p("  * ");				
			Sp.p("*   ");			
			sm.out.println("* ************************************************************************");
			sm.out.println("Poi: Poi-Test did not work out, jar="+ws.getPoiClassLoaderDesciption());
			sm.out.println("Poi: ExceptionMessage="+e.getMessage());
			sm.out.println("Poi: LocalExceptionMessage="+e.getLocalizedMessage());
			Sp.p("* * Poi-Test did not work out, jar="+ws.getPoiClassLoaderDesciption());
			Sp.p("* * ExceptionMessage="+e.getMessage());
			Sp.p("* * LocalExceptionMessage="+e.getLocalizedMessage());
			sm.out.println("* ************************************************************************");
			sm.out.println("*   ");		
			sm.out.println("  * ");				

		}			
	}
	sm.printImportants();
	sm.out.println("Init of Scripting-Framework successfull.");
		

	
}
public static void build( 
		String wsFilename, 
		String paraHome, 
		CommStartMsg sm,
		InterfaceParas ip,
		ISitzung isitzung
		) throws Exception {
	
	sm.out.println("Doing Configuration for Takeoff ["+wsFilename+"]");		
	buildCommon1(sm,ip);


	
	ws = Workspace.build(wsFilename,paraHome,sm,ip,isitzung);
	buildCommon2(sm,ip,false,isitzung);

	
}

	
}