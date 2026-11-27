package at.nicolas.x.int_setup;

import java.util.HashMap;

public class Lang {
	private static HashMap<String, Lang> langMap = null;
	
	protected String uplHeader ="-UPLOAD**";
	protected String uplWhat ="Upload**: ";
	protected String uplFileSel ="Selected file on your computer";
	protected String uplOverwrite ="Overwrite if file exists";
	protected String uplSaveLibro ="Save file in Libro";
	protected String uplSavePDb ="Save file in Project-Db";
	protected String uplLibroFolder ="Target-Folder in Libro";
	protected String uplDoUpload ="Do the upload**";
	protected String uplDoCancel ="Cancel the Upload";
	
	
	public String getUplHeader() {
		return uplHeader;
	}
	public void setUplHeader(String uplHeader) {
		this.uplHeader = uplHeader;
	}
	public String getUplWhat() {
		return uplWhat;
	}
	public void setUplWhat(String uplWhat) {
		this.uplWhat = uplWhat;
	}
	public String getUplFileSel() {
		return uplFileSel;
	}
	public void setUplFileSel(String uplFileSel) {
		this.uplFileSel = uplFileSel;
	}
	public String getUplOverwrite() {
		return uplOverwrite;
	}
	public void setUplOverwrite(String uplOverwrite) {
		this.uplOverwrite = uplOverwrite;
	}
	public String getUplSaveLibro() {
		return uplSaveLibro;
	}
	public void setUplSaveLibro(String uplSaveLibro) {
		this.uplSaveLibro = uplSaveLibro;
	}
	public String getUplSavePDb() {
		return uplSavePDb;
	}
	public void setUplSavePDb(String uplSavePDb) {
		this.uplSavePDb = uplSavePDb;
	}
	public String getUplLibroFolder() {
		return uplLibroFolder;
	}
	public void setUplLibroFolder(String uplLibroFolder) {
		this.uplLibroFolder = uplLibroFolder;
	}
	public String getUplDoUpload() {
		return uplDoUpload;
	}
	public void setUplDoUpload(String uplDoUpload) {
		this.uplDoUpload = uplDoUpload;
	}
	
	public String getUplDoCancel() {
		return uplDoCancel;
	}
	public void setUplDoCancel(String uplDoCancel) {
		this.uplDoCancel = uplDoCancel;
	}	


	protected String uriPro = "ServiceProject";     //
	protected String uriProMaster = "Project";     //
	protected String uriProArt = "The";  	
	
	private String lang = "code";
	private String langLang = "Sprachenname in Sprache";
	private String langEn = "Sprachenbname auf Englisch";
	
	protected String yes = "Yes";
	protected String no = "No";
	protected String ignore = "Ignore";
	
	protected String label_warning = "W A R N I N G:";
	protected String label_error = "E R R O R:";
	protected String thereMustBeAValueFor = "There must be a value for";
	protected String rule_filename = "Filename Compability";
	protected String rule_filename_blanks = "Filename Compability with Blanks";
	protected String rule_reformating = "Reformating nessessary for";
	protected String rule_old = "Old value was:";
	protected String rule_new = "New value is:";
	protected String rule_accept = "Do you accept that ?";
	protected String rule_rule = "Rule:";
	
	protected String eepMenuSystem = "System";      //  '>System</a>
	protected String eepMenuDrop = "Drop";      //  t1'>Drop</a>
	protected String eepMenuSave = "Save";      //      kin'>Save</a>
	protected String eepMenuRead = "Read";      //    out'>Read</a>
	protected String eepMenuConnect = "Connect";      //     in'>Connect</a>
	protected String eepMenuDecrypt = "Decrypt";      //    rypt'>Decrypt</a>
	protected String eepMenuToolbox = "Toolbox";      //    rypt'>Decrypt</a>
	protected String eepErrorTkMood = "Tk is not in the right mood";      //    tk is not in the right mode
	protected String eepErrorTkNoTicket = "No Ticketmanager";      //    tk is not in the right mode	
	protected String eepErrorTkSolution = "Press the Project-Button to reset the application";
	protected String eepErrorTkOther = "Other Ticket-Problem:";
	
	private String zzzDASENDE = "das ende";

	public String getEepMenuSystem() {
		return eepMenuSystem;
	}
	public String getEepMenuDrop() {
		return eepMenuDrop;
	}
	public String getEepMenuSave() {
		return eepMenuSave;
	}
	public String getEepMenuRead() {
		return eepMenuRead;
	}
	public String getEepMenuConnect() {
		return eepMenuConnect;
	}
	public String getEepMenuDecrypt() {
		return eepMenuDecrypt;
	}
	public String getEepMenuToolbox() {
		return eepMenuToolbox;
	}	
	public String getEepErrorTkMood() {
		return eepErrorTkMood;
	}
	public String getEepErrorTkNoTicket() {
		return eepErrorTkNoTicket;
	}
	
	public String getEepErrorTkOther() {
		return eepErrorTkOther;
	}	
	public String getEepErrorTkSolution(String appid ) {
		return eepErrorTkSolution+ ":&nbsp;[&nbsp;"+appid+"&nbsp;]";
	}
	public String getRule_reformating() {
		return rule_reformating;
	}
	public String getRule_old() {
		return rule_old;
	}
	public String getRule_new() {
		return rule_new;
	}
	public String getRule_accept() {
		return rule_accept;
	}
	public String getRule_rule() {
		return rule_rule;
	}
	public String getRule_filename() {
		return rule_filename;
	}
	public String getRule_filename_blanks() {
		return rule_filename_blanks;
	}
	public String getThereMustBeAValueFor() {
		return thereMustBeAValueFor;
	}
	public String getLabel_warning() {
		return label_warning;
	}

	public String getLabel_error() {
		return label_error;
	}

	public String getYes() {
		return yes;
	}

	public String getNo() {
		return no;
	}
	
	public String getIgnore() {
		return ignore;
	}	

	protected void setLang( String lang) {
		this.lang = lang;
	}
	
	protected void setLangLang(String langLang) {
		this.langLang = langLang;
	}

	protected void setLangEn(String langEn) {
		this.langEn = langEn;
	}

