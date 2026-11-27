package at.nicolas.x.int_specs;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.CoopGlobalManager;
import at.nicolas.x.int_interpreter.CoopListOfApp;
import at.nicolas.x.int_interpreter.ProzessInfo;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_setup.Rollen;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.pub.UserInfo;
import at.nicolas.x.term_ti.LinkConverterRequest;

public class OfflineSitzung implements ISitzung {
	
	
	private String sessionHomeFsDir = null;
	private String sessionHomeMemDir = null;
	private String sessionToken = null;
	private String systempostHome = null;

	private String wsDir = null;
	
	public OfflineSitzung ( String _wsFilename , String calledBy) throws URIException, FileSystemException, URISyntaxException {
		wsDir = Commons.getDirOfFile(_wsFilename);
		/*System.out.println("offline-sitzung: "+wsDir);
		 JOptionPane.showMessageDialog (null, 
				 "OfflineSitzung: _wsFilename="+_wsFilename+" by="+calledBy, "Information", JOptionPane.INFORMATION_MESSAGE);
		*/
		
	}
	public void setWsFilename( String _wsFilename ) throws URIException, FileSystemException, URISyntaxException {
		wsDir = Commons.getDirOfFile(_wsFilename);
	}
	
	@Override
	public String getSystempostDir() throws FileSystemException {
		// TODO Auto-generated method stub
		if ( systempostHome == null ) {
			systempostHome = wsDir+"systempost/";
		}
		return systempostHome;
	}	

	@Override
	public String getLibroDir2(String forAppId) {
		// TODO Auto-generated method stub
		return wsDir+"libro2/"+forAppId+"/";

	}
	
	public void OfflineSitzung() {
		respect();
		
		Sp.ph("Started Offline-Sitzung " +getDescriptor());
		
	}
	private String roleName="user";
	private int roleIndex=1;
	public void setRole( String _roleName ) throws Exception {
		roleName = _roleName;
		roleIndex = Rollen.getRoleHeightDefaultFail(roleName);
		
	}
	@Override
	public void setRoleIndex(int _roleIndex) {
		// TODO Auto-generated method stub
		roleIndex = _roleIndex;
		try {
			roleName = Rollen.getRoleByHeight(roleIndex);
		} catch (Exception e) {
			roleName = "public";
			roleIndex = 1;
		}		
	}	
	@Override
	public String getDescriptor() {
		// TODO Auto-generated method stub
		respect();
		return "Offline-Sitzung: sesssionToken=["+sessionToken+"] sessionHomeFsUri=["+
				sessionHomeFsDir+"] sessionHomeMemUri=["+sessionHomeMemDir+"] libroDir=["+getLibroDir("?")+"] libroDir2=["+getLibroDir2("?")+"]";
	}
	final String msg = new String("[ Grant ] - [ off ] : ");
	final String msgd = new String("[ Grant ] D [ off ] : ");

	private void respect() {
		
		if ( sessionToken == null ) {
			
			sessionToken = "desktop";
			String nics = wsDir;
			
			String someHome= CommonsVfs.makeyBeautifulUri("./");
			File someFile = new File("");
			
			// JOptionPane.showMessageDialog (null, 
			//		 "file="+someFile.getAbsolutePath() +"; someHome="+someHome, "Information", JOptionPane.INFORMATION_MESSAGE);			
			// sessionHomeFsUri = nics+Commons.getTsShortDate()+"/"+Commons.getTsShortTime()+"_"+sessionToken;
			sessionHomeFsDir = nics+sessionToken+"/";
			// JOptionPane.showMessageDialog (null, 
			//		 "sessionHomeFsDir="+sessionHomeFsDir, "Information", JOptionPane.INFORMATION_MESSAGE);

			//System.out.println("setting sessionHomeFsUri to "+sessionHomeFsDir);
			sessionHomeMemDir = "ram:///ramses/"+sessionToken+"/";
			//System.out.println("setting sessionHomeMemUri to "+sessionHomeMemDir);
			
			Sp.ph("Started Offline-Sitzung with respect " +getDescriptor());			
		}
		
	}

