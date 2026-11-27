/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_ButtonTerminal implements Tc_UiMap {
	
	JButton butPlus = null;
	JButton butMinus = null;
	JButton butRef = null;
	JButton butInfo = null;
	JButton butOpen = null;	
	ButtonHandler buttonHandler = null;
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("map.ButtonTerminal");
	JPanel a = null;
			
	public Map_ButtonTerminal ( Font font, Color foregroundColor , Color backgroundColor ) {

		buttonHandler = new ButtonHandler();
		
		butPlus = new JButton("+");
		butPlus.addActionListener(buttonHandler);
		butMinus = new JButton("x");
		butMinus.addActionListener(buttonHandler);	
		butRef = new JButton("r");
		butRef.addActionListener(buttonHandler);		
		butInfo = new JButton("i");
		butInfo.addActionListener(buttonHandler);	
		butOpen = new JButton("o");
		butOpen.addActionListener(buttonHandler);		
		if ( font != null ) {
			butPlus.setFont(font);
			butMinus.setFont(font);	
			butRef.setFont(font);	
			butInfo.setFont(font);		
			butOpen.setFont(font);				
		}
		if ( backgroundColor != null ) {
			butPlus.setBackground(backgroundColor);
			butMinus.setBackground(backgroundColor);
			butRef.setBackground(backgroundColor);		
			butInfo.setBackground(backgroundColor);		
			butOpen.setBackground(backgroundColor);				
		}
		if ( foregroundColor != null ) {
			butPlus.setForeground(foregroundColor);
			butMinus.setForeground(foregroundColor);	
			butRef.setForeground(foregroundColor);	
			butInfo.setForeground(foregroundColor);	
			butOpen.setForeground(foregroundColor);			
		}
		a = new JPanel ( new GridLayout(1,2));
		a.add(butPlus);
		a.add(butMinus);
		a.add(butRef);		
		a.add(butInfo);		
		a.add(butOpen);			
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}

	public void bind() {

	}

	public JComponent getJComponent() {
		return ((JComponent)a);		
	}

	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			JButton pressed = (JButton)e.getSource();
			if ( pressed == butRef ) {
				appConsole3.refresh();
			} else if ( pressed == butInfo ) {
				appConsole3.runScCurrDb("info", Interpreter.SILENCE_VERY);
			} else if ( pressed == butOpen ) {
				appConsole3.runScCurrDb("openfolder", Interpreter.SILENCE_VERY);
			}

		}
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}	
}