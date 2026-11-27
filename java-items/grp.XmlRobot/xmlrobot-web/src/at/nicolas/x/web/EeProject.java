package at.nicolas.x.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.log4j.Level;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.WebVfs.ReqRes;
import at.nicolas.x.WebVfs.TkTicket;
import at.nicolas.x.WebVfs.TkTicketFileExtra;
import at.nicolas.x.WebVfs.TkTicketManager;
import at.nicolas.x.WebVfs.TkValidationError;
import at.nicolas.x.WebVfs.TkValidationResult;
import at.nicolas.x.WebVfs.XrRqRpEx;
import at.nicolas.x.WebVfs.XrRqRpTi;
import at.nicolas.x.WebVfs.XrRqRpTk;
import at.nicolas.x.int_coop.CryptoBase;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_setup.Lang;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.Interface;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.int_specs.TkChooseException;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;
import at.nicolas.x.pub.UserInfo;
import at.nicolas.x.tasks.FileSelectorAll;
import at.nicolas.x.tasks.Zip;
import at.nicolas.x.term_core.Seitenlinks;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_ti.RefCode;
import at.nicolas.x.term_ti.Ti_TerminalLink;

public class EeProject implements IGuiController, IMessageBox{

	//private static final String submitOk = "Ok";
	
	//private static final String submitCancel = "Stop";
	private String appid;
	private DbApp dbApp;
	private String commitsPath="";
	private String cachePath="";	


	private String projectId = null;
	private String projectDir = null;

	private JobRunner jobrunner ;
	private TkTicketManager ticketManager = null;	
	private boolean type_to_choose_is_file = false;
	private TkTicket ticket_to_choose = null;
	
	private Tc_Terminator terminator = null;

	private ArrayList<Seitenlinks> seitenlinks;
	
	private Interpreter interpreterParked ;
	private ReqRes reqResParked;
	//private JobRunner jobrunnerParked;
	private TkTicketManager ticketManagerParked;
	private ArrayList<Seitenlinks> seitenlinksParked;
	private boolean isInFirstRoundParked = false;
	
	private boolean isParked = false;
	private int parkType = 0;
	public static final int PARK_CHOOSE_FILE = 	1;
	public static final int PARK_CHOOSE_DIR = 	2;
	
	private boolean tkIsOut = false;

	private Surrounding surrounding;
	
	public boolean gcfIsSave =false;
	public boolean gcfIsFile =false;
	public String gcfLabel;
	public String gcfTip;
	public String gcfProposedFilenameSave;
	public String gcfExtension;
	public boolean gcfForceExtension =false;
	public String gcfFollowJob;
	public String gcfCurVal;	
	
	
	private boolean isInFirstRound = false;

	Stack<StorageMessage> storageMessageStack = new Stack<StorageMessage>();
	
	public void sendNewMessage(StorageMessage storageMessage) {
		storageMessageStack.push(storageMessage);
	}
	
	private String getHtmlProDbButton() {
		
		/*
		if ( sitz.hasPrivDbService())
			return "<div class='stdButtonHome stdButtonHomeSystem'><a class='stdButtonHome stdButtonHomeSystem' href='projectDb'>" + sitz.lang().getEepMenuSystem() +"</a></div>";
		else*/
			return "";	
				
			
		
	}
	
	private String getHtmlProTermination() {
		return "<div class='stdButtonHome stdButtonHomeDrop'><a class='stdButtonHome stdButtonHomeDrop' href='quit1'>" + sitz.lang().getEepMenuDrop() +"</a></div>";
		/*
		if ( sitz.hasPrivDrop() ) 
			return "<div class='stdButtonHome stdButtonHomeDrop'><a class='stdButtonHome stdButtonHomeDrop' href='quit1'>" + sitz.lang().getEepMenuDrop() +"</a></div>";
		else
			return "";		*/
	}	
	
	private String getHtmlProDbCheckinButton() {
		
		if ( sitz.hasPrivDbWrite() )
			return "<div class='stdButtonHome stdButtonHomeSave'><a class='stdButtonHome stdButtonHomeSave' href='projectDbCheckin'>" + sitz.lang().getEepMenuSave() +"</a></div>";
		else
			return "";		
	}	
	
	private String getHtmlProDbCheckoutButton() {
		if ( sitz.hasPrivDbRead() )
			return "<div class='stdButtonHome stdButtonHomeRead'><a class='stdButtonHome stdButtonHomeRead' href='projectDbCheckout'>" + sitz.lang().getEepMenuRead() +"</a></div>";
		else
			return "";		
	}	
	
	private String getHtmlLoginButton() {
		String htmlLoginButton = "";
		// if ( sitz.hasPrivDbConnect())
			htmlLoginButton = "<div class='stdButtonHome stdButtonHomeConnect'><a class='stdButtonHome stdButtonHomeConnect' href='projectDbLogin'>" + sitz.lang().getEepMenuConnect() +"</a></div>";
		if ( userInfo != null ) {
			String a=userInfo.getName();
			if ( userInfo.getCollection() != null )
				a = a+"-"+userInfo.getCollection();
			htmlLoginButton = htmlLoginButton+a;
		}
		return htmlLoginButton;
	}		
	
	private String getHtmlDecryptButton() {
		String htmlLoginButton = "";
		/* vorerst nicht
		if ( !sitz.hasPrivDbDecrypt())
			return "";
		else {
			htmlLoginButton = "<div class='stdButtonHome stdButtonHomeDecrypt'><a class='stdButtonHome stdButtonHomeDecrypt' href='decrypt'>" + sitz.lang().getEepMenuDecrypt() +"</a></div>";
		}
		*/

		return htmlLoginButton;
	}		
	
	private String getHtmlProjectButton() {
		String htmlLoginButton = "";

		htmlLoginButton = "<div class='stdProButtonSuper stdButtonSuper'><a class='stdProButtonSuper stdButtonSuper' href='info'>"+TkTicket.htmlByNatural(appid)+"</a></div>";
	

		return htmlLoginButton;
	}			
	
	private String getHtmlToolboxButton() {
		String htmlLoginButton = "";

		htmlLoginButton = "<div class='stdToolButtonSuper stdButtonSuper'><a class='stdToolButtonSuper stdButtonSuper' href='toolbox'>" + sitz.lang().getEepMenuToolbox() +"</a></div>";
	

		return htmlLoginButton;
	}	
	
	private String getHtmlSimpleloginButton() {
		String htmlLoginButton = "";

		htmlLoginButton = "<div class='stdButtonHome'><a class='stdButtonHome' href='simplelogin'>" + "Login" +"</a></div>";
	

		return htmlLoginButton;
	}			
	
	private String getHtmlControl() {
		boolean isCurrentlyDbLogged;
		if ( userInfo == null ) isCurrentlyDbLogged=false;
		else isCurrentlyDbLogged=true;
		String htmlLoginButton = getHtmlLoginButton();
		if ( sitz.hasPrivEeFooter()) {

			
			
			String htmlProTermination = getHtmlProTermination();
	
			String ret;
			if ( isCurrentlyDbLogged ) {
				String htmlProDbButton = getHtmlProDbButton();
				String htmlProDbCheckinButton = getHtmlProDbCheckinButton();
				String htmlProDbCheckoutButton = getHtmlProDbCheckoutButton();	
				String htmlDecryptButton = getHtmlDecryptButton();	
				ret = "<hr/><div class='Homes'>"+
						
						htmlDecryptButton+
						htmlProDbCheckinButton+
						htmlProDbCheckoutButton+
						htmlProDbButton+
					
						getHtmlSimpleloginButton()+
						htmlLoginButton+
						htmlProTermination+
						getHtmlProjectButton()+
						getHtmlToolboxButton()+
						
						
						"</div>";				
			} else {
				ret = "<hr/><div class='Homes'>"+
						
						getHtmlSimpleloginButton()+
						htmlLoginButton+
						htmlProTermination+
						getHtmlProjectButton()+
						getHtmlToolboxButton()+
						
						"</div>";				
				
			}
			return ret;
		} else {

			String ret = "<hr/><div class='Homes'>"+
			
			getHtmlSimpleloginButton()+
			htmlLoginButton+
			getHtmlProTermination()+
			getHtmlProjectButton()+
			
			"</div>";		
			return ret;
		}
		
	}
	private String getHtmlHeader( String titel, boolean isTk, HttpServletResponse response ) {
		response.setCharacterEncoding("UTF-8");
		String styleSheet = null;
		if ( surrounding.css!=null )
			styleSheet = "css/"+surrounding.css;
		else
			styleSheet= "css/standard.css";
		// styleSheet="file:///C:/Users/nicolas/Documents/standard.css";
		
		
		String ret;
		String addHead = "<meta name=\"viewport\" "
		+"content=\"width=device-width,initial-scale=1\"/>";
		if ( isTk ) {
			ret = "<html>"
					+"<head>"+
					"<meta charset=\"UTF-8\">"+addHead+"<link rel=\"stylesheet\" href=\""+styleSheet+"\" type= \"text/css\"><title>"+TkTicket.htmlByNatural(titel)+"</title></head>"+
					
					"<body class='bodyTk'>"+getHtmlTipToggle()+getHtmlTextareaCtrlEnter();   ;// + getHtmlControl();
			
		} else {
			ret = "<html><head><meta charset=\"UTF-8\">"+addHead+"<link rel=\"stylesheet\" href=\""+styleSheet+"\" type= \"text/css\"><title>"+TkTicket.htmlByNatural(titel)+"</title></head>"+
					
					"<body class='bodyStd'>"+getHtmlTipToggle() ; //  + getHtmlControl();
			
		}


		return ret;
		  
		
	}
	
	private String getHtmlTipToggle () {
		String ret = 
				" <script type=\"text/javascript\">"
				+ " function tipToggle(id) {"
				+" var e = document.getElementById(id);"
				+" if(e.style.display == 'block')"
				+" e.style.display = 'none';"
				+" else"
				+" e.style.display = 'block';"
				
				+"}"
				+ " function valErase(id) {"
				+ "  var e = document.getElementById('input-'+id); e.value=''; e.focus();"
				+""
				
				+"}"
				+" </script>";
		return ret;
	}
	
	private String getHtmlTextareaCtrlEnter () {
		String ret = 
			" <script type=\"text/javascript\">"	
			+ " function textareaKeyEvent(event) {"
			+ " if (event.which == 13 || event.keyCode == 13) {"
			+ "     if ( event.ctrlKey ) { document.forms[0].submit(); }"
			+ " }};"
			+" </script>";
		return ret;
	}	

	
	private String getNewMessagesAsHtml() {
		if ( storageMessageStack.isEmpty())
			return new String("");
		else {
			String result = "";
			while ( !storageMessageStack.isEmpty() ) {
				result = result + Commons.stringReplace(Commons.text_to_html(storageMessageStack.pop().getMessage())," ","&nbsp;")+"<br>";
			}
			result = result +"<br>";
			return result;
		}
	}
	private String getHtmlFooter() {
		return 
			   getHtmlControl()+
				"<div class=\"copyright\">Copyright 2010-2026 Nicolas Prandstaetter. All rights reserved.</div></body></html>";
	}

	private void resetProject () {	
		isParked = false;
		tkIsOut = false;
		isInFirstRound = false;
		ticket_to_choose = null;
		seitenlinks = null;
		seitenlinksParked = null;
		ticketManager = null;
		ticketIdToUpload = null;

		
	}
	private void resetProjectMore () throws FileSystemException, StorageLimitException {
		userInfo = null;
		e = null;
		deleteEncryptedProject();
	}
	private void parkIn ( int _parkType ) {
		tkIsOut = false;
		isParked = true;
		parkType = _parkType;
		// jobrunnerParked = jobrunner;
		interpreterParked = jobrunner.interpreter;
		reqResParked = jobrunner.getReqRes();
		seitenlinksParked = seitenlinks;		
		ticketManagerParked = ticketManager;
		isInFirstRoundParked = isInFirstRound;
						
		seitenlinks = new ArrayList<Seitenlinks>();
		isInFirstRound = false;
		terminator.parkIn();
	}
	
	private void parkOut() {
		isParked = false;
		parkType = 0;
		jobrunner.set(interpreterParked, reqResParked);
		seitenlinks = seitenlinksParked;
		ticketManager = ticketManagerParked;
		isInFirstRound = isInFirstRoundParked;
		

		seitenlinksParked = null;
		ticketManagerParked = null;
		isInFirstRoundParked = false;
		terminator.parkOut();
		tkIsOut = true;
							
	}
	private String getStatusProblem( String from ) {
		return sitz.lang().getEepErrorTkMood()+
				"<br>Source="+from
				+", Out="+tkIsOut
				+", Parked="+isParked
				+ sitz.lang().getEepErrorTkSolution(appid);			
	}
	private String getStatusProblemNoTicket( String from ) {
		return sitz.lang().getEepErrorTkNoTicket() +"<br>Source="+from
				+ sitz.lang().getEepErrorTkSolution(appid);			
	}	
	
	private String getStatusProblemOther( String from, String paras ) {
		return sitz.lang().getEepErrorTkNoTicket() +"<br>Source="+from+" "+paras
				+ sitz.lang().getEepErrorTkSolution(appid);			
	}		
	private String status_OUT_TRY() throws Exception {
		//System.out.println("iiiiiiiiiiiiiiiiiiiiiii  "+projectDir);
		//System.out.println(this.surrounding.getDescription());
		// if ( tkStatus != TK_OFF )
		if ( tkIsOut )
			throw new Exception(getStatusProblem("OUT_TRY"));		
		return null;
	}
	
	private void status_OUT_COMMIT(TkTicketManager neuerTicketManager) {
		tkIsOut = true;
		ticketManager = neuerTicketManager;	
		isInFirstRound = false;
		//Sp.p("DID: status_OUT_COMMIT");
	}
	
	private String status_OUTIP_TRY() throws Exception {
		// if ( tkStatus != TK_OFF )
		if ( tkIsOut )
			throw new Exception(getStatusProblem("OUTIP_TRY"));
		return null;
	}
	
	private void status_OUTIP_COMMIT(TkTicketManager neuerTicketManager) {
		tkIsOut = true;
		isInFirstRound = true;
		ticketManager = neuerTicketManager;		
		//Sp.p("DID: status_OUT_COMMIT");
	}	
	
	private String status_OK_TRY() throws Exception {
		if ( isParked );
		else if ( tkIsOut );
		else 
			throw new Exception(getStatusProblem("OK_TRY"));
		
		if (ticketManager == null)
			throw new Exception(getStatusProblemNoTicket("OK_TRY"));

		return null;
	}
	
	private void status_OK_COMMIT() {
		
		if ( isParked) {
			// das ist ein ok irgendwo innhalb des parked
			// parked bleibt erhalten
			// parkOut();

		}
		tkIsOut = false;
		isInFirstRound =false;
		ticketManager = null;
		//Sp.p("DID: status_OK_COMMIT");
	}	
	
	private String status_CANCEL_TRY() throws Exception {
		
		if ( isParked );
		else if ( tkIsOut );
		else 
			throw new Exception(getStatusProblem("CANCEL_TRY"));
		
		if (ticketManager == null)
			throw new Exception(getStatusProblemNoTicket("CANCEL_TRY"));
		
		return null;
	}
	private void status_CANCEL_COMMIT() {
				
		tkIsOut = false;
		isInFirstRound =false;
		ticketManager = null;
		//Sp.p("DID: status_CANCEL_COMMIT");
		

	}		

	private String ticketIdToUpload = null;

