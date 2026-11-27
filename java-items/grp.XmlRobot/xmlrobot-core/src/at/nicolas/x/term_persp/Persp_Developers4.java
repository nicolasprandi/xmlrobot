/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.PatternLayout;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;


public class Persp_Developers4 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 1;
	Tc_Terminator terminator = null;
	
	boolean smallTime = false;
	public void setSmallTime() {
		smallTime = true;
	}
	
	public void build() {
		

		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		int helpCode;
		if (smallTime)
			helpCode = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		else
			helpCode = Map_JobButtonsApp.STYLE_RUN_HELP;
		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 12),Color.WHITE,Color.BLACK,2,Map_JobButtonsSc.TYPE_COLS,null,helpCode);
		//SystemOut systemOut = new SystemOut();
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(2,Map_JobButtonsSc.TYPE_COLS,null,!smallTime);
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

		
		infoPane = new JTabbedPane();

		infoPane.addTab("Browser",terminator.getJComponent());	
		infoPane.addTab("Runtime Logger",logViewer.getJComponent());		

		
		JPanel b = new JPanel ( new BorderLayout());
		b.add(infoPane, BorderLayout.CENTER);
	
		JPanel leftMainPanel = new JPanel(new GridLayout(2,1));
		leftMainPanel.add(allJobs.getJComponent());
		leftMainPanel.add(dbSelector.getJComponent());
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(leftMainPanel,BorderLayout.CENTER);
	
		leftPanel.add(systemcalls.getJComponent(),BorderLayout.SOUTH);

		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,b);
		//panel.setDividerLocation(Tc_AppConsole.guiSizer.value_400);
		panel.setDividerLocation(200);
		dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*5,Tc_AppConsole.guiSizer.value100_scaled));
		allJobs.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*5,Tc_AppConsole.guiSizer.value100_scaled));
		systemcalls.getJComponent().setPreferredSize( new Dimension(70,70));
		if ( smallTime )
		  leftPanel.setPreferredSize(new Dimension(100,100));

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