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
import at.nicolas.x.int_specs.OfflineSitzung;
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Noname7 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JPanel panel = null;
	JTabbedPane infoPane = null;
	Tc_Terminator terminator = null;
	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );

		Map_JobButtonsDb appJobs = new Map_JobButtonsDb(new Font("Arial",1 , 10),Color.WHITE,Color.BLACK,2,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);

		Map_JobButtonsSc sysJobs = new Map_JobButtonsSc(2,Map_JobButtonsSc.TYPE_COLS,null,true);
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );

		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		//terminator.setTpFontSize(14);
		terminator.setPrefDimTp(new Dimension(20,45));
		terminator.setupDone();
		mapList.add(terminator);
		mapList.add(dbSelector);
		mapList.add(appJobs);
		mapList.add(sysJobs);
		mapList.add(logViewer);

		
		panel = new JPanel(new GridLayout());
		
		panel.add(appJobs.getJComponent());	
		panel.add(sysJobs.getJComponent());
		
		JPanel teilen = new JPanel(new GridLayout(2,1));
		teilen.add(dbSelector.getJComponent());
		teilen.add(logViewer.getJComponent());
		panel.add(teilen);
		panel.add(terminator.getJComponent());		
		
	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "SEXY-7";	
	}



	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return (ITerminator)terminator;
	}

}