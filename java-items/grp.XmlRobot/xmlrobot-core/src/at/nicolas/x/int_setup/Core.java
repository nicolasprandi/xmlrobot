/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.vfs2.FileName;

import at.nicolas.x.int_const.Constants;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;




public class Core  {
	public static String futureFile = null;
	
	private MountConfig mountConfig = null;
	
	private static final int DIR_SYS           =  0;
	
	private static final int DIR_SERVICES                  = 30 ;
	public static final int DIR_SERVICE_APPS              = 31 ;
	private static final int FILE_SYSCALLS_BUILTIN         = 70 ;
	
	private static final int DIR_ANTLIBS_BUILTIN           = 40 ;
	private static final int DIR_ANTLIBS_CUSTOM            = 41 ;
	private static final int DIR_ANTLIBS_ROOT              = 42 ;
	
	private static final int DIR_LIBS              = 64 ;
	private static final int DIR_INT              = 65 ;


	/* private static final int DIR_KNOWNCOMPUTERS            =  4; */
	private static final int DIR_JAVA                      =  8;
	private static final int DIR_JAVA_COMPILE              = 12;
	private static final int DIR_JAVA_COMPILE_SRC          = 13;
	private static final int DIR_JAVA_COMPILE_BIN          = 14;
	private static final int DIR_JAVA_COMPILE_SRC_STD      = 51;
	private static final int FILE_ADDONS_JAR               = 15;
	/* private static final int DIR_STICK_STORAGE             = 22; */
		
	private static final int DIR_STATUS                    = 60;
	private static final int FILE_GUI_PERSPLIST            = 61;
	private static final int FILE_GUI_STATUS               = 63;
	// private static final int FILE_COMPILE_XML              =  2;
	
	// private static final int DIR_STORAGE          = 23;
	private static final int DIR_STORAGE_BACKUP   = 16;
	private static final int DIR_STORAGE_LOGS     = 17;
	private static final int DIR_STORAGE_SYSTEMP  = 21;

	private static final String fileGuiPerspList = "perspList.xml";
	private static final String fileGuiStatus = "guistatus.xml";
	
	public static final String def_hiddenApps = "hiddenApps";
	

	private static String dirData= null;
	private static String dirDataEscaped = null;
	private static String dirSys = null;
	private static String dirSystemp = null;
	private static String dirBackup = null;
	private static String dirLog = null;	
	private static String dirStatus = null;
	private static String dirMuseum = null;
	private static String dirHome= null;
	
	private static String dirSysEscaped = null;
	private static String dirSystempEscaped = null;
	private static String dirBackupEscaped = null;
	private static String dirLogEscaped = null;	
	private static String dirStatusEscaped = null;
	private static String dirMuseumEscaped = null;
	private static String dirHomeEscaped= null;
	
	private static boolean setup = false;
	private static CommStartMsg sm = null;
	
	private ISitzung coreSitzung;	
	public Core ( String _data, String _sys, String _backup, String _log, boolean _setup, CommStartMsg _sm, 
			String _systemp, 
			String _dirStatus, String _dirMuseum, String _dirHome, ISitzung coreSitzung ) throws Exception {
		this.coreSitzung = coreSitzung; 
		if (_data == null )
			throw new Exception("Parameter data must be defined.");
		if (_sys == null )
			throw new Exception("Parameter sys must be defined.");
		if (_backup == null )
			throw new Exception("Parameter backup must be defined.");
		if (_log == null )
			throw new Exception("Parameter log must be defined.");		
		
		if (_systemp == null )
			throw new Exception("Parameter systemp must be defined.");			
		if (_dirStatus == null )
			throw new Exception("Parameter _dirStatus must be defined.");
		if (_dirHome == null )
			throw new Exception("Parameter _dirHome must be defined.");
		sm = _sm;
		dirData = _data;
		dirDataEscaped = Entities.importEntities(_data);
		
		dirSys = _sys;
		dirSystemp = _systemp;
		dirBackup = _backup;
		dirLog = _log;
		dirStatus = _dirStatus;
		setup = _setup;		
		dirMuseum = _dirMuseum;
		dirHome = _dirHome;
		
		dirSysEscaped = Entities.importEntities(_sys);
		dirSystempEscaped = Entities.importEntities(_systemp);
		dirBackupEscaped = Entities.importEntities(_backup);
		dirLogEscaped = Entities.importEntities(_log);
		dirStatusEscaped = Entities.importEntities(_dirStatus);		
		dirMuseumEscaped = Entities.importEntities(_dirMuseum);
		dirHomeEscaped = Entities.importEntities(_dirHome);		
	}
	

