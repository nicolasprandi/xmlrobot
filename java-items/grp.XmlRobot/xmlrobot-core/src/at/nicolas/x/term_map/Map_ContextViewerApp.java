/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_ContextViewerApp implements Tc_UiMap {

	
	JTextArea   textarea = null;
	
	Tc_AppConsole appConsole3 = null;
	JFrame jFrame = null;
	public Map_ContextViewerApp ( Font font, Color foregroundColor, Color backgroundColor, Border border,
				JFrame jFrame) {
		this.jFrame = jFrame;
		textarea = new JTextArea();
		if ( font != null )
			textarea.setFont(font);
		if ( backgroundColor != null )
			textarea.setBackground(backgroundColor);
		if ( foregroundColor != null )
			textarea.setForeground(foregroundColor);
		if ( border != null )
			textarea.setBorder(border);
		try {
			
			textarea.setEditable(false);
			textarea.setText("");
		} catch (Exception e) {

		}
		
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}

	String last_posted_title = "";
	public void bind() {
		
		Tc_UiContext uiContext = appConsole3.getCurrentContext();
		textarea.setText(uiContext.getApp());
		
		String target_title = null;
		if ( uiContext.getInst() != null ) 
			target_title = uiContext.getApp().toUpperCase() + 
						   ": " + uiContext.getInst() +" ";
		else if ( uiContext.getApp() != null )
			target_title = uiContext.getApp().toUpperCase();
		else
			target_title = "Workspace: "+CoreBuilder.ws.getWsId();
		
		if ( !last_posted_title.equals(target_title)) {
			jFrame.setTitle(/*[echnaton-productname2]*/ "Echnaton" 
					+ " : " + target_title);
			last_posted_title = target_title;
		}
		
	
		/*
		 * 
		
		Sp.p("last_app="+last_app);
		Sp.p("this_app="+this_app);
		
		if ( this_app != null && (!last_app.equals(this_app))) {
			last_app = this_app;
			target_title = last_app.toUpperCase()+" ";			
		}
		
		String this_pro = uiContext.getInst();
		Sp.p("last_pro="+last_pro);
		Sp.p("this_pro="+this_pro);		
		if ( this_pro != null && (!last_pro.equals(this_pro))) {
			last_pro = this_pro;
			target_title = last_app.toUpperCase()+" [ "+last_pro+" ]";			
		}		
		
		if ( target_title != null ) {
			
		} else
			jFrame.setTitle("xxx XmlRobot.net v2: "+CoreBuilder.ws.getWsId());
		*/
	}
	

	public JComponent getJComponent() {
		return ((JComponent)textarea);		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

}