package at.nicolas.x.pub;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.term_ti.LinkConverterRequest;

public interface ISitzung {
	public boolean hasRightAgent() ;
	public int registerProcess(StoppableThread thread, 
			String typetitle,
			String details) throws Exception;
	public void killProcess (int prozessId) throws Exception;
	public void finishedProcess (int prozessId) throws Exception;
	public Element getProcesses(boolean all) throws Exception;
	public String getLongLivingTempDir() throws Exception;
	public void setRights ( String rights );
	public boolean hasRight( String right );
	public ConcurrentHashMap<String, ICoop> getCoopMap(String appId, String coopId, boolean appIsMaster, boolean needsRightAgent) throws Exception ;
	public ConcurrentHashMap<String, ICoop> getCoopMap_k(String appId, boolean appIsMaster);
	public void closeAllSessionCoops();
	//public void removeAllNoKEEP(String appId);
	public boolean setLang( String langCode );
	public Lang lang();
	public void requestStorageStatus();
	public void grantsFileRead(FileObject grant) throws FileSystemException;
	public void grantsFileProcess(FileObject grant) throws FileSystemException;
	public void grantsFolderRead(FileObject grant) throws FileSystemException;
	
	//public void grantsDelete(FileObject grant) throws FileSystemException, StorageLimitException;
	// public void grantsWrite(FileObject grant, long newSize ) throws FileSystemException, StorageLimitException;
	public long grantsUpdateFileP1(FileObject grant, String titel) throws FileSystemException,StorageLimitException;
	public void grantsUpdateFileP2(FileObject grant, long opnum) throws StorageLimitException,FileSystemException;
	
	
	public long grantsUpdateFolderP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException;
	public void grantsUpdateFolderP2(FileObject grant, long opnum) throws StorageLimitException, FileSystemException;	
	public void grantsUpdateFolder(FileObject grant ) throws StorageLimitException, FileSystemException;
	
	public String getDescriptor();
	
	public String getSessionHomeFs();
	public String getSessionHomeMem();
	public String getNextIndexTempPro();
	public boolean isOnline();
	public boolean isLoggedIn();
	public void setLoggedIn(boolean setLogin);
	
	public boolean hasMasterProjectDir();
	public String getMasterProjektDir();
	public String getMasterProjektApp();
	public boolean hasMasterAppDir();
	public String getMasterAppDir();
	
	public String getSystempostDir() throws Exception;
	
	
	public void performUserLogin( 
			String _user, 
			String _pwd, 
			String _role, 
			String _appid, 
			Surrounding _surrounding)throws Exception;
	
	public void doBackgroundLoginPdB(
			String _userPdB,
			String _pwdPdB ) throws Exception;
	
	public String getDatabaseLinkUri();
	public UserInfo getUserInfo( );
	public String getRoleName();
	public int getRoleIndex();
	public void setRoleIndex ( int _roleIndex );
	public String getLibroDir(String forAppId);
	public String getRelativeLibroPath ( String filename) throws Exception;
	public String getLibroDir2(String forAppId);

	
	public void setGuiPrivs(String guiPrivs);
	public boolean hasGuiPrivJobHeaderName();
	public boolean hasGuiPrivJobHeaderTip();
	public boolean hasGuiPrivJobFooter();


	public void setLibroUpdatePrivileg(boolean mayupdate );
	public boolean getLibroUpdatePrivileg();
	
	public void setPrivs( String privs );
	public boolean hasPriv(String requestedPriv);
	
	public void setPrivParas ( String privParas );
	public String getPrivPara ( String requestedPrivPara);
	
	public void setAppPrivs( String appPrivs );
	public boolean hasAppPriv(String requestedApp);	
	
	public Integer getMaxTkTextLen();
	public Integer getMaxTkTextblockLen();
	public void checkMaxFileMemory(FileObject grant) throws FileSystemException, Exception;
	
	public boolean hasPrivTrace();	
	public boolean hasPrivEeFooter();
	public boolean hasPrivAppFooter();
	
	public boolean hasPrivDbConnect();
	public boolean hasPrivDbDecrypt();
	public boolean hasPrivDbRead();
	public boolean hasPrivDbWrite();
	public boolean hasPrivDbService();
	public boolean hasPrivDump();	
	public boolean hasPrivDownFiles();
	public boolean hasPrivDownPro();
	public boolean hasPrivAnt();
	public boolean hasPrivDrop();
	
	public LinkConverterRequest getLinkConverterRequest();
	public boolean hasPrivOpFiles();
	public boolean hasPrivOpDirs();
	
	public int getCachedLinkPagesMin();
	public int getCachedLinkPagesMax() ;
	
	public String nextLoginAttempt();
	public void resetLoginAttempt();
	public String checkLoginSuspension();
	public void setLoginSuspension(long _ms);
	
	public String getUsername();

	
	public String getIP();
	public void setIP(String _explain, String ipForBlocktest );	
	
	public String getIdentity();
	
	

	

	
}
