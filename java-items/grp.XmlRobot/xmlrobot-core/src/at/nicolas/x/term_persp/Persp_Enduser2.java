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
import at.nicolas.x.run.Terminal;
import at.nicolas.x.term_core.Tc_Terminator;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Enduser2 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	Tc_Terminator terminator = null;
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);

		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,true);

		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );		
		Map_LogViewer logViewer = new Map_LogViewer(
				new PatternLayout( "%-5p %m%n" ),
				BorderLayout.NORTH,
				Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);

		
		mapList.add(dbSelector);
		mapList.add(terminator);
		mapList.add(allJobs);
		mapList.add(systemcalls);

		JPanel boxButtons = new JPanel(new BorderLayout());
		boxButtons.add(allJobs.getJComponent(),BorderLayout.CENTER);	
		boxButtons.add(systemcalls.getJComponent(),BorderLayout.SOUTH);
		
		JPanel boxLeft = new JPanel(new BorderLayout());
		boxLeft.add(dbSelector.getJComponent(),BorderLayout.CENTER);
		boxLeft.add(boxButtons,BorderLayout.EAST);
		boxLeft.setPreferredSize(new Dimension(300,800));
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");

		jtp.add(logViewer.getJComponent(),"Runtime Logger");

		
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,boxLeft,jtp);
		panel.setDividerLocation(500);

	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "USER";	
	}



	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}

}