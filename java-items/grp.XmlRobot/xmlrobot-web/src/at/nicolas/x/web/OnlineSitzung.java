package at.nicolas.x.web;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.CoopGlobalManager;
import at.nicolas.x.int_interpreter.CoopListOfApp;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.pub.UserInfo;
import at.nicolas.x.term_ti.LinkConverterRequest;

public class OnlineSitzung implements ISitzung {
	//private HashMap<String, ICoop> noMapKeep = new HashMap<String, ICoop>();
	// diese coopList gibts je app
	private static final String starterAppName = "Starter";
	private String privs = null;
	private String privParas = null;
	private String appPrivs = "Formix-Rezeptblock";
	private Parameters privParasParas = new Parameters();
	private Surrounding sourrounding;
	
	private boolean privSeeFnPro = false;
	private boolean privSeeFnApp = false;
	private boolean privSeeFnElse = false;
	
	private boolean privSeeFnLibro = false;
	private boolean privSeeFnSession = false;
	
	private boolean privViewFnPro = false;
	private boolean privViewFnApp = false;
	private boolean privViewFnElse = false;	
	
	private boolean privOpFiles = false;
	private boolean privOpDirs = false;	
	
	private boolean privDbConnect = false;	
	private boolean privDbDecrypt = false;
	private boolean privDbRead = false;	
	private boolean privDbWrite = false;	
	private boolean privDbService = false;	
	private boolean privDump = false;	
	private boolean privDownFiles = false;		
	private boolean privDownPro = false;
	private boolean privDrop = false;

	
	String projectDir;
	String projectId;
	String appDir;
	String appId;
	String systemDir;	
	String databaseDir = null;
	String myUserHome;
	String myUserHomeMem;
	String libroUri;
	String libroUri2;
	String webAppUri;
	String myPostDir;
	
	String projectDirC;
	String appDirC;
	String systemDirC;	
	String databaseDirC = null;
	String myUserHomeC;
	String myUserHomeMemC;
	String libroUriC;
	String libroUriC2;
	String webAppUriC;	
	String myPostDirC;
	
	UserInfo userInfo = null;
	
	private String roleName = "public";
	private int roleIndex = 1;
	private boolean isLoggedIn = false;
	
	private StorageControl scUser = null;
	private StorageControl scUserMem = null;
	private StorageControl scPro = null;
	private StorageControl scDb = null;
	
	public void requestStorageStatus() {
		scUserMem.requestStatus();
		
		scPro.requestStatus();		
		
		scUser.requestStatus();		
		
		if ( scDb != null ) {
			scDb.requestStatus();
		}		

		



		
	}
	
	public void setRoleIndex ( int _roleIndex ) {
		//System.out.println("online sitzung received for roleindex "+_roleIndex);
		roleIndex = _roleIndex; 
		try {
			roleName = Rollen.getRoleByHeight(roleIndex);
			//System.out.println("which is "+roleName);
		} catch (Exception e) {
			roleName = "public";
			roleIndex = 1;
		}
	}
	
	public void setUserInfo( UserInfo _userInfo,HttpServletRequest request ) throws FileSystemException {
		//System.out.println("OnlineSitzung received userinfo ");
		String messageIp = null;
		if ( request != null ) 
			messageIp = "local="+request.getLocalAddr()+" remote="+request.getRemoteAddr();
		else
			messageIp="userlogin in db";
		
		Sp.pUserInfo("did setUserInfo: "+messageIp);
		
		
		userInfo = _userInfo;
		if ( userInfo != null ) {
			String theDatabaseLinkUri = getDatabaseLinkUriDir();
			//if ( theDatabaseLinkUri.startsWith("file:///C:/wow1/db/")) ;
			//else {
			//	( !theDatabaseLinkUri.startsWith("file:///var/www/formix.at/private/formixDd/"))
			// System.out.println("uri+++++++++++++++++++++++++++++++++++++++="+theDatabaseLinkUri);
			/* sicherheitsstufe */ if  ( false ) throw new FileSystemException("There is another database on the floor."+theDatabaseLinkUri);
			//}
				

			//System.out.println("uri ="+theDatabaseLinkUri);
			databaseDir = CommonsVfs.makeyBeautifulUri(theDatabaseLinkUri);
			/*System.out.println("dbdir="+databaseDir+" The userinfo is: "+userInfo.getName()+", "
					+userInfo.getCollection()+", "+userInfo.getDbext()); */
			databaseDirC = databaseDir;
			if ( !databaseDirC.endsWith("/"))
				databaseDirC= databaseDirC+"/";
			scDb = new StorageControl(databaseDirC,userInfo.getDbSize(),this,StorageControl.TYPE_DB,eeProject);
			scDb.calculateCurrentUsage();
			
		} else{
			databaseDir=null;
			databaseDirC=null;
			scDb = null;
			//System.out.println("The database and database was set to null");
		}
		
	}
	public UserInfo getUserInfo( ) {
		return userInfo;
	}

