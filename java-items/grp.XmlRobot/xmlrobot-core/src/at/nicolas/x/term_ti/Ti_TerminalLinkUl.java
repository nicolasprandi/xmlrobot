/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;



public class Ti_TerminalLinkUl extends Ti_TerminalLink {

	Element eE;
	public Ti_TerminalLinkUl(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir, inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
	}

	@Override
	public Ti_HtmlReturn getHtml() {
		String inner = "<table cellpadding='0'><tr><td valign='top'>&bull;&nbsp;</td><td valign='top'>"+Ti_TerminalLink.getHtmlByXFromEscaped(eE)+"</td></tr></table>";

		Ti_HtmlReturn ret = new Ti_HtmlReturn(
				Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null),
					inner,
					null);

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
		// TODO Auto-generated method stub

	}

	@Override
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus, IGuiController guiController, ISitzung isitzung) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}