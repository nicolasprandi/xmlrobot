/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.PatternLayout;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_core.Tc_UiMap;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Asien implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;
	Tc_Terminator terminator = null;
	boolean bottom = true;
	private int db_width = Tc_AppConsole.guiSizer.getLeftPanelWidth();
	private int buttonCount = 4;
	private boolean withHelp = false;
	public Persp_Asien( boolean bottom, boolean withHelp ) {
		this.bottom = bottom;
		this.withHelp = withHelp;
		
	}
	public void setButtonCount( int buttonCount ) {
		this.buttonCount = buttonCount;
	}
	public Persp_Asien( boolean bottom, int _db_width ) {
		this.bottom = bottom;
		db_width = _db_width;
		
	}
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		int stil = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		if ( withHelp)
			stil = Map_JobButtonsApp.STYLE_RUN_HELP;
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										new Font("Arial",1 , 13),
										Color.WHITE,
										Color.BLACK,
										buttonCount,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										stil);
										// Map_JobButtonsApp.STYLE_RUN_HELP);


		Map_JobButtonsSc serviceButtons = 
				new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,withHelp);		
		

		Map_LogViewer logViewer = new Map_LogViewer(
									new PatternLayout( "%-5p %m%n" ),
									BorderLayout.NORTH,
									Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
		
		//
		//
		mapList.add(dbSelector);
		mapList.add(jobButtons);
		mapList.add(serviceButtons);
		mapList.add(logViewer);		
		mapList.add(terminator);	
		
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");
		jtp.add(logViewer.getJComponent(),"Runtime Logger");
		
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		
		JPanel docPanel = new JPanel(new BorderLayout());
		docPanel.add(controlPanel,BorderLayout.NORTH);
		docPanel.add(jtp,BorderLayout.CENTER);
				
		JPanel treePanel = new JPanel(new BorderLayout());
		if ( bottom)
			treePanel.add(serviceButtons.getJComponent(),BorderLayout.SOUTH);
		else
			treePanel.add(serviceButtons.getJComponent(),BorderLayout.NORTH);
		
		treePanel.add(dbSelector.getJComponent(),BorderLayout.CENTER);
		
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								treePanel,
								docPanel);
		panel.setDividerLocation(db_width);

	
	}

	public ArrayList<Tc_UiMap> getMaps() {
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