	EeSystem eeSystem;
	EeProject eeProject;
	public OnlineSitzung(
			Surrounding sourrounding,
			String projectDir,
			String projectId,
			String appDir,
			String appId,
			String systemDir,
			String myUserHome,
			String myUserHomeMem,
			String webAppUri,
			EeSystem _eeSystem,
			String myPostDir,
			EeProject _eeProject,
			String _libroUri2 ) throws FileSystemException {
		// Sp.pl("#HARRY# OnlineSitzung aufgerufen mit myPostDir="+myPostDir);
		//System.out.println("libro2 = "+_libroUri2);
		eeSystem = _eeSystem;
		eeProject = _eeProject;
		setLang("de");
		/*
		System.out.println("Online-sitzung received:");
		
		System.out.println("**** projectDir    = "+ projectDir);
		System.out.println("**** appDir        = "+ appDir);
		System.out.println("**** systemDir     = "+ systemDir);
		System.out.println("**** databaseDir   = "+databaseDir );
		System.out.println("**** myUserHome    = "+ myUserHome);
		System.out.println("**** myUserHomeMem = "+ myUserHomeMem);
		System.out.println("**** webAppUri     = "+ webAppUri);
		System.out.println("**** sourrounding  = "+ sourrounding.getDescription());
		*/
		this.myPostDir = myPostDir;
		this.webAppUri = webAppUri;
		
		this.sourrounding = sourrounding;
		this.projectDir = CommonsVfs.makeyBeautifulUri(projectDir) ;
		this.projectId =projectId ;
		this.appDir = CommonsVfs.makeyBeautifulUri(appDir) ;
		this.appId = appId ;
		
		if ( appId.equals(starterAppName) )
			this.hasRightAgent = true;
		else
			this.hasRightAgent = false;	
		
		this.systemDir = CommonsVfs.makeyBeautifulUri(systemDir) ;

		this.myUserHome = CommonsVfs.makeyBeautifulUri(myUserHome);
		this.myUserHomeMem = myUserHomeMem;
		if ( webAppUri != null && webAppUri.length()>0) {
			this.libroUri = webAppUri+"/libro";
		} else
			this.libroUri = null;
		System.out.println("Online.libroUri="+this.libroUri);

		this.libroUri2 = CommonsVfs.makeyBeautifulUri(_libroUri2);
		
		 projectDirC=this.projectDir;
		 appDirC=this.appDir;
		 systemDirC=this.systemDir;	
		 databaseDirC = this.databaseDir;
		 myUserHomeC=this.myUserHome;
		 myUserHomeMemC=this.myUserHomeMem;
		 libroUriC=this.libroUri;
		 libroUriC2=this.libroUri2;
		 webAppUriC=this.webAppUri;	
		 myPostDirC = this.myPostDir;
		 /* SessionLog: hier erzeugen */
		 
		
		
		if ( !webAppUriC.endsWith("/")) webAppUriC = webAppUriC.concat("/");
		if ( !projectDirC.endsWith("/")) projectDirC = projectDirC.concat("/");
		if ( !appDirC.endsWith("/")) appDirC = appDirC.concat("/");
		if ( !systemDirC.endsWith("/")) systemDirC = systemDirC.concat("/");
		if ( !myUserHomeC.endsWith("/")) myUserHomeC = myUserHomeC.concat("/");
		if ( !myUserHomeMemC.endsWith("/")) myUserHomeMemC = myUserHomeMemC.concat("/");
		if ( myPostDirC != null ) {
			if ( !myPostDirC.endsWith("/")) myPostDirC = myPostDirC.concat("/");
		}
		
		
		if ( libroUriC != null ) {
			if ( !libroUriC.endsWith("/")) libroUriC = libroUriC.concat("/");
		}
		if ( libroUriC2 != null ) {
			if ( !libroUriC2.endsWith("/")) libroUriC2 = libroUriC2.concat("/");
		}		
		System.out.println("libroUriC= "+libroUriC);
		System.out.println("libroUriC2= "+libroUriC2);
		
		scUser = new StorageControl(myUserHomeC,sourrounding.getStorageLimit_Session(),this,StorageControl.TYPE_SESSION,eeProject);
		scUserMem = new StorageControl(myUserHomeMemC,sourrounding.getStorageLimit_Mem(),this,StorageControl.TYPE_MEMORY,eeProject);
		scPro = new StorageControl(projectDirC,sourrounding.getStorageLimit_Pro(),this,StorageControl.TYPE_PROJECT,eeProject);

		/* SessionLog: hier erzeugen myUserHomeC + ... */
		//System.out.println("****      libroUri = "+ libroUriC);
		//System.out.println("**** *************** Online-sitzung received report finished.");	
		
	}

