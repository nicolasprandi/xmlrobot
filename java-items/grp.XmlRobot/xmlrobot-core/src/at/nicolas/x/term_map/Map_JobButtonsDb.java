/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.nicolas.x.int_const.Info;
import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_JobButtonsDb implements Tc_UiMap {

	JPanel jobsPanel = null;  
	ArrayList<Map_JobButtonsApp> jobButtonList = new ArrayList<Map_JobButtonsApp>();
	Tc_AppConsole appConsole3 = null;
	Tc_UiContext lastUiContext = null;
	private final String emptyname="EMPTYPANELNAME";
	JPanel emptypanel = null;
	int anzahl = 0;
	private LogConnector lc = new LogConnector("map.JobsButtonsCollection");
	
	public Map_JobButtonsDb(Font font, Color foregroundColor , Color backgroundColor, int size, int type, ComponentOrientation componentOrientation, int style ) {
		lc.debug(".init");

		lastUiContext = new Tc_UiContext();
		// jobs
		jobsPanel = new JPanel( new CardLayout() );

		anzahl++;
		emptypanel = new JPanel();
		
	    JLabel jlabel = new JLabel("Welcome to "
	    					+ /*[echnaton-productname3]*/ "XmlRobot"
	    					);
	    jlabel.setFont(new Font("Verdana",1,20));
	    emptypanel.add(jlabel);
	    
		jobsPanel.add(emptypanel,emptyname);
		
		ArrayList<DbApp> appList = CoreBuilder.db.getAppList();
		Iterator<DbApp> appIter = appList.iterator();
		while (  appIter.hasNext()) {
			DbApp curAppElement = (DbApp)appIter.next();
			Map_JobButtonsApp jobs = new Map_JobButtonsApp(curAppElement, font, foregroundColor , backgroundColor,size, type, componentOrientation,style);
			jobButtonList.add(jobs);
			jobsPanel.add(jobs.getJComponent(),curAppElement.getId());
			
			anzahl++;

		}
		
		CardLayout cl = (CardLayout)(jobsPanel.getLayout());
				
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;
		Iterator<Map_JobButtonsApp> i = jobButtonList.iterator();
		while ( i.hasNext()) {
			Map_JobButtonsApp iJobs = (Map_JobButtonsApp)i.next();
			appConsole3.appendUiMapStd(iJobs);
		}

	}

	public void bind() {

		Tc_UiContext uiContext = appConsole3.getCurrentContext();
		CardLayout cl = (CardLayout)(jobsPanel.getLayout());
		if ( Tc_UiContext.changedApp(lastUiContext,uiContext)) {

			if ( uiContext.getApp()==null ) {
				cl.show ( (Container)jobsPanel, emptyname );	//nic123

			} else {
				cl.show ( (Container)jobsPanel, uiContext.getApp() );				
			}
		}
		lastUiContext = uiContext.cloneMe();

	}

	public JComponent getJComponent() {
		// TODO Auto-generated method stub
		return ((JComponent)jobsPanel);
	}

	public void start() {
		CardLayout cl = (CardLayout)(jobsPanel.getLayout());
		cl.last((Container)jobsPanel);
		for ( int i = 0; i <= anzahl; i++)
			cl.previous((Container)jobsPanel);
		cl.show ( (Container)jobsPanel, emptyname );	
		
	}

}