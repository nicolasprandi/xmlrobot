/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.jdom2.Element;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_setup.CoreBuilder;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.ITiHandler;
import at.nicolas.x.int_specs.TkStopException;
import at.nicolas.x.term_core.Tc_AppConsole;


public class Tk_TkTextblock extends Tk_Tk implements Tk_IKeysOn{

	private Element tkN;
	private String idN;
	private String curValN = null;
	ITiHandler tih;
	String mainLabelN;
	String tipN;
	boolean nono;
	
	JButton okButton;
	JButton cancelButton;

	ButtonHandler buttonHandler;
	
	static String ok= new String("Ok");
	static String cancel= new String("No Value");
	
	
	public void built(Element tkN,ITiHandler tih,String mainLabelN, String tipN) throws Exception {
		this.tkN = tkN;
		this.tih = tih;
		this.mainLabelN = mainLabelN;
		this.tipN = tipN;
		idN = tkN.getAttributeValue("id");
		curValN =  Tk_TkStatic.getDefaultValue(tkN,getVarType(), idN, tih);
		this.nono = Commons.hasFlag(tkN, "nono");

	}

	public String getVarType() {
		return ("prop");
	}

	public String getVarIdN() {
		return idN;
	}
	boolean did_a_cancel;
	public void execute(boolean first_go) throws Exception {
		did_a_cancel = false;
		int w = 500;
		int h = 200;
		int r = 10;
		if ( Commons.hasFlag(tkN, "big")) {
			w = 900;
			h = 700;
			r=30;
		}		
		JTextArea textarea = new JTextArea(curValN,r,50);
		JScrollPane scroll = new JScrollPane(textarea);
		textarea.setBackground(Color.LIGHT_GRAY);
		textarea.setFont(Tc_AppConsole.guiSizer.tkFont);
		
		JButton okButton = Tk_TkStatic.getJButton(ok);
		JButton cancelButton = Tk_TkStatic.getJButton(cancel);
		
		int sizeadder = Tk_TkStatic.getSizeAdder();
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		if ( !nono )
			buttonPanel.add(cancelButton);		

		JLabel advice = new JLabel ("<html><body><br>Use ctrl+enter to perform an 'ok' by key</body></html>");

		advice.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(buttonPanel,BorderLayout.CENTER);
		p2.add(advice,BorderLayout.SOUTH);
		
		JPanel panel = Tk_TkStatic.getPanel(tkN.getAttributeValue("label"), scroll, p2,tipN);
		
		jDialog = new JDialog();
		jDialog.setContentPane(panel);
		
		jDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing ( WindowEvent e) {		
				wcTrue();
			}
			
		});
		buttonHandler = new ButtonHandler();
		okButton.addActionListener(buttonHandler);
		cancelButton.addActionListener(new ClearButtonHandler());
		//textarea.setCaretPosition(textarea.getDocument().getLength());
		textarea.setCaretPosition(0);
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				int code = ke.getKeyCode();  
				int modifiers = ke.getModifiers();  
				if(code == KeyEvent.VK_ENTER && modifiers == KeyEvent.CTRL_MASK && keys_on&& (ke.getWhen()>keys_on_start)) {
					wcFalse();
					dispose();
				}
			}			
		};	
		textarea.addKeyListener(keyAdapter);

		
		Tk_TkStatic.executeJDialog(jDialog, mainLabelN,Tk_TkStatic.getLabelOrId(tkN),new Dimension(w,h),this,null,w);
		
		if ( windowCanceled )
			throw new TkStopException(Tk_TkStatic.getQuitMessage(tkN));
		
		if ( !did_a_cancel)
		curValN = textarea.getText();

		if ( curValN == null ) 
			throw new Exception("Problem #79, TkTextblock received null as value");

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
					dispose();
				} else if ( buttonText.equals(cancel) ) {
					Sp.p("im buttonhandler,cancel");
					wcTrue();
					dispose();
						
				}				
			}


		}
	}
	@Override
	public String getValueN() {
		return curValN;
	}
	
	public String getValuePN() {
		String retour = curValN;
		if ( retour.length() > 90 ) {
			retour = curValN.substring(0, 89) +" ...!";
		}
		return retour;
	}	
	public void clearValue() {
		// TODO Auto-generated method stub
		curValN = "";
		
	}	
	public void clearValue2() {
		// TODO Auto-generated method stub
		curValN = "";
		did_a_cancel = true;
		
	}	
	private class ClearButtonHandler implements ActionListener {
		
		public void actionPerformed ( ActionEvent e )
		{

					clearValue2();
					wcFalse();
					dispose();


		}	
	}
	@Override
	public boolean hasNono() {
		// TODO Auto-generated method stub
		return nono;
	}	
	
	@Override
	public  synchronized void setValueN(String value) {
		curValN = value;	
	}
	public String getValue4RegexN() {
		return getValueN();
	}
}