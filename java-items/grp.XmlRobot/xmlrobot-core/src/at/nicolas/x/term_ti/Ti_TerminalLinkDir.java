/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.Surrounding;
import at.nicolas.x.int_specs.TkChooseException;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_TerminalLinkDir extends Ti_TerminalLink {


	Element e;
	ConvertResult cv;
	boolean no_loxo = false;
	private Surrounding surrounding = null;
	private ISitzung sitzung = null;
	
	
	public Ti_TerminalLinkDir(Element e, ITerminator _terminator, String app, String appdir,
			String inst,String instdir, ITiHandler rh) {
		super(e, _terminator, app, appdir,inst,instdir,rh);
		// TODO Auto-generated constructor stub
		// Sp.p("rh dir="+rh);
		this.e = e;
		no_loxo = Commons.hasFlag(this.e, "no_pano");
		surrounding = rh.getSurrounding();
		sitzung = rh.getSitzung();
		
		//Sp.p("* * * * Ti_TerminalLink DIR:"+Commons.toText(e));
		
		
	}


	@Override
	public Ti_HtmlReturn getHtml() {
		// String loxoDirActionCode = null;
		String myOrigDir = Ti_TerminalLink.getXUnescaped(e,rh);
		
		if ( myOrigDir.equals("?")) {
			Ti_HtmlReturn y= new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(e, "label", null),
						"?",
						null,
						false,
						Commons.hasFlag(e, "label_right"),
						false);
			return y;
		} else {		
			String myCanoDir = myOrigDir; 
			ArrayList<RefCode> refcodes = new ArrayList<RefCode>();
	
			if (!Commons.hasFlag(e, "no_can"))
				try {
					//myCanoDir = Commons.getCanoDirXrIfExists(myCanoDir);
					// System.out.println("AAAAAAAAAAAAAAAAAa: "+myCanoDir);
					myCanoDir = Commons.constructNormalizedDir(myCanoDir);
					// System.out.println("BBBBBBBBBBBBBBBBBb: "+myCanoDir);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					myCanoDir = myOrigDir;
				}		
			
			// Commons.cano_dir(myOrigDir);
			// Sp.p("****  **** TermDir, orig=["+myOrigDir+"] cano=["+myCanoDir+"]");
			LinkConverterRequest lcr1;
			if ( Tc_Terminator.stilIsWeb ) 
				// nic123
				lcr1 = sitzung.getLinkConverterRequest();
			else
				lcr1 = new LinkConverterRequest(true,true,true,true,true,true,true,true,true,true,true,true);
			
			cv = convertFileDirText(
					Xml(myCanoDir), 
					getRefCode(1), 
					getRefCode(2),
					getRefCode(4),
					true,
					e.getAttribute("len"),
					e.getAttribute("len2"),
					lcr1);		
			
			// Sp.p("TerminalLinkDir: cv.html = "+cv.html +", remainer="+cv.remainer); // font-weight:bold;
			String partDownload = "";
			
			if ( Tc_Terminator.stilIsWeb && 
					( ( Commons.hasFlag(e, "download") && sitzung.hasPrivDownFiles() ) 
					|| 
					  ( Commons.hasFlag(e, "downloadpro") && sitzung.hasPrivDownPro() ) )  ){
				
				LinkTextResponse ltr = getHtmlLinktextFromEscaped("Download",e,"tiButtonDownload tiButtonDownloadDir","download");
				partDownload = 
						"<div "+ltr.getHtmlClasses(" tiDownloadDir")+">"+
						"<a "+ts.getHref(getRefCode(9))+
						//ltr.getHtmlClasses(Tc_Terminator.stil.getAnkerULClass(false)+" tiDownloadDir")
						ltr.getHtmlClasses(" tiDownloadDir")
						+">"+
						ltr.getLinktext()
						+"</a></div>";
				
			}
			String partChoose = "";
			boolean dirIsPartOfChoose = false;
			if ( Tc_Terminator.stilIsWeb && Commons.hasFlag(e, "choose") && terminator.getDirChoose()) {
				dirIsPartOfChoose = true;
				String call_choose = "Choose";
				if ( e.getAttribute("call_choose") != null ) {
					call_choose = getHtmlByAttrFromEscaped(e,"call_choose",rh); // e.getAttributeValue("call_choose");
				}
				LinkTextResponse ltrChoose = getHtmlLinktextFromEscaped(call_choose,e,"tiButtonChoose tiButtonChooseDir","choose");
				
				partChoose = 
						"<div "+ltrChoose.getHtmlClasses("tiChooseDir")+">"+
						"<a "+ts.getHref(getRefCode(10))+
						//Tc_Terminator.stil.getAnkerUL(false)+">"+
						ltrChoose.getHtmlClasses("tiChooseDir")+">"+
						call_choose+"</a></div>";
			}
			Ti_HtmlReturn ret;
			boolean rightButtonsOn = true;
			
			if ( Commons.hasFlag(e, "naked")) {
				rightButtonsOn = false;
				cv.setRightButtonsOff();
			}
			
				
			if ( Commons.hasFlag(e, "plain")) {
				
				LinkTextResponse ltrPlain = getHtmlLinktextFromEscaped(Commons.cutDir2Att(cv.basename, e.getAttribute("len")),e,"","plain");

				
				ret= new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(e,"label",null),
						partChoose+
						"<div "+ltrPlain.getHtmlClasses("stdPlainDir")+">"+
						ltrPlain.getLinktext()
						+"</div> "+partDownload,				
						"");			
				rightButtonsOn = false;
			} else if ( Commons.hasFlag(e, "blind")) {
				
				LinkTextResponse ltrPlain = getHtmlLinktextFromEscaped(Commons.cutDir2Att(cv.basename, e.getAttribute("len")),e,"","plain");

				
				ret= new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(e,"label",null),
						partChoose
						//+"<div "+ltrPlain.getHtmlClasses("stdPlainDir")+">"+
						//ltrPlain.getLinktext()
						//+"</div> "
						+partDownload,				
						"");			
				rightButtonsOn = false;
			} else if ( !cv.linkDoesUse.clickRemainer ) {

				ret= new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(e,"label",null),
						partChoose+
						"<div "+Tc_Terminator.stil.getAnkerDirBLACK(dirIsPartOfChoose)+">"+
						"<span "+
						Tc_Terminator.stil.getAnkerDirBLACK(dirIsPartOfChoose)+">"+
						Commons.cutDir2Att(cv.basename, e.getAttribute("len"))+"</span></div> " +partDownload
						,
						cv.rightPanel);	

			} else {

				ret= new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(e,"label",null),
						partChoose+
						// "<div "+Tc_Terminator.stil.getAnkerDirBLACK(dirIsPartOfChoose)+">"+
						"<a "+ts.getHref(getRefCode(3))+
						Tc_Terminator.stil.getAnkerDirBLACK(dirIsPartOfChoose)+">"+
						Commons.cutDir2Att(cv.basename, e.getAttribute("len"))+"</a>"+
						//"</div> " +
						partDownload
						,
						cv.rightPanel);	

			}
			
			
			boolean set_wrap = false;		
			if ( Commons.hasFlag(e, "wrap"))
				set_wrap = true;
			ret.setMainNowrap(!set_wrap);
			ret.setKeyLabelRight(e,rh.getSitzung());
			
			//
			/*
			if ( chooserHtml != null )
				refcodes.add(new RefCode(getRefCode(8),"perform CHOOSE"));
			*/
			if ( partDownload != null )
				refcodes.add(new RefCode(getRefCode(9),"perform DOWNLOAD"));
			if ( partChoose != null )
				refcodes.add(new RefCode(getRefCode(10),"perform CHOOOSE"));		
			if ( Tc_Terminator.stilIsWeb ) {
				if ( cv.linkDoesUse.clickRemainer )
					refcodes.add(new RefCode(getRefCode(3),"The Directory"));
				if ( rightButtonsOn ) {
					if ( cv.linkDoesUse.home )
						refcodes.add(new RefCode(getRefCode(1),"SECOND PARENT/HOME"));
					if ( cv.linkDoesUse.dir)
						refcodes.add(new RefCode(getRefCode(2),"PARENT"));
				}
				
			} else {
				refcodes.add(new RefCode(getRefCode(1),"Parent Directory"));
				refcodes.add(new RefCode(getRefCode(2),"PARENT"));
				refcodes.add(new RefCode(getRefCode(3),"The Directory"));
				refcodes.add(new RefCode(getRefCode(4),"Run Dir-Activity"));
				refcodes.add(new RefCode(getRefCode(9),"Download Folder"));
			}		
			//	
	
			ret.setRefcodes(refcodes);
						
			ret.setFlow(Commons.hasFlag(e, "flow"));
			
			return ret;
		}
		
	}
	

	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		return Ti_TerminalLink.getXUnescaped(e,rh);
	}


	public void reactOn(String mark ) {
		// Sp.p("TermialLinkDir-react on-target="+cv.targetHome);
		if ( mark.startsWith(getRefCode(1)) ) {	
			// Sp.p("thats the mother-dir");
			executeDir(cv.targetHome, terminator);
		} else if ( mark.startsWith(getRefCode(3)) ) {	
			// Sp.p("thats the effective dir");
			executeLink();
		} else if ( mark.startsWith(getRefCode(2)) ) {	
			// Sp.p("thats the effective dir");
			try {
				executeDir(Commons.getDirOfDir(Ti_TerminalLink.getXUnescaped(e,rh)), terminator);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Sp.pe(e);
			}
		}  else if ( mark.startsWith(getRefCode(4)) ) {	
			// Sp.p("thats the activity");
			executeLoxoActionDir(Ti_TerminalLink.getX(e,rh), terminator);
		}  else if ( mark.startsWith(getRefCode(10)) ) {	
			// Sp.p("thats the activity");
			executeLoxoActionDir(Ti_TerminalLink.getX(e,rh), terminator);
		}			
	}	
	public void executeLink() {
		// Sp.p("TerminalLinkDir;Execute");
		Element flex = new Element("config");
		
		Element ca = new Element("dir");
		ca.setAttribute("id", "with");
		ca.setText(Commons.java2WinInCase(Ti_TerminalLink.getX(e,rh)));
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_dir_with,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkDir")).warn("The systemcall '"+Wellknown.open_dir_with+"' could not be executed !");
		}

	}


	@Override
	public Interpreter createInterpreterToReact(String mark, boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
		if ( interWebModus == false )
			throw new Exception("Ti_TerminalLinkDir nur im Webmodus implementiert");		
		if ( mark.startsWith(getRefCode(1)) ) {	
			// PARENT-DIRECTORY
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			ca.setText(Entities.importEntities(Commons.java2WinInCase(cv.targetHome)));
			flex.addContent(ca);
			

				 return JobRunner.createInterpreter(
						 "Goto-Panorama", 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_NO, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,false);				

		} else if ( mark.startsWith(getRefCode(3)) ) {	
			// THE DIRECTORY
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			ca.setText(Commons.java2WinInCase(Ti_TerminalLink.getX(e,rh)));
			flex.addContent(ca);
			
			if ( interWebModus ) {
				 return JobRunner.createInterpreter(
						 "Goto-Panorama", 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_NO, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,
							false);				
			} else {
				 return JobRunner.createInterpreter(
						 Wellknown.open_dir_with, 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_YES, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,
							false);				
			}
			

		}  else if ( mark.startsWith(getRefCode(2)) ) {	
			// THE UPPER-DIRECTORY
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			
			ca.setText(Commons.java2WinInCase(Commons.getDirOfDir(Ti_TerminalLink.getX(e,rh))));
			flex.addContent(ca);
			
			if ( interWebModus ) {
				 return JobRunner.createInterpreter(
						 "Goto-Panorama", 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_NO, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,false);				
			} else {
				 return JobRunner.createInterpreter(
						 Wellknown.open_dir_with, 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_YES, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,false);				
			}
			

		} else if ( mark.startsWith(getRefCode(4)) ) {	
			// ACTIVITY-f
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			ca.setText(Commons.java2WinInCase(Ti_TerminalLink.getX(e,rh)));
			flex.addContent(ca);
			
			if ( interWebModus ) {
				 return JobRunner.createInterpreter(
						 Wellknown.see_dir_actions, 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_NO, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,false);				
			} else {
				 return JobRunner.createInterpreter(
						 Wellknown.see_dir_actions, 
							true, 
							app, 
							inst, 
							instdir, 
							flex, 
							null, 
							Interpreter.SILENCE_NO, 
							null, 
							0, 
							interWebModus, 
							interWebModus,
							terminator,
							true,
							guiController,
							isitzung,false);				
			}
		} else if ( mark.startsWith(getRefCode(9))) {
			Sp.p("Did DOWNLOAD for: "+Commons.toText(e));
			throw new TkChooseException(
					"blabla",
					"dir",
					Ti_TerminalLink.getXUnescaped(e,rh),
					"download");
		} else if ( mark.startsWith(getRefCode(10))) {
			Sp.p("Did CHOOSE for: "+Commons.toText(e));
			throw new TkChooseException(
					"blabla",
					"dir",
					Ti_TerminalLink.getXUnescaped(e,rh),
					"choose");
		}		
		return null;
	}
	


}