/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_ContextViewerJob implements Tc_UiMap {


	JTextArea   textarea = null;
	
	Tc_AppConsole appConsole3 = null;

	public Map_ContextViewerJob ( Font font, Color foregroundColor, Color backgroundColor, Border border ) {
		textarea = new JTextArea();
		if ( font != null )
			textarea.setFont(font);
		if ( backgroundColor != null )
			textarea.setBackground(backgroundColor);
		if ( foregroundColor != null )
			textarea.setForeground(foregroundColor);
		if ( border != null )
			textarea.setBorder(border);

		
		textarea.setEditable(false);
		textarea.setText("");
		
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}

	public void bind() {
		Tc_UiContext uiContext = appConsole3.getCurrentContext();
		textarea.setText(uiContext.getJob());
	}

	public JComponent getJComponent() {
		return ((JComponent)textarea);		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

}