	public String getTomcatTempDir() {
		// TODO Auto-generated method stub
		return "[[tomcat-dir]]";
	}
	@Override
	public String getSessionHomeFs() {
		// TODO Auto-generated method stub
		respect();
		// System.out.println("giving back "+sessionHomeFsUri);
		return sessionHomeFsDir;
	}
	@Override
	public String getSessionHomeMem() {
		// TODO Auto-generated method stub
		respect();
		// System.out.println("giving back "+sessionHomeMemUri);
		return sessionHomeMemDir;
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
		return false;
	}
	@Override
	public String getDatabaseLinkUri() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRoleName() {
		// TODO Auto-generated method stub
		// return "test";
		//return roleName;
		return CoreBuilder.ws.getRole();
	}
	@Override
	public int getRoleIndex() {
		// TODO Auto-generated method stub
		// return roleIndex;
		return CoreBuilder.ws.getRoleIndex();
		/*
		String the = CoreBuilder.ws.getRole();
		System.out.println("received role "+the);
		getRoleHeightAndFail
		if ( the.equals("user")) return 1;
		else if ( the.equals("expert")) return 2;
		else if ( the.equals("admin")) return 3;
		else if ( the.equals("test")) return 4;
		else if ( the.equals("dev")) return 5;
		else if ( the.equals("hidden")) return 6;
		else return 6;
		*/
		
		
		//return CoreBuilder.ws.ro
		//		Commons
	}
	@Override
	public String getLibroDir(String forAppId) {
		// TODO Auto-generated method stub
		return wsDir+"libro/"+forAppId+"/";
	}
	@Override
	public String getRelativeLibroPath(String filename) throws Exception {
		// TODO Auto-generated method stub
		return filename;
	}
	@Override
	public void setGuiPrivs(String guiPrivs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasGuiPrivJobHeaderName() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasGuiPrivJobHeaderTip() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasGuiPrivJobFooter() {
		// TODO Auto-generated method stub
		return false;
	}


	
	@Override
	public void setLibroUpdatePrivileg(boolean mayupdate) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean getLibroUpdatePrivileg() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void setPrivs(String privs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasPriv(String requestedPriv) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void setPrivParas(String privParas) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getPrivPara(String requestedPrivPara) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer getMaxTkTextLen() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer getMaxTkTextblockLen() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasPrivTrace() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivEeFooter() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private LinkConverterRequest myLinkConverter = null;
	@Override
	public LinkConverterRequest getLinkConverterRequest() {
		if ( myLinkConverter == null ) {
			myLinkConverter= new LinkConverterRequest(
					true,
					true,
					true,
					true,
					true,
					true,
					true,
					true, 
					true, 
					true,
					true,
					true);			
		}


		//System.out.println("---- Surround.getLinkConverterRequest returns "+x.getDoku()+" for surrounding "+getDescription());
		return myLinkConverter;
	}
	@Override
	public boolean hasPrivOpFiles() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivOpDirs() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivAppFooter() {
		// TODO Auto-generated method stub
		return true;
	}

	public int getCachedLinkPagesMin() {
		return 3;
	}
	public int getCachedLinkPagesMax() {
		return 5;
	}
	@Override
	public boolean hasPrivDbConnect() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDbRead() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDbWrite() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDbService() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDump() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDownFiles() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDownPro() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDbDecrypt() {
		// TODO Auto-generated method stub
		return true;
	}
	private int loginAttempts = 0;
	private long loginSuspendedUntil = -1;
	@Override
	public String nextLoginAttempt() {
		// TODO Auto-generated method stub
		loginAttempts++;
		System.out.println("-------------------------- loginAttmpegs "+loginAttempts);
		if ( loginAttempts > 2 ) {
			
			loginSuspendedUntil = System.currentTimeMillis() + (2*60*100);
			return "Maximal 3 Login-Attempts";
		} else
			return null;
		
	}

	@Override
	public void resetLoginAttempt() {
		// TODO Auto-generated method stub
		System.out.println("-------------------------- RESET login "+loginAttempts);
		loginAttempts = 0;
		
	}

	@Override
	public String checkLoginSuspension() {
		if ( loginSuspendedUntil == -1)
			return null;
		if ( loginSuspendedUntil < System.currentTimeMillis()) {
			System.out.println("loginSuspension Quitted");
			loginSuspendedUntil = -1;
			return null;
		}
		System.out.println("loginSuspension continues until "+loginSuspendedUntil);
		// TODO Auto-generated method stub
		return "Login Suspended ... Make yourselfe a cup of tea ;-) ... when you finished your cup of tea, you could try again ...";
	}		
	
	@Override
	public void setLoginSuspension(long _ms) {
		// TODO Auto-generated method stub
		loginSuspendedUntil = System.currentTimeMillis()+_ms;
		
	}
	@Override
	public void setAppPrivs(String appPrivs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasAppPriv(String requestedApp) {
		// TODO Auto-generated method stub
		return true;
	}
		
	private boolean isLoggedIn = false;
	public void performUserLogin( String _user, String _pwd, String _role, String _appid,
			Surrounding _surrounding)throws Exception {
		String com = _user+"="+_pwd;
		String tr=null;
		String txPrivs=null;
		String txPrivParas=null;
		String txAppPrivs=null;
		/*
		 * privs
		 * 
		 *   libro ........ erlaubt ein Update auf Libro
		 *   trace ........ macht das tracing, das ist an einen eignen user gehaengt
		 *   dump ......... macht einen dump sichtbar
		 *   eeFooter ..... Macht den eeFooter grundsaetzlich sichtbar
		 *   appFooter .... macht den app-footer, das ist das job-inlcude-ende sichtbar,
		 *                  das ist auch von der web-app-config abhaengig
		 *  
		 *   
		 *	 viewFnPro .... macht filename im ti-file sichtbar
		 *	 viewFnApp
		 *	 viewFnElse
		 *
		 *	
		 *	 linkFnPro .... zeigt den link von einem file im ti-file an
		 *	 linkFnApp
		 *	 linkFnElse
		 *	
		 *
		 *   dbConnect
		 *   dbRead
		 *   dbWrite
		 *   dbService
		 *   dbDecrypt
		 *   
		 *   downnFiles
		 *   downPro	
		 *	
		 * Operations
		 *	 opFiles ............ all File-Operations
		 *   opDirs ............. all Dir Operations
		 *	 
		 *   opContent ... View Textfile, view pic, see checksums
		 *   opDelete .... Delete
		 *   opRename .... Rename
		 *   opCopy ...... Copy
		 *   opUpdate .... Update
		 *   opZip ....... Zipping
		 *   opUnzip ..... Unzipping
		 *   opInfo ...... Detail Infos of dir/file
		 *   opAnalyze ... stats, compare ....
		 *   
		 * priv paras
		 * 
		 *   max_text_len
		 *   max_textblock_len
		 * 
		 */


		final String privsOpsBasic  = " opFiles opDirs opContent opInfo ";
		final String privsOpsSuper  = " opFiles opDirs opContent opInfo opAnalyze ";
		final String privsOpsMaster = " opFiles opDirs opContent opInfo opAnalyze opDelete opRename opCopy opUpdate opZip opUnzip";
		final String privsFiles 	= " linkFnPro linkFnApp linkFnElse viewFnPro viewFnApp viewFnElse ";
		final String privsDb 		= " eeFooter dbConnect dbRead dbWrite dbService dbDecrypt downPro ";
		final String appPrivsMaster = "Formix-Rezeptblock Compare Panorama";
		
		final String cutPrivParas = "max_text_len='100' max_textblock_len='300'";
		tr="admin";
		txPrivs= privsFiles + privsDb + privsOpsMaster + " libro dump downFiles appFooter  " ;
		txPrivParas="";
		txAppPrivs=appPrivsMaster;	
		

		
		
			int trInt = Rollen.getRoleHeightDefaultFail(tr);		
			int neueRolleIndex =0;
			// rolle gegebenen fall runtersetzen
			if ( _role == null || _role.length() == 0 ) {
				// keine rolle eingegeben
				neueRolleIndex=trInt; // neue rolle ist die aus der pwddb
			} else {
				int eigegeben = Rollen.getRoleHeightDefaultFail(_role);
				if ( trInt < eigegeben )
					neueRolleIndex = trInt;
				else
					neueRolleIndex = eigegeben;
			}		
				
			boolean libroUpdate = Commons.hasFlag(txPrivs, "libro")	;
			
			setRoleIndex(neueRolleIndex);
			setGuiPrivs(_surrounding.getGuiprivsOfRole(neueRolleIndex));
			setLibroUpdatePrivileg(libroUpdate);
			
			setPrivs(txPrivs);
			setPrivParas(txPrivParas);
			if ( txAppPrivs != null )
				setAppPrivs(txAppPrivs);
			
			this.isLoggedIn = true;		
			resetLoginAttempt();
		
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setIP(String _explain, String ipForBlocktest) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getIdentity() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasPrivAnt() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean hasPrivDrop() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return isLoggedIn;
	}
	@Override
	public void setLoggedIn(boolean setLogin) {
		// TODO Auto-generated method stub
		isLoggedIn = setLogin;
	}
	@Override
	public boolean hasMasterProjectDir() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getMasterProjektDir() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getMasterProjektApp() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void grantsFileRead(FileObject grant) throws FileSystemException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void grantsFolderRead(FileObject grant) throws FileSystemException {
		// TODO Auto-generated method stub
		
	}

	private long getSizeDir( FileObject fo ) throws FileSystemException {
		return 0;
		//return CommonsVfs.getSizeOfDirSilent(fo);
	
		
	}
	
	private long getSizeFile( FileObject fo ) throws FileSystemException {
		return CommonsVfs.getSizeOfFileSilent(fo);
		
	}	
	private long opnumSequence = 0;
	@Override
	public long grantsUpdateFileP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {
		opnumSequence++;
		Sp.pSpaceSubFile(grant, getSizeFile(grant),opnumSequence,titel);
		
		return opnumSequence;		
	}



	@Override
	public void grantsUpdateFileP2(FileObject grant, long opnum ) throws StorageLimitException, FileSystemException {
		Sp.pSpaceAddFile(grant, getSizeFile(grant),opnum);	
		
	}

	@Override
	public long grantsUpdateFolderP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {
		opnumSequence++;
		Sp.pSpaceSubDir(grant, getSizeDir(grant),opnumSequence,titel);	
		
		return opnumSequence;		
	}

	@Override
	public void grantsUpdateFolderP2(FileObject grant, long opnum) throws StorageLimitException, FileSystemException {
		Sp.pSpaceAddDir(grant, getSizeDir(grant),opnum);	
	}



	@Override
	public void requestStorageStatus() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void grantsFileProcess(FileObject grant) throws FileSystemException {
		// TODO Auto-generated method stub
		
	}

private Lang lang = null;

	@Override
	public boolean setLang(String langCode) {
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
		return false;
	}



	@Override
	public String getMasterAppDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void grantsUpdateFolder(FileObject grant) throws StorageLimitException, FileSystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasRight(String right) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setRights(String rights) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doBackgroundLoginPdB(String _userPdB, String _pwdPdB) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkMaxFileMemory(FileObject grant) throws FileSystemException, Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getLongLivingTempDir() throws Exception {
		// TODO Auto-generated method stub
		String ll= wsDir+"ll/";
		CommonsVfs.mkdirCascade(ll, this);
		return ll;
	}
	
	Hashtable<Integer, ProzessInfo> prozesstable = new Hashtable<Integer, ProzessInfo>();
	private static int prozessIdCounter = 0;
	@Override
	public int registerProcess(StoppableThread thread, String typetitle, String details) throws Exception {
		
		ProzessInfo pi = new ProzessInfo();
		pi.setThread(thread);
		pi.setTypetitle(typetitle);
		pi.setDetails(details);
		pi.setDate(new Date());

		prozessIdCounter++;
		prozesstable.put(prozessIdCounter, pi);
		return prozessIdCounter;
	}
	@Override
	public void killProcess(int prozessId) throws Exception {
		// TODO Auto-generated method stub
		
		if ( !prozesstable.containsKey(prozessId))
			throw new Exception("Prozess with id '"+prozessId+"' does not exist");
		StoppableThread thread = (prozesstable.get(prozessId)).getThread();
		if ( thread == null )
			throw new Exception("this tread is null");
		// prozesstable.remove(prozessId);			
		//thread.stop();
		// thread.interrupt();
		thread.stopExecution();
		

			

			
		
	}
	@Override
	public Element getProcesses(boolean all) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Element result = new Element("procs");
		result.setAttribute("now", formatter.format(new Date()));
		
		Set<Integer> setOfKeys = prozesstable.keySet();
		for (Integer key : setOfKeys) {
			ProzessInfo pi = prozesstable.get(key);
			boolean take = all;

			if ( take) {
				Element neu = new Element("proc");
				neu.setAttribute("pid", ""+key);
				String state = pi.getState();
				if (state!=null )
					neu.setAttribute("state", state);				
				
				if (pi.getTypetitle()!=null )
					neu.setAttribute("type", pi.getTypetitle());
				if (pi.getDetails()!=null )
					neu.setAttribute("details", pi.getDetails());
				if (pi.getDate()!=null )
					neu.setAttribute("start", formatter.format(pi.getDate()));
				if (all && pi.getUsername()!=null)
					neu.setAttribute("user", pi.getUsername());
				
				try {
					neu.setAttribute("id", "" + pi.getThread().getId());
					neu.setAttribute("alive", Commons.boolToString(pi.getThread().isAlive()));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				/*
				neu.setAttribute("daemon", Commons.boolToString(pi.getThread().isDaemon()));
				neu.setAttribute("interrup", Commons.boolToString(pi.getThread().isAlive()));

				pi.getThread().isDaemon()
				pi.getThread().isInterrupted() */
				
				result.addContent(neu);
			}			
		}
		return result;
	}
	@Override
	public void finishedProcess(int prozessId) throws Exception {
		// TODO Auto-generated method stub
		if ( !prozesstable.containsKey(prozessId))
			throw new Exception("Prozess with id '"+prozessId+"' does not exist");

		prozesstable.remove(prozessId);			
	
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
	public ConcurrentHashMap<String, ICoop> getCoopMap(String appid,String coopId, boolean appIsMaster, boolean needsRightAgent) throws Exception {
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
	    	r= cgm.getCoopMap(appid, coopId,ct,  needsRightAgent,this);
	    }
	    }
		
		return r;
	}

	protected CoopGlobalManager cgm = new CoopGlobalManager();
	
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
	@Override
	public boolean hasRightAgent() {
		// TODO Auto-generated method stub
		return true;
	}



}
