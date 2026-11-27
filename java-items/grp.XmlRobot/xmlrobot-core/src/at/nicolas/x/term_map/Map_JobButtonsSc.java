/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import at.nicolas.x.int_const.Zet_Privileg;
import at.nicolas.x.int_db.DbJm;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;


public class Map_JobButtonsSc implements Tc_UiMap {

	JPanel panel = null;
	ArrayList<Map_JobButton> buttonList = null;
		
	ButtonHandler buttonHandler = null;
	Tc_AppConsole appConsole3 = null;

	Tc_UiContext myUiContext = new Tc_UiContext();
	
	public final static int TYPE_COLS = 0;
	public final static int TYPE_ROWS = 1;
	
	private LogConnector lc = new LogConnector ("map.Systemcalls");
	
	private Map_JobButtonsSc me = null;
				
	public Map_JobButtonsSc ( int size, int type, 
			ComponentOrientation componentOrientation, 
			boolean doShowHelp ) {
		 
		me = this;  
		ArrayList<DbJob> joblistA = CoreBuilder.db.getScJm().getJoblistTerminal();		
		ArrayList<DbJob> joblist = DbJm.arrangeJoblistGridLayoutGUADEOIDEZEIT(joblistA, 0, size);  
		int sizeup = (joblist.size()+size-1)/size;

		if (  type == TYPE_COLS )
			if (  sizeup == 1)
				panel = new JPanel(new GridLayout( sizeup,joblist.size() ));
			else
				panel = new JPanel(new GridLayout( sizeup,size ));
		else
			if (  sizeup == 1)
				panel = new JPanel(new GridLayout( joblist.size(),sizeup ));
			else
				panel = new JPanel(new GridLayout( size,sizeup ));
		  		
		  
		if ( componentOrientation != null )
		  panel.setComponentOrientation(componentOrientation);
		
		buttonList = new ArrayList<Map_JobButton>();
		buttonHandler = new ButtonHandler();
		
		Iterator<DbJob> jobIterator = joblist.iterator();
		while ( jobIterator.hasNext()) {
			DbJob curJob = (DbJob)jobIterator.next();
			if ( curJob != null ) {
				JButton runButton = new JButton("|  "+curJob.getJobid());
				/*
				if ( CoreBuilder.ws.isRoleAdminUp())
					runButton = new JButton("* "+curJob.getJobid());
				else
					runButton = new JButton(curJob.getJobid());
					*/
				String tt = curJob.getToolTip();
				if ( tt!=null)
					runButton.setToolTipText(tt);
				else
					runButton.setToolTipText("Runs job '"+curJob.getJobid()+"'");

				if ( curJob.getPri() < 10)
					runButton.setFont(Tc_AppConsole.guiSizer.serviceButtonFont);
				else
					runButton.setFont(Tc_AppConsole.guiSizer.serviceButtonFontSmall);
				runButton.addActionListener(buttonHandler);
				runButton.setBackground(Color.LIGHT_GRAY);
				runButton.setHorizontalAlignment(SwingConstants.LEFT);
				if ( CoreBuilder.ws.isTablet) {
					runButton.setPreferredSize(new Dimension(10,30));
				}
				
				Map_JobButton jobButton = null;
				
				if ( Zet_Privileg.showServiceHelp() && doShowHelp) { 

					JButton helpButton = new JButton(" ");
					helpButton.setBackground(Color.LIGHT_GRAY);
					String tth = curJob.getToolTipHelp();
					if ( tth!=null)
						helpButton.setToolTipText(tth);
					else
						helpButton.setToolTipText("Help '"+curJob.getJobid()+"'");
					if ( CoreBuilder.ws.getOs().startsWith("mac")) {
						helpButton.setPreferredSize ( new Dimension(72,10));						
					} else {
						helpButton.setPreferredSize(new Dimension(36,10));
						helpButton.setMargin( new Insets(2,2,2,2));	
					}
					if ( curJob.getToolTipHelp() != null )
						helpButton.setToolTipText(curJob.getToolTipHelp());
					else					
						helpButton.setToolTipText("Help '"+curJob.getJobid()+"'");

					
					helpButton.setFont(new Font("Arial",1 , 1));
					
					
					helpButton.addActionListener(buttonHandler);
					
					jobButton = new Map_JobButton(runButton, helpButton, curJob );					
					JPanel x = new JPanel(new BorderLayout());
					x.add(runButton, BorderLayout.CENTER);
					x.add(helpButton,BorderLayout.EAST);
					
					panel.add(x);
					
				} else {			
					jobButton = new Map_JobButton(runButton, null, curJob );				
					panel.add(runButton);						
				}
				
				jobButton.setEnabledPerLevel(DbJob.V_LEVEL_DB);
				buttonList.add(jobButton);						
			} else {
				panel.add(new JPanel());
			}

	
			
		}	
		
				
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
	}

	public JComponent getJComponent() {
		return ((JComponent)panel);		
	}
	
	public void bind() {
		Tc_UiContext currentContext = appConsole3.getCurrentContext();
		if ( Tc_UiContext.changedInst(myUiContext,currentContext)) {
			Iterator<Map_JobButton> iterator = buttonList.iterator();
			String currentLevel = currentContext.getLevelOk();
			
			while ( iterator.hasNext()) {
				Map_JobButton jobentry = (Map_JobButton)iterator.next();
				jobentry.setEnabledPerLevel(currentLevel);
			}			
		}
		myUiContext = currentContext.cloneMe();
	}

	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			
			Map_JobButton found = null;
			found = Map_JobButton.searchButton((JButton)e.getSource(),"run",buttonList);
			if ( found != null ) {
				// run
				appConsole3.runScCurrDb(found.job.getJobid(),Interpreter.SILENCE_NO);
			} else {
				found = Map_JobButton.searchButton((JButton)e.getSource(),"help",buttonList);
				
				if ( found != null ) {
					appConsole3.runAboutJob(found.job,true);				
				} 				
			}
		}
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}	
}