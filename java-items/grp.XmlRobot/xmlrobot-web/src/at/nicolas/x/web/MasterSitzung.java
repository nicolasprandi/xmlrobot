package at.nicolas.x.web;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.pub.UserInfo;
import at.nicolas.x.term_ti.LinkConverterRequest;

public class MasterSitzung implements ISitzung {

	@Override
	public String getDescriptor() {
		// TODO Auto-generated method stub
		return "** MASTER-SITZUNG **";
	}	



	
	public String getTomcatTempDir() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSessionHomeFs() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSessionHomeMem() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}
	@Override
	public int getRoleIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setRoleIndex(int _roleIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getLibroDir(String forAppId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getRelativeLibroPath(String filename) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
		return false;
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
		return false;
	}
	@Override
	public void setPrivs(String privs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasPriv(String requestedPriv) {
		// TODO Auto-generated method stub
		return false;
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
		return false;
	}
	@Override
	public LinkConverterRequest getLinkConverterRequest() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasPrivOpFiles() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivOpDirs() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivAppFooter() {
		// TODO Auto-generated method stub
		return false;
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
		return false;
	}
	@Override
	public boolean hasPrivDbRead() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDbWrite() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDbService() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDump() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDownFiles() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDownPro() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDbDecrypt() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String nextLoginAttempt() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void resetLoginAttempt() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String checkLoginSuspension() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setLoginSuspension(long _ms) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setAppPrivs(String appPrivs) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasAppPriv(String requestedApp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performUserLogin(String _user, String _pwd, String _role, String _appid, 
			Surrounding _surrounding)
			throws Exception {
		// TODO Auto-generated method stub
		
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
		return "MasterSitzung";
	}
	
	@Override
	public boolean hasPrivAnt() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasPrivDrop() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setLoggedIn(boolean setLogin) {
		// TODO Auto-generated method stub
		
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
	public String getSystempostDir() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void grantsFileRead(FileObject grant) throws FileSystemException {
		Sp.pAccessSystem(grant.getName().getURI());
		
	}
	@Override
	public void grantsFolderRead(FileObject grant) throws FileSystemException {
		Sp.pAccessSystem(grant.getName().getURI());
		
	}
	@Override
	public long grantsUpdateFileP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {
		Sp.pAccessSystemDelete(grant.getName().getURI());
		return 0;
	}
	@Override
	public void grantsUpdateFileP2(FileObject grant, long opnum) throws StorageLimitException, FileSystemException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public long grantsUpdateFolderP1(FileObject grant, String titel) throws FileSystemException, StorageLimitException {
		Sp.pAccessSystemDelete(grant.getName().getURI()+"/");
		return 0;
	}
	@Override
	public void grantsUpdateFolderP2(FileObject grant, long opnum) throws StorageLimitException, FileSystemException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void grantsUpdateFolder(FileObject grant) throws StorageLimitException, FileSystemException {
		Sp.pAccessSystemDelete(grant.getName().getURI()+"/");
		
	}	




	@Override
	public void requestStorageStatus() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void grantsFileProcess(FileObject grant) throws FileSystemException {
		Sp.pAccessSystem(grant.getName().getURI());
		
	}




	@Override
	public boolean setLang(String langCode) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public Lang lang() {
		// TODO Auto-generated method stub
		return null;
	}









	@Override
	public void closeAllSessionCoops() {
		// TODO Auto-generated method stub
		
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
	public boolean hasRight(String right) {
		// TODO Auto-generated method stub
		return false;
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
	public String getLibroDir2(String forAppId) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void checkMaxFileMemory(FileObject grant) throws FileSystemException, Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public String getLongLivingTempDir() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public int registerProcess(StoppableThread thread, String typetitle, String details) throws Exception {
		// TODO Auto-generated method stub
		return 0;
		
	}




	@Override
	public void killProcess(int prozessId) throws Exception {
		// TODO Auto-generated method stub
		
	}




	@Override
	public Element getProcesses(boolean all) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void finishedProcess(int prozessId) throws Exception {
		// TODO Auto-generated method stub
		
	}








	@Override
	public ConcurrentHashMap<String, ICoop> getCoopMap_k(String appId, boolean appIsMaster) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public boolean hasRightAgent() {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public ConcurrentHashMap<String, ICoop> getCoopMap(String appId, String coopId, boolean appIsMaster,
			boolean needsRightAgent) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}























		

}