	public String getLangLang() {
		return langLang;
	}

	public String getLangEn() {
		return langEn;
	}
	
	public String getLangCode() {
		return lang;
	}	
	public String getLangDescInternational(){
	
		if ( langLang.equals(langEn) )
			return langLang;
		else
			return langLang+ "/" + langEn;
	}	


	protected String eepLogoQuit = "Quit ";      //            lHeader("Quit ",t
	protected String eepLogoError = "ERROR";      //        out.println(getHtmlHeader("ERROR",false));
	protected String eepLogoError2 = "Error:";      //        out.println("<p>Error: "+			
	protected String eepLogoTiException = "TI-EXCEPTION";      //        ntln(getHtmlHeader("TI-EXCEPTION",false));
	protected String eepLogoTiException2 = "TP: ";      //       rintln("<p>TP: "+TkTicket.htmlByEscap
				

	protected String eepTkLogoEdit = "EDIT: ";      //   der("EDIT: "+    +++     ln(getHtmlHeader("EDIT: "+TkT      +++ ntln(getHtmlHeader("EDIT: "+TkTicket.    +++ tHtmlHeader("EDIT: "+TkTicket.htmlByEscap   +++  tln(getHtmlHeader("EDIT: "+ TkTicke
	protected String eepTkLogoConfirmChoose = "CONFIRM CHOOSE: ";      //     der("CONFIRM CHOOSE"
	protected String eepTkLogoReview = "REVIEW: ";      //   eader("REVIEW: "+Tk  +++   getHtmlHeader("REVIEW: "    +++ getHtmlHeader("REVIEW: "+TkT     +++ ln(getHtmlHeader("REVIEW: "+TkTicket.htmlBy   +++ ln(getHtmlHeader("REVIEW: "+TkTicket.html    +++ println(getHtmlHeader("REVIEW: "+TkTicket.htmlByE      +++ lHeader("REVIEW: "+TkTicke     +++ intln(getHtmlHeader("REVIEW: "+TkTicket.htmlByEsc
	protected String eepTkLogoReview2 = "REVIEW2: ";      //      HtmlHeader("REVIEW2: "+TkTicket.htmlByEscaped(t
	protected String eepTkLogoProblems = "PROBLEMS: ";      //      Header("PROBLEMS: "+TkT

	protected String eepTkLogoProblem = "PROBLEM: ";      //ntln(getHtmlHeader("PROBLEM: "+TkTicket.html   +++ mlHeader("PROBLEM: "+TkTicket.h



	protected String eepTkSubmitConfReview = "Confirm and Review";      //     submitLabel="Confirm and Review";
	protected String eepTkSubmitConfGo = "Confirm and Go";      //            submitLabel="Confirm and Go";
	protected String eepTkSubmitOk = "Ok";  // type=\"submit\" value=\""+"Ok"+"\" ");

	protected String eepTkActFullEdit = "Full Edit";      //     Button'>Full Edit</a><
	protected String eepTkActSequEdit = "Sequencial Edit";      //     wButton'>Sequencial Edit</a>
	protected String eepTkActCancel = "Cancel";      //       utton'>Cancel</a>
	protected String eepTkActStop = "Stop";      //   ttonStop'>Stop</a></div>");
	protected String eepTkActForget = "Forget it";      //    etailButtonCancel'>Forget it</a></ 
	protected String eepTkHeadReview = "Review";      //              ewHeader'>Review: "+TkTic
     // 

	protected String eepMsgQuitClickHere = "Click here to [ QuIT the SESSION ]";      //     out.println("<a class='Home' href='quit2'>Click here to [ QuIT the SESSION ]</a> ");
	protected String eepMsgQuitDid = "did quit project with success";      //    out.println("<p>did quit project with success</p>");
	protected String eepMsgQuitStart = "Start a new session here ...";      //    <a href='go'>Start a new session here ...</a></p>
	protected String eepMsgCryptNoProFound = "kein verschluesseltes project gefunden";      //      eption("kein verschluesseltes project gefunden");
	protected String eepMsgTiOutOfService = "this link is out of service";      //               ption("this link is out of service"), 

