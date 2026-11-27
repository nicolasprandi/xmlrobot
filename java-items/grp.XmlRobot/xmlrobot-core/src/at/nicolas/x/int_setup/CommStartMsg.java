/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import at.nicolas.x.int_specs.LogConnector;

public class CommStartMsg {

	JButton jbContinue;
	JButton jbStop;
	private boolean continueAfterPresentWindow = false;
	private ByteArrayOutputStream outputStream;
	public PrintStream out;
	JDialog jFrame;
	ButtonHandler buttonHandler = null;
	TextArea textarea;
	
	public CommStartMsg() {
		outputStream = new ByteArrayOutputStream();
		out = new PrintStream(outputStream);
	}
	public CommStartMsg(PrintStream printStream) {
		out = printStream;
	}	
	
	ArrayList<String> importants = new ArrayList<String>();
	public void addImportant ( String importantMessage) {
		importants.add(importantMessage);
	}
	
	public void printImportants () {
		Iterator<String> i = importants.iterator();
		while ( i.hasNext()) {
			String next = (String)i.next();
			out.println("***************************************************************************");
			out.println("** "+next);
			out.println("***************************************************************************");
		}
	}	
	public boolean presentWindow( Exception e ) {
		buttonHandler = new ButtonHandler();
		jbContinue = new JButton("Try Again");
		jbContinue.addActionListener(buttonHandler);
		jbStop = new JButton("Quit");
		jbStop.addActionListener(buttonHandler);
		textarea =	new TextArea();
		textarea.setEditable(false);
		textarea.setFont(new Font("Courier",0 , 12));
		
		JPanel panel = new JPanel( );
		panel.setLayout(new BorderLayout());
		panel.add(textarea,BorderLayout.CENTER);
		
		JPanel butPanel = new JPanel(new GridLayout(1,2));
		butPanel.add(jbStop);
		butPanel.add(jbContinue);
		butPanel.setPreferredSize(new Dimension(100,100));
		panel.add(butPanel,BorderLayout.NORTH);
		
		
		jFrame = new JDialog();
		jFrame.setSize(1200, 900);
		jFrame.setLocation(30, 10);
		jFrame.setTitle("XmlRobot Initial-Phase" );
		// jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setContentPane(panel);
		
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				continueAfterPresentWindow =false;
				// jFrame.setVisible(false);
				jFrame.dispose();				
			}
			
		});

		out.flush();
			
		if ( e!= null ) {
			textarea.append("\nMessage: \n\n  " +e.getLocalizedMessage()+"\n\n");		
			textarea.append("ExceptionClass: "+ e.getClass().getCanonicalName()+"\n");
	        StackTraceElement[] trace = e.getStackTrace();
	        for(int i = 0; i<trace.length; i++){
	        	textarea.append("  "+trace[i].toString()+"\n");
	        }	
	        // textarea.append("\nMessage: \n\n  " +e.getLocalizedMessage()+"\n\n");
	        
	        	
		}
		textarea.append(outputStream.toString());
		textarea.append("\n");
		// importants
		Iterator<String> i = importants.iterator();
		while ( i.hasNext()) {
			String next = (String)i.next();
			textarea.append("!! "+next+"\n");
		}
		jFrame.setModal(true);
		jFrame.setVisible(true);
		
		return ( continueAfterPresentWindow);

	}
	public void presentLc( LogConnector lc ) {
		lc.info(outputStream.toString());
		
	}
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{

			if ( (JButton)e.getSource() == jbContinue) {
				continueAfterPresentWindow =true;
				jFrame.setVisible(false);
				jFrame.dispose();								
			} else if ( (JButton)e.getSource() == jbStop) {
				continueAfterPresentWindow =false;
				jFrame.setVisible(false);
				jFrame.dispose();
								
								
			}
		}
	}
	

}