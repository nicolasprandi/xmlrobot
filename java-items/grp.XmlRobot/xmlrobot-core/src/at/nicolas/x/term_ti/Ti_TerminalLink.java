/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import javax.swing.JOptionPane;

import org.jdom2.Attribute;
import org.jdom2.Element;

import at.nicolas.x.WebVfs.TkTicket;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.Core;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.term_core.ITerminatorStyler;
import at.nicolas.x.term_core.Tc_Terminator;


public abstract class Ti_TerminalLink {
	private boolean teston = false;
	private static String wsDir = null;
	private static  String sysDir = null;
	private static  String museumDir = null;
	private static  String backupDir = null;
	private static  String docDir = null;
	private static  String homeDir = null;
	private static  String serviceAppsDir = null;
	private static int wsDirLen = 0;
	private static  int sysDirLen = 0;
	private static  int museumDirLen = 0;
	private static  int backupDirLen = 0;
	private static  int docDirLen = 0;
	private static  int homeDirLen = 0;
	private static  int serviceAppsDirLen = 0;	
	
	private static String statusDir = null;
	private static int statusDirLen = 0;
	
	private final String loxodirsign = "&#160;&raquo;";  // &rarr;	&raquo;&bull;
	private final String loxoactionsign = "&#160;&#402;"; 
	String label = null;
	private String wert = null;

	String app = null;
	String appdir = null;
	String appdirMaster = null;
	String inst = null;
	String instdir = null;
	String instdirMaster = null;
	String librodir = null;
	String librodir2 = null;
	int instdirLen = 0;
	int appdirLen = 0;
	int instdirLenMaster = 0;
	int appdirLenMaster = 0;	
	int librodirLen = 0;
	int librodirLen2 = 0;
	private String job = null;
	
	private int docu_length = -1;
	private LogConnector lc = new LogConnector("TerminalLink");
	ITerminator terminator = null;
	ITerminatorStyler ts = null;
	ITiHandler rh = null;
	
	private static int myIdSequence = 0;
	private int myId = 0;
	
	private static final int logTextOffset = 30;
	private ISitzung isitzung = null;
	
	private static String buttonTagOpen = "<button ";
	private static String buttonTagClose = "</button>";
	protected  LinkTextResponse getHtmlLinktextFromEscaped(String escapedLinktext, Element tiElement, String buttonClass) {
		return getHtmlLinktextFromEscapedInternal(
				 escapedLinktext,  tiElement, 
				   buttonClass,"",true);
	}
	
	protected  LinkTextResponse getHtmlLinktextFromEscapedNoNbsp(String escapedLinktext, Element tiElement, String buttonClass) {
		return getHtmlLinktextFromEscapedInternal(
				 escapedLinktext,  tiElement, 
				   buttonClass,"",false);
	}	
	protected  LinkTextResponse getHtmlLinktextFromEscaped(
			String escapedLinktext, Element tiElement,  
			String buttonClass,
			String extension) {
		return getHtmlLinktextFromEscapedInternal(
				 escapedLinktext,  tiElement, 
				   buttonClass,"_"+extension,true);
	}	
	protected LinkTextResponse getHtmlLinktextFromEscapedInternal(
				String escapedLinktext, Element tiElement, 
				 String buttonClass, String extension, boolean nbspReplace) {
		//System.out.println("getHtmlLinktextFromEscaped with ["+escapedLinktext+"] with tiElement : "+Commons.toText(tiElement));
		LinkTextResponse ltr = new LinkTextResponse();
		if ( escapedLinktext == null )
			return ltr;
		String msg = escapedLinktext;
		String classes="";
		
		msg = Entities.exportEntities(msg);
		boolean didRemoveGoodies = false;

		if ( ( rh.triggerButtonsByButton() &&  Commons.hasFlag(tiElement, "button"+extension))
				|| (!rh.triggerButtonsByButton() && !Commons.hasFlag(tiElement, "nobutton"+extension))				
				) {
			classes = buttonClass; 
			if ( tiElement.getAttribute("class"+extension) != null ) {
				classes = classes + " " +tiElement.getAttributeValue("class"+extension);				
			}
		
			if ( isOnline && Commons.hasFlag(tiElement, "xbutton"+extension)) {
				// die sonderzeichen rauskuerzen
				msg = goodiesRemove(msg);
				didRemoveGoodies = true;
				int firstLetter = findFirstLetterOrDigit(msg);
				int lastLetter = findLastLetterOrDigit(msg);
				/* if ( msg.startsWith("[ + ") && msg.endsWith(" ]") )
					msg= "+ "+msg.substring(4, msg.length()-2);
				else*/ if ( firstLetter > -1 && lastLetter > -1)
					msg = msg.substring(firstLetter, lastLetter+1);		
				else if (msg.equals("[ ? ]"))
					msg = "?";
				else if (msg.equals("[ ?? ]"))
					msg = "??";
			}
			ltr.setOpenTag(buttonTagOpen);
			ltr.setClasses(buttonTagClose);
			ltr.setIsButton(true);
		}
		String msg1	= msg;
		msg = TkTicket.html3ByNatural(msg);	
		//System.out.println(">>>>" + msg1+"    >>>>  "+msg);
		if (! didRemoveGoodies)
			msg = goodies(msg);		
		
		msg = msg.replaceAll("\n", "<br>");	
		if ( nbspReplace)
			msg = Commons.stringReplace(msg," ","&nbsp;");
		
		ltr.setLinktext(msg);
		ltr.setClasses(classes);
		return ( ltr );		
	}	
	
	protected static int findFirstLetterOrDigit( String input ) {
		for ( int i=0; i < input.length(); i++) {
			if ( Character.isLetterOrDigit(input.charAt(i)))
				return i;
		}
		return -1;
	}
	
	protected static int findLastLetterOrDigit( String input ) {
		for ( int i=input.length()-1; i >= 0; i--) {
			if ( Character.isLetterOrDigit(input.charAt(i)))
				return i;
		}
		return -1;
	}	
	
	