	public void run() throws Exception {
		
		checkSys();
		checkData();				
		checkSampleApps();		
		 mountConfig = buildMountConfig();
		
	}

	public MountConfig getMountConfig() throws Exception {
		return mountConfig;
	}
	
	
	private void checkSys() throws Exception {
		String x = "XR-Installation not valid, expecting to exist:";

		if ( setup ) {

			CommonsVfs.mkdirCascade ( getMount(DIR_SERVICES),coreSitzung);
			CommonsVfs.mkdirCascade ( getMount(DIR_SERVICE_APPS),coreSitzung);
			
			CommonsVfs.mkdirCascade ( getMount(DIR_LIBS),coreSitzung);
			
			CommonsVfs.mkdirCascade ( getMount(DIR_STATUS),coreSitzung);
			
			
			// systemscripts
			try {
				CommonsVfs.mkdirCascade ( getMount(DIR_STATUS),coreSitzung);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new Exception("Please hit the 'Try Again' Button once more! \n\n  Want to know more?\n  Computer is confused, did assume something which is not true :-)\n  This usually happens in the inital-setup-phase! \n  BUT: Don't worry, just try again!\n\n  The newly created directories are listed on this screen highlighted by:  * IMPORTANT INFO *");
				/* lc.abend(e.getMessage());*/
			}
					
		}
	}

	private void checkData() throws Exception {
		
		if ( setup ) {
			CommonsVfs.mkdirCascade ( getMount(DIR_STORAGE_BACKUP),coreSitzung);		
			CommonsVfs.mkdirCascade ( getMount(DIR_STORAGE_LOGS),coreSitzung);
			CommonsVfs.mkdirCascade ( getMount(DIR_STORAGE_SYSTEMP),coreSitzung);		
		}
	}
		
	private void checkSampleApps() throws Exception {
			
	}	
	
	
	private void checkSampleAppsFirstTry(ISitzung sampleSitzung) throws Exception {

		String appDir = dirData+"app.helloWorld";
		String instDir = dirData+"app.helloWorld"+FileName.SEPARATOR+"inst.default";
		
		// hier muss dann vielleicht eher das dataDirectory verwendet werden
		String hiddenAppsDir = dirSys+def_hiddenApps;
		String appHelloWorldDir = dirSys+def_hiddenApps+FileName.SEPARATOR+"app.helloWorld";

		String jobFrom = "sampleApps"+FileName.SEPARATOR+"app.helloWorld"+FileName.SEPARATOR+"jobs.xml";
		String jobTo   = dirSys+"hiddenApps"+FileName.SEPARATOR+"app.helloWorld"+FileName.SEPARATOR+"jobs.xml";		
		
		if ( !CommonsVfs.existsDir(appDir,coreSitzung) ) {
			CommonsVfs.mkdirCascade(appDir,coreSitzung);
			sm.out.println("HH: Creating app.helloWorld-Data");
		}	
		
		if ( !CommonsVfs.existsDir(instDir,coreSitzung) ) {
			CommonsVfs.mkdirCascade(instDir,coreSitzung);
			sm.out.println("HH: Creating app.helloWorld"+FileName.SEPARATOR+"inst.default");
		}	

		if ( !CommonsVfs.existsDir(hiddenAppsDir,coreSitzung) ) {
			CommonsVfs.mkdirCascade(hiddenAppsDir,coreSitzung);
			sm.out.println("HH: Creating hiddenApps");
		}	
		
		if ( !CommonsVfs.existsDir(appHelloWorldDir,coreSitzung) ) {
			CommonsVfs.mkdirCascade(appHelloWorldDir,coreSitzung);
			sm.out.println("HH: Creating app.helloWorld-Software");
		}	
		
		if ( !CommonsVfs.existsFile(jobTo,sampleSitzung) ) {
			resourceToFile(jobFrom, jobTo );
			sm.out.println("HH: Creating app.helloWorld"+FileName.SEPARATOR+"jobs.xml");
		}			
		// hier muss dann vielleicht eher das homeDirectory verwendet werden
		hiddenAppsDir = dirData;
		appHelloWorldDir = dirData+"app.helloWorld";
				
	}
	/*
	private void copySystemscript(String xslName ) throws Exception {
		resourceToFile("services"+FileName.SEPARATOR+""+xslName, getMount(DIR_SERVICES)+xslName);		
	}
	
	private void copyStatus(String fn ) throws Exception {
		if ( !Commons.existsFile(getMount(DIR_STATUS)+fn))
			resourceToFile("status"+FileName.SEPARATOR+""+fn, getMount(DIR_STATUS)+fn);		
	}	

	private void copyAntlib(String jarName ) throws Exception {
		resourceToFile("antlibs"+FileName.SEPARATOR+jarName, getMount(DIR_ANTLIBS_BUILTIN)+jarName);
	}
	*/
	public  void resourceToFile(String resourcename, String filename ) throws Exception {
		if ( Constants.resources_from_DEVELOPMENT )
			resourceToFile_DEVELOPMENT(resourcename, filename );
		else
			resourceToFile_DEPLOYED(resourcename, filename );			
	}

