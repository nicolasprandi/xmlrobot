/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.PatternLayout;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Noname5 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;
	Tc_Terminator terminator = null;
	
	boolean right = true;
	public Persp_Noname5( boolean right ) {
		this.right = right;
		
	}	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );

		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										new Font("Arial",1 , 13),
										Color.WHITE,
										Color.BLACK,
										3,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										Map_JobButtonsApp.STYLE_RUN_HELP);


		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,true);	
		Tc_AppConsole.guiSizer.setTabletServicesSize(scButtons);
		

		Map_LogViewer logViewer = new Map_LogViewer(
									new PatternLayout( "%-5p %m%n" ),
									BorderLayout.NORTH,
									Map_LogViewer.loglevelstyle_combo,true);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();

		
		//
		//
		try {
			mapList.add(dbSelector);
			mapList.add(jobButtons);
			mapList.add(scButtons);
			
			mapList.add(logViewer);		
			mapList.add(terminator);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}	
		
		
		//
		//
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");

		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		if ( right )
			controlPanel.add(scButtons.getJComponent(),BorderLayout.EAST);
		else
			controlPanel.add(scButtons.getJComponent(),BorderLayout.WEST);
		
		JPanel docPanel = new JPanel(new BorderLayout());
		docPanel.add(controlPanel,BorderLayout.NORTH);
		docPanel.add(jtp,BorderLayout.CENTER);
		
		
		JSplitPane treePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				dbSelector.getJComponent(),
				logViewer.getJComponent());
		treePanel.setDividerLocation(0);
		
		
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePanel,docPanel);
		panel.setDividerLocation(Tc_AppConsole.guiSizer.getLeftPanelWidth());
	
		treePanel.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*4);
		dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),Tc_AppConsole.guiSizer.value100_scaled*4));
		logViewer.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.value100_scaled*4));
	
	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		
		return this.getClass().getName(); 	
	}

	

	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}



}