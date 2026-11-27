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

public class Persp_WebWalk1 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	Tc_Terminator terminator = null;
	boolean right = true;
	boolean help = true;
	int columns = 3;
	//int some_divider = Tc_AppConsole.guiSizer.value100_scaled*2;
	//int another_divider = Tc_AppConsole.guiSizer.getLeftPanelWidth();
	public Persp_WebWalk1( boolean right, boolean help ) {
		this.right = right;
		this.help = help;
		
	}
	public void setColumns ( int columns) {
		this.columns = columns;
	}
	public void setSpace( int columns, int some_divider, int some_other_divider) {
		this.columns = columns;
		//this.some_divider = some_divider;
		//this.another_divider = some_other_divider;
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

		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
		mapList.add(dbSelector);
		mapList.add(jobButtons);
		mapList.add(scButtons);	
		mapList.add(terminator);	
		
			
		JPanel allJobs = new JPanel(new BorderLayout());
		allJobs.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		if ( right )
			allJobs.add(scButtons.getJComponent(),BorderLayout.EAST);
		else
			allJobs.add(scButtons.getJComponent(),BorderLayout.WEST);
		
		JPanel upperArea = new JPanel(new BorderLayout());
		upperArea.add(dbSelector.getJComponent(),BorderLayout.WEST);
		upperArea.add(allJobs,BorderLayout.CENTER);
		upperArea.setPreferredSize(new Dimension(50,150));
		
		 panel = new JPanel(new BorderLayout());
		 panel.add(upperArea,BorderLayout.NORTH);
		 panel.add(terminator.getJComponent(),BorderLayout.CENTER);
		
					
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