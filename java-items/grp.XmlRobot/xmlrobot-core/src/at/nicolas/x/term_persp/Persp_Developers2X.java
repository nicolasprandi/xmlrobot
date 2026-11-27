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

public class Persp_Developers2X implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	JTabbedPane infoPane = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 1;
	private  int buttons_count = 7;
	
	private boolean changedSomething = false;
	Tc_Terminator terminator = null;
	boolean help = true;
	public void setButtonCount ( int buttons_count ) {
		this.buttons_count = buttons_count;
	}
	public void setHelp ( boolean help ) {
		this.help = help;
	}	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		int helpCode;
		if ( help )
			helpCode = Map_JobButtonsApp.STYLE_RUN_HELP;
		else
			helpCode = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,buttons_count,Map_JobButtonsSc.TYPE_COLS,null,helpCode);


		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(buttons_count*2,Map_JobButtonsSc.TYPE_COLS,null,help);		
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );
		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		mapList.add(terminator);

		mapList.add(dbSelector);

		mapList.add(jobButtons);
		mapList.add(scButtons);
		mapList.add(logViewer);
		
		panel = new JPanel();

		JSplitPane x = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,dbSelector.getJComponent(),terminator.getJComponent());
		x.setDividerLocation(Tc_AppConsole.guiSizer.getLeftPanelWidth());
		panel.setLayout(new BorderLayout());	
		panel.add(x,BorderLayout.CENTER);
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(scButtons.getJComponent(),BorderLayout.NORTH);
		topPanel.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		topPanel.add(logViewer.getJComponent(),BorderLayout.EAST);
		panel.add(topPanel,BorderLayout.NORTH);
			
		logViewer.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*4,Tc_AppConsole.guiSizer.value100_scaled));
		dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),Tc_AppConsole.guiSizer.value100_scaled*4));

	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "DEVELOPER2";	
	}



	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}



}