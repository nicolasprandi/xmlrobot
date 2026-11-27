/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_ButtonProjectManager implements Tc_UiMap {
	
	
	JButton button2 = null;
	ButtonHandler2 buttonHandler2 = null;
	
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("ButtonProjectManager");
	JPanel jpanel = null;
			
	public Map_ButtonProjectManager (  ) {


		button2 = new JButton("+");
		button2.setToolTipText("Adds a new application or project to your database");
		buttonHandler2 = new ButtonHandler2();
		button2.addActionListener(buttonHandler2);
		
		jpanel = new JPanel(new BorderLayout());
		jpanel.add(button2);
		
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
	}

	public void bind() {
	}

	public JComponent getJComponent() {
		return ((JComponent)jpanel);		
	}

	
	private class ButtonHandler2 implements ActionListener {		
		public void actionPerformed ( ActionEvent e )   {
			appConsole3.runProjectManager();
		}
	}	

	public void start() {
		// TODO Auto-generated method stub		
	}	
}