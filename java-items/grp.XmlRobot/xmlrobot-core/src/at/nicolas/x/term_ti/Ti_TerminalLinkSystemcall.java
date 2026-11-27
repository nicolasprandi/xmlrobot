/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.WebVfs.JobRunner;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.Entities;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_TerminalLinkSystemcall extends Ti_TerminalLink {

	LinkTextResponse wert;
	Element eE;
	String prefix="";
	String postfix="";	
	public Ti_TerminalLinkSystemcall(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir,inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
			
		String visible = null;
		if ( eE.getAttribute("pic") != null )
			visible = Ti_TerminalLinkImg.getImgHtml(eE, rh);
			//visible = getAttUnescapedXml(eE,"explain",rh);
		else if ( eE.getAttribute("explain") != null ) 
			visible = eE.getAttributeValue("explain");
		else 
			visible = eE.getAttributeValue("job"); 
		wert = getHtmlLinktextFromEscaped(visible,eE,"tiButtonService");

		if (eE.getAttribute("prefix") != null )
			prefix = getHtmlByTextFromEscaped(eE.getAttributeValue("prefix"));
		
		if (eE.getAttribute("postfix") != null )
			postfix = getHtmlByTextFromEscaped(eE.getAttributeValue("postfix"));	
		
		/*if ( !Commons.hasFlag(eE, "no_arrow") )
			wert = "&rarr; "+ visible;	
		else */ 
		
	}

	@Override
	public Ti_HtmlReturn getHtml() {
		Ti_HtmlReturn ret = null;
/*
 * 		if ( Tc_Terminator.stilIsWeb ) {
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
					prefix+"<a "+ts.getHref(getRefCode())+
					Tc_Terminator.stil.getAnkerJobBLACK()+">"+wert.getLinktext()+"</a>"+postfix,
					null);	
		}
 * 		
 */
		if ( Tc_Terminator.stilIsWeb ) {
			if ( (rh.triggerButtonsByButton() &&  Commons.hasFlag(eE, "button")) ||
				 (!rh.triggerButtonsByButton() &&  !Commons.hasFlag(eE, "nobutton"))) {	
				ret = new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
							prefix+
							"<div "+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerServiceBLACKClass())+">"+
							"<a "+ts.getHref(getRefCode())
							+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerServiceBLACKClass())+"\">"+
							wert.getLinktext()+"</a>"+postfix+"</div>",
							null);				
				
			} else {
				ret = new Ti_HtmlReturn(
						Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
							prefix+
							"<div "+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerServicePlainBLACKClass())+">"+
							"<a "+ts.getHref(getRefCode())
							+wert.getHtmlClasses(Tc_Terminator.stil.getAnkerServicePlainBLACKClass())+"\">"+
							wert.getLinktext()+"</a>"+postfix+"</div>",
							null);				
				
			}

		} else {
			 ret = new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						prefix+"<a "+ts.getHref(getRefCode())+
						Tc_Terminator.stil.getAnkerServiceBLACK()+">"+wert.getLinktext()+"</a>"+postfix,
						null);			
		}
		ret.setKeyLabelRight(eE,rh.getSitzung());	
		
		ArrayList<RefCode> refcodes = new ArrayList<RefCode>();
		refcodes.add(new RefCode(getRefCode(),"Run: service="+eE.getAttributeValue("job")));
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
		LogConnector lc = new LogConnector("TerminalLinkSystemcall");
		String job = Entities.exportEntities(eE.getAttributeValue("job"));
		
		// lc.core("Starting to execute systemcall "+job+" based "+inst+"."+app,LogConnector.CORE_TERMINAL);
		int silence = Interpreter.SILENCE_NO;
		if ( Commons.hasFlag(eE, "silent"))
			silence = Interpreter.SILENCE_YES;
		try {
			
			lc.core("Starting systemcall "+job+":"+inst+":"+app+":",LogConnector.CORE_TERMINAL);
			AppSystemcall x = new AppSystemcall(
					job,app,inst,
					eE,
					silence,
					terminator,0,instdir);
			x.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}	

	}

	@Override
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
		// TODO Auto-generated method stub
		String job = Entities.exportEntities(eE.getAttributeValue("job"));
		
		// lc.core("Starting to execute systemcall "+job+" based "+inst+"."+app,LogConnector.CORE_TERMINAL);
		int silence = Interpreter.SILENCE_NO;
		if ( Commons.hasFlag(eE, "silent"))
			silence = Interpreter.SILENCE_YES;
		 return JobRunner.createInterpreter(
					job, 
					true, 
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
					true,
					guiController,
					isitzung,false);		
	}

}