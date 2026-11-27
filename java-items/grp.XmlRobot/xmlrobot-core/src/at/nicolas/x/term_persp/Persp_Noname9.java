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


public class Persp_Noname9 implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;
	Tc_Terminator terminator = null;

	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );

		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										new Font("Arial",1 , 13),
										Color.WHITE,
										Color.BLACK,
										3,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										Map_JobButtonsApp.STYLE_RUN_HELP);


		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,true);		
		

		Map_LogViewer logViewer = new Map_LogViewer(
									new PatternLayout( "%-5p %m%n" ),
									BorderLayout.NORTH,
									Map_LogViewer.loglevelstyle_combo,true);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();		
		
		mapList.add(dbSelector);
		mapList.add(jobButtons);
		mapList.add(scButtons);
		mapList.add(logViewer);		
		mapList.add(terminator);	
		
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		


		
		JPanel upper = new JPanel(new BorderLayout());
		upper.add(dbSelector.getJComponent(),BorderLayout.WEST);
		upper.add(controlPanel,BorderLayout.CENTER);

		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");

		/*
		 * 		JPanel westernPanel = new JPanel(new BorderLayout());
		westernPanel.add(scButtons.getJComponent(),BorderLayout.NORTH);
		westernPanel.add(,BorderLayout.CENTER);
		 */
		JPanel allesGrau = new JPanel(new BorderLayout());
		allesGrau.add(logViewer.getJComponent(),BorderLayout.CENTER);
		allesGrau.add(scButtons.getJComponent(),BorderLayout.SOUTH);
		JSplitPane lower = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,allesGrau,jtp);
		lower.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*3);
	
		dbSelector.getJComponent().setPreferredSize(new Dimension(Tc_AppConsole.guiSizer.getLeftPanelWidth(),230));
		//scButtons.getJComponent().setPreferredSize(Tc_AppConsole.guiSizer.dim_260_230);

		panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,upper,lower);
		panel.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*2);	
	
	}

	public ArrayList getMaps() {
		return mapList;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);
	}

	public String getName() {
		
		return "NONAME-9";	
	}

	
	@Override
	public ITerminator getTerminator() {
		// TODO Auto-generated method stub
		return terminator;
	}


}