	public  void resourceToFile_DEVELOPMENT(String resourcename, String filename ) throws IOException, StorageLimitException {

		String fnIn = LocalFilesystem.getFilenameOfResource(resourcename);
		CommonsVfs.copy_filename(fnIn, filename,coreSitzung);
		sm.out.println("created '"+filename+"' from fs-ressource '"+resourcename+"'");
	}

	public void resourceToFile_DEPLOYED(String resourcename, String filename ) throws Exception  {
		String res = ""+FileName.SEPARATOR+"resources"+FileName.SEPARATOR+""+resourcename;
		InputStream is = this.getClass().getResourceAsStream( res );
		if ( is == null )
			throw new Exception("resourceToFile_DEPLOYED: Ressource '"+res+"'not found");
		sm.out.println("going to create file '"+filename+"' from jar-ressource '"+resourcename+"'");
		
		OutputStreamRequest osr =CommonsVfs.getOutputStreamByFilenameP1(filename, false,coreSitzung);
				
		OutputStream os = osr.getOutputStream();
		
		try {
			Commons.copy_stream ( is, os);
			//is.close();
			//os.close();
		}/* catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
			try {
				if ( is!= null)
				is.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}
			try {
				if ( os != null )
				os.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Sp.pe(e1);
			}
			CommonsVfs.finishOutputStreamP2(filename, coreSitzung,osr.opnum);
			throw e;
		}*/ finally {
			if ( is!= null)
				is.close();
			if ( os != null )
				os.close();
			CommonsVfs.finishOutputStreamP2(filename, coreSitzung,osr.getOpnum());
		}
		sm.out.println("created '"+filename+"' from jar-ressource '"+resourcename+"'");
	}
	
	
	private MountConfig buildMountConfig() throws Exception {

		MountConfig x = new MountConfig(this.coreSitzung.isOnline());


		x.add("dir","workspace",n2n(dirDataEscaped));
		x.add("dir","system",n2n(dirSysEscaped));
		x.add("dir","museum",n2n(dirMuseumEscaped));
		x.add("dir","home",n2n(dirHomeEscaped));
		// nic321
		x.add("dir","services",n2n(Entities.importEntities(getMount(DIR_SERVICES))));
		x.add("dir","service_apps",n2n(Entities.importEntities(getMount(DIR_SERVICE_APPS))));

		x.add("dir","libs",n2n(Entities.importEntities(getMount(DIR_LIBS))));
			
		x.add("file","guiStatus",n2n(Entities.importEntities(getMount(FILE_GUI_STATUS))));		
		// x.add("file","guiPerspList",n2n(getMount(FILE_GUI_PERSPLIST)));
		
		x.add("dir","backup",n2n(Entities.importEntities(getMount(DIR_STORAGE_BACKUP))));
		x.add("dir","logs",n2n(Entities.importEntities(getMount(DIR_STORAGE_LOGS))));
		x.add("dir","systemp",n2n(Entities.importEntities(getMount(DIR_STORAGE_SYSTEMP))));		

		//FileObject fo = Commons.getFileObjectByFilename();	
		
		x.add("file", "takeoff",Entities.importEntities(CoreBuilder.ws.getWsFilename()));

		x.add("prop", "workspace",CoreBuilder.ws.getWsIdEscaped());
		
		x.definitionFinished();
		
		return ( x );
				
	}
	