	/*
	protected static Element eTiDownloadDir = null;
	protected static Element eTiDownloadFile = null;
	*/
	private static boolean isOnline;
	public static void setup(boolean _isOnline) {
		isOnline = _isOnline;
		wsDir = CoreBuilder.ws.getWsDir();
		sysDir = CoreBuilder.ws.getSystemDir();
		museumDir = CoreBuilder.ws.getMuseumDir();
		backupDir = CoreBuilder.ws.getBackupDir();
		docDir = CoreBuilder.ws.getUserDocDir();
		statusDir = CoreBuilder.ws.getStatusDir();
		
		// Sp.p("*** Terminal-SETUP: wsDir="+wsDir);
		// Sp.p("*** Terminal-SETUP: sysDir="+sysDir);
		// Sp.p("*** Terminal-SETUP: museumDir="+museumDir);
		// Sp.p("*** Terminal-SETUP: backupDir="+backupDir);
		// Sp.p("*** Terminal-SETUP: docDir="+docDir);
		// Sp.p("*** Terminal-SETUP: statusDir="+statusDir);
		if ( docDir==null)
			docDirLen = 0;
		else
			docDirLen = docDir.length();
			
		homeDir = CoreBuilder.ws.getUserHomeDir();
		// Sp.p("*** Terminal-SETUP: homeDir="+homeDir);
		serviceAppsDir = Core.getMount(Core.DIR_SERVICE_APPS);	
		// Sp.p("*** Terminal-SETUP: serviceAppsDir="+serviceAppsDir);
		wsDirLen = wsDir.length();
		sysDirLen = sysDir.length();
		museumDirLen = museumDir.length();
		backupDirLen = backupDir.length();
		statusDirLen = statusDir.length();
		
		homeDirLen = homeDir.length();
		serviceAppsDirLen = serviceAppsDir.length();	
		// Sp.p("************* WS ["+wsDir+"]");
		// Sp.p("********* WS len ["+wsDirLen+"]");
		/*
		eTiDownloadDir = new Element("ti");
		eTiDownloadDir.setAttribute("flag","button");
		eTiDownloadDir.setAttribute("class","tiDownloadDirButton");
		
		eTiDownloadFile = new Element("ti");
		eTiDownloadFile.setAttribute("flag","button");
		eTiDownloadFile.setAttribute("class","tiDownloadFileButton");	*/	
	}
	

	private String sitzungDir;
	public Ti_TerminalLink (
			Element e, 
			ITerminator _terminator, 
			String app, 
			String appdir, 
			String inst, 
			String instdir, 
			ITiHandler rh ) {
		// if ( rh != null ) 
			isitzung = rh.getSitzung();
			sitzungDir = isitzung.getSessionHomeFs();
		myIdSequence++;
		myId = myIdSequence;
		
		this.app = app;
		this.appdir = appdir;
		// Sp.p("*** Terminal-SETUP2:XXXX appdir="+appdir);
		if ( appdir == null || appdir.equals("")) {
			// Sp.p("-xxxx appdir ist null");
			String supposedappdir = null;
			try {
				supposedappdir= rh.getAppDir();
				// Sp.p("- supposedappdir is "+supposedappdir);
			} catch (Exception e1) {
				supposedappdir = null;
				// Sp.p("- supposedappdir failes ");
				Sp.p("rh exists="+(rh!=null));
				Sp.pe(e1);
			}
			if ( supposedappdir != null ) {
				this.appdir = supposedappdir;
				try {
					this.app = rh.getAppId();
				} catch (Exception e1) {
					this.app = "unknowAppId";
				}
			}
		} else {
			// Sp.p("appdir is not null= "+this.appdir);
			// Sp.p(" len="+this.appdir.length());
		}
		this.inst = inst;
		this.instdir = instdir;	
		
		//System.out.println("XXXXXXXXXXXX this.instdir="+this.instdir);
		if ( instdir == null || instdir.length()== 0 ) {
			// Sp.p("*** the instance is null");
			try {
				this.instdir = rh.translateString("(dir,pro)");
				this.inst = rh.translateString("(prop,pro)");
				
				if ( teston ) {
					System.out.println ("Ti_TerminalLink.CREATE ORGA inst="+this.inst+" instdir = "+this.instdir);
				}				
				
			} catch (Exception e1) {
				this.inst = null;
				this.instdir = null;
			}
			
		}		
		if ( isitzung.isOnline() ) {
			if ( teston ) {
				System.out.println (" ");
				System.out.println ("Ti_TerminalLink.CREATE PARA inst="+this.inst+" instdir = "+this.instdir);
				try {
					System.out.println ("  masterAppDir="+rh.getSitzung().getMasterAppDir());
					System.out.println ("  masterProDir="+rh.getSitzung().getMasterProjektDir());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("Problem in gettting proDir or proId");
					e1.printStackTrace();
				}
			}			

			this.appdirMaster = rh.getSitzung().getMasterAppDir();	

			this.instdirMaster = rh.getSitzung().getMasterProjektDir();				
		}
			

		


		
		
		try {
			this.librodir = rh.translateString("(f,request,dir,libro)");			
		} catch (Exception e1) {
			this.librodir = null;
		}		
		
		try {
			this.librodir2 = rh.translateString("(f,request,dir,libro2)");			
		} catch (Exception e1) {
			this.librodir2 = null;
		}			
		
				
		
		//Sp.p("*** XXX Terminal-SETUP2: this.appdir ="+this.appdir);
		//Sp.p("*** XXX Terminal-SETUP2: this.instdir="+this.instdir);
		try {
			// Sp.p("RH SAYS: "+"appdir="+rh.getAppDir()+"appid="+rh.getAppId());
		} catch (Exception e1) {
			
			// Sp.p("RH SAYS:e "+e1.getMessage());
			// TODO Auto-generated catch block
			Sp.pe(e1);
		}

		terminator = _terminator;
		ts = terminator.getStyler();
		this.rh = rh;
		label =  e.getAttributeValue("label");
		if ( label == null) label = "";
		if ( !label.trim().equals(""))
			label = label+":";
	
	}
	
	public abstract Ti_HtmlReturn getHtml();
	
	public String getLabel() {
		return label;
	} 
	public abstract String getLogText();
	
	public String getRefCodeProlog() {
		return "X"+Commons.int2String(myId)+"x";
	}
	
	public int getPostLen() {
		return 1;
	}
	