	protected String eepLoginNeedPwd = "We need a password here";      //       ption("We need a password here");
	protected String eepLoginBadUser = "you are a bad user and you dont know it";      //                   xception("you are a bad user and you dont know it");
	protected String eepLoginBadColl = "you are a bad coll and you dont know it";      //    eption("We need a user or collection and a password");
	protected String eepLoginMissing = "We need a user or collection and a password";      //    eption("We need a user or collection and a password");
	protected String eepLoginMsgCryptSave = "Enrypted Database works in a save way.";      //           ption("Enrypted Database works in a save way.");
	protected String eepLoginMsgCryptSecond = "we need the encryptor a second time";      //           xception("we need the encryptor a second time");
	protected String eepLoginMsgCryptSame = "the encryptors are not the same";      //          eption("the encryptors are not the same");
	protected String eepLogoLogin = "storage-login";      //       out.println(getHtmlHeader("storage-login",false));
	protected String eepLoginTxtHeadline = "Wahl des Speicherortes";      //         	out.println("<p>Wahl des Speicherortes</p>");
	protected String eepLoginTxtUser = "Username";      //    out.print("Username <input size=\"20\" type='text' name='n' /><br/>");
	protected String eepLoginTxtCollection = "Collection";      //    out.print("Collection <input size=\"20\" type='text' name='c' /><br/>");
	protected String eepLoginTxtStorage = "Storage-File";      //    out.print("Storage-File <input size=\"20\" type='text' name='d' /><br/>");
	protected String eepLoginTxtPasswords = "Project-Encryption";      //             out.print("<p>Passwords</p>");
	protected String eepLoginTxtPwd = "User/Coll Pwd";      //             out.print("User/Coll Pwd <input size=\"20\" type='password' name='ncp' /><br/>");	
	protected String eepLoginTxtEncryptor1 = "Key x 1";      //              out.print("Encryptor <input size=\"20\" type='password' name='e' /><br/>");
	protected String eepLoginTxtEncryptor2 = "Key x 2";      //              out.print("Encryptor <input size=\"20\" type='password' name='e2' /><br/>");
	protected String eepLoginTxtSubmit = "Ok";      //           value=\"Ok\"");	
	protected String eepLoginTxtWeiters = "Options";
	public String getEepLogoQuit() {
		return eepLogoQuit;
	}
	public String getEepLogoError() {
		return eepLogoError;
	}
	public String getEepLogoError2() {
		return eepLogoError2;
	}
	public String getEepLogoTiException() {
		return eepLogoTiException;
	}
	public String getEepLogoTiException2() {
		return eepLogoTiException2;
	}
	public String getEepTkLogoEdit() {
		return eepTkLogoEdit;
	}
	public String getEepTkLogoConfirmChoose() {
		return eepTkLogoConfirmChoose;
	}
	public String getEepTkLogoReview() {
		return eepTkLogoReview;
	}
	public String getEepTkLogoReview2() {
		return eepTkLogoReview2;
	}
	public String getEepTkLogoProblems() {
		return eepTkLogoProblems;
	}
	public String getEepTkLogoProblem() {
		return eepTkLogoProblem;
	}
	public String getEepTkSubmitConfReview() {
		return eepTkSubmitConfReview;
	}
	public String getEepTkSubmitConfGo() {
		return eepTkSubmitConfGo;
	}
	public String getEepTkSubmitOk() {
		return eepTkSubmitOk;
	}
	public String getEepTkActFullEdit() {
		return eepTkActFullEdit;
	}
	public String getEepTkActSequEdit() {
		return eepTkActSequEdit;
	}
	public String getEepTkActCancel() {
		return eepTkActCancel;
	}
	public String getEepTkActStop() {
		return eepTkActStop;
	}
	public String getEepTkActForget() {
		return eepTkActForget;
	}
	public String getEepTkHeadReview() {
		return eepTkHeadReview;
	}
	public String getEepMsgQuitClickHere() {
		return eepMsgQuitClickHere;
	}
	public String getEepMsgQuitDid() {
		return eepMsgQuitDid;
	}
	public String getEepMsgQuitStart() {
		return eepMsgQuitStart;
	}
	public String getEepMsgCryptNoProFound() {
		return eepMsgCryptNoProFound;
	}
	public String getEepMsgTiOutOfService() {
		return eepMsgTiOutOfService;
	}
	public String getEepLoginNeedPwd() {
		return eepLoginNeedPwd;
	}
	public String getEepLoginBadUser() {
		return eepLoginBadUser;
	}
	public String getEepLoginBadColl() {
		return eepLoginBadColl;
	}
	public String getEepLoginMissing() {
		return eepLoginMissing;
	}	
	public String getEepLoginMsgCryptSave() {
		return eepLoginMsgCryptSave;
	}
	public String getEepLoginMsgCryptSecond() {
		return eepLoginMsgCryptSecond;
	}
	public String getEepLoginMsgCryptSame() {
		return eepLoginMsgCryptSame;
	}
	public String getEepLogoLogin() {
		return eepLogoLogin;
	}
	public String getEepLoginTxtHeadline() {
		return eepLoginTxtHeadline;
	}
	public String getEepLoginTxtUser() {
		return eepLoginTxtUser;
	}
	public String getEepLoginTxtCollection() {
		return eepLoginTxtCollection;
	}
	public String getEepLoginTxtStorage() {
		return eepLoginTxtStorage;
	}
	public String getEepLoginTxtPasswords() {
		return eepLoginTxtPasswords;
	}
	public String getEepLoginTxtPwd() {
		return eepLoginTxtPwd;
	}
	public String getEepLoginTxtEncryptor1() {
		return eepLoginTxtEncryptor1;
	}
	public String getEepLoginTxtEncryptor2() {
		return eepLoginTxtEncryptor2;
	}
	public String getEepLoginTxtSubmit() {
		return eepLoginTxtSubmit;
	}
	
	public String getEepLoginTxtWeiters() {
		return eepLoginTxtWeiters;
	}	
	
	protected String eeAcceptDefaultLabel = "Do you accept this request?";      //     labelN = "Do you accept this request?";
	protected String eeAcceptDefaultOk = "Accept";      //  ok = "Accept";
	protected String eeAcceptDefaultDecline = "Decline";      //   cancel = "Decline";
	public String getEeAcceptDefaultLabel() {
		return eeAcceptDefaultLabel;
	}
	public String getEeAcceptDefaultOk() {
		return eeAcceptDefaultOk;
	}
	public String getEeAcceptDefaultDecline() {
		return eeAcceptDefaultDecline;
	}	
	
	protected String eeFfChoose = "Choose";      //    ticketId+"'>Choose</a></d
	protected String eeFfUpload = "Upload";      //    Id+"'>Upload</a></
	protected String eeFfPersonalFile = "Personal file";      //      rint("Personal file");
	protected String eeFfPersonalFolder = "Personal folder";      //     .print("Personal folder");
	protected String eeFfInYourProject = "In your Project:";      //      h, nextIndex,"In your Project:",isFile);
	protected String eeFfInYourApp = "Sample:";      //             h, nextIndex,"Sample:",isFile);
	public String getEeFfChoose() {
		return eeFfChoose;
	}
	public String getEeFfUpload() {
		return eeFfUpload;
	}
	public String getEeFfPersonalFile() {
		return eeFfPersonalFile;
	}
	public String getEeFfPersonalFolder() {
		return eeFfPersonalFolder;
	}
	public String getEeFfInYourProject() {
		return eeFfInYourProject;
	}
	public String getEeFfInYourApp() {
		return eeFfInYourApp;
	}	
	
	protected String eeTmQuestionsNORMAL = "Questions";      //        "label", "Questions");
	protected String eeTmQuestionNORMAL = "Question";      //              setAttribute("label", "Question");
	protected String eeTmInputDataNORMAL = "Input your data";      //             , "Input your data");	
	protected String eeTmEnterRegexKompatibelNORMAL = "Enter a value according to the regular expression: ";      // ex_msg = "Enter a value according to the regular expression: "+regex;
	public String getEeTmQuestionsNORMAL() {
		return eeTmQuestionsNORMAL;
	}
	public String getEeTmQuestionNORMAL() {
		return eeTmQuestionNORMAL;
	}
	public String getEeTmInputDataNORMAL() {
		return eeTmInputDataNORMAL;
	}
	public String getEeTmEnterRegexKompatibelNORMAL() {
		return eeTmEnterRegexKompatibelNORMAL;
	}	
	
