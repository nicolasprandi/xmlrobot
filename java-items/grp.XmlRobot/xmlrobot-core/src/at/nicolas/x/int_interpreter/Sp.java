package at.nicolas.x.int_interpreter;

import org.apache.commons.vfs2.FileObject;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ISitzung;

public class Sp {

	private static boolean lowmod = false;
	public Sp() {
		// TODO Auto-generated constructor stub
	}
	public static void pd(String what) {
		
		System.out.println(gh("pd")+what);
		
	}
	public static void pe(Exception e) {
		//System.out.println(what);
		e.printStackTrace();
		
	}	
	
	public static void pAccessSystem(String what) {
		
		System.out.println(gh("Access-System")+what);
		
	}
	
	public static void pDecryptionProblem(String what) {

		System.out.println(gh("Decryption-Problem")+what);
		
	}		
	public static void sl(String what) {
	
		System.out.println(gh("low")+what);
		
	}	
	public static void p(String what) {

		if ( lowmod)
		System.out.println(gh("print easy")+what);
		
	}
	public static void ph(String what) {
		System.out.println(gh("high")+what);		
	}	
	
	public static void pSoftwareProblem(String what) {
		System.out.println(gh("Software-Problem")+what);
		
	}	
	
	public static void pShutDown(String what) {
		System.out.println(gh("shutdown")+what);		
	}	
	
	public static void pWrongLogin(String what, ISitzung _isitzung) {
		System.out.println(gh("VIOLA-LOGIN")+what+" BY:"+_isitzung.getIdentity());		
		
	}	
	
	public static void pProblem(String what, ISitzung _isitzung) {
		System.out.println(gh("PROBLEM")+what+" BY:"+_isitzung.getIdentity());		
		
	}		
	public static void pAccessSystemDelete(String what) { 
		System.out.println(gh("Access-System-Delete")+what);
		
	}	
	
	public static void pAccessUser(String what) {
		System.out.println(gh("Access-User")+what);
		
	}	
	public static void pGrantUserPrivs(String what) {
		System.out.println(gh("Grant-User-Privs")+what);
		
	}	
	
	public static void pGrantUserPoorLogin(String what) {
		System.out.println(gh("User-Poor-Login")+what);
		
	}	
	
	public static void pAccessLibroUpdate(String what) {
		System.out.println(gh("Access-Libro-Update")+what);
		
	}
	
	public static void pProjectShutdownProblemDir(ISitzung _iSitzung, String dirname, Exception ex) {
		System.out.println(gh("Project-Shutdown-Problem")+" of dir ["+dirname+"] of sitzung: "
	+_iSitzung.getIdentity()+" Exception: "+ex.getLocalizedMessage());
		
	}		
		
	
	public static void pAccessViolation(String what) {
		System.out.println(gh("VIOLA-Read")+what);
		
	}	
	
	public static void pAccessViolationProcess(String what) {
		System.out.println(gh("VIOLA-Process")+what);
		
	}	
	
	public static void pAccessViolationFolderRead(String what) {
		System.out.println(gh("VIOLA-FolderRead")+what);
		
	}		
	/*
	public static void pAccessViolationMaster(String what) {
		System.out.println(gh("VIOLA-Read-Master")+what);
		
	}
	*/	
	public static void pSpaceSubFile ( FileObject fo, long size, long opnum, String titel ) {
		if ( lowmod)
			System.out.println(gh("Space-Sub-File ")+"["+Commons.leadBlanks("-"+size,9)+" ]"+Commons.leadBlanks(""+opnum,10) + " = " + fo.getName().getBaseName()+" - " +"{"+titel+"} "+ fo.getName().getURI());
	}
	public static void pSpaceAddFile ( FileObject fo, long size, long opnum ) {
		if ( lowmod)
			System.out.println(gh("Space-Add-File ")+"["+Commons.leadBlanks(""+size,9)+" ]"+Commons.leadBlanks(""+opnum,10)+ " = " + fo.getName().getBaseName()+" - "+ fo.getName().getURI());
	}	
	
	public static void pSpaceSubDir ( FileObject fo, long size, long opnum, String titel ) {
		if ( lowmod)
			System.out.println(gh("Space-Sub-Dir ")+"["+Commons.leadBlanks("-"+size,9)+" ]"+Commons.leadBlanks(""+opnum,10)+ " = " + fo.getName().getBaseName()+" - "+"{"+titel+"} "+ fo.getName().getURI()+"/");
	}
	public static void pSpaceAddDir ( FileObject fo, long size, long opnum ) {
		if ( lowmod)
			System.out.println(gh("Space-Add-Dir ")+"["+Commons.leadBlanks(""+size,9)+" ]"+Commons.leadBlanks(""+opnum,10)+ " = " + fo.getName().getBaseName()+" - "+ fo.getName().getURI()+"/");
	}	
	public static void pAccessViolationDelete(String what) {
		System.out.println(gh("VIOLA-Update")+what);
		
	}	
	
	public static void pRejectedInputText(int attempted, int max) {
		System.out.println(gh("RejectedInputText")+" attempted="+attempted+" max="+max+" exeeded="+(attempted-max));
		
	}	
	
	public static void pRejectedInputTextBlock( int attempted, int max) {
		System.out.println(gh("RejectedInputText")+" attempted="+attempted+" max="+max+" exeeded="+(attempted-max));
		
	}	
	
	public static void pUserRepoLoginProblem(String what) {
		System.out.println(gh("UserRepoLoginProblem")+what);
		
	}	
	
	private static String gh ( String tit ) {
		// String lg = "                       ";
		String a = tit;
		if ( a.length() < 25 ) {
			a = a + "                         ".substring(a.length());
		}
		return Commons.getTsMs() + " | " + a +" | ";
		
	}
	/*
	public static void pAccessViolationMasterDelete(String what) {
		System.out.println(gh("VIOLA update MASTER")+what);
		
	}		*/
	public static void pAccessUserDelete(String what) {
		System.out.println(gh("Access-Update-User")+what);
	}	
	
	public static void pAccessSession(String what) {
		System.out.println(gh("Access-Session")+what);
		
	}	
	
	public static void pUserInfo(String what) {
		System.out.println(gh("User-Info")+what);
		
	}	
	
	public static void pRegisterUserIp(ISitzung _iSitzung) {
		System.out.println(gh("RegisterUserIp")+_iSitzung.getIdentity());
		
	}	
	public static void pShutDownProblem(String what) {
		System.out.println(gh("Shutdown-Problem")+what);
		
	}		
	
	public static void pAlarmx(String what) {
		System.out.println(gh("Alarm")+what);
		
	}
	public static void pAlarmSicherheitsstufe(String what) {
		System.out.println(gh("Alarm-Sicherheitstufe")+what);
		
	}	
	public static void pAlarmMaybeExcludeAttac(String what) {
		System.out.println(gh("Alarm-Maybe-Exclude-Attac")+what);
		
	}	
	public static void pAlarmSetLoginSuspension(String what) {
		System.out.println(gh("set-login-suspension")+what);
		
	}	
	
	public static void pAlarmIpBlock(String what) {
		System.out.println(gh("Alarm-IP-Block")+what);
		
	}	
	
	public static void pAlarmExclude(String what) {
		System.out.println(gh("Exclude-Alarm")+what);
		
	}	
	
	public static void pl(String what) {
		System.out.println(gh("Low")+what);
		
	}	

}
