/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_ButtonDbManagement implements Tc_UiMap {
	
	JButton button = null;
	ButtonHandler buttonHandler = null;
	
	JButton button2 = null;
	ButtonHandler2 buttonHandler2 = null;
	
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("DbAdmin");
	JPanel jpanel = null;
			
	public Map_ButtonDbManagement (  ) {
		button = new JButton("Projects");
		buttonHandler = new ButtonHandler();
		button.addActionListener(buttonHandler);

		button2 = new JButton("Project Admin");
		buttonHandler2 = new ButtonHandler2();
		button2.addActionListener(buttonHandler2);
		
		jpanel = new JPanel(new GridLayout(1,1));
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

	private class ButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )   {
			appConsole3.runDbAdmin();
		}
	}
	
	private class ButtonHandler2 implements ActionListener {		
		public void actionPerformed ( ActionEvent e )   {
			appConsole3.runProject();
		}
	}	

	public void start() {
		// TODO Auto-generated method stub		
	}	
}