/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_setup;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_interpreter.Sp;

public class CommWelcome extends AppenderSkeleton{
	
	private JFrame jFrame = null;
	private JPanel panel = null;
	private TextArea textarea = null;
	private String appenderId = null;
	public CommWelcome( )  {
		
		JLabel a = new JLabel(Wellknown.copyright);
		
		textarea = new TextArea();
		textarea.setEditable(false);
		textarea.setFont(new Font("Courier",0 , 12));
		
		panel = new JPanel( );
		panel.setLayout(new BorderLayout());
		panel.add(a,BorderLayout.NORTH);
		panel.add(textarea,BorderLayout.CENTER);
		
		jFrame = new JFrame();
		jFrame.setSize(1200, 900);
		jFrame.setLocation(30, 10);
		jFrame.setTitle("XmlRobot Workspace" );
		// jFrame.setDefaultCloseOperation(JFrame. .EXIT_ON_CLOSE);
		
		jFrame.setContentPane(panel);
		jFrame.setVisible(true);
				
		appenderId = CoreBuilder.rslogger.addAppender(this);
		
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
		// TODO Auto-generated method stub
		String prefix = arg0.getLevel().toString().substring(0,1);
		if (prefix.equals("I"))
			prefix = " ";
		String meldung = prefix+" "+ arg0.getRenderedMessage()+"\n";
		textarea.append(meldung);
		
	}	
	
	public void printLog( String x ) {
		textarea.append(x);
	}

	public void closeWindow() {
		try {
			CoreBuilder.rslogger.removeAppender(appenderId);
			jFrame.dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Sp.pe(e);
		}
	}
}