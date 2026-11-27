/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_ButtonClearLog implements Tc_UiMap {
	
	JButton button = null;
	ButtonHandler buttonHandler = null;
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("map.ButtonClearLog");
			
	public Map_ButtonClearLog ( Font font, Color foregroundColor , Color backgroundColor ) {
		button = new JButton("Clear Log4j");
		button.setToolTipText("Clears the output on the log4j-logger");
		buttonHandler = new ButtonHandler();
		button.addActionListener(buttonHandler);
		
		if ( font != null )
			button.setFont(font);
		if ( backgroundColor != null )
			button.setBackground(backgroundColor);
		if ( foregroundColor != null )
			button.setForeground(foregroundColor);
		
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}

	public void bind() {

	}

	public JComponent getJComponent() {
		return ((JComponent)button);		
	}

	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			appConsole3.clearLogOnly();

		}
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}	
}