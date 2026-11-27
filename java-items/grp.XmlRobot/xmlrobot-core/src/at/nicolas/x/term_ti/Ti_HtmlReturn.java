/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_ti;

import java.util.ArrayList;

import org.jdom2.Element;

import at.nicolas.x.int_coop.InterLoopXml;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.term_core.Tc_Terminator;

public class Ti_HtmlReturn {
	
	public ArrayList<Ti_TerminalLink> menuList = new ArrayList<Ti_TerminalLink>();
	
	private String label = null;
	public String main = null;
	public String right = null;
	public boolean alignRight = false;
	// public String width=null;
	//public boolean is_big = false;
	private String key = null;
	
	public boolean lable_right = false;
	
	public boolean main_nowrap = false;
	
	public boolean is_no_rec_menu = true;
	public boolean rec_is_menu = false;
	public boolean flow = false;
	public void setFlow( boolean flow ) {
		this.flow = flow;
	}
	public boolean existsKey() {
		if ( key == null ) return false;
		else return true;
	}
	public String getKey() {
		return key;
	}
	
	public void setKeyLabelRight(Element e, ISitzung keySitzung) {
		if ( e.getAttribute("key")!=null && e.getAttributeValue("key").length()>0)
			key = Ti_TerminalLink.getHtmlByTextFromEscaped(e.getAttributeValue("key"));
		this.lable_right = Commons.hasFlag(e, "label_right");
	}
	public void setKey(Element e, ISitzung keySitzung) {
		if ( e.getAttribute("key")!=null && e.getAttributeValue("key").length()>0)
			key = Ti_TerminalLink.getHtmlByTextFromEscaped(e.getAttributeValue("key"));
	}	
	
	/*
	public void setKeyLabelRight(Element e) {
		if ( e.getAttribute("key")!=null && e.getAttributeValue("key").length()>0)
			key = Ti_TerminalLink.getAttUnescapedXml(e,("key"),null);
		this.lable_right = Commons.hasFlag(e, "label_right");
	}
	public void setKey(Element e) {
		if ( e.getAttribute("key")!=null && e.getAttributeValue("key").length()>0)
			key = Ti_TerminalLink.getAttUnescapedXml(e,("key"),null);
	}	
	*/
	public void setLabelStyle( Element e ) {
		this.lable_right = Commons.hasFlag(e, "label_right");
	}
	public void setMainNowrap( boolean main_nowrap ) {
		this.main_nowrap = main_nowrap;
	}


	Ti_HtmlReturn(String label, String main, String right ) {
		this.main = main;
		this.right = right;
		setLabel(label);		
			
	}	
	Ti_HtmlReturn() {
			
	}		
	Ti_HtmlReturn(String label ) {
		setLabel(label);				
	}		
	ArrayList<RefCode> refcodes = null;
	public void setRefcodes(ArrayList<RefCode> refcodes) {
		this.refcodes = refcodes;
	}
	public ArrayList<RefCode> getRefcodes() {
		return refcodes;
	}	
	public void setMain ( String main ) {
		this.main = main;
	}
	public void setLabel ( String label ) {
		if (label!=null && label.length()>0)
			this.label = label;
	}
	
	public String getLabel (  ) {
		return label;
	}
	
	Ti_HtmlReturn(String label, String main, String right, boolean is_number) {
		this.main = main;
		this.right = right;
		setLabel(label);
		this.alignRight = is_number;			
			
	}	
	boolean pushMain = false;
	Ti_HtmlReturn(String label, String main, String right, boolean is_number,   boolean label_right, boolean pushMain) {
		this.main = main;
		this.right = right;
		setLabel(label);
		this.alignRight = is_number;
		this.lable_right = label_right;	
		this.pushMain = pushMain;
			
	}	
	
