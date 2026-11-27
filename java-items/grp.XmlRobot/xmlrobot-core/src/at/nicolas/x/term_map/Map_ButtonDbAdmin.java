/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.int_const.Zet_Privileg;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_ButtonDbAdmin implements Tc_UiMap {
	
	JButton butPlus = null;
	JButton butMinus = null;
	JButton butRef = null;
	JButton butInfo = null;
	JButton butOpen = null;	
	ButtonHandler buttonHandler = null;
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("map.ButtonDbAdmin");
	JPanel a = null;
	
	boolean style_refresh = false;
	Font font;
	Color foregroundColor;
	Color backgroundColor;
	
	JPanel emptyPanel = new JPanel();
	Map_ButtonTiBack butBack;
	Map_ButtonTiFore butFore;
	Map_ButtonDbManagement butDb;
	
	public void setStyle( String name, String value )  {
		if ( name.equals("refresh")) {
			style_refresh = Commons.stringjava2bool(value, false);
		} else {
			lc.warn("style "+name+" ("+value+") is not known.");
		}
	}

	public Map_ButtonDbAdmin ( Font font, Color foregroundColor , Color backgroundColor ) {

		this.font = font;;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;		
		buttonHandler = new ButtonHandler();	
	}
	
	public void run (  ) {

		buttonHandler = new ButtonHandler();		
		butPlus = new JButton("Add");
		butPlus.addActionListener(buttonHandler);
		butMinus = new JButton("Del");
		butMinus.addActionListener(buttonHandler);	
		butRef = new JButton("Restart");
		butRef.addActionListener(buttonHandler);		
		butInfo = new JButton("Help");
		butInfo.addActionListener(buttonHandler);	
		butOpen = new JButton("Open");
		butOpen.addActionListener(buttonHandler);		
		
		butBack = new Map_ButtonTiBack(null,null,null);
		butFore = new Map_ButtonTiFore(null,null,null);
		butDb = new Map_ButtonDbManagement();
		
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
		
		/* JPanel trashPanel = new JPanel(new GridLayout(1,2));
		trashPanel.add(butBack.getJComponent());
		trashPanel.add(butFore.getJComponent()); */
		/*
		if ( style_refresh )
			a = new JPanel ( new GridLayout(1,4));
		else */
			a = new JPanel ( new GridLayout(1,4));
		
		// a.add(butInfo);		
		a.add(butOpen);
		a.add(butDb.getJComponent());

		a.add(butBack.getJComponent());
		a.add(butFore.getJComponent());

		a.setPreferredSize(new Dimension(50,40));

	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
		butBack.setMapSetMaster(_appConsole3);
		butFore.setMapSetMaster(_appConsole3);
		butDb.setMapSetMaster(_appConsole3);
	}

	public void bind() {
	}

	public JComponent getJComponent() {
		if ( Zet_Privileg.technicalInterface())
			return ((JComponent)a);		
		else
			return null;
	}

	private class ButtonHandler implements ActionListener {		
		public void actionPerformed ( ActionEvent e )    {
			JButton pressed = (JButton)e.getSource();
			if ( pressed == butRef ) {
				appConsole3.refresh();
			} else if ( pressed == butInfo ) {
				// appConsole3.printInfo();
			} else if ( pressed == butOpen ) {
				appConsole3.runScOpenFolder();
			}
		}
	}

	public void start() {
		// TODO Auto-generated method stub		
	}	
}