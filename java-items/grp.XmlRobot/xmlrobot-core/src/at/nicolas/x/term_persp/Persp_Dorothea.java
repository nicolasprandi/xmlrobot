/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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

public class Persp_Dorothea implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	Tc_Terminator terminator = null;
	boolean top = false;
	boolean logger = true;
	public Persp_Dorothea( boolean top ) {
		this.top = top;
		
	}	
	private int logsizer = Tc_AppConsole.guiSizer.value100_scaled*4;
	public Persp_Dorothea( boolean top, boolean logger ) {
		this.top = top;
		this.logger = logger;
		
	}	
	public void setLogsizer( int logsizer ) {
		this.logsizer = logsizer;
	}
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,true );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,false);
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );

		Map_LogViewer logViewer = null;
		if ( logger ) {
			logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
			CoreBuilder.rslogger.addAppender(logViewer);
		}
		
		mapList.add(dbSelector);
		mapList.add(allJobs);
		mapList.add(systemcalls);
		mapList.add(terminator);
		if ( logger )
			mapList.add(logViewer);
		

		JPanel boxButtons = new JPanel(new BorderLayout());
		boxButtons.add(allJobs.getJComponent(),BorderLayout.CENTER);	
		if ( top )
			boxButtons.add(systemcalls.getJComponent(),BorderLayout.NORTH);
		else
			boxButtons.add(systemcalls.getJComponent(),BorderLayout.SOUTH);


		
		JPanel centralPanel = new JPanel( new BorderLayout());
		centralPanel.add(boxButtons,BorderLayout.WEST);
		
		if ( logger ) {
			JSplitPane boxReader = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					terminator.getJComponent(),
					logViewer.getJComponent());
					boxReader.setDividerLocation(logsizer);		
			centralPanel.add(boxReader,BorderLayout.CENTER);			
		} else {
			centralPanel.add(terminator.getJComponent(),BorderLayout.CENTER);
		}


		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,dbSelector.getJComponent(),centralPanel);
		panel.setDividerLocation(Tc_AppConsole.guiSizer.getLeftPanelWidth());
		
		dbSelector.getJComponent().setPreferredSize( new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),600));
		boxButtons.setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*3,Tc_AppConsole.guiSizer.value100_scaled*6));


	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "DOROTHEA";	
	}

	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}

}