	private String explain( ) {
		String das =
		"\nprojectDir        = "+projectDir+", "+

		"\nappDir            = "+appDir+", "+

		"\nsystemDir         = "+systemDir+", "+
		"\ndatabaseDir       = "+databaseDir+", "+
		"\nmyUserHome        = "+myUserHome+", "+
		"\nmyUserHomeMem     = "+myUserHomeMem+", "+
		"\nlibroUri          = "+libroUri+", "+		
		"\nlibroUri2         = "+libroUri2+", "+	
		"\nprojectId         = "+projectId+", "+
		"\nappId             = "+appId+", "+	
		"\npostDir           = "+myPostDir+", "+	
		"\ndatabaseAgain     = "+sourrounding.getDbUserDir();
		das = das + "\n DBCONFIG: "+sourrounding.getDbConfigText();
		
		Sp.pl(das);
		return das;
		
		
	}
	private String getExplain2( ) {
		String das =
		getIdentity()+		
		" project="+projectDirC
		+", app="+ appDirC
		+", system="+ systemDirC	
		+", database="+ databaseDirC
		+", myUserHome="+ myUserHomeC
		+", myPostDir="+ myPostDirC
		+", myUserHomeMem="+ myUserHomeMemC
		+", libro="+ libroUriC
		+", libro2="+ libroUriC2		
		+", webApp="+ webAppUriC	;
		return das;
		
		
	}	
	private void someProblem(FileObject grant, String check, String fromFunction, Exception e ) {
		
		System.out.println("Online-Sitzung-Exception");
		System.out.println("Requested From="+fromFunction);
		if ( grant != null )
			System.out.println("Grant="+grant.getName().getURI());
		System.out.println("Requested Check="+check);
		
		
		if ( e!=null)
		System.out.println("Exception="+e.getMessage());
		
		System.out.println("getExplain2= "+getExplain2());
		System.out.println("libroUpdatePrivileg= "+libroUpdatePrivileg);
				
		
	}
	@Override
	public void grantsFolderRead(FileObject grant) throws FileSystemException {
		if ( grant == null )
			System.out.println("das grant ist sehr leider null");
		else {
			String check = grant.getName().getURI()+"/";

			String tit = "** ** ** ACCESS-REQUEST ** "+check;		
			Sp.pAccessUser("??? "+tit);	
			
			/*if ( check == null )Sp.pAccessUser("nul "+tit);
			else */			
			if ( check.startsWith(projectDirC))Sp.pAccessUser("pro "+tit);
			else if ( check.startsWith(myUserHomeC)) Sp.pAccessUser("ses "+tit);
			else if ( check.startsWith(appDirC)) Sp.pAccessUser("app "+tit);
			else if ( check.startsWith(systemDirC)) Sp.pAccessUser("sys "+tit);
			else if ( check.startsWith(myUserHomeMemC))Sp.pAccessUser("mem "+tit);		
			//else if ( libroUriC!= null && check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC2)) Sp.pAccessUser("lib "+tit);
			else if ( databaseDirC!= null && check.startsWith(databaseDirC)) Sp.pAccessUser("db "+tit);
			
			/*
			else if ( databaseDirC == null && userInfo!=null  ) Sp.pAccessUser("ndb "+tit); // ist gleichbedeutend mit anmeldung
			else if ( check.startsWith(databaseDirC)) Sp.pAccessUser("db  "+tit); 
			*/
			else {
				someProblem(grant,check,"grantsFolderRead",null);	
				// Sp.pAccessViolationMaster(ask+" with "+getExplain2() );	
				Sp.pAccessViolationFolderRead(check+" with "+getExplain2() );				
				throw new FileSystemException("Access not granted for: "+check);
			}				
		}
	
	}	
	

	
	public void grantsFileRead(FileObject grant) throws FileSystemException {
		if ( grant == null )
			System.out.println("das grant ist sehr leider null");
		else {
			String check = grant.getName().getURI();
			// grantsThingRead(grant.getName().getURI());
			String tit = "** ** ** ACCESS-REQUEST ** "+check;
			
			Sp.pAccessUser("??? "+tit);
			
			
			/*if ( check == null )Sp.pAccessUser("nul "+tit);
			else */
			if ( check.startsWith(projectDirC))Sp.pAccessUser("pro "+tit);
			else if ( check.startsWith(myUserHomeC)) Sp.pAccessUser("ses "+tit);
			else if ( check.startsWith(appDirC)) {
				String extension = grant.getName().getExtension();
				if ( extension.equals("xsl") || extension.equals("xrs") || extension.equals("js")|| extension.equals("xrj")) {
					// kein read, nur process
					Sp.pAccessViolation(check+" with "+getExplain2() );				
					throw new FileSystemException("Access not granted for: "+check);					
				} else
					Sp.pAccessUser("app "+tit);
			}
			else if ( check.startsWith(systemDirC)) { 
				String extension = grant.getName().getExtension();
				if ( extension.equals("xsl") || extension.equals("xrs") || extension.equals("js") || extension.equals("xrj")) {
					// kein read, nur process
					Sp.pAccessViolation(check+" with "+getExplain2() );				
					throw new FileSystemException("Access not granted for: "+check);					
				} else
					Sp.pAccessUser("sys "+tit);				 
			}
			else if ( check.startsWith(myUserHomeMemC))Sp.pAccessUser("mem "+tit);		
			// else if ( libroUriC!= null && check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC2)) Sp.pAccessUser("lib "+tit);
			
			/* am 9.1 das rein */
			else if ( databaseDirC!= null && check.startsWith(databaseDirC)) Sp.pAccessUser("db "+tit);
			
			/* am 9.1.rausgenommen
			else if ( databaseDirC == null && userInfo!=null  ) Sp.pAccessUser("ndb "+tit); // ist gleichbedeutend mit anmeldung
			else if ( check.startsWith(databaseDirC)) Sp.pAccessUser("db  "+tit); 
			*/

			else {
				someProblem(grant,check,"grantsFileRead",null);			
				// Sp.pAccessViolationMaster(ask+" with "+getExplain2() );	
				Sp.pAccessViolation(check+" with "+getExplain2() );				
				throw new FileSystemException("Access not granted for: "+check);
			}				
		}
	
	}
	
	public void grantsFileProcess(FileObject grant) throws FileSystemException {
		if ( grant == null )
			System.out.println("das grant ist sehr leider null");
		else {
			String check = grant.getName().getURI();
			// grantsThingRead(grant.getName().getURI());
			String tit = "** ** ** ACCESS-REQUEST ** "+check;
			
			Sp.pAccessUser("??? "+tit);
			
			
			/*if ( check == null )Sp.pAccessUser("nul "+tit);
			else */
			if ( check.startsWith(projectDirC))Sp.pAccessUser("pro "+tit);
			else if ( check.startsWith(myUserHomeC)) Sp.pAccessUser("ses "+tit);
			else if ( check.startsWith(appDirC)) Sp.pAccessUser("app "+tit);
			else if ( check.startsWith(systemDirC)) Sp.pAccessUser("sys "+tit);
			else if ( check.startsWith(myUserHomeMemC))Sp.pAccessUser("mem "+tit);		
			// else if ( libroUriC!= null && check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( check.startsWith(libroUriC2)) Sp.pAccessUser("lib "+tit);
			else if ( databaseDirC!= null && check.startsWith(databaseDirC)) Sp.pAccessUser("db "+tit);
			/*
			else if ( databaseDirC == null && userInfo!=null  ) Sp.pAccessUser("ndb "+tit); // ist gleichbedeutend mit anmeldung
			else if ( check.startsWith(databaseDirC)) Sp.pAccessUser("db  "+tit); 
*/
			else {
				someProblem(grant,check,"grantsFileProcess",null);				
				// Sp.pAccessViolationMaster(ask+" with "+getExplain2() );	
				Sp.pAccessViolationProcess(check+" with "+getExplain2() );				
				throw new FileSystemException("Process Access not granted for: "+check);
			}				
		}
	
	}	
		
