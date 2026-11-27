/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdom2.Element;

import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.LogConnector;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.term_core.Tc_AppConsole;



public class Tk_TkMsg extends Tk_Tk implements Tk_IKeysOn{

	private Element tk;
	private String id;

	ITiHandler tih;
	String mainLabel;
	String tip;
	String message = null;
	String t;
	
	JButton okButton;
	JButton cancelButton;
	

	ButtonHandler buttonHandler;
	ButtonHandlerSpecial buttonHandlerSpecial;
	
	JButton specialButton;
	
	static String ok= new String("Ok");
	static String okTablet= new String("Ok");
	static String cancel= new String("Stop");
	
	
	
	private boolean do_special = false;
	String special = null;
	
	boolean finished_with_special = false;
	
	boolean isCopypasteMessage = false;
	
	
	public void built(Element tk,ITiHandler tih,String mainLabel, String tip) {
		this.tk = tk;
		this.tih = tih;
		this.mainLabel = mainLabel;
		this.tip = tip;
		id = tk.getAttributeValue("id");
		
		if ( tk.getAttribute("msg") != null ) {
			message = tk.getAttributeValue("msg");
			LogConnector lc = new LogConnector("tk-message");
			lc.warn("Attribute 'msg' of tk-msg is depressive, use attribute 'x' or text()");
		} else if ( tk.getAttribute("x") != null ) 
			message = tk.getAttributeValue("x");
		else
			message = tk.getText();
		
		String msg_type = tk.getAttributeValue("msg_type");
		if ( msg_type == null )
			msg_type = "i";
		else if ( msg_type.startsWith("c"))
			isCopypasteMessage = true;  
		
		
		if ( msg_type.startsWith("e"))
			t = "E r r o r";
		else if ( msg_type.startsWith("w"))
			t = "W a r n i n g";
		else if  ( msg_type.startsWith("i"))
			t = "Information";
		else if ( msg_type.startsWith("c") && Tk_TkStatic.getLabel(tk)!=null) // copy paste
			t = Tk_TkStatic.getLabel(tk);
		else 
			t = "Information";		
		
		if ( tk.getAttribute("special") != null  ) {
			LogConnector lc = new LogConnector("tk-message");
			lc.warn("Attribute 'special' of tk-msg is depressive, tk-type 'sel'");
			/*
			do_special=true;
			special = tk.getAttributeValue("special");
			*/
			
		}

	}

	public String getVarTypeN() {
		return ("prop");
	}

	public String getVarIdN() {
		return id;
	}

	public String getDefaultValueN() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public void execute(boolean first_go) throws Exception {

		JTextArea textField = new JTextArea(message);
		textField.setFont(Tc_AppConsole.guiSizer.dbFont);
		textField.setEditable(false);
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setMargin(new Insets(10,10,10,10));
		
		if ( tk.getAttribute("cols") != null )
			textField.setColumns(Commons.string2int(tk.getAttributeValue("cols")));
		
		if ( Commons.hasFlag(tk, "wrap"))
			textField.setLineWrap(true);		
		else if ( Commons.hasFlag(tk, "wordwrap")) {
			textField.setLineWrap(true);
			textField.setWrapStyleWord(true);	
		}
		
		if ( isCopypasteMessage )
			textField.select(0, message.length());
		
		
		JButton okButton =  Tk_TkStatic.getJButton(ok);
		
		JButton cancelButton =  Tk_TkStatic.getJButton(cancel);
		

		JButton specialButton;
		
		int sizeadder = Tk_TkStatic.getSizeAdder();
		//int sizeadder = 0;
		
		JPanel buttonPanel;
		
		if ( do_special == false) {
			buttonPanel= new JPanel();
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);		
		} else {
			specialButton = Tk_TkStatic.getJButton(special);
			JPanel firstPanel = new JPanel();
			firstPanel.add(okButton);
			firstPanel.add(cancelButton);	
			
			buttonPanel = new JPanel(new BorderLayout());
			buttonPanel.add(firstPanel,BorderLayout.NORTH);
			buttonPanel.add(specialButton,BorderLayout.CENTER);
			buttonHandlerSpecial = new ButtonHandlerSpecial();
			specialButton.addActionListener(buttonHandlerSpecial);
		}
	

		JScrollPane scroll = new JScrollPane(textField);
		JPanel panel = Tk_TkStatic.getPanel(t, scroll, buttonPanel,true,tip);
		// 
		// Dimension sd = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		// Dimension md = new Dimension ( sd.width-100, sd.height-300);
		
		
		jDialog = new JDialog();
		// jDialog.setMaximumSize(md);
		// jDialog.pack();
		
		jDialog.setContentPane(panel);
		
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {
				// windowCanceled =true;	
				wcTrue();
			}
			
		});
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(new CancelButtonHandler());
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (  ke.getKeyCode() == KeyEvent.VK_ENTER  && keys_on&& (ke.getWhen()>keys_on_start)) {

					wcFalse();
					dispose();
				}
			}
			
		};	
		
		textField.addKeyListener(keyAdapter);
		finished_with_special = false;
		Tk_TkStatic.executeJDialog(jDialog, mainLabel,null,new Dimension(500,200+sizeadder),this,null,400); //new Dimension(500,200)
		
		if ( windowCanceled ) {
			if (  windowStopped ) {
				throw new Exception("Stopped: "+message);
			} else
				throw new TkStopException("Message stopped: "+message);
		}
		
		if ( do_special ) {
			if ( finished_with_special ) {
				tih.setVar("prop", "tk_msg_special", "1");
			} else
				tih.setVar("prop", "tk_msg_special", "0");
		}
		
		// JOptionPane.showMessageDialog (null, msg, "Information", t);
		
	}
	
	private class ButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			Object source = e.getSource();
			String cn = source.getClass().getName();
			
			if ( cn.equals("javax.swing.JButton")) {
				// JButton pressed = (JButton)source;
				String buttonText = ((JButton)source).getText();
				
				if ( buttonText.equals(ok) ) {
					wcFalse();
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					wcTrueStop();
					dispose();
						
				}				
			}

		}
	}
	
	private class ButtonHandlerSpecial implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
				wcFalse();
				pressedSpecial();
				dispose();		
		

		}
	}	
	
	public synchronized void pressedSpecial() {
		finished_with_special=true;		
	}	
	volatile boolean  windowStopped = false;
	public synchronized void wcTrueStop() {
		wcTrue();
		windowStopped =true;		
	}	
	private class CancelButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{
			wcTrue();
			dispose();

		}
	}	
	@Override
	public String getValueN() {
		return message;
	}
	
	public String getValuePN() {
		return message;
	}	
	public void clearValue() {
		// TODO Auto-generated method stub

		
	}

	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return false;
	}	
	
	@Override
	public  synchronized void setValueN(String value) {
	}

	@Override
	public String getVarType() {
		// TODO Auto-generated method stub
		return "";
	}
	public String getValue4RegexN() {
		return getValueN();
	}	
}