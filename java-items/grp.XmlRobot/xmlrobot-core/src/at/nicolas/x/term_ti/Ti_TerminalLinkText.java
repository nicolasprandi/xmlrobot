/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.ISitzung;



public class Ti_TerminalLinkText extends Ti_TerminalLink {

	Element eE;
	public Ti_TerminalLinkText(Element eE, ITerminator _terminator, String app, String appdir,
			String inst, String instdir, ITiHandler rh) {
		super(eE, _terminator, app, appdir, inst, instdir,rh);
		// TODO Auto-generated constructor stub
		this.eE = eE;
	}

	@Override
	public Ti_HtmlReturn getHtml() {


		boolean go_right = false;
		if ( eE.getAttribute("align")!=null && eE.getAttributeValue("align").equals("right"))
			go_right = true;
		Ti_HtmlReturn y;
		if ( Commons.hasFlag(eE, "html"))
			y= new Ti_HtmlReturn(
					Ti_TerminalLink.getHtmlByAttrFromEscaped(eE, "label", null), // label
						Ti_TerminalLink.getHtmlByXFromEscapedHtml(eE),				// kontent
						null,
						go_right,
						Commons.hasFlag(eE, "label_right"),
						true);
		else {
			
			// System.out.println("1 ***** ###### ****** "+Ti_TerminalLink.getXUnescapedLfXml(eE));
			
			// System.out.println("2 ***** ###### ****** "+Commons.text_to_html(Ti_TerminalLink.getXUnescapedLfXml(eE)));
			// System.out.println("3 ***** ###### ****** "+Ti_TerminalLink.getXUnescapedLfXml(eE));
			String inhalt = getHtmlByXFromEscaped(eE);
			if ( inhalt.trim().length() == 0 ) {
				inhalt = "&nbsp;";
				// System.out.println("da war was leer");
			}
			y= new Ti_HtmlReturn(
					getHtmlByTextFromEscaped(eE.getAttributeValue("label")), //  +" [L]",	// label
					inhalt, // getHtmlByXFromEscaped(eE), // +" [C]",
				// Ti_TerminalLink.getXUnescapedLfXml(eE),				// content
					null,
					go_right,
					Commons.hasFlag(eE, "label_right"),
					true);
		}
		y.setKey(eE,rh.getSitzung());				
		return y;
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
	public Interpreter createInterpreterToReact(String mark,boolean interWebModus,
			IGuiController guiController, ISitzung isitzung) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}