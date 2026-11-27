/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_run;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import at.nicolas.x.int_setup.CoreBuilder;

public class JobConsoleLogViewer   extends AppenderSkeleton {
	
	private JScrollPane jsp =null;
	private JPanel    panel = null;
	private JTextArea textarea = null;
	
	public JobConsoleLogViewer() {
	
		
		textarea = new JTextArea();
		textarea.setEditable(false);
		// textarea.setFont(fntSmall);
		textarea.setBackground(Color.LIGHT_GRAY);
		jsp = new JScrollPane ( textarea );
		
		/*
		panel = new JPanel( );
		
			panel.setLayout(new BorderLayout());
			panel.add(jsp, BorderLayout.CENTER);	
		*/
			
		textarea.append(CoreBuilder.rslogger.getLevel().toString()+"\n");	
		CoreBuilder.rslogger.addAppender(this);
	}
	
	public void say ( String that ) {
		textarea.append(that+"\n");	
	}
	public void gototop() {
		textarea.setCaretPosition(0);
	}
	
	public JComponent getJComponent() {

		return (JComponent)jsp;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent arg0) {

		String meldung;
		if ( arg0.getLevel() == Level.INFO )
			meldung = "  "+ arg0.getRenderedMessage()+"\n";
		else
			meldung = (arg0.getLevel().toString().substring(0,1)).toLowerCase()+" "+ arg0.getRenderedMessage()+"\n";

		textarea.append(meldung);
		textarea.setCaretPosition(textarea.getText().length());
		
		
	}
	
	public void clear () {
		textarea.setText("");
		textarea.setCaretPosition(0);
	}	

}