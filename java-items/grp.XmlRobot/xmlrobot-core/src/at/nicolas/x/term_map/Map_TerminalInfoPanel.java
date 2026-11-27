/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_TerminalInfoPanel implements Tc_UiMap {
	
	
	Tc_AppConsole appConsole3 = null;
	LogConnector lc = new LogConnector("map.TerminalInfoPanel");
	JComponent tpComponent;
	Map_ButtonTiBack buttonTiBack;
	Map_ButtonTiFore buttonTiFore;
	Map_ButtonClear buttonClear;
	Map_ButtonSaveBrowser buttonSave;
	
	JPanel mainPanel;
			
	public Map_TerminalInfoPanel ( JComponent tpComponent, JPanel stopPanel ) {
		this.tpComponent = tpComponent;
		
		buttonTiBack = new Map_ButtonTiBack(null,null,null);
		buttonTiFore = new Map_ButtonTiFore(null,null,null);
		buttonClear = new Map_ButtonClear(null,null,null);
		buttonSave = new Map_ButtonSaveBrowser(null,null,null);

		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		buttonPanel.add(buttonTiBack.getJComponent());
		buttonPanel.add(buttonTiFore.getJComponent());
		// buttonPanel.add(buttonClear.getJComponent());
		buttonPanel.add(buttonSave.getJComponent());
		
		mainPanel = new JPanel(new BorderLayout());
		try {
			mainPanel.add(buttonPanel,BorderLayout.WEST);
			mainPanel.add(tpComponent,BorderLayout.CENTER);
			
			mainPanel.add(stopPanel,BorderLayout.EAST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
	
		
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
		buttonTiBack.setMapSetMaster(appConsole3);
		buttonTiFore.setMapSetMaster(appConsole3);
		buttonClear.setMapSetMaster(appConsole3);
		buttonSave.setMapSetMaster(appConsole3);

	}

	public void bind() {

	}

	public JComponent getJComponent() {
		return ((JComponent)mainPanel);		
	}


	public void start() {
		// TODO Auto-generated method stub
		
	}	
}