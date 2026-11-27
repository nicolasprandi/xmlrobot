/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Tk_MyMessageDialog implements Tk_IKeysOn {

	private String header;
	private String msg;
	boolean windowCanceled = false;
	
	JButton okButton;
	JButton cancelButton;
	JDialog jDialog;
	JButton focusButton = null;
	ButtonHandler buttonHandler;
	
	String ok= new String("Ok");
	String cancel= new String("Cancel");
	String topheader = new String("Message");
	
	int focus = 0;
	
	public void built(String header, String msg ) {
		this.header = header;
		this.msg = msg;

	}

	public void setFocusToOkButton() {
		focus = 1;
	}
	public void setFocusToCancelButton() {
		focus = 2;
	}	
	
	public void setOkText(String okText) {
		ok = okText;
	}
	
	public void setCancelText(String cancelText) {
		cancel = cancelText;
	}	
	
	public void setTopHeaderText(String topHeaderText) {
		topheader = topHeaderText;
	}	

	public boolean execute() throws Exception {
		
		// JTextField textField = new JTextField(msg);
		JTextArea textField = new JTextArea(msg);
		textField.setEditable(false);		
		
		okButton = Tk_TkStatic.getJButton(ok);
		// okButton.setPreferredSize(new Dimension(300,300));
		cancelButton = Tk_TkStatic.getJButton(cancel);
		if ( focus == 1) 
			focusButton = okButton;
		else if ( focus == 2) 
			focusButton = cancelButton;
					
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);		
		
		JPanel panel = Tk_TkStatic.getPanel(header, textField, buttonPanel,null);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				windowCanceled =true;				
			}
			
		});
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(buttonHandler);
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				
				if ( ke.getKeyCode() == KeyEvent.VK_ENTER && 
						keys_on && 
						(ke.getWhen()>keys_on_start)) {
					if ( ke.getSource() == okButton) {
						windowCanceled=false;
						jDialog.dispose();						
					} else if ( ke.getSource() == cancelButton) {
						windowCanceled=true;
						jDialog.dispose();						
					}


				}
			}
			
		};	
		
		textField.addKeyListener(keyAdapter);
		
		Tk_TkStatic.executeJDialog(jDialog, topheader,header,new Dimension(500,200),this,focusButton,300);
		Thread.sleep(100);
		return !windowCanceled;
	}
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			if ( e.getSource() == okButton) {
				windowCanceled=false;
				jDialog.dispose();						
			} else if ( e.getSource() == cancelButton) {
				windowCanceled=true;
				jDialog.dispose();						
			}
			/*
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {
					windowCanceled =false;
					jDialog.dispose();
				} else if ( buttonText.equals(cancel) ) {
					windowCanceled =true;
					jDialog.dispose();						
				}				
			}
			*/


		}
	}
	
	boolean keys_on = false;
	long    keys_on_start = 0;
	public void keysOn()  {
		keys_on = true;		
		keys_on_start = System.currentTimeMillis()+40;
	}	

}