/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_run;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import at.nicolas.x.int_call.AppJobexecution;
import at.nicolas.x.int_call.AppJobexecutionUsage;
import at.nicolas.x.int_call.AppSystemcall;
import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Interpreter;
import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CommStartMsg;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.CoreDummyTerminator;
import at.nicolas.x.int_specs.LogConnector;


public class JobConsoleGui {

	private LogConnector lc = new LogConnector ("JobConsole");
	private Container myContainer = null;
	private static JFrame jFrame;
	
	private JTextField je_app;
	private JTextField je_pro;
	private JTextField je_pro_dir;
	private JTextField je_job;
	
	private JTextField se_job;
	private JTextField se_app;
	private JTextField se_pro;
	
	private CoreDummyTerminator dummyTerminator;
	JobConsoleLogViewer jclv;
	
	
	public static JobConsoleGui getInstance() {
		return new JobConsoleGui();
	}
	
	public void run(CommStartMsg _sm) throws Exception  {
		// dreate logger
		jclv = new JobConsoleLogViewer();
		
		// Wrtiging initals to logger

		jclv.say(/*[echnaton-productname1]*/ "XmlRobot"		
				+": " +Wellknown.copyright);	
		
		dummyTerminator = new CoreDummyTerminator();
		// AppJobexecution, executes something existing
		// needs: appid, proid, jobid
		JPanel p1a = new JPanel();
		p1a.setLayout(new GridLayout(6,2));
		
		je_app = new JTextField();
		je_pro = new JTextField();
		je_pro_dir = new JTextField();
		je_job = new JTextField();
		//private JTextField je_pro;
		// private JTextField je_job;
		p1a.add(new JLabel("Workspace"));
		p1a.add(new JTextArea(CoreBuilder.ws.getWsId()));
		p1a.add(new JLabel("Takeoff"));
		p1a.add(new JTextArea(CoreBuilder.ws.getWsFilename()));		
		p1a.add(new JLabel("Application Id"));
		p1a.add(je_app);
		p1a.add(new JLabel("Project Id"));
		p1a.add(je_pro);
		p1a.add(new JLabel("Project Dir"));
		p1a.add(je_pro_dir);		
		p1a.add(new JLabel("Job Id"));
		p1a.add(je_job);
		
		// Button
		JButton je_run = new JButton("Run");
		je_run.addActionListener(new JeButtonHandler());
		
		// GesamtComposit
		JPanel p1b = new JPanel();
		p1b.setLayout(new BorderLayout());
		p1b.add(p1a,BorderLayout.CENTER);
		p1b.add(je_run,BorderLayout.SOUTH);
		
		// AppJobexecution, executes something existing
		// needs: appid, proid, jobid
		JPanel p2a = new JPanel();
		p2a.setLayout(new GridLayout(5,2));
		p2a.add(new JLabel("Workspace"));
		p2a.add(new JTextArea(CoreBuilder.ws.getWsId()));
		p2a.add(new JLabel("Takeoff"));
		p2a.add(new JTextArea(CoreBuilder.ws.getWsFilename()));
		
		se_app = new JTextField();
		se_pro = new JTextField();
		se_job = new JTextField();
		p2a.add(new JLabel("Job Id"));
		p2a.add(se_job);

		p2a.add(new JLabel("Application Id"));
		p2a.add(se_app);
		p2a.add(new JLabel("Project Id"));
		p2a.add(se_pro);
		
		// Button
		JButton p2Button = new JButton("Run");
		p2Button.addActionListener(new SeButtonHandler());
		// GesamtComposit
		JPanel p2b = new JPanel();
		p2b.setLayout(new BorderLayout());
		p2b.add(p2a,BorderLayout.CENTER);
		p2b.add(p2Button,BorderLayout.SOUTH);
		
		// Doing cards
		
		JTabbedPane tabpane = new JTabbedPane
	            (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
		
		tabpane.add("Application Job",p1b);
		tabpane.add("Service Job",p2b);
		
		// Doing Main
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(tabpane,BorderLayout.NORTH);	

		mainPanel.add(jclv.getJComponent(),BorderLayout.CENTER);
		
		jFrame = new JFrame();	
		jFrame.setPreferredSize(new Dimension(1000,700));
		jFrame.setTitle("XmlRobot Core" );
		jFrame.getContentPane().add(mainPanel);
		jFrame.pack();
		
		jFrame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  try {
		    		  lc.core("Window CLOSE .................",LogConnector.CORE_TERMINAL);
			    	  shutdown();
		    	  } catch (Exception e1) {
		    		  Sp.pe(e1);
		    	  }
		      	}
			});		

		
		_sm.presentLc(lc);	
		jclv.gototop();
		java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
			jFrame.setVisible(true);
		}
		});		
		
	}
	
	private void shutdown() throws Exception {
		Sp.p("Did shutdown");
		jFrame.dispose();
		System.exit(0);

	}
	
	private class JeButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
						
			String pro2use = null;
			if ( je_pro.getText().length() > 0 )
				pro2use = je_pro.getText();
			jclv.clear();
			
			if ( je_pro_dir.getText().length() == 0 ) {
				try {
					lc.info("Button JobExecution pressed: "+je_app.getText()+"-"+je_pro.getText()+"-"+je_job.getText());
					
					AppJobexecution x = new AppJobexecution(
							je_app.getText(),
							pro2use,
							je_job.getText(),
							null,
							false,
							Interpreter.SILENCE_NO,
							dummyTerminator,
							0,
							null); 	
					x.run();		

				} catch (Exception ex) {
					lc.warn("Some Exception in Job Execution: "+ex.getLocalizedMessage());
				}					
			} else {
				try {
					lc.info("Button JobExecution-Usage pressed: "+je_app.getText()+"-"+je_pro.getText()+"-"+je_pro_dir.getText()+"-"+je_job.getText());
					AppJobexecutionUsage x = new AppJobexecutionUsage(
							je_app.getText(),
							pro2use,
							je_pro_dir.getText(),
							je_job.getText(),
							null,
							false,
							Interpreter.SILENCE_NO,
							dummyTerminator, 
							0,
							null); 	
					x.run();		

				} catch (Exception ex) {
					lc.warn("Some Exception in Job Execution-Usage: "+ex.getLocalizedMessage());
				}					
			}
		
			
			

		}
	}		
		
	private class SeButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			lc.info("Button Service pressed: "+se_app.getText()+"-"+se_pro.getText()+"-"+se_job.getText());

			String pro2use = null;
			if ( se_pro.getText().length() > 0 )
				pro2use = se_pro.getText();
			
			String app2use = null;
			if ( se_app.getText().length() > 0 )
				app2use = se_app.getText();			
			
			jclv.clear();
			
			try {
				AppSystemcall y = new AppSystemcall ( 
						se_job.getText(),
			            app2use,
			            pro2use,
			            null,
			            Interpreter.SILENCE_YES,
			   			dummyTerminator,
						0,
						null );
							
				y.run();	

			} catch (Exception ex) {
				lc.warn("Some Exception: "+ex.getLocalizedMessage());
			}			

		}
	}		
}