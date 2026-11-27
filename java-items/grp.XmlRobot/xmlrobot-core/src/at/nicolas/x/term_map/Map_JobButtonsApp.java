/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import at.nicolas.x.int_db.DbApp;
import at.nicolas.x.int_db.DbJm;
import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_JobButtonsApp implements Tc_UiMap {
	
	Tc_AppConsole appConsole3 = null;
	boolean has_static_project = false;
	
	JPanel panel = null;
	JPanel a = null;
	ArrayList<Map_JobButton> buttonList = null;
	
	DbApp app = null;
	Tc_UiContext myUiContext = null;
	
	ButtonHandler buttonHandler = null;

	boolean runButtonsAreVisible = true;
	Map_JobButtonsApp me = null;
	public final static int TYPE_COLS = 0;
	public final static int TYPE_ROWS = 1;
	public final static int STYLE_RUN_HELP = 0;
	public final static int STYLE_DONT_RUN_HELP = 2;
	public final static int STYLE_SELECT = 1;
	int style = 0;
	private LogConnector lc = new LogConnector ("map.JobsButtonsApp");
	
	private JButton butProjectInfo;
	private JButton proButton;
	private int projectInfoLevel;
	private JButton butAppHelp;
	private JButton butProjectAdmin;
	private Font font;
	private Color foregroundColor;
	private Color backgroundColor;
	
	ImageIcon icon ;
	JLabel label1 ;
	//boolean has_anonym_projects = false;
				
	public Map_JobButtonsApp ( DbApp _app, Font _font, Color _foregroundColor , Color _backgroundColor, int size, int type, ComponentOrientation componentOrientation, int _style ) {
		app = _app;
		myUiContext = new Tc_UiContext();
		myUiContext.setApp(app.getId());
		me=this;
		style = _style;
		
		font = _font;
		foregroundColor = _foregroundColor;
		backgroundColor = _backgroundColor;
		
		
		ArrayList<DbJob> jobList = app.getJm().getJoblistTerminal();
		ArrayList<JComponent> compList = new ArrayList<JComponent>();
		
		//if ( app.getJm().getProjectInfoLevel() == DbJob.getLevelHeight ( DbJob.V_LEVEL_APP))
		//	has_anonym_projects = true;

		buttonList = new ArrayList<Map_JobButton>();
		buttonHandler = new ButtonHandler();

		// AppHelp
		butAppHelp = new JButton(app.getId()+"  _?"); // new JButton("? "+app.getId());
		butAppHelp.setToolTipText("Help about app '"+app.getId()+"'");
		applyButtonFormatAppInfo(butAppHelp);

		butAppHelp.addActionListener(buttonHandler);
		has_static_project = app.getJm().hasStaticPro();
		if ( !app.getJm().getHasAppHelp() )
			butAppHelp.setEnabled(false);		
		
		/*
		if ( app.hasIcon()) {
			icon = new ImageIcon(app.icon_file);
			label1 = new JLabel("Iccon");

		}
		*/
		
		// ProjectInfo
		butProjectInfo = new JButton("??"); 
		butProjectInfo.setToolTipText("Project-Status");
		
		applyButtonFormatProjectStatus(butProjectInfo);
		butProjectInfo.addActionListener(buttonHandler);

		
		if ( !app.getJm().getHasProjectInfo() ) {
			butProjectInfo.setEnabled(false);
			projectInfoLevel = 1;
		} else
			projectInfoLevel = app.getJm().getProjectInfoLevel();
		
		
		proButton = new JButton("Pro:");
		proButton.addActionListener(new ProjectButtonHandler());
		proButton.setToolTipText("Standard Project Commands");
		proButton.setEnabled(butProjectInfo.isEnabled());

		applyFormatAppGeneral(proButton);
		proButton.setMargin(new Insets(0,0,0,0));
		proButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel pi = new JPanel ( new BorderLayout());
		pi.add(butProjectInfo,BorderLayout.CENTER);
		pi.add(proButton,BorderLayout.WEST);
					
	
		JButton appButton = new JButton("App:");
		appButton.addActionListener(new ApplicationButtonHandler());
		appButton.setToolTipText("Standard Application Commands");
		
		applyButtonFormatAppStatus(butAppHelp);
		applyFormatAppGeneral(appButton);
		appButton.setMargin(new Insets(0,0,0,0));
		appButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel p1 = new JPanel ( new BorderLayout());
		p1.add(butAppHelp,BorderLayout.CENTER);
		p1.add(appButton,BorderLayout.WEST);
		compList.add(p1);
		compList.add(pi);
				
		
		Iterator<DbJob> jobIterator = jobList.iterator();
		while ( jobIterator.hasNext()) {
		
			DbJob curJob = (DbJob)jobIterator.next();
			if (curJob != null ) {
				String jobid = curJob.getJobid();
				JButton button = null;
				if ( style == STYLE_RUN_HELP)
					button = new JButton(jobid);
				else
					button = new JButton(jobid);	
				
				String tt = curJob.getToolTip();
				if ( tt!=null)
					button.setToolTipText(tt);
				else
					button.setToolTipText("Runs job '"+curJob.getJobid()+"'");
					
				JButton helpButton = new JButton("");

				helpButton.setFont(new Font("Arial",1 , 1));
				String tth = curJob.getToolTipHelp();
				if ( tth!=null)
					helpButton.setToolTipText(tth);
				else {
					if ( tt!=null)
						helpButton.setToolTipText("Help '"+curJob.getJobid()+"'. "+tt);
					else
						helpButton.setToolTipText("Help '"+curJob.getJobid()+"'");
				}

				applyButtonFormat(button,curJob.getPri());
				applyButtonFormatHelp(helpButton);

				if ( jobid.startsWith("View") || jobid.startsWith("Project Info") ) {
					applyButtonFormatAppInfo(button);
				}				
				
				Map_JobButton jobButton = new Map_JobButton(button,helpButton, curJob);
			
				buttonList.add(jobButton);

				JPanel x = new JPanel(new BorderLayout());
				x.add(button, BorderLayout.CENTER);
				if ( style == STYLE_RUN_HELP)
					x.add(helpButton,BorderLayout.EAST);
				
				try {
					compList.add(x);
					
					button.addActionListener(buttonHandler);
					helpButton.addActionListener(buttonHandler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Sp.pe(e);
				}
				
			} else
				compList.add(new JPanel());

		}		
		
		ArrayList<JComponent> compList2 = DbJm.arrangeJoblistGridLayout(compList, size);
		
		int sizeup = (jobList.size()+size+1)/size;
		lc.trace("size="+size+", sizeup="+sizeup);
		if ( type == TYPE_COLS )
			if ( sizeup == 1)
				panel = new JPanel(new GridLayout( sizeup,size ));
			else
				panel = new JPanel(new GridLayout( sizeup,size ));
		else
			if ( sizeup == 1)
				panel = new JPanel(new GridLayout( size,sizeup ));
			else
				panel = new JPanel(new GridLayout( size,sizeup ));
		  
		if ( componentOrientation != null )
		  panel.setComponentOrientation(componentOrientation);

		
		Iterator<JComponent> compIterator = compList2.iterator();
		while ( compIterator.hasNext()) {
		
			JComponent curComp = (JComponent)compIterator.next();
			if (curComp != null )
				panel.add(curComp);
			else
				panel.add( new JPanel());
		}
	}

	private void applyButtonFormat ( JButton b, int pri ) {

		if ( pri < 10)
			b.setFont(Tc_AppConsole.guiSizer.jobButtonFont);
		else
			b.setFont(Tc_AppConsole.guiSizer.jobButtonFontSmall);
		b.setHorizontalAlignment(SwingConstants.LEFT);
	
	}
	public static final Dimension prefferedHelpButtonDimension =new Dimension(36,12);
	public static final Dimension prefferedHelpButtonDimensionAppPro =new Dimension(72,12);

	private void applyButtonFormatHelp ( JButton b ) {
		b.setPreferredSize(prefferedHelpButtonDimension);
	}	
	
	private void applyFormatAppGeneral ( JButton b ) {
		Font dieser = Tc_AppConsole.guiSizer.buttonAppProTechInfo;
		b.setFont(Tc_AppConsole.guiSizer.buttonAppProTechInfo);
		b.setPreferredSize(prefferedHelpButtonDimensionAppPro);
		b.setForeground(Color.gray);
	}		
	
	private void applyButtonFormatProjectInfo ( JButton b ) {

		b.setFont(Tc_AppConsole.guiSizer.proInfoButtonFont);
		b.setHorizontalAlignment(SwingConstants.LEFT);
	}		
	
	private void applyButtonFormatAppInfo ( JButton b ) {	
		b.setFont(Tc_AppConsole.guiSizer.jobButtonViewFont);
		b.setForeground(Map_DbSelector.myBlue);
		b.setHorizontalAlignment(SwingConstants.LEFT);
	}
	private void applyButtonFormatProjectStatus ( JButton b ) {	
		b.setFont(Tc_AppConsole.guiSizer.proInfoFont);
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.setForeground(Map_DbSelector.myBlue);
	}	
	private void applyButtonFormatAppStatus ( JButton b ) {	
		b.setFont(Tc_AppConsole.guiSizer.proInfoFont);
		b.setHorizontalAlignment(SwingConstants.LEFT);
	}		
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}
	public JComponent getJComponent() {
		return ((JComponent)panel);		
	}
	
	public void bind() {
	
		Tc_UiContext currentContext = appConsole3.getCurrentContext();
		
		//JOptionPane.showMessageDialog (null, 
		//		 "kukuk binding app "+currentContext.getApp()+" "+currentContext.getInst());
		//;				
		Iterator<Map_JobButton> iterator = buttonList.iterator();
		String currentLevel = currentContext.getLevelOk();
		//nic123
		while ( iterator.hasNext()) {
			Map_JobButton jobentry = (Map_JobButton)iterator.next();
			jobentry.setEnabledPerLevelAppjobs(currentLevel,has_static_project);
		}			
		//if ( has_anonym_projects);
		//else {
			Map_JobButton.setPiEnabledPerLevelAppjobs(currentLevel, projectInfoLevel, butProjectInfo,has_static_project);
			proButton.setEnabled(butProjectInfo.isEnabled());
		//}

		if ( currentContext.getInst() != null ) { 
			butProjectInfo.setText(currentContext.getInst()+"  _??"); // ("? "+currentContext.getInst());
		} else if ( has_static_project) 
			butProjectInfo.setText("Project Info  _??"); // ("? "+"Anonym Project");
		else {
			butProjectInfo.setText("");
		}
		
		
	}

	public String getApp() {
		return app.getId();
	}
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e ){
			if ( (JButton)e.getSource() == butProjectInfo) {
				appConsole3.runProjectInfo();
			} else if ( (JButton)e.getSource() == butAppHelp) {
				appConsole3.runAppHelp(false);
			}  else if ( (JButton)e.getSource() == butProjectAdmin) {
				appConsole3.runProject();
			}else {

				Map_JobButton found = Map_JobButton.searchButton((JButton)e.getSource(),"run",buttonList);
				if ( found != null ) {
					// run
					//JOptionPane.showMessageDialog (null, 
					//		 "kukuk running job current "+found.job.getJobid())
					//;

					appConsole3.runJobCurrDb(found.job.getJobid(), Interpreter.SILENCE_NO);

				} else {
					found = Map_JobButton.searchButton((JButton)e.getSource(),"help",buttonList);
					
					if ( found != null ) {
						appConsole3.runAboutJob(found.job, false);					
					} 
				}				
			}
		}
	}


	
	public void start() {
		// TODO Auto-generated method stub
		
	}	
	
	private class ApplicationButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.info(" Application button pressed.");
			appConsole3.runSc("Application", appConsole3.getCurrentContext().getApp(),
					appConsole3.getCurrentContext().getInst() , 0,appConsole3.getCurrentContext().getInstDir());
		}
	}		
	private class ProjectButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.info(" Project button pressed.");
			appConsole3.runSc("Project", appConsole3.getCurrentContext().getApp(),
					appConsole3.getCurrentContext().getInst() , 0,appConsole3.getCurrentContext().getInstDir());
		}
	}		
}