	public String getPost(int i) {
		String r = "mark"+Commons.int2String(i);

		return "x"+r+"X";
	}
	
	public String getMark(int i) {
		
		String r = "mark"+Commons.int2String(i);

		return "x"+r+"X";
		
	}
	
	public String getRefCode(int i) {
		return getRefCodeProlog()+getMark(i);
	}	
	
	public String getRefCode () {
		return getRefCodeProlog()+getMark(1);
	}
	
	public boolean reactsOn(String mark ) {

		if ( mark.startsWith(getRefCodeProlog())) {
			return true;
		} else
			return false;
	}
	/*	
	public Interpreter createInterpreterToReact( String mark ) {
		if ( mark.startsWith(getRefCode(1)) ) {
			executeLink();
		}		
	}*/
	public abstract void executeLink();
	public abstract Interpreter createInterpreterToReact(String mark, boolean interWebModus,
			IGuiController guiController, ISitzung isitzung ) throws Exception;
	
	public void reactOn(String mark ) {
		if ( mark.startsWith(getRefCode(1)) ) {
			executeLink();
		} else if (  mark.startsWith(getRefCode(2)) ) {
		}				
	}
	
	public Interpreter getInterpreterForReaction(
			String mark, 
			boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
	
			// Sp.p("Comparing '"+mark+"' with '"+getRefCodeProlog()+"'");
			if ( mark.startsWith(getRefCodeProlog()) )  {
				return createInterpreterToReact(mark,interWebModus,guiController,isitzung);
			}

		return null;

	}	
	