	private String status_WISHUPLOAD_TRY(String _ticketIdToUpload, TkTicketManager tm ) throws Exception {
		
		if ( isParked)
			throw new Exception(getStatusProblem("WISHUPLOAD_TRY"));		
		else if ( isInFirstRound);
		else if ( !tkIsOut)
			throw new Exception(getStatusProblem("WISHUPLOAD_TRY"));

			
		if (tm == null)
			throw new Exception(getStatusProblemNoTicket("WISHUPLOAD_TRY"));
		
		TkTicket ticketToUpload = tm.getTickets().get(_ticketIdToUpload);
		if ( ticketToUpload == null)
			throw new Exception(getStatusProblemOther("WISHUPLOAD_TRY", "Unknown TicketId="+_ticketIdToUpload));

		if ( !(ticketToUpload.var_type.equals("file")))
			throw new Exception(getStatusProblemOther("WISHUPLOAD_TRY", "TicketId is no file="+_ticketIdToUpload));
		this.ticketIdToUpload = _ticketIdToUpload;	
		return null;
	}
	
	private void status_PERFORMED_UPLOAD_TRY(TkTicketManager tm, String htmlTicketId ) throws Exception {
		if (ticketManager == null)
			throw new  Exception("tk doesnt find ticketmanager but has status ");		
		else if ( ticketIdToUpload == null )
			throw new Exception(getStatusProblemOther("PERFORMED UPLOAD_TRY", "No upload currently going on"));
		else if ( htmlTicketId == null )
			throw new Exception(getStatusProblemOther("PERFORMED UPLOAD_TRY", "No ticket-id from html"));
		else if ( !htmlTicketId.equals(ticketIdToUpload) )
			throw new Exception(getStatusProblemOther("PERFORMED UPLOAD_TRY", "Ticket-id from html differs from internal ticket-id"));			
	}	
	private void status_PERFORMED_UPLOAD_COMMIT() {
		ticketIdToUpload = null;
	}
	
	
	private String status_WISHCHOOSE_TRY(String ticketIdToChoose, TkTicketManager tm ) throws Exception {
		
		if ( isParked)
			throw new Exception(getStatusProblem("WISHCHOOSE_TRY"));		
		else if ( isInFirstRound);
		else if ( !tkIsOut)
			throw new Exception(getStatusProblem("WISHCHOOSE_TRY"));

			
		if (tm == null)
			throw new Exception(getStatusProblemNoTicket("WISHCHOOSE_TRY"));
		
		TkTicket ticketToChoose = tm.getTickets().get(ticketIdToChoose);
		if ( ticketToChoose == null)
			throw new Exception(getStatusProblemOther("WISHCHOOSE_TRY", "Unknown TicketId="+ticketIdToChoose));

		if ( !(ticketToChoose.var_type.equals("dir") || ticketToChoose.var_type.equals("file")))
			throw new Exception(getStatusProblemOther("WISHCHOOSE_TRY", "TicketId is not dir or file="+ticketIdToChoose));
		return null;
	}
	
	private void status_WISHCHOOSE_COMMIT_WHATEVER(
			TkTicket chooserTicket,
			int _parkType,
			HttpServletRequest request ) {
		
		if ( isInFirstRound);
		else
			ticketManager.reflectToMemory(new MyRequestor(request));

		parkIn(_parkType);
		
		
		if ( chooserTicket.var_type.equals("dir"))
			type_to_choose_is_file = false;
		else 
			type_to_choose_is_file = true;	
				
		ticket_to_choose = chooserTicket;	
		//Sp.p("DID: status_WISHCHOOSE_COMMIT_WHATEVER");
	}	
	

	
	private void status_WISHCHOOSE_COMMIT_FILE(
			TkTicket chooserTicket,
			boolean isSave,
			String label,
			String tip,
			String proposedFilenameSave,
			String extension,
			boolean force_extension,
			String followJob, 
			String curVal,
			HttpServletRequest request,
			int PARK_TYPE,
			boolean isFile ) {
		status_WISHCHOOSE_COMMIT_WHATEVER(chooserTicket,PARK_TYPE,request);

		gcfIsFile = isFile;
		gcfIsSave =isSave;
		gcfLabel=label;
		gcfTip=tip;
		gcfProposedFilenameSave=proposedFilenameSave;
		gcfExtension=extension;
		gcfForceExtension =force_extension;
		gcfFollowJob=followJob;	
		gcfCurVal = curVal;
		//Sp.p("DID: status_WISHCHOOSE_COMMIT_FILE");
		
	}
	
	private String status_CONFIRMCHOOSE_TRY() throws Exception {
		
		if ( !isParked ) 
			throw new Exception(getStatusProblem("CONFIRMCHOOSE_TRY"));	
		else if ( parkType != PARK_CHOOSE_FILE && parkType != PARK_CHOOSE_DIR) 
			throw new Exception(getStatusProblem("CONFIRMCHOOSE_TRY2"));	
		//else if ( tkStatus != TK_OFF  )
		//	throw new Exception("status_CONFIRMCHOOSE_TRY: Wantet to confirm choose but tkStatus is in state: "+tkStatus);
		else if ( ticket_to_choose == null ) 
			throw new Exception(getStatusProblemOther("CONFIRMCHOOSE_TRY", "No choose-ticket found for confirmation"));
		return null;
	}
	private void status_CONFIRMCHOOSE_COMMIT() {
		parkOut();
		
		
		type_to_choose_is_file = false;
		ticket_to_choose = null;	
		//Sp.p("DID: status_CONFIRMCHOOSE_COMMIT");
	
	}	
	
