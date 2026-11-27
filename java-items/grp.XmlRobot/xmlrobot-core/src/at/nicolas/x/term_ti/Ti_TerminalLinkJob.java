/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.int_call.AppJobexecutionUsage;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_TerminalLinkJob extends Ti_TerminalLink {

	LinkTextResponse wert;
	Element eE;
	String prefix="";
	String postfix="";

	public Ti_TerminalLinkJob(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app,appdir, inst,instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
		
		if (eE.getAttribute("prefix") != null )
			prefix = getHtmlByTextFromEscaped(eE.getAttributeValue("prefix"));
		
		
		if (eE.getAttribute("postfix") != null )
			postfix = getHtmlByTextFromEscaped(eE.getAttributeValue("postfix"));		
		
		//0801
		// Sp.p("e-Ti_TerminalLinkJob="+Commons.toTextRaw(e));
		
		String visible = null;
		if ( eE.getAttribute("pic") != null )
			visible = Ti_TerminalLinkImg.getImgHtml(eE, rh);
			//visible = getAttUnescapedXml(eE,"explain",rh);
		else if ( eE.getAttribute("explain") != null ) 
			visible = eE.getAttributeValue("explain");
		else 
			visible = eE.getAttributeValue("job"); 
		wert = getHtmlLinktextFromEscaped(visible,eE,"tiButtonJob");
	}

	@Override
	public Ti_HtmlReturn getHtml() {
		Ti_HtmlReturn ret = null;
		if ( Tc_Terminator.stilIsWeb ) {
			/*( ( isitzung.triggerButtonsByButton() &&  Commons.hasFlag(tiElement, "button"+extension))
					|| (!Commons.hasFlag(tiElement, "nobutton"+extension))				
					)*/
			if ( (rh.triggerButtonsByButton() &&  Commons.hasFlag(eE, "button")) ||
					 (!rh.triggerButtonsByButton() &&  !Commons.hasFlag(eE, "nobutton"))) {		
			// if ( Commons.hasFlag(eE, "button")) {
				 ret =  new Ti_HtmlReturn(
							Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
							prefix+
							"<div "+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerJobBLACKClass())+">"+
							"<a "+ts.getHref(getRefCode())+
							wert.getHtmlClasses(Tc_Terminator.stil.getAnkerJobBLACKClass())+">"+
							wert.getLinktext()+"</a>"+postfix+"</div>",
							null);								
			} else {
				 ret =  new Ti_HtmlReturn(
							Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
							prefix+
							"<div "+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerJobPlainBLACKClass())+">"+
							"<a "+ts.getHref(getRefCode())+
							wert.getHtmlClasses(Tc_Terminator.stil.getAnkerJobPlainBLACKClass())+">"+
							wert.getLinktext()+"</a>"+postfix+"</div>",
							null);				
				
			}

		} else {
			ret =  new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
					prefix+"<a "+ts.getHref(getRefCode())+
					Tc_Terminator.stil.getAnkerJobBLACK()+">"+wert.getLinktext()+"</a>"+postfix,
					null);	
		}
		/*
		Sp.p("========================");
		Sp.p("========================");
		Sp.p("========================");
		Sp.p("LINK-JOB-STIL="+Tc_Terminator.stil.getAnkerJobBLACK());
*/
		ret.setKeyLabelRight(eE,rh.getSitzung());	
		
		ArrayList<RefCode> refcodes = new ArrayList<RefCode>();
		refcodes.add(new RefCode(getRefCode(),"Run: job="+eE.getAttributeValue("job")));
		ret.setRefcodes(refcodes);
		return ret;			
		
	}
	
 
	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		return wert.getLinktext();
	}

	@Override
	public void executeLink() {
		LogConnector lc = new LogConnector("TerminalLinkJob");
		String job = getAttUnescaped(eE,"job",rh);
		// lc.core("Starting to execute job "+job+" ...",LogConnector.CORE_TERMINAL);
		int silence = Interpreter.SILENCE_NO;
		if ( Commons.hasFlag(eE, "silent"))
			silence = Interpreter.SILENCE_YES;

		AppJobexecutionUsage x = new AppJobexecutionUsage ( 	
				app,
				inst,
				instdir,
				job,
				eE,
				false,
				silence,
				terminator,
				0,
				null);		
		try {
			x.run();
		} catch (Exception e) {
			Sp.pe(e);
		}	

	}

	@Override
	public Interpreter createInterpreterToReact(
			String mark,
			boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
		String job = getAttUnescaped(eE,"job",rh);
		// lc.core("Starting to execute job "+job+" ...",LogConnector.CORE_TERMINAL);
		int silence = Interpreter.SILENCE_NO;
		if ( Commons.hasFlag(eE, "silent"))
			silence = Interpreter.SILENCE_YES;
		
		 Interpreter ret = JobRunner.createInterpreter(
				job, 
				false, 
				app, 
				inst, 
				instdir, 
				eE, 
				null, 
				silence, 
				null, 
				0, 
				interWebModus,
				interWebModus,
				terminator,
				false,
				guiController,
				isitzung,false);
			ArrayList<Element> configList = 
					CoreBuilder.db.getExecutionPrologUsage(
							app, 
							inst, 
							instdir,
							true,
							isitzung);
			ret.addConfigList(configList);
			return ret;
	
	}

}