	/*
	private void grantsThingRead(String check) throws FileSystemException {
		if ( check == null )
			System.out.println("das grant ist sehr leider null");

		String tit = "** ** ** ACCESS-REQUEST ** "+check;
		
		Sp.pAccessUser("??? "+tit);
		
		try {
			if ( check == null )Sp.pAccessUser("nul "+tit);
			else if ( check.startsWith(projectDirC))Sp.pAccessUser("pro "+tit);
			else if ( check.startsWith(myUserHomeC)) Sp.pAccessUser("ses "+tit);
			else if ( check.startsWith(appDirC)) Sp.pAccessUser("app "+tit);
			else if ( check.startsWith(systemDirC)) Sp.pAccessUser("sys "+tit);
			else if ( check.startsWith(myUserHomeMemC))Sp.pAccessUser("mem "+tit);		
			else if ( libroUriC!= null && check.startsWith(libroUriC)) Sp.pAccessUser("lib "+tit);
			else if ( databaseDirC == null && userInfo!=null  ) Sp.pAccessUser("ndb "+tit); // ist gleichbedeutend mit anmeldung
			else if ( check.startsWith(databaseDirC)) Sp.pAccessUser("db  "+tit); 

			else {
		
				// Sp.pAccessViolationMaster(ask+" with "+getExplain2() );	
				Sp.pAccessViolation(check+" with "+getExplain2() );				
				throw new FileSystemException("Access not granted for: "+check);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new FileSystemException("Access not granted for uri: "+check+" explain="+getExplain2()+"   OrigMsg: "+e.getLocalizedMessage());
		}		
	}	
	*/
	/*
	public void grantsDeleteFolder(FileObject grant) throws FileSystemException {
		if ( grant == null )
			System.out.println("das grant-delete-folder ist sehr leider null");		
		grantsDeleteThing (grant.getName().getURI() +"/");
		
	} */
	/*
	public void grantsDelete(FileObject grant) throws FileSystemException, StorageLimitException {
		if ( grant == null )
			System.out.println("das grant-delete-file ist sehr leider null");		
		grantsModifyFile (grant, 1, 0);
	}
	
	@Override
	public void grantsWrite(FileObject grant, long newSize ) throws FileSystemException, StorageLimitException {
		if ( grant == null )
			System.out.println("das grant-write-file ist sehr leider null");		
		grantsModifyFile (grant, 2, newSize);
		
	}	
	*/
	
	private long opnumSequence = 0;
	
	@Override
	public long grantsUpdateFileP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {
		// TODO Auto-generated method stub
		if ( grant == null )
			System.out.println("das grant-update1-file ist sehr leider null");	
		opnumSequence++;
		grantsModifyFile (grant, 3, 0,opnumSequence,titel);	
		
		return opnumSequence;
	}

	@Override
	public void grantsUpdateFileP2(FileObject grant, long opnum) throws FileSystemException, StorageLimitException {
		// TODO Auto-generated method stub
		if ( grant == null )
			System.out.println("das grant-update2-file ist sehr leider null");		
		grantsModifyFile (grant, 4, 0,opnum,null);		
	}

	private void grantsModifyFile(FileObject grant, int dwuu , long newSize, long opnum, String titel ) throws FileSystemException, StorageLimitException {

		String ask = grant.getName().getURI();
		StorageControl foundGranted = null;
		
		if ( ask == null )Sp.pAccessUser("nul ");
		else if ( ask.startsWith(projectDirC)) foundGranted=scPro;
		else if ( ask.startsWith(myUserHomeC)) foundGranted=scUser;
		else if ( ask.startsWith(myUserHomeMemC)) foundGranted=scUserMem;		
		// else if ( databaseDir == null && userInfo!=null ) ;
		else if ( databaseDirC !=null && ask.startsWith(databaseDirC))  foundGranted=scDb; 
		else if ( myPostDirC != null && ask.startsWith(myPostDirC) ) Sp.pAccessLibroUpdate(ask);
		// else if ( libroUriC!= null && ask.startsWith(libroUriC) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else if ( ask.startsWith(libroUriC) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else if ( ask.startsWith(libroUriC2) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else {
			someProblem(grant,ask,"grantsModifyFile",null);	
	
			// Sp.pAccessViolationMasterDelete(ask+" with "+getExplain2() );	
			Sp.pAccessViolationDelete(ask+" with "+getExplain2() );			
			throw new FileSystemException("Delete-Access not granted for: "+ask);
		}
		