	protected String scStorageLimit = "StorageLimit: ";     //
	protected String scFree = "Free:";     //
	protected String scCurrentUsage = "Consumtion:";     //
	protected String scLocation = "Location:";     //
	protected String scLimit = "Maximum:";     //
	public String getScLimit() {
		return scLimit;
	}	
	public String getScStorageLimit() {
		return scStorageLimit;
	}
	public String getScFree() {
		return scFree;
	}
	public String getScCurrentUsage() {
		return scCurrentUsage;
	}
	public String getScLocation() {
		return scLocation;
	}	
	
	protected String scTpSess = "Session";     //    if ( type == TYPE_SESSION ) return "Session";
	protected String scTpMemo = "Memory";     //    else if ( type == TYPE_MEMORY ) return "Memory";
	protected String scTpProj = "Project";     //    else if ( type == TYPE_PROJECT ) return "Project";
	protected String scTpDb = "Database";     //    else if ( type == TYPE_DB ) return "Database";
	protected String scTpUnknown = "unbekannter typ ";     //    else return ("unbekannter typ "+type);
	protected String scWarn1 = "Warning about StorageLimit of";     //
	protected String scWarn2 = "We reached";     //
	protected String scWarn3 = "% of that which is ";     //
	protected String scWarn4 = ". In total you have here";     //
	protected String scWarn5 = "The directory is in ";     //

	protected String scErr1 = "Limit of ";     //
	protected String scErr2 = " exceeded in ";     //
	protected String scErr3 = " of type ";     //
	public String getScTpSess() {
		return scTpSess;
	}
	public String getScTpMemo() {
		return scTpMemo;
	}
	public String getScTpProj() {
		return scTpProj;
	}
	public String getScTpDb() {
		return scTpDb;
	}
	public String getScTpUnknown() {
		return scTpUnknown;
	}
	public String getScWarn1() {
		return scWarn1;
	}
	public String getScWarn2() {
		return scWarn2;
	}
	public String getScWarn3() {
		return scWarn3;
	}
	public String getScWarn4() {
		return scWarn4;
	}
	public String getScWarn5() {
		return scWarn5;
	}
	public String getScErr1() {
		return scErr1;
	}
	public String getScErr2() {
		return scErr2;
	}
	public String getScErr3() {
		return scErr3;
	}	
	
	protected String uriApp = "AppAux";     //
	protected String uriAppMaster = "App";     //
	protected String uriAppArt = "The";     //
	protected String uriPdb = "ProjectDb";     //
	protected String uriPdbArt = "The";     //
	protected String uriSession = "Session";     //
	protected String uriSessionArt = "The";     //
	protected String uriLibro = "Libro";     //
	protected String uriLibroArt = "The";     //
	protected String uriMem = "Memory";     //
	protected String uriMemArt = "The";     //
	protected String uriSys = "System";     //
	protected String uriSysArt = "The";     //
	public String getUriApp() {
		return uriApp;
	}
	public String getUriAppMaster() {
		return uriAppMaster;
	}	
	public String getUriAppArt() {
		return uriAppArt;
	}
	public String getUriPdb() {
		return uriPdb;
	}
	public String getUriPdbArt() {
		return uriPdbArt;
	}
	public String getUriSession() {
		return uriSession;
	}
	public String getUriSessionArt() {
		return uriSessionArt;
	}
	public String getUriLibro() {
		return uriLibro;
	}
	public String getUriLibroArt() {
		return uriLibroArt;
	}
	public String getUriMem() {
		return uriMem;
	}
	public String getUriMemArt() {
		return uriMemArt;
	}
	public String getUriSys() {
		return uriSys;
	}
	public String getUriSysArt() {
		return uriSysArt;
	}	
	
 //
	public String getUriPro() {
		return uriPro;
	}
	public String getUriProMaster() {
		return uriProMaster;
	}	
	public String getUriProArt() {
		return uriProArt;
	}	
	
	protected String eeFfChooseDir = "Choose Dir";     
	protected String eeFfChooseFile = "Choose File";
	public String getEeFfChooseDir() {
		return eeFfChooseDir;
	}
	public String getEeFfChooseFile() {
		return eeFfChooseFile;
	}   	
	protected String filelocIn = "in";  
	public String getFilelocIn() {
		return filelocIn;
	}

	protected String locProject  = "Project";
	protected String locApp  = "App";
	protected String locLibro  = "Libro";
	protected String locSession  = "Session";
	protected String locSystem  = "System";
	protected String locWorkspace  = "Workspace";
	protected String locBackup  = "Backup";
	protected String locStatus  = "Status";
	protected String locUserDocument  = "UserDocument";
	protected String locUserHome  = "UserHome";
	
