/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_persp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import at.nicolas.x.term_map.Map_JobButtonsApp;
import at.nicolas.x.term_map.Map_JobButtonsDb;
import at.nicolas.x.term_map.Map_JobButtonsSc;


public class Persp_MaskControlCenter {
	
	JPanel give ;
	public Persp_MaskControlCenter(ArrayList mapList) {
		Map_JobButtonsSc systemcalls = new Map_JobButtonsSc(2,Map_JobButtonsSc.TYPE_COLS,null,false);
		Map_JobButtonsDb allJobs = new Map_JobButtonsDb(new Font("Arial",1 , 15),Color.WHITE,Color.BLACK,1,Map_JobButtonsSc.TYPE_COLS,null,Map_JobButtonsApp.STYLE_RUN_HELP);

		give = new JPanel ( new BorderLayout());
		give.add(allJobs.getJComponent(),BorderLayout.CENTER);
		give.add(systemcalls.getJComponent(),BorderLayout.EAST);
		mapList.add(allJobs);
		mapList.add(systemcalls);
	}
	
	public JComponent getJComponent() {
		// TODO Auto-generated method stub
		return ((JComponent)give);
	}

}