	private String lastChooseDirEscaped = null;
	public void doConfirmChoose ( 
				HttpServletRequest request, 
			   HttpServletResponse response,
			   String varType, 
			   String value ) {
		
		// System.out.println("----- Bin im Confirm CHOOSE "+varType+", "+value);
		
		try {

			status_CONFIRMCHOOSE_TRY();
		
			ticket_to_choose.setValueN(value);
			status_CONFIRMCHOOSE_COMMIT();
			if ( varType.equals("dir"))
				lastChooseDirEscaped = value;
			else if ( varType.equals("file"))
				lastChooseDirEscaped = Commons.getDirOfFile(value);
			
			lastChooseDirEscaped = Entities.importEntities(lastChooseDirEscaped);
			// System.out.println("----- lastChooseDir="+lastChooseDirEscaped);
			
			changeContext(request, response);

			PrintWriter out = response.getWriter();
			if ( isInFirstRound ) {
				// weiter so
				//Sp.p("etwas gefunden");
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+ TkTicket.htmlByEscaped(ticketManager.getLastTicket().label),true,response));
				sendSingleInputIp(out,ticketManager.getLastTicket(),ticketManager,sitz.lang());
				out.println(getHtmlFooter());
			} else {
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoConfirmChoose(),true,response));

				sendMap(out, ticketManager,false);

				out.println(getHtmlFooter());				
				
			}



		}catch (Exception e) {
			// TODO Auto-generated catch block
			haveError(e, request, response);
		}		
	}

	boolean uploadPastOverwrite = false;
	boolean uploadPastLibro = false;
	boolean uploadPastPdb = false;
	String  uploadPastFolder = null;
	
	public void doWishUpload ( 
			HttpServletRequest request, 
			   HttpServletResponse response,
			   String ticketid ) {
		try {
			talk("EeProject "+projectId+" received wish for upload: '"+ticketid+"'");

			ReqResEe reqResEe = changeContext(request, response);
			String statusText = status_WISHUPLOAD_TRY(ticketid,ticketManager);
			TkTicket ticket = ticketManager.getTickets().get(ticketid);
						
			PrintWriter out = response.getWriter(); 
	
			out.println(getHtmlHeader(sitz.lang().getEepLogoLogin()+sitz.lang().getUplHeader(),false,response));
			out.println("<form class=\"Upload\" action=\"performUpload?ticketid="+ticketid+"\" method=\"post\" enctype=\"multipart/form-data\">");;
			
			out.println("<table class=\"Upload\">"); // like PdB
			
			out.println("<tr>");
				out.println("<td colspan=\"2\" class=\"UploadHead\">");
				out.println(sitz.lang().getUplWhat()+ticket.getLabelHtml() );
				// out.println(sitz.lang().getEepLoginTxtHeadline() );
				out.println("</td>");				
			out.println("</tr>");		
			
			out.println("<tr class=\"UploadFile\">");
				out.println("<td>");
				//out.println(sitz.lang().getEepLoginTxtUser());
				out.println(sitz.lang().getUplFileSel());
				out.println("</td>");
				out.println("<td>");
				out.println("<input type=\"file\" name=\"file\" />");
				out.println("</td>");			
			out.println("</tr>");		
			
			
			
			out.println("<tr class=\"UploadOverwrite\">");
				out.println("<td>");
				out.println(sitz.lang().getUplOverwrite());
				out.println("</td>");
				out.println("<td>");
				out.println("<input type=\"checkbox\" name=\"overwrite\" ");
				if (uploadPastOverwrite)
					out.println("checked");
				out.println(">");
				out.println("</td>");			
			out.println("</tr>");		
			if ( sitz.getLibroUpdatePrivileg()) {			
				out.println("<tr class=\"UploadLibro\">");
					out.println("<td>");
					out.println(sitz.lang().getUplSaveLibro());
					out.println("</td>");
					out.println("<td>");
					out.println("<input type=\"checkbox\" name=\"libro\" ");
					if (uploadPastLibro)
						out.println("checked");
					out.println(">");
					out.println("</td>");			
				out.println("</tr>");	
				
				out.println("<tr class=\"UploadPdb\">");
				out.println("<td>");
				out.println(sitz.lang().getUplSavePDb());
				out.println("</td>");
				out.println("<td>");
				out.println("<input type=\"checkbox\" name=\"pdb\" ");
				if (uploadPastPdb)
					out.println("checked");
				out.println(">");
				out.println("</td>");			
				out.println("</tr>");					

				out.println("<tr class=\"UploadFolder\">");
					out.println("<td>");
					out.println(sitz.lang().getUplLibroFolder());
					out.println("</td>");
					out.println("<td>");
					out.println("<input type=\"text\" name=\"folder\" cols=\"50\"");
					if (uploadPastFolder!=null)
						out.println("value=\""+uploadPastFolder+"\"");
					out.println(">");
					out.println("</td>");			
				out.println("</tr>");			
			}
			out.println("<tr>");
				out.println("<td>");
				out.println("</td>");
				out.println("<td>");
				out.println("<input class='UploadSubmit' type=\"submit\" value=\"" + sitz.lang().getUplDoUpload() +"\" />");
				out.println("</td>");			
			out.println("</tr>");	
			
			
			out.println("<tr>");
				out.println("<td>");
				out.println("</td>");
				out.println("<td>");
				out.println("<a href='info' class='UploadCancel'>" + sitz.lang().getUplDoCancel() +"</a>");
				out.println("</td>");			
			out.println("</tr>");				
		
			out.println("</table>");
			out.println("</form>");
			

			out.println(getHtmlFooter());
			
		} catch (Exception e) {
			haveError(e, request, response);
		}
	}		

   
   private void copyStreams(InputStream source, OutputStream target) throws IOException {
	    byte[] buf = new byte[8192];
	    int length;
	    while ((length = source.read(buf)) > 0) {
	        target.write(buf, 0, length);
	    }
	}
   
   private String copyUploadedFile (FileItem fileItem ) throws FileSystemException, StorageLimitException {

	    String targetFolder;
	    if ( uploadPastLibro ) {
	    	targetFolder = sitz.getLibroDir(appid);
	    	if ( uploadPastFolder != null ) {
	    		targetFolder = targetFolder + uploadPastFolder;
	    		targetFolder = CommonsVfs.makeyBeautifulUri(targetFolder);
				if ( !targetFolder.endsWith("/"))
					targetFolder= targetFolder+"/";
	    	}
	    } else if ( uploadPastPdb ) {
	    	targetFolder = sitz.getDatabaseLinkUri();
	    	if ( uploadPastFolder != null ) {
	    		targetFolder = targetFolder + uploadPastFolder;
	    		targetFolder = CommonsVfs.makeyBeautifulUri(targetFolder);
				if ( !targetFolder.endsWith("/"))
					targetFolder= targetFolder+"/";
	    	}
	    } else 
	    	targetFolder = projectDir+"uploads/";
		String targetFile = targetFolder+fileItem.getName();

		FileObject target_fileFO = CommonsVfs.resolveAnySilent(targetFile);

		if ( target_fileFO.exists() && !uploadPastOverwrite ) {
			String vorname = target_fileFO.getName().getBaseName();
			int dot_pos = vorname.indexOf(".");
			if ( dot_pos != -1 ) {
				vorname = vorname.substring(0, dot_pos);
			}
			String extension = target_fileFO.getName().getExtension();
			if ( extension!=null && extension.length()==0 ) 
				extension = null;
			talk("existiert bereits: "+vorname+":"+extension);

			int versuchteNummer = 0;
			String versuchterName = null;
			boolean erfolgreicherVersuch = false;
			while (!erfolgreicherVersuch && versuchteNummer < 50000) {
				versuchteNummer++;
				if ( extension == null )
					versuchterName = targetFolder+vorname+"-"+versuchteNummer;
				else
					versuchterName = targetFolder+vorname+"-"+versuchteNummer+"."+extension;
				
				target_fileFO = CommonsVfs.resolveAnySilent(versuchterName);
				if ( !target_fileFO.exists()) {
					erfolgreicherVersuch = true;
					targetFile = versuchterName;
				}
				
			}
			if ( !erfolgreicherVersuch ) {
				throw new FileSystemException("File to upload existiert bereits und das renaming ist fehlgeschlagen: "
						+ targetFolder+vorname+"-??."+extension);
			}

		}
		
		OutputStreamRequest osr = null ;
		InputStream uploadedStream = null;
		OutputStream psOutputStream = null;
		
		osr = CommonsVfs.getOutputStreamByFilenameP1(targetFile, false,sitz);
		
		try {
			uploadedStream = fileItem.getInputStream();
			psOutputStream = osr.getOutputStream();
			copyStreams(uploadedStream, psOutputStream);
			uploadedStream.close();
			psOutputStream.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			try {
				if ( uploadedStream != null ) uploadedStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if ( psOutputStream != null ) psOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CommonsVfs.finishOutputStreamP2(targetFile, sitz,osr.getOpnum());//sitz,osr.opnum);
		return targetFile;
		
		   
   }
	public void doWishUploadPerformed (
			HttpServletRequest request, 
			HttpServletResponse response,
			String htmlTicketId) {
		
		talk("EeProject "+projectId+" received performance of wish for upload");
		FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(
				request.getSession().getServletContext());		
		changeContext(request, response);
		try {

			status_PERFORMED_UPLOAD_TRY(ticketManager,htmlTicketId);
			TkTicket ticketWithUpload = ticketManager.getTickets().get(htmlTicketId);
			
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) throw new Exception("Das sollte ein mulitpart-post sein");
			talk("isMultipart="+isMultipart);
			// Create a factory for disk-based file items
			//DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			//ServletContext servletContext = this.getServletConfig().getServletContext();
			//File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			//factory.setRepository(new File("C:\\Users\\nicolas\\Documents\\tomcat-temp-upload"));
			
			String uploadTempFolder = "C:\\Users\\nicolas\\Documents\\tomcat-temp-upload";
			/*[echnaton-upload-temp-folder-classic]*/
			
			DiskFileItemFactory factory = new DiskFileItemFactory(
					0, 
					new File(uploadTempFolder));
			
			if ( fileCleaningTracker != null )
				factory.setFileCleaningTracker(fileCleaningTracker);


			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			talk("projctdir="+projectDir);
			
			
			uploadPastOverwrite = false;
			uploadPastLibro = false;
			uploadPastPdb = false;
			uploadPastFolder = null;
			FileItem itemToUpload = null;
			while (iter.hasNext()) {
			    FileItem item = iter.next();

			    if (item.isFormField()) {
			        //processFormField(item);
			    	if ( item.getFieldName().equals("overwrite")) {
			    		if (item.getString().equals("on"))
			    			uploadPastOverwrite = true;
			    	}
			    	if ( item.getFieldName().equals("libro")) {
			    		if (item.getString().equals("on"))
			    			uploadPastLibro = true;
			    	}	
			    	if ( item.getFieldName().equals("pdb")) {
			    		if (item.getString().equals("on"))
			    			uploadPastPdb = true;
			    	}				    	
			    	if ( item.getFieldName().equals("folder")) {
			    		if (item.getString().trim().length() > 0)
			    			uploadPastFolder = item.getString();
			    	}				    	
			    	talk("ein-form-field: "+item.getFieldName()+" = ["+item.getString()+"]");
			    } else if (item.getName() !=null && item.getName().length()>0) {
			        String fieldName = item.getFieldName();
			        String fileName = item.getName();
			        String contentType = item.getContentType();
			        boolean isInMemory = item.isInMemory();
			        long sizeInBytes = item.getSize();
			        talk("fileitem: fieldName="+fieldName
			        		+ " fileName="+fileName
			        		+ " contentType="+contentType
			        		+ " isInMemory="+isInMemory
			        		+ " sizeInBytes="+sizeInBytes
			        		);
			        itemToUpload = item;

			        // processUploadedFile(item);
			    }
			}
			talk("upload-request-summary");
			talk("uploadPastOverwrite="+uploadPastOverwrite);
			talk("uploadPastLibro    ="+uploadPastLibro);
			talk("uploadPastPdb      ="+uploadPastPdb);
			talk("uploadPastFolder   ="+uploadPastFolder);
			
			if ( itemToUpload != null ) {
		        String nameOfUploadedFile = copyUploadedFile(itemToUpload);
		        ticketWithUpload.setValueN(nameOfUploadedFile);				
			}


			status_PERFORMED_UPLOAD_COMMIT();


			
			

			PrintWriter out = response.getWriter();
			if ( isInFirstRound ) {
				// weiter so
				//Sp.p("etwas gefunden");
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+ TkTicket.htmlByEscaped(ticketManager.getLastTicket().label),true,response));
				sendSingleInputIp(out,ticketManager.getLastTicket(),ticketManager,sitz.lang());
				out.println(getHtmlFooter());
			} else {
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoConfirmChoose(),true,response));

				sendMap(out, ticketManager,false);

				out.println(getHtmlFooter());				
				
			}



		}catch (Exception e) {
			// TODO Auto-generated catch block
			haveError(e, request, response);
		}			
		
		try {
			
			
			
			
			
			//if ( tkStatus != TK_DOING_UPLOAD  
			//		|| ticket_to_choose == null) 
			//	throw new Exception("tk is not in the right moode, needed to be waiting but is "+tkStatus);

			
			// ticket_to_choose
			/*
			
			TkTicket ticket = ticket_to_choose;
						
			Date mydate = new Date();
			String myformat = "yyyyMMdd_HHmmss";			
			SimpleDateFormat formatter = new SimpleDateFormat (myformat);
			String dateString = formatter.format(mydate);			
			
			String target_dir = projectDir+"myData/"+myformat+"/";
			FileObject theDir = CommonsVfs.getFileObjectByFilenameToWrite(target_dir,theBenutzerProjektSitzung);
			//System.out.println("--- 2 ---");
			CommonsVfs.mkdirCascade(theDir,theBenutzerProjektSitzung);
			
			 // Check that we have a file upload request
		    boolean  isMultipart = ServletFileUpload.isMultipartContent(request);
		    response.setContentType("text/html");
		    java.io.PrintWriter out = response.getWriter( );
		    
		      if( !isMultipart ) {
		    	  out.println(getHtmlHeader("SERVER-UPLOAD",true));
		          out.println("<p>No file uploaded</p>"); 
		          out.println(getHtmlFooter());
		          return;
		       }		
		      
		      */
		      
		      /*
		      DiskFileItemFactory factory = new DiskFileItemFactory();
		      
		      // maximum size that will be stored in memory
		      factory.setSizeThreshold(maxMemSize);
		   
		      // Location to save data that is larger than maxMemSize.
		      factory.setRepository(new File("c:\\temp"));

		      // Create a new file upload handler
		      ServletFileUpload upload = new ServletFileUpload(factory);
		   
		      // maximum file size to be uploaded.
		      upload.setSizeMax( maxFileSize );	
		      
		      try { 
		          // Parse the request to get file items.
		          List fileItems = upload.parseRequest(request);
		 	
		          // Process the uploaded file items
		          Iterator<?> i = fileItems.iterator();

		          out.println("<html>");
		          out.println("<head>");
		          out.println("<title>Servlet upload</title>");  
		          out.println("</head>");
		          out.println("<body>");
		    
		          while ( i.hasNext () ) {
		             FileItem fi = (FileItem)i.next();
		             if ( !fi.isFormField () ) {
		                // Get the uploaded file parameters
		                String fieldName = fi.getFieldName();
		                String fileName = fi.getName();
		                String contentType = fi.getContentType();
		                boolean isInMemory = fi.isInMemory();
		                long sizeInBytes = fi.getSize();
		             
		                // Write the file
		               
		                if( fileName.lastIndexOf("\\") >= 0 ) {
		                   file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
		                } else {
		                   file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		                }
		                fi.write( file ) ;
		                
		                out.println("Uploaded Filename: " + fileName + "<br>");
		             }
		          }
		          out.println("</body>");
		          out.println("</html>");
		          } catch(Exception ex) {
		             Sp.p(ex);
		          }		
		                
		     */ 
		      
			// Sp.p("Uploading nbr of parts "+ request.getParts().size());
			/*
	        for (Part part : request.getParts()) {
	            String fileName = extractFileName(part);
	            Sp.p("targetFileName="+theDir);
	            Sp.p("size="+part.getSize());
	            Sp.p("name="+part.getName());
	            InputStream is = part.getInputStream();
	            
	            
	            //part.getInputStream()
	            
	            // refines the fileName in case it is an absolute path
	            //fileName = new File(fileName).getName();
	            
	            //part.write(savePath + File.separator + fileName);
	        }
	        */
			//tring upload_file = mydata_dir + "uploaded";
			
	        /*
		    String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
		    Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		    //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		    InputStream fileContent = filePart.getInputStream();	
		    OutputStream os = Commons.getOutputStreamByFilename(upload_file, false);
		    Commons.copy_stream(fileContent, os);
		    fileContent.close();
		    os.close();


			*/
		    // soll-so-sein ticket.setValueN("xx");
			// setTkStatusChooseOff();
			
			//jr.set(interpreterParked, reqResParked);
			//ReqResEe reqResEe = changeContext(request, response);
			// PrintWriter out2 = response.getWriter();
			// soll-so-sein out.println(getHtmlHeader("SOME-UPLOAD",false));
			// sendMap(out, ticketManager);
			// soll-so-sein out.println(getHtmlFooter());
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
 private String extractFileName(Part part) {
     String contentDisp = part.getHeader("content-disposition");
     String[] items = contentDisp.split(";");
     for (String s : items) {
         if (s.trim().startsWith("filename")) {
             return s.substring(s.indexOf("=") + 2, s.length()-1);
         }
     }
     return "";
 }     		
	public void doWishChoose ( 
			HttpServletRequest request, 
			   HttpServletResponse response,
			   String ticketid ) {
		try {
			talk("eeProject "+projectId+" received wish for choose: '"+ticketid+"'");

			ReqResEe reqResEe = changeContext(request, response);
			String statusText = status_WISHCHOOSE_TRY(ticketid,ticketManager);
			TkTicket ticket = ticketManager.getTickets().get(ticketid);
							

			String targetServiceJob=null;
			boolean ticketIsFile = false;
			boolean isSave = false;
			String label = null;
			String tip = null;
			String proposedFilenameSave = null;
			String extension=null;
			boolean force_extension = false;
			String followJob = null;
			String curVal = null;
			
			String base = null;
			String x2 = null;
			
			if ( ticket instanceof TkTicketFileExtra ) {
				
				TkTicketFileExtra r = (TkTicketFileExtra)ticket;
				base = r.base;
				x2 = r.getX2();
				Sp.ph("base from tkticketfile="+base);
				//x2 = r.x2;
				//Sp.ph("x2 from tkticketfile="+x2);
				
				ticketIsFile = r.isFile;
				
				isSave = r.isSave;
				label= Entities.exportEntities(r.label);
				tip = Entities.exportEntities(r.tip);
				force_extension = r.forceExtension;
				extension = r.extension;
				followJob = Entities.exportEntities(r.follow_job);
				
				if ( ticketIsFile )
					targetServiceJob ="Goto-Choose-File";
				else
					targetServiceJob ="Goto-Choose-Dir";
				try {
					curVal = r.getVarFromMemoryN();
				} catch (Exception e) {
					curVal = null;
				}
				
				if ( isSave ) {
					if ( extension != null ) proposedFilenameSave="x"+extension;
					else
						 proposedFilenameSave="x.y";
				}				
			}
			 
			/*
			if ( base == null ) {
				base = "(dir,pro)";
			}
			*/

			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			

			
			Element c1=new Element("conf");
			
			if ( base != null ) {
				Element s1 = new Element("dir");
				s1.setAttribute("id","base");
				s1.setAttribute("x",base);
				c1.addContent(s1);
			}
			
			
			if ( x2 != null && !ticketManager.do_first_round ) {
				Element s2 = new Element("prop");
				s2.setAttribute("id","x2");
				s2.setAttribute("x",x2);
				c1.addContent(s2);				
				
			}
			
			if ( lastChooseDirEscaped != null ){
				Sp.ph("--- lastChooseDir="+lastChooseDirEscaped);
				Element s2 = new Element("dir");
				s2.setAttribute("id","lastChooseDir");
				s2.setAttribute("x",lastChooseDirEscaped);
				c1.addContent(s2);				
				
			}
			
			
			//System.out.println("Paras for choose ["+targetServiceJob+"]: \n"+Commons.toText(c1));
			configListx.add(c1);		
			
			

			
			Interpreter interpreter = JobRunner.createInterpreter(
					targetServiceJob, // "Anton", 
					true, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(false); 
			
			interpreter.setSurrounder(surrounding.getSurroundingChoose());
			
			int PARK_TYPE = 0;
			if ( ticketIsFile )
				PARK_TYPE = PARK_CHOOSE_FILE;
			else
				PARK_TYPE = PARK_CHOOSE_DIR;

			status_WISHCHOOSE_COMMIT_FILE(ticket,
					isSave,
					label,
					tip,
					proposedFilenameSave,
					extension,
					force_extension,
					followJob,
					curVal,
					request,
					PARK_TYPE,
					ticketIsFile);

			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));			
				
		} catch (Exception e) {
			haveError(e, request, response);
		}
	}	
	

	public void doTkCancel ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		changeContext(request, response);
		boolean cont = true;
		try {
			status_CANCEL_TRY();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			cont= false;
			haveError(e, request, response);
			
		}
		
		if ( cont ) {
			status_CANCEL_COMMIT();
			jobrunner.continueAfterTkCancel();	
			
		}

	}
	
	
	
	public void doTkOk ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		try {
			doTkOk2 ( request, response );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			haveError(e, request, response);
		}
	}	
	private void printMaybeTkEnd() throws Exception {
		//Sp.p("X-3");

			if ( ticketManager.getTkElementForPrint() != null &&
				 ticketManager.getTkElementForPrint().getChildren().size()>0) {
				//Sp.p("A2");
				// boolean flag_print_before = false;
				boolean flag_print = false;
				boolean flag_print_tips_only = false;
				boolean flag_print_with_tips = false;
				boolean flag_print_all_fields = false;
				boolean flag_print_all_tested_fields = false;
				
				// flag_print_before = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print_before");
				flag_print = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print");
				flag_print_tips_only = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print_tips_only");
				flag_print_with_tips = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print_with_tips");
				flag_print_all_fields = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print_all_fields");				
				flag_print_all_tested_fields = Commons.hasFlag(ticketManager.getTkElementForPrint(), "print_all_tested_fields");				
				// Sp.p("A3");
				if ( flag_print ) {
					// Sp.p("A4");
					// inperformant
					//System.out.println("bb cccccccccc print_tk ");
					terminator.print_tk(
							ticketManager.getTkElementForPrint(), 
							Level.INFO, 
							ticketManager.tiHandler, 
							null, 
							appid, 
							"",
							projectId,
							projectDir,
							flag_print_with_tips,flag_print_tips_only,flag_print_all_fields,flag_print_all_tested_fields);
					// Sp.p("A5");
				}			
			}
			
		
	}
	
	public void doTkRequestFast ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		try {
			talk("eeProject "+projectId+" received TK_REQUEST_FAST ");
			changeContext(request, response);
			status_OK_TRY();
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
			sendMap(out, ticketManager,true);
			out.println(getHtmlFooter());	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			haveError(e, request, response);
		}
	}		
	public void doTkOk2 ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) throws Exception {
		talk("eeProject "+projectId+" received TK OK ");
		changeContext(request, response);
		status_OK_TRY();
		

		if ( makeFast)
			ticketManager.reflectToMemory(new MyRequestor(request));
		
		

		TkValidationResult v = ticketManager.validate();
	
		if ( v.errors.size() == 0) {

			if ( makeFastOnPersonalRequest ) {
				// status_OUT_TRY();
				PrintWriter out = response.getWriter();
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
				sendMap(out, ticketManager,false);
				out.println(getHtmlFooter());
				// status_OUT_COMMIT(ticketManager);					
			} else {
				// hier das tkende
				//Sp.p("A0");
				printMaybeTkEnd();
				
				// Sp.p("A6");
				status_OK_COMMIT();
				jobrunner.continueAfterTk();				
			}
			
	


		} else {

			// validation error
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepTkLogoProblems()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
			sendTkErrors(out, v);
			out.println("<hr/>");
			sendMap(out, ticketManager,false);
			out.println(getHtmlFooter());
		}
	
			

	}
	
	public static void sendTkErrors ( 
			PrintWriter out, 
			TkValidationResult tkValidationResult) throws Exception {
		// ArrayList<Element> l = e.getChildren();
		Sp.p("Going to send errors");
		out.println("<div class='problemsHeader'>Problems</div>");
		out.println("<ul>");
		Iterator<TkValidationError> iterator = tkValidationResult.errors.iterator();
		while ( iterator.hasNext() ) {			
			TkValidationError valError = (TkValidationError)iterator.next();
			out.println("<li>"+TkTicket.htmlByEscaped(valError.varLabel)+": "+TkTicket.htmlByEscaped(valError.problem)+"</li>");		
		}	
		out.println("</ul>");
	}
	/**/
	public static void sendTkError ( 
			PrintWriter out, 
			TkValidationError validationError) throws Exception {
		// ArrayList<Element> l = e.getChildren();
		Sp.p("Going to send errors");
		out.println("<div class='problemsHeader'>Problem</div>");
		out.println("<ul>");	
		out.println("<li>"+TkTicket.htmlByEscaped(validationError.problem)+"</li>");
		out.println("</ul>");		
	}	
	boolean makeFast = false;
	boolean makeFastOnPersonalRequest = false;
	
	
	
	private void printTkSumPanel(
			PrintWriter out,
			boolean bottom,
			boolean doReviewAgainOnSubmit,
			boolean editAllTechnicallyPossible,
			boolean isSlowSum,
			boolean showActionsAtAll,
			boolean isTopPanel
			) {

		String submitLabel = null;

		if ( doReviewAgainOnSubmit ) {
			submitLabel=sitz.lang().getEepTkSubmitConfReview();
		} else {
			submitLabel=sitz.lang().getEepTkSubmitConfGo();
		}
		
		String individualClass = null;

		if ( isTopPanel ) {
			if ( isSlowSum ) {
				individualClass ="tkReviewSubmitSlowButton";
		
			} else 	if ( doReviewAgainOnSubmit ) {
				individualClass="tkReviewSubmitFastReviewButtonTop";
		
			} else {
				individualClass="tkReviewSubmitFastGoButton";

			}			
		} else {
			if ( isSlowSum ) {
		
				individualClass ="tkReviewSubmitSlowButton";
			} else 	if ( doReviewAgainOnSubmit ) {
	
				individualClass="tkReviewSubmitFastReviewButtonBottom";
			} else {

				individualClass="tkReviewSubmitFastGoButton";
			}			
		}

		
		if ( isSlowSum ) {
			
			out.println("<tr class='tkReviewNavRow'>");
			
			out.println("<td class='tkReviewNavCellWest tkReviewNavCellWestSlow tkReviewNavCell tkReviewNavCellTop'>");
			if ( !showActionsAtAll) {
	            out.println("<div class='tkReviewAllButton tkReviewButton'><a href=\"tkRequestFast\" class='tkReviewAllButton tkReviewButton'>" + sitz.lang().getEepTkActFullEdit() +"</a></div>");       	
			}
			out.println("</td>");	
			
			out.println("<td class='tkReviewNavCellCenter tkReviewNavCell' colspan='2'>");
			printTkPanelSubmit( out,submitLabel,individualClass);
			if ( showActionsAtAll) {
				//out.println("<br>"); 
				printTkPanelActions( out,  editAllTechnicallyPossible,"Slow");
			}
			out.println("</td>");
			
	
        


			out.println("</tr>");		
        
			
		} else  {
			out.println("<div class='tkReviewNav'>");
			
			out.println("<table class='tkReviewNavTable'>");
			
			out.println("<tr class='tkReviewNavRow'>");
			
			out.println("<td class='tkReviewNavCellWest tkReviewNavCellWestFast tkReviewNavCell'>");
			printTkPanelSubmit( out,submitLabel,individualClass);
			if (showActionsAtAll) {
				//out.println("<br>");
				printTkPanelActions( out,  editAllTechnicallyPossible,"Fast");	
			}
			out.println("</td>");	
			
			out.println("<td class='tkReviewNavCellCenter tkReviewNavCell'>");

			out.println("</td>");
			
			out.println("<td class='tkReviewNavCellEast tkReviewNavCell'>");
			out.println("</td>");		
	       	
	        out.println("</tr>");		
	        out.println("</table>");
	        out.println("</div>");        
		}
		

              
	}
	public void printTkPanelActions(PrintWriter out, boolean editAllTechnicallyPossible, String classAdderTkReviewActions) {
        out.println("<div class='tkReviewActions"+classAdderTkReviewActions+"'>");       	

		if ( editAllTechnicallyPossible ) {
            out.println("<div class='tkReviewAllButton tkReviewButton'><a href=\"tkRequestFast\" class='tkReviewAllButton tkReviewButton'>" + sitz.lang().getEepTkActFullEdit() +"</a></div>");       	
        }		        
        out.println("<div class='tkReviewSequButton tkReviewButton'><a href=\"tkRequestSequence\" class='tkReviewSequButton tkReviewButton'>" + sitz.lang().getEepTkActSequEdit() +"</a></div>");       		
		
    	out.println("<div class='tkReviewCancelButton tkReviewButton'><a href=\"tkCancel\" class='tkReviewCancelButton tkReviewButton'>" + sitz.lang().getEepTkActCancel() +"</a></div>");
    	out.println("</div>");   
	}
	
	public void printTkPanelSubmit(PrintWriter out, String submitLabel, 
			String indivdualClass ) {
		/*
		 * 		
		 */
	out.println("<table><tr><td>");  
		out.println("<div class='tkReviewSubmitButton tkReviewButton "+indivdualClass+"'><input  class='tkReviewSubmitButton tkReviewButton "+indivdualClass+"' type=\"submit\" value=\""+submitLabel+"\" ");
    	out.println("autofocus");
    	out.println("></div>");		
    out.println("</td><td>");	
		out.println("<div class='tkItemTipToggleTop'><a class='tkItemTipToggleTop' href=\"javascript:;\" onclick=\"tipToggleTopAll();\">&nbsp;&nbsp;??&nbsp;&nbsp;</a></div>");
	out.println("</td></tr></table>");
	}	
	/*
	public void sendMapV1 ( 
			PrintWriter out, 
			TkTicketManager ticketManager) throws Exception {
		out.println("<div class='tkReviewFields'>");
		out.println("<table class='tkReviewFieldsTable'>");
		Iterator<TkTicket> iterator = ticketManager.getTicketList().iterator();
		while ( iterator.hasNext() ) {
			
			TkTicket ticket = (TkTicket)iterator.next();

			if ( makeFast  )
				ticket.printHtmlDiv(out, false);
			else
				ticket.printHtmlDivSum(out); 
		
		}
		out.println("</table>");	
		out.println("</div>");			
		
	}
	
	public void sendMapSlowSum ( 
			PrintWriter out, 
			TkTicketManager ticketManager) throws Exception {

		Iterator<TkTicket> iterator = ticketManager.getTicketList().iterator();
		while ( iterator.hasNext() ) {
			
			TkTicket ticket = (TkTicket)iterator.next();

				ticket.printHtmlDivSum(out); 
		
		}
		out.println("</table>");	
		out.println("</div>");			
		
	}	
	*/ 
	public void sendMap ( 
			PrintWriter out, 
			TkTicketManager ticketManager,
			boolean fastForSure ) throws Exception {
		// ArrayList<Element> l = e.getChildren();
		//Sp.p("-D 01 -");
		boolean doReviewAgainOnSubmit = false;
		makeFast = false;
		//String okLabel="Confirm and go";
		if ( fastForSure ) {
			makeFast = true;
			//okLabel="Confirm and review";
			doReviewAgainOnSubmit = true;
		} else if ( surrounding.getTkMatcherFast() != null )
			makeFast = surrounding.getTkMatcherFast().doesMatch(ticketManager.tkid);
		makeFastOnPersonalRequest = fastForSure;
		//Sp.p("-D 02 -");
		out.println("<div class='tkReviewHeader'>" + 
				sitz.lang().getEepTkHeadReview()+"<br>"+TkTicket.htmlByEscaped(ticketManager.top_label)
				+"</div>");
		//Sp.p("-D 03 -");
		// out.println("<div class='tkItemTipToggleTop'><a class='tkItemTipToggleTop' href=\"javascript:;\" onclick=\"tipToggleTopAll();\">&nbsp;&nbsp;??&nbsp;&nbsp;</a></div>");

		out.println("<form action=\"tkOk\" id=\"main\" method=\"post\">"); 
		
		String toggleScriptTotal = "";
		
		if ( makeFast ) {
			boolean mf = !makeFast;
			if ( Commons.hasFlag(ticketManager.top_flag, "critical") )
				mf = false;
			printTkSumPanel(out,false,doReviewAgainOnSubmit,
					mf,false,false,true);
			

			Iterator<TkTicket> iterator = ticketManager.getTicketList().iterator();
			while ( iterator.hasNext() ) {
				
				TkTicket ticket = (TkTicket)iterator.next();
				
				if ( ticket.tktype.equals("msg"))
					ticket.printHtmlDiv(out, false);
				else if ( ticket.tktype.equals("accept"))
					ticket.printHtmlDiv(out, false);	
				else {
					out.print("<div class='tkMbrComplete'>");
/*
 * <a href="#" onclick="tipToggle('foo');">[ ? ]</a>					
 */					
					out.println("<div class='tkMbrLabel'>"+ticket.getLabelHtml()+
					"</div>"); 
					
					// resi
					out.println("<div class='tkMbrTipToggle'><a class='tkMbrTipToggle' href=\"javascript:;\" tabindex=\"500\" onclick=\"tipToggle('tip-"+ticket.getTicketId()+"');\">&nbsp;&nbsp;?&nbsp;&nbsp;</a></div>");
						toggleScriptTotal = toggleScriptTotal + "tipToggle('tip-"+ticket.getTicketId()+"');";					
					out.print("<div class='tkMbrEditor'>");
					ticket.printHtmlDivEditorOnly(out, false,"tkMbr");
					
					out.print("</div>");
					
					out.print("<div class='tkMbrTip' id='tip-"+ticket.getTicketId()+"' style='display:none;'>");
					out.print(TkTicket.htmlLfByEscaped (ticket.tip));
					out.print("</div>");
					out.print("</div>");
				}			
			}			
			
			printTkSumPanel(out,true,doReviewAgainOnSubmit,mf,false,true,false);
		} else {	
			/*
			String onload = 
					" <script type=\"text/javascript\">"
					+ "window.onload = (event) => {"
					+ "alert('hello onload');"
					+ ""
					+ ""
					+ "};"
					+" </script>";
					*/
			out.println(tkToggleSumScript);
			
			out.println("<div class='tkReviewFields'>");
			out.println("<table class='tkReviewFieldsTable'>");	
			//printTkSumPanel(out,false,doReviewAgainOnSubmit,!makeFast,true);
			boolean mf = !makeFast;
			if ( Commons.hasFlag(ticketManager.top_flag, "critical") )
				mf = false;
			printTkSumPanel(out,true,doReviewAgainOnSubmit,mf,true,false,true);
			Iterator<TkTicket> iterator = ticketManager.getTicketList().iterator();
			
			while ( iterator.hasNext() ) {
				
				TkTicket ticket = (TkTicket)iterator.next();
					// resi
				String scriptToggle = ticket.printHtmlDivSum(out); 
				if ( scriptToggle != null )
					toggleScriptTotal = toggleScriptTotal + scriptToggle;
			
			}	

			printTkSumPanel(out,true,doReviewAgainOnSubmit,mf,true,true,false);
			out.println("</table>");	
			out.println("</div>");			
			
		}
		

        out.println("</form>");	
        // resi hier das script fuer den top
        String tkToggleTopScript = 
				"<script type=\"text/javascript\">function tipToggleTopAll() {" 
        		+ toggleScriptTotal	
				+" }; </script>";
        out.println(tkToggleTopScript);		    
        
        
		/*
		if ( neustil ) {
			out.println("<div class='tkReviewFields'>");
			out.println("<div class='tkReviewTable'>");
			
			Iterator<TkTicket> iterator = ticketManager.getTicketList().iterator();
			while ( iterator.hasNext() ) {
				
				TkTicket ticket = (TkTicket)iterator.next();
				out.println("<div class='tkReviewRow'>");
					
				out.println("</div>");				
			
			}
			

			out.println("</div>");		
			out.println("</div>");	
			
		} else {
			
			
		}
*/
		
		
	
        
	}	

	private void talk(String x ) {
		// Sp.p("+ talk + "+x);
	}	
	
	private ReqResEe changeContext(
			HttpServletRequest request, 
			HttpServletResponse response)  {
		
		if ( sitz.getIP() == null ) {
			sitz.setIP(
					"Remote-Addr="+request.getRemoteAddr()+
					" Remote-Host="+request.getRemoteHost()+
					" Remote-Port="+request.getRemotePort()+
					" Remote-User="+request.getRemoteUser()+
					" Protocol="+request.getProtocol()+
					" Local-Addr"+request.getLocalAddr(),request.getRemoteHost()
					
					);
		}

		//System.out.println("b1");
		ReqRes reqRes = jobrunner.getReqRes();
		//System.out.println("b2");
		ReqResEe reqResEe = (ReqResEe)reqRes;
		//System.out.println("b3");
		talk("changing context ");
		//System.out.println("b4");
		reqResEe.changeContext(request, response);
		//System.out.println("b5");
		return reqResEe;
		
	}

	public  void addElementsForProjectDb( Element c1,
			HttpServletRequest request , boolean forceCommit, String _projectDir ) {
		
		// 2 props �bergeben ext_ons und ext_config...
		// String eONs = ;
		// String eConf = ;
		/*
		String username=null;
		if ( request != null ) {
			if ( request != null && request.getUserPrincipal()!= null &&
					request.getUserPrincipal().getName()!=null &&
					request.getUserPrincipal().getName().length() > 0 ) {
				username = request.getUserPrincipal().getName();
			}			
		}
*/
		/*
		Sp.p ("- 1 -: username ["+username+"]");
		if ( username != null ) {
			Element s1x = new Element("prop");			
			s1x.setAttribute("id","db_tomcat_user");
			s1x.setAttribute("x",username);
			c1.addContent(s1x);			
			
		} else {
			Element s1x = new Element("prop");			
			s1x.setAttribute("id","db_tomcat_user");
			s1x.setAttribute("x","");
			c1.addContent(s1x);			
		}
		
		String xtheuserdir = System.getProperty("user.dir");
		if ( theuserdir != null && theuserdir.length() > 0 ) {
			Element s1y = new Element("dir");			
			s1y.setAttribute("id","db_user_dir");
			s1y.setAttribute("x",theuserdir);
			c1.addContent(s1y);	
			Element s1y2 = new Element("dir");			
			s1y2.setAttribute("id","db_user_dir");
			s1y2.setAttribute("x","(dir,db_user_dir)dbfs/");
			c1.addContent(s1y2);					
		} else {
			Element s1y = new Element("dir");			
			s1y.setAttribute("id","db_user_dir");
			s1y.setAttribute("x","");
			s1y.setAttribute("flag","cool");
			c1.addContent(s1y);				
		}
		Sp.p ("- 1 -: userdir ["+theuserdir+"]");
*/
		Element s1 = new Element("prop");			
		s1.setAttribute("id","db_ons");
		s1.setAttribute("x",surrounding.extOn);
		c1.addContent(s1);
		
		// Sp.p ("- 2b -");
		Element s1b = new Element("prop");			
		s1b.setAttribute("id","db_bits");
		s1b.setAttribute("x",surrounding.extBits);
		c1.addContent(s1b);			
		
		// Sp.p ("- 3 -");
		Element s2 = new Element("prop");
		s2.setAttribute("id","db_conf");
		s2.setAttribute("x",surrounding.getCurrentProDbConfStringForServices(sitz));
		c1.addContent(s2);		
		
		// Sp.p ("- 4 -");
		Element s3 = new Element("prop");
		s3.setAttribute("id","db_pro");
		s3.setAttribute("x",projectId);
		c1.addContent(s3);	
		
		Element s3bx = new Element("prop");
		s3bx.setAttribute("id","force_commit");
		if ( forceCommit )
			s3bx.setAttribute("x","1");
		else
			s3bx.setAttribute("x","0");
		c1.addContent(s3bx);				
		
		 
		// 	Sp.p ("- 4b -");
			Element s3b = new Element("prop");
			s3b.setAttribute("id","db_pro_extension");
			if ( userInfo!= null && userInfo.getDbext() != null )
				// achtung nic123
				s3b.setAttribute("x",userInfo.getDbext());
			else 
				s3b.setAttribute("x","");
			c1.addContent(s3b);		
		
		
			// Sp.p ("- 5 -");
		Element s4 = new Element("dir");
		s4.setAttribute("id","db_pro");
		s4.setAttribute("x",_projectDir); // hier ein anderes dir hinein
		c1.addContent(s4);				
		
		// Sp.p ("- 6 -");
		Element s5 = new Element("prop");
		s5.setAttribute("id","db_app");
		s5.setAttribute("x",appid);
		c1.addContent(s5);		
		
		// Sp.p ("-  -");
		Element s6 = new Element("prop");
		s6.setAttribute("id","db_pro_commits");
		s6.setAttribute("x",dbApp.getCommitsPath());
		c1.addContent(s6);		
		
		Element s7 = new Element("prop");
		s7.setAttribute("id","db_pro_cache");
		s7.setAttribute("x",dbApp.getCachePath());
		c1.addContent(s7);			
		
	}
	
	public void doLogin2 ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		//System.out.println("a1");
		resetProject();
		//System.out.println("a2");
		talk("eeProject "+projectId+" received login request");
		//System.out.println("a3");
		ReqResEe reqResEe = changeContext(request, response);
		//System.out.println("a4");
		doPerformLogin(request,response);
		//System.out.println("a5");
		
	}
	
	public void doDecrypt ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {

		try {
			checkMyLogin();
			loadEncryptedProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pDecryptionProblem("-- decrypt -- problem -- projectid=["+projectId+"]");
			
		}

		doSeeInfo(request,response);

	}	
	
	public void doQuit1 ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		
		talk("eeProject "+projectId+" received quit1 request");
		

		ReqResEe reqResEe = changeContext(request, response);
		try {
			
			
			//if ( ! sitz.hasPrivDrop())
			//	throw new Exception("sorry");
			
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepLogoQuit(),true,response));
			out.println("<a class='Home' href='quit2'>" + sitz.lang().getEepMsgQuitClickHere() +"</a> ");
			if ( sitz.isLoggedIn()) {
				sitz.requestStorageStatus();
				out.println("<br>"+getNewMessagesAsHtml());
		        Charset cs = Charset.defaultCharset();
		        out.println("<br>"+"The default charset of the machine:" + cs.displayName());				
			}

	        
	        //out.println("<br>"+"oe=" + Ti_TerminalLink.getHtmlByTextFromEscaped("ö"));
			out.println("</body></html>");				
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.p("Could not write error");
			e1.printStackTrace();
		}					
	}	
	private void deleteSessionHandlers()  {
		deleteSessionHandlers2( sitz.getSessionHomeFs());
		deleteSessionHandlers2( sitz.getSessionHomeMem());
		
	}
	private void deleteSessionHandlers2(String del)  {
		try {
			if ( del != null)
				CommonsVfs.deleteDir(del,sitz);
		} catch (Exception e) {
			Sp.pProjectShutdownProblemDir(sitz,del,e);
			
		}
		
	}	
	public void doQuit2 ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		request.getSession().invalidate();
		Sp.ph("Did quit project with success!");
		/*
		resetProject();
		try {
			resetProjectMore();
		} catch (FileSystemException e2) {
			e2.printStackTrace();
		}
		talk("eeProject "+projectId+" received quit2 request");

		ReqResEe reqResEe = changeContext(request, response);
		
		EeSystem eeSystem = (EeSystem)request.getServletContext().getAttribute("eeSystem");

		boolean wasOk = false;
		try {
			
			Sp.ph("Going to quit project ...");
			if ( eeSystem != null )
				eeSystem.unRegisterNewProjects(this);

			request.getSession().invalidate();
			Sp.ph("Did quit project with success!");
			wasOk = true;
		} catch (Exception e) {
			Sp.ph("Problem in quitting a project: "+e.getMessage());
			e.printStackTrace();
		}	
		*/
		try {
			PrintWriter out = response.getWriter();
			out.println("<html><body>");

				out.println("<p>" + sitz.lang().getEepMsgQuitDid() +"</p>");
				out.println("<p><a href='go'>" + sitz.lang().getEepMsgQuitStart() +"</a></p> ");

			out.println("</body></html>");				
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Sp.p("Could not write error");
			e1.printStackTrace();
		}			
		
		
	}	
	
	public void clearTheCompleteMemory() {
		Sp.ph("Removing projectdir: "+projectDir);
		try {
			CommonsVfs.deleteDir(projectDir,sitz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.ph("Problem in Removing projectdir: "+projectDir);
			e.printStackTrace();
		}
		
		
		appid = null;
		dbApp = null;
		commitsPath = null;
		cachePath = null;	
		projectId = null;
		projectDir = null;
		jobrunner  = null;
		ticketManager = null;	
		ticket_to_choose = null;		
		terminator = null;
		seitenlinks = null;		
		interpreterParked  = null;
		reqResParked = null;
		ticketManagerParked = null;
		seitenlinksParked = null;
		surrounding = null;
		userInfo = null;
		ticketToEdit = null;	
		e = null;
		encryptedProject = null;
		
	}
	private void sessionDebug( String what ) {
		String id = ""+sessionId+"/"+projectId;
		Sp.pAccessSession(id);
		// System.out.println("SESSION "+what+" "+id); // Commons.getTsMs()+
	}
	public void shutDown() throws FileSystemException {
		sessionDebug("shutDown()");
		//System.out.println("eeProject.shutDown ("+sessionId+"/"+projectId);
		
		sitz.closeAllSessionCoops();
		talk("eeProject "+projectId+" received project-db-checkin request");
		
		String shutApp = appid;
		String shutProId = projectId;
		String shutProDir = projectDir;
		
		String shutExtension = "";
		// Parameters px = new Parameters(surrounding.extProDbConfString,"'");
		//System.out.println("Shooting "+shutApp+"."+shutProId+"."+shutProDir);
		/*
		if ( save && ( userInfo != null && e == null)){
			String db1 = surrounding.getDatabaseLink(theBenutzerProjektSitzung);
			System.out.println("sreaming out "+shutApp+":"+shutProId+" # "+db1);
			
			try {
				FileObject uri = CommonsVfs.getFileObjectByFilenameToRead(shutProDir,theBenutzerProjektSitzung);
				FileObject touri = CommonsVfs.getFileObjectByFilenameToWrite(db1,theBenutzerProjektSitzung);
				Element e0 = new Element("filters");
				System.out.println("cache="+dbApp.getCachePath());
				if ( dbApp.getCachePath() != null) {
					
					Element e1 = new Element("except");
					Element e2 = new Element("dir");
					e2.setAttribute("base_equals", dbApp.getCachePath().substring(0, dbApp.getCachePath().length()-1));//);
					e2.setAttribute("path_level_eq", "1");
					e1.addContent(e2);
					e0.addContent(e1);							
				}

				Sp.p("using selector "+Commons.toText(e0));
				FlexSpecs theFileSelector = new FlexSpecs(e0);
				CommonsVfs.grants(uri, theBenutzerProjektSitzung);
				CommonsVfs.grantsDelete(touri, theBenutzerProjektSitzung);
				touri.copyFrom(uri,theFileSelector);
				uri.close();
				touri.close();
				System.out.println("did well shutdown "+shutApp+":"+shutProId);
			} catch (FileSystemException e) {
				System.out.println("PROBLEM did well shutdown "+shutApp+":"+shutProId+e.getMessage());
			}			
			
		} else {
			System.out.println("User not logged on");
		}
		*/
		/*
		if ( userInfo!= null && userInfo.getDatabaseTerm() != null && surrounding.allowsButtonCheckin() ) {
			shutExtension= userInfo.getDatabaseTerm();
			if ( px.getParameter("db_dir") != null && px.getParameter("project_token")!=null ) {
				
				String db1 = px.getParameter("db_dir")+"p/"+px.getParameter("project_token")+"-"+shutExtension;
				System.out.println("sreaming out "+shutApp+":"+shutProId+" # "+db1);


			} else {
				System.out.println("not enout configuration");
			}
		} else {
			System.out.println("User not logged on");
		}*/
		resetProject();	
		deleteSessionHandlers();
		
		// aber tz
		clearTheCompleteMemory();
	
				
	}	
	
	private FileObject encryptedProject = null;
	
	private void checkMyLogin() throws FileSystemException {
		
		if ( sitz.isLoggedIn());
		else
			throw new FileSystemException("It seems courious, your off ...or... maybe the hour changed, please do login ;-)");		
	
	}
	
	private void copyMe2Me(FileObject fromDir, FileObject toDir, int level,CryptoBase cb, boolean encrypt ) throws Exception {
		FileObject[] files = CommonsVfs.getNinos(fromDir, sitz );
		
		long opnum = -1;
		if ( level == 0 )
			opnum = CommonsVfs.grantsUpdateFolderP1(toDir, sitz,"copyMe2Me");
		
		
		String fromDirUriString = fromDir.getName().getURI();
		String toDirUriString = toDir.getName().getURI();
		
		if ( files != null ) {
			for (int i=0; i<files.length; i++) {
				FileObject einFile =  files[i];
				String einFileBaseName = einFile.getName().getBaseName();
				if ( einFile.isFolder() && level==0 && einFileBaseName.equals("cache")) {
					System.out.println("cache auf level 0 ausgelassen");
				} if ( einFile.isFile()) {
					// copy wohin?
					String dasZielDir = toDirUriString+"/"+einFileBaseName;
					if ( encrypt)
						cb.encryptFile(einFile.getName().getURI(), dasZielDir,sitz);
					else
						cb.decryptFile(einFile.getName().getURI(), dasZielDir,sitz);
					
					
				} else {
					String zielUri = fromDirUriString +"/"+einFileBaseName;
					FileObject zielUriFo = CommonsVfs.constructNormalizedDirPerText(zielUri);
					copyMe2Me(einFile,zielUriFo,level+1,cb,encrypt);
				}
			}
		}
		if ( level == 0 )
			CommonsVfs.grantsUpdateFolderP2(toDir, sitz,opnum);
	}
	private void deleteEncryptedProject() throws FileSystemException, StorageLimitException {
		if ( encryptedProject != null ) {
			long opnum = CommonsVfs.grantsUpdateFolderP1(encryptedProject, sitz,"delEncryptedProj");
			encryptedProject.deleteAll();
			CommonsVfs.grantsUpdateFolderP2(encryptedProject, sitz,opnum);
			encryptedProject=null;			
		}
	}
	private void loadEncryptedProject() throws Exception {
		checkMyLogin();
		if ( e == null ){
			//System.out.println("keine crypto");
		} else {
			// project verschluesseln
			if ( encryptedProject == null ) 
				throw new Exception(sitz.lang().getEepMsgCryptNoProFound());
			/*[echnaton-crypto]*/	
			CryptoBase cb = new CryptoBase(e);
			
			FileObject projectFo = CommonsVfs.resolveFolderSilent(projectDir);
			System.out.println("Starting to produce glass "+encryptedProject.getName().getURI());
			copyMe2Me(encryptedProject,projectFo,0,cb,false);
			deleteEncryptedProject();
			System.out.println("did create decrypted-version glass: "+projectDir);

			cb = null;
			
		}
		
	}	
	public String prepareProjectForServices() throws Exception {
		if ( e == null ){
			//System.out.println("keine crypto");
			encryptedProject = null;
			return projectDir;
		} else {
			// project verschluesseln
			if ( encryptedProject != null ) {
				CommonsVfs.deleteDir(encryptedProject.getName().getURI(), sitz);
			}
			CryptoBase cb = new CryptoBase(e);
			
			FileObject projectFo = CommonsVfs.resolveFolderRead(projectDir,sitz);
			
			String encryptedProjectName = sitz.getSessionHomeFs()+
					"/crypto/" +
					Commons.randomAlphaNumeric(6);
			System.out.println("encrypted project will be: "+encryptedProjectName);
			
			encryptedProject = CommonsVfs.resolveFolderSilent(encryptedProjectName);
			// CommonsVfs.grantsDeleteFolder(encryptedProject, theBenutzerProjektSitzung);
			
			copyMe2Me(projectFo,encryptedProject,0,cb,true);
			
			System.out.println("did create encrypted-version silencio: "+encryptedProjectName);
			
			
			// kopieren und verschluesseln
			
			
			return encryptedProjectName;
		}
		
	}
	public void doProjectDbCheckin ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		talk("eeProject "+projectId+" received project-db-checkin request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			checkMyLogin();
			if ( ! sitz.hasPrivDbWrite())
				throw new Exception("sorry1");
			
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Element c1=new Element("conf");
			addElementsForProjectDb(c1,request,false,prepareProjectForServices());
		
			
			configListx.add(c1);			

			
			Interpreter interpreter = JobRunner.createInterpreter(
					surrounding.proDbCheckinJob, // "Anton", 
					true, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(false); 
			
			interpreter.setSurrounder(surrounding);

			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();

			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));				

		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}
	
	public void doFinalizeLogin ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		
		resetProject();
		talk("eeProject "+projectId+" received finalize db login request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			checkMyLogin();
			
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Element c1=new Element("conf");
			// addElementsForProjectDb(c1);
		
			
			configListx.add(c1);			

			Sp.pd("----------- Running first job: "+surrounding.firstJob);
			Interpreter interpreter = JobRunner.createInterpreter(
					surrounding.firstJob, // "Anton", 
					false, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(false); 
			
			interpreter.setSurrounder(surrounding);

			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();

			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));				
			Sp.pd("---b-------- Running first job: "+surrounding.firstJob);
		} catch (Exception e) {
			Sp.pd("---c-------- Running first job: "+surrounding.firstJob);
			haveError(e, request, response);
		}
		
	}		
	
	public void doProjectDbCheckout ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		
		talk("eeProject "+projectId+" received project-db-checkout request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			checkMyLogin();
			if ( ! sitz.hasPrivDbRead())
				throw new Exception("sorry2");
			
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Element c1=new Element("conf");
			addElementsForProjectDb(c1,request,false,prepareProjectForServices());
		
			
			configListx.add(c1);			

			
			Interpreter interpreter = JobRunner.createInterpreter(
					surrounding.proDbCheckoutJob, // "Anton", 
					true, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(false); 
			
			interpreter.setSurrounder(surrounding);

			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();

			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));				

		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}	
	
	//
	
	public void doProjectDb ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		talk("eeProject "+projectId+" received project-db request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			checkMyLogin();
			if ( ! sitz.hasPrivDbService()) {
				System.out.println("descriptor-sitzun="+sitz.getDescriptor());
				throw new Exception("sorry3, no privateDbService in desciptor: "+sitz.getDescriptor());
				
			}
			
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Element c1=new Element("conf");
			addElementsForProjectDb(c1,request,false,prepareProjectForServices());
		
			
			Sp.p ("- 7 -");
			configListx.add(c1);			

			
			Interpreter interpreter = JobRunner.createInterpreter(
					surrounding.proDbServiceJob, // "Anton", 
					true, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(false); 
			
			interpreter.setSurrounder(surrounding);

			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();

			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));				

		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}	
	
	public void doSeeSimplelogin ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		talk("eeProject "+projectId+" received info request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Interpreter interpreter = JobRunner.createInterpreter(
					"_olix_login", // "Anton", 
					false, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(headerAndFooter);
			interpreter.setSurrounder(surrounding);
			
			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();
			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));
		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}	
	
	public void doSeeToolbox ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		talk("eeProject "+projectId+" received info request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Interpreter interpreter = JobRunner.createInterpreter(
					"_olix_tools", // "Anton", 
					false, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(headerAndFooter);
			interpreter.setSurrounder(surrounding);
			
			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();
			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));
		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}	
	public void doSeeInfo ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		resetProject();
		talk("eeProject "+projectId+" received info request");

		ReqResEe reqResEe = changeContext(request, response);
		
		try {
			ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
					appid, 
					projectId, 
					projectDir,
					false,
					sitz);	
			
			Interpreter interpreter = JobRunner.createInterpreter(
					"??", // "Anton", 
					false, 
					appid, // "Test-Sanchez", 
					projectId, 
					projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
					null, 
					null, 
					Interpreter.SILENCE_NO, 
					configListx, 
					0, 
					true, 
					true,
					terminator,
					false,
					this,sitz,false);
			//interpreter.setIncludeHeaderAndFooter(headerAndFooter);
			interpreter.setSurrounder(surrounding);
			
			jobrunner = new JobRunner();
			seitenlinks = new ArrayList<Seitenlinks>();
			jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));
		} catch (Exception e) {
			haveError(e, request, response);
		}
		
	}
	
	public void doClickALink ( 
			HttpServletRequest request, 
			   HttpServletResponse response,
			   String marker ) {
		talk("eeProject "+projectId+" received click: '"+marker+"'");
		// nic123
		ReqResEe reqResEe = changeContext(request, response);
		

		//Sp.p("Performing marker for: "+marker);
		//Sp.p("Links was to search in with nrbOfPages "+seitenlinks.size());		
		Ti_TerminalLink found = null;
		
		Iterator<Seitenlinks> i = seitenlinks.iterator();
		int gefundener_seitennlink_index = -1;
		while ( i.hasNext() && found == null ) {
			Seitenlinks aktuell = (Seitenlinks)i.next();
			Iterator<Ti_TerminalLink> j = aktuell.tlList.iterator();
			gefundener_seitennlink_index++;
			
			while ( j.hasNext() && found==null) {
				
				Ti_TerminalLink tl = (Ti_TerminalLink)j.next();
				
				ArrayList<RefCode> refcodes = tl.getHtml().getRefcodes();
				if ( refcodes != null ) {
					Iterator<RefCode> fci = refcodes.iterator();
					while ( fci.hasNext() && found == null ) {
						RefCode refcode = (RefCode)fci.next();
						// Sp.p("Checking "+refcode.refcode);
						if ( refcode.refcode.equals(marker))
							found = tl;				
					}
				}			
			}			
		}
		
		if ( found != null ) {
			// System.out.println("gefundener_seitennlink_index="+gefundener_seitennlink_index);
			Seitenlinks aufheben = seitenlinks.get(gefundener_seitennlink_index);
			seitenlinks.remove(gefundener_seitennlink_index);
			seitenlinks.add(aufheben);
			Sp.p("Found Reactor: "+found.getLabel());
			Interpreter tocont = null;
			try {
				tocont = found.getInterpreterForReaction(marker, true,this,sitz);
			} catch (Exception e) {
				// es wird ein TkChooseException zur�ckgegeben wenn aus 
				// ein choose beendet wird
				tocont = null;
				if ( e instanceof TkChooseException ) {
					
					TkChooseException e2 = (TkChooseException)e;
					String myx = "Das ist ein TkChooseException type="
							+e2.choosenVarType+ ", value="
							+e2.choosenValue+", actiontype="+
							e2.actionType;
					Sp.p(myx);
					if ( e2.actionType.equals("choose")) {
						this.doConfirmChoose(
								request,
								response,
								e2.choosenVarType, 
								e2.choosenValue);						
					} else if ( e2.actionType.equals("download")) {
						try {
							performDownload(e2.choosenVarType, e2.choosenValue,response);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							haveError(e1, request, response);						

						}
						
					} else {
						haveError(new Exception("unknown type for "+myx+" not implemented yet"), request, response);						
						
					}


					
				} else
					Sp.p("Having a problem["+marker+"] : "+e.getMessage());
				
			}
			if (tocont != null ) {
				//Sp.p("VON A");
				if ( !statusIsChoose() ) 
					tocont.setSurrounder(surrounding);
				else
					tocont.setSurrounder(surrounding.getSurroundingChoose());
					//tocont.setIncludeHeaderAndFooter(headerAndFooter);
				jobrunner.runNewJob(tocont, (ReqRes)reqResEe);
			} else 
				Sp.p("Didnt receive an interpreter from link for ["+marker+"]");
		} else {
			Sp.p("Cant find link ["+marker+"]");
			haveError(new Exception(sitz.lang().getEepMsgTiOutOfService()), request, response);	
		}
			
	}		
	private String dirDownloadedZips = null;
	private int countDownloads = 0;
	void performDownload( String varTypeX, String valueNX,HttpServletResponse response ) throws IOException, StorageLimitException {
		Sp.p("Requested download: "+varTypeX+","+valueNX);
		String valueN;
		String titel;
		if ( varTypeX.equals("dir")) {
			if ( dirDownloadedZips== null ) {
				
				dirDownloadedZips = 
			Commons.addFsInCase(sitz.getSessionHomeFs())
				+"ProDown-"+Commons.randomAlphaNumeric(10)+"/";
			}
			countDownloads++;
			String tempFileName = dirDownloadedZips+countDownloads;
			Sp.p("Tempfile is: "+tempFileName);
			Zip.zip(tempFileName, valueNX, "",sitz);
			valueN= tempFileName;
			try {
				titel = Commons.getNameOfDir(valueNX)+".zip";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				titel = "your-data.zip";
			}
			
		} else {
			valueN = valueNX;
			titel = null;
		}
			
	
		FileObject fo = CommonsVfs.resolveFileRead(valueN,sitz);
		FileContent fc = fo.getContent();
		FileName fn = fo.getName();
		if ( titel == null ) {
			/*
			if ( fn.getBaseName().endsWith("_special.xsl")) {
				int lx = fn.getBaseName().length();
				int extension_position_start = lx - 15; 
				String extension = fn.getBaseName().substring(extension_position_start, extension_position_start+3);
				titel = fn.getBaseName().substring(0, extension_position_start)+"."+extension;
				
				
			} else */
				titel = fn.getBaseName();
		}
		
			
		

			String extension = fn.getExtension().toLowerCase();	
			String contentType;
			if ( varTypeX.equals("dir") )
				contentType="application/zip";
			else
				contentType = getDownloadContentType( extension );
			response.setContentType(contentType);
			response.setContentLength((int) fc.getSize());
			response.setHeader("Content-disposition","attachment; filename="+titel);
			
			OutputStream out = response.getOutputStream();
			InputStream in = fc.getInputStream();
			
		    try {
				byte[] buffer = new byte[4096];
				int length;
				while ((length = in.read(buffer)) > 0) {
				    out.write(buffer, 0, length);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if ( in != null)
						in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				try {
					if ( out != null)
						out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		
	}
	private String getDownloadContentType( String extension ) {

		if ( extension.equals("txt")) return "text/plain";
		else if ( extension.equals("htm") || extension.equals("html")) return "text/HTML";
		 
		else if ( extension.equals("doc") || extension.equals("docx")|| extension.equals("rtf")) return "application/msword";
		else if ( extension.equals("xml") || 
				  extension.equals("xrs") ||
				  extension.equals("xrj") ||
				  extension.equals("ljx") || extension.equals("xsl") ||
				  extension.equals("xsj") ) return "application/xml";
		else if ( extension.equals("zip") ) return "application/zip";
		else if ( extension.equals("tar") ) return "application/x-tar";
		else if ( extension.equals("xls") || extension.equals("xlsx")) return "application/x-msexcel";
		else if ( extension.equals("jpg") || extension.equals("jpeg")) return "image/jpeg";
		else if ( extension.equals("gif") ) return "image/gif";
		else if ( extension.equals("png") ) return "image/png";
		else if ( extension.equals("bmp") ) return "image/bmp";
		else if ( extension.equals("pdf") ) return "application/pdf";
		else if ( extension.equals("js") ) return "application/javascript";
		else if ( extension.equals("json") ) return "application/json";
		return "application/octet-stream";
		/*
		else if ( extension.equals("htm") || extension.equals("html")) return "text/HTML";
		else if ( extension.equals("htm") || extension.equals("html")) return "text/HTML";
		else if ( extension.equals("htm") || extension.equals("html")) return "text/HTML";
		else if ( extension.equals("htm") || extension.equals("html")) return "text/HTML";
		*/
	}
	boolean statusIsChoose() {
		return (isParked);
		// return (tkStatus == TK_DOING_CHOOSE);
	}
	
	private boolean mustHaveLogin = false;
	private String e = null;


	

	
	public void doBackgroundLoginPdB(String userName__, String userPwd) throws Exception {
		String userName = userName__.toLowerCase();
		if ( userName != null && userName.trim().length() > 0 && userPwd != null && userPwd.trim().length()> 0 ) {
			DbLoginResponse dlr = _eeSystem.isValidDbUser(userName, userPwd,sitz);
			if ( !dlr.isHasAccess()) {
				throw new Exception(sitz.lang().getEepLoginBadUser());
			}
			userInfo = new UserInfo(userName,null,null,0);
			at.nicolas.x.web.OnlineSitzung os = (at.nicolas.x.web.OnlineSitzung)sitz;
			Sp.p("YYZ - - 3b");
			os.setUserInfo(userInfo,null);

			Sp.p("YYZ - - 4");
			// doProjectDb(request, response);
			
		}

		
		
	}
	public void doReactLogin ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		try {
			
			changeContext(request, response);
			// MyRequestor mr = new MyRequestor(request);
			//String form_type = request.getParameter("t");
			//Sp.p(" form_type="+form_type);
			//String form_password = request.getParameter("p");
			//Sp.p(" form_password="+form_password);
			String form_n__ = request.getParameter("n");
			String form_n = form_n__.toLowerCase();
			Sp.p(" form_n="+form_n);
	
			String form_c__ = request.getParameter("c");
			String form_c = form_c__.toLowerCase();
			Sp.p(" form_c="+form_c);
			

			String form_ncp = request.getParameter("ncp");

			
			if ( form_ncp == null || form_ncp.length() == 0 ) {
				throw new Exception(sitz.lang().getEepLoginNeedPwd());
			}
			
			String form_multi = request.getParameter("multi");
			//System.out.println("multi=["+form_multi+"]");
			if ( form_multi !=null && form_multi.equals("on")) {
				// app-login-durchfuehren
				String form_role = request.getParameter("role");
				if ( form_role == null || form_role.trim().length() == 0 )
					form_role="admin5";
				else
					form_role = form_role.trim().toLowerCase();
				sitz.performUserLogin(form_n, form_ncp, form_role, appid, surrounding);
				sitz.setLoggedIn(true);
				//System.out.println("did login for app "+appid);
				// auch die daten zum menue loeschen
				String zumLoeschen = sitz.getSessionHomeFs()+"headers/"; // +appid+"/";
				if ( CommonsVfs.existsDir(zumLoeschen, sitz))
					CommonsVfs.deleteDir(zumLoeschen, sitz);
			}
			DbLoginResponse dlr = null;
			boolean istNamen = true;
			if ( form_n != null && form_n.length() > 0 ) {
				dlr = _eeSystem.isValidDbUser(form_n, form_ncp,sitz);
				// nic123 da wurden jetzt die eingabedaten geprüft
				if ( !dlr.isHasAccess()) {
					throw new Exception(sitz.lang().getEepLoginBadUser());
				} 
				istNamen = true;
				
				/*
				if ( allPwdUserDatabase.indexOf(" "+form_n+"="+form_ncp+" ") < 0 )
					throw new Exception("you are a bad user and you dont know it");
					*/
			} else if ( form_c != null && form_c.length() > 0 ) {
				istNamen = false;
				dlr = _eeSystem.isValidDbCollection(form_c, form_ncp,sitz);
				if ( !dlr.isHasAccess()) {
					throw new Exception(sitz.lang().getEepLoginBadColl());
				}
				/*
				if ( allPwdCollection.indexOf(" "+form_c+"="+form_ncp+" ") < 0 )
					throw new Exception("you are a bad user and you dont know it");
					*/
			} else
				throw new Exception(sitz.lang().getEepLoginMissing());
			
			// hier haett ich jetzt die groesse, bei -1 wird der default-wert genommen
			long users_db_size = dlr.getSize();
			String form_db__ = request.getParameter("d");
			String form_db = form_db__.toLowerCase();
			Sp.p(" form_db="+form_db);
			
			// hier jetzt das verzeichnis erzeugen
			
			e = request.getParameter("e");
			final String e2 = request.getParameter("e2");
			if ( e!= null && e.length()> 0 ) {
				if ( !projectDir.startsWith("ram:///") )
					throw new Exception(sitz.lang().getEepLoginMsgCryptSave());
				if ( e2 == null || e2.length() == 0 )
					throw new Exception(sitz.lang().getEepLoginMsgCryptSecond());
				if ( !e.equals(e2))
					throw new Exception(sitz.lang().getEepLoginMsgCryptSame());
			}
			if ( e != null && e.length()==0)
				e=null;
				
			Sp.p("X - - 3");

			// das passwort ist richtig
			userInfo = new UserInfo(form_n,form_c,form_db,users_db_size);
			at.nicolas.x.web.OnlineSitzung os = (at.nicolas.x.web.OnlineSitzung)sitz;

			
			Sp.p("X - - 3b");
			os.setUserInfo(userInfo,request);
			
			if ( istNamen ) {
				System.out.println("XXXXXX dburi="+os.getDatabaseLinkUriDir());
				File f = new File (os.getDatabaseLinkUri().substring(8));
				if ( !f.exists()) {
					if ( ! f.mkdir() )
						System.out.println("Error in creating the directory '"+os.getDatabaseLinkUri());
						//throw new IOException  ( "Error in creating the directory '"+os.getDatabaseLinkUri())+"'.")
						//;
					else
						System.out.println("did create user-dir '"+os.getDatabaseLinkUri());
				
				} else 
					System.out.println("user-dir exists already '"+os.getDatabaseLinkUri());
				/*
				Commons.cutDir(dir, len)
				FileObject fix = CommonsVfs.resolveFolderSilent(os.getDatabaseLinkUriDir());
				
				try {
					fix.createFolder();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
				
			}			

			Sp.p("X - - 4");
			doProjectDb(request, response);

			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Sp.p("X - - 8");
			haveError(e, request, response);
		}
		//Sp.p("X - - 9");
	}		
	public void doPerformLogin (
			   HttpServletRequest request, 
			   HttpServletResponse response) {
		
		
		
	   	try {
	   		//checkMyLogin();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepLogoLogin(),false,response));
			//out.println("<p>" + sitz.lang().getEepLoginTxtHeadline() +"</p>");
			// out.println("<div class='tkDetailTop'>"+TkTicket.html(tm.top_label)+"</div>");
			out.println("<form action=\"PdBLogin\"  method=\"post\">");
			//out.println("<div class='tkDetailData'>");
			// out.print("Username");
			/*
			out.print("LoginName <input size=\"20\" type='text' name='l' /><br/>");
			out.print("Password <input size=\"20\" type='text' name='p' /><br/><br/>");			
			out.print("Displayed Username <input size=\"20\" type='text' name='n' /><br/>");

			out.print("UserType <input size=\"20\" type='text' name='t' /><br/>");
			*/
			out.println("<table class=\"PdB\">");
			
			out.println("<tr>");
				out.println("<td colspan=\"2\" class=\"PdBHead\">");
				out.println(sitz.lang().getEepLoginTxtHeadline() );
				out.println("</td>");				
			out.println("</tr>");				
			

			
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtUser());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='text' name='n' />");
				out.println("</td>");			
			out.println("</tr>");
			
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtPwd());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='password' name='ncp' />");
				out.println("</td>");			
			out.println("</tr>");	
			
			out.println("<tr>");
				out.println("<td>");
				out.println("Multi-Login");
				out.println("</td>");
				out.println("<td>");
				
				out.println("<input type=\"checkbox\" name=\"multi\" ");
				//System.out.println("sitz.isLoggedIn = "+sitz.isLoggedIn());
				if (!sitz.isLoggedIn())
					out.println("checked");
				out.println(">");
				
				out.println("</td>");			
			out.println("</tr>");		
			
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtStorage());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='text' name='d' />");
				out.println("</td>");			
			out.println("</tr>");			
			// da die buttons auch
			
			// submit button
			out.println("<tr>");
				out.println("<td>");
				out.println("</td>");
				out.println("<td>");
		        out.println("<input class='dbSubmit' type=\"submit\" value=\"" + sitz.lang().getEepLoginTxtSubmit() +"\"");
		        
	
				out.println(">");
				out.println("</td>");			
			out.println("</tr>");				
			
			// das ist die überschrift
			out.println("<tr>");
				out.println("<td colspan=\"2\" class=\"PdBHead\">");
				out.println(sitz.lang().getEepLoginTxtWeiters() );
				out.println("</td>");				
			out.println("</tr>");
			// die benutzer-gruppe
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtCollection());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='text' name='c' />");
				out.println("</td>");			
			out.println("</tr>");	
			// die rolle bei mulit-login-role
			out.println("<tr>");
				out.println("<td>");
				out.println("Rolle/Multi-Login");
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='text' name='role' />");
				
				out.println("</td>");			
			out.println("</tr>");	
		
		
		

			
			// überschrift projektverschluesselung
			out.println("<tr>");
				out.println("<td colspan=\"2\" class=\"PdBHead\">");
				out.println(sitz.lang().getEepLoginTxtPasswords());
				out.println("</td>");				
			out.println("</tr>");	


		
			
			// passwort 1
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtEncryptor1());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='password' name='e' />");
				out.println("</td>");			
			out.println("</tr>");
		
			// passwort 2 1
			out.println("<tr>");
				out.println("<td>");
				out.println(sitz.lang().getEepLoginTxtEncryptor2());
				out.println("</td>");
				out.println("<td>");
				out.println("<input size=\"20\" type='password' name='e2' />");
				out.println("</td>");			
			out.println("</tr>");
			
			// submit-button
			out.println("<tr>");
				out.println("<td>");
				out.println("</td>");
				out.println("<td>");
		        out.println("<input class='dbSubmit' type=\"submit\" value=\"" + sitz.lang().getEepLoginTxtSubmit() +"\"");
		        

				out.println(">");
				out.println("</td>");			
			out.println("</tr>");			
		

			out.println("</table>");			
			/*
			out.println(sitz.lang().getEepLoginTxtUser() +" <input size=\"20\" type='text' name='n' /><br/>");
			out.println(sitz.lang().getEepLoginTxtCollection() +" <input size=\"20\" type='text' name='c' /><br/>");	
			out.println(sitz.lang().getEepLoginTxtStorage() +" <input size=\"20\" type='text' name='d' /><br/>");
			out.println("<p>" + sitz.lang().getEepLoginTxtPasswords() +"</p>");
			out.println(sitz.lang().getEepLoginTxtPwd() +" <input size=\"20\" type='password' name='ncp' /><br/>");			
			out.println(sitz.lang().getEepLoginTxtEncryptor1() +" <input size=\"20\" type='password' name='e' /><br/>");
			out.println(sitz.lang().getEepLoginTxtEncryptor2() +" <input size=\"20\" type='password' name='e2' /><br/>");
			 */
			

			//ut.println("</div>");

			
	        //out.println("<a href=\"tkCancelIp\">Stop</a>");
	        out.println("</form>");				
			
			out.println(getHtmlFooter());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Sp.p("Could not write error");
			e1.printStackTrace();
		}			
		

		
	}
	public void doTheNextThing(
		   HttpServletRequest request, 
		   HttpServletResponse response )  {
		Sp.p("wir sind im do the next thing");
		
		try {
			Sp.p("Haltestelle 2");
			
			if ( terminator == null ) {
				Sp.p("Haltestelle 3");
				terminator = new Tc_Terminator(false,this,sitz);
				if ( terminator == null )
					Sp.pl("der terminator ist peinlicherweise null");
				Sp.p("Haltestelle 4");
				// set modus
				terminator.setupDone();
				Sp.p("Haltestelle 5");

				ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
						appid, 
						projectId, 
						projectDir,
						false,
						sitz);	
				Sp.p("Haltestelle 6");
				//da muessen wir uns reinhaengen
				// ?? surrounding !!
				Sp.p("Haltestelle 7");


				if ( false ) { // checkForLogin && Commons.hasFlag(surrounding.extBits,"mustLogin")) {
					// muss das login machen 
					// doPerformLogin(true,request,response);
				} else {
					Interpreter interpreter = JobRunner.createInterpreter(
							surrounding.firstJob, // "Anton", 
							false, 
							appid, // "Test-Sanchez", 
							projectId, 
							projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
							null, 
							null, 
							Interpreter.SILENCE_NO, 
							configListx, 
							0, 
							true, 
							true,
							terminator,
							false,
							this,sitz,false);
					//interpreter.setIncludeHeaderAndFooter(headerAndFooter);
					interpreter.setSurrounder(surrounding);


					jobrunner = new JobRunner();
					seitenlinks = new ArrayList<Seitenlinks>();
					jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));					
				}
						

			}
		} catch (Exception e) {
			haveError(e,request,response);
		}
		
	}
	
	public void doTheNextThingNachLogin(
			   HttpServletRequest request, 
			   HttpServletResponse response )  {
		//Sp.p("y - - - 1");
			try {
				
				//Sp.p("y - - - 2");
				if ( terminator == null ) {
					//Sp.p("y - - - 3");
					terminator = new Tc_Terminator(false,this,sitz);
					// set modus
					terminator.setupDone();

					ArrayList<Element> configListx = CoreBuilder.db.getExecutionPrologUsage(
							appid, 
							projectId, 
							projectDir,
							false,
							sitz);	
					//Sp.p("y - - - 4");
						Interpreter interpreter = JobRunner.createInterpreter(
								surrounding.firstJob, // "Anton", 
								false, 
								appid, // "Test-Sanchez", 
								projectId, 
								projectDir, // "C:\\Users\\nicolas\\Documents\\_temp\\systemp\\projekt", // projectdir 
								null, 
								null, 
								Interpreter.SILENCE_NO, 
								configListx, 
								0, 
								true, 
								true,
								terminator,
								false,
								this,sitz,false);
						//interpreter.setIncludeHeaderAndFooter(headerAndFooter);
						interpreter.setSurrounder(surrounding);


						jobrunner = new JobRunner();
						seitenlinks = new ArrayList<Seitenlinks>();
						//Sp.p("y - - - 5");
						jobrunner.runNewJob(interpreter, new ReqResEe(request,response,this));	
						//Sp.p("y - - - 6");
					
							

				}
			} catch (Exception e) {
				//Sp.p("y - - - 7");
				haveError(e,request,response);
			}
			//Sp.p("y - - - 8");
			
		}
	public  void haveError ( Exception e, 
			HttpServletRequest request, 
		   HttpServletResponse response )  {
		
	   	try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepLogoError(),false,response));
			out.println("<p>" + sitz.lang().getEepLogoError2() +" "+e.getLocalizedMessage()+"</p>");			
			out.println(getHtmlFooter());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Sp.p("Could not write error");
			e1.printStackTrace();
		}		
		
	}

	String sessionId = "";
	ISitzung sitz;
	public void setSitzung( ISitzung theSitzung ) throws FileSystemException, StorageLimitException {
		sitz = theSitzung;
		//System.out.println("--- 1 ---");
		CommonsVfs.mkdirCascade(projectDir,sitz);
	}
	EeSystem _eeSystem;
	static String tkToggleSumScript = null;
	private void createOneTimers() {
		if ( tkToggleSumScript == null ) {
			tkToggleSumScript = 
				"<script type=\"text/javascript\">window.onload = (event) => {" +
						//alert('hello onloadx');
				"var tkItems = document.getElementsByClassName(\"tkItem\");" +
				//alert('nbr of tkItems: '+tkItems.length);
				"var lowestLen = 1000;" +
				"for ( i = 0; i < tkItems.length; i++) {" +
				"var childs =tkItems[i].children;" +	
				//alert('tkItems ['+i+'] has nbr of childs: '+childs.length);
				"var sumLen=0;" +
				"for ( j = 0; j < childs.length; j++) {" +
				//alert('tkItems ['+i+'] - child['+j+'] has class '+ childs[j].getAttribute('class')+ ' and width :'+childs[j].getBoundingClientRect().width+' = '+childs[j].offsetWidth);
				"if ( (childs[j].getAttribute('class') == 'tkItemValue') ||" +
				"(childs[j].getAttribute('class') == 'tkItemValue') || " +
				"(childs[j].getAttribute('class') == 'tkItemAdd')) {" +
				"sumLen = sumLen + childs[j].getBoundingClientRect().width" +
				"}" +
				"}" +
				"var sumLen2 = Math.trunc(sumLen);" +
				//alert('!!! tkItems ['+i+'] hat breite: '+sumLen+' = ' +sumLen2);
				"if ( sumLen2 < lowestLen )" +				
				"lowestLen = sumLen2;" +
				"}" +
				"var theWidth = lowestLen+'px';" +				
				//alert('xxxxx lowestLen ['+lowestLen+'] = ' +theWidth);
				"var tkTips = document.getElementsByClassName(\"tkItemTipContent\");" +
				"for ( k = 0; k < tkTips.length; k++) {" +
				"tkTips[k].style.width = theWidth;" +						    
				"}" +						    
				"}; </script>";						 
						 
		}
		
	}
	public EeProject (	
				EeSystem eeSystem,
				String _xrapp,
				DbApp _dbApp,
				String _projectId,
				HttpSession _httpSession,
				Surrounding surrounding,
				String sessionId ) throws Exception {
		// Sp.p("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj dbApp="+_dbApp);
		if ( _dbApp == null ) Sp.p("die app ist ist ist null");
		_eeSystem = eeSystem;
		
		/*[echnaton-timer]*/
		/*[echnaton-customer]*/
		/*
		 * ,
				ISitzung benutzerProjektSitzung
				
				theBenutzerProjektSitzung = benutzerProjektSitzung;
		 */
		
		createOneTimers();
		this.dbApp = _dbApp;
		this.commitsPath = dbApp.getCommitsPath();
		this.cachePath = dbApp.getCachePath();
		
		// this.httpSession = _httpSession;
		this.surrounding = surrounding;
		this.sessionId = sessionId;
		//Sp.p("eeProject received surround "+this.surrounding.getDescription());
		appid = _xrapp;
		// this.eeSystem = eeSystem;
		projectId = _projectId;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String date2 = dateFormat.format(date);
		
		
		DateFormat dateFormat5 = new SimpleDateFormat("yyyyMMdd");
		String date5 = dateFormat5.format(new Date());		
		
		DateFormat dateFormat2 = new SimpleDateFormat("HH-mm-ss");
		String date3 = dateFormat2.format(date);
		
		DateFormat dateFormat4 = new SimpleDateFormat("HHmm");
		String date4 = dateFormat4.format(date);		
		
		String projectPath = "ram:///userdata/";
		if ( this.surrounding.extHardProject )
			projectPath = Commons.constructNormalizedDir(eeSystem.getTomcatTempDirForProject(sessionId)+"pros/");

	
		
		String projectLocation = date5+"/"+Commons.randomAlphaNumeric(5)+"/"+date4+"/"+appid+"-Proj/";
		/*
				
				appid+FileName.SEPARATOR+date2+FileName.SEPARATOR+
				 date3+FileName.SEPARATOR+"pro."+projectId+FileName.SEPARATOR; */
		Sp.p("Home of projects is ["+projectPath+"] and [ "+projectLocation+"]");
		if ( this.surrounding.projectDir != null ) {
			//Sp.p("Halo A");
			if ( this.surrounding.cloneProjectDir ) {
				//Sp.p("Halo B");
				// clone
				projectDir = 
						projectPath+projectLocation;
				//Commons.mkdirCascade(projectDir,"eeProject");
				Sp.p("Project-Dir not created yet created x: "+projectDir);
				FileObject uri = CommonsVfs.resolveAnySilent(this.surrounding.projectDir);
				FileObject touri = CommonsVfs.resolveAnySilent(projectDir);
				
				long opnum = CommonsVfs.grantsUpdateFolderP1(touri, sitz,"eeProject-Clone") ;
				try {
					touri.copyFrom(uri, new FileSelectorAll());
				} catch (Exception e1) {
					Sp.pe(e1);					
					throw e1;
				} finally {
					if ( uri!=null) uri.close();
					if ( touri!=null) touri.close();		
					CommonsVfs.grantsUpdateFolderP2(touri, sitz,opnum) ;	
				}
						
			} else {
				// not clone live
				//Sp.p("Halo C");
				projectDir = this.surrounding.projectDir;
				
			}
		} else {
			//Sp.p("Halo D");
			projectDir = // tomcatTempDir+
					projectPath+projectLocation;

			Sp.p("Project-Dir created y: "+projectDir);			
			
		}
		Sp.ph("Project-Dir is: "+projectDir);
	
		/*[echnaton-timer]*/

		// Sp.p("Halo E, projectDir is="+projectDir);

		/*
		File f = new File(projectDir);
		if ( f.isDirectory() && f.exists());
		else
			f.mkdirs();
			*/
		// Sp.p("+ project '"+projectId+"' in: "+projectDir);
		
	}
	
	/*
	public String getXrapp() {
		return xrapp;
	}*/
	
	
	public void runJob( String jobtodo ) throws JDOMException, IOException, Exception {
		Interface.runJob(jobtodo, appid, projectId, projectDir);

	}

	public String getProjectId() {
		return projectId;
	}

	public String getProjectDir() {
		return projectDir;
	}

	@Override
	public boolean chooseFileOn() {

		
		return ( isParked && (parkType == PARK_CHOOSE_FILE ));
	}
	@Override
	public boolean chooseDirOn() {
		return ( isParked && (parkType == PARK_CHOOSE_DIR ));

	}
	@Override
	public boolean seeFileFunctions() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean seeDirFunctions() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isSocalledWeb() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	public void treatTkResponse(XrRqRpTk answerTk,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		try {
			PrintWriter out = response.getWriter();
			// print before
			//Sp.p("A0");
			if ( answerTk.getTranslatedForPrint() != null ) {
				//Sp.p("A1");
			if ( answerTk.getTranslatedForPrint().getChildren().size()>0) {
				//Sp.p("A2");
				boolean flag_print_before = false;
				// boolean flag_print = false;
				boolean flag_print_tips_only = false;
				boolean flag_print_with_tips = false;
				boolean flag_print_all_fields = false;
				boolean flag_print_all_tested_fields = false;
				flag_print_before = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print_before");
				// flag_print = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print");
				flag_print_tips_only = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print_tips_only");
				flag_print_with_tips = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print_with_tips");
				flag_print_all_fields = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print_all_fields");	
				flag_print_all_tested_fields = Commons.hasFlag(answerTk.getTranslatedForPrint(), "print_all_tested_fields");
				// Sp.p("A3");
				if ( flag_print_before ) {
					// Sp.p("A4");
					// inperformant
					//System.out.println("cc cccccccccc print_tk ");
					terminator.print_tk(
							answerTk.getTranslatedForPrint(), 
							answerTk.loglevel, 
							answerTk.ticketManager.tiHandler, 
							"before edit", 
							answerTk.app, 
							answerTk.appdir,
							answerTk.inst,
							answerTk.inst_dir,
							flag_print_with_tips,flag_print_tips_only,flag_print_all_fields,flag_print_all_tested_fields);
					//Sp.p("A5");
				}			
			}		
			}
			//Sp.p("A6");
			// hier muss man sich reinh�ngen
			// hier hat ein interpeter einen TK zur verarbeitung zurueckgebracht
			// alles steckt in answerTk
			if ( answerTk.ticketManager.do_first_round ) {
				
				
				answerTk.ticketManager.startIterator();
				TkTicket firstRoundTicket = answerTk.ticketManager.continueIterator();
				if ( firstRoundTicket != null ) {
					status_OUTIP_TRY();
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+TkTicket.htmlByEscaped(firstRoundTicket.label),true,response));
					sendSingleInputIp(out,firstRoundTicket,answerTk.ticketManager,sitz.lang());
					status_OUTIP_COMMIT(answerTk.ticketManager);
					
				} else if ( answerTk.ticketManager.flag_sum ){
					// da bits garnichts
					status_OUT_TRY();

					out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(answerTk.ticketManager.top_label),true,response));
					sendMap(out, answerTk.ticketManager,false);
					out.println(getHtmlFooter());
					status_OUT_COMMIT(answerTk.ticketManager);						
				} else {
					// da ist ueberhaupt nichts zu tun
					// weiter im programm
					Sp.p("X-1");
					// printMaybeTkEnd();
					// das geht nicht bei Tk-General.msg
					Sp.p("X-2");
					jobrunner.continueAfterTk();
				}
				
			} else {
				//Sp.p("-C 01 -");
				answerTk.ticketManager.setupSUMONLY();
				//Sp.p("-C 02 -");
				status_OUT_TRY();
				//Sp.p("-C 03 -");

				out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(answerTk.ticketManager.top_label),true,response));
				//Sp.p("-C 04 -");
				sendMap(out, answerTk.ticketManager,false);
				//Sp.p("-C 05 -");
				out.println(getHtmlFooter());
				//Sp.p("-C 06 -");
				status_OUT_COMMIT(answerTk.ticketManager);			
				//Sp.p("-C 07 -");
			}

			

			
		} catch (Exception e) {
			haveError(e, request, response);
		}		
	}
	
	public void doTkRequestSequence(
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		try {
			PrintWriter out = response.getWriter();

			//System.out.println("doTkRequestSequence doTkRequestSequence doTkRequestSequence");
			//System.out.println("doTkRequestSequence doTkRequestSequence doTkRequestSequence");
			
				ticketManager.resetNewIteratorForSequence();
				ticketManager.startIterator();
				TkTicket firstRoundTicket = ticketManager.continueIterator();
				
				if ( firstRoundTicket != null ) {
					System.out.println("firstRoundTicket != null");
					//status_OUTIP_TRY();
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+TkTicket.htmlByEscaped(firstRoundTicket.label),true,response));
					sendSingleInputIp(out,firstRoundTicket,ticketManager,sitz.lang());
					//status_OUTIP_COMMIT(ticketManager);
					
				} else {
					// da bits garnichts
					//status_OUT_TRY();
					//System.out.println("firstRoundTicket == null");
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
					sendMap(out, ticketManager,false);
					out.println(getHtmlFooter());
					//status_OUT_COMMIT(ticketManager);						
				} 
				
			

			

			
		} catch (Exception e) {
			haveError(e, request, response);
		}		
	}	
	
	public void doTkOkIp ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		try {

			changeContext(request, response);
			
			PrintWriter out = response.getWriter();
			//Sp.p("vor aufruf get last");
			TkTicket last = ticketManager.getLastTicket();
			//Sp.p("nach aufruf get last "+last);
			TkValidationError problem = null;
			if ( !last.isMsg) {
				last.reflectToMemory(new MyRequestor(request));
				//Sp.p("nach refleckt");
				problem = last.validate();

			}
			if ( problem != null ) {
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoProblem()+TkTicket.htmlByEscaped(last.label),true,response));
				sendTkError(out, problem);
				out.println("<hr/>");
				sendSingleInputIp(out,last,ticketManager,sitz.lang());
				out.println(getHtmlFooter());
			} else if ( ticketManager.has_accept ) {
				doTkOk2(request, response);
			} else {

				TkTicket firstRoundTicket = ticketManager.continueIterator();
				//Sp.p("nach continue");
				if ( firstRoundTicket != null ) {
					//Sp.p("etwas gefunden");
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+TkTicket.htmlByEscaped(firstRoundTicket.label),true,response));
					sendSingleInputIp(out,firstRoundTicket,ticketManager,sitz.lang());
					
					out.println(getHtmlFooter());
				} else {
					// wir sind fertig und gehen womoeglich in sum
					//Sp.p("nichts mehr da");
					if ( ticketManager.flag_sum ) {
						// Sp.p("da gibts ein sum");
						isInFirstRound = false;
					

						//Sp.p("da sind wir");
						out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
						sendMap(out, ticketManager,false);
						out.println(getHtmlFooter());
						//Sp.p("passt");
					} else {
						// kein sum		
						// hier muss statusmaessig das tk-beendet werden
						//Sp.p("da gibts kein sum");
						doTkOk2(request, response);
					}
						
				}				
			}
			
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			haveError(e, request, response);
		}
	}	
	
	public void doTkCancelIp ( 
			HttpServletRequest request, 
			   HttpServletResponse response ) {
		//Sp.p("bin im doTkCancelIp");
		changeContext(request, response);
		boolean cont = true;
		try {
			status_CANCEL_TRY();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			cont= false;
			haveError(e, request, response);
			
		}
		
		if ( cont ) {
			status_CANCEL_COMMIT();
			jobrunner.continueAfterTkCancel();	
			
		}

	}		
	public static void sendSingleInputCommon ( 
			PrintWriter out, 
			TkTicket sendTicket,
			TkTicketManager tm,
			String formAction,
			Lang lang) throws Exception {
		out.println("<div class='tkDetailTopLabel'>"+TkTicket.htmlByEscaped(tm.top_label)+"</div>");		
		out.print("<div class='tkDetailComplete'>");
		
		out.println("<div class='tkDetailLabel'>"+sendTicket.getLabelHtml()+"</div>"); 
		
		out.println("<form action=\""+formAction+"\" method=\"post\" id=\"main\">");
		
		//if ( sendTicket.tktype.equals("textblock")) {
		out.println("<div class='tkDetailSubmitButton tkDetailButton tkDetailButtonTop tkDetailButtonTop"+sendTicket.tktype+"'><input  class='tkDetailSubmitButton tkDetailButton' type=\"submit\" value=\""+lang.getEepTkSubmitOk()+"\" ");
		out.println("></div>");
		//}
		
		out.print("<div class='tkDetailEditor'>");
		sendTicket.printHtmlDivEditorOnly(out, true,"tkDetail");
		
		out.print("</div>");
		if ( sendTicket.tip != null ) {
			out.println("<div class='tkDetailTip'>"+ TkTicket.htmlLfByEscaped (sendTicket.tip) +"</div>");
		}			

		out.print("</div>");
			
    	// submint button
        out.println("<div class='tkDetailSubmitButton tkDetailButton'><input  class='tkDetailSubmitButton tkDetailButton' type=\"submit\" value=\""+lang.getEepTkSubmitOk()+"\" ");

        
        if ( sendTicket.isMsg )
        	out.println(" autofocus ");

		out.println("></div>");	
		
		
		
	
	}
	public static void sendSingleInputIp ( 
			PrintWriter out, 
			TkTicket sendTicket,
			TkTicketManager tm,
			Lang lang) throws Exception {
		// ArrayList<Element> l = e.getChildren();
		sendSingleInputCommon(out,sendTicket,tm,"tkOkIp",lang);



		// out.println("<br/><br/><br/><br/>");
		//out.println("<br>");
		out.println("<div class='tkDetailActions'>");
		out.println("<div class='tkDetailButton tkDetailButtonStop'><a href=\"tkCancelIp\" class='tkDetailButton tkDetailButtonStop'>" + lang.getEepTkActStop() +"</a></div>");
		out.println("</div>");
        out.println("</form>");		
     	
	}		
	
	public static void sendSingleInputEdit ( 
			PrintWriter out, 
			TkTicket sendTicket,
			TkTicketManager tm,
			Lang lang) throws Exception {
		sendSingleInputCommon(out,sendTicket,tm,"tkOkEdit",lang);
		// ArrayList<Element> l = e.getChildren();
		out.println("<br>");
		out.println("<div class='tkDetailActions'>");
		out.println("<div class='tkDetailButton tkDetailButtonCancel'><a href=\"tkCancelEdit\" class='tkDetailButton tkDetailButtonCancel'>"+lang.getEepTkActForget()+"</a></div>");
		out.println("</div>");
		
        out.println("</form>");		
    	
	}		
	
	private TkTicket ticketToEdit = null;
	public void doEdit ( 
			HttpServletRequest request, 
			   HttpServletResponse response,
			   String ticketid ) {
		//Sp.p("zum editieren");
		
		changeContext(request, response);
		try {
			ticketToEdit = ticketManager.getTickets().get(ticketid);
			//ticketToEdit.setX2();
			
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepTkLogoEdit()+ TkTicket.htmlByEscaped(ticketToEdit.label),true,response));
			sendSingleInputEdit(out,ticketToEdit,ticketManager,sitz.lang());
			out.println(getHtmlFooter());
		} catch (Exception e) {
			haveError(e, request, response);
		}
	}	
	public void doEditOk ( 
			HttpServletRequest request, 
			HttpServletResponse response) {
		//Sp.p("UU-");
		changeContext(request, response);
		
		try {
			PrintWriter out = response.getWriter();

			ticketToEdit.reflectToMemory(new MyRequestor(request));

			TkValidationError problem = ticketToEdit.validate();

			if ( problem == null ) {
				if ( ticketToEdit.isDynamo() )  {
					ticketManager.setupSUMONLY();
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview2()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
					
				} else {
					
					out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
				}
				


					sendMap(out, ticketManager,false);

					out.println(getHtmlFooter());					
				


			} else {
				out.println(getHtmlHeader(sitz.lang().getEepTkLogoProblem()+TkTicket.htmlByEscaped(ticketToEdit.label),true,response));

				sendTkError(out, problem);

				out.println("<hr/>");

				sendSingleInputEdit(out,ticketToEdit,ticketManager,sitz.lang());

				out.println(getHtmlFooter());

			}
			
		} catch (Exception e) {
			//Sp.p("UU-15");			
			e.printStackTrace();
			//Sp.p("UU-16");
			haveError(e, request, response);
			//Sp.p("UU-17");
		}

	}
	public void doEditCancel ( 
			HttpServletRequest request, 
			   HttpServletResponse response) {
		
		changeContext(request, response);
		try {
			PrintWriter out = response.getWriter();
			
			out.println(getHtmlHeader(sitz.lang().getEepTkLogoReview()+TkTicket.htmlByEscaped(ticketManager.top_label),true,response));
			sendMap(out, ticketManager,false);
			out.println(getHtmlFooter());
			
		} catch (Exception e) {
			haveError(e, request, response);
		}		
		
	}	
	
	
	
	public void treatTiResponse(XrRqRpTi answerTi,
			HttpServletRequest request, 
			HttpServletResponse response) {
		Seitenlinks newseitenlinks = terminator.getTerminalLinkArrayLast();
		seitenlinks.add(newseitenlinks);
		limitSeitenlinks();
		String screenBm = terminator.getTiScreenBodyMemberAndConsume("eeProject.treatTiResponse");
		
		try {
			PrintWriter out = response.getWriter();
			
			out.println(getHtmlHeader(appid,false,response));
			out.println(getNewMessagesAsHtml());
			out.println(screenBm);
			out.println(getHtmlFooter());
		} catch (IOException e) {
			haveError(e, request, response);
		}
	}
	private void limitSeitenlinks() {

		int min = sitz.getCachedLinkPagesMin();
		int max = sitz.getCachedLinkPagesMax();
		if ( seitenlinks.size() > max ) {
		while ( seitenlinks.size() > min ) {
			seitenlinks.remove(0);
			//Sp.p("removed a linkpage"); // hund
		}
		}
		//Sp.p("limitSeitenlinks() finished with nrb-of-pages="+seitenlinks.size());
	}
	public void treatException(XrRqRpEx answerEx,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		Seitenlinks newseitenlinks = terminator.getTerminalLinkArrayLast();
		seitenlinks.add(newseitenlinks);
		limitSeitenlinks();
		String screenBm = terminator.getTiScreenBodyMember();
		
		try {
			PrintWriter out = response.getWriter();
			out.println(getHtmlHeader(sitz.lang().getEepLogoTiException(),false,response));
			out.println("<p>"+sitz.lang().getEepLogoTiException2()+TkTicket.htmlByEscaped(answerEx.getTp())+"</p>"+ screenBm);
			haveError(answerEx.getException(), request, response);
			out.println(getHtmlFooter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			haveError(e, request, response);
		}		
		
		
				
		
	}

	
	/*
	private static String statusText(int status ) {
		if (status == TK_OFF ) return "OFF";
		else if (status == TK_DOING_CHOOSE ) return "CHOOSE";
		else if (status == TK_DOING_UPLOAD ) return "UPLOAD";
		else if (status == TK_OUT_WAITING ) return "OUT_WAITING";
		else return "??"+status+"??";
	}*/

	@Override
	public boolean chooseSaveFile() {
		// TODO Auto-generated method stub
		return gcfIsSave;
	}

	@Override
	public String getChooseLabel() {
		// TODO Auto-generated method stub
		return gcfLabel;
	}

	@Override
	public String getChooseTip() {
		// TODO Auto-generated method stub
		return gcfTip;
	}

	@Override
	public boolean getHasFileExtension() {
		// TODO Auto-generated method stub
		return (gcfExtension !=null);
	}

	@Override
	public String getFileExtension() {
		// TODO Auto-generated method stub
		return gcfExtension;
	}

	@Override
	public String getFileProposedFilename() {
		// TODO Auto-generated method stub
		return gcfProposedFilenameSave;
	}

	@Override
	public String getFileCurVal() {
		// TODO Auto-generated method stub
		return gcfCurVal;
	}

	@Override
	public boolean hasFileCurVal() {
		if ( gcfCurVal!= null && gcfCurVal.length()> 0)
			return true;
		else
			return false;
	}
	UserInfo userInfo = null;
	@Override
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return  userInfo;
	}
	@Override
	public boolean mayOperateFiles() {
		// TODO Auto-generated method stub
		
		return sitz.hasPrivOpFiles();
	}
	@Override
	public boolean mayOperateDirs() {
		// TODO Auto-generated method stub
		return sitz.hasPrivOpDirs();
	}


}