	public String getLocProject() {
		return locProject;
	}
	public String getLocApp() {
		return locApp;
	}
	public String getLocLibro() {
		return locLibro;
	}
	public String getLocSession() {
		return locSession;
	}
	public String getLocSystem() {
		return locSystem;
	}
	public String getLocWorkspace() {
		return locWorkspace;
	}
	public String getLocBackup() {
		return locBackup;
	}
	public String getLocStatus() {
		return locStatus;
	}
	public String getLocUserDocument() {
		return locUserDocument;
	}
	public String getLocUserHome() {
		return locUserHome;
	}

		
	private static Lang buildLang(String langCode,CommStartMsg sm) {
		Lang l = new Lang();
		l.setLang(langCode);
		l.filelocIn = "&#8834;";
		if ( langCode.equals("de")) {

			l.setLangLang("Deutsch");
			l.setLangEn("German");
			l.yes ="Ja";
			l.no = "Nein";
			l.ignore="Ignorieren";
			l.label_warning = "W A R N U N G:";
			l.label_error = "F E H L E R:";
			l.thereMustBeAValueFor = "Es wird ein Wert verlangt fuer";
			l.rule_filename = "Filenamen-Kompatibilitaet";
			l.rule_filename_blanks = "Filenamen-Kompatibilitaet mit Leerzeichen";			
			l.rule_reformating = "Reformatierung notwendig fuer";
			l.rule_old = "Alter Wert war:";
			l.rule_new = "Neuer Wert ist:";
			l.rule_accept = "Akzeptieren Sie das?";
			l.rule_rule = "Regel:";		
			
			l.eepMenuSystem = "PdB"; 
			l.eepMenuDrop = "Beenden";   
			l.eepMenuSave = "Speichern";    
			l.eepMenuRead = "Lesen";   
			l.eepMenuConnect = "PdB-Login";    
			l.eepMenuDecrypt = "Decrypt";    
			l.eepMenuToolbox = "Werkzeug";    
			l.eepErrorTkMood = "Es gibt ein Problem mit dem aktuellen Zustand bei den Eingaben: ";  
			l.eepErrorTkNoTicket = "Es gibt ein Problem mit dem aktuellen Zustand bei den Eingaben. Gegenwaertig werden keine Eingaben gebraucht: ";  			
			l.eepErrorTkOther = "Da ist ein Problem mit dem aktuellen Zustand bei den Eingaben: ";
			l.eepErrorTkSolution = "<br>Betaetigen Sie den Projekt-Button um die Applikation auf Null zu stellen";			

			l.eepLogoQuit = "Ende ";      //            lHeader("Quit ",t
			l.eepLogoError = "Fehler";      //        out.println(getHtmlHeader("ERROR",false));
			l.eepLogoError2 = "Fehler:";      //        out.println("<p>Error: "+			
			l.eepLogoTiException = "Problemsituation";      //        ntln(getHtmlHeader("TI-EXCEPTION",false));
			l.eepLogoTiException2 = "Historie: ";      //       rintln("<p>TP: "+TkTicket.htmlByEscap
						

			l.eepTkLogoEdit = "Bearbeiten: ";      //   der("EDIT: "+    +++     ln(getHtmlHeader("EDIT: "+TkT      +++ ntln(getHtmlHeader("EDIT: "+TkTicket.    +++ tHtmlHeader("EDIT: "+TkTicket.htmlByEscap   +++  tln(getHtmlHeader("EDIT: "+ TkTicke
			l.eepTkLogoConfirmChoose = "Auswahl bestaetigen: ";      //     der("CONFIRM CHOOSE"
			l.eepTkLogoReview = "Zusammenfassung: ";      //   eader("REVIEW: "+Tk  +++   getHtmlHeader("REVIEW: "    +++ getHtmlHeader("REVIEW: "+TkT     +++ ln(getHtmlHeader("REVIEW: "+TkTicket.htmlBy   +++ ln(getHtmlHeader("REVIEW: "+TkTicket.html    +++ println(getHtmlHeader("REVIEW: "+TkTicket.htmlByE      +++ lHeader("REVIEW: "+TkTicke     +++ intln(getHtmlHeader("REVIEW: "+TkTicket.htmlByEsc
			l.eepTkLogoReview2 = "Zusammenfassung: ";      //      HtmlHeader("REVIEW2: "+TkTicket.htmlByEscaped(t
			l.eepTkLogoProblems = "Probleme: ";      //      Header("PROBLEMS: "+TkT

			l.eepTkLogoProblem = "Problem: ";      //ntln(getHtmlHeader("PROBLEM: "+TkTicket.html   +++ mlHeader("PROBLEM: "+TkTicket.h



			l.eepTkSubmitConfReview = "Bestaetigen und Kontrollieren";      //     submitLabel="Confirm and Review";
			l.eepTkSubmitConfGo = "Ok, Fertig"; //"Best&auml;tigen und Fertig";      //            submitLabel="Confirm and Go";
			l.eepTkSubmitOk = "Ok";  // type=\"submit\" value=\""+"Ok"+"\" ");

			l.eepTkActFullEdit = "Alles Bearbeiten";      //     Button'>Full Edit</a><
			l.eepTkActSequEdit = "Schrittweise Bearbeiten";      //     wButton'>Sequencial Edit</a>
			l.eepTkActCancel = "Abbruch";      //       utton'>Cancel</a>
			l.eepTkActStop = "Stop";      //   ttonStop'>Stop</a></div>");
			l.eepTkActForget = "Zurueck";      //    etailButtonCancel'>Forget it</a></ 
			l.eepTkHeadReview = "Zusammenfassung";      //              ewHeader'>Review: "+TkTic
		     // 

			l.eepMsgQuitClickHere = "Klicken Sie hier um [ die Sitzung zu beenden ]";      //     out.println("<a class='Home' href='quit2'>Click here to [ QuIT the SESSION ]</a> ");
			l.eepMsgQuitDid = "Die Sitzung wurde erfolgreich beendet";      //    out.println("<p>did quit project with success</p>");
			l.eepMsgQuitStart = "Beginnen Sie eine neue Sitzung hier ...";      //    <a href='go'>Start a new session here ...</a></p>
			l.eepMsgCryptNoProFound = "Kein verschluesseltes Projekt gefunden";      //      eption("kein verschluesseltes project gefunden");
			l.eepMsgTiOutOfService = "Dieser Link ist veraltet";      //               ption("this link is out of service"), 

			l.eepLoginNeedPwd = "Passwort-Eingabe erforderlich";      //       ption("We need a password here");
			l.eepLoginBadUser = "Es sind Unstimmigkeiten bei den Zugansdaten des Benutzers aufgetreten";      //                   xception("you are a bad user and you dont know it");
			l.eepLoginBadColl = "Es sind Unstimmigkeiten bei den Zugansdaten der Sammlung aufgetreten";      //    eption("We need a user or collection and a password");
			l.eepLoginMissing = "Es sind folgenden Eingaben ist erforderlich: Benutzer oder Sammlung UND Passwort";      //    eption("We need a user or collection and a password");
			l.eepLoginMsgCryptSave = "Verschluesselte Datenbank arbeitet nur im sicheren Modus.";      //           ption("Enrypted Database works in a save way.");
			l.eepLoginMsgCryptSecond = "Die Eingabe des Verschluesselungs-Kennwortes ist ein zweites mal erforderlich";      //           xception("we need the encryptor a second time");
			l.eepLoginMsgCryptSame = "Die Verschluesselungskennwoerter sind nicht gleich";      //          eption("the encryptors are not the same");
			l.eepLogoLogin = "Login zur Projekt-Datenbank";      //       out.println(getHtmlHeader("storage-login",false));
			l.eepLoginTxtHeadline = "Zugangsdaten zur Projekt-Datenbank";      //         	out.println("<p>Wahl des Speicherortes</p>");
			l.eepLoginTxtUser = "Benutzername";      //    out.print("Username <input size=\"20\" type='text' name='n' /><br/>");
			l.eepLoginTxtCollection = "Benutzer-Gruppe";      //    out.print("Collection <input size=\"20\" type='text' name='c' /><br/>");
			l.eepLoginTxtStorage = "Db-Subset";      //    out.print("Storage-File <input size=\"20\" type='text' name='d' /><br/>");
			l.eepLoginTxtPasswords = "Projekt-Verschluesselung";      //             out.print("<p>Passwords</p>");
			l.eepLoginTxtPwd = "Passwort";      //    f&uuml;r Benutzer oder Sammlung          out.print("User/Coll Pwd <input size=\"20\" type='password' name='ncp' /><br/>");	
			l.eepLoginTxtEncryptor1 = "Kennwort x 1";      //              out.print("Encryptor <input size=\"20\" type='password' name='e' /><br/>");
			l.eepLoginTxtEncryptor2 = "Kennwort x 2";      //              out.print("Encryptor <input size=\"20\" type='password' name='e2' /><br/>");
			l.eepLoginTxtSubmit = "Ok";      //           value=\"Ok\"");	
			l.eepLoginTxtWeiters = "Optionen";
			
			l.eeAcceptDefaultLabel = "Akzeptieren Sie dies Aufforderung?";      //     labelN = "Do you accept this request?";
			l.eeAcceptDefaultOk = "Akzeptieren";      //  ok = "Accept";
			l.eeAcceptDefaultDecline = "Ablehen";      //   cancel = "Decline";		
			
			l.eeFfChoose = "Auswaehlen";      //    ticketId+"'>Choose</a></d
			l.eeFfUpload = "Upload";      //    Id+"'>Upload</a></
			l.eeFfPersonalFile = "Persoenliche Datei";      //      rint("Personal file");
			l.eeFfPersonalFolder = "Persoenliches Verzeichnis";      //
			l.eeFfInYourProject = "In Ihrem Projekt:";      //      h, nextIndex,"In your Project:",isFile);
			l.eeFfInYourApp = "Vorlage:";      //             h, nextIndex,"Sample:",isFile);	
			
			l.eeTmQuestionsNORMAL = "Fragen";      //        "label", "Questions");
			l.eeTmQuestionNORMAL = "Frage";      //              setAttribute("label", "Question");
			l.eeTmInputDataNORMAL = "Dateneingabe";      //             , "Input your data");	
			l.eeTmEnterRegexKompatibelNORMAL = "Geben Sie einen Wert entsprechen des folgenden Regulaeren Ausdrucks ein: "; 				

			l.scStorageLimit = "Speicher-Status von: ";     //
			l.scFree = "Frei:";     //
			l.scCurrentUsage = "Verbraucht:";     //
			l.scLocation = "Ort:";   		
			l.scLimit = "Maximum:"; 
			
			l.scTpSess = "Sitzung";     //    if ( type == TYPE_SESSION ) return "Session";
			l.scTpMemo = "Memory";     //    else if ( type == TYPE_MEMORY ) return "Memory";
			l.scTpProj = "Projekt";     //    else if ( type == TYPE_PROJECT ) return "Project";
			l.scTpDb = "Projekt-Datenbank";     //    else if ( type == TYPE_DB ) return "Database";
			l.scTpUnknown = "unbekannter Typ ";     //    else return ("unbekannter typ "+type);
			l.scWarn1 = "Warnung bezueglich Speicherplatzgrenze von";     //
			l.scWarn2 = "Es wurden ";     //
			l.scWarn3 = "% erreich, das sind ";     //
			l.scWarn4 = ". In Summe stehen Ihnen hier zur Verfuegung";     //
			l.scWarn5 = "Das Verzeichnis liegt in";     //

			l.scErr1 = "Speicherplatzgrenze von ";     //
			l.scErr2 = " Bytes Ueberschritten in ";     //
			l.scErr3 = " vom Typ ";   	
			
			l.uriApp = "ServiceApp";     //
			l.uriAppMaster = "App";     //
			l.uriAppArt = "Die";     //
			l.uriPdb = "ProjektDb";     //
			l.uriPdbArt = "Die";     //
			l.uriSession = "Sitzung";     //
			l.uriSessionArt = "Die";     //
			l.uriLibro = "Libro";     //
			l.uriLibroArt = "Das";     //
			l.uriMem = "Hauptspeicher";     //
			l.uriMemArt = "Der";     //
			l.uriSys = "System";     //
			l.uriSysArt = "Das";     //	
			l.uriPro = "ServiceProjekt";     //
			l.uriProMaster = "Projekt";     //
			l.uriProArt = "Das";		
			l.eeFfChooseDir = "Verzeichnis auswaehlen";     
			l.eeFfChooseFile = "Datei auswaehlen";		
			
			l.locProject  = "Projekt";
			l.locApp  = "App";
			l.locLibro  = "Libro";
			l.locSession  = "Sitzung";
			l.locSystem  = "System";
			l.locWorkspace  = "Workspace";
			l.locBackup  = "Backup";
			l.locStatus  = "Status";
			l.locUserDocument  = "UserDokument";
			l.locUserHome  = "UserHome";
			l.uplHeader ="-UPLOAD";
			l.uplWhat ="Upload: ";
			l.uplFileSel ="Diese Datei raufladen";
			l.uplOverwrite ="Ueberschreiben, wenn Datei bereits existiert";
			l.uplSaveLibro ="Upload in Libro speichern";
			l.uplSavePDb ="Upload in Projekt-Db speichern";
			l.uplLibroFolder ="Ziel-Verzeichnis in Libro";
			l.uplDoUpload ="Upload starten";
			l.uplDoCancel ="Upload abbrechen";
			
		} else {
			l.setLangLang("English");
			l.setLangEn("English");		
			l.yes ="Yes";
			l.no = "No";
			l.ignore="Ignore";
			l.label_warning = "W A R N I N G:";
			l.label_error = "E R R O R:";	
			l.thereMustBeAValueFor = "There must be a value for";
			l.rule_filename = "Filename Compability";
			l.rule_filename_blanks = "Filename Compability with Blanks";
			l.rule_reformating = "Reformating nessessary for";
			l.rule_old = "Old value was:";
			l.rule_new = "New value is:";
			l.rule_accept = "Do you accept that ?";
			l.rule_rule = "Rule:";			
			
			l.eepMenuSystem = "PdB"; 
			l.eepMenuDrop = "Quit";   
			l.eepMenuSave = "Save";    
			l.eepMenuRead = "Read";   
			l.eepMenuConnect = "PdB-Login";    
			l.eepMenuDecrypt = "Decrypt";    
			l.eepMenuToolbox = "Toolbox";    
			l.eepErrorTkMood = "There is a problem with the current state of inputs: ";  
			l.eepErrorTkNoTicket = "There is a problem with the current state of inputs. No user-inputs are requested right now: ";  			
			l.eepErrorTkOther = "Whe have a problem with the current state of inputs:  ";
			l.eepErrorTkSolution = "<br>Press the Project-Button to reset the application";
			
			l.eepLogoQuit = "Quit ";      //            lHeader("Quit ",t
			l.eepLogoError = "Error";      //        out.println(getHtmlHeader("ERROR",false));
			l.eepLogoError2 = "Error:";      //        out.println("<p>Error: "+			
			l.eepLogoTiException = "Situation of Problem";      //        ntln(getHtmlHeader("TI-EXCEPTION",false));
			l.eepLogoTiException2 = "History: ";      //       rintln("<p>TP: "+TkTicket.htmlByEscap
						

			l.eepTkLogoEdit = "Edit: ";      //   der("EDIT: "+    +++     ln(getHtmlHeader("EDIT: "+TkT      +++ ntln(getHtmlHeader("EDIT: "+TkTicket.    +++ tHtmlHeader("EDIT: "+TkTicket.htmlByEscap   +++  tln(getHtmlHeader("EDIT: "+ TkTicke
			l.eepTkLogoConfirmChoose = "Confirm the choose: ";      //     der("CONFIRM CHOOSE"
			l.eepTkLogoReview = "Review: ";      //   eader("REVIEW: "+Tk  +++   getHtmlHeader("REVIEW: "    +++ getHtmlHeader("REVIEW: "+TkT     +++ ln(getHtmlHeader("REVIEW: "+TkTicket.htmlBy   +++ ln(getHtmlHeader("REVIEW: "+TkTicket.html    +++ println(getHtmlHeader("REVIEW: "+TkTicket.htmlByE      +++ lHeader("REVIEW: "+TkTicke     +++ intln(getHtmlHeader("REVIEW: "+TkTicket.htmlByEsc
			l.eepTkLogoReview2 = "Review: ";      //      HtmlHeader("REVIEW2: "+TkTicket.htmlByEscaped(t
			l.eepTkLogoProblems = "Problems: ";      //      Header("PROBLEMS: "+TkT

			l.eepTkLogoProblem = "Problem: ";      //ntln(getHtmlHeader("PROBLEM: "+TkTicket.html   +++ mlHeader("PROBLEM: "+TkTicket.h



			l.eepTkSubmitConfReview = "Confirm &amp; Review";      //     submitLabel="Confirm and Review";
			l.eepTkSubmitConfGo = "Ok, Ready";      //            submitLabel="Confirm and Go";
			l.eepTkSubmitOk = "Ok";  // type=\"submit\" value=\""+"Ok"+"\" ");

			l.eepTkActFullEdit = "Full Edit";      //     Button'>Full Edit</a><
			l.eepTkActSequEdit = "Step-by-Step Edit";      //     wButton'>Sequencial Edit</a>
			l.eepTkActCancel = "Cancel";      //       utton'>Cancel</a>
			l.eepTkActStop = "Stop";      //   ttonStop'>Stop</a></div>");
			l.eepTkActForget = "Go Back";      //    etailButtonCancel'>Forget it</a></ 
			l.eepTkHeadReview = "Review";      //              ewHeader'>Review: "+TkTic
		     // 

			l.eepMsgQuitClickHere = "Click here to [ Quit the SESSION ]";      //     out.println("<a class='Home' href='quit2'>Click here to [ QuIT the SESSION ]</a> ");
			l.eepMsgQuitDid = "Did quit project with success";      //    out.println("<p>did quit project with success</p>");
			l.eepMsgQuitStart = "Start a new session here ...";      //    <a href='go'>Start a new session here ...</a></p>
			l.eepMsgCryptNoProFound = "No encrypted Project found";      //      eption("kein verschluesseltes project gefunden");
			l.eepMsgTiOutOfService = "This link is out of service";      //               ption("this link is out of service"), 

			l.eepLoginNeedPwd = "We need a password here";      //       ption("We need a password here");
			l.eepLoginBadUser = "A problem occured with the user-login";      //                   xception("you are a bad user and you dont know it");
			l.eepLoginBadColl = "A problem occured with the collection-login";      //    eption("We need a user or collection and a password");
			l.eepLoginMissing = "We need a user or collection AND a password";      //    eption("We need a user or collection and a password");
			l.eepLoginMsgCryptSave = "Enrypted Database works in a save way.";      //           ption("Enrypted Database works in a save way.");
			l.eepLoginMsgCryptSecond = "We need the encryptor a second time";      //           xception("we need the encryptor a second time");
			l.eepLoginMsgCryptSame = "The encryptors are not the same";      //          eption("the encryptors are not the same");
			l.eepLogoLogin = "Login to Project-Database";      //       out.println(getHtmlHeader("storage-login",false));
			l.eepLoginTxtHeadline = "Credentials of Project-Database";      //         	out.println("<p>Wahl des Speicherortes</p>");
			l.eepLoginTxtUser = "Username";      //    out.print("Username <input size=\"20\" type='text' name='n' /><br/>");
			l.eepLoginTxtCollection = "Group-Account";      //    out.print("Collection <input size=\"20\" type='text' name='c' /><br/>");
			l.eepLoginTxtStorage = "Storage-Area";      //    out.print("Storage-File <input size=\"20\" type='text' name='d' /><br/>");
			l.eepLoginTxtPasswords = "Password";      //             out.print("<p>Passwords</p>");
			l.eepLoginTxtPwd = "Password";      //    (User or Collection)          out.print("User/Coll Pwd <input size=\"20\" type='password' name='ncp' /><br/>");	
			l.eepLoginTxtEncryptor1 = "Key x 1";      //              out.print("Encryptor <input size=\"20\" type='password' name='e' /><br/>");
			l.eepLoginTxtEncryptor2 = "Key x 2";      //              out.print("Encryptor <input size=\"20\" type='password' name='e2' /><br/>");
			l.eepLoginTxtSubmit = "Ok";      //           value=\"Ok\"");	
			l.eepLoginTxtWeiters = "Options"; 
			
			l.eeAcceptDefaultLabel = "Do you accept this request?";      //     labelN = "Do you accept this request?";
			l.eeAcceptDefaultOk = "Accept";      //  ok = "Accept";
			l.eeAcceptDefaultDecline = "Decline";      //   cancel = "Decline";		
			
			l.eeFfChoose = "Choose";      //    ticketId+"'>Choose</a></d
			l.eeFfUpload = "Upload";      //    Id+"'>Upload</a></
			l.eeFfPersonalFile = "Personal file";      //      rint("Personal file");
			l.eeFfPersonalFolder = "Personal folder";      //     .print("Personal folder");
			l.eeFfInYourProject = "In your Project:";      //      h, nextIndex,"In your Project:",isFile);
			l.eeFfInYourApp = "Sample:";      //             h, nextIndex,"Sample:",isFile);	
			
			l.eeTmQuestionsNORMAL = "Questions";      //        "label", "Questions");
			l.eeTmQuestionNORMAL = "Question";      //              setAttribute("label", "Question");
			l.eeTmInputDataNORMAL = "Input your data";      //             , "Input your data");	
			l.eeTmEnterRegexKompatibelNORMAL = "Enter a value according to the regular expression: "; 	
			
			l.scStorageLimit = "State of Storage of: ";     //
			l.scFree = "Free:";     //
			l.scCurrentUsage = "Consumption:";     //
			l.scLocation = "Location:";     
			l.scLimit = "Maximum:"; 
			
			l.scTpSess = "Session";     //    if ( type == TYPE_SESSION ) return "Session";
			l.scTpMemo = "Memory";     //    else if ( type == TYPE_MEMORY ) return "Memory";
			l.scTpProj = "ProjectDatabase";     //    else if ( type == TYPE_PROJECT ) return "Project";
			l.scTpDb = "Database";     //    else if ( type == TYPE_DB ) return "Database";
			l.scTpUnknown = "unbekannter typ ";     //    else return ("unbekannter typ "+type);
			l.scWarn1 = "Warning about StorageLimit of";     //
			l.scWarn2 = "We reached";     //
			l.scWarn3 = "% of that which is ";     //
			l.scWarn4 = ". In total you have here";     //
			l.scWarn5 = "The directory is in ";     //

			l.scErr1 = "Limit of ";     //
			l.scErr2 = " bytes exceeded in ";     //
			l.scErr3 = " of type ";     //			
			
			l.uriApp = "ServiceApp";     //
			l.uriAppMaster = "App";     //
			l.uriAppArt = "The";     //
			l.uriPdb = "ProjectDb";     //
			l.uriPdbArt = "The";     //
			l.uriSession = "Session";     //
			l.uriSessionArt = "The";     //
			l.uriLibro = "Libro";     //
			l.uriLibroArt = "The";     //
			l.uriMem = "Memory";     //
			l.uriMemArt = "The";     //
			l.uriSys = "System";     //
			l.uriSysArt = "The";     //		
  			
			l.uriPro = "ServiceProject";     //
			l.uriProMaster = "Project";     //
			l.uriProArt = "The"; 		
			
			l.eeFfChooseDir = "Choose Folder";     
			l.eeFfChooseFile = "Choose File";		
			
			l.locProject  = "Project";
			l.locApp  = "App";
			l.locLibro  = "Libro";
			l.locSession  = "Session";
			l.locSystem  = "System";
			l.locWorkspace  = "Workspace";
			l.locBackup  = "Backup";
			l.locStatus  = "Status";
			l.locUserDocument  = "UserDocument";
			l.locUserHome  = "UserHome";			
			l.uplHeader ="-UPLOAD";
			l.uplWhat ="Upload: ";
			l.uplFileSel ="File to upload";
			l.uplOverwrite ="Overwrite if file exists";
			l.uplSaveLibro ="Save file in Libro";
			l.uplSavePDb ="Save file in Project-Db";
			l.uplLibroFolder ="Target-Folder in Libro";
			l.uplDoUpload ="Do the upload";	
			l.uplDoUpload ="Cancel the upload";
		}
		sm.out.println("Did build language: "+langCode);
		return l;
		
	}
	public static void startupActions(CommStartMsg sm) {
		langMap = new HashMap<String, Lang>();
		langMap.put("de", buildLang("de",sm));
		langMap.put("en", buildLang("en",sm));
	}
	
	public static String getDefaultLangCode() {
		String defaultLangCode = "de";
		/*[replace-defaultLangCode]*/
		return defaultLangCode;
	}
	public static Lang getLang(String langCode ) {
		Lang reti = langMap.get(langCode);
		if ( reti != null ) 
			return reti;
		else 
			return langMap.get(getDefaultLangCode());
		
				
	}

}
