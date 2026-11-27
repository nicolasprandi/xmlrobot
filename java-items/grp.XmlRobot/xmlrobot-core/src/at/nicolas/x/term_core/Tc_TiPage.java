/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_core;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.LogConnector;

public class Tc_TiPage {
	
	String content;
	Point viewPoint;
	Point viewPoint2;
	int caret_pos = 0;
	public void addContent( String _content ) {
		content=content+_content;
	}
	Tc_TiPage() {
		content = "";
		viewPoint = new Point(0,0);		
		caret_pos = 0;
	}

	Tc_TiPage ( String content) {
		this.content = content;

	}
	
	Tc_TiPage ( String content, JEditorPane jep,  JScrollPane scroll, LogConnector lc) {
		this.content = content;
		storeCaretPoint(jep,scroll,lc);
		caret_pos = jep.getCaretPosition();
	}
	
	Tc_TiPage ( String content, Point viewPoint, int caret_pos ) {
		this.content = content;
		this.viewPoint = viewPoint;
		this.caret_pos = caret_pos;
	}	
	
	 void storeCaretPoint( JEditorPane jep, JScrollPane scroll, LogConnector lc ) {
		 viewPoint2 = scroll.getViewport().getLocation();
		 //lc.warn(" Stored caret : "+viewPoint2.y);
		Container container = SwingUtilities.getAncestorOfClass(JViewport.class, jep);
		if ( container == null ) {
			lc.warn(" Container is null");
			return;
		}
		try {
			Thread.sleep(10);
			JViewport viewport = (JViewport)container;
			viewPoint = viewport.getViewPosition();
			// lc.warn(" Stored Point : "+viewPoint.x+"/"+viewPoint.y);
			
		} catch (Exception e) {
			lc.warn(" Exception is null");
			Sp.pe(e);
		}
	}
	 
	 void applyCaretPointxxx( JEditorPane jep,  JScrollPane scroll, LogConnector lc ) {
		 scroll.getViewport().setLocation(viewPoint2);
		 jep.setCaretPosition(caret_pos);
		 lc.warn(" applied caret : "+viewPoint2.y);
		 try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}		 

	}	
	 
	 void applyCaretPoint77( JEditorPane jep,  JScrollPane scroll, LogConnector lc ) {
		 jep.setCaretPosition(caret_pos);
		 lc.warn(" applied caret : "+caret_pos);
		 try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}		 

	}		 
	 JViewport viewport;
	 void applyCaretPoint( JEditorPane jep,  JScrollPane scroll, LogConnector lc) {

		 
		 Container container = SwingUtilities.getAncestorOfClass(JViewport.class, jep);
		if ( container == null ) {
			lc.warn(" Container is null");
			return;
		}
		try {
			viewport = (JViewport)container;

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						viewport.setViewPosition(viewPoint);
					}				
				}); 
														
		} catch (Exception e) {
			lc.warn(" Exception is null");
			Sp.pe(e);
		}
	}	
	 
	 private void invokePoint ( ) {
		 
	 }
	 
		/*
*/
}