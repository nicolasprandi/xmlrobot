/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;

import at.nicolas.x.int_db.DbJob;

public class Map_JobButton {

	public JButton buttonRun = null;
	public JButton buttonHelp = null;
	public DbJob	   job = null;
	
	public Map_JobButton ( JButton buttonRun, JButton buttonHelp, DbJob job ) {
		this.buttonRun = buttonRun;
		this.buttonHelp = buttonHelp;
		this.job = job;
	}
	
	public void setEnabledPerLevelAppjobs ( String current_level,
			boolean has_static_pro ) {

		boolean view = false;
		if ( has_static_pro )
			view = true;
		else if ( DbJob.getLevelHeight(job.getLevel()) >= DbJob.getLevelHeight(current_level))
			// wird genommen
			view = true;
		buttonRun.setEnabled(view);
	
		if ( buttonHelp != null )
			buttonHelp.setEnabled(true);
	}	
	public void setEnabledPerLevel ( String current_level) {

		boolean view = false;
		if ( DbJob.getLevelHeight(job.getLevel()) >= DbJob.getLevelHeight(current_level))
			// wird genommen
			view = true;
		buttonRun.setEnabled(view);
	
		if ( buttonHelp != null )
			buttonHelp.setEnabled(true);
	}	
	public static void setPiEnabledPerLevelAppjobs ( 
			String current_level, int piLevel, JButton button,
			boolean has_static_project) {

		boolean view = false;
		if ( has_static_project)
			view = true;
		else if ( (piLevel >= DbJob.getLevelHeight(current_level)))
			// wird genommen
			view = true;
		button.setEnabled(view);
	}	
	
	public static void setPiEnabledPerLevel ( 
			String current_level, int piLevel, JButton button) {

		boolean view = false;
		if ( (piLevel >= DbJob.getLevelHeight(current_level)))
			// wird genommen
			view = true;
		button.setEnabled(view);
	}		
	
	public static Map_JobButton searchButton(JButton search, String type, ArrayList<Map_JobButton> buttonList ) {
		Iterator<Map_JobButton> i = buttonList.iterator();
		Map_JobButton found = null;
		while ( i.hasNext()) {
			Map_JobButton iJobButton = (Map_JobButton)i.next();
			JButton iButton = null;
			if ( type.equals("run") )
				iButton = iJobButton.buttonRun;
			else if ( type.equals("help"))
				iButton = iJobButton.buttonHelp;			
			if ( search == iButton ) {
				found = iJobButton;
				break;
			}
		}
		return found;

	}	
}