		if ( foundGranted != null ) {
			
			switch ( dwuu ) {
			case 1: {
				// delete
				foundGranted.subFile(grant,opnum,titel);
				break;
				} 
			case 2: {
				// write
				foundGranted.subFile(grant,opnum,titel);
				foundGranted.addFilesize(grant,newSize,opnum);
				break;
			} 
			case 3: {
				// update phase 1
				foundGranted.subFile(grant,opnum,titel);
				break;
			} 
			case 4: {
				// update phase 2
				foundGranted.addFile(grant,opnum);
				break;
			} 
			}

		}
		
		
	}	
	
	public long grantsUpdateFolderP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {

		opnumSequence++;

		grantsUpdateFolder ( grant,1,opnumSequence,titel);	

		
		return opnumSequence;		
	}

	@Override
	public void grantsUpdateFolderP2(FileObject grant, long opnum) throws StorageLimitException, FileSystemException {
		grantsUpdateFolder ( grant,2,opnum,null);		
	}	
	@Override
	public void grantsUpdateFolder(FileObject grant) throws StorageLimitException, FileSystemException {
		grantsUpdateFolder ( grant,3,0,null);
		
	}
	private FileObject grantsUpdateFolder(FileObject inquestion, int phase, long opnum, String titel) throws FileSystemException, StorageLimitException  {
		String ask = inquestion.getName().getURI()+"/";
		StorageControl foundGranted = null;
		if ( ask == null ) Sp.pAccessUser("nul ");
		else if ( ask.startsWith(projectDirC)) foundGranted=scPro;
		else if ( ask.startsWith(myUserHomeC)) foundGranted=scUser;
		else if ( ask.startsWith(myUserHomeMemC)) foundGranted=scUserMem;		
		// else if ( databaseDir == null && userInfo!=null ) ;
		else if ( databaseDirC !=null && ask.startsWith(databaseDirC)) foundGranted=scDb; 
		else if ( myPostDirC != null && ask.startsWith(myPostDirC) ) Sp.pAccessLibroUpdate(ask);
		// else if ( libroUriC!= null && ask.startsWith(libroUriC) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else if ( ask.startsWith(libroUriC) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else if ( ask.startsWith(libroUriC2) && libroUpdatePrivileg) Sp.pAccessLibroUpdate(ask);
		else {

			someProblem(inquestion,ask,"grantsUpdateFolder",null);	
			// Sp.pAccessViolationMasterDelete(ask+" with "+getExplain2() );	
			Sp.pAccessViolationDelete(ask+" with "+getExplain2() );			
			throw new FileSystemException("Delete-Access not granted for: "+ask);
		}
		
		if ( foundGranted != null ) {
			switch ( phase ) {
			case 1: {
				// delete
				foundGranted.subDirSize(inquestion,opnum,titel);
				break;
				} 
			case 2: {
				// write
				foundGranted.addDirSize(inquestion,opnum);
				break;
			} }					
		}
		

		return inquestion;
		
	}	

	@Override
	public String getDescriptor() {
		// TODO Auto-generated method stub
		return "Sitzung: "+explain();
	}
	@Override
	public String getSessionHomeFs() {
		// TODO Auto-generated method stub
		return myUserHomeC;
	}
	@Override
	public String getSessionHomeMem() {
		// TODO Auto-generated method stub
		return myUserHomeMemC;
	}
	
	private int indexNewTempPro = 0;
	@Override
	public String getNextIndexTempPro() {
		// TODO Auto-generated method stub
		indexNewTempPro++;
		return Commons.int2Prefixedstring(indexNewTempPro, 3);
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDatabaseLinkUri() {
		// TODO Auto-generated method stub
		//return null;
		//System.out.println("looking for getDatabaseLinkUri "+sourrounding.getDatabaseLink(this));
		return sourrounding.getDatabaseLink(this);
	}
	
	public String getDatabaseLinkUriDir() {
		// TODO Auto-generated method stub
		//return null;
		//System.out.println("looking for getDatabaseLinkUri "+sourrounding.getDatabaseLink(this));
		String ret = CommonsVfs.makeyBeautifulUri(sourrounding.getDatabaseLink(this))+"/";
		return ret;
	}	

	@Override
	public String getRoleName() {
		// TODO Auto-generated method stub
		return roleName;
	}

	@Override
	public int getRoleIndex() {
		// TODO Auto-generated method stub
		return roleIndex;
	}

	@Override
	public String getLibroDir(String forAppId) {
		// TODO Auto-generated method stub
		 return libroUriC;
	}
	public String getLibroDir2(String forAppId) {
		// TODO Auto-generated method stub
		 return libroUriC2;
	}	

	@Override
	public String getRelativeLibroPath(String filename) throws Exception {
		// TODO Auto-generated method stub
		if ( filename.startsWith(libroUri)) {
			// ok das file liegt im libro
			String ret = filename.substring(webAppUri.length()+1);
			//System.out.println("--- retournieren: "+ret+" - for "+filename);
			return ret;
		} else {
			throw new Exception("Picture out of reach: "+filename);
		}
	
	}
	

	private boolean guiprivJobHeaderName = false;
	private boolean guiprivJobHeaderTip = false;
	private boolean guiprivJobFooter = false;


	@Override
	public void setGuiPrivs(String guiPrivs) {
		// TODO Auto-generated method stub
		if ( guiPrivs == null || guiPrivs.length() != 3 ) {
			guiprivJobHeaderName = false;
			guiprivJobHeaderTip = false;
			guiprivJobFooter = false;
			
		} else {
			guiprivJobHeaderName= Commons.stringxr2bool(guiPrivs.substring(0,1), false);
			guiprivJobHeaderTip = Commons.stringxr2bool(guiPrivs.substring(1,2), false);
			guiprivJobFooter = Commons.stringxr2bool(guiPrivs.substring(2,3), false);
				
		}
		/*
		System.out.println("################ Did setGuiPrivs ######### in OnlineSitzung by ["+guiPrivs+"] to:"+
				guiprivJobHeaderName+" "+
				guiprivJobHeaderTip+" "+
				guiprivJobFooter+" "+
				guiprivEeFooter+" " +
				guiprivIncludeHeader+" " +
				guiprivIncludeFooter+" " 
				); */
		
	}

	@Override
	public boolean hasGuiPrivJobHeaderName() {
		// TODO Auto-generated method stub
		return guiprivJobHeaderName;
	}

	@Override
	public boolean hasGuiPrivJobHeaderTip() {
		// TODO Auto-generated method stub
		return guiprivJobHeaderTip;
	}

	@Override
	public boolean hasGuiPrivJobFooter() {
		// TODO Auto-generated method stub
		return guiprivJobFooter;
	}

	private boolean libroUpdatePrivileg = false;
	@Override
	public void setLibroUpdatePrivileg(boolean mayupdate) {
		libroUpdatePrivileg = mayupdate;
		
	}

	@Override
	public boolean getLibroUpdatePrivileg() {
		// TODO Auto-generated method stub
		return libroUpdatePrivileg;
	}

	private boolean privTrace = false;
	private boolean privEeFooter = false;
	private boolean privAppFooter = false;
	public void setPrivs(String privs) {
		System.out.println("OnlineSitzung.setPrivs ("+privs+") CALLED.");
		clearMyLinkConverter();
		this.privs = privs;
		//System.out.println("setPrivs:"+privs);
		privTrace = Commons.hasFlag(this.privs, "trace");
		privEeFooter = Commons.hasFlag(this.privs, "eeFooter");
		privAppFooter = Commons.hasFlag(this.privs, "appFooter");
		//System.out.println("privAppFooter:"+privAppFooter);
		privSeeFnPro = Commons.hasFlag(this.privs, "linkFnPro");
		privSeeFnApp = Commons.hasFlag(this.privs, "linkFnApp");
		privSeeFnElse = Commons.hasFlag(this.privs, "linkFnElse");
		
		privSeeFnLibro = Commons.hasFlag(this.privs, "linkFnLibro");
		privSeeFnSession = Commons.hasFlag(this.privs, "linkFnSession");
		
		privViewFnPro = Commons.hasFlag(this.privs, "viewFnPro");
		privViewFnApp = Commons.hasFlag(this.privs, "viewFnApp");
		privViewFnElse = Commons.hasFlag(this.privs, "viewFnElse");		
		
		if ( !privViewFnPro ) privSeeFnPro = false;
		if ( !privViewFnApp ) privSeeFnApp = false;
		if ( !privViewFnElse ) privSeeFnElse = false;
		
		privOpFiles = Commons.hasFlag(this.privs, "opFiles");
		privOpDirs = Commons.hasFlag(this.privs, "opDirs");
		
		privDbConnect =  Commons.hasFlag(this.privs, "dbConnect");
		privDbDecrypt =  Commons.hasFlag(this.privs, "dbDecrypt");
		privDbRead =     Commons.hasFlag(this.privs, "dbRead");
		privDbWrite =    Commons.hasFlag(this.privs, "dbWrite");	
		privDbService =  Commons.hasFlag(this.privs, "dbService");
		privDump =       Commons.hasFlag(this.privs, "dump");	
		privDownFiles =  Commons.hasFlag(this.privs, "down");	
		privDownPro =    Commons.hasFlag(this.privs, "downPro");
		privDrop    =    Commons.hasFlag(this.privs, "drop");
		//System.out.println("Searching vor [down")
	}
	
	public boolean hasPrivTrace() {
		return privTrace;
	}
	
	public boolean hasPrivEeFooter() {
		return privEeFooter;
	}	

	
	public boolean hasPriv(String requestedPriv) {
		// TODO Auto-generated method stub
		return Commons.hasFlag(this.privs, requestedPriv);
	}

	private Integer maxTkTextLen = 300;
	private Integer maxTkTextblockLen = 2000;	
	private Integer maxFileMemory = 300000; // bytes
	
	public void setPrivParas(String privParas) {
		// TODO Auto-generated method stub
		this.privParas = privParas;
		this.privParasParas = new Parameters(privParas,"'");
		
		if ( privParasParas.getParameter("max_text_len") != null ) {
			this.maxTkTextLen = new Integer(privParasParas.getParameter("max_text_len"));
			System.out.println("OnlineSitzung did set max_text_len to "+this.maxTkTextLen);
		} // else this.maxTkTextLen = null;
		
		if ( privParasParas.getParameter("max_textblock_len") != null ) {
			maxTkTextblockLen = new Integer(privParasParas.getParameter("max_textblock_len"));
			System.out.println("OnlineSitzung did set max_textblock_len to "+this.maxTkTextblockLen);
		} // else		maxTkTextblockLen = null;
		if ( privParasParas.getParameter("max_file_memory") != null ) {
			maxFileMemory = new Integer(privParasParas.getParameter("max_file_memory"));
			System.out.println("OnlineSitzung did set max_file_memory to "+this.maxFileMemory);
		}
		
	}

	@Override
	public String getPrivPara(String requestedPrivPara) {
		// TODO Auto-generated method stub
		return privParasParas.getParameter(requestedPrivPara);
	}	
	
	public Integer getMaxTkTextLen() {
		return maxTkTextLen;
	}
	
	public Integer getMaxTkTextblockLen() {
		return maxTkTextblockLen;
	}	

	private LinkConverterRequest myLinkConverter = null;
	private void clearMyLinkConverter() {
		myLinkConverter = null;
	}
	public LinkConverterRequest getLinkConverterRequest() {
		if ( myLinkConverter == null ) {
			myLinkConverter= new LinkConverterRequest(
					privSeeFnApp,
					privSeeFnPro,
					privSeeFnElse,
					privSeeFnElse,
					privOpFiles,
					privOpDirs,
					privSeeFnElse,
					privViewFnPro, 
					privViewFnApp, 
					privViewFnElse,
					privSeeFnLibro,
					privSeeFnSession);			
		}


		//System.out.println("---- Surround.getLinkConverterRequest returns "+x.getDoku()+" for surrounding "+getDescription());
		return myLinkConverter;
	}

	@Override
	public boolean hasPrivOpFiles() {
		// TODO Auto-generated method stub
		return privOpFiles;
	}

	@Override
	public boolean hasPrivOpDirs() {
		// TODO Auto-generated method stub
		return privOpDirs;
	}

	@Override
	public boolean hasPrivAppFooter() {
		// TODO Auto-generated method stub
		//System.out.println("hasPrivAppFooter = "+privAppFooter);
		return privAppFooter;
	}
	
	public int getCachedLinkPagesMin() { // hund
		return 5;
	}
	public int getCachedLinkPagesMax() { // hund
		return 15;
	}

	
	@Override
	public boolean hasPrivDbConnect() {
		// TODO Auto-generated method stub
		return privDbConnect;
	}
	
	@Override
	public boolean hasPrivDbDecrypt() {
		// TODO Auto-generated method stub
		return privDbDecrypt;
	}	

	@Override
	public boolean hasPrivDbRead() {
		// TODO Auto-generated method stub
		return privDbRead;
	}

	@Override
	public boolean hasPrivDbWrite() {
		// TODO Auto-generated method stub
		return privDbWrite;
	}

	@Override
	public boolean hasPrivDbService() {
		// TODO Auto-generated method stub
		// return privDbService;
		return true;
	}

	@Override
	public boolean hasPrivDump() {
		// TODO Auto-generated method stub
		return privDump;
	}

	@Override
	public boolean hasPrivDownFiles() {
		// TODO Auto-generated method stub
		return privDownFiles;
	}

	@Override
	public boolean hasPrivDownPro() {
		// TODO Auto-generated method stub
		return privDownPro;
	}

	private int loginAttempts = 0;
	private long loginSuspendedUntil = -1;
	@Override
	public String nextLoginAttempt() {
		// TODO Auto-generated method stub
		loginAttempts++;
		//System.out.println("-------------------------- loginAttmpegs "+loginAttempts);
		if ( loginAttempts > 2 ) {
			
			loginSuspendedUntil = System.currentTimeMillis() + (2*60*1000);
			return "Maximal 3 Login-Attempts";
		} else
			return null;
		
	}

	@Override
	public void resetLoginAttempt() {
		// TODO Auto-generated method stub
		// System.out.println("-------------------------- RESET login "+loginAttempts);
		loginAttempts = 0;
		
	}

	@Override
	public String checkLoginSuspension() {
		if ( loginSuspendedUntil == -1)
			return null;
		if ( loginSuspendedUntil < System.currentTimeMillis()) {
			// System.out.println("loginSuspension Quitted");
			loginSuspendedUntil = -1;
			return null;
		}
		Sp.pGrantUserPoorLogin(""+loginSuspendedUntil+" Id="+getIdentity());
		
		// TODO Auto-generated method stub
		return "Login Suspended ... Make yourselfe a cup of tea ;-) ... when you finished your cup of tea, you could try again ...";
	}

	@Override
	public void setLoginSuspension(long _ms) {
		// TODO Auto-generated method stub
		loginSuspendedUntil = System.currentTimeMillis()+_ms;
		Sp.pAlarmSetLoginSuspension(getIdentity());
		
		
	}

	@Override
	public void setAppPrivs(String appPrivs) {
		this.appPrivs = appPrivs;
		
	}

	@Override
	public boolean hasAppPriv(String requestedApp) {
		// TODO Auto-generated method stub
		return Commons.hasFlag(this.appPrivs, requestedApp);
	}

	private String myUserName = null;
	public void performUserLogin( String _user, String _pwd, String _role, String _appid, Surrounding _surrounding)throws Exception {

		try {
			System.out.println("start OnlineSitzung.performUserLogin");
			eeSystem.performUserLogin(_user, _pwd, _role, _appid, _surrounding, this);
			myUserName = _user;
			
			if( this.privParasParas.getParameter("proSize") != null ) {
				long size = Long.parseLong(this.privParasParas.getParameter("proSize")) * 1000*1000;
				scPro = new StorageControl(projectDirC,size,this,StorageControl.TYPE_PROJECT,eeProject);
				scPro.calculateCurrentUsage();				
			}
			
			if( this.privParasParas.getParameter("sessionSize") != null ) {
				long size = Long.parseLong(this.privParasParas.getParameter("sessionSize")) * 1000*1000;
				scUser = new StorageControl(myUserHomeC,size,this,StorageControl.TYPE_SESSION,eeProject);				
				scUser.calculateCurrentUsage();				
			}		
			
			if( this.privParasParas.getParameter("memSize") != null ) {
				long size = Long.parseLong(this.privParasParas.getParameter("memSize")) ;
				scUserMem = new StorageControl(myUserHomeMemC,size,this,StorageControl.TYPE_MEMORY,eeProject);								
				scUserMem.calculateCurrentUsage();				
			}			
			// if ( this.hasPriv(requestedPriv))
			/*
			 * 		
		scUser = new StorageControl(myUserHomeC,sourrounding.getStorageLimit_Session(),this,StorageControl.TYPE_SESSION,eeProject);
		scUserMem = new StorageControl(myUserHomeMemC,sourrounding.getStorageLimit_Mem(),this,StorageControl.TYPE_MEMORY,eeProject);
		scPro = new StorageControl(projectDirC,sourrounding.getStorageLimit_Pro(),this,StorageControl.TYPE_PROJECT,eeProject);

			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception OnlineSitzung.performUserLogin");
			Sp.pWrongLogin("WRONG LOGIN",this);
			
		}
		
		
	
		
	}

	public String getUsername() {
		return myUserName;
		
	}

	String myIPExplain = null;
	public String getIP() {
		return myIPExplain;
		
	}
	public void setIP(String _explain, String ipForBlocktest){
		myIPExplain = _explain;
		Sp.pRegisterUserIp(this);
		if ( eeSystem.isIpBlocked(ipForBlocktest)) {
			Sp.pAlarmIpBlock(getIdentity());
			setLoginSuspension(100*60*1000);
		}
		
	}
	
	public String getIdentity() {
		String ret = "";
		if ( myUserName != null ) ret = ret+"user='"+myUserName+"'";
		if ( myIPExplain != null ) ret = ret+" ip='"+myIPExplain+"'";
		if ( ret.length() == 0 )
			ret="unknown";
		return ret;
	}
	
	@Override
	public boolean hasPrivAnt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrivDrop() {
		// TODO Auto-generated method stub
		return privDrop;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return this.isLoggedIn;
	}

	@Override
	public void setLoggedIn(boolean setLogin) {
		// TODO Auto-generated method stub
		this.isLoggedIn = setLogin;
		
	}

	@Override
	public boolean hasMasterProjectDir() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getMasterProjektDir() {
		// TODO Auto-generated method stub
		return this.projectDirC;
	}

	@Override
	public String getMasterProjektApp() {
		// TODO Auto-generated method stub
		return this.appId;
		
	}

	
	@Override
	public String getSystempostDir() throws Exception {

		return myPostDirC;
	}


	private Lang lang = null;

	@Override
	public boolean setLang(String langCode) {
		// System.out.println("----------- sprache sprache------ sprache sprache------ sprache sprache "+langCode);
		if ( lang == null ) {
			lang = Lang.getLang(langCode);
			return true;
		} else if ( lang.getLangCode().equals(langCode))
			return false;
		else {
			lang = Lang.getLang(langCode);
			return true;
		}
		
		
		
	}



	@Override
	public Lang lang() {
		// TODO Auto-generated method stub
		return lang;
	}





	@Override
	public boolean hasMasterAppDir() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getMasterAppDir() {
		// TODO Auto-generated method stub
		return this.appDirC;
	}

	private String rights = null;
	@Override
	public boolean hasRight(String right) {
		// TODO Auto-generated method stub
		// System.out.println("rights="+this.rights);
		if ( rights==null) return false;
		else return Commons.hasFlag(this.rights,right);

	}

	@Override
	public void setRights(String rights) {
		// TODO Auto-generated method stub
		this.rights = rights;
		System.out.println("OnlineSitzung.setRights ("+rights+") CALLED.");
		//System.out.println("@      @        @    Setting the right: "+this.rights);
		if ( appId.equals(starterAppName) || hasRight("agent") )
			this.hasRightAgent = true;
		else
			this.hasRightAgent = false;	
		
	}

	@Override
	public void doBackgroundLoginPdB(String _userPdB, String _pwdPdB) throws Exception {
		// TODO Auto-generated method stub
		eeProject.doBackgroundLoginPdB(_userPdB, _pwdPdB);
	}

	@Override
	public void checkMaxFileMemory(FileObject grant) throws FileSystemException, Exception {
		// TODO Auto-generated method stub
		if ( grant.getContent().getSize() > maxFileMemory )
			throw new Exception("File to big to be loaded into memory. File="+grant.getName().getURI()+
					" Size="+grant.getContent().getSize()+
					" bytes limit="+maxFileMemory+" bytes");
		
	}

	@Override
	public String getLongLivingTempDir() throws FileSystemException {
		// TODO Auto-generated method stub
		longLiving++;
		
		String base = Commons.removeFsInCase(eeSystem.getTomcatTempDirxx())
				+"/ll/ll_"+longLiving+"/";
		FileObject fo = CoreBuilder.fsm.resolveFile(base);
		fo.createFolder();
		// CommonsVfs.mkdirCascade(dirname, sitzung);
		return base;
	}
	
	private static int longLiving = 0;

	private String getUsernameOrFail () throws Exception {
		if (!isLoggedIn())
			throw new Exception(
					"Background-processes-management works only for loginned-users");
		
		String username = getUsername();
		
		if ( username==null || username.trim().equals(""))
			throw new Exception(
				"Background-processes-management works only for loginned-users");
		return username;
	}
	@Override
	public int registerProcess(StoppableThread thread, String typetitle, String details) throws Exception {
		return ( eeSystem.registerProcess(
				thread, typetitle, details, 
				getUsernameOrFail ()));		
	}

	@Override
	public void killProcess(int prozessId) throws Exception {
		eeSystem.killProcess(
				prozessId, 
				getUsernameOrFail (),
				hasRight("kill"));
		
	}
	
	@Override
	public void finishedProcess(int prozessId) throws Exception {
		eeSystem.finishedProcess(
				prozessId);
		
	}	



	@Override
	public Element getProcesses(boolean all) throws Exception {
		// TODO Auto-generated method stub
		return eeSystem.getProcesses(all,getUsernameOrFail ());
	}


	// gleich wie offline-sitzung

	private ConcurrentHashMap<String, CoopListOfApp> coopListOfApp = new ConcurrentHashMap<String, CoopListOfApp>();
	private ConcurrentHashMap <String, ICoop> masterappMap = new ConcurrentHashMap <String, ICoop>();
	private ConcurrentHashMap <String, ICoop> sessionMap = new ConcurrentHashMap <String, ICoop>();
	
	@Override
	public ConcurrentHashMap<String, ICoop> getCoopMap_k(String appid, boolean appIsMaster) {
		if ( appIsMaster ) {
			return masterappMap;			
		} else {
			CoopListOfApp clop = coopListOfApp.get(appid);
			if ( clop == null ) {
				// gibts noch nicht anlegen
				clop = new CoopListOfApp();
				coopListOfApp.put(appid, clop);
			}
			return clop.noMapKeep;
		}	
	}	
	@Override
	public synchronized ConcurrentHashMap<String, ICoop> getCoopMap(String appid,String coopId, boolean appIsMaster, boolean needsRightAgent) throws Exception {
		//System.out.println("of coopId:"+coopId);
		String derBuchstabe = coopId.substring(coopId.length()-3,coopId.length()-2);
		//System.out.println("der buchstabe=["+derBuchstabe+"] of coopId:"+coopId);
		int ct = CoopGlobalManager.loctypes.convert(derBuchstabe);
		ConcurrentHashMap<String, ICoop> r = null;
		switch ( ct ) {
	    case CoopGlobalManager.loctype_k: {	
	    		if ( appIsMaster) r = masterappMap;
	    		else {
					CoopListOfApp clop = coopListOfApp.get(appid);
					if ( clop == null ) {
						// gibts noch nicht anlegen
						clop = new CoopListOfApp();
						coopListOfApp.put(appid, clop);
					}
					r = clop.noMapKeep;
	    		}
				break;
			}
	    case CoopGlobalManager.loctype_s: {	
	    		r= sessionMap;
				break;
			}
	                         
	    default: {
	    	r= eeSystem.cgm.getCoopMap(appid, coopId,ct, needsRightAgent,this);
	    }
	    }
		
		return r;
	}

	// eeSystem.cgm.getCoopMap(appid, coopId);
	public void closeAllSessionCoops() {
		// TODO Auto-generated method stub
		Set<String> keySet = coopListOfApp.keySet();
		Iterator<String> i = keySet.iterator();
		while ( i.hasNext() ) {
			String aktKey   = i.next();

			CoopListOfApp toremove = coopListOfApp.get(aktKey);
			CoopListOfApp.closeAllCoops(toremove.noMapKeep, this);
			
		}	
		CoopListOfApp.closeAllCoops(masterappMap, this);
		CoopListOfApp.closeAllCoops(sessionMap, this);

	}

	private boolean hasRightAgent = false;
	public boolean hasRightAgent() {
		// TODO Auto-generated method stub
		return hasRightAgent;
	}



}