	public boolean existsLabel() {
		return ( !(label==null));
	}
	public boolean existsMain() {
		return ( !(main==null));
	}
	public boolean existsRight() {
		return ( !(right==null));
	}
	public boolean getIsNoRecMenu() {
		return is_no_rec_menu;
	}
	public void setIsNoRecMenu(boolean is_no_rec_menu) {
		this.is_no_rec_menu = is_no_rec_menu;
		// System.out.println("-- is_no_rec_menu = "+is_no_rec_menu);
	}	
	public String getTdLabel () {
		if ( Tc_Terminator.stilIsWeb ) {
			if ( lable_right )
				return ("<td class='tiLabel tiLabelRight'><span style=\"white-space:nowrap\" \\>"
						+ label+"</span></td>");	
			else
				return ("<td class='tiLabel'><span style=\"white-space:nowrap\" \\>"
						+ label+"</span></td>");	
		} else {
			String doLableRight = "";
			if (lable_right ) {
				doLableRight=" text-align: right;";
			}	

			return ("<td valign='top' style='padding-left: 9px; padding-right: 8px;"+doLableRight+"' nowrap>"
					+ label+"</td>");			
			
		}

	}
	
	public String getTdKey () {

		
		if ( Tc_Terminator.stilIsWeb ) {
			return ("<td class='tiTdKey'>"
					+ key+"</td>");	
		} else {
			return ("<td valign='top' style='padding-left: 4px; text-align: right;' nowrap>"
					+ key+"</td>");		
			
		}		
	}
	
	
	
	public String getTdMain (int span, boolean temp_nowrap) {
		
		if ( Tc_Terminator.stilIsWeb ) {
			String spanner;
			if ( span == 0 || span==1)
				spanner="";
			else
				spanner=" colspan='"+Commons.int2String(span)+"' ";
			
			if ( pushMain )
				spanner = spanner + " width='100%' ";
			
			String aligner = " ";
			if ( alignRight)
				aligner = " align='right' ";
			
			String mainnowrap ="";
			//if ( main_nowrap )
			//	mainnowrap = " nowrap";
			
			
			if (label==null)
				return ("<td class='tiDataNotLabeled'"+spanner+aligner+mainnowrap+">" + main + "</td>");
			else
				return ("<td class='tiDataLabeled'"+spanner+aligner+mainnowrap+">" + main + "</td>");	
		} else {
			String spanner;
			if ( span == 0 || span==1)
				spanner="";
			else
				spanner=" colspan='"+Commons.int2String(span)+"' ";
			
			String aligner = " ";
			if ( alignRight)
				aligner = " align='right' ";
			
			String mainnowrap ="";
			if ( main_nowrap )
				mainnowrap = " nowrap";
			
			String styler = "";
			if (label==null)
				styler=" style='padding-left: 9px;'";

				return ("<td valign='top'"+spanner+aligner+styler+mainnowrap+">" + main + "</td>");	
			
		}
		
	
	}	
	
	public String getTdRight (int span, boolean span_html) {
		
		if ( Tc_Terminator.stilIsWeb ) {
			String spanner;
			if ( span == 0)
				spanner="";
			else
				spanner=" colspan='"+Commons.int2String(span)+"' ";
			
			if ( !span_html)
				return ("<td class='tiTdRightStd'"+spanner+" align='right'>" + right + "</td>");
			else
				return ("<td class='tiTdRightSph'"+spanner+" align='right'><span>" + right + "</span></td>");
		} else {
			String spanner;
			if ( span == 0)
				spanner="";
			else
				spanner=" colspan='"+Commons.int2String(span)+"' ";
			
			if ( !span_html)
				return ("<td valign='top'"+spanner+" align='right'>" + right + "</td>");
			else
				return ("<td valign='top'"+spanner+" align='right'><span>" + right + "</span></td>");	
			
		}			

	}
	public boolean isRec_is_menu() {
		return rec_is_menu;
	}
	public void setRec_is_menu(boolean rec_is_menu) {
		this.rec_is_menu = rec_is_menu;
		//System.out.println("-- rec_is_menu = "+rec_is_menu);
	}		

}