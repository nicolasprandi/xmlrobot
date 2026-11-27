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


public class Persp_Cromwell2 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	JTabbedPane infoPane = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 1;
	Tc_Terminator terminator = null;
	
	boolean smallOn = false;
	public void setSmall() {
		smallOn = true;
	}
	
	public void build() {

		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_DONT_RUN_HELP);
		//SystemOut systemOut = new SystemOut();
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(8,Map_JobButtonsSc.TYPE_COLS,null,false);
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );
		
		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		mapList.add(terminator);

		mapList.add(dbSelector);
		mapList.add(allJobs);
		mapList.add(systemcalls);
		mapList.add(logViewer);
		
		panel = new JPanel();
		
		infoPane = new JTabbedPane();

		infoPane.addTab("Browser",terminator.getJComponent());	
		infoPane.addTab("Runtime Logger",logViewer.getJComponent());
		
		JPanel b = new JPanel ( new BorderLayout());
		b.add(infoPane, BorderLayout.CENTER);
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(dbSelector.getJComponent(),BorderLayout.NORTH);
		leftPanel.add(allJobs.getJComponent(),BorderLayout.CENTER);
		
		JSplitPane x = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,b);
		int lp = Tc_AppConsole.guiSizer.getLeftPanelWidth();
		if ( smallOn )
			lp = (lp*2)/3;
		x.setDividerLocation(lp);
		
		panel.setLayout(new BorderLayout());
		panel.add(x);
		panel.add(systemcalls.getJComponent(),BorderLayout.NORTH);
		//Tc_AppConsole.guiSizer.getLeftPanelWidth()/2

		//leftPanel.setPreferredSize( new Dimension(50,150));
		dbSelector.getJComponent().setPreferredSize( new Dimension(lp,Tc_AppConsole.guiSizer.value100_scaled*2));

	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "DEVELOPERS";	
	}


	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}

}