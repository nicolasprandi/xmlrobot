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
import at.nicolas.x.term_core.Tc_UiMap;
import at.nicolas.x.term_map.Map_DbSelector;
import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;
import at.nicolas.x.term_map.Map_LogViewer;

public class Persp_Afrika implements Persp_IZP {

	ArrayList mapList = new ArrayList();
	JSplitPane panel = null;
	JTabbedPane infoPane = null;
	JPanel b = null;
	Tc_Terminator terminator = null;
	boolean bottom = true;
	private int db_width = Tc_AppConsole.guiSizer.getLeftPanelWidth();
	private int buttonCount = 4;
	boolean swap = false;
	private boolean withHelp = false;
	boolean leftSmall = false;
	int buttonHoehe = -1;
	public Persp_Afrika( boolean bottom, boolean swap, boolean withHelp  ) {
		this.bottom = bottom;
		this.swap=swap;
		this.withHelp = withHelp;
	}
	public void setButtonHoehe ( int buttonHoehex ) {
		this.buttonHoehe = buttonHoehex;
	}
	public void setLeftSmall() {
		leftSmall = true;
	}
	public void setButtonCount( int buttonCount ) {
		this.buttonCount = buttonCount;
	}
	public Persp_Afrika( boolean bottom, int _db_width ) {
		this.bottom = bottom;
		db_width = Tc_AppConsole.guiSizer.getLeftPanelWidth();//_db_width;
		
	}
	public void build() {
		
		Map_DbSelector dbSelector = new Map_DbSelector (new Font("Arial",1 , 13),Color.BLACK,Color.WHITE,false );
		int stil = Map_JobButtonsApp.STYLE_DONT_RUN_HELP;
		if ( withHelp)
			stil = Map_JobButtonsApp.STYLE_RUN_HELP;
		Map_JobButtonsDb jobButtons = new Map_JobButtonsDb(
										new Font("Arial",1 , 13),
										Color.WHITE,
										Color.BLACK,
										buttonCount,
										Map_JobButtonsSc.TYPE_COLS,
										null,
										stil);
										// Map_JobButtonsApp.STYLE_RUN_HELP);


		Map_JobButtonsSc serviceButtons = 
				new Map_JobButtonsSc(1,Map_JobButtonsSc.TYPE_COLS,null,withHelp);		
		

		Map_LogViewer logViewer = new Map_LogViewer(
									new PatternLayout( "%-5p %m%n" ),
									BorderLayout.NORTH,
									Map_LogViewer.loglevelstyle_combo);
		CoreBuilder.rslogger.addAppender(logViewer);
		
		terminator = new Tc_Terminator(true, Terminal.offlineSitzung);
		terminator.setupDone();
		
	
		//
		//
		mapList.add(dbSelector);
		mapList.add(jobButtons);
		mapList.add(serviceButtons);
		mapList.add(logViewer);		
		mapList.add(terminator);	
		
		/*
		JTabbedPane jtp = new JTabbedPane();
		jtp.add(terminator.getJComponent(),"Browser");
		jtp.add(logViewer.getJComponent(),"Logger");
		*/
		//JPanel controlPanel = new JPanel(new BorderLayout());
		//controlPanel.add(,BorderLayout.CENTER);
		
		JPanel docPanel = new JPanel(new BorderLayout());
		docPanel.add(jobButtons.getJComponent(),BorderLayout.NORTH);
		docPanel.add(terminator.getJComponent(),BorderLayout.CENTER);
		if ( this.buttonHoehe != -1 )
			jobButtons.getJComponent().setPreferredSize(new Dimension(100,buttonHoehe));
		
		jobButtons.getJComponent().setPreferredSize(new Dimension(180,180));
		JPanel southPanel =null;
		if ( swap ) {
			
			southPanel = new JPanel(new GridLayout(2,1));
			southPanel.add(serviceButtons.getJComponent());
			southPanel.add(logViewer.getJComponent());	
			southPanel.setPreferredSize(new Dimension(150,250));
			/*
			southPanel = new JPanel(new BorderLayout());
			southPanel.add(serviceButtons.getJComponent(),BorderLayout.NORTH);
			
			southPanel.add(logViewer.getJComponent(), BorderLayout.CENTER);	*/
			
		} else { 
			logViewer.getJComponent().setPreferredSize(new Dimension(150,150));
			southPanel = new JPanel(new GridLayout(1,2));
			southPanel.add(serviceButtons.getJComponent());
			southPanel.add(logViewer.getJComponent());			
		}

		JPanel treePanel = new JPanel(new BorderLayout());
		if ( bottom)
			treePanel.add(southPanel,BorderLayout.SOUTH);
		else
			treePanel.add(southPanel,BorderLayout.NORTH);
		
		treePanel.add(dbSelector.getJComponent(),BorderLayout.CENTER);
	
		
		panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								treePanel,
								docPanel);
		if ( leftSmall ){
			int x = (db_width/3)*2;
			panel.setDividerLocation(x);
		}else 
			panel.setDividerLocation(db_width);
		// panel.setPreferredSize(50,50)
		//treePanel.setPreferredSize(new Dimension(50,200));
	
	}

	public ArrayList<Tc_UiMap> getMaps() {
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