	String emptyToNull ( String in ) {
		if ( in.equals(""))
			return null;
		return in;
	}
	ConvertResult convertFileDirText( 
			String in2, 
			String refcodeHome, 
			String refcodeDir, 
			String refcodeLoxoFileAction ,
			boolean isDir , 
			Attribute lenatt,
			Attribute len2att,
			LinkConverterRequest lcv) {
		if ( teston ) {
			System.out.println ("----");
			System.out.println ("Ti_TerminalLink.convert in2="+in2);
			System.out.println ("                       inst="+instdir);
			
			
		}
		
		//Sp.p("----- convertFileDirText ["+in2+"]mit LinkConverterRequest:"+lcv.getDoku());
		LinkDoesUse ldu = new LinkDoesUse();
		boolean seesAppData = false;
		boolean seesLibroData = false;
		boolean seesSessionData = false;
		 boolean seesProData = false;
		 boolean seesWorkspaceData = false;
		 boolean seesSystemData = false;
		 boolean seesOtherData = false;
		
		/*Sp.p("*!!!** ");
		Sp.p("*!!!** ");seesOtherData
		Sp.p("*!!!** ");
		Sp.p("*!!!** Terminal-REQUEST ****** in="+in);
		Sp.p("*!!!** ");
		Sp.p("*!!!** ");*/
		// if ( in.startsWith(wsDir) ) {
			// Sp.p("That starts with ws: ["+wsDir+"]");
		// }
		String in;
		try {
			if ( in2 == null || in2.length()==0 )
				in="?";
			if ( isDir )
				in = Commons.getUriByDirname(in2);
			else
				in = Commons.getUriByFilename(in2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Sp.pe(e1);
			in=in2;
		}
		if ( teston ) {
			System.out.println ("in="+in);
			Sp.p("*** Terminal-REQUEST *** in2 = ["+in2+"] "+
							" in= ["+in+"] inLen=["+in.length()+"]");
		}		
		ConvertResult cv = new ConvertResult(); 
		cv.linkDoesUse = ldu;

		String loxoHtml="";
		if ( ( ( lcv.isSeeFileFunction() && !isDir ) || ( lcv.isSeeDirFunction() && isDir ) )
				
				 && 
				
				!Tc_Terminator.stilIsWeb) {
			loxoHtml = "&nbsp;<a "+ts.getHref(refcodeLoxoFileAction)+Tc_Terminator.stil.getAnkerGREYNONE()+">&nbsp;"+loxoactionsign+"&nbsp;</a>";
			ldu.objectAction = true;
		}
		
		int inLen = in.length();
		int lena = -1;
		if ( lenatt !=null) {
			lena = Commons.string2int(lenatt.getValue());
		}
		
		int lena2 = -1;
		if ( len2att !=null) {
			lena2 = Commons.string2int(len2att.getValue());
		}		
		if ( instdir != null && instdir.length()>0) {
			instdirLen = instdir.length();
		} else {
			instdir = null;
			instdirLen = 0;
		}
		if ( instdirMaster != null && instdirMaster.length()>0) {
			instdirLenMaster = instdirMaster.length();
		} else {
			instdirMaster = null;
			instdirLenMaster = 0;
		}		
		
		if ( teston ) {
			System.out.println ("instdirLen="+instdirLen);
		}		
		
		if ( librodir != null && librodir.length()>0) {
			librodirLen = librodir.length();
		} else {
			librodirLen = 0;
		}
		
		if ( librodir2 != null && librodir2.length()>0) {
			librodirLen2 = librodir2.length();
		} else {
			librodirLen2 = 0;
		}		
		
		if (teston)
			Sp.p("instdir= ["+instdir+"]"+" instdirLen= ["+instdirLen+"]");
		
		if ( appdir != null && appdir.length()>0) {
			appdirLen = appdir.length();
		} else {
			appdir = null;
			appdirLen = 0;
		}	
		if ( appdirMaster != null && appdirMaster.length()>0) {
			appdirLenMaster = appdirMaster.length();
		} else {
			appdirMaster = null;
			appdirLenMaster = 0;
		}			
		/*
		System.out.println(" instdir="+instdir);
		System.out.println("  appdir="+appdir);
		System.out.println("librodir="+librodir);
		System.out.println("   wsDir="+wsDir);*/
		
		String inHtmlSpan = "<span>&nbsp;"+isitzung.lang().getFilelocIn()+"&nbsp;</span>";
		
		
		
		boolean dontcrossout = false;
		
		// Sp.p("==============================================================");
		// Sp.p("in= ["+in+"]");
		// Sp.p("instdir= ["+instdir+"]"+" instdirLen= ["+instdirLen+"]");
		// Sp.p("appdir= ["+appdir+"]"+" appdirLen= ["+appdirLen+"]");		
		// Sp.p("inLen: ["+inLen+"]");
		String isin = null;
		if ( instdir!=null && in.equals(instdir)) {
			isin = isitzung.lang().getLocProject();
		} else if ( instdirMaster!=null && in.equals(instdirMaster)) {
			isin = isitzung.lang().getLocProject();
		} else if ( appdir!=null && in.equals(appdir) ) {
			isin = isitzung.lang().getLocApp();
		} else if ( appdirMaster!=null && in.equals(appdirMaster) ) {
			isin = isitzung.lang().getLocApp();
		} else if ( in.equals(librodir) ) {
			isin = isitzung.lang().getLocLibro();
		} else if ( in.equals(librodir2) ) {
			isin = isitzung.lang().getLocLibro()+"-2";
		} else if ( in.equals(sitzungDir) ) {
			isin = isitzung.lang().getLocSession();
		} else if ( in.equals(sysDir) ) {
			isin = isitzung.lang().getLocSystem();
		} else if ( !isOnline ) {
			if ( in.equals(wsDir) ) {
				isin = isitzung.lang().getLocWorkspace();
			}else if ( in.equals(backupDir) ) {
				isin = isitzung.lang().getLocBackup();
			}else if ( in.equals(statusDir) ) {
				isin = isitzung.lang().getLocStatus();
			}else if ( docDir!=null && in.equals(docDir) ) {
				isin = isitzung.lang().getLocUserDocument();
			}else if ( in.equals(homeDir) ) {
				isin = isitzung.lang().getLocUserHome();
				
			}	
		}
		
		if ( teston ) {
			System.out.println ("isin="+isin);
			System.out.println ("inLen="+inLen+ " instdirLen="+instdirLen+ " instdir="+instdir+" in="+in);
		}		
		if ( instdirMaster!=null && in.startsWith(instdirMaster) && inLen>instdirLenMaster) {
				// Sp.p("OOO is in project: "+instdir);
				seesProData = true;
				dontcrossout = lcv.isExtDontCrossFnPro();
				if ( lcv.isSeeProData()) {
				
				
					cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriProMaster()+"</a>";//+loxoHtml;				
					ldu.home = true;
					ldu.clickRemainer = true;
				} else {
					
					cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriProMaster()+"</span>";//+loxoHtml;				
					
				}
				cv.basename = in.substring(instdirLenMaster);
				cv.targetHome = instdirMaster;
				cv.homeEnd = instdirLenMaster;
				//System.out.println ("is INST MASTER");

				
			}
		else if ( instdir!=null && in.startsWith(instdir) && inLen>instdirLen) {
			// Sp.p("OOO is in project: "+instdir);
			seesProData = true;
			dontcrossout = lcv.isExtDontCrossFnPro();
			if ( lcv.isSeeProData()) {
			
			
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriPro()+"</a>";//+loxoHtml;				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriPro()+"</span>";//+loxoHtml;				
				
			}
			cv.basename = in.substring(instdirLen);
			cv.targetHome = instdir;
			cv.homeEnd = instdirLen;
			//System.out.println ("is INST AUX");

			
		} else if ( appdirMaster!=null && in.startsWith(appdirMaster) && inLen>appdirLenMaster) {
			// Sp.p("is in app");
			seesAppData = true;
			dontcrossout = lcv.isExtDontCrossFnApp();
			if ( lcv.isSeeAppData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriAppMaster()+"</a>";			
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriAppMaster()+"</span>";//+loxoHtml;				
				
			}			
			
			cv.basename = in.substring(appdirLenMaster);
			cv.targetHome = appdirMaster;
			cv.homeEnd = appdirLenMaster;
		
		} else if ( appdir!=null && in.startsWith(appdir) && inLen>appdirLen) {
			// Sp.p("is in app");
			seesAppData = true;
			dontcrossout = lcv.isExtDontCrossFnApp();
			if ( lcv.isSeeAppData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriApp()+"</a>";			
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriApp()+"</span>";//+loxoHtml;				
				
			}			
			
			cv.basename = in.substring(appdirLen);
			cv.targetHome = appdir;
			cv.homeEnd = appdirLen;
		
		} else if ( in.startsWith(librodir) && inLen>librodirLen) {
			
			// Sp.p("is in app");
			seesLibroData = true;
			dontcrossout = lcv.isExtDontCrossFnApp(); //true;

			cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriLibro()+"</a>";			
			ldu.home = true;
			ldu.clickRemainer = true;
			
			
			cv.basename = in.substring(librodirLen);
			cv.targetHome = librodir;
			cv.homeEnd = librodirLen;
		
		}  else if ( in.startsWith(librodir2) && inLen>librodirLen2) {
			
			// Sp.p("is in app");
			seesLibroData = true;
			dontcrossout = lcv.isExtDontCrossFnApp(); //true;

			cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriLibro()+"-2</a>";			
			ldu.home = true;
			ldu.clickRemainer = true;
			
			
			cv.basename = in.substring(librodirLen2);
			cv.targetHome = librodir2;
			cv.homeEnd = librodirLen2;
		
		}else if ( in.startsWith(sitzungDir) && inLen>sitzungDir.length()) {
			//System.out.println("IST SESSION-"+in);
			//System.out.println(" sitzungDir-"+sitzungDir);
			//System.out.println("           lcv.isSeeSessionData()="+lcv.isSeeSessionData());
			
			// Sp.p("is in app");
			//seesLibroData = true;
			seesSessionData = true;
			dontcrossout = lcv.isExtDontCrossFnElse(); //true;

			cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriSession()+"</a>";			
			ldu.home = true;
			ldu.clickRemainer = true;
			
			
			cv.basename = in.substring(sitzungDir.length());
			cv.targetHome = sitzungDir;
			cv.homeEnd = sitzungDir.length();
		
		} else if ( !isOnline && in.startsWith(wsDir) && inLen>wsDirLen) {
			String dof = null;
			dontcrossout = lcv.isExtDontCrossFnElse();
			try {
				try {
					dof = Commons.getDirOfFile(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Sp.pe(e);
					dof="? getDirOfFile("+in+")";
				}
				if ( wsDir.equals(dof)) {
					seesWorkspaceData = true;
					if ( lcv.isSeeWorkspaceData()) {
						cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">WORKSPACE</a>";// +loxoHtml;
						ldu.home = true;
						ldu.clickRemainer = true;
					} else {
						cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">WORKSPACE"+"</span>";
						
					}
				} else
					cv.rightPanel = "";//loxoHtml;				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
				cv.rightPanel = "";//loxoHtml;
			}

			cv.basename = in.substring(wsDirLen);
			cv.targetHome = wsDir;
			cv.homeEnd = wsDirLen;
		} else if ( in.startsWith(serviceAppsDir) && inLen>serviceAppsDirLen) {
			//
			seesSystemData = true;
			dontcrossout = lcv.isExtDontCrossFnElse();
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">SERVICE-APPS"+"</a>";	
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {				
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">SERVICE-APPS"+"</span>";									
			}	
			cv.basename = in.substring(serviceAppsDirLen);
			cv.targetHome = serviceAppsDir;
			cv.homeEnd = serviceAppsDirLen;
	
		} else if ( !isOnline && in.startsWith(museumDir) && inLen>museumDirLen) {
			// Sp.p("XXXXXXXXXXXXXXXX is in museum: "+in);
			// Sp.p("XXXXXXXXXXXXXXXX museumDir: "+museumDir);
			// Sp.p("XXXXXXXXXXXXXXXX museumDirLen: "+museumDirLen);
			seesSystemData = true;
			dontcrossout = lcv.isExtDontCrossFnElse();
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">MUSEUM"+"</a>";//+loxoHtml;				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">MUSEUM</span>";
																	
			}				
			cv.basename = in.substring(museumDirLen);
			cv.targetHome = museumDir;
			cv.homeEnd = museumDirLen;

			
		}  else if ( !isOnline && in.startsWith(backupDir) && inLen>backupDirLen) {
			dontcrossout = lcv.isExtDontCrossFnElse();
			seesSystemData = true;
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">BACKUP"+"</a>";//+loxoHtml;
				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">BACKUP"+"</span>";//+loxoHtml;
																	
			}			
			cv.basename = in.substring(backupDirLen);
			cv.targetHome = backupDir;
			cv.homeEnd = backupDirLen;
			
		}  else if ( !isOnline && in.startsWith(statusDir) && inLen>statusDirLen) {
			dontcrossout = lcv.isExtDontCrossFnElse();
			seesSystemData = true;
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">STATUS"+"</a>";//+loxoHtml;
				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">STATUS"+"</span>";//+loxoHtml;
																	
			}
			cv.basename = in.substring(statusDirLen);
			cv.targetHome = statusDir;
			cv.homeEnd = statusDirLen;
			
		}  else if ( in.startsWith(sysDir) && inLen>sysDirLen) {
			dontcrossout = lcv.isExtDontCrossFnElse();
			seesSystemData = true;
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriSys()+"</a>";//+loxoHtml;
				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+isitzung.lang().getUriSys()+"</span>";//+loxoHtml;
																	
			}
			cv.basename = in.substring(sysDirLen);
			cv.targetHome = sysDir;
			cv.homeEnd = sysDirLen;
			
		} else if ( !isOnline && docDir!=null && in.startsWith(docDir) && inLen>docDirLen) {
			dontcrossout = lcv.isExtDontCrossFnElse();
			seesSystemData = true;
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">DOCS"+"</a>";//+loxoHtml;
				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">DOCS"+"</span>";//+loxoHtml;
																	
			}			
			cv.basename = in.substring(docDirLen);
			cv.targetHome = docDir;
			cv.homeEnd = docDirLen;
			
		} else if ( !isOnline && in.startsWith(homeDir) && inLen>homeDirLen) {
			dontcrossout = lcv.isExtDontCrossFnElse();
			seesSystemData = true;
			if ( lcv.isSeeSystemData()) {
				cv.rightPanel = inHtmlSpan+"<a "+ts.getHref(refcodeHome)+Tc_Terminator.stil.getAnkerGREY()+">USER"+"</a>";//+loxoHtml;
				
				ldu.home = true;
				ldu.clickRemainer = true;
			} else {
				cv.rightPanel = inHtmlSpan+"<span "+Tc_Terminator.stil.getAnkerGREY()+">USER"+"</span>";//+loxoHtml;
																	
			}			
			cv.basename = in.substring(homeDirLen);
			cv.targetHome = homeDir;
			cv.homeEnd = homeDirLen;
			
		}   else {
			cv.rightPanel = "";
			cv.basename = in;
			cv.targetHome = "";
			cv.homeEnd = 0;
			seesOtherData =true;
			dontcrossout = lcv.isExtDontCrossFnElse();
			//System.out.println("others cv.homeEnd="+cv.homeEnd);
			// Sp.p("IRGENDWO: "+in+"isdir="+isDir);
			if ( lcv.isSeeOtherData()) {
				ldu.clickRemainer = true;
			}
		}
		if ( cv.rightPanel.length() > 0 && isin != null ) 
			cv.rightPanel ="=&nbsp;"+isin+" "+cv.rightPanel;
		
		boolean crossout = !dontcrossout;
		
		//Sp.p("YYY cv.remainer  : "+cv.remainer);
		//Sp.p("YYY cv.targetHome: "+cv.targetHome);
		//Sp.p("YYY cv.homeEnd   : "+cv.homeEnd);		
		
		if ( false) {
			// its a dir
			
		} else {
			// its a file
			String dod = null;
			try {
				// Sp.p("???????????????????????????????????");


				try {
					if ( isDir ) {
						dod = Commons.getDirOfDir(in);
						//Sp.p("dirofdir of "+in+" is: "+dod);
					}
					else
						dod = Commons.getDirOfFile(in);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					dod="? getDirOfFile/Dir("+in+")";
					Sp.pe(e);
				}

				/*
				System.out.println("cv.homeEnd="+cv.homeEnd);
						
				System.out.println("seesAppData ="+seesAppData);
				System.out.println("seesProData ="+seesProData);
				System.out.println("seesWorkspaceData ="+seesWorkspaceData);
				System.out.println("seesSystemData ="+seesSystemData);
				System.out.println("seesOtherData ="+seesOtherData);
				System.out.println("seesLibroData ="+seesLibroData);
				System.out.println("seesSessionData ="+seesSessionData);
			
				System.out.println("xxx  dod="+dod+" in="+in+" cv.homeEnd="+cv.homeEnd+" dod.len="+dod.length());
				*/
				String dod2 = dod.substring(cv.homeEnd); 
				// Sp.p("xxx dod2 ="+dod2);
				// Sp.p("xxx cv.html.length() ="+cv.html.length());
				if ( cv.rightPanel.length()>0) {
					// Sp.p("IF 1");
					cv.rightPanel = "&nbsp;"+cv.rightPanel; // cv.html = "&nbsp;&nbsp;&nbsp;"+cv.html;
				}
							
				if ( dod2.trim().length() > 0 ) {
					// Sp.p("IF 2");
					
					//String sender = cutDirectory2(dod2,len2att);
					String sender = cutDirectory2(dod2,60);
					
					//System.out.println("---- SENDER="+sender);
					// sender = Entities.importEntities(sender);
					String senderHtml = getHtmlByTextFromEscaped(sender);
					// Sp.p("sender='"+sender+"' by dod2='"+dod2+"' len2attr="+len2att);
					
					boolean showThat = false;
					
					if ( seesAppData && lcv.isSeeAppData()) showThat = true;
					else if ( seesProData && lcv.isSeeProData()) showThat = true;
					else if ( seesWorkspaceData && lcv.isSeeWorkspaceData()) showThat = true;
					else if ( seesSystemData && lcv.isSeeSystemData()) showThat = true;
					else if ( seesOtherData && lcv.isSeeOtherData()) showThat = true;
					else if ( seesLibroData && lcv.isSeeLibroData() ) showThat = true;
					else if ( seesSessionData && lcv.isSeeSessionData()) showThat = true;
					String plus ="";
					if ( cv.rightPanel.length() > 0 )
						plus = "<span>:&nbsp;</span>";			
					String derHtml = cv.rightPanel;
					if ( crossout ) {
						derHtml="XX";
						sender="YY";
					}
					if ( showThat ) {

						// cv.html = cv.html+"&nbsp;in&nbsp;<a "+ts.getHref(refcodeDir)+Tc_Terminator.stil.getAnkerGREY()+">"+sender+"</a>" ;
						cv.rightPanel = derHtml+plus+"<a "+ts.getHref(refcodeDir)+Tc_Terminator.stil.getAnkerGREY()+">"+TkTicket.html3ByNatural(sender)+"</a>" ;
						ldu.dir = true;
					} else {
						cv.rightPanel = derHtml+ plus+"<span "+Tc_Terminator.stil.getAnkerGREY()+">"+TkTicket.html3ByNatural(sender)+"</span>" ;
						
					}
					
					
				}
				if ( crossout )
					cv.basename="ZZZ";
				else
					cv.basename = in.substring(dod.length());				

				if ( isOnline ) {
					cv.basename = TkTicket.html3ByNatural(cv.basename);
					// System.out.println ("cv.basename html="+cv.basename);
				}				
				if ( teston ) {
					System.out.println ("cv.rightPanel="+cv.rightPanel);
					System.out.println ("cv.basename="+cv.basename);
					System.out.println ("cv.targetHome="+cv.targetHome);
					System.out.println ("cv.homeEnd="+cv.homeEnd);

					
					

				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
				cv.rightPanel = "";
				cv.basename = in;
				cv.targetHome = "";
				cv.homeEnd = 0;
			}

		}
		cv.rightPanel = loxoHtml + cv.rightPanel;
		return cv;
			
	}
	public static String cutDirectory(String dir, int len ) {
		if ( len == -1 ) return dir;
		String ret;
		try {
			ret = Commons.cutDir(dir, len);
			return ret;
		} catch (Exception e) {
			Sp.pe(e);
			return dir;
		}
	 
	}
	
	public static String cutDirectory(String dir, Attribute attribute ) {
		if ( attribute == null ) return dir;
		String ret;
		try {
			ret = Commons.cutDir(dir, Commons.string2int(attribute.getValue()));
			return ret;
		} catch (Exception e) {
			Sp.pe(e);
			return dir;
		}
		
	}	
	
	public static String cutDirectory2(String dir, Attribute attribute ) {
		if ( attribute == null ) return dir;
		String ret = Commons.cutDir2(dir, Commons.string2int(attribute.getValue()));
		// Sp.p("Converted dir ("+dir+","+attribute.getValue()+") => "+ret);
		return ret;
	}	
	
	public static String cutDirectory2(String dir, int len ) {

		String ret = Commons.cutDir2(dir,len);
		// Sp.p("Converted dir ("+dir+","+attribute.getValue()+") => "+ret);
		return ret;
	}		
	
	public static String cutFile(String dir, Attribute attribute ) {
		if ( attribute == null ) return dir;
		String ret;
	
			try {
				ret = Commons.cutFile(dir, Commons.string2int(attribute.getValue()));
				return ret;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
				return dir;
			} 
		 
	}	
	
	public static String cutFile2(String dir, Attribute attribute ) {
		if ( attribute == null ) return dir;
		String ret = Commons.cutFile2(dir, Commons.string2int(attribute.getValue()));
		// Sp.p("Converted file ("+dir+","+attribute.getValue()+") => "+ret);
		return ret;
	}		
	
	
	
	public static Ti_TerminalLink getTerminalLink  (Element eE, ITerminator _terminator, String app, String appdir,String inst,String instdir, ITiHandler rh ) throws Exception {
		Ti_TerminalLink ret = null;
		String tipe = eE.getAttributeValue("type");
		// Sp.p("rh link="+rh);
		
		if ( tipe==null || (tipe.equals("text"))) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkText(eE, _terminator, app, appdir, inst,instdir,rh);
		} else if ( tipe.equals("dir")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkDir(eE, _terminator, app, appdir,inst,instdir,rh);
		} else if ( tipe.equals("file")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkFile(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("call_job")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkJob(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("call_service")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkSystemcall(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("web")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkWeb(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("ul")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkUl(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("img")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkImg(eE, _terminator, app,  appdir, inst,instdir,rh);
		} else if ( tipe.equals("rec")) {
			if ( Commons.hasFlag(eE, "divs"))
				throw new LangException(999,"ti rec/divs gibts nicht mehr, ist jetzt menu: "+Commons.toText(eE));
			ret = (Ti_TerminalLink)new Ti_TerminalLinkRec(eE, _terminator, app,  appdir, inst,instdir,rh,true,false);
		} else if ( tipe.equals("menu")) {
			ret = (Ti_TerminalLink)new Ti_TerminalLinkRec(eE, _terminator, app,  appdir, inst,instdir,rh,false,false);
		} else if ( tipe.equals("call_system") ){
			JOptionPane.showMessageDialog(null, "TerminalLink: call_system is depressive");
		} else if ( tipe.equals("call") ){
			int x = JOptionPane.showConfirmDialog(null, "TerminalLink: call is depressive, use call_job - You want to quit?");
			if ( x== JOptionPane.OK_OPTION)
				throw new Exception ( "Unknown type of ti: "+tipe);
		} else
			throw new Exception ( "Unknown type of ti: "+tipe);
		
		return ret;
	}
	
	class ConvertResult {
		String basename = null;
		String rightPanel = null;
		String targetHome = null;
		int homeEnd = 0;
		LinkDoesUse linkDoesUse;
		void print (String msg) {
			Sp.p(msg +" ConvertResult.basename  = "+basename );
			Sp.p(msg +" ConvertResult.rightPanel= "+rightPanel );
			Sp.p(msg +" ConvertResult.targetDir = "+targetHome );
		}
		public void setRightButtonsOff() {
			rightPanel="";
		}
		
	}
	
	static void executeDir(String dir, ITerminator terminator ) {
		// Sp.p("TerminalLinkDir;executeDir");
		Sp.p("### A ########## "+dir);
		Element flex = new Element("config");
		
		Element ca = new Element("dir");
		ca.setAttribute("id", "with");
		String goforit = Entities.importEntities(Commons.java2WinInCase(dir));
		Sp.p("                 "+goforit);
		ca.setText(goforit);
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_dir_with,null,null,
									flex,
									Interpreter.SILENCE_VERY,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			//e.printStackTrace();
			(new LogConnector("TerminalLinkDir;executeDir")).warn("The systemcall 'open_dir_with' could not be executed !");
		}		
	}	

	static void executeLoxoBrowser(String dir, ITerminator terminator ) {
		// Sp.p("executeLoxoBrowser: "+dir);
		Sp.p("### 1 ########## "+dir);
		Element flex = new Element("config");
		


		try {
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			ca.setText(Commons.constructNormalizedDir(dir));
			flex.addContent(ca);			
			AppSystemcall x = new AppSystemcall(
									Wellknown.loxo_browser,null,null,
									flex,
									Interpreter.SILENCE_NO,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkDir;Ava Browser")).warn("The systemcall '"+Wellknown.loxo_browser+"' could not be executed !");
		}		
	}
	
	static void executeLoxoActionFile(String file, ITerminator terminator ) {
		// Sp.p("executeLoxoAction-File: "+file);
		Sp.p("### 3 ########## "+file);
		Element flex = new Element("config");
		
		Element ca = new Element("file");
		ca.setAttribute("id", "with");
		ca.setText(file);
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.loxo_action_file,null,null,
									flex,
									Interpreter.SILENCE_NO,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkDir;PanorameActionFile")).warn("The systemcall '"+Wellknown.loxo_action_file+"' could not be executed !");
		}		
	}	
	
	static void executeLoxoActionDir(String dir, ITerminator terminator ) {
		// Sp.p("executeLoxoAction-Dir: "+dir);
		Sp.p("### 2 ########## "+dir);
		Element flex = new Element("config");
		


		try {
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			ca.setText(Commons.constructNormalizedDir(dir));
			flex.addContent(ca);			
			AppSystemcall x = new AppSystemcall(
									Wellknown.loxo_action_dir,null,null,
									flex,
									Interpreter.SILENCE_NO,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkDir;PanoramaActionDir")).warn("The systemcall '"+Wellknown.loxo_action_dir+"' could not be executed !");
		}		
	}		
	public static String getAttUnescaped (Element e, String attname, ITiHandler rh) {
		Attribute a = e.getAttribute(attname);
		if ( a == null ) return null;
		else return Entities.exportEntities(a.getValue());	
	}
	public static String getAttUnescapedXmlHIN (Element e, String attname, ITiHandler rh) {
		Attribute a = e.getAttribute(attname);
		if ( a == null ) return null;
		else return "** "+Entities.exportEntities(Xml(a.getValue()));	
	}	

	
	public static String Xml (String in) {
		if ( in == null ) return null;
		else return goodies(Commons.cdata_html(in));	
	}		

	static String getXUnescaped(Element e, ITiHandler rh) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();
		/*
		String msg2;
		if (Commons.hasFlag(e, "translate")) {
			try {
				msg2= rh.translateString(msg);

			} catch (Exception e1) {
				msg2 = msg;
			}
		} else
			msg2 = msg;*/
		msg = Entities.exportEntities(msg);

		return msg;
	}
	
	static String getXUnescapedXml(Element e, ITiHandler rh) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();
		
		String msg2;
		if (Commons.hasFlag(e, "translate")) {
			try {
				msg2= rh.translateString(msg);

			} catch (Exception e1) {
				msg2 = msg;
			}
		} else
			msg2 = msg;
		msg2 = Xml(Entities.exportEntities(msg2));

		return msg2;
	}	
	static String getX(Element e, ITiHandler rh) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();

		return msg;
	}	
	
	static String getX(Element e) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();

		return msg;
	}	
	

	

	
	/*
		public String getXForHtml(Element e) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null) {
			msg=e.getText();
		}
		
		return getHtmlByTextFromEscaped(msg);
	}
	public static String getForHtmlStatic(String msgx, ISitzung mySitzung) {
		if ( msgx == null )
			return null;
		String msg = msgx;
		if ( mySitzung.isOnline() ) {
			msg = Entities.exportEntities(msg);

			msg = Commons.cdata_html(msg); 

			 								
			msg = HtmlEscape.escapeMeins(msg);
			

			msg = goodies(msg);
			
				
			msg = msg.replaceAll("\n", "<br>");			
		} else {
			msg = Entities.exportEntities(msg);

			msg = Commons.cdata_html(msg);
			

			msg = goodies(msg);
			
				
			msg = msg.replaceAll("\n", "<br>");			
		}
		return "!!5 "+
				msg;
	}	
	*/
	public static String getHtmlByTextFromEscaped(String _msg) {
		if ( _msg == null )
			return null;
		String msg = _msg;
		//System.out.println("HABI-1-input: "+_msg);
		
		msg = Entities.exportEntities(msg);
		//System.out.println("HABI-2-exEnt: "+msg);
		
		msg = TkTicket.html3ByNatural(msg);
		//System.out.println("HABI-3-html : "+msg);
		
		msg = goodies(msg);				
		//System.out.println("HABI-4-goodi: "+msg);
		msg = msg.replaceAll("\n", "<br>");	
		//System.out.println("HABI-5-lineb: "+msg);
		return ( // "!!2 "+
				msg);		
	}
	static String getHtmlByXFromEscaped(Element e) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();
		
		/*if ( msg.indexOf("toprint") > 0 )
			System.out.println("getHtmlByXFromEscaped.toprint.begin="+msg); */

		String resulti = getHtmlByTextFromEscaped(msg);
		
		/* if ( resulti.indexOf("toprint") > 0 )
			System.out.println("getHtmlByXFromEscaped.toprint.end="+resulti); */
		
		return resulti;
		/*
		msg = Entities.exportEntities(msg);
		msg = TkTicket.html3ByNatural(msg);		
		msg = goodies(msg);
		msg = msg.replaceAll("\n", "<br>");
		return // "!!3"+
				msg; */
		// return ( msg.replaceAll(" ", "&nbsp;")); 
	}	
	
	static String getHtmlByXFromEscapedHtml(Element e) {
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();
		msg = Entities.exportEntities(msg);
		// msg = TkTicket.html3ByEscaped(msg);		
		msg = goodies(msg);
		// msg = msg.replaceAll("\n", "<br>");
		return // "!!6"+
				msg;
		// return ( msg.replaceAll(" ", "&nbsp;")); 
	}		
	
	public static String getHtmlByAttrFromEscaped (Element e, String attname, ITiHandler rh) {
		Attribute a = e.getAttribute(attname);
		if ( a == null ) return null;
		String msg = a.getValue();
		return getHtmlByTextFromEscaped(msg);
	/*
	msg = Entities.exportEntities(msg);
	msg = TkTicket.html3ByNatural(msg);
	
	msg = goodies(msg);				
	msg = msg.replaceAll("\n", "<br>");	
	return (  // "!!1 "+
			msg); */
	}	
	/*
	
	public String getForHtmlAUSLAUFMoDELL(String _msg) {
		
		if ( _msg == null )
			return null;

		String msg =  _msg;
		msg = Entities.exportEntities(msg);
		msg = Commons.cdata_html(msg);
		
		if ( isitzung.isOnline() ) {
			 								
			//msg = Commons.text_to_html(msg);
			msg = HtmlEscape.escapeMeins(msg);
					
		} else {

					
		}
		msg = goodies(msg);				
		msg = msg.replaceAll("\n", "<br>");	
	
		return msg;

	}	
	
	static String getXUnescapedLfXml(Element e) {
		if ( e.getAttributeValue("msg")!= null) 
			JOptionPane.showMessageDialog(null, "ti-"+e.getAttributeValue("type")+": msg2 depressive, use x, found="+e.getAttributeValue("msg"));
		
		String msg = e.getAttributeValue("x");
		if ( msg==null)
			msg=e.getText();

		msg = TkTicket.htmlLfByEscaped(msg);
		return msg;

	}		*/

	public static String goodiesRemove ( String in ) {
		String worker = new String(in);
		if ( worker.indexOf("[N:") > -1 ) {
		worker = Commons.stringReplace(worker, "[N:B]oeuw", "");
		worker = Commons.stringReplace(worker, "[F:B]94kd", "");
		worker = Commons.stringReplace(worker, "[N:G]49k", "");
		worker = Commons.stringReplace(worker, "[F:G]pi8d", "");
		
		worker = Commons.stringReplace(worker, "[N:S]p85f", "");
		worker = Commons.stringReplace(worker, "[F:S]p6Juz", "");	
		
		worker = Commons.stringReplace(worker, "[N:U]Pu1u", "");
		worker = Commons.stringReplace(worker, "[F:U]Pl3C", "");	
		worker = Commons.stringReplace(worker, "[N:I]vC12R", "");
		worker = Commons.stringReplace(worker, "[F:I]Pi4q", "");	
		
		worker = Commons.stringReplace(worker, "[N:R]w84t", "");
		worker = Commons.stringReplace(worker, "[F:R]ieks", "");	
		
		worker = Commons.stringReplace(worker, "[N:X]jfu4", "");
		}
		return worker;
		
	}		
	public static String goodies ( String in ) {
		String worker = new String(in);
		if ( worker.indexOf("[N:") > -1 ) {
		worker = Commons.stringReplace(worker, "[N:B]oeuw", "<b>");
		worker = Commons.stringReplace(worker, "[F:B]94kd", "</b>");
		worker = Commons.stringReplace(worker, "[N:G]49k", "<big>");
		worker = Commons.stringReplace(worker, "[F:G]pi8d", "</big>");
		
		worker = Commons.stringReplace(worker, "[N:S]p85f", "<small>");
		worker = Commons.stringReplace(worker, "[F:S]p6Juz", "</small>");	
		
		worker = Commons.stringReplace(worker, "[N:U]Pu1u", "<u>");
		worker = Commons.stringReplace(worker, "[F:U]Pl3C", "</u>");	
		worker = Commons.stringReplace(worker, "[N:I]vC12R", "<i>");
		worker = Commons.stringReplace(worker, "[F:I]Pi4q", "</i>");	
		
		worker = Commons.stringReplace(worker, "[N:R]w84t", "<s>");
		worker = Commons.stringReplace(worker, "[F:R]ieks", "</s>");	
		
		worker = Commons.stringReplace(worker, "[N:X]jfu4", "<br/>");	
		}
		return worker;
		
	}		

}