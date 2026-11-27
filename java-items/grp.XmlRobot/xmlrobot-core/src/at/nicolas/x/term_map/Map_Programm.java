/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdom2.Element;

import at.nicolas.x.int_db.DbJob;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.term_core.Tc_AppConsole;
import at.nicolas.x.term_core.Tc_UiContext;
import at.nicolas.x.term_core.Tc_UiMap;

public class Map_Programm implements Tc_UiMap {
	
	public static Map_Programm single = null;
	private Tc_UiContext lastContext = new Tc_UiContext();

	TextArea   jobdokumentation = null;
	JPanel panel = null;
	Tc_AppConsole appConsole3 = null;
	boolean updateTerminalLogger = false;
		
	public Map_Programm() {
		single = this;
		jobdokumentation = new TextArea();
		jobdokumentation.setBackground(Color.white);
		jobdokumentation.setEditable(false);
		Font font2 = new Font("Courier",0,12);
		jobdokumentation.setFont(font2);
		panel = new JPanel(new BorderLayout());
		panel.add(jobdokumentation,BorderLayout.CENTER);
	}
	
	public static void singlePrint ( DbJob job ) {
		if ( single != null )
			single.print(job);
	}
	
	public void print ( DbJob job ) {
		String s = 	job.getJobText() + "\n\n"+
					"Located in: "+job.getFullfn();

		jobdokumentation.setText(s);
		
		Element tl = new Element("ti");		
		Element y = new Element("ti");
		y.setAttribute("type", "file");
		y.setAttribute("doku", "Jobfile with '"+ job.getJobidEscaped()+"':");
		y.setText(job.getFullfnEscaped());
		tl.addContent(y);	

	}
	public void setUpdateTerminalLogger ( boolean updateTerminalLogger ) {
		this.updateTerminalLogger = updateTerminalLogger;
	}
	
	public void setMapSetMaster(Tc_AppConsole _appConsole3) {
		appConsole3 = _appConsole3;

	}

	public void bind() {
		

		Tc_UiContext newUiContext = appConsole3.getCurrentContext();
		if ( Tc_UiContext.changedJob(lastContext,newUiContext)) {
			// neues Jobfile	
			if ( newUiContext.haveJob()) {
				// print(Conf.db.getAppById(newUiContext.getApp()).getJm().getJobOfJoblistTerminal(newUiContext.getJob()));
				/*
				// Sp.p("... job selected");
				jobdokumentation.setText("job selected");
				
				String header = "App: " + newUiContext.getApp() +"\n\n";
				
				// String dokumentation = header + Interpreter.getExecutionDokumentation(jobfile,newUiContext.getJob());
				String dokumentation = 	
						Conf.db.getAppById(newUiContext.getApp()).getJm().getJobOfJoblistTerminal(newUiContext.getJob()).getJobText();


				jobdokumentation.setText(dokumentation);
				
				if ( updateTerminalLogger ) {
					TerminalLogger.singleAddText(Programm.makeHtmlEntities(dokumentation));
				}
				// jobdokumentation.setCaretPosition( 0 );	
				 * */			

			} else {
				// Sp.p("... no job selected");
				// jobdokumentation.setText("no job selected");
			}
		} else {
			// Sp.p("... job not changed");			
		}
		
		lastContext = newUiContext.cloneMe();
		// Sp.p("Programm.bind().end");
		// Sp.p("     new lastContext="+lastContext.getDesc());

	}

	public JComponent getJComponent() {
		// TODO Auto-generated method stub
		
		return (JComponent)panel;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	public static String makeHtmlEntities ( String in ) {
		String out = new String(in);
		out = Commons.stringReplace(out, "<", "&lt;");
		out = Commons.stringReplace(out, ">", "&gt;");
		out = Commons.stringReplace(out, "\n", "<br/>");
		out = Commons.stringReplace(out, " ", "&nbsp;");
		return out;
	}

}