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

public class Persp_WebWalk2 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	Tc_Terminator terminator = null;
	//boolean right = true;
	boolean help = true;
	int columns = 3;
	//int some_divider = Tc_AppConsole.guiSizer.value100_scaled*2;
	//int another_divider = Tc_AppConsole.guiSizer.getLeftPanelWidth();
	public Persp_WebWalk2( int columns, boolean help ) {
		this.columns = columns;
		this.help = help;
		
	}


	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (Tc_AppConsole.guiSizer.dbFont,Color.BLACK,Color.WHITE,false );
		dbSelector.getJPanel().setPreferredSize(new Dimension(200,100));
		
		int helper_style = 0;
		if ( this.help )
			helper_style = Map_JobButtonsApp.STYLE_RUN_HELP;
		else
			helper_style = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										Tc_AppConsole.guiSizer.jobButtonFont,
										Color.WHITE,
										Color.BLACK,
										columns,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										helper_style);


		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,this.help);		
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
		mapList.add(terminator);	
		
			
		//JPanel allJobs = new JPanel(new BorderLayout());
		//allJobs.add(jobButtons.getJComponent(),BorderLayout.CENTER);

			

		
		//JPanel upperArea = new JPanel(new BorderLayout());
		//
		//upperArea.add(allJobs,BorderLayout.CENTER);
		//upperArea.setPreferredSize(new Dimension(50,150));

		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");
		jtp.add(logViewer.getJComponent(),"Runtime Logger");
		jtp.add(dbSelector.getJComponent(),"Projects and Apps");
		jtp.add(scButtons.getJComponent(),"Services");
		 panel = new JPanel(new BorderLayout());
		 panel.add(jobButtons.getJComponent(),BorderLayout.NORTH);
		 panel.add(jtp,BorderLayout.CENTER);
		
					
		//dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),500));
		//logViewer.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.value100_scaled*3,Tc_AppConsole.guiSizer.value100_scaled*2));

	
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