/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.PatternLayout;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.ITerminator;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_Names;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;
import at.nicolas.x.term_map.Map_Programm;

public class Persp_Full implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	JTabbedPane infoPane = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 0;
	Tc_Terminator terminator = null;
	
	public void build() {

		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,true );
		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);
		Map_Programm programm = new Map_Programm();
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(2,Map_JobButtonsSc.TYPE_COLS,null,true);
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );

		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(false, Terminal.offlineSitzung);
		terminator.setProgressLength(50);
		mapList.add(terminator);

		mapList.add(dbSelector);
		mapList.add(allJobs);
		mapList.add(systemcalls);
		mapList.add(programm);
		mapList.add(logViewer);
		
		panel = new JPanel();
		
		infoPane = new JTabbedPane();
		infoPane.addTab(Tc_Names.tab_ti,terminator.getJComponent());			
		infoPane.addTab(Tc_Names.tab_log4j,logViewer.getJComponent());
		
		JPanel upper = new JPanel(new GridLayout(1,2));
		upper.add(dbSelector.getJComponent());
		upper.add(allJobs.getJComponent());	

		terminator.getProgressComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled,Tc_AppConsole.guiSizer.value100_scaled));
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(upper, BorderLayout.CENTER);
		leftPanel.add(systemcalls.getJComponent(), BorderLayout.SOUTH);
		leftPanel.add(terminator.getProgressComponent(), BorderLayout.NORTH);
		
		panel.setLayout(new BorderLayout());
		panel.add(infoPane,BorderLayout.CENTER);
		panel.add(leftPanel,BorderLayout.WEST);
		
		infoPane.setSelectedIndex(index_tl);
	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "XXXX";	
	}



	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}

}