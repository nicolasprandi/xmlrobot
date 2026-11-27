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

public class Persp_Confuzius implements Persp_IZP {
	

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;

	private static int index_job = 0;	
	private static int index_logger = 2;
	private static int index_tl = 1;
	
	private boolean changedSomething = false;
	Tc_Terminator terminator = null;
	Dimension dbDimension = null;
	
	private int columns = 3;
	public void setDbDimension ( Dimension dbDimension ) {
		this.dbDimension = dbDimension;
	}
	
	public void setColumns ( int columns ) {
		this.columns = columns;
	}
	private boolean withHelp = true;	
	public void setHelp( boolean withHelp  ) {
		this.withHelp = withHelp;
	}
	
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		// ContextViewer contextViewer = new ContextViewer(null,null,null,null);
		int helpPara = Map_JobButtonsApp.STYLE_RUN_HELP;
		if ( withHelp )
			helpPara = Map_JobButtonsApp.STYLE_RUN_HELP;
		else
			helpPara = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,columns,Map_JobButtonsSc.TYPE_COLS,null,helpPara);

		Map_JobButtonsSc scButtons = new Map_JobButtonsSc(columns,Map_JobButtonsSc.TYPE_COLS,null,withHelp);		
		PatternLayout layout = new PatternLayout( "%-5p %m%n" );
		Map_LogViewer logViewer = new Map_LogViewer(layout,BorderLayout.NORTH,Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		//terminator.setPrefDimTp(new Dimension(5,5));
		//terminator.setTpFontSize(13);
		terminator.setupDone();
		mapList.add(terminator);

		mapList.add(dbSelector);
		// mapList.add(contextViewer);
		mapList.add(jobButtons);
		mapList.add(scButtons);
		mapList.add(logViewer);
		
		infoPane = new JTabbedPane();
		infoPane.addTab("Browser",terminator.getJComponent());
		
		infoPane.addTab("Runtime Logger",logViewer.getJComponent());	
		
		JPanel allButtons = new JPanel ( new BorderLayout());
		allButtons.add(jobButtons.getJComponent(),BorderLayout.CENTER);
		allButtons.add(scButtons.getJComponent(),BorderLayout.SOUTH);
		
		
		b = new JPanel(new BorderLayout());
		b.add(dbSelector.getJComponent(),BorderLayout.WEST);
		b.add(allButtons,BorderLayout.CENTER);

		panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,b,infoPane);
		// panel.setDividerLocation(220);
		panel.setDividerLocation(Tc_AppConsole.guiSizer.value100_scaled*2);
		
		//
		
		if ( this.dbDimension == null )
			dbSelector.getJComponent().setPreferredSize( new Dimension((Tc_AppConsole.guiSizer.getLeftPanelWidth()*2)/3,Tc_AppConsole.guiSizer.value100_scaled*6));
		else 
			dbSelector.getJComponent().setPreferredSize( this.dbDimension);

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