	private String n2n ( String in ) {
		if ( in == null )
			return ("NULL");
		else
			return in;
	}
	public static String getMountSystem() {
		return getMount(DIR_SYS);
	}
	public static String getMount ( int what ) {
		String sys = dirSys;
		String syscalls = sys + "services" + FileName.SEPARATOR;
		String service_apps = sys + "service_apps" + FileName.SEPARATOR;
		String antlibs = sys + "antlibs" + FileName.SEPARATOR;
		String status = dirStatus;
		if ( what == DIR_SYS )
			return sys;
		else if ( what == DIR_SERVICES )
			return ( syscalls );
		else if ( what == DIR_SERVICE_APPS )
			return ( service_apps );
		else if ( what == FILE_SYSCALLS_BUILTIN )
			return ( syscalls+"builtin.jobs.xml");		
		else if ( what == DIR_LIBS )
			return (  sys + "libs" + FileName.SEPARATOR );		
		else if ( what == DIR_INT )
			return (  sys + "int" + FileName.SEPARATOR );			
		else if ( what == DIR_ANTLIBS_ROOT )
			return ( antlibs );
		else if ( what == DIR_ANTLIBS_CUSTOM )
			return ( antlibs + "custom" + FileName.SEPARATOR);
		else if ( what == DIR_ANTLIBS_BUILTIN )
			return ( antlibs + "builtin" + FileName.SEPARATOR);
		else if ( what == DIR_STORAGE_BACKUP )
			return ( dirBackup  );
		else if ( what == DIR_STORAGE_LOGS )
			return (dirLog  );
		else if ( what == DIR_STORAGE_SYSTEMP )
			return ( dirSystemp  );
		else if ( what == DIR_JAVA )
			return ( sys + "java" + FileName.SEPARATOR );
		else if ( what == DIR_JAVA_COMPILE )
			return ( sys + "java" + FileName.SEPARATOR +"compile" + FileName.SEPARATOR );
		else if ( what == DIR_JAVA_COMPILE_SRC )
			return ( sys + "java" + FileName.SEPARATOR +"compile" + FileName.SEPARATOR+"src" + FileName.SEPARATOR );
		else if ( what == DIR_JAVA_COMPILE_BIN )
			return ( sys + "java" + FileName.SEPARATOR +"compile" + FileName.SEPARATOR+"bin" + FileName.SEPARATOR );
		else if ( what == DIR_JAVA_COMPILE_SRC_STD )
			return ( sys + "java" + FileName.SEPARATOR +"compile" + FileName.SEPARATOR+"srcstd" + FileName.SEPARATOR );
				
		else if ( what == FILE_ADDONS_JAR )
			return ( sys + "java" + FileName.SEPARATOR +"compile" + FileName.SEPARATOR+"addons.jar" );
		
		else if ( what == DIR_STATUS )
			return ( status);
		else if ( what == FILE_GUI_PERSPLIST )
			return ( status +fileGuiPerspList );
		else if ( what == FILE_GUI_STATUS )
			return ( status +fileGuiStatus );	

		return (null);
	}
		
}