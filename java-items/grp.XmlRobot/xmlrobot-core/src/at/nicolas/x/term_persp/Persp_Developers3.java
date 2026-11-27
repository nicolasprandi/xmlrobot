/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class Persp_Developers3 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 1;
	
	private boolean changedSomething = false;
	Tc_Terminator terminator = null;
	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);

		// Programm programm = new Programm();
		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(2,Map_JobButtonsSc.TYPE_COLS,null,true);		
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );
		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo,true);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		//terminator.setTpFontSize(14);
		terminator.setupDone();
		mapList.add(terminator);

		mapList.add(dbSelector);

		mapList.add(jobButtons);
		mapList.add(scButtons);
		mapList.add(logViewer);
		
		JSplitPane c = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				terminator.getJComponent(),
				logViewer.getJComponent());
		c.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*4);


		JSplitPane a = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,dbSelector.getJComponent(),jobButtons.getJComponent());
		a.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*2);
		b = new JPanel(new BorderLayout());
		b.add(a,BorderLayout.CENTER);
		b.add(scButtons.getJComponent(),BorderLayout.SOUTH);

		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				b,
				c);
		panel.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*5);

	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		return "DEVELOPER3";	
	}

	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}



}