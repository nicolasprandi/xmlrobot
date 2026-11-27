/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Noname62 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;
	Tc_Terminator terminator = null;
	boolean right = true;
	int columns = 3;
	int some_divider = Tc_AppConsole.guiSizer.value100_scaled*2;
	int another_divider = Tc_AppConsole.guiSizer.getLeftPanelWidth();
	public Persp_Noname62( boolean right ) {
		this.right = right;
		
	}
	public void setColumns ( int columns) {
		this.columns = columns;
	}
	public void setSpace( int columns, int some_divider, int some_other_divider) {
		this.columns = columns;
		this.some_divider = some_divider;
		this.another_divider = some_other_divider;
	}
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (Tc_AppConsole.guiSizer.dbFont,Color.BLACK,Color.WHITE,false );
		
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										Tc_AppConsole.guiSizer.jobButtonFont,
										Color.WHITE,
										Color.BLACK,
										columns,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										Map_JobButtonsApp.STYLE_RUN_HELP);


		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,true);		
		Tc_AppConsole.guiSizer.setTabletServicesSize(scButtons);

		Map_LogViewer logViewer = new Map_LogViewer(
									new PatternLayout( "%-5p %m%n" ),
									BorderLayout.NORTH,
									Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
		mapList.add(dbSelector);
		mapList.add(jobButtons);
		mapList.add(scButtons);
		mapList.add(logViewer);		
		mapList.add(terminator);	
		
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");
		jtp.add(logViewer.getJComponent(),"Runtime Logger");
		
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		if ( right )
			controlPanel.add(scButtons.getJComponent(),BorderLayout.EAST);
		else
			controlPanel.add(scButtons.getJComponent(),BorderLayout.WEST);
		
		JSplitPane docPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,controlPanel,jtp);

		docPanel.setDividerLocation(some_divider);

				
		JPanel treePanel = new JPanel(new BorderLayout());
		treePanel.add(dbSelector.getJComponent(),BorderLayout.CENTER);
		
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								treePanel,
								docPanel);

		panel.setDividerLocation(another_divider);
				
		dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),500));
		logViewer.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*3,Tc_AppConsole.guiSizer.value100_scaled*2));

	
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