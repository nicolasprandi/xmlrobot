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
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_TerminalLinkFile extends Ti_TerminalLink {

	Element eE;
	boolean bold = false;
	ConvertResult cv;
	private Surrounding surrounding = null;
	private ISitzung isitzung = null;
	

	public Ti_TerminalLinkFile(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir,inst,instdir,rh);

		// TODO Auto-generated constructor stub
		this.eE = eE; 
		bold = Commons.hasFlag(this.eE, "focus");
		surrounding = rh.getSurrounding();
		isitzung = rh.getSitzung();
		//Sp.p("* * * * Ti_TerminalLink FILE: requ="+surrounding.getLinkConverterRequest().getDoku());
		// Sp.p("* * * * Ti_TerminalLink FILE:"+Commons.toText(e));
	}

	
	@Override
	public Ti_HtmlReturn getHtml() {

		String add_bold ="";
		if ( bold)
			add_bold = " font-weight:bold;";
		
		String myOrigFile = Ti_TerminalLink.getXUnescaped(eE,rh);
		
		if ( myOrigFile.equals("?")) {
			Ti_HtmlReturn y= new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						"?",
						null,
						false,
						Commons.hasFlag(eE, "label_right"),
						false);
			return y;			
		} else {		
		
			if (!Commons.hasFlag(eE, "no_can"))
				try {
					if ( myOrigFile != null && myOrigFile.length() > 0 ) {
						myOrigFile = Commons.constructNormalizedFile(myOrigFile);
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Sp.p("schaut nach cano-problem aus ["+myOrigFile+"]");
					myOrigFile = Ti_TerminalLink.getXUnescaped(eE,rh);
				}			
	
			int index_first_dot = -1;
			// Sp.p("STIL IS WEB====="+Tc_Terminator.stilIsWeb);
	
			if ( Tc_Terminator.stilIsWeb ) {
				
				// Sp.p("------ DiESER "+ rh.getSurrounding().getLinkConverterRequest().getDoku());
				//LinkConverterRequest lcr1 = new LinkConverterRequest(false,true,false,false,false,false);
				LinkConverterRequest lcr1 = isitzung.getLinkConverterRequest();

				cv = convertFileDirText(
						Xml(myOrigFile), 
						getRefCode(1), 
						getRefCode(2), 
						getRefCode(6) , 
						false,
						eE.getAttribute("len"),
						eE.getAttribute("len2"),
						lcr1);

				
				// index_first_dot = Ti_TerminalLinkRec.getIndexDotExtension(cv.remainer);			
			} else {
				LinkConverterRequest lcr2 = new LinkConverterRequest(true,true,true,true,true,true,true,
						true,true,true,true,true);
				cv = convertFileDirText(Xml(myOrigFile), 
						getRefCode(1), 
						getRefCode(2), 
						getRefCode(6) , 
						false,
						eE.getAttribute("len"),
						eE.getAttribute("len2"),
						lcr2);
				index_first_dot = Ti_TerminalLinkRec.getIndexDotExtension(cv.basename);			
			}
	
			boolean rightButtonsOn = true;
			Ti_HtmlReturn ret = null;
			
			if ( Commons.hasFlag(eE, "naked")) {
				rightButtonsOn = false;
				//System.out.println("in simple_mode");
			}/* else 
				System.out.println("not in simple_mode: "+eE.getAttributeValue("flag"));*/
				
			
			String partChoose = null;
			if ( Commons.hasFlag(eE, "choose") && terminator.getFileChoose() ) {
	
				// bekommt refcode 8
				//String fname = Ti_TerminalLink.getXUnescaped(eE,rh);
				
				String call_choose = "Choose";
				if ( eE.getAttribute("call_choose") != null ) {
					call_choose = getHtmlByAttrFromEscaped(eE,"call_choose",rh); // eE.getAttributeValue("call_choose");
				}
	
				LinkTextResponse ltrChoose = getHtmlLinktextFromEscaped(call_choose,eE,"tiButtonChoose tiButtonChooseFile","choose");

				
				String prefix = "";
				if (eE.getAttribute("prefix") != null )
					prefix = getHtmlByTextFromEscaped(eE.getAttributeValue("prefix"));
				
				partChoose = prefix+
				"<div "+ltrChoose.getHtmlClasses("tiChooseFile")+">"+		
				" <a "+ltrChoose.getHtmlClasses("tiChooseFile")+ts.getHref(getRefCode(8))+">"+
				call_choose+
				"</a></div>";
				//rightButtonsOn = false;
				
			} 
			String partBasename1 = null;
			String partBasename2 = null;
			String partBasenameIntermediate = null;
			String partDownload = null;		
	
			if ( Commons.hasFlag(eE, "blind")) {
				partBasename1 = "";		
				rightButtonsOn = false;
			} else if ( !cv.linkDoesUse.clickRemainer ) {
				partBasename1 = "<span "+
						Tc_Terminator.stil.getAnkerFile(bold)+">"+
						cv.basename +
						// Ti_TerminalLink.getXUnescaped(eE, rh) +
						"</span>";
				
				//System.out.println("A part1="+part1);
				
			} else  if ( index_first_dot == -1 ) {
	
				partBasename1 = "<a "+
						ts.getHref(getRefCode(3))+
						Tc_Terminator.stil.getAnkerFile(bold)+">"+
						cv.basename +
						// Ti_TerminalLink.getXUnescaped(eE, rh) +
						"</a>";
				//System.out.println("B part1="+part1);
			} else {
				String textOfPart1 = Ti_TerminalLink.cutFile2(cv.basename.substring(0, index_first_dot),eE.getAttribute("len"));
				String textOfPart2 = cv.basename.substring(index_first_dot+1);
				
				partBasename1 = "<a "+ts.getHref(getRefCode(7))+
						Tc_Terminator.stil.getAnkerFile(bold)+">"+
						textOfPart1+"</a>";
				
				partBasename2 = "<a "+ts.getHref(getRefCode(3))+
						Tc_Terminator.stil.getAnkerFile(false)+">"+
						textOfPart2+"</a>";
				partBasenameIntermediate = "<u>.</u>";
				
				//System.out.println("C part1="+part1);
				//System.out.println("C part2="+part2);
				//System.out.println("C partIntermediate="+partIntermediate);
	
				
			}
		
			if ( Tc_Terminator.stilIsWeb && 
					( ( Commons.hasFlag(eE, "download") && isitzung.hasPrivDownFiles() ) 
					|| 
					  ( Commons.hasFlag(eE, "downloadpro") && isitzung.hasPrivDownPro() ) )  ) {
				LinkTextResponse ltr = getHtmlLinktextFromEscaped("Download",eE,"tiButtonDownload tiButtonDownloadFile","download");
				partDownload = 
						"<div "+ltr.getHtmlClasses(" tiDownloadFile")+ ">"+
						"<a "+ts.getHref(getRefCode(9))+
						//ltr.getHtmlClasses(Tc_Terminator.stil.getAnkerULClass(false)+" tiDownloadFile")
						ltr.getHtmlClasses(" tiDownloadFile")
						+">"+
						ltr.getLinktext()
						+"</a></div>";						
			}
					
			//System.out.println("D partAction="+partAction);
			
			String partComposit = "";
			if ( partChoose !=null) partComposit = partComposit+partChoose;
			
			if ( partBasename1 !=null) partComposit = partComposit+partBasename1;
			if ( partBasenameIntermediate !=null) partComposit = partComposit+partBasenameIntermediate;
			if ( partBasename2 !=null) partComposit = partComposit+partBasename2;
			if ( partDownload !=null) partComposit = partDownload+ partComposit;
			
			//System.out.println("E partComposit="+partComposit);
			if (!rightButtonsOn) {
				ret = new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						partComposit,
						"" );					
			}else {
				ret = new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						partComposit,
						cv.rightPanel );					
			}
		
			
			boolean set_wrap = false;		
			if ( Commons.hasFlag(eE, "wrap"))
				set_wrap = true;
			ret.setMainNowrap(!set_wrap);
			ret.setFlow(Commons.hasFlag(eE, "flow"));
			
			ret.setKeyLabelRight(eE,rh.getSitzung());		
			
			ArrayList<RefCode> refcodes = new ArrayList<RefCode>();
			if ( partChoose != null )
				refcodes.add(new RefCode(getRefCode(8),"perform CHOOSE"));
			if ( partDownload != null )
				refcodes.add(new RefCode(getRefCode(9),"perform DOWNLOAD"));
			if ( Tc_Terminator.stilIsWeb ) {
				if ( cv.linkDoesUse.clickRemainer )
					refcodes.add(new RefCode(getRefCode(3),"open the item"));				
				if ( rightButtonsOn ) {

					if ( cv.linkDoesUse.home )
						refcodes.add(new RefCode(getRefCode(1),"SECOND PARENT/HOME"));
					if ( cv.linkDoesUse.dir)
						refcodes.add(new RefCode(getRefCode(2),"PARENT"));
				}
				
			} else {
				refcodes.add(new RefCode(getRefCode(7),"open TYPICAL"));
				refcodes.add(new RefCode(getRefCode(3),"the item"));
				refcodes.add(new RefCode(getRefCode(6),"ACTIONS"));
				refcodes.add(new RefCode(getRefCode(1),"SECOND PARENT/HOME"));
				refcodes.add(new RefCode(getRefCode(2),"PARENT"));
			}
			
			
				
			//refcodes.add(new RefCode(getRefCode(3),"The Directory"));
			//refcodes.add(new RefCode(getRefCode(4),"Run Dir-Activity"));
			ret.setRefcodes(refcodes);
			
			return ret;
		}
	}

	@Override
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus
			,IGuiController guiController, ISitzung isitzung ) throws Exception {
		if ( interWebModus == false )
			throw new Exception("Ti_TerminalLinkFile nur im Webmodus implementiert");
		
		if ( mark.startsWith(getRefCode(8))) {
			Sp.p("Did choose for: "+Commons.toText(eE));
			throw new TkChooseException(
					"blabla",
					"file",
					Ti_TerminalLink.getXUnescaped(eE,rh),
					"choose");
		} else if ( mark.startsWith(getRefCode(9))) {
			Sp.p("Did DOWNLOAD for: "+Commons.toText(eE));
			throw new TkChooseException(
					"blabla",
					"file",
					Ti_TerminalLink.getXUnescaped(eE,rh),
					"download");
		} else if ( mark.startsWith(getRefCode(6)) ||  mark.startsWith(getRefCode(3)) || mark.startsWith(getRefCode(7))) {
			Sp.p("X1: ");
			Element flex = new Element("config");
			
			Element ca = new Element("file");
			ca.setAttribute("id", "with");
			//ca.setText(Ti_TerminalLink.getXUnescaped(eE,rh));
			ca.setText(Ti_TerminalLink.getX(eE,rh));
			
			flex.addContent(ca);
			
			 return JobRunner.createInterpreter(
					 Wellknown.see_file_actions, 
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
			
		} else if ( mark.startsWith(getRefCode(2))) {
			Sp.p("X2: ");
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			
			ca.setText(Commons.getDirOfFile(Ti_TerminalLink.getX(eE,rh)));
			flex.addContent(ca);
			
			 return JobRunner.createInterpreter(
					 Wellknown.loxo_browser, 
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
			
		} else if ( mark.startsWith(getRefCode(1))) {
			Sp.p("X3: ");
			Element flex = new Element("config");
			
			Element ca = new Element("dir");
			ca.setAttribute("id", "with");
			
			ca.setText(cv.targetHome);
			flex.addContent(ca);
			
			 return JobRunner.createInterpreter(
					 Wellknown.loxo_browser, 
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
		throw new Exception("kkaksdf");
	}	
	
	public void reactOn(String mark )  {
		//System.out.println("bin im react file");
		try {
			if ( mark.startsWith(getRefCode(1)) ) {	
				//Sp.p("Link 1: targethome="+cv.targetHome);
				// das ist das zweite directory das benannte
				//Sp.p("------------- [1]");
				
				executeDir(cv.targetHome, terminator);
			} else if ( mark.startsWith(getRefCode(2)) ) {
				//Sp.p("------------- [2] "+Ti_TerminalLink.getXUnescaped(eE,rh));
				// Sp.p("Link 2: ParentDirectory");
				// das ist das erste directory, das parent directory
				executeDir(Commons.getDirOfFile(Ti_TerminalLink.getXUnescaped(eE,rh)), terminator);			
			} else if ( mark.startsWith(getRefCode(4)) ) {
				//Sp.p("------------- [4]");
				// Sp.p("Link 4:");
				executeLink();
			} else if ( mark.startsWith(getRefCode(3)) ) {
				//Sp.p("------------- [3]");
				// Sp.p("Link 3: open by extension");
				// das ist die extension
				executeLinkExtension();
			}  else if ( mark.startsWith(getRefCode(7)) ) {
				//Sp.p("------------- [7]");
				// Sp.p("Link 7: Typical");
				// link auf den filenamen
				executeLinkTypical();
			} else if ( mark.startsWith(getRefCode(5)) ) {
				//Sp.p("------------- [5]");
				// Sp.p("Link 5:");
				executeLoxoBrowser(Commons.getDirOfFile(Ti_TerminalLink.getX(eE,rh)), terminator);	
			} else if ( mark.startsWith(getRefCode(6)) ) {
				//Sp.p("------------- [6]");
				// Sp.p("Link 6: Activvity");
				// das ist die operation, das 'f'
				executeLoxoActionFile((Ti_TerminalLink.getX(eE,rh)), terminator);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Tc_Terminator.valueProblem("Ti_Terminal.reactOn()", e);
		}				
	}
	

	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		return Ti_TerminalLink.getXUnescaped(eE,rh);
	}

	@Override
	public void executeLink() {
		Element flex = new Element("config");
		
		Element ca = new Element("file");
		ca.setAttribute("id", "with");
		ca.setText(Commons.fileClassic(Ti_TerminalLink.getX(eE,rh)));
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_file_with,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkFile")).warn("The service 'open_file_with' could not be executed !");
		}

	}
	
	public void executeLinkExtension() {
		Element flex = new Element("config");
		
		Element ca = new Element("file");
		ca.setAttribute("id", "with");
		// Sp.p("myOrigFile="+Ti_TerminalLink.getMsgLink(e,rh));
		String y = (Ti_TerminalLink.getX(eE,rh));
		
		y = Commons.fileClassic(y);
		if ( (System.getProperty("os.name")).startsWith("Windows")) {
			// y=Commons.stringReplace(y, "/", "\\");
		}
		
		ca.setText(y);
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_file_selection,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkFile")).warn("The service '"+Wellknown.open_file_selection+"' could not be executed !");
		}

	}	
	
	public void executeLinkTypical() {
		Element flex = new Element("config");
		
		Element ca = new Element("file");
		ca.setAttribute("id", "with");
		ca.setText(Commons.fileClassic(Ti_TerminalLink.getX(eE,rh)));
		flex.addContent(ca);

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_file_selection_typical,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,
									0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkFile")).warn("The service '"+Wellknown.open_file_selection_typical+"' could not be executed !");
		}

	}


	

}