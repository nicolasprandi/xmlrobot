/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import org.jdom2.Element;

import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_TerminalLinkWeb extends Ti_TerminalLink {

	Element eE;
	public Ti_TerminalLinkWeb(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir, inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
	}

	@Override
	public Ti_HtmlReturn getHtml() {
		Ti_HtmlReturn ret;
		if ( terminator.isWeb() ) {	
			ret = new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						"<a href='"+Ti_TerminalLink.getXUnescapedXml(eE,rh)+"' "+
						Tc_Terminator.stil.getAnkerWebBLACK()+">"+Ti_TerminalLink.getXUnescapedXml(eE,rh)+"</a>",
						null);			
		} else {	
			ret = new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
						"<a "+ts.getHref(getRefCode())+
						Tc_Terminator.stil.getAnkerWebBLACK()+">"+Ti_TerminalLink.getXUnescapedXml(eE,rh)+"</a>",
						null);			
		}


		ret.setKeyLabelRight(eE,rh.getSitzung());		
		return ret;			
		
	}
	

	@Override
	public String getLogText() {
		// TODO Auto-generated method stub
		return Ti_TerminalLink.getXUnescaped(eE,rh);
	}

	@Override
	public void executeLink() {
		Element flex = new Element("config");
		
		Element ca = new Element("prop");
		ca.setAttribute("id", "with");
		ca.setText(Ti_TerminalLink.getXUnescapedXml(eE,rh));
		flex.addContent(ca);	

		try {
			AppSystemcall x = new AppSystemcall(
									Wellknown.open_web_with,null,null,
									flex,
									Interpreter.SILENCE_YES,
									terminator,0,null);
			x.run();
		} catch (Exception e) {
			(new LogConnector("TerminalLinkWeb")).warn("The systemcall 'open_web_with' could not be executed !");
		}